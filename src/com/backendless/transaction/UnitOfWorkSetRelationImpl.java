package com.backendless.transaction;

import java.util.List;
import java.util.Map;

public class UnitOfWorkSetRelationImpl implements UnitOfWorkSetRelation
{
  private final RelationOperation relationOperation;

  public UnitOfWorkSetRelationImpl( RelationOperation relationOperation )
  {
    this.relationOperation = relationOperation;
  }

  @Override
  public <E> OpResult setToRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                     List<E> children )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult setToRelation( String parentTable, Map<String, Object> parentObject,
                                 String columnName, OpResult children )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentTable,
                                           parentObject, columnName, children );
  }

  @Override
  public OpResult setToRelation( String parentTable, Map<String, Object> parentObject,
                                 String columnName, String whereClauseForChildren )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentTable, parentObject,
                                           columnName, whereClauseForChildren );
  }

  @Override
  public <E> OpResult setToRelation( String parentTable, String parentObjectId, String columnName, List<E> children )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentTable,
                                           parentObjectId, columnName, children );
  }

  @Override
  public OpResult setToRelation( String parentTable, String parentObjectId, String columnName, OpResult children )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentTable,
                                           parentObjectId, columnName, children );
  }

  @Override
  public OpResult setToRelation( String parentTable, String parentObjectId, String columnName, String whereClauseForChildren )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentTable, parentObjectId,
                                           columnName, whereClauseForChildren);
  }

  @Override
  public <E, U> OpResult setToRelation( E parentObject, String columnName, List<U> children )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentObject, columnName, children );
  }

  @Override
  public <E> OpResult setToRelation( E parentObject, String columnName, OpResult children )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentObject, columnName, children );
  }

  @Override
  public <E> OpResult setToRelation( E parentObject, String columnName, String whereClauseForChildren )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentObject, columnName, whereClauseForChildren );
  }

  @Override
  public <E> OpResult setToRelation( String parentTable, OpResult parentObject, String columnName, List<E> children )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult setToRelation( String parentTable, OpResult parentObject, String columnName, OpResult children )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult setToRelation( String parentTable, OpResult parentObject, String columnName, String whereClauseForChildren )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentTable, parentObject,
                                           columnName, whereClauseForChildren );
  }

  @Override
  public <E> OpResult setToRelation( String parentTable, OpResultIndex parentObject, String columnName, List<E> children )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult setToRelation( String parentTable, OpResultIndex parentObject, String columnName, OpResult children )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult setToRelation( String parentTable, OpResultIndex parentObject, String columnName,
                                 String whereClauseForChildren )
  {
    return relationOperation.addOperation( OperationType.SET_RELATION, parentTable, parentObject,
                                           columnName, whereClauseForChildren );
  }
}
