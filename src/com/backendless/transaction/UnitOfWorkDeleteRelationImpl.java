package com.backendless.transaction;

import java.util.List;
import java.util.Map;

class UnitOfWorkDeleteRelationImpl implements UnitOfWorkDeleteRelation
{
  private final RelationOperation relationOperation;

  UnitOfWorkDeleteRelationImpl( RelationOperation relationOperation )
  {
    this.relationOperation = relationOperation;
  }

  @Override
  public OpResult deleteRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                  String[] childrenObjectIds )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentTable,
                                           parentObject, columnName, childrenObjectIds );
  }

  @Override
  public <E> OpResult deleteRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                      E[] childrenInstance )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentTable,
                                           parentObject, columnName, childrenInstance );
  }

  @Override
  public OpResult deleteRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                  List<Map<String, Object>> childrenMaps )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentTable,
                                           parentObject, columnName, childrenMaps );
  }

  @Override
  public OpResult deleteRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                  OpResult children )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentTable,
                                           parentObject, columnName, children );
  }

  @Override
  public OpResult deleteRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                  String whereClauseForChildren )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentTable, parentObject,
                                           columnName, whereClauseForChildren );
  }

  @Override
  public OpResult deleteRelation( String parentTable, String parentObjectId, String columnName,
                                  String[] childrenObjectIds )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentTable,
                                           parentObjectId, columnName, childrenObjectIds );
  }

  @Override
  public <E> OpResult deleteRelation( String parentTable, String parentObjectId, String columnName,
                                      E[] childrenInstances )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentTable,
                                           parentObjectId, columnName, childrenInstances );
  }

  @Override
  public OpResult deleteRelation( String parentTable, String parentObjectId, String columnName,
                                  List<Map<String, Object>> childrenMaps )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentTable,
                                           parentObjectId, columnName, childrenMaps );
  }

  @Override
  public OpResult deleteRelation( String parentTable, String parentObjectId, String columnName, OpResult children )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentTable,
                                           parentObjectId, columnName, children );
  }

  @Override
  public OpResult deleteRelation( String parentTable, String parentObjectId, String columnName,
                                  String whereClauseForChildren )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentTable,
                                           parentObjectId, columnName, whereClauseForChildren);
  }

  @Override
  public <E> OpResult deleteRelation( E parentObject, String columnName, String[] childrenObjectIds )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentObject, columnName, childrenObjectIds );
  }

  @Override
  public <E, U> OpResult deleteRelation( E parentObject, String columnName, U[] childrenInstances )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentObject, columnName, childrenInstances );
  }

  @Override
  public <E> OpResult deleteRelation( E parentObject, String columnName, List<Map<String, Object>> childrenMaps )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentObject, columnName, childrenMaps );
  }

  @Override
  public <E> OpResult deleteRelation( E parentObject, String columnName, OpResult children )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentObject, columnName, children );
  }

  @Override
  public <E> OpResult deleteRelation( E parentObject, String columnName, String whereClauseForChildren )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentObject,
                                           columnName, whereClauseForChildren );
  }

  @Override
  public OpResult deleteRelation( OpResult parentObject, String columnName, String[] childrenObjectIds )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentObject, columnName, childrenObjectIds );
  }

  @Override
  public <E> OpResult deleteRelation( OpResult parentObject, String columnName, E[] childrenInstances )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentObject, columnName, childrenInstances );
  }

  @Override
  public OpResult deleteRelation( OpResult parentObject, String columnName, List<Map<String, Object>> childrenMaps )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentObject, columnName, childrenMaps );
  }

  @Override
  public OpResult deleteRelation( OpResult parentObject, String columnName, OpResult children )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentObject, columnName, children );
  }

  @Override
  public OpResult deleteRelation( OpResult parentObject, String columnName,
                                  String whereClauseForChildren )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentObject, columnName, whereClauseForChildren );
  }

  @Override
  public OpResult deleteRelation( OpResultValueReference parentObject, String columnName, String[] childrenObjectIds )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentObject, columnName, childrenObjectIds );
  }

  @Override
  public <E> OpResult deleteRelation( OpResultValueReference parentObject, String columnName, E[] childrenInstances )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentObject, columnName, childrenInstances );
  }

  @Override
  public OpResult deleteRelation( OpResultValueReference parentObject, String columnName,
                                  List<Map<String, Object>> childrenMaps )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentObject, columnName, childrenMaps );
  }

  @Override
  public OpResult deleteRelation( OpResultValueReference parentObject, String columnName, OpResult children )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentObject, columnName, children );
  }

  @Override
  public OpResult deleteRelation( OpResultValueReference parentObject, String columnName, String whereClauseForChildren )
  {
    return relationOperation.addOperation( OperationType.DELETE_RELATION, parentObject, columnName, whereClauseForChildren );
  }
}
