package com.backendless.transaction;

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

    return TransactionHelper.makeOpResult( operationResultId );
  }

  @Override
  public OpResult delete( String tableName, OpResult result )
  {
    String operationResultId = OperationType.DELETE + "_" + countDelete.getAndIncrement();
    OperationDelete operationDelete = new OperationDelete( OperationType.DELETE, tableName, operationResultId,
                                                           result.getReference() );

    operations.add( operationDelete );

    return TransactionHelper.makeOpResult( operationResultId );
  }

  @Override
  public OpResult delete( String tableName, OpResult result, int opResultIndex )//TODO ??? delete or change server
  {
    String operationResultId = OperationType.DELETE + "_" + countDelete.getAndIncrement();
    OperationDelete operationDelete = new OperationDelete( OperationType.DELETE, tableName, operationResultId,
                                                           result.viaIndex( opResultIndex ) );

    operations.add( operationDelete );

    return TransactionHelper.makeOpResult( operationResultId );
  }

  @Override
  public <E> OpResult bulkDelete( List<E> instances )
  {
    List<Map<String, Object>> serializedEntities = TransactionHelper.getConvertInstancesToMaps( instances );

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

    return TransactionHelper.makeOpResult( operationResultId );
  }

  @Override
  public OpResult bulkDelete( String tableName, String whereClause )
  {
    String operationResultId = OperationType.DELETE_BULK + "_" + countDeleteBulk.getAndIncrement();
    DeleteBulkPayload deleteBulkPayload = new DeleteBulkPayload( whereClause, null );
    OperationDeleteBulk operationDeleteBulk = new OperationDeleteBulk( OperationType.DELETE_BULK, tableName,
                                                                       operationResultId, deleteBulkPayload );

    operations.add( operationDeleteBulk );

    return TransactionHelper.makeOpResult( operationResultId );
  }

  @Override
  public OpResult bulkDelete( String tableName, OpResult result, String propName )
  {
    String operationResultId = OperationType.DELETE_BULK + "_" + countDeleteBulk.getAndIncrement();
    Map<String, Object> whereClause = result.viaPropName( propName );
    DeleteBulkPayload deleteBulkPayload = new DeleteBulkPayload( whereClause, null );
    OperationDeleteBulk operationDeleteBulk = new OperationDeleteBulk( OperationType.DELETE_BULK, tableName,
                                                                       operationResultId, deleteBulkPayload );

    operations.add( operationDeleteBulk );

    return TransactionHelper.makeOpResult( operationResultId );
  }
}
