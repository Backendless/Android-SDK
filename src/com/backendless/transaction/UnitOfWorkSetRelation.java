package com.backendless.transaction;

import java.util.List;
import java.util.Map;

interface UnitOfWorkSetRelation
{
  // HashMap + array of objectIds
  OpResult setRelation( String parentTable, Map<String, Object> parentObject, String columnName, String[] childrenObjectIds );

  // HashMap + array of custom classes
  <E> OpResult setRelation( String parentTable, Map<String, Object> parentObject, String columnName, E[] childrenInstance );

  // HashMap + List of hashmaps
  OpResult setRelation( String parentTable, Map<String, Object> parentObject, String columnName, List<Map<String, Object>> childrenMaps );

  // HashMap + OpResult=CREATE_BULK
  OpResult setRelation( String parentTable, Map<String, Object> parentObject, String columnName, OpResult children );

  // HashMap + whereClause
  OpResult setRelation( String parentTable, Map<String, Object> parentObject,
                          String columnName, String whereClauseForChildren );

  // String + array of objectIds
  OpResult setRelation( String parentTable, String parentObjectId, String columnName, String[] childrenObjectIds );

  // String + array of custom classes
  <E> OpResult setRelation( String parentTable, String parentObjectId, String columnName, E[] childrenInstances );

  // String + List of hashmaps
  OpResult setRelation( String parentTable, String parentObjectId, String columnName, List<Map<String, Object>> childrenMaps );

  // String + OpResult=CREATE_BULK
  OpResult setRelation( String parentTable, String parentObjectId, String columnName, OpResult children );

  // String + whereClause
  OpResult setRelation( String parentTable, String parentObjectId, String columnName, String whereClauseForChildren );

  // Custom class + array of objectIds
  <E> OpResult setRelation( E parentObject, String columnName, String[] childrenObjectIds );

  // Custom class + array of custom classes
  <E, U> OpResult setRelation( E parentObject, String columnName, U[] childrenInstances );

  // Custom class + List of hashmaps
  <E> OpResult setRelation( E parentObject, String columnName, List<Map<String, Object>> childrenMaps );

  // Custom class + OpResult=CREATE_BULK
  <E> OpResult setRelation( E parentObject, String columnName, OpResult children );

  // Custom class + whereClause
  <E> OpResult setRelation( E parentObject, String columnName, String whereClauseForChildren );

  // OpResult=CREATE/UPDATE(getObjectId) + array of objectIds
  OpResult setRelation( OpResult parentObject, String columnName, String[] childrenObjectIds );

  // OpResult=CREATE/UPDATE(getObjectId) + array of custom classes
  <E> OpResult setRelation( OpResult parentObject, String columnName, E[] childrenInstances );

  // OpResult=CREATE/UPDATE(getObjectId) + List of hashmaps
  OpResult setRelation( OpResult parentObject, String columnName, List<Map<String, Object>> childrenMaps );

  // OpResult=CREATE/UPDATE(getObjectId) + OpResult=CREATE_BULK
  OpResult setRelation( OpResult parentObject, String columnName, OpResult children );

  // OpResult=CREATE/UPDATE(getObjectId) + where clause
  OpResult setRelation( OpResult parentObject, String columnName, String whereClauseForChildren );

  // OpResult=CREATE_BULK(resultIndex) + array of objectIds
  OpResult setRelation( OpResultValueReference parentObject, String columnName, String[] childrenObjectIds );

  // OpResultValueReference=CREATE_BULK/FIND(getObjectId)+ array of custom classes
  <E> OpResult setRelation( OpResultValueReference parentObject, String columnName, E[] childrenInstances );

  // OpResultValueReference=CREATE_BULK/FIND(getObjectId) + List of hashmaps
  OpResult setRelation( OpResultValueReference parentObject, String columnName, List<Map<String, Object>> childrenMaps );

  // OpResult=CREATE_BULK(resultIndex) + OpResult=CREATE_BULK
  OpResult setRelation( OpResultValueReference parentObject, String columnName, OpResult children );

  // OpResult=CREATE_BULK(resultIndex) + where clause
  OpResult setRelation( OpResultValueReference parentObject, String columnName, String whereClauseForChildren );
}
