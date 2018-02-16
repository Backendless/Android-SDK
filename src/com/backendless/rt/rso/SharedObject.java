package com.backendless.rt.rso;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.rt.RTListener;
import com.backendless.rt.command.Command;
import com.backendless.rt.users.UserInfo;
import com.backendless.rt.users.UserStatus;

import java.util.Collection;

public interface SharedObject extends RTListener
{
  void addConnectListener( AsyncCallback<Void> callback );

  void removeConnectListener( AsyncCallback<Void> callback );

  void removeConnectListeners();

  void addChangesListener( AsyncCallback<SharedObjectChanges> callback );

  void removeChangesListener( AsyncCallback<SharedObjectChanges> callback );

  void removeChangesListeners();

  void addClearListener( AsyncCallback<UserInfo> callback );

  void removeClearListener( AsyncCallback<UserInfo> callback );

  void removeClearListeners();

  void addCommandListener( AsyncCallback<Command<String>> callback );

  <T> void addCommandListener( Class<T> dataType, AsyncCallback<Command<T>> callback );

  void removeCommandListener( AsyncCallback<Command> callback );

  void removeCommandListeners();

  void addUserStatusListener( AsyncCallback<UserStatus> callback );

  void removeUserStatusListener( AsyncCallback<UserStatus> callback );

  void removeUserStatusListeners();

  void set( String key, Object value, AsyncCallback<Void> callback );

  void set( String key, Object value );

  void get( String key, AsyncCallback<String> callback );

  <T> void get( String key, Class<T> dataType, AsyncCallback<T> callback );

  void clear( AsyncCallback<Void> callback );

  void clear();

  void sendCommand( String type, Object value );

  void sendCommand( String type, Object value, AsyncCallback<Void> callback );

  void invoke( String methodName, Object... args );

  void invoke( String methodName, Object[] args, AsyncCallback<Void> callback );

  void invoke( String methodName, Object[] args, Collection<String> target, AsyncCallback<Void> callback );

  void setInvocationTarget( Object target );

  void setInvocationTarget( Object target, AsyncCallback<Object> callback );

  void connect();

  void disconnect();

  boolean isConnected();
}
