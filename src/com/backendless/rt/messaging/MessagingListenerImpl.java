package com.backendless.rt.messaging;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.rt.RTCallback;
import com.backendless.rt.RTClient;
import com.backendless.rt.RTClientFactory;
import com.backendless.rt.RTListenerImpl;
import weborb.types.IAdaptingType;

public class MessagingListenerImpl extends RTListenerImpl implements MessagingListener
{
  private final String channel;
  private final RTClient rtClient = RTClientFactory.get();

  MessagingListenerImpl( String channel )
  {
    this.channel = channel;
  }

  public void connect( )
  {
    connect( null );
  }

  public void connect( final AsyncCallback<Void> callback )
  {
    MessagingSubscription messagingSubscription = MessagingSubscription.connect( channel, new RTCallback()
    {
      @Override
      public AsyncCallback usersCallback()
      {
        return callback;
      }

      @Override
      public void handleResponse( IAdaptingType response )
      {
        if( callback != null )
          callback.handleResponse( null );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        if( callback != null )
          callback.handleFault( fault );
      }
    } );

    rtClient.subscribe( messagingSubscription );
  }

  @Override
  public void addConnectListener( AsyncCallback<Void> callback )
  {

  }

  @Override
  public void removeConnectListeners( AsyncCallback<Void> callback )
  {

  }

  @Override
  public void addMessageListener( AsyncCallback<String> callback )
  {

  }

  @Override
  public <T> void addMessageListener( AsyncCallback<T> callback, Class<T> clazz )
  {

  }

  @Override
  public void addMessageListener( String selector, AsyncCallback<String> callback )
  {

  }

  @Override
  public <T> void addMessageListener( String selector, AsyncCallback<T> callback, Class<T> clazz )
  {

  }

  @Override
  public void removeMessageListeners( String selector )
  {

  }

  @Override
  public void removeMessageListeners( AsyncCallback<String> callback )
  {

  }

  @Override
  public void removeMessageListeners( String selector, AsyncCallback<String> callback )
  {

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
}
