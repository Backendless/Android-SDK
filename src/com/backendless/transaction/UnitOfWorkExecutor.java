package com.backendless.transaction;

import com.backendless.async.callback.AsyncCallback;

interface UnitOfWorkExecutor
{
  UnitOfWorkResult execute();

  void execute( AsyncCallback<UnitOfWorkResult> responder );
}
