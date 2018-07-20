package com.backendless.rt.command;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.rt.RTCallback;
import com.backendless.rt.RTClient;
import com.backendless.rt.RTClientFactory;
import com.backendless.rt.RTMethodRequest;
import com.backendless.rt.RTSubscription;
import com.backendless.rt.users.UserInfo;
import com.backendless.utils.WeborbSerializationHelper;
import weborb.exceptions.AdaptingException;
import weborb.types.IAdaptingType;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class CommandListener<T extends RTSubscription, R extends CommandRequest>
{
  private static final Logger logger = Logger.getLogger( "CommandListener" );

  private final RTClient rtClient = RTClientFactory.get();
  private final ConcurrentLinkedDeque<RTMethodRequest> commandsToSend = new ConcurrentLinkedDeque<>();

  public abstract CopyOnWriteArrayList<T> getSubscriptionHolder();

  public abstract T createSubscription( RTCallback rtCallback );

  public abstract R createCommandRequest( RTCallback rtCallback );

  public abstract boolean isConnected();

  public void connected()
  {
    RTMethodRequest methodRequest = commandsToSend.poll();

    while( methodRequest != null )
    {
      rtClient.invoke( methodRequest );
      methodRequest = commandsToSend.poll();
    }
  }

  public <T> void addCommandListener( final Class<T> dataType, final AsyncCallback<Command<T>> callback )
  {
    RTCallback rtCallback = new RTCallback()
    {
      @Override
      public AsyncCallback usersCallback()
      {
        return callback;
      }

      @Override
      public void handleResponse( IAdaptingType response )
      {
        try
        {
          Command<T> command = Command.of( dataType );

          UserInfo userInfo = new UserInfo();

          command.setUserInfo( userInfo );

          userInfo.setConnectionId( WeborbSerializationHelper.asString( response, "connectionId" ) );
          userInfo.setUserId( WeborbSerializationHelper.asString( response, "userId" ) );

          command.setType( WeborbSerializationHelper.asString( response, "type" ) );

          IAdaptingType data = WeborbSerializationHelper.asAdaptingType( response, "data" );

          command.setData( (T) data.adapt( dataType ) );
          callback.handleResponse( command );
        }
        catch( AdaptingException e )
        {
          callback.handleFault( new BackendlessFault( e.getMessage() ) );
        }
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        callback.handleFault( fault );
      }
    };

    addCommandListener( rtCallback );
  }

  public void sendCommand( String type, Object data, final AsyncCallback<Void> callback )
  {

    logger.fine( "Send command with type" + type );
    CommandRequest rtMethodRequest = createCommandRequest( new RTCallback()
    {
      @Override
      public AsyncCallback usersCallback()
      {
        return callback;
      }

      @Override
      public void handleResponse( IAdaptingType response )
      {
        logger.info( "command sent" );

        if( callback != null )
          callback.handleResponse( null );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        logger.info( "command fault " + fault );
        if( callback != null )
          callback.handleFault( fault );
      }
    } ).setData( data ).setType( type );

    if( isConnected() )
    {
      rtClient.invoke( rtMethodRequest );
    }
    else
    {
      commandsToSend.addLast( rtMethodRequest );
    }
  }

  public void removeCommandListener( AsyncCallback<Command> callback )
  {
    final CopyOnWriteArrayList<T> subscriptionHolder = getSubscriptionHolder();
    for( T messagingSubscription : subscriptionHolder )
    {
      if( messagingSubscription.getCallback().usersCallback() == callback )
      {
        //we can do it because it is CopyOnWriteArrayList so we iterate through the copy
        subscriptionHolder.remove( messagingSubscription );

        if( isConnected() )
        {
          rtClient.unsubscribe( messagingSubscription.getId() );
        }
      }
    }
  }

  private void addCommandListener( RTCallback rtCallback )
  {
    logger.fine( "try to add command listener" );
    T subscription = createSubscription( rtCallback );
    logger.log( Level.FINE, "subscription object {0}", new Object[]{subscription} );
    getSubscriptionHolder().add( subscription );

    if( isConnected() )
    {
      logger.fine("subscription is connected try to subscribe");
      rtClient.subscribe( subscription );
    }
  }

}
