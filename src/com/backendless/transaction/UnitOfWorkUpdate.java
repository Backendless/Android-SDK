package com.backendless.transaction;

import java.util.List;
import java.util.Map;

interface UnitOfWorkUpdate
{
  <E> OpResult update( E instance );

  OpResult update( String tableName, Map<String, Object> objectMap );

  // OpResult from CREATE/UPDATE = identification object what will update (get object id)
  OpResult update( OpResult result, Map<String, Object> changes );

  // OpResult from CREATE/UPDATE = identification object what will update (get object id)
  OpResult update( OpResult result, String propertyName, Object propertyValue );

  // OpResultValueReference from FIND = identification object what will update (get object id)
  // OpResultValueReference from CREATE_BULK = already an object identifier
  OpResult update( OpResultValueReference result, Map<String, Object> changes );

  // OpResultValueReference from FIND = identification object what will update (get object id)
  // OpResultValueReference from CREATE_BULK = already an object identifier
  OpResult update( OpResultValueReference result, String propertyName, Object propertyValue );

  OpResult bulkUpdate( String tableName, String whereClause, Map<String, Object> changes );

  OpResult bulkUpdate( String tableName, List<String> objectsForChanges, Map<String, Object> changes );

  // OpResult from FIND or CREATE_BULK
  OpResult bulkUpdate( OpResult objectIdsForChanges, Map<String, Object> changes );
}
