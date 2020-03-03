package com.backendless.transaction;

import com.backendless.Invoker;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.core.responder.AdaptingResponder;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.utils.ResponderHelper;
import weborb.types.Types;

import java.util.Map;

class UnitOfWorkExecutorImpl implements UnitOfWorkExecutor
{
  private final static String TRANSACTION_MANAGER_SERVER_ALIAS = "com.backendless.services.transaction.TransactionService";

  private final UnitOfWork unitOfWork;
  private final Map<String, Class> clazzes;

  UnitOfWorkExecutorImpl( UnitOfWork unitOfWork, Map<String, Class> clazzes )
  {
    this.unitOfWork = unitOfWork;
    this.clazzes = clazzes;
  }

  @Override
  public UnitOfWorkResult execute()
  {
    return execute( null, false );
  }

  @Override
  public void execute( AsyncCallback<UnitOfWorkResult> responder )
  {
    execute( responder, true );
  }

  private UnitOfWorkResult execute( AsyncCallback<UnitOfWorkResult> responder, boolean async )
  {
    if( unitOfWork.getOperations() == null || unitOfWork.getOperations().isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.LIST_OPERATIONS_NULL_EMPTY );

    Object[] args = new Object[]{ unitOfWork };

    for( Map.Entry<String, Class> entry : clazzes.entrySet() )
      Types.addClientClassMapping( entry.getKey(), entry.getValue() );

    AdaptingResponder<UnitOfWorkResult> unitOfWorkAdaptingResponder = ResponderHelper.getPOJOAdaptingResponder( UnitOfWorkResult.class );
    if( async )
      Invoker.invokeAsync( TRANSACTION_MANAGER_SERVER_ALIAS, "execute", args, responder, unitOfWorkAdaptingResponder );
    else
      return Invoker.invokeSync( TRANSACTION_MANAGER_SERVER_ALIAS, "execute", args, unitOfWorkAdaptingResponder );

    return null;
  }
}
