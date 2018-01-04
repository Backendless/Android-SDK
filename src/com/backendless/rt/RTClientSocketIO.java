package com.backendless.rt;

import com.backendless.async.message.AsyncMessage;
import com.backendless.core.ResponseCarrier;
import com.backendless.exceptions.BackendlessFault;
import io.socket.emitter.Emitter;
import weborb.reader.AnonymousObject;
import weborb.types.IAdaptingType;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;

import static com.backendless.utils.WeborbSerializationHelper.*;

class RTClientSocketIO implements RTClient
{
  private static final Logger logger = Logger.getLogger( "RTClient" );

  private Map<String, RTSubscription> subscriptions = new ConcurrentHashMap<>();

  private Map<String, RTMethodRequest> sentRequests = new ConcurrentHashMap<>();

  private ConcurrentLinkedDeque<RTMethodRequest> methodsToSend = new ConcurrentLinkedDeque<>();

  private final SocketIOConnectionManager connectionManager;

  RTClientSocketIO( )
  {
    this.connectionManager = new SocketIOConnectionManager()
    {
      @Override
      void connected()
      {
        resubscribe();
      }

      @Override
      void subscriptionResult( Object... args )
      {
        logger.info( "subscription result " + Arrays.toString( args ) );
        handleResult( args, subscriptions );
      }

      @Override
      void invocationResult( Object... args )
      {
        logger.info( "invocation result " + Arrays.toString( args ) );
        RTRequest request = handleResult( args, sentRequests );

        if( request != null )
        {
          sentRequests.remove( request.getId() );
        }
      }
    };
  }

  private RTRequest handleResult( Object[] args, Map<String, ? extends RTRequest> requestMap )
  {
    if( args == null || args.length < 1 )
    {
      logger.warning( "subscription result is null or empty" );
      return null;
    }

    AnonymousObject result = (AnonymousObject) deserialize( args[ 0 ] );

    String id = asString( result, "id" );

    logger.info( "Got result for subscription " + id );

    RTRequest request = requestMap.get( id );

    if( request == null )
    {
      logger.info( "There is no handler for subscription " + id );
      return null;
    }

    final Object error = asObject( result, "error" );

    if( error != null )
    {
      logger.info( "got error " + error.toString() );
      final BackendlessFault fault = new BackendlessFault( error.toString() );
      ResponseCarrier.getInstance().deliverMessage( new AsyncMessage<>( fault, request.getCallback() ) );
      return request;
    }

    IAdaptingType data = asAdaptingType( result, "data" );
    ResponseCarrier.getInstance().deliverMessage( new AsyncMessage<>( data, request.getCallback() ) );

    return request;
  }

  @Override
  public void subscribe( RTSubscription subscription )
  {
    logger.info( "try to subscribe " + subscription );
    subscriptions.put( subscription.getId(), subscription );

    if( connectionManager.get().connected() )
      subOn( subscription );

  }

  @Override
  public void unsubscribe( String subscriptionId )
  {
    logger.info( "unsubscribe for " + subscriptionId + " subscrition called"  );

    if( !subscriptions.containsKey( subscriptionId ) )
    {
      logger.info( "subscriber "+ subscriptionId +" is not subscribed" );
      return;
    }

    subOff( subscriptionId );
    subscriptions.remove( subscriptionId );
    logger.info( "subscription removed" );

    if( subscriptions.isEmpty() && sentRequests.isEmpty() )
    {
      connectionManager.disconnect();
    }
  }

  @Override
  public void userLoggedIn( String userToken )
  {

  }

  @Override
  public void userLoggedOut()
  {

  }

  @Override
  public void invoke( RTMethodRequest methodRequest )
  {
    sentRequests.put( methodRequest.getId(), methodRequest );
    if( connectionManager.isConnected() )
    {
      metReq( methodRequest );
    }
    else
    {
      methodsToSend.addFirst( methodRequest );
    }

  }

  private void resubscribe()
  {
    for( RTSubscription rtSubscription : subscriptions.values() )
    {
      subOn( rtSubscription );
    }

    RTMethodRequest methodRequest = methodsToSend.poll();
    while( methodRequest != null )
    {
      metReq( methodRequest );
      methodRequest = methodsToSend.poll();
    }
  }

  private Emitter subOn( RTSubscription subscription )
  {
    final Emitter emitter = connectionManager.get().emit( "SUB_ON", serialize( subscription.toArgs() ) );

    logger.info( "subOn called" );
    return emitter;
  }

  private Emitter subOff( String subscriptionId )
  {
    final Emitter emitter = connectionManager.get().emit( "SUB_OFF", serialize( subscriptionId ) );
    logger.info( "subOff called" );
    return emitter;
  }

  private Emitter metReq( RTMethodRequest methodRequest )
  {
    final Emitter emitter = connectionManager.get().emit( "MET_REQ", serialize( methodRequest.toArgs() ) );

    logger.info( "subOn called" );
    return emitter;
  }
}