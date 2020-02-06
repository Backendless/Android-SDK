package com.backendless.transaction;

import java.util.Map;

public class OpResultValueReference
{
  private final OpResult opResult;
  private final Integer resultIndex;
  private final String propName;

  OpResultValueReference( OpResult opResult, Integer resultIndex, String propName )
  {
    this.opResult = opResult;
    this.resultIndex = resultIndex;
    this.propName = propName;
  }

  OpResultValueReference( OpResult opResult, Integer resultIndex )
  {
    this.opResult = opResult;
    this.resultIndex = resultIndex;
    this.propName = null;
  }

  OpResultValueReference( OpResult opResult, String propName )
  {
    this.opResult = opResult;
    this.resultIndex = null;
    this.propName = propName;
  }

  public OpResult getOpResult()
  {
    return opResult;
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
    return new OpResultValueReference( opResult, resultIndex, propName );
  }

  public Map<String, Object> makeReference()
  {
    Map<String, Object> referenceMap = opResult.makeReference();

    if( resultIndex != null )
      referenceMap.put( UnitOfWork.RESULT_INDEX, resultIndex );

    if( propName != null )
      referenceMap.put( UnitOfWork.PROP_NAME, propName );

    return referenceMap;
  }
}
