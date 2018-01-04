package com.backendless.rt.messaging;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.rt.Command;
import com.backendless.rt.RTListener;
import com.backendless.rt.users.UserStatusResponse;

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

  <T> void addCommandListener( Class<T> dataType, AsyncCallback<Command<T>> callback );

  void addCommandListener( AsyncCallback<Command<String>> callback );

  <T> void sendCommand( String type, Object data );

  <T> void sendCommand( String type, Object data, AsyncCallback<Void> callback );

  void removeCommandListener( AsyncCallback<Command> callback );

  //----------------------------------

  void addUserStatusListener( AsyncCallback<UserStatusResponse> callback );

  void removeUserStatusListeners();

  void removeUserStatusListeners( AsyncCallback<UserStatusResponse> callback );
}
