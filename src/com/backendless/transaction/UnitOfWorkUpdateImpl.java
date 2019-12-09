package com.backendless.transaction;

import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.BackendlessSerializer;
import com.backendless.transaction.operations.Operation;
import com.backendless.transaction.operations.OperationUpdate;
import com.backendless.transaction.operations.OperationUpdateBulk;
import com.backendless.transaction.payload.UpdateBulkPayload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UnitOfWorkUpdateImpl implements UnitOFWorkUpdate
{
  AtomicInteger countUpdate = new AtomicInteger( 1 );

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
    String operationResultId = OperationType.UPDATE + "_" + countUpdate.getAndIncrement();
    OperationUpdate operationUpdate = new OperationUpdate( OperationType.UPDATE, tableName, operationResultId, objectMap );

    operations.add( operationUpdate );

    Map<String, Object> reference = new HashMap<>();
    reference.put( UnitOfWork.REFERENCE_MARKER, true );
    reference.put( UnitOfWork.OP_RESULT_ID, operationResultId );
    return new OpResult( reference );
  }

  @Override
  public <E> OpResult update( List<E> instances )
  {
    return null;
  }

  @Override
  public OpResult update( String tableName, List<Map<String, Object>> arrayOfHashMaps )
  {
    String operationResultId = OperationType.UPDATE_BULK + "_" + countUpdate.getAndIncrement();
    UpdateBulkPayload updateBulkPayload = new UpdateBulkPayload( null, arrayOfHashMaps, null );//TODO ???
    OperationUpdateBulk operationUpdateBulk = new OperationUpdateBulk( OperationType.UPDATE_BULK, tableName,
                                                                       operationResultId, updateBulkPayload );

    operations.add( operationUpdateBulk );

    Map<String, Object> reference = new HashMap<>();
    reference.put( UnitOfWork.REFERENCE_MARKER, true );
    reference.put( UnitOfWork.OP_RESULT_ID, operationResultId );
    return new OpResult( reference );
  }

  @Override
  public <E> OpResult bulkUpdate( String whereClause, List<E> instances )
  {
    if( instances == null || instances.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    String tableName =  BackendlessSerializer.getSimpleName( instances.get( 0 ).getClass() );

    return bulkUpdate(  tableName, whereClause, null );//TODO ???
  }

  @Override
  public OpResult bulkUpdate( String tableName, String whereClause, Map<String, Object> changes )
  {
    String operationResultId = OperationType.UPDATE_BULK + "_" + countUpdate.getAndIncrement();
    UpdateBulkPayload updateBulkPayload = new UpdateBulkPayload( whereClause, null, changes );
    OperationUpdateBulk operationUpdateBulk = new OperationUpdateBulk( OperationType.UPDATE_BULK, tableName,
                                                                       operationResultId, updateBulkPayload );

    operations.add( operationUpdateBulk );

    Map<String, Object> reference = new HashMap<>();
    reference.put( UnitOfWork.REFERENCE_MARKER, true );
    reference.put( UnitOfWork.OP_RESULT_ID, operationResultId );
    return new OpResult( reference );
  }
}
