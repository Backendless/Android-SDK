package com.backendless.transaction;

import com.backendless.Persistence;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.BackendlessSerializer;
import com.backendless.transaction.operations.Operation;
import com.backendless.transaction.operations.OperationAddRelation;
import com.backendless.transaction.operations.OperationDeleteRelation;
import com.backendless.transaction.operations.OperationSetRelation;
import com.backendless.transaction.payload.Relation;

import java.util.List;
import java.util.Map;

class RelationOperationImpl implements RelationOperation
{
  private final List<Operation<?>> operations;
  private final OpResultIdGenerator opResultIdGenerator;

  public RelationOperationImpl( List<Operation<?>> operations, OpResultIdGenerator opResultIdGenerator )
  {
    this.operations = operations;
    this.opResultIdGenerator = opResultIdGenerator;
  }

  @Override
  public OpResult addOperation( OperationType operationType, String parentTable, Map<String, Object> parentObject,
                                String columnName, String[] childrenObjectIds )
  {
    String parentObjectId = TransactionHelper.convertObjectMapToObjectId( parentObject );

    if( childrenObjectIds == null || childrenObjectIds.length == 0 )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    return addOperation( operationType, parentTable, parentObjectId, columnName, null, childrenObjectIds );
  }

  @Override
  public <E> OpResult addOperation( OperationType operationType, String parentTable,
                                    Map<String, Object> parentObject, String columnName, E[] childrenInstances )
  {
    if( childrenInstances == null || childrenInstances.length == 0 )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    if( childrenInstances[ 0 ] instanceof String )
    {
      String[] childrenObjectIds = (String[]) childrenInstances;
      return addOperation( operationType, parentTable, parentObject, columnName, childrenObjectIds );
    }

    String parentObjectId = TransactionHelper.convertObjectMapToObjectId( parentObject );
    return addOperation( operationType, parentTable, parentObjectId, columnName, childrenInstances );
  }

  @Override
  public OpResult addOperation( OperationType operationType, String parentTable,
                                Map<String, Object> parentObject, String columnName, List<Map<String, Object>> childrenMaps )
  {
    String parentObjectId = TransactionHelper.convertObjectMapToObjectId( parentObject );
    return addOperation( operationType, parentTable, parentObjectId, columnName, childrenMaps );
  }

  @Override
  public OpResult addOperation( OperationType operationType, String parentTable,
                                Map<String, Object> parentObject, String columnName, OpResult children )
  {
    String parentObjectId = TransactionHelper.convertObjectMapToObjectId( parentObject );
    return addOperation( operationType, parentTable, parentObjectId, columnName, children );
  }

  @Override
  public OpResult addOperation( OperationType operationType, String parentTable, Map<String, Object> parentObject,
                                String columnName, String whereClauseForChildren )
  {
    String parentObjectId = TransactionHelper.convertObjectMapToObjectId( parentObject );
    return addOperation( operationType, parentTable, parentObjectId, columnName, whereClauseForChildren );
  }

  @Override
  public OpResult addOperation( OperationType operationType, String parentTable, String parentObjectId,
                                String columnName, String[] childrenObjectIds )
  {
    if( childrenObjectIds == null || childrenObjectIds.length == 0 )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    return addOperation( operationType, parentTable, parentObjectId, columnName, null, childrenObjectIds );
  }

  @Override
  public <E> OpResult addOperation( OperationType operationType, String parentTable, String parentObjectId,
                                    String columnName, E[] childrenInstances )
  {
    if( childrenInstances == null || childrenInstances.length == 0 )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    if( childrenInstances[ 0 ] instanceof String )
    {
      String[] childrenObjectIds = (String[]) childrenInstances;
      return addOperation( operationType, parentTable, parentObjectId, columnName, childrenObjectIds );
    }

    List<String> childrenIds = getChildrenFromArrayInstances( childrenInstances );

    return addOperation( operationType, parentTable, parentObjectId, columnName,
                         null, childrenIds );
  }

  @Override
  public OpResult addOperation( OperationType operationType, String parentTable, String parentObjectId,
                                    String columnName, List<Map<String, Object>> childrenMaps )
  {
    List<Object> childrenIds = getChildrenFromListMap( childrenMaps );

    return addOperation( operationType, parentTable, parentObjectId, columnName,
                         null, childrenIds );
  }

  @Override
  public OpResult addOperation( OperationType operationType, String parentTable, String parentObjectId,
                                String columnName, OpResult children )
  {
    checkOpResultForChildren( children );

    return addOperation( operationType, parentTable, parentObjectId, columnName,
                         null, children.makeReference() );
  }

  @Override
  public OpResult addOperation( OperationType operationType, String parentTable, String parentObjectId,
                                String columnName, String whereClauseForChildren )
  {
    return addOperation( operationType, parentTable, parentObjectId, columnName,
                         whereClauseForChildren, null );
  }

  @Override
  public <E> OpResult addOperation( OperationType operationType, E parentObject, String columnName,
                                    String[] childrenObjectIds )
  {
    String parentObjectId = getParentObjectIdFromInstance( parentObject );
    String parentTable = BackendlessSerializer.getSimpleName( parentObject.getClass() );

    if( childrenObjectIds == null || childrenObjectIds.length == 0 )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    return addOperation( operationType, parentTable, parentObjectId, columnName, null, childrenObjectIds );
  }

  @Override
  public <E, U> OpResult addOperation( OperationType operationType, E parentObject, String columnName, U[] childrenInstances )
  {
    if( childrenInstances == null || childrenInstances.length == 0 )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    if( childrenInstances[ 0 ] instanceof String )
    {
      String[] childrenObjectIds = (String[]) childrenInstances;
      return addOperation( operationType, parentObject, columnName, childrenObjectIds );
    }

    String parentObjectId = getParentObjectIdFromInstance( parentObject );
    String parentTable = BackendlessSerializer.getSimpleName( parentObject.getClass() );

    List<String> childrenIds = getChildrenFromArrayInstances( childrenInstances );

    return addOperation( operationType, parentTable, parentObjectId, columnName, null, childrenIds );
  }

  @Override
  public <E> OpResult addOperation( OperationType operationType, E parentObject, String columnName, List<Map<String, Object>> childrenMaps )
  {
    String parentObjectId = getParentObjectIdFromInstance( parentObject );
    String parentTable = BackendlessSerializer.getSimpleName( parentObject.getClass() );

    List<Object> childrenIds = getChildrenFromListMap( childrenMaps );

    return addOperation( operationType, parentTable, parentObjectId, columnName, null, childrenIds );
  }

  @Override
  public <E> OpResult addOperation( OperationType operationType, E parentObject, String columnName,
                                    OpResult children )
  {
    String parentObjectId = getParentObjectIdFromInstance( parentObject );
    String parentTable = BackendlessSerializer.getSimpleName( parentObject.getClass() );

    checkOpResultForChildren( children );

    return addOperation( operationType, parentTable, parentObjectId, columnName,
                         null, children.makeReference() );
  }

  @Override
  public <E> OpResult addOperation( OperationType operationType, E parentObject, String columnName,
                                    String whereClauseForChildren )
  {
    String parentObjectId = getParentObjectIdFromInstance( parentObject );
    String parentTable = BackendlessSerializer.getSimpleName( parentObject.getClass() );

    return addOperation( operationType, parentTable, parentObjectId, columnName, whereClauseForChildren, null );
  }

  @Override
  public OpResult addOperation( OperationType operationType, OpResult parentObject, String columnName,
                                String[] childrenObjectIds )
  {
    if( childrenObjectIds == null || childrenObjectIds.length == 0 )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    return addOperation( operationType, parentObject.getTableName(),
                         parentObject.resolveTo( Persistence.DEFAULT_OBJECT_ID_FIELD ).makeReference(),
                         columnName, null, childrenObjectIds );
  }

  @Override
  public <E> OpResult addOperation( OperationType operationType, OpResult parentObject, String columnName, E[] childrenInstances )
  {
    if( childrenInstances == null || childrenInstances.length == 0 )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    if( childrenInstances[ 0 ] instanceof String )
    {
      String[] childrenObjectIds = (String[]) childrenInstances;
      return addOperation( operationType, parentObject, columnName, childrenObjectIds );
    }

    checkOpResultFoParent( parentObject );

    List<String> childrenIds = getChildrenFromArrayInstances( childrenInstances );

    return addOperation( operationType, parentObject.getTableName(),
                         parentObject.resolveTo( Persistence.DEFAULT_OBJECT_ID_FIELD ).makeReference(),
                         columnName, null, childrenIds );
  }

  @Override
  public OpResult addOperation( OperationType operationType, OpResult parentObject,
                                String columnName, List<Map<String, Object>> childrenMaps )
  {
    checkOpResultFoParent( parentObject );

    List<Object> childrenIds = getChildrenFromListMap( childrenMaps );

    return addOperation( operationType, parentObject.getTableName(),
                         parentObject.resolveTo( Persistence.DEFAULT_OBJECT_ID_FIELD ).makeReference(),
                         columnName, null, childrenIds );
  }

  @Override
  public OpResult addOperation( OperationType operationType, OpResult parentObject,
                                String columnName, OpResult children )
  {
    checkOpResultFoParent( parentObject );

    checkOpResultForChildren( children );

    return addOperation( operationType, parentObject.getTableName(),
                         parentObject.resolveTo( Persistence.DEFAULT_OBJECT_ID_FIELD ).makeReference(),
                         columnName, null, children.makeReference() );
  }

  @Override
  public OpResult addOperation( OperationType operationType, OpResult parentObject,
                                String columnName, String whereClauseForChildren )
  {
    checkOpResultFoParent( parentObject );

    return addOperation( operationType, parentObject.getTableName(),
                         parentObject.resolveTo( Persistence.DEFAULT_OBJECT_ID_FIELD ).makeReference(),
                         columnName, whereClauseForChildren, null );
  }

  @Override
  public OpResult addOperation( OperationType operationType, OpResultValueReference parentObject, String columnName,
                                String[] childrenObjectIds )
  {
    Map<String, Object> referenceToObjectId = getReferenceToParentFromOpResultValue( parentObject );

    if( childrenObjectIds == null || childrenObjectIds.length == 0 )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    return addOperation( operationType, parentObject.getOpResult().getTableName(), referenceToObjectId, columnName,
                         null, childrenObjectIds );
  }

  @Override
  public <E> OpResult addOperation( OperationType operationType, OpResultValueReference parentObject,
                                    String columnName, E[] childrenInstances )
  {
    if( childrenInstances == null || childrenInstances.length == 0 )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    if( childrenInstances[ 0 ] instanceof String )
    {
      String[] childrenObjectIds = (String[]) childrenInstances;
      return addOperation( operationType, parentObject, columnName, childrenObjectIds );
    }

    Map<String, Object> referenceToObjectId = getReferenceToParentFromOpResultValue( parentObject );

    List<String> childrenIds = getChildrenFromArrayInstances( childrenInstances );

    return addOperation( operationType, parentObject.getOpResult().getTableName(), referenceToObjectId, columnName,
                         null, childrenIds );
  }

  @Override
  public OpResult addOperation( OperationType operationType, OpResultValueReference parentObject,
                                String columnName, List<Map<String, Object>> childrenMaps )
  {
    Map<String, Object> referenceToObjectId = getReferenceToParentFromOpResultValue( parentObject );

    List<Object> childrenIds = getChildrenFromListMap( childrenMaps );

    return addOperation( operationType, parentObject.getOpResult().getTableName(), referenceToObjectId, columnName,
                         null, childrenIds );
  }

  @Override
  public OpResult addOperation( OperationType operationType, OpResultValueReference parentObject,
                                String columnName, OpResult children )
  {
    Map<String, Object> referenceToObjectId = getReferenceToParentFromOpResultValue( parentObject );

    checkOpResultForChildren( children );

    return addOperation( operationType, parentObject.getOpResult().getTableName(), referenceToObjectId, columnName,
                         null, children.makeReference() );
  }

  @Override
  public OpResult addOperation( OperationType operationType, OpResultValueReference parentObject,
                                String columnName, String whereClauseForChildren )
  {
    Map<String, Object> referenceToObjectId = getReferenceToParentFromOpResultValue( parentObject );

    return addOperation( operationType, parentObject.getOpResult().getTableName(), referenceToObjectId, columnName,
                         whereClauseForChildren, null );
  }

  private OpResult addOperation( OperationType operationType, String parentTable, Object parentObject,
                                 String columnName, String whereClauseForChildren, Object children )
  {
    if( parentTable == null || parentTable.equals( "" ) )
      throw new IllegalArgumentException( ExceptionMessage.NULL_PARENT_TABLE_NAME );

    if( columnName == null || columnName.equals( "" ) )
      throw new IllegalArgumentException( ExceptionMessage.NULL_RELATION_COLUMN_NAME );

    String operationResultId = opResultIdGenerator.generateOpResultId( operationType, parentTable );

    Relation relation = new Relation();
    relation.setParentObject( parentObject );
    relation.setRelationColumn( columnName );
    relation.setConditional( whereClauseForChildren );
    relation.setUnconditional( children );
    switch( operationType )
    {
      case ADD_RELATION:
        operations.add( new OperationAddRelation( operationType, parentTable, operationResultId, relation ) );
        break;
      case SET_RELATION:
        operations.add( new OperationSetRelation( operationType, parentTable, operationResultId, relation ) );
        break;
      case DELETE_RELATION:
        operations.add( new OperationDeleteRelation( operationType, parentTable, operationResultId, relation ) );
        break;
    }

    return TransactionHelper.makeOpResult( parentTable, operationResultId, operationType );
  }

  private void checkOpResultFoParent( OpResult parentObject )
  {
    if( parentObject == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OP_RESULT );

    if( !OperationType.supportEntityDescriptionResultType.contains( parentObject.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );
  }

  private <E> String getParentObjectIdFromInstance( E parentObject )
  {
    if( parentObject == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );
    String parentObjectId = Persistence.getEntityId( parentObject );
    if( parentObjectId == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OBJECT_ID_IN_INSTANCE );
    return parentObjectId;
  }

  private Map<String, Object> getReferenceToParentFromOpResultValue( OpResultValueReference parentObject )
  {
    if( parentObject == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OP_RESULT_VALUE_REFERENCE );

    if( parentObject.getResultIndex() == null || parentObject.getPropName() != null )
      throw new IllegalArgumentException( ExceptionMessage.OP_RESULT_INDEX_YES_PROP_NAME_NOT );

    return TransactionHelper.convertCreateBulkOrFindResultIndexToObjectId( parentObject );
  }

  private void checkOpResultForChildren( OpResult children )
  {
    if( children == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OP_RESULT );

    if( ! ( OperationType.supportCollectionEntityDescriptionType.contains( children.getOperationType() )
            || OperationType.supportListIdsResultType.contains( children.getOperationType() ) ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );
  }

  private List<Object> getChildrenFromListMap( List<Map<String, Object>> childrenMaps )
  {
    if( childrenMaps == null || childrenMaps.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    return TransactionHelper.convertMapsToObjectIds( childrenMaps );
  }

  private <E> List<String> getChildrenFromArrayInstances( E[] children )
  {
    if( children == null || children.length == 0 )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    return TransactionHelper.getObjectIdsFromListInstances( children );
  }
}
