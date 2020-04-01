package com.backendless.transaction;

import java.util.List;
import java.util.Map;

class UnitOfWorkAddRelationImpl implements UnitOfWorkAddRelation
{
  private final RelationOperation relationOperation;

  UnitOfWorkAddRelationImpl( RelationOperation relationOperation )
  {
    this.relationOperation = relationOperation;
  }

  @Override
  public OpResult addToRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                 String[] childrenObjectIds )
  {
    return relationOperation.addOperation( OperationType.ADD_RELATION, parentTable,
                                           parentObject, columnName, childrenObjectIds );
  }

  @Override
  public <E> OpResult addToRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                     List<E> children )
  {
    return relationOperation.addOperation( OperationType.ADD_RELATION, parentTable,
                                           parentObject, columnName, children );
  }

  @Override
  public OpResult addToRelation( String parentTable, Map<String, Object> parentObject,
                                 String columnName, OpResult children )
  {
    return relationOperation.addOperation( OperationType.ADD_RELATION, parentTable,
                                           parentObject, columnName, children );
  }

  @Override
  public OpResult addToRelation( String parentTable, Map<String, Object> parentObject,
                                 String columnName, String whereClauseForChildren )
  {
    return relationOperation.addOperation( OperationType.ADD_RELATION, parentTable,
                                           parentObject, columnName, whereClauseForChildren );
  }

  @Override
  public OpResult addToRelation( String parentTable, String parentObjectId, String columnName,
                                 String[] childrenObjectIds )
  {
    return relationOperation.addOperation( OperationType.ADD_RELATION, parentTable,
                                           parentObjectId, columnName, childrenObjectIds );
  }

  @Override
  public <E> OpResult addToRelation( String parentTable, String parentObjectId, String columnName, List<E> children )
  {
    return relationOperation.addOperation( OperationType.ADD_RELATION, parentTable,
                                           parentObjectId, columnName, children );
  }

  @Override
  public OpResult addToRelation( String parentTable, String parentObjectId, String columnName, OpResult children )
  {
    return relationOperation.addOperation( OperationType.ADD_RELATION, parentTable,
                                           parentObjectId, columnName, children );
  }

  @Override
  public OpResult addToRelation( String parentTable, String parentObjectId, String columnName,
                                 String whereClauseForChildren )
  {
    return relationOperation.addOperation( OperationType.ADD_RELATION, parentTable,
                                           parentObjectId, columnName, whereClauseForChildren );
  }

  @Override
  public <E> OpResult addToRelation( E parentObject, String columnName, String[] childrenObjectIds )
  {
    return relationOperation.addOperation( OperationType.ADD_RELATION, parentObject, columnName, childrenObjectIds );
  }

  @Override
  public <E, U> OpResult addToRelation( E parentObject, String columnName, List<U> children )
  {
    return relationOperation.addOperation( OperationType.ADD_RELATION, parentObject, columnName, children );
  }

  @Override
  public <E> OpResult addToRelation( E parentObject, String columnName, OpResult children )
  {
    return relationOperation.addOperation( OperationType.ADD_RELATION, parentObject, columnName, children );
  }

  @Override
  public <E> OpResult addToRelation( E parentObject, String columnName, String whereClauseForChildren )
  {
    return relationOperation.addOperation( OperationType.ADD_RELATION, parentObject,
                                           columnName, whereClauseForChildren );
  }

  @Override
  public OpResult addToRelation( OpResult parentObject, String columnName, String[] childrenObjectIds )
  {
    return relationOperation.addOperation( OperationType.ADD_RELATION, parentObject, columnName, childrenObjectIds );
  }

  @Override
  public <E> OpResult addToRelation( OpResult parentObject, String columnName, List<E> children )
  {
    return relationOperation.addOperation( OperationType.ADD_RELATION, parentObject, columnName, children );
  }

  @Override
  public OpResult addToRelation( OpResult parentObject, String columnName, OpResult children )
  {
    return relationOperation.addOperation( OperationType.ADD_RELATION, parentObject, columnName, children );
  }

  @Override
  public OpResult addToRelation( OpResult parentObject, String columnName, String whereClauseForChildren )
  {
    return relationOperation.addOperation( OperationType.ADD_RELATION, parentObject, columnName, whereClauseForChildren );
  }

  @Override
  public OpResult addToRelation( OpResultValueReference parentObject, String columnName, String[] childrenObjectIds )
  {
    return relationOperation.addOperation( OperationType.ADD_RELATION, parentObject, columnName, childrenObjectIds );
  }

  @Override
  public <E> OpResult addToRelation( OpResultValueReference parentObject, String columnName, List<E> children )
  {
    return relationOperation.addOperation( OperationType.ADD_RELATION, parentObject, columnName, children );
  }

  @Override
  public OpResult addToRelation( OpResultValueReference parentObject, String columnName, OpResult children )
  {
    return relationOperation.addOperation( OperationType.ADD_RELATION, parentObject, columnName, children );
  }

  @Override
  public OpResult addToRelation( OpResultValueReference parentObject, String columnName, String whereClauseForChildren )
  {
    return relationOperation.addOperation( OperationType.ADD_RELATION, parentObject, columnName, whereClauseForChildren );
  }
}
