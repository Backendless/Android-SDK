package com.backendless.transaction;

import java.util.List;
import java.util.Map;

interface RelationOperation
{
  // HashMap + array of objectIds
  OpResult addOperation( OperationType operationType, String parentTable, Map<String, Object> parentObject,
                             String columnName, String[] childrenObjectIds );

  // HashMap + array of custom classes
  <E> OpResult addOperation( OperationType operationType, String parentTable,
                             Map<String, Object> parentObject, String columnName, E[] childrenInstances );

  // HashMap + List of hashmaps
  OpResult addOperation( OperationType operationType, String parentTable,
                         Map<String, Object> parentObject, String columnName, List<Map<String, Object>> childrenMaps );

  // HashMap + OpResult=CREATE_BULK or FIND
  OpResult addOperation( OperationType operationType, String parentTable, Map<String, Object> parentObject,
                         String columnName, OpResult children );

  // HashMap + whereClause
  OpResult addOperation( OperationType operationType, String parentTable, Map<String, Object> parentObject,
                         String columnName, String whereClauseForChildren );

  // String + array of objectIds
  OpResult addOperation( OperationType operationType, String parentTable, String parentObjectId,
                         String columnName, String[] childrenObjectIds );

  // String + array of custom classes
  <E> OpResult addOperation( OperationType operationType, String parentTable, String parentObjectId,
                             String columnName, E[] childrenInstances );

  // String + List of hashmaps
  OpResult addOperation( OperationType operationType, String parentTable, String parentObjectId,
                         String columnName, List<Map<String, Object>> childrenMaps );

  // String + OpResult=CREATE_BULK or FIND
  OpResult addOperation( OperationType operationType, String parentTable, String parentObjectId,
                         String columnName, OpResult children );

  // String + whereClause
  OpResult addOperation( OperationType operationType, String parentTable, String parentObjectId,
                         String columnName, String whereClauseForChildren );

  // Custom class + array of objectIds
  <E> OpResult addOperation( OperationType operationType, E parentObject, String columnName, String[] childrenObjectIds );

  // Custom class + array of custom classes
  <E, U> OpResult addOperation( OperationType operationType, E parentObject, String columnName, U[] childrenInstances );

  // Custom class + List of hashmaps
  <E> OpResult addOperation( OperationType operationType, E parentObject, String columnName, List<Map<String, Object>> childrenMaps );

  // Custom class + OpResult=CREATE_BULK or FIND
  <E> OpResult addOperation( OperationType operationType, E parentObject, String columnName, OpResult children );

  // Custom class + whereClause
  <E> OpResult addOperation( OperationType operationType, E parentObject, String columnName,
                             String whereClauseForChildren );

  // OpResult=CREATE/UPDATE(getObjectId) + array of objectIds
  OpResult addOperation( OperationType operationType, OpResult parentObject, String columnName, String[] childrenObjectIds );

  // OpResult=CREATE/UPDATE(getObjectId) + array of custom classes
  <E> OpResult addOperation( OperationType operationType, OpResult parentObject, String columnName, E[] childrenInstances );

  // OpResult=CREATE/UPDATE(getObjectId) + List of hashmaps
  OpResult addOperation( OperationType operationType, OpResult parentObject,
                         String columnName, List<Map<String, Object>> childrenMaps );

  // OpResult=CREATE/UPDATE(getObjectId) + OpResult=CREATE_BULK or FIND
  OpResult addOperation( OperationType operationType, OpResult parentObject,
                         String columnName, OpResult children );

  // OpResult=CREATE/UPDATE(getObjectId) + where clause
  OpResult addOperation( OperationType operationType, OpResult parentObject,
                         String columnName, String whereClauseForChildren );

  // OpResultValueReference=CREATE_BULK/FIND(getObjectId) + array of objectIds
  OpResult addOperation( OperationType operationType, OpResultValueReference parentObject,
                         String columnName, String[] childrenObjectIds );

  // OpResultValueReference=CREATE_BULK/FIND(getObjectId)+ array of custom classes
  <E> OpResult addOperation( OperationType operationType, OpResultValueReference parentObject,
                             String columnName, E[] childrenInstances );

  // OpResultValueReference=CREATE_BULK/FIND(getObjectId) + List of hashmaps
  OpResult addOperation( OperationType operationType, OpResultValueReference parentObject,
                             String columnName, List<Map<String, Object>> childrenMaps );

  // OpResultValueReference=CREATE_BULK/FIND(getObjectId) + OpResult=CREATE_BULK or FIND
  OpResult addOperation( OperationType operationType, OpResultValueReference parentObject,
                         String columnName, OpResult children );

  // OpResultValueReference=CREATE_BULK/FIND(getObjectId) + where clause
  OpResult addOperation( OperationType operationType, OpResultValueReference parentObject,
                         String columnName, String whereClauseForChildren );
}
