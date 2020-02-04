package com.backendless.transaction;

import com.backendless.exceptions.ExceptionMessage;
import com.backendless.transaction.operations.Operation;
import com.backendless.UnitOfWork;

import java.util.HashMap;
import java.util.Map;

public class OpResult
{
  private String tableName;
  private Map<String, Object> reference;
  private OperationType operationType;

  public OpResult( String tableName, Map<String, Object> reference, OperationType operationType )
  {
    this.tableName = tableName;
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

  public String getTableName()
  {
    return tableName;
  }

  public Map<String, Object> resolveTo( String propName )
  {
    Map<String, Object> referencePropName = new HashMap<>( reference );
    referencePropName.put( UnitOfWork.PROP_NAME, propName );
    return referencePropName;
  }

  public Map<String, Object> resolveTo( int opResultIndex )
  {
    Map<String, Object> referenceIndex = new HashMap<>( reference );
    referenceIndex.put( UnitOfWork.RESULT_INDEX, opResultIndex );
    return referenceIndex;
  }

  public Map<String, Object> resolveTo( int opResultIndex, String propName )
  {
    Map<String, Object> referenceIndexPropName = new HashMap<>( reference );
    referenceIndexPropName.put( UnitOfWork.RESULT_INDEX, opResultIndex );
    referenceIndexPropName.put( UnitOfWork.PROP_NAME, propName );
    return referenceIndexPropName;
  }

  public OpResultIndex resolveToIndex( int opResultIndex )
  {
    Map<String, Object> referenceIndex = new HashMap<>( reference );
    referenceIndex.put( UnitOfWork.RESULT_INDEX, opResultIndex );
    return new OpResultIndex( tableName, referenceIndex, operationType );
  }

  public void setOpResultId( UnitOfWork unitOfWork, String newOpResultId )
  {
    if( unitOfWork.getOpResultIdStrings().contains( newOpResultId ) )
      throw new IllegalArgumentException( ExceptionMessage.OP_RESULT_ID_ALREADY_PRESENT );

    String oldOpResultId = (String) this.reference.get( UnitOfWork.OP_RESULT_ID );
    this.reference.put( UnitOfWork.OP_RESULT_ID, newOpResultId );

    for( Operation<?> operation : unitOfWork.getOperations() )
      if( operation.getOpResultId().equals( oldOpResultId ) )
      {
        operation.setOpResultId( newOpResultId );
        break;
      }
  }
}
