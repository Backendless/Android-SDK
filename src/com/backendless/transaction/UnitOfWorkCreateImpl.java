package com.backendless.transaction;

import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.BackendlessSerializer;
import com.backendless.transaction.operations.Operation;
import com.backendless.transaction.operations.OperationCreate;
import com.backendless.transaction.operations.OperationCreateBulk;

import java.util.List;
import java.util.Map;

class UnitOfWorkCreateImpl implements UnitOfWorkCreate
{
  private final List<Operation<?>> operations;
  private final OpResultIdGenerator opResultIdGenerator;
  private final Map<String, Class> clazzes;

  public UnitOfWorkCreateImpl( List<Operation<?>> operations, OpResultIdGenerator opResultIdGenerator,
                               Map<String, Class> clazzes )
  {
    this.operations = operations;
    this.opResultIdGenerator = opResultIdGenerator;
    this.clazzes = clazzes;
  }

  @Override
  public <E> OpResult create( E instance )
  {
    Map<String, Object> entityMap = SerializationHelper.serializeEntityToMap( instance );
    String tableName = BackendlessSerializer.getSimpleName( instance.getClass() );

    clazzes.put( tableName, instance.getClass() );

    return create( tableName, entityMap );
  }

  @Override
  public OpResult create( String tableName, Map<String, Object> objectMap )
  {
    if( objectMap == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_MAP );

    String operationResultId = opResultIdGenerator.generateOpResultId( OperationType.CREATE, tableName );
    OperationCreate operationCreate = new OperationCreate( OperationType.CREATE, tableName, operationResultId, objectMap );

    operations.add( operationCreate );

    return TransactionHelper.makeOpResult( tableName, operationResultId, OperationType.CREATE );
  }

  @Override
  public <E> OpResult bulkCreate( List<E> instances )
  {
    List<Map<String, Object>> serializedEntities = TransactionHelper.convertInstancesToMaps( instances );

    String tableName = BackendlessSerializer.getSimpleName( instances.get( 0 ).getClass() );

    return bulkCreate( tableName, serializedEntities );
  }

  @Override
  public OpResult bulkCreate( String tableName, List<Map<String, Object>> arrayOfObjectMaps )
  {
    if( arrayOfObjectMaps == null || arrayOfObjectMaps.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    for( Map<String, Object> mapObject : arrayOfObjectMaps )
      if( mapObject == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_MAP );

    String operationResultId = opResultIdGenerator.generateOpResultId( OperationType.CREATE_BULK, tableName );
    OperationCreateBulk operationCreateBulk = new OperationCreateBulk( OperationType.CREATE_BULK, tableName,
                                                                       operationResultId, arrayOfObjectMaps );

    operations.add( operationCreateBulk );

    return TransactionHelper.makeOpResult( tableName, operationResultId, OperationType.CREATE_BULK );
  }
}
