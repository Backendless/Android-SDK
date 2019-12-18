package com.backendless.transaction;

import com.backendless.Invoker;
import com.backendless.UnitOfWork;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.utils.ResponderHelper;

public class UnitOfWorkExecutorImpl implements UnitOfWorkExecutor
{
  private final static String TRANSACTION_MANAGER_SERVER_ALIAS = "com.backendless.services.transaction.TransactionService";

  private final UnitOfWork unitOfWork;

  public UnitOfWorkExecutorImpl( UnitOfWork unitOfWork )
  {
    this.unitOfWork = unitOfWork;
  }

  @Override
  public UnitOfWorkStatus execute()
  {
    return execute( null, false );
  }

  public void execute( AsyncCallback<UnitOfWorkStatus> responder )
  {
    execute( responder, true );
  }

  private UnitOfWorkStatus execute( AsyncCallback<UnitOfWorkStatus> responder, boolean async )
  {
    if( unitOfWork.getOperations() == null || unitOfWork.getOperations().isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.LIST_OPERATIONS_NULL_EMPTY );

    Object[] args = new Object[]{ this };

    if( async )
      Invoker.invokeAsync( TRANSACTION_MANAGER_SERVER_ALIAS, "execute", args, responder, ResponderHelper.getPOJOAdaptingResponder( UnitOfWorkStatus.class ) );
    else
      return Invoker.invokeSync( TRANSACTION_MANAGER_SERVER_ALIAS, "execute", args, ResponderHelper.getPOJOAdaptingResponder( UnitOfWorkStatus.class ) );

    return null;
  }
}
