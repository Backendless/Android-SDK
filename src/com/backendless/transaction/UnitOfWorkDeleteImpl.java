package com.backendless.transaction;

import com.backendless.Persistence;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.BackendlessSerializer;
import com.backendless.transaction.operations.Operation;
import com.backendless.transaction.operations.OperationDelete;
import com.backendless.transaction.operations.OperationDeleteBulk;
import com.backendless.transaction.payload.DeleteBulkPayload;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UnitOfWorkDeleteImpl implements UnitOfWorkDelete
{
  AtomicInteger countDelete = new AtomicInteger( 1 );
  AtomicInteger countDeleteBulk = new AtomicInteger( 1 );

  private final List<Operation> operations;

  public UnitOfWorkDeleteImpl( List<Operation> operations )
  {
    this.operations = operations;
  }

  @Override
  public <E> OpResult delete( E instance )
  {
    Map<String, Object> entityMap = SerializationHelper.serializeEntityToMap( instance );
    String tableName = BackendlessSerializer.getSimpleName( instance.getClass() );

    return delete( tableName, entityMap );
  }

  @Override
  public OpResult delete( String tableName, Map<String, Object> objectMap )
  {
    if( objectMap == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_MAP );

    String operationResultId = OperationType.DELETE + "_" + countDelete.getAndIncrement();
    OperationDelete operationDelete = new OperationDelete( OperationType.DELETE, tableName, operationResultId, objectMap );

    operations.add( operationDelete );

    return TransactionHelper.makeOpResult( operationResultId, OperationType.DELETE );
  }

  @Override
  public OpResult delete( String tableName, String objectId )
  {
    String operationResultId = OperationType.DELETE + "_" + countDelete.getAndIncrement();
    OperationDelete operationDelete = new OperationDelete( OperationType.DELETE, tableName, operationResultId, objectId );

    operations.add( operationDelete );

    return TransactionHelper.makeOpResult( operationResultId, OperationType.DELETE );
  }

  @Override
  public OpResult delete( String tableName, OpResult result )
  {
    if( !OperationType.supportPropNameType.contains( result.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    String operationResultId = OperationType.DELETE + "_" + countDelete.getAndIncrement();
    OperationDelete operationDelete = new OperationDelete( OperationType.DELETE, tableName, operationResultId,
                                                           result.resolveTo( Persistence.DEFAULT_OBJECT_ID_FIELD ) );

    operations.add( operationDelete );

    return TransactionHelper.makeOpResult( operationResultId, OperationType.DELETE );
  }

  @Override
  public OpResult delete( String tableName, OpResultIndex resultIndex )
  {
    if( !OperationType.supportResultIndexType.contains( resultIndex.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    String operationResultId = OperationType.DELETE + "_" + countDelete.getAndIncrement();
    OperationDelete operationDelete = new OperationDelete( OperationType.DELETE, tableName, operationResultId,
                                                           resultIndex.getReference() );

    operations.add( operationDelete );

    return TransactionHelper.makeOpResult( operationResultId, OperationType.DELETE );
  }

  @Override
  public <E> OpResult bulkDelete( List<E> instances )
  {
    List<Map<String, Object>> serializedEntities = TransactionHelper.convertInstancesToMaps( instances );

    String tableName =  BackendlessSerializer.getSimpleName( instances.get( 0 ).getClass() );

    return bulkDelete( tableName, serializedEntities );
  }

  @Override
  public OpResult bulkDelete( String tableName, List<Map<String, Object>> arrayOfObjectMaps )
  {
    if( arrayOfObjectMaps == null || arrayOfObjectMaps.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    String operationResultId = OperationType.DELETE_BULK + "_" + countDeleteBulk.getAndIncrement();
    DeleteBulkPayload deleteBulkPayload = new DeleteBulkPayload( null, arrayOfObjectMaps );
    OperationDeleteBulk operationDeleteBulk = new OperationDeleteBulk( OperationType.DELETE_BULK, tableName,
                                                                       operationResultId, deleteBulkPayload );

    operations.add( operationDeleteBulk );

    return TransactionHelper.makeOpResult( operationResultId, OperationType.DELETE_BULK );
  }

  @Override
  public OpResult bulkDelete( String tableName, String whereClause )
  {
    String operationResultId = OperationType.DELETE_BULK + "_" + countDeleteBulk.getAndIncrement();
    DeleteBulkPayload deleteBulkPayload = new DeleteBulkPayload( whereClause, null );
    OperationDeleteBulk operationDeleteBulk = new OperationDeleteBulk( OperationType.DELETE_BULK, tableName,
                                                                       operationResultId, deleteBulkPayload );

    operations.add( operationDeleteBulk );

    return TransactionHelper.makeOpResult( operationResultId, OperationType.DELETE_BULK );
  }

  @Override
  public OpResult bulkDelete( String tableName, OpResult result, String propName )
  {
    String operationResultId = OperationType.DELETE_BULK + "_" + countDeleteBulk.getAndIncrement();
    Map<String, Object> whereClause = result.resolveTo( propName );
    DeleteBulkPayload deleteBulkPayload = new DeleteBulkPayload( whereClause, null );
    OperationDeleteBulk operationDeleteBulk = new OperationDeleteBulk( OperationType.DELETE_BULK, tableName,
                                                                       operationResultId, deleteBulkPayload );

    operations.add( operationDeleteBulk );

    return TransactionHelper.makeOpResult( operationResultId, OperationType.DELETE_BULK );
  }
}
