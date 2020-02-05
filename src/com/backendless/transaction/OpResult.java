package com.backendless.transaction;

import java.util.HashMap;
import java.util.Map;

public class OpResult
{
  private final String tableName;
  private final OperationType operationType;
  private final String opResultId;

  OpResult( String tableName, OperationType operationType, String opResultId )
  {
    this.tableName = tableName;
    this.operationType = operationType;
    this.opResultId = opResultId;
  }

  OperationType getOperationType()
  {
    return operationType;
  }

  String getTableName()
  {
    return tableName;
  }

  public OpResultValueReference resolveTo( int resultIndex, String propName )
  {
    return new OpResultValueReference( this, resultIndex, propName );
  }

  public OpResultValueReference resolveTo( int resultIndex )
  {
    return new OpResultValueReference( this, resultIndex );
  }

  public OpResultValueReference resolveTo( String propName )
  {
    return new OpResultValueReference( this, propName );
  }

  public Map<String, Object> makeReference()
  {
    Map<String, Object> referenceMap = new HashMap<>();
    referenceMap.put( UnitOfWork.REFERENCE_MARKER, true );
    referenceMap.put( UnitOfWork.OP_RESULT_ID, opResultId );
    return referenceMap;
  }
}
