package com.backendless.transaction;

import com.backendless.Persistence;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.BackendlessSerializer;
import com.backendless.persistence.operations.Operation;
import com.backendless.persistence.operations.OperationDelete;
import com.backendless.persistence.operations.OperationDeleteBulk;
import com.backendless.transaction.payload.DeleteBulkPayload;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UnitOfWorkDeleteImpl implements UnitOfWorkDelete
{
  AtomicInteger countDelete = new AtomicInteger( 1 );
  AtomicInteger countDeleteBulk = new AtomicInteger( 1 );

  private final List<Operation<?>> operations;

  public UnitOfWorkDeleteImpl( List<Operation<?>> operations )
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
  public <E> OpResult bulkDelete( String tableName, List<E> arrayOfObjects )
  {
    if( arrayOfObjects == null || arrayOfObjects.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    List<String> objectIds;
    if( arrayOfObjects.get( 0 ).getClass().isAssignableFrom( Map.class ) )
      objectIds = TransactionHelper.convertMapToObjectIds( (List<Map<String, Object>>) arrayOfObjects );
    else if( arrayOfObjects.get( 0 ).getClass().isAssignableFrom( String.class ) )
      objectIds = (List<String>) arrayOfObjects;
    else
      throw new IllegalArgumentException( ExceptionMessage.LIST_MAP_OR_STRING );

    return bulkDelete( tableName, null, objectIds );
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
  public OpResult bulkDelete( String tableName, OpResult result )
  {
    if( !OperationType.supportResultIndexType.contains( result.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    return bulkDelete( tableName, result.getReference(), null );
  }

  private OpResult bulkDelete( String tableName, Map<String, Object> reference, List<String> objectIds )
  {
    Object unconditional = reference != null ? reference : objectIds;

    String operationResultId = OperationType.DELETE_BULK + "_" + countDeleteBulk.getAndIncrement();
    DeleteBulkPayload deleteBulkPayload = new DeleteBulkPayload( null, unconditional );
    OperationDeleteBulk operationDeleteBulk = new OperationDeleteBulk( OperationType.DELETE_BULK, tableName,
                                                                       operationResultId, deleteBulkPayload );

    operations.add( operationDeleteBulk );

    return TransactionHelper.makeOpResult( operationResultId, OperationType.DELETE_BULK );
  }
}
