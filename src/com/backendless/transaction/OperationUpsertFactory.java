package com.backendless.transaction;


public class OperationUpsertFactory extends OperationFactory<OperationUpsertReturned>
{
  @Override
  protected Class<OperationUpsertReturned> getClazz()
  {
    return OperationUpsertReturned.class;
  }

  @Override
  protected OperationUpsertReturned createOperation( OperationType operationType, String table, String opResultId, Object payload )
  {
    return new OperationUpsertReturned( operationType, table, opResultId, payload );
  }
}
