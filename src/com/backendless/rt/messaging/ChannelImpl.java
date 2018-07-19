package com.backendless.rt.messaging;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.PublishMessageInfo;
import com.backendless.rt.ConnectListener;
import com.backendless.rt.RTCallback;
import com.backendless.rt.RTClient;
import com.backendless.rt.RTClientFactory;
import com.backendless.rt.RTListenerImpl;
import com.backendless.rt.SubscriptionNames;
import com.backendless.rt.command.Command;
import com.backendless.rt.command.CommandListener;
import com.backendless.rt.users.UserStatusResponse;
import com.backendless.utils.WeborbSerializationHelper;
import weborb.exceptions.AdaptingException;
import weborb.types.IAdaptingType;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class ChannelImpl extends RTListenerImpl implements Channel
{
  private static final Logger logger = Logger.getLogger( "ChannelImpl" );

  private final String channel;
  private final RTClient rtClient = RTClientFactory.get();
  private final CopyOnWriteArrayList<MessagingSubscription> messagingCallbacks = new CopyOnWriteArrayList<>();

  private final CommandListener<MessagingSubscription, MessagingCommandRequest> commandListener = new CommandListener<MessagingSubscription, MessagingCommandRequest>()
  {
    @Override
    public CopyOnWriteArrayList<MessagingSubscription> getSubscriptionHolder()
    {
      return messagingCallbacks;
    }

    @Override
    public MessagingSubscription createSubscription( RTCallback rtCallback )
    {
      return MessagingSubscription.command( channel, rtCallback );
    }

    @Override
    public MessagingCommandRequest createCommandRequest( RTCallback rtCallback )
    {
      return new MessagingCommandRequest( channel, rtCallback );
    }

    @Override
    public boolean isConnected()
    {
      return connectListener.isConnected();
    }
  };

  private final ConnectListener<MessagingSubscription> connectListener;

  ChannelImpl( final String channel )
  {
    this.channel = channel;
    connectListener = new ConnectListener<MessagingSubscription>( channel )
    {
      @Override
      public void connected()
      {
        for( MessagingSubscription messagingCallback : messagingCallbacks )
        {
          rtClient.subscribe( messagingCallback );
        }

        commandListener.connected();
      }

      @Override
      public MessagingSubscription createSubscription( RTCallback callback )
      {
        return MessagingSubscription.connect( channel, callback );
      }
    };
    join();
  }

  public void join( )
  {
    connectListener.connect();
  }

  @Override
  public void leave()
  {
    connectListener.disconnect();
  }

  @Override
  public boolean isJoined()
  {
    return connectListener.isConnected();
  }

  @Override
  public void addJoinListener( AsyncCallback<Void> callback )
  {
      connectListener.addConnectListener( callback );
  }

  @Override
  public void removeJoinListener( AsyncCallback<Void> callback )
  {
     connectListener.removeConnectListener( callback );
  }

  //---messaging
  @Override
  public void addMessageListener( AsyncCallback<String> callback )
  {
    addMessageListener( null, callback, String.class );
  }

  @Override
  public <T> void addMessageListener( AsyncCallback<T> callback, Class<T> clazz )
  {
    addMessageListener( null, callback, clazz );
  }

  @Override
  public void addMessageListener( String selector, AsyncCallback<String> callback )
  {
    addMessageListener( selector, callback, String.class );
  }

  @Override
  public <T> void addMessageListener( String selector, final AsyncCallback<T> callback, final Class<T> clazz )
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
          IAdaptingType message = WeborbSerializationHelper.asAdaptingType( response, "message" );

          T adaptedResponse = (T) message.adapt( clazz );
          callback.handleResponse( adaptedResponse );
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

    addMessageListener( selector, rtCallback );
  }

  @Override
  public void addMessageListener( String selector, final MessageInfoCallback callback )
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

          PublishMessageInfo adaptedResponse = (PublishMessageInfo) response.adapt( PublishMessageInfo.class );
          callback.handleResponse( adaptedResponse );
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

    addMessageListener( selector, rtCallback );
  }

  @Override
  public void addMessageListener( final MessageInfoCallback callback )
  {
    addMessageListener( null, callback );
  }

  @Override
  public void removeMessageListeners( String selector )
  {
    for( MessagingSubscription messagingSubscription : messagingCallbacks )
    {
      if( messagingSubscription.getSelector() != null && messagingSubscription.getSelector().equals( selector ) )
      {
        removeSubscription( messagingSubscription );
      }
    }
  }

  @Override
  public void removeMessageListener( AsyncCallback<?> callback )
  {
    for( MessagingSubscription messagingSubscription : messagingCallbacks )
    {
      if( messagingSubscription.getCallback().usersCallback() == callback )
      {
        removeSubscription( messagingSubscription );
      }
    }
  }

  @Override
  public void removeMessageListeners( String selector, AsyncCallback<?> callback )
  {
    for( MessagingSubscription messagingSubscription : messagingCallbacks )
    {
      if( (selector == null || selector.equals( messagingSubscription.getSelector() ))
              && (callback == null || messagingSubscription.getCallback().usersCallback() == callback) )
      {
        removeSubscription( messagingSubscription );
      }
    }
  }

  @Override
  public void removeAllMessageListeners()
  {
    Iterator<MessagingSubscription> iterator = messagingCallbacks.iterator();

    messagingCallbacks.clear();

    while( iterator.hasNext() )
    {
      MessagingSubscription messagingSubscription = iterator.next();
      if( isJoined() )
      {
        rtClient.unsubscribe( messagingSubscription.getId() );
      }
    }
  }

  @Override
  public <T> void addCommandListener( final Class<T> dataType, final AsyncCallback<Command<T>> callback )
  {
    commandListener.addCommandListener( dataType, callback );
  }

  @Override
  public void addCommandListener( AsyncCallback<Command<String>> callback )
  {
     addCommandListener( String.class, callback );
  }

  @Override
  public <T> void sendCommand( String type, Object data )
  {
    sendCommand( type, data, null );
  }

  @Override
  public <T> void sendCommand( String type, Object data, final AsyncCallback<Void> callback )
  {
    commandListener.sendCommand( type, data, callback );
  }

  @Override
  public void removeCommandListener( AsyncCallback<Command> callback )
  {
     removeMessageListener( callback );
  }

  @Override
  public void addUserStatusListener( final AsyncCallback<UserStatusResponse> callback )
  {
    if( callback == null )
      throw new BackendlessException( "Callback can not be null" );

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
          UserStatusResponse userStatusResponse = (UserStatusResponse) response.adapt( UserStatusResponse.class );
          callback.handleResponse( userStatusResponse );
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

    addUserListener( rtCallback );
  }

  @Override
  public void removeUserStatusListeners()
  {
    for( MessagingSubscription messagingSubscription : messagingCallbacks )
    {
      if( messagingSubscription.getSubscriptionName() == SubscriptionNames.PUB_SUB_USERS )
      {
        removeSubscription( messagingSubscription );
      }
    }
  }

  @Override
  public void removeUserStatusListener( AsyncCallback<UserStatusResponse> callback )
  {
     removeMessageListener( callback );
  }

  private void addMessageListener( String selector, RTCallback rtCallback )
  {
    MessagingSubscription subscription = selector == null ? MessagingSubscription.subscribe( channel, rtCallback ) : MessagingSubscription.subscribe( channel, selector, rtCallback );

    messagingCallbacks.add( subscription );

    if( isJoined() )
      rtClient.subscribe( subscription );
  }

  private void addUserListener( RTCallback rtCallback )
  {
    MessagingSubscription subscription = MessagingSubscription.userStatus( channel, rtCallback );

    messagingCallbacks.add( subscription );

    if( isJoined() )
      rtClient.subscribe( subscription );
  }

  private void removeSubscription( MessagingSubscription messagingSubscription )
  {
    //we can do it because it is CopyOnWriteArrayList so we iterate through the copy
    messagingCallbacks.remove( messagingSubscription );

    if( isJoined() )
    {
      rtClient.unsubscribe( messagingSubscription.getId() );
    }
  }
}
