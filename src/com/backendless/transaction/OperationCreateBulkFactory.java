package com.backendless.transaction;

import java.util.List;

public class OperationCreateBulkFactory extends OperationFactory<OperationCreateBulkReturned>
{
  @Override
  protected Class<OperationCreateBulkReturned> getClazz()
  {
    return OperationCreateBulkReturned.class;
  }

  @Override
  protected OperationCreateBulkReturned createOperation( OperationType operationType, String table, String opResultId, Object payload )
  {
    return new OperationCreateBulkReturned( operationType, table, opResultId, (List) payload );
  }
}
