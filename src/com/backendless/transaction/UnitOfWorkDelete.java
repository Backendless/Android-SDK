package com.backendless.transaction;

import java.util.List;
import java.util.Map;

interface UnitOfWorkDelete
{
  <E> OpResult delete( E instance );

  OpResult delete( String tableName, Map<String, Object> objectMap );

  OpResult delete( String tableName, String objectId );

  // OpResult from CREAT/UPDATE = identification object what will update (get object id)
  OpResult delete( OpResult result );

  // OpResultValueReference from FIND = identification object what will update (get object id)
  // OpResultValueReference from CREATE_BULK = already an object identifier
  OpResult delete( OpResultValueReference resultIndex );

  <E> OpResult bulkDelete( List<E> instances );

  OpResult bulkDelete( String tableName, String[] objectIdValues );

  OpResult bulkDelete( String tableName, List<Map<String, Object>> arrayOfObjects );

  OpResult bulkDelete( String tableName, String whereClause );

  // OpResult from FIND or CREATE_BULK
  OpResult bulkDelete( OpResult result );
}
