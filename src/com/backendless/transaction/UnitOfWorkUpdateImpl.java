package com.backendless.transaction;

import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.BackendlessSerializer;
import com.backendless.transaction.operations.Operation;
import com.backendless.transaction.operations.OperationUpdate;
import com.backendless.transaction.operations.OperationUpdateBulk;
import com.backendless.transaction.payload.UpdateBulkPayload;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UnitOfWorkUpdateImpl implements UnitOfWorkUpdate
{
  AtomicInteger countUpdate = new AtomicInteger( 1 );
  AtomicInteger countUpdateBulk = new AtomicInteger( 1 );

  private final List<Operation> operations;

  public UnitOfWorkUpdateImpl( List<Operation> operations )
  {
    this.operations = operations;
  }

  @Override
  public <E> OpResult update( E instance )
  {
    Map<String, Object> entityMap = SerializationHelper.serializeEntityToMap( instance );
    String tableName = BackendlessSerializer.getSimpleName( instance.getClass() );

    return update( tableName, entityMap );
  }

  @Override
  public OpResult update( String tableName, Map<String, Object> objectMap )
  {
    if( objectMap == null || objectMap.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_MAP );

    String operationResultId = OperationType.UPDATE + "_" + countUpdate.getAndIncrement();
    OperationUpdate operationUpdate = new OperationUpdate( OperationType.UPDATE, tableName, operationResultId, objectMap );

    operations.add( operationUpdate );

    return TransactionHelper.makeOpResult( tableName, operationResultId, OperationType.UPDATE );
  }

  @Override
  public OpResult update( String tableName, OpResult objectMap )
  {
    if( objectMap == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OP_RESULT );

    if( !OperationType.CREATE.equals( objectMap.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    String operationResultId = OperationType.UPDATE + "_" + countUpdate.getAndIncrement();
    OperationUpdate operationUpdate = new OperationUpdate( OperationType.UPDATE, tableName,
                                                           operationResultId, objectMap.getReference() );

    operations.add( operationUpdate );

    return TransactionHelper.makeOpResult( tableName, operationResultId, OperationType.UPDATE );
  }

  @Override
  public <E> OpResult bulkUpdate( String whereClause, E changes )
  {
    Map<String, Object> changesMap = SerializationHelper.serializeEntityToMap( changes );
    String tableName = BackendlessSerializer.getSimpleName( changes.getClass() );

    return bulkUpdate( tableName, whereClause, changesMap );
  }

  @Override
  public OpResult bulkUpdate( String tableName, String whereClause, Map<String, Object> changes )
  {
    return bulkUpdate( tableName, whereClause, null, changes );
  }

  @Override
  public OpResult bulkUpdate( String tableName, List<String> objectsForChanges, Map<String, Object> changes )
  {
    return bulkUpdate( tableName, null, objectsForChanges, changes );
  }

  @Override
  public OpResult bulkUpdate( String tableName, OpResult objectIdsForChanges, Map<String, Object> changes )
  {
    if( objectIdsForChanges == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OP_RESULT );

    if( !OperationType.supportResultIndexType.contains( objectIdsForChanges.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    return bulkUpdate( tableName, null, objectIdsForChanges.getReference(), changes );
  }

  private OpResult bulkUpdate( String tableName, String whereClause, Object objectsForChanges,
                               Map<String, Object> changes )
  {
    if( changes == null || changes.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_MAP );

    TransactionHelper.removeSystemField( changes );

    String operationResultId = OperationType.UPDATE_BULK + "_" + countUpdateBulk.getAndIncrement();
    UpdateBulkPayload updateBulkPayload = new UpdateBulkPayload( whereClause, objectsForChanges, changes );
    OperationUpdateBulk operationUpdateBulk = new OperationUpdateBulk( OperationType.UPDATE_BULK, tableName,
                                                                       operationResultId, updateBulkPayload );

    operations.add( operationUpdateBulk );

    return TransactionHelper.makeOpResult( tableName, operationResultId, OperationType.UPDATE_BULK );
  }
}
