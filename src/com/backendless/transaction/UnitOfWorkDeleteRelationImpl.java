package com.backendless.transaction;

import java.util.List;
import java.util.Map;

public class UnitOfWorkDeleteRelationImpl implements UnitOfWorkDeleteRelation
{
  private final RelationOperation relationOperation;

  public UnitOfWorkDeleteRelationImpl( RelationOperation relationOperation )
  {
    this.relationOperation = relationOperation;
  }

  @Override
  public <E> OpResult deleteRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                      List<E> children )
  {
    return null;
  }

  @Override
  public OpResult deleteRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                  OpResult children )
  {
    return null;
  }

  @Override
  public OpResult deleteRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                  String whereClauseForChildren )
  {
    return null;
  }

  @Override
  public <E> OpResult deleteRelation( String parentTable, String parentObjectId, String columnName, List<E> children )
  {
    return null;
  }

  @Override
  public OpResult deleteRelation( String parentTable, String parentObjectId, String columnName, OpResult children )
  {
    return null;
  }

  @Override
  public OpResult deleteRelation( String parentTable, String parentObjectId, String columnName,
                                  String whereClauseForChildren )
  {
    return null;
  }

  @Override
  public <E, U> OpResult deleteRelation( E parentObject, String columnName, List<U> children )
  {
    return null;
  }

  @Override
  public <E> OpResult deleteRelation( E parentObject, String columnName, OpResult children )
  {
    return null;
  }

  @Override
  public <E> OpResult deleteRelation( E parentObject, String columnName, String whereClauseForChildren )
  {
    return null;
  }

  @Override
  public <E> OpResult deleteRelation( String parentTable, OpResult parentObject, String columnName, List<E> children )
  {
    return null;
  }

  @Override
  public OpResult deleteRelation( String parentTable, OpResult parentObject, String columnName, OpResult children )
  {
    return null;
  }

  @Override
  public OpResult deleteRelation( String parentTable, OpResult parentObject, String columnName,
                                  String whereClauseForChildren )
  {
    return null;
  }

  @Override
  public <E> OpResult deleteRelation( String parentTable, OpResultIndex parentObject, String columnName,
                                      List<E> children )
  {
    return null;
  }

  @Override
  public OpResult deleteRelation( String parentTable, OpResultIndex parentObject, String columnName, OpResult children )
  {
    return null;
  }

  @Override
  public OpResult deleteRelation( String parentTable, OpResultIndex parentObject, String columnName,
                                  String whereClauseForChildren )
  {
    return null;
  }
}
