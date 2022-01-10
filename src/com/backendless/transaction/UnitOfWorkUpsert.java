package com.backendless.transaction;

import java.util.List;
import java.util.Map;

public interface UnitOfWorkUpsert
{
  <E> OpResult upsert( E instance );

  OpResult upsert( String tableName, Map<String, Object> objectMap );

  <E> OpResult bulkUpsert( List<E> instances );

  OpResult bulkUpsert( String tableName, List<Map<String, Object>> arrayOfObjectMaps );
}
