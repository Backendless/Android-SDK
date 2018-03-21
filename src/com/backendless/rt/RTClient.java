package com.backendless.rt;

import com.backendless.async.callback.Fault;
import com.backendless.async.callback.Result;

public interface RTClient
{
  void subscribe( RTSubscription subscription );

  void unsubscribe( String subscriptionId );

  void userLoggedIn( String userToken );

  void userLoggedOut();

  void invoke( RTMethodRequest methodRequest );

  void setConnectEventListener( Result<Void> callback );

  void setReconnectAttemptEventListener( Result<ReconnectAttempt> callback );

  void setConnectErrorEventListener( Fault fault );

  void setDisconnectEventListener( Result<String> callback );

  boolean isConnected();

  void connect();

  void disconnect();

  boolean isAvailable();
}
