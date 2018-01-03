package com.backendless.rt.messaging;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.PublishMessageInfo;
import com.backendless.rt.RTCallback;
import com.backendless.rt.RTClient;
import com.backendless.rt.RTClientFactory;
import com.backendless.rt.RTListenerImpl;
import com.backendless.utils.WeborbSerializationHelper;
import weborb.exceptions.AdaptingException;
import weborb.types.IAdaptingType;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChannelImpl extends RTListenerImpl implements Channel
{
  private final String channel;
  private final RTClient rtClient = RTClientFactory.get();
  private volatile boolean connected;
  private final Collection<AsyncCallback<Void>> connectedCallbacks = new CopyOnWriteArrayList<>();
  private final CopyOnWriteArrayList<MessagingSubscription> messagingCallbacks = new CopyOnWriteArrayList<>();
  private final MessagingSubscription connectSubscription;

  ChannelImpl( String channel )
  {
    this.channel = channel;
    connectSubscription = createConnectSubscription( channel );
    connect();
  }

  public void connect( )
  {
    addEventListener( connectSubscription );
    rtClient.subscribe( connectSubscription );
  }

  private MessagingSubscription createConnectSubscription( String channel )
  {
    return MessagingSubscription.connect( channel, new RTCallback()
    {
      @Override
      public AsyncCallback usersCallback()
      {
        return null;
      }

      @Override
      public void handleResponse( IAdaptingType response )
      {
        connected = true;
        for( AsyncCallback<Void> connectedCallback : connectedCallbacks )
        {
          connectedCallback.handleResponse( null );
        }

        for( MessagingSubscription messagingCallback : messagingCallbacks )
        {
          rtClient.subscribe( messagingCallback );
        }
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        connected = false;
        for( AsyncCallback<Void> connectedCallback : connectedCallbacks )
        {
          connectedCallback.handleFault( fault );
        }
      }
    } );
  }

  @Override
  public void disconnect()
  {
    rtClient.unsubscribe( connectSubscription.getId() );
    connected = false;
  }

  @Override
  public boolean isConnected()
  {
    return connected;
  }

  @Override
  public void addConnectListener( AsyncCallback<Void> callback )
  {
      if( connected )
        callback.handleResponse( null );

      connectedCallbacks.add( callback );
  }

  @Override
  public void removeConnectListeners( AsyncCallback<Void> callback )
  {
     connectedCallbacks.remove( callback );
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
  public void removeMessageListeners( AsyncCallback<?> callback )
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
      if( connected )
      {
        rtClient.unsubscribe( messagingSubscription.getId() );
      }
    }
  }

  @Override
  public <T> void addCommandListener( Class<T> dataType, AsyncCallback<RTCommand<T>> callback )
  {

  }

  @Override
  public void addCommandListener( AsyncCallback<RTCommand<String>> callback )
  {

  }

  @Override
  public <T> void sendCommand( RTCommand<T> command )
  {

  }

  @Override
  public void removeCommandListener( AsyncCallback<RTCommand> callback )
  {

  }

  private void addMessageListener( String selector, RTCallback rtCallback )
  {
    MessagingSubscription subscription = selector == null ? MessagingSubscription.subscribe( channel, rtCallback ) : MessagingSubscription.subscribe( channel, selector, rtCallback );

    messagingCallbacks.add( subscription );

    if( isConnected() )
      rtClient.subscribe( subscription );
  }

  private void removeSubscription( MessagingSubscription messagingSubscription )
  {
    //we can do it because it is CopyOnWriteArrayList so we iterate through the copy
    messagingCallbacks.remove( messagingSubscription );

    if( connected )
    {
      rtClient.unsubscribe( messagingSubscription.getId() );
    }
  }
}
