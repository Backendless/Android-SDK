package com.backendless.transaction;

import com.backendless.Persistence;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.BackendlessSerializer;
import com.backendless.transaction.operations.Operation;
import com.backendless.transaction.operations.OperationUpdate;
import com.backendless.transaction.operations.OperationUpdateBulk;
import com.backendless.transaction.payload.UpdateBulkPayload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class UnitOfWorkUpdateImpl implements UnitOfWorkUpdate
{
  private final List<Operation<?>> operations;
  private final OpResultIdGenerator opResultIdGenerator;
  private final Map<String, Class> clazzes;

  public UnitOfWorkUpdateImpl( List<Operation<?>> operations, OpResultIdGenerator opResultIdGenerator,
                               Map<String, Class> clazzes )
  {
    this.operations = operations;
    this.opResultIdGenerator = opResultIdGenerator;
    this.clazzes = clazzes;
  }

  @Override
  public <E> OpResult update( E instance )
  {
    Map<String, Object> entityMap = SerializationHelper.serializeEntityToMap( instance );
    String tableName = BackendlessSerializer.getSimpleName( instance.getClass() );

    clazzes.put( tableName, instance.getClass() );

    return update( tableName, entityMap );
  }

  @Override
  public OpResult update( OpResult result, String propertyName, Object propertyValue )
  {
    Map<String, Object> changes = new HashMap<>();
    changes.put( propertyName, propertyValue );

    return update( result, changes );
  }

  @Override
  public OpResult update( OpResult result, Map<String, Object> changes )
  {
    if( result == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OP_RESULT );

    if( !OperationType.supportEntityDescriptionResultType.contains( result.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    changes.put( Persistence.DEFAULT_OBJECT_ID_FIELD, result.resolveTo( Persistence.DEFAULT_OBJECT_ID_FIELD ).makeReference() );

    return update( result.getTableName(), changes );
  }

  @Override
  public OpResult update( OpResultValueReference result, String propertyName, Object propertyValue )
  {
    Map<String, Object> changes = new HashMap<>();
    changes.put( propertyName, propertyValue );

    return update( result, changes );
  }

  @Override
  public OpResult update( OpResultValueReference result, Map<String, Object> changes )
  {
    if( result == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OP_RESULT );

    if( result.getResultIndex() == null || result.getPropName() != null )
      throw new IllegalArgumentException( ExceptionMessage.OP_RESULT_INDEX_YES_PROP_NAME_NOT );

    if( OperationType.supportCollectionEntityDescriptionType.contains( result.getOpResult().getOperationType() ) )
      changes.put( Persistence.DEFAULT_OBJECT_ID_FIELD, result.resolveTo( Persistence.DEFAULT_OBJECT_ID_FIELD ).makeReference() );
    else if( OperationType.supportListIdsResultType.contains( result.getOpResult().getOperationType() ) )
      changes.put( Persistence.DEFAULT_OBJECT_ID_FIELD, result.makeReference() );
    else
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    return update( result.getOpResult().getTableName(), changes );
  }

  @Override
  public OpResult update( String tableName, Map<String, Object> objectMap )
  {
    if( objectMap == null || objectMap.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_MAP );

    TransactionHelper.makeReferenceToValueFromOpResult( objectMap );

    String operationResultId = opResultIdGenerator.generateOpResultId( OperationType.UPDATE, tableName );
    OperationUpdate operationUpdate = new OperationUpdate( OperationType.UPDATE, tableName, operationResultId, objectMap );

    operations.add( operationUpdate );

    return TransactionHelper.makeOpResult( tableName, operationResultId, OperationType.UPDATE );
  }

  @Override
  public OpResult bulkUpdate( String tableName, String whereClause, Map<String, Object> changes )
  {
    return bulkUpdate( tableName, whereClause, null, changes );
  }

  @Override
  public <E> OpResult bulkUpdate( String tableName, List<E> objectsForChanges, Map<String, Object> changes )
  {
    if( objectsForChanges == null || objectsForChanges.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    if( ! ( objectsForChanges.get( 0 ) instanceof String ) )
      TransactionHelper.makeReferenceToObjectIdFromOpResult( (List<Object>) objectsForChanges );

    return bulkUpdate( tableName, null, objectsForChanges, changes );
  }

  @Override
  public OpResult bulkUpdate( OpResult objectIdsForChanges, Map<String, Object> changes )
  {
    if( objectIdsForChanges == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OP_RESULT );

    if( ! ( OperationType.supportCollectionEntityDescriptionType.contains( objectIdsForChanges.getOperationType() )
            || OperationType.supportListIdsResultType.contains( objectIdsForChanges.getOperationType() ) ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    return bulkUpdate( objectIdsForChanges.getTableName(), null, objectIdsForChanges.makeReference(), changes );
  }

  private OpResult bulkUpdate( String tableName, String whereClause, Object objectsForChanges,
                               Map<String, Object> changes )
  {
    if( changes == null || changes.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_MAP );

    TransactionHelper.removeSystemField( changes );

    TransactionHelper.makeReferenceToValueFromOpResult( changes );

    String operationResultId = opResultIdGenerator.generateOpResultId( OperationType.UPDATE_BULK, tableName );
    UpdateBulkPayload updateBulkPayload = new UpdateBulkPayload( whereClause, objectsForChanges, changes );
    OperationUpdateBulk operationUpdateBulk = new OperationUpdateBulk( OperationType.UPDATE_BULK, tableName,
                                                                       operationResultId, updateBulkPayload );

    operations.add( operationUpdateBulk );

    return TransactionHelper.makeOpResult( tableName, operationResultId, OperationType.UPDATE_BULK );
  }
}
