package com.backendless.transaction;

import java.util.List;
import java.util.Map;

public interface UnitOfWorkDelete
{
  <E> OpResult delete( E instance );

  OpResult delete( String tableName, Map<String, Object> objectMap );

  OpResult delete( String tableName, OpResult result );

  OpResult delete( String tableName, OpResult result, int opResultIndex );

  OpResult delete( String tableName, List<Map<String, Object>> arrayOfObjectMaps );

  <E> OpResult delete( List<E> instances );

  OpResult bulkDelete( String tableName, String query, OpResult result );
}
