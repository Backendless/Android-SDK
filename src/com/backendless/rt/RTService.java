package com.backendless.rt;

import com.backendless.async.callback.Fault;
import com.backendless.async.callback.Result;

public interface RTService
{
  void addConnectListener( Result<Void> callback );

  void addReconnectAttemptListener( Result<ReconnectAttempt> callback );

  void addConnectErrorListener( Fault fault );

  void removeConnectionListeners();

  void addDisconnectListener( Result<String> callback );

  <T extends Result> void removeListener( T callback );

  void connect();

  void disconnect();
}
