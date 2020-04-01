package com.backendless.transaction;

import java.util.List;
import java.util.Map;

interface UnitOfWorkAddRelation
{
  // HashMap + array of objectIds
  OpResult addToRelation( String parentTable, Map<String, Object> parentObject, String columnName, String[] childrenObjectIds );

  // HashMap + List of hashmaps
  // HashMap + List of custom classes
  <E> OpResult addToRelation( String parentTable, Map<String, Object> parentObject, String columnName, List<E> children );

  // HashMap + OpResult=CREATE_BULK
  OpResult addToRelation( String parentTable, Map<String, Object> parentObject, String columnName, OpResult children );

  // HashMap + whereClause
  OpResult addToRelation( String parentTable, Map<String, Object> parentObject,
                          String columnName, String whereClauseForChildren );

  // String + array of objectIds
  OpResult addToRelation( String parentTable, String parentObjectId, String columnName, String[] childrenObjectIds );

  // String + List of hashmaps
  // String + List of custom classes
  <E> OpResult addToRelation( String parentTable, String parentObjectId, String columnName, List<E> children );

  // String + OpResult=CREATE_BULK
  OpResult addToRelation( String parentTable, String parentObjectId, String columnName, OpResult children );

  // String + whereClause
  OpResult addToRelation( String parentTable, String parentObjectId, String columnName, String whereClauseForChildren );

  // Custom class + array of objectIds
  <E> OpResult addToRelation( E parentObject, String columnName, String[] childrenObjectIds );

  // Custom class + List of hashmaps
  // Custom class + List of custom classes
  <E, U> OpResult addToRelation( E parentObject, String columnName, List<U> children );

  // Custom class + OpResult=CREATE_BULK
  <E> OpResult addToRelation( E parentObject, String columnName, OpResult children );

  // Custom class + whereClause
  <E> OpResult addToRelation( E parentObject, String columnName, String whereClauseForChildren );

  // OpResult=CREATE/UPDATE(getObjectId) + array of objectIds
  OpResult addToRelation( OpResult parentObject, String columnName, String[] childrenObjectIds );

  // OpResult=CREATE/UPDATE(getObjectId) + List of hashmaps
  // OpResult=CREATE/UPDATE(getObjectId) + List of custom classes
  <E> OpResult addToRelation( OpResult parentObject, String columnName, List<E> children );

  // OpResult=CREATE/UPDATE(getObjectId) + OpResult=CREATE_BULK
  OpResult addToRelation( OpResult parentObject, String columnName, OpResult children );

  // OpResult=CREATE/UPDATE(getObjectId) + where clause
  OpResult addToRelation( OpResult parentObject, String columnName, String whereClauseForChildren );

  // OpResult=CREATE_BULK(resultIndex) + array of objectIds
  OpResult addToRelation( OpResultValueReference parentObject, String columnName, String[] childrenObjectIds );

  // OpResult=CREATE_BULK(resultIndex) + List of hashmaps
  // OpResult=CREATE_BULK(resultIndex) + List of custom classes
  <E> OpResult addToRelation( OpResultValueReference parentObject, String columnName, List<E> children );

  // OpResult=CREATE_BULK(resultIndex) + OpResult=CREATE_BULK
  OpResult addToRelation( OpResultValueReference parentObject, String columnName, OpResult children );

  // OpResult=CREATE_BULK(resultIndex) + where clause
  OpResult addToRelation( OpResultValueReference parentObject, String columnName, String whereClauseForChildren );
}
