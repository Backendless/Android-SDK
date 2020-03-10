package com.backendless.transaction;

import com.backendless.exceptions.ExceptionMessage;
import com.backendless.transaction.operations.Operation;

import java.util.HashMap;
import java.util.Map;

public class OpResult
{
  private final String tableName;
  private final OperationType operationType;
  private String opResultId;

  OpResult( String tableName, OperationType operationType, String opResultId )
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

  public String getOpResultId()
  {
    return opResultId;
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

  public void setOpResultId( UnitOfWork unitOfWork, String newOpResultId )
  {
    if( unitOfWork.getOpResultIdStrings().contains( newOpResultId ) )
      throw new IllegalArgumentException( ExceptionMessage.OP_RESULT_ID_ALREADY_PRESENT );

    for( Operation<?> operation : unitOfWork.getOperations() )
      if( operation.getOpResultId().equals( opResultId ) )
      {
        operation.setOpResultId( newOpResultId );
        break;
      }

    opResultId = newOpResultId;
  }
}
