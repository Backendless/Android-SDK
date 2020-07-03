package com.backendless.transaction;

public class OperationCreateReturned extends Operation<Object>
{
  private Object payload;

  public OperationCreateReturned()
  {
  }

  public OperationCreateReturned( OperationType operationType, String table, String opResultId, Object payload)
  {
    super(operationType, table, opResultId);
    this.payload = payload;
  }

  @Override
  public Object getPayload()
  {
    return this.payload;
  }

  @Override
  public void setPayload( Object payload )
  {
    this.payload = payload;
  }
}
