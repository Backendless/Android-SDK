package com.backendless.rt;

import com.backendless.async.callback.Fault;
import com.backendless.async.callback.Result;

public class RTClientWithoutSocketIO implements RTClient
{
  @Override
  public void subscribe( RTSubscription subscription )
  {
    throw new NoSocketIOException();
  }

  @Override
  public void unsubscribe( String subscriptionId )
  {
    throw new NoSocketIOException();
  }

  @Override
  public void userLoggedIn( String userToken )
  {
    //ignore
  }

  @Override
  public void userLoggedOut()
  {
    //ignore
  }

  @Override
  public void invoke( RTMethodRequest methodRequest )
  {
    throw new NoSocketIOException();
  }

  @Override
  public void setConnectEventListener( Result<Void> callback )
  {
    throw new NoSocketIOException();
  }

  @Override
  public void setReconnectAttemptEventListener( Result<ReconnectAttempt> callback )
  {
    throw new NoSocketIOException();
  }

  @Override
  public void setConnectErrorEventListener( Fault fault )
  {
    throw new NoSocketIOException();
  }

  @Override
  public void setDisconnectEventListener( Result<String> callback )
  {
    throw new NoSocketIOException();
  }

  @Override
  public boolean isConnected()
  {
    throw new NoSocketIOException();
  }

  @Override
  public void connect()
  {
    throw new NoSocketIOException();
  }

  @Override
  public void disconnect()
  {
    throw new NoSocketIOException();
  }

  @Override
  public boolean isAvailable()
  {
    return false;
  }
}
