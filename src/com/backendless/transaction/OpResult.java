package com.backendless.transaction;

import java.util.HashMap;
import java.util.Map;

public class OpResult
{
  private Map<String, Object> reference;
  private OperationType operationType;

  public OpResult( Map<String, Object> reference, OperationType operationType )
  {
    this.reference = reference;
    this.operationType = operationType;
  }

  public Map<String, Object> getReference()
  {
    return reference;
  }

  public OperationType getOperationType()
  {
    return operationType;
  }

  public Map<String, Object> resolveTo( String propName )
  {
    Map<String, Object> referencePropName = new HashMap<>( reference );
    referencePropName.put( UnitOfWork.PROP_NAME, propName );
    return referencePropName;
  }

  public OpResultIndex resolveTo( int opResultIndex )
  {
    Map<String, Object> referenceIndex = new HashMap<>( reference );
    referenceIndex.put( UnitOfWork.RESULT_INDEX, opResultIndex );
    return new OpResultIndex( referenceIndex, operationType );
  }
}
