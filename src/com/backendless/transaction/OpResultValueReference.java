package com.backendless.transaction;

import java.util.HashMap;
import java.util.Map;

public class OpResultValueReference
{
  private final String tableName;
  private final OperationType operationType;
  private final boolean ___ref = true;
  private final String opResultId;
  private final Integer resultIndex;
  private final String propName;

  public OpResultValueReference( String tableName, OperationType operationType, String opResultId,
                                 Integer resultIndex, String propName )
  {
    this.tableName = tableName;
    this.operationType = operationType;
    this.opResultId = opResultId;
    this.resultIndex = resultIndex;
    this.propName = propName;
  }

  public OpResultValueReference( String tableName, OperationType operationType, String opResultId, Integer resultIndex )
  {
    this.tableName = tableName;
    this.operationType = operationType;
    this.opResultId = opResultId;
    this.resultIndex = resultIndex;
    this.propName = null;
  }

  public OpResultValueReference( String tableName, OperationType operationType, String opResultId, String propName )
  {
    this.tableName = tableName;
    this.operationType = operationType;
    this.opResultId = opResultId;
    this.resultIndex = null;
    this.propName = propName;
  }

  public String getTableName()
  {
    return tableName;
  }

  public OperationType getOperationType()
  {
    return operationType;
  }

  public Integer getResultIndex()
  {
    return resultIndex;
  }

  public String getPropName()
  {
    return propName;
  }

  public OpResultValueReference resolveTo( String propName )
  {
    return new OpResultValueReference( tableName, operationType, opResultId, resultIndex, propName );
  }

  public Map<String, Object> makeReference()
  {
    Map<String, Object> referenceMap = new HashMap<>();
    referenceMap.put( UnitOfWork.REFERENCE_MARKER, ___ref );
    referenceMap.put( UnitOfWork.OP_RESULT_ID, opResultId );

    if( resultIndex != null )
      referenceMap.put( UnitOfWork.RESULT_INDEX, resultIndex );

    if( propName != null )
      referenceMap.put( UnitOfWork.PROP_NAME, propName );

    return referenceMap;
  }
}
