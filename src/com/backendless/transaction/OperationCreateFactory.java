package com.backendless.transaction;

import java.util.Map;

public class OperationCreateFactory extends OperationFactory<OperationCreate>
{
  @Override
  protected Class<OperationCreate> getClazz()
  {
    return OperationCreate.class;
  }

  @Override
  protected OperationCreate createOperation( OperationType operationType, String table, String opResultId, Object payload )
  {
    return new OperationCreate( operationType, table, opResultId, (Map<String, Object>) payload );
  }
}
