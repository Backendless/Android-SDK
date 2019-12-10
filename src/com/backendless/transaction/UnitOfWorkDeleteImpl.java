package com.backendless.transaction;

import com.backendless.persistence.BackendlessSerializer;
import com.backendless.transaction.operations.Operation;
import com.backendless.transaction.operations.OperationCreate;
import com.backendless.transaction.operations.OperationDelete;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UnitOfWorkDeleteImpl implements UnitOfWorkDelete
{
  AtomicInteger countDelete = new AtomicInteger( 1 );

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
    String operationResultId = OperationType.DELETE + "_" + countDelete.getAndIncrement();
    OperationDelete operationDelete = new OperationDelete( OperationType.DELETE, tableName, operationResultId, objectMap );

    operations.add( operationDelete );

    Map<String, Object> reference = new HashMap<>();
    reference.put( UnitOfWork.REFERENCE_MARKER, true );
    reference.put( UnitOfWork.OP_RESULT_ID, operationResultId );
    return new OpResult( reference );
  }

  @Override
  public OpResult delete( String tableName, OpResult result )
  {
    String operationResultId = OperationType.DELETE + "_" + countDelete.getAndIncrement();
    OperationDelete operationDelete = new OperationDelete( OperationType.DELETE, tableName, operationResultId,
                                                           result.getReference() );

    operations.add( operationDelete );

    Map<String, Object> reference = new HashMap<>();
    reference.put( UnitOfWork.REFERENCE_MARKER, true );
    reference.put( UnitOfWork.OP_RESULT_ID, operationResultId );
    return new OpResult( reference );
  }

  @Override
  public OpResult delete( String tableName, OpResult result, int opResultIndex )
  {
    String operationResultId = OperationType.DELETE + "_" + countDelete.getAndIncrement();
    Map<String, Object> referenceMap = result.getReference();//TODO cloning
    referenceMap.put( UnitOfWork.RESULT_INDEX, opResultIndex );
    OperationDelete operationDelete = new OperationDelete( OperationType.DELETE, tableName, operationResultId,
                                                           referenceMap );

    operations.add( operationDelete );

    Map<String, Object> reference = new HashMap<>();
    reference.put( UnitOfWork.REFERENCE_MARKER, true );
    reference.put( UnitOfWork.OP_RESULT_ID, operationResultId );
    return new OpResult( reference );
  }

  @Override
  public OpResult bulkDelete( String tableName, List<Map<String, Object>> arrayOfObjectMaps )
  {
    return null;
  }

  @Override
  public <E> OpResult bulkDelete( List<E> instances )
  {
    return null;
  }

  @Override
  public OpResult bulkDelete( String tableName, String query, OpResult result )
  {
    return null;
  }
}
