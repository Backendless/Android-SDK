package com.backendless.transaction;

import java.util.List;
import java.util.Map;

class UnitOfWorkSetRelationImpl implements UnitOfWorkSetRelation
{
  private final RelationOperation relationOperation;

  UnitOfWorkSetRelationImpl( RelationOperation relationOperation )
  {
    this.relationOperation = relationOperation;
  }

  @Override
  public OpResult setRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                               String[] childrenObjectIds )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentTable, parentObject, columnName, childrenObjectIds );
  }

  @Override
  public <E> OpResult setRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                     List<E> children )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult setRelation( String parentTable, Map<String, Object> parentObject,
                                 String columnName, OpResult children )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentTable,
                                           parentObject, columnName, children );
  }

  @Override
  public OpResult setRelation( String parentTable, Map<String, Object> parentObject,
                                 String columnName, String whereClauseForChildren )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentTable, parentObject,
                                           columnName, whereClauseForChildren );
  }

  @Override
  public OpResult setRelation( String parentTable, String parentObjectId, String columnName,
                               String[] childrenObjectIds )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentTable,
                                           parentObjectId, columnName, childrenObjectIds );
  }

  @Override
  public <E> OpResult setRelation( String parentTable, String parentObjectId, String columnName, List<E> children )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentTable,
                                           parentObjectId, columnName, children );
  }

  @Override
  public OpResult setRelation( String parentTable, String parentObjectId, String columnName, OpResult children )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentTable,
                                           parentObjectId, columnName, children );
  }

  @Override
  public OpResult setRelation( String parentTable, String parentObjectId, String columnName, String whereClauseForChildren )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentTable, parentObjectId,
                                           columnName, whereClauseForChildren);
  }

  @Override
  public <E> OpResult setRelation( E parentObject, String columnName, String[] childrenObjectIds )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentObject, columnName, childrenObjectIds );
  }

  @Override
  public <E, U> OpResult setRelation( E parentObject, String columnName, List<U> children )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentObject, columnName, children );
  }

  @Override
  public <E> OpResult setRelation( E parentObject, String columnName, OpResult children )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentObject, columnName, children );
  }

  @Override
  public <E> OpResult setRelation( E parentObject, String columnName, String whereClauseForChildren )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentObject, columnName, whereClauseForChildren );
  }

  @Override
  public OpResult setRelation( OpResult parentObject, String columnName, String[] childrenObjectIds )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentObject, columnName, childrenObjectIds );
  }

  @Override
  public <E> OpResult setRelation( OpResult parentObject, String columnName, List<E> children )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentObject, columnName, children );
  }

  @Override
  public OpResult setRelation( OpResult parentObject, String columnName, OpResult children )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentObject, columnName, children );
  }

  @Override
  public OpResult setRelation( OpResult parentObject, String columnName, String whereClauseForChildren )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentObject, columnName, whereClauseForChildren );
  }

  @Override
  public OpResult setRelation( OpResultValueReference parentObject, String columnName, String[] childrenObjectIds )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentObject, columnName, childrenObjectIds );
  }

  @Override
  public <E> OpResult setRelation( OpResultValueReference parentObject, String columnName, List<E> children )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentObject, columnName, children );
  }

  @Override
  public OpResult setRelation( OpResultValueReference parentObject, String columnName, OpResult children )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentObject, columnName, children );
  }

  @Override
  public OpResult setRelation( OpResultValueReference parentObject, String columnName, String whereClauseForChildren )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentObject, columnName, whereClauseForChildren );
  }
}
