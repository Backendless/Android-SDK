package com.backendless.async.callback;

import com.backendless.exceptions.BackendlessFault;

public interface Fault extends Result<BackendlessFault>
{
  @Override
  void handle( BackendlessFault fault );
}
