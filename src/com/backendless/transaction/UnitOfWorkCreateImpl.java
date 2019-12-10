package com.backendless.transaction;

import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.BackendlessSerializer;
import com.backendless.transaction.operations.Operation;
import com.backendless.transaction.operations.OperationCreate;
import com.backendless.transaction.operations.OperationCreateBulk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UnitOfWorkCreateImpl implements UnitOfWorkCreate
{
  AtomicInteger countCreate = new AtomicInteger( 1 );
  AtomicInteger countCreateBulk = new AtomicInteger( 1 );

  private final List<Operation> operations;

  public UnitOfWorkCreateImpl( List<Operation> operations )
  {
    this.operations = operations;
  }

  @Override
  public <E> OpResult create( E instance )
  {
    Map<String, Object> entityMap = SerializationHelper.serializeEntityToMap( instance );
    String tableName = BackendlessSerializer.getSimpleName( instance.getClass() );

    return create( tableName, entityMap );
  }

  @Override
  public OpResult create( String tableName, Map<String, Object> objectMap )
  {
    if( objectMap == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_MAP );

    String operationResultId = OperationType.CREATE + "_" + countCreate.getAndIncrement();
    OperationCreate operationCreate = new OperationCreate( OperationType.CREATE, tableName, operationResultId, objectMap );

    operations.add( operationCreate );

    return TransactionHelper.makeOpResult( operationResultId );
  }

  @Override
  public <E> OpResult bulkCreate( List<E> instances )
  {
    if( instances == null || instances.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    List<Map<String, Object>> serializedEntities = new ArrayList<>();
    for ( final Object entity : instances )
    {
      serializedEntities.add( SerializationHelper.serializeEntityToMap( entity ) );
    }

    String tableName =  BackendlessSerializer.getSimpleName( instances.get( 0 ).getClass() );

    return bulkCreate( tableName, serializedEntities );
  }

  @Override
  public OpResult bulkCreate( String tableName, List<Map<String, Object>> arrayOfObjectMaps )
  {
    if( arrayOfObjectMaps == null || arrayOfObjectMaps.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    String operationResultId = OperationType.CREATE_BULK + "_" + countCreateBulk.getAndIncrement();
    OperationCreateBulk operationCreateBulk = new OperationCreateBulk( OperationType.CREATE_BULK, tableName,
                                                                       operationResultId, arrayOfObjectMaps );

    operations.add( operationCreateBulk );

    return TransactionHelper.makeOpResult( operationResultId );
  }
}
