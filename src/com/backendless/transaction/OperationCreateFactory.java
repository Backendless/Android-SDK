package com.backendless.transaction;

public class OperationCreateFactory extends OperationFactory<OperationCreateReturned>
{
  @Override
  protected Class<OperationCreateReturned> getClazz()
  {
    return OperationCreateReturned.class;
  }

  @Override
  protected OperationCreateReturned createOperation( OperationType operationType, String table, String opResultId, Object payload )
  {
    return new OperationCreateReturned( operationType, table, opResultId, payload );
  }
}
