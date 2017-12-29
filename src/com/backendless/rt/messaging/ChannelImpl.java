package com.backendless.rt.messaging;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.rt.RTCallback;
import com.backendless.rt.RTClient;
import com.backendless.rt.RTClientFactory;
import com.backendless.rt.RTListenerImpl;
import weborb.types.IAdaptingType;

import java.util.ArrayList;
import java.util.Collection;

public class ChannelImpl extends RTListenerImpl implements Channel
{
  private final String channel;
  private final RTClient rtClient = RTClientFactory.get();
  private volatile boolean connected;
  private final Collection<AsyncCallback<Void>> connectedCallbacks = new ArrayList<>(  );
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
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        connected = false;
        for( AsyncCallback<Void> connectedCallback : connectedCallbacks )
        {
          connectedCallback.handleResponse( null );
        }
      }
    } );
  }

  @Override
  public void disconnect()
  {
    rtClient.unsubscribe( connectSubscription.getId() );
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
