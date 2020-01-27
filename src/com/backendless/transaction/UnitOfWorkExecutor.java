package com.backendless.transaction;

import com.backendless.async.callback.AsyncCallback;

public interface UnitOfWorkExecutor
{
  UnitOfWorkResult execute();

  void execute( AsyncCallback<UnitOfWorkResult> responder );
}
