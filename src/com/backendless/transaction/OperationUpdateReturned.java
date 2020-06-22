package com.backendless.transaction;

public class OperationUpdateReturned extends Operation<Object>
{
  private Object payload;

  public OperationUpdateReturned()
  {
  }

  public OperationUpdateReturned( OperationType operationType, String table, String opResultId, Object payload )
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
