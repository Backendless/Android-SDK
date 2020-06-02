package com.backendless.transaction;

import java.util.Map;

public class OperationUpdateFactory extends OperationFactory<OperationUpdate>
{
  @Override
  protected Class<OperationUpdate> getClazz()
  {
    return OperationUpdate.class;
  }

  @Override
  protected OperationUpdate createOperation( OperationType operationType, String table, String opResultId, Object payload )
  {
    return new OperationUpdate( operationType, table, opResultId, (Map<String, Object>) payload );
  }
}
