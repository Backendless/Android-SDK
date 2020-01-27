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
  public UnitOfWorkResult execute()
  {
    return execute( null, false );
  }

  public void execute( AsyncCallback<UnitOfWorkResult> responder )
  {
    execute( responder, true );
  }

  private UnitOfWorkResult execute( AsyncCallback<UnitOfWorkResult> responder, boolean async )
  {
    if( unitOfWork.getOperations() == null || unitOfWork.getOperations().isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.LIST_OPERATIONS_NULL_EMPTY );

    Object[] args = new Object[]{ unitOfWork };

    if( async )
      Invoker.invokeAsync( TRANSACTION_MANAGER_SERVER_ALIAS, "execute", args, responder, ResponderHelper.getPOJOAdaptingResponder( UnitOfWorkResult.class ) );
    else
      return Invoker.invokeSync( TRANSACTION_MANAGER_SERVER_ALIAS, "execute", args, ResponderHelper.getPOJOAdaptingResponder( UnitOfWorkResult.class ) );

    return null;
  }
}
