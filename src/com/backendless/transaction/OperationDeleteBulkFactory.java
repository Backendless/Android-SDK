package com.backendless.transaction;

import com.backendless.transaction.payload.DeleteBulkPayload;

public class OperationDeleteBulkFactory extends OperationFactory<OperationDeleteBulk>
{
  @Override
  protected Class<OperationDeleteBulk> getClazz()
  {
    return OperationDeleteBulk.class;
  }

  @Override
  protected OperationDeleteBulk createOperation( OperationType operationType, String table, String opResultId, Object payload )
  {
    return new OperationDeleteBulk( operationType, table, opResultId, (DeleteBulkPayload) payload );
  }
}
