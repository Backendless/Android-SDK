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
import java.util.concurrent.atomic.AtomicInteger;

public class RelationOperationImpl implements RelationOperation
{
  AtomicInteger countAddRelation = new AtomicInteger( 1 );
  AtomicInteger countSetRelation = new AtomicInteger( 1 );
  AtomicInteger countDeleteRelation = new AtomicInteger( 1 );

  private final List<Operation> operations;

  public RelationOperationImpl( List<Operation> operations )
  {
    this.operations = operations;
  }

  @Override
  public <E> OpResult addOperation( OperationType operationType, String parentTable,
                                    Map<String, Object> parentObject, String columnName, List<E> children )
  {
    String parentObjectId = TransactionHelper.convertObjectMapToObjectId( parentObject );
    return addOperation( operationType, parentTable, parentObjectId, columnName, children );
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
  public <E> OpResult addOperation( OperationType operationType, String parentTable, String parentObjectId,
                                    String columnName, List<E> children )
  {
    if( children == null || children.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    List<String> childrenIds = TransactionHelper.getObjectIdsFromUnknownList( children );

    return addOperation( operationType, parentTable, parentObjectId, columnName,
                         null, childrenIds );
  }

  @Override
  public OpResult addOperation( OperationType operationType, String parentTable, String parentObjectId,
                                String columnName, OpResult children )
  {
    if( children == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OP_RESULT );

    if( !OperationType.supportResultIndexType.contains( children.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    return addOperation( operationType, parentTable, parentObjectId, columnName,
                         null, children.getReference() );
  }

  @Override
  public OpResult addOperation( OperationType operationType, String parentTable, String parentObjectId,
                                String columnName, String whereClauseForChildren )
  {
    return addOperation( operationType, parentTable, parentObjectId, columnName,
                         whereClauseForChildren, null );
  }

  @Override
  public <E, U> OpResult addOperation( OperationType operationType, E parentObject, String columnName,
                                       List<U> children )
  {
    if( parentObject == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );
    String parentObjectId = Persistence.getEntityId( parentObject );
    if( parentObjectId == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OBJECT_ID_IN_INSTANCE );
    String parentTable = BackendlessSerializer.getSimpleName( parentObject.getClass() );

    List<String> childrenIds = TransactionHelper.getObjectIdsFromUnknownList( children );

    return addOperation( operationType, parentTable, parentObjectId, columnName,
                         null, childrenIds );
  }

  @Override
  public <E> OpResult addOperation( OperationType operationType, E parentObject, String columnName,
                                    OpResult children )
  {
    if( parentObject == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );
    String parentObjectId = Persistence.getEntityId( parentObject );
    if( parentObjectId == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OBJECT_ID_IN_INSTANCE );
    String parentTable = BackendlessSerializer.getSimpleName( parentObject.getClass() );

    if( children == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OP_RESULT );

    if( !OperationType.supportResultIndexType.contains( children.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    return addOperation( operationType, parentTable, parentObjectId, columnName,
                         null, children.getReference() );
  }

  @Override
  public <E> OpResult addOperation( OperationType operationType, E parentObject, String columnName,
                                    String whereClauseForChildren )
  {
    if( parentObject == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );
    String parentObjectId = Persistence.getEntityId( parentObject );
    if( parentObjectId == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OBJECT_ID_IN_INSTANCE );
    String parentTable = BackendlessSerializer.getSimpleName( parentObject.getClass() );

    return addOperation( operationType, parentTable, parentObjectId, columnName,
                         whereClauseForChildren, null );
  }

  @Override
  public <E> OpResult addOperation( OperationType operationType, OpResult parentObject,
                                    String columnName, List<E> children )
  {
    if( parentObject == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OP_RESULT );

    if( children == null || children.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    List<String> childrenIds = TransactionHelper.getObjectIdsFromUnknownList( children );

    if( !OperationType.supportEntityDescriptionResultType.contains( parentObject.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    return addOperation( operationType, parentObject.getTableName(),
                         parentObject.resolveTo( Persistence.DEFAULT_OBJECT_ID_FIELD ),
                         columnName, null, childrenIds );
  }

  @Override
  public OpResult addOperation( OperationType operationType, OpResult parentObject,
                                String columnName, OpResult children )
  {
    if( parentObject == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OP_RESULT );

    if( !OperationType.supportEntityDescriptionResultType.contains( parentObject.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    if( !OperationType.supportResultIndexType.contains( children.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    return addOperation( operationType, parentObject.getTableName(),
                         parentObject.resolveTo( Persistence.DEFAULT_OBJECT_ID_FIELD ),
                         columnName, null, children.getReference() );
  }

  @Override
  public OpResult addOperation( OperationType operationType, OpResult parentObject,
                                String columnName, String whereClauseForChildren )
  {
    if( parentObject == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OP_RESULT );

    if( !OperationType.supportEntityDescriptionResultType.contains( parentObject.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    return addOperation( operationType, parentObject.getTableName(),
                         parentObject.resolveTo( Persistence.DEFAULT_OBJECT_ID_FIELD ),
                         columnName, whereClauseForChildren, null );
  }

  @Override
  public <E> OpResult addOperation( OperationType operationType, OpResultIndex parentObject,
                                    String columnName, List<E> children )
  {
    if( parentObject == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OP_RESULT_INDEX );

    if( children == null || children.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    List<String> childrenIds = TransactionHelper.getObjectIdsFromUnknownList( children );

    if( !OperationType.supportResultIndexType.contains( parentObject.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    return addOperation( operationType, parentObject.getTableName(), parentObject.getReference(), columnName,
                         null, childrenIds );
  }

  @Override
  public OpResult addOperation( OperationType operationType, OpResultIndex parentObject,
                                String columnName, OpResult children )
  {
    if( parentObject == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OP_RESULT_INDEX );

    if( !OperationType.supportResultIndexType.contains( parentObject.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    if( !OperationType.supportResultIndexType.contains( children.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    return addOperation( operationType, parentObject.getTableName(), parentObject.getReference(), columnName,
                         null, children.getReference() );
  }

  @Override
  public OpResult addOperation( OperationType operationType, OpResultIndex parentObject,
                                String columnName, String whereClauseForChildren )
  {
    if( parentObject == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OP_RESULT_INDEX );

    if( !OperationType.supportResultIndexType.contains( parentObject.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    return addOperation( operationType, parentObject.getTableName(), parentObject.getReference(), columnName,
                         whereClauseForChildren, null );
  }

  private OpResult addOperation( OperationType operationType, String parentTable, Object parentObject,
                                 String columnName, String whereClauseForChildren, Object children )
  {
    if( parentTable == null || parentTable.equals( "" ) )
      throw new IllegalArgumentException( ExceptionMessage.NULL_PARENT_TABLE_NAME );

    if( columnName == null || columnName.equals( "" ) )
      throw new IllegalArgumentException( ExceptionMessage.NULL_RELATION_COLUMN_NAME );

    String operationResultId = null;

    Relation relation = new Relation();
    relation.setParentObject( parentObject );
    relation.setRelationColumn( columnName );
    relation.setConditional( whereClauseForChildren );
    relation.setUnconditional( children );
    switch( operationType )
    {
      case ADD_RELATION:
        operationResultId = operationType + "_" + countAddRelation.getAndIncrement();
        operations.add( new OperationAddRelation( operationType, parentTable, operationResultId, relation ) );
        break;
      case SET_RELATION:
        operationResultId = operationType + "_" + countSetRelation.getAndIncrement();
        operations.add( new OperationSetRelation( operationType, parentTable, operationResultId, relation ) );
        break;
      case DELETE_RELATION:
        operationResultId = operationType + "_" + countDeleteRelation.getAndIncrement();
        operations.add( new OperationDeleteRelation( operationType, parentTable, operationResultId, relation ) );
        break;
    }

    return TransactionHelper.makeOpResult( parentTable, operationResultId, operationType );
  }
}
