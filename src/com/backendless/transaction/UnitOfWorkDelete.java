package com.backendless.transaction;

import java.util.List;
import java.util.Map;

public interface UnitOfWorkDelete
{
  <E> OpResult delete( E instance );

  OpResult delete( String tableName, Map<String, Object> objectMap );

  OpResult delete( String tableName, String objectId );

  OpResult delete( String tableName, OpResult result );

  OpResult delete( String tableName, OpResultIndex resultIndex );

  <E> OpResult bulkDelete( List<E> instances );

  OpResult bulkDelete( String tableName, List<Map<String, Object>> arrayOfObjectMaps );

  OpResult bulkDelete( String tableName, String whereClause );

  OpResult bulkDelete( String tableName, OpResult result, String propName );
}