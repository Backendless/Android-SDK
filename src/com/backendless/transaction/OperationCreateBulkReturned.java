package com.backendless.transaction;

import java.util.List;

public class OperationCreateBulkReturned extends Operation<List<Object>>
{
  private List<Object> payload;

  public OperationCreateBulkReturned()
  {
  }

  public OperationCreateBulkReturned( OperationType operationType, String table, String opResultId, List<Object> payload)
  {
    super(operationType, table, opResultId);
    this.payload = payload;
  }

  @Override
  public List<Object> getPayload()
  {
    return this.payload;
  }

  @Override
  public void setPayload( List<Object> payload )
  {
    this.payload = payload;
  }
}
