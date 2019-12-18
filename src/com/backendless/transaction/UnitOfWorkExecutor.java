package com.backendless.transaction;

import com.backendless.async.callback.AsyncCallback;

public interface UnitOfWorkExecutor
{
  UnitOfWorkStatus execute();

  void execute( AsyncCallback<UnitOfWorkStatus> responder );
}
