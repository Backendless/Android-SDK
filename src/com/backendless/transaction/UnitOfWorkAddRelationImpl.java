package com.backendless.transaction;

import com.backendless.Persistence;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.BackendlessSerializer;
import com.backendless.transaction.operations.Operation;
import com.backendless.transaction.operations.OperationAddRelation;
import com.backendless.transaction.payload.Relation;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UnitOfWorkAddRelationImpl implements UnitOfWorkAddRelation
{
  AtomicInteger countAddRelation = new AtomicInteger( 1 );

  private final List<Operation> operations;

  public UnitOfWorkAddRelationImpl( List<Operation> operations )
  {
    this.operations = operations;
  }

  @Override
  public <E> OpResult addToRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                     List<E> children )
  {
    if( children == null || children.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    String parentObjectId = (String) parentObject.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    List<String> childrenIds = TransactionHelper.getObjectIdsFromUnknownList( children );

    return addToRelation( parentTable, parentObjectId, columnName, null, childrenIds );
  }

  @Override
  public OpResult addToRelation( String parentTable, Map<String, Object> parentObject,
                                 String columnName, OpResult children )
  {
    String parentObjectId = (String) parentObject.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    if( !OperationType.supportResultIndexType.contains( children.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    return addToRelation( parentTable, parentObjectId, columnName, null, children.getReference() );
  }

  @Override
  public OpResult addToRelation( String parentTable, Map<String, Object> parentObject,
                                 String columnName, String whereClauseForChildren )
  {
    String parentObjectId = (String) parentObject.get( Persistence.DEFAULT_OBJECT_ID_FIELD );
    return addToRelation( parentTable, parentObjectId, columnName, whereClauseForChildren, null );
  }

  @Override
  public <E, U> OpResult addToRelation( E parentObject, String columnName, List<U> children )
  {
    String parentObjectId = Persistence.getEntityId( parentObject );
    String parentTable = BackendlessSerializer.getSimpleName( parentObject.getClass() );

    List<String> childrenIds = TransactionHelper.getObjectIdsFromUnknownList( children );

    return addToRelation( parentTable, parentObjectId, columnName, null, childrenIds );
  }

  @Override
  public <E> OpResult addToRelation( E parentObject, String columnName, OpResult children )
  {
    String parentObjectId = Persistence.getEntityId( parentObject );
    String parentTable = BackendlessSerializer.getSimpleName( parentObject.getClass() );

    if( !OperationType.supportResultIndexType.contains( children.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    return addToRelation( parentTable, parentObjectId, columnName, null, children.getReference() );
  }

  @Override
  public <E> OpResult addToRelation( E parentObject, String columnName, String whereClauseForChildren )
  {
    String parentObjectId = Persistence.getEntityId( parentObject );
    String parentTable = BackendlessSerializer.getSimpleName( parentObject.getClass() );

    return addToRelation( parentTable, parentObjectId, columnName, whereClauseForChildren, null );
  }

  @Override
  public <E> OpResult addToRelation( String parentTable, OpResult parentObject, String columnName, List<E> children )
  {
    if( children == null || children.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    List<String> childrenIds = TransactionHelper.getObjectIdsFromUnknownList( children );

    if( !OperationType.supportEntityDescriptionResultType.contains( parentObject.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    return addToRelation( parentTable, parentObject.resolveTo( Persistence.DEFAULT_OBJECT_ID_FIELD ),
                          columnName, null, childrenIds );
  }

  @Override
  public OpResult addToRelation( String parentTable, OpResult parentObject, String columnName, OpResult children )
  {
    if( !OperationType.supportEntityDescriptionResultType.contains( parentObject.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    if( !OperationType.supportResultIndexType.contains( children.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    return addToRelation( parentTable, parentObject.resolveTo( Persistence.DEFAULT_OBJECT_ID_FIELD ),
                          null, columnName, children.getReference() );
  }

  @Override
  public OpResult addToRelation( String parentTable, OpResult parentObject, String columnName, String whereClauseForChildren )
  {
    if( !OperationType.supportEntityDescriptionResultType.contains( parentObject.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    return addToRelation( parentTable, parentObject.resolveTo( Persistence.DEFAULT_OBJECT_ID_FIELD ),
                          columnName, whereClauseForChildren, null );
  }

  private OpResult addToRelation( String parentTable, Object parentObject, String columnName,
                                  String whereClauseForChildren, Object children )
  {
    String operationResultId = OperationType.ADD_RELATION + "_" + countAddRelation.getAndIncrement();

    Relation relation = new Relation();
    relation.setParentObject( parentObject );
    relation.setRelationColumn( columnName );
    relation.setConditional( whereClauseForChildren );
    relation.setUnconditional( children );
    OperationAddRelation operationAddRelation = new OperationAddRelation( OperationType.ADD_RELATION, parentTable,
                                                                          operationResultId, relation );

    operations.add( operationAddRelation );

    return TransactionHelper.makeOpResult( operationResultId, OperationType.ADD_RELATION );
  }
}
