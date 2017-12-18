package com.backendless.rt;

import com.backendless.Backendless;
import com.backendless.rt.data.RTDataEvents;
import io.socket.emitter.Emitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

class RTClientSocketIO implements RTClient
{

  private static final Logger logger = Logger.getLogger( "RTClient" );

  private Map<String, RTSubscription> subscriptions = new ConcurrentHashMap<>();

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

      }

      @Override
      void invocationResult( Object... args )
      {

      }
    };

  }

  @Override
  public void subscribe( RTSubscription subscription )
  {
    logger.info( "try to subscribe " + subscription );
    subscriptions.put( subscription.getId(), subscription );

    subOn( subscription );

  }

  private Emitter subOn( RTSubscription subscription )
  {
    final Emitter emitter = connectionManager
            .get()
            .emit( "SUB_ON", subscription.getId(), subscription.getSubscriptionName(), subscription.getOptions().toArray() );

    logger.info( "subOn called" );
    return emitter;
  }

  private Emitter subOff( String subscriptionId )
  {
    final Emitter emitter = connectionManager.get().emit( "SUB_OFF", subscriptionId );
    logger.info( "subOff called" );
    return emitter;
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

    if( subscriptions.size() == 0 )
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

  }

  private void resubscribe()
  {
    for( RTSubscription rtSubscription : subscriptions.values() )
    {
      subOn( rtSubscription );
    }
  }
}