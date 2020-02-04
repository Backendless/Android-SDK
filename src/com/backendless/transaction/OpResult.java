package com.backendless.transaction;

import java.util.HashMap;
import java.util.Map;

public class OpResult
{
  private final String tableName;
  private final OperationType operationType;
  private final boolean ___ref = true;
  private final String opResultId;

  public OpResult( String tableName, OperationType operationType, String opResultId )
  {
    this.tableName = tableName;
    this.operationType = operationType;
    this.opResultId = opResultId;
  }

  public OperationType getOperationType()
  {
    return operationType;
  }

  public String getTableName()
  {
    return tableName;
  }

  public OpResultValueReference resolveTo( int resultIndex, String propName )
  {
    return new OpResultValueReference( tableName, operationType, opResultId, resultIndex, propName );
  }

  public OpResultValueReference resolveTo( int resultIndex )
  {
    return new OpResultValueReference( tableName, operationType, opResultId, resultIndex );
  }

  public OpResultValueReference resolveTo( String propName )
  {
    return new OpResultValueReference( tableName, operationType, opResultId, propName );
  }

  public Map<String, Object> makeReference()
  {
    Map<String, Object> referenceMap = new HashMap<>();
    referenceMap.put( UnitOfWork.REFERENCE_MARKER, ___ref );
    referenceMap.put( UnitOfWork.OP_RESULT_ID, opResultId );
    return referenceMap;
  }
}
