package com.backendless.rt;

import com.backendless.exceptions.BackendlessException;

public class NoSocketIOException extends BackendlessException
{
  public NoSocketIOException()
  {
    super(new BackendlessException( "To use real time features add socket.io-client:1.0.0 dependency" ));
  }
}
