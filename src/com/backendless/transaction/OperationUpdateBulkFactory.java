package com.backendless.transaction;

import com.backendless.transaction.payload.UpdateBulkPayload;

public class OperationUpdateBulkFactory extends OperationFactory<OperationUpdateBulk>
{
  @Override
  protected Class<OperationUpdateBulk> getClazz()
  {
    return OperationUpdateBulk.class;
  }

  @Override
  protected OperationUpdateBulk createOperation( OperationType operationType, String table, String opResultId, Object payload )
  {
    return new OperationUpdateBulk( operationType, table, opResultId, (UpdateBulkPayload) payload );
  }
}
