package com.backendless.rt.messaging;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.rt.RTListener;

public interface MessagingListener extends RTListener
{
  void connect( );

  void connect( AsyncCallback<Void> callback );

  void addConnectListener( AsyncCallback<Void> callback );

  void removeConnectListeners( AsyncCallback<Void> callback );

  //----------------------------------

  void addMessageListener( AsyncCallback<String> callback );

  <T> void addMessageListener( AsyncCallback<T> callback, Class<T> clazz );

  void addMessageListener( String selector, AsyncCallback<String> callback );

  <T> void addMessageListener( String selector, AsyncCallback<T> callback, Class<T> clazz );

  void removeMessageListeners( String selector );

  void removeMessageListeners( AsyncCallback<String> callback );

  void removeMessageListeners( String selector,  AsyncCallback<String> callback );

  //----------------------------------

  <T> void addCommandListener( Class<T> dataType, AsyncCallback<RTCommand<T>> callback );

  void addCommandListener( AsyncCallback<RTCommand<String>> callback );

  <T> void sendCommand( RTCommand<T> command );

  void removeCommandListener( AsyncCallback<RTCommand> callback );
}
