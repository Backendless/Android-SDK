package com.backendless.transaction;


public class OperationUpdateFactory extends OperationFactory<OperationUpdateReturned>
{
  @Override
  protected Class<OperationUpdateReturned> getClazz()
  {
    return OperationUpdateReturned.class;
  }

  @Override
  protected OperationUpdateReturned createOperation( OperationType operationType, String table, String opResultId, Object payload )
  {
    return new OperationUpdateReturned( operationType, table, opResultId, payload );
  }
}
