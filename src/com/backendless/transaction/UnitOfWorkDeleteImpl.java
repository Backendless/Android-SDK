package com.backendless.transaction;

import com.backendless.Persistence;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.BackendlessSerializer;
import com.backendless.transaction.payload.DeleteBulkPayload;

import java.util.List;
import java.util.Map;

class UnitOfWorkDeleteImpl implements UnitOfWorkDelete
{
  private final List<Operation> operations;
  private final OpResultIdGenerator opResultIdGenerator;

  UnitOfWorkDeleteImpl( List<Operation> operations, OpResultIdGenerator opResultIdGenerator )
  {
    this.operations = operations;
    this.opResultIdGenerator = opResultIdGenerator;
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
    String objectId = TransactionHelper.convertObjectMapToObjectId( objectMap );
    return delete( tableName, objectId );
  }

  @Override
  public OpResult delete( String tableName, String objectId )
  {
    String operationResultId = opResultIdGenerator.generateOpResultId( OperationType.DELETE, tableName );
    OperationDelete operationDelete = new OperationDelete( OperationType.DELETE, tableName, operationResultId, objectId );

    operations.add( operationDelete );

    return TransactionHelper.makeOpResult( tableName, operationResultId, OperationType.DELETE );
  }

  @Override
  public OpResult delete( OpResult result )
  {
    if( result == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OP_RESULT );

    if( !OperationType.supportEntityDescriptionResultType.contains( result.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    String operationResultId = opResultIdGenerator.generateOpResultId( OperationType.DELETE, result.getTableName() );
    OperationDelete operationDelete = new OperationDelete( OperationType.DELETE, result.getTableName(), operationResultId,
                                                           result.resolveTo( Persistence.DEFAULT_OBJECT_ID_FIELD ).makeReference() );

    operations.add( operationDelete );

    return TransactionHelper.makeOpResult( result.getTableName(), operationResultId, OperationType.DELETE );
  }

  @Override
  public OpResult delete( OpResultValueReference resultIndex )
  {
    if( resultIndex == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OP_RESULT_VALUE_REFERENCE );

    if( resultIndex.getResultIndex() == null || resultIndex.getPropName() != null )
      throw new IllegalArgumentException( ExceptionMessage.OP_RESULT_INDEX_YES_PROP_NAME_NOT );

    Map<String, Object> referenceToObjectId = TransactionHelper.convertCreateBulkOrFindResultIndexToObjectId( resultIndex );

    String operationResultId = opResultIdGenerator.generateOpResultId( OperationType.DELETE, resultIndex.getOpResult().getTableName() );
    OperationDelete operationDelete = new OperationDelete( OperationType.DELETE, resultIndex.getOpResult().getTableName(),
                                                           operationResultId, referenceToObjectId );

    operations.add( operationDelete );

    return TransactionHelper.makeOpResult( resultIndex.getOpResult().getTableName(), operationResultId, OperationType.DELETE );
  }

  @Override
  public <E> OpResult bulkDelete( List<E> instances )
  {
    if( instances == null || instances.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    if( instances.get( 0 ) instanceof Map )
      throw new IllegalArgumentException( ExceptionMessage.USE_ANOTHER_METHOD_WITH_TABLE_NAME_AND_LIST_OBJECT_MAPS );
    else if( instances.get( 0 ) instanceof String )
      throw new IllegalArgumentException( ExceptionMessage.USE_ANOTHER_METHOD_WITH_TABLE_NAME_AND_LIST_IDS );

    List<Map<String, Object>> serializedEntities = TransactionHelper.convertInstancesToMaps( instances );

    String tableName =  BackendlessSerializer.getSimpleName( instances.get( 0 ).getClass() );

    return bulkDelete( tableName, serializedEntities );
  }

  @Override
  public OpResult bulkDelete( String tableName, String[] objectIdValues )
  {
    if( objectIdValues == null || objectIdValues.length == 0 )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    return bulkDelete( tableName, null, objectIdValues );
  }

  @Override
  public OpResult bulkDelete( String tableName, List<Map<String, Object>> arrayOfObjects )
  {
    if( arrayOfObjects == null || arrayOfObjects.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    List<Object> objectIds = TransactionHelper.convertMapsToObjectIds( arrayOfObjects );

    return bulkDelete( tableName, null, objectIds );
  }

  @Override
  public OpResult bulkDelete( String tableName, String whereClause )
  {
    if( whereClause == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_WHERE_CLAUSE );

    return bulkDelete( tableName, whereClause, null );
  }

  @Override
  public OpResult bulkDelete( OpResult result )
  {
    if( result == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OP_RESULT );

    if( ! ( OperationType.supportCollectionEntityDescriptionType.contains( result.getOperationType() )
            || OperationType.supportListIdsResultType.contains( result.getOperationType() ) ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    return bulkDelete( result.getTableName(), null, result.makeReference() );
  }

  private OpResult bulkDelete( String tableName, String whereClause, Object unconditional )
  {
    String operationResultId = opResultIdGenerator.generateOpResultId( OperationType.DELETE_BULK, tableName );
    DeleteBulkPayload deleteBulkPayload = new DeleteBulkPayload( whereClause, unconditional );
    OperationDeleteBulk operationDeleteBulk = new OperationDeleteBulk( OperationType.DELETE_BULK, tableName,
                                                                       operationResultId, deleteBulkPayload );

    operations.add( operationDeleteBulk );

    return TransactionHelper.makeOpResult( tableName, operationResultId, OperationType.DELETE_BULK );
  }
}
