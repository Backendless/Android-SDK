package com.backendless.transaction;

import java.util.List;

public class OperationCreateBulkFactory extends OperationFactory<OperationCreateBulk>
{
  @Override
  protected Class<OperationCreateBulk> getClazz()
  {
    return OperationCreateBulk.class;
  }

  @Override
  protected OperationCreateBulk createOperation( OperationType operationType, String table, String opResultId, Object payload )
  {
    return new OperationCreateBulk( operationType, table, opResultId, (List) payload );
  }
}
