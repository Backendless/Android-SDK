package com.backendless.transaction;

import java.util.List;
import java.util.Map;

public interface UnitOfWorkAddRelation
{
  // HashMap + List of hashmaps
  // HashMap + List of custom classes
  <E> OpResult addToRelation( String parentTable, Map<String, Object> parentObject,
                              String columnName, List<E> children );

  // HashMap + OpResult
  OpResult addToRelation( String parentTable, Map<String, Object> parentObject,
                          String columnName, OpResult children );

  // HashMap + whereClause
  OpResult addToRelation( String parentTable, Map<String, Object> parentObject,
                          String columnName, String whereClauseForChildren );

  // Custom class + List of hashmaps
  // Custom class + List of custom classes
  <E, U> OpResult addToRelation( E parentObject, String columnName, List<U> children );

  // Custom class + OpResult
  <E> OpResult addToRelation( E parentObject, String columnName, OpResult children );

  // Custom class + whereClause
  <E> OpResult addToRelation( E parentObject, String columnName, String whereClauseForChildren );

  // OpResult + List of hashmaps
  // OpResult + List of custom classes
  <E> OpResult addToRelation( String parentTable, OpResult parentObject, String columnName, List<E> children );

  // OpResult + OpResult
  OpResult addToRelation( String parentTable, OpResult parentObject, String columnName, OpResult children );

  // OpResult + where clause
  OpResult addToRelation( String parentTable, OpResult parentObject, String columnName, String whereClauseForChildren );
}
