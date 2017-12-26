package com.backendless.rt;

import com.backendless.Backendless;
import com.backendless.persistence.local.UserTokenStorageFactory;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.logging.Logger;

abstract class SocketIOConnectionManager
{
  private static final Logger logger = Logger.getLogger( "SocketIOConnectionManager" );
  private static final RTLookupService rtLookupService = new RTLookupService();
  private static final int INITIAL_TIMEOUT = 100;

  private final Object lock = new Object();

  private int retryConnectTimeout = INITIAL_TIMEOUT;
  private Socket socket;

  Socket get()
  {
    if( socket != null || isConnected() )
    {
      logger.info( "Socket is connected" );
      return socket;
    }

    logger.info( "Socket not connected. Try to get lock" );

    synchronized( lock )
    {
      logger.info( "Got lock" );

      if( socket != null || isConnected() )
      {
        logger.info( "Socket is connected" );
        return socket;
      }

      try
      {
        final IO.Options opts = new IO.Options();
        opts.reconnection = false;

        opts.path = "/" + Backendless.getApplicationId();

        opts.query = "apiKey=" + Backendless.getSecretKey() + "&binary=true";

        final String host = rtLookupService.lookup() + opts.path;
        logger.info( "Looked up for server " + host );

        String userToken = UserTokenStorageFactory.instance().getStorage().get();
        if( userToken != null && !userToken.isEmpty() )
          opts.query += "&userToken=" + userToken;

        socket = IO.socket( host, opts );
        logger.info( "Socket object created" );
      }
      catch( URISyntaxException e )
      {
        return handleConnectionFailed();
      }

      socket.on( Socket.EVENT_CONNECT, new Emitter.Listener()
      {
        @Override
        public void call( Object... args )
        {
          logger.info( "Connected event" );
          retryConnectTimeout = INITIAL_TIMEOUT;
          connected();
        }
      } ).on( Socket.EVENT_DISCONNECT, new Emitter.Listener()
      {
        @Override
        public void call( Object... args )
        {
          logger.info( "Disconnected event" );
          handleConnectionFailed();
        }
      } ).on( Socket.EVENT_CONNECT_ERROR, new Emitter.Listener()
      {
        @Override
        public void call( Object... args )
        {
          logger.severe( "Connection failed " + Arrays.toString( args ) );
          handleConnectionFailed();
        }
      } ).on( "SUB_RES", new Emitter.Listener()
      {
        @Override
        public void call( Object... args )
        {
          logger.info( "Got sub res" );
          subscriptionResult( args );
        }
      } ).on( "MET_RES", new Emitter.Listener()
      {
        @Override
        public void call( Object... args )
        {
          logger.info( "Got met res" );
          invocationResult( args );
        }
      } );

      socket.connect();
    }

    return socket;
  }

  private Socket handleConnectionFailed()
  {
    disconnect();
    logger.info( "Wait for " + retryConnectTimeout + " before reconnect" );
    try
    {
      Thread.sleep( retryConnectTimeout );
    }
    catch( InterruptedException e1 )
    {
      throw new RuntimeException( e1 );
    }

    retryConnectTimeout *= 2;
    return get();
  }

  void disconnect()
  {
    logger.info( "Try to disconnect" );
    synchronized( lock )
    {
      if( socket != null )
        socket.close();

      socket = null;
    }
  }

  private boolean isConnected()
  {
    return socket != null && socket.connected();
  }

  abstract void connected();

  abstract void subscriptionResult( Object... args );

  abstract void invocationResult( Object... args );
}
