package com.backendless.rt.messaging;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.rt.RTListener;

public interface Channel extends RTListener
{
  void connect( );

  void disconnect();

  boolean isConnected();

  void addConnectListener( AsyncCallback<Void> callback );

  void removeConnectListeners( AsyncCallback<Void> callback );

  //----------------------------------

  void addMessageListener( AsyncCallback<String> callback );

  <T> void addMessageListener( AsyncCallback<T> callback, Class<T> clazz );

  void addMessageListener( String selector, AsyncCallback<String> callback );

  <T> void addMessageListener( String selector, AsyncCallback<T> callback, Class<T> clazz );

  void addMessageListener( String selector, final MessageInfoCallback callback );

  void addMessageListener( final MessageInfoCallback callback );

  void removeMessageListeners( String selector );

  void removeMessageListeners( AsyncCallback<?> callback );

  void removeMessageListeners( String selector, AsyncCallback<?> callback );

  void removeAllMessageListeners();

  //----------------------------------

  <T> void addCommandListener( Class<T> dataType, AsyncCallback<RTCommand<T>> callback );

  void addCommandListener( AsyncCallback<RTCommand<String>> callback );

  <T> void sendCommand( String type, Object data );

  <T> void sendCommand( String type, Object data, AsyncCallback<Void> callback );

  void removeCommandListener( AsyncCallback<RTCommand> callback );
}
