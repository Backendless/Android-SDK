package com.backendless.transaction;

import java.util.List;
import java.util.Map;

public interface UnitOfWorkAddRelation
{
  <E, U> OpResult addToRelation( E parentObject, String columnName, List<U> children );

  OpResult addToRelation( String parentTable, Map<String, Object> parentObject,
                          String columnName, List<Map<String, Object>> children );

  <E> OpResult addToRelation( String parentTable, OpResult parentObject, String columnName, List<E> children );

  <E> OpResult addToRelation( E parentObject, String columnName, String whereClauseForChildren );

  OpResult addToRelation( String parentTable, Map<String, Object> parentObject,
                          String columnName, String whereClauseForChildren );

  OpResult addToRelation( String parentTable, OpResult parentObject, String columnName, String whereClauseForChildren );
}
