package com.backendless.transaction;

import java.util.List;
import java.util.Map;

public interface UnitOfWorkUpdate
{
  <E> OpResult update( E instance );

  OpResult update( String tableName, Map<String, Object> objectMap );

  <E> OpResult bulkUpdate( String whereClause, E changes );

  OpResult bulkUpdate( String tableName, String whereClause, Map<String, Object> changes );

  OpResult bulkUpdate( String tableName, List<Map<String, Object>> objectsForChanges, Map<String, Object> changes );

  OpResult bulkUpdate( String tableName, OpResult objectsForChanges, Map<String, Object> changes );
}
