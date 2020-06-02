package com.backendless.transaction;

public class OperationDeleteFactory extends OperationFactory<OperationDelete>
{
  @Override
  protected Class<OperationDelete> getClazz()
  {
    return OperationDelete.class;
  }

  @Override
  protected OperationDelete createOperation( OperationType operationType, String table, String opResultId, Object payload )
  {
    return new OperationDelete( operationType, table, opResultId, payload );
  }
}
