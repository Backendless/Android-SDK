package com.backendless.transaction;

import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.BackendlessSerializer;

import java.util.List;
import java.util.Map;

public class UnitOfWorkUpsertImpl implements UnitOfWorkUpsert
{
  private final List<Operation> operations;
  private final OpResultIdGenerator opResultIdGenerator;
  private final Map<String, Class> clazzes;

  UnitOfWorkUpsertImpl( List<Operation> operations, OpResultIdGenerator opResultIdGenerator,
                        Map<String, Class> clazzes )
  {
    this.operations = operations;
    this.opResultIdGenerator = opResultIdGenerator;
    this.clazzes = clazzes;
  }

  @Override
  public <E> OpResult upsert( E instance )
  {
    Map<String, Object> entityMap = SerializationHelper.serializeEntityToMap( instance );
    String tableName = BackendlessSerializer.getSimpleName( instance.getClass() );

    clazzes.put( tableName, instance.getClass() );

    return upsert( tableName, entityMap );
  }

  @Override
  public OpResult upsert( String tableName, Map<String, Object> objectMap )
  {
    if( objectMap == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_MAP );

    TransactionHelper.makeReferenceToValueFromOpResult( objectMap );

    String operationResultId = opResultIdGenerator.generateOpResultId( OperationType.UPSERT, tableName );
    OperationUpsert OperationUpsert = new OperationUpsert( OperationType.UPSERT, tableName, operationResultId, objectMap );

    operations.add( OperationUpsert );

    return TransactionHelper.makeOpResult( tableName, operationResultId, OperationType.UPSERT );
  }

  @Override
  public <E> OpResult bulkUpsert( List<E> instances )
  {
    List<Map<String, Object>> serializedEntities = TransactionHelper.convertInstancesToMaps( instances );

    String tableName = BackendlessSerializer.getSimpleName( instances.get( 0 ).getClass() );

    return bulkUpsert( tableName, serializedEntities );
  }

  @Override
  public OpResult bulkUpsert( String tableName, List<Map<String, Object>> arrayOfObjectMaps )
  {
    if( arrayOfObjectMaps == null || arrayOfObjectMaps.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    for( Map<String, Object> mapObject : arrayOfObjectMaps )
      if( mapObject != null )
        TransactionHelper.makeReferenceToValueFromOpResult( mapObject );
      else
        throw new IllegalArgumentException( ExceptionMessage.NULL_MAP );

    String operationResultId = opResultIdGenerator.generateOpResultId( OperationType.UPSERT_BULK, tableName );
    OperationUpsertBulk OperationUpsertBulk = new OperationUpsertBulk( OperationType.UPSERT_BULK, tableName,
                                                                       operationResultId, arrayOfObjectMaps );

    operations.add( OperationUpsertBulk );

    return TransactionHelper.makeOpResult( tableName, operationResultId, OperationType.UPSERT_BULK );
  }
}
