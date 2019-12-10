package com.backendless.transaction;

import java.util.HashMap;
import java.util.Map;

public class OpResult
{
  private Map<String, Object> reference;

  public OpResult( Map<String, Object> reference )
  {
    this.reference = reference;
  }

  public Map<String, Object> getReference()
  {
    return reference;
  }

  public Map<String, Object> viaPropName( String propName )
  {
    Map<String, Object> referencePropName = new HashMap<>( reference );
    referencePropName.put( UnitOfWork.PROP_NAME, propName );
    return referencePropName;
  }

  public Map<String, Object> viaIndex( int opResultIndex )
  {
    Map<String, Object> referenceIndex = new HashMap<>( reference );
    referenceIndex.put( UnitOfWork.RESULT_INDEX, opResultIndex );
    return referenceIndex;
  }
}
