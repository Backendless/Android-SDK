package com.backendless.transaction;

public class OperationFindFactory extends OperationFactory<OperationFind>
{
  @Override
  protected Class<OperationFind> getClazz()
  {
    return OperationFind.class;
  }

  @Override
  protected OperationFind createOperation( OperationType operationType, String table, String opResultId, Object payload )
  {
    return new OperationFind( operationType, table, opResultId, payload );
  }
}
