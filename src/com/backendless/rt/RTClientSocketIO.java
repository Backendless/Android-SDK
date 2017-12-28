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
import java.util.logging.Logger;

import static com.backendless.utils.WeborbSerializationHelper.*;

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
        logger.info( "subscription result " + Arrays.toString( args ) );

        if( args == null || args.length < 1 )
        {
          logger.warning( "subscription result is null or empty" );
          return;
        }

        AnonymousObject result = (AnonymousObject) deserialize( args[ 0 ] );

        String id = asString( result, "id" );

        logger.info( "Got result for subscription " + id );

        RTSubscription subscription = subscriptions.get( id );

        if( subscription == null )
        {
          logger.info( "There is no handler for subscription " + id );
          return;
        }

        final Object error = asObject( result, "error" );

        if( error != null )
        {
          logger.info( "got error " + error.toString() );
          final BackendlessFault fault = new BackendlessFault( error.toString() );
          ResponseCarrier.getInstance().deliverMessage( new AsyncMessage<>( fault, subscription.getCallback() ) );
          return;
        }

        IAdaptingType data = asAdaptingType( result, "data" );
        ResponseCarrier.getInstance().deliverMessage( new AsyncMessage<>( data, subscription.getCallback() ) );
      }

      @Override
      void invocationResult( Object... args )
      {
        logger.info( "invocation result " + Arrays.toString( args ) );
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