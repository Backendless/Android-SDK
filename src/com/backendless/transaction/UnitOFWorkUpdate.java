package com.backendless.transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UnitOFWorkUpdate
{
  OpResult update( String tableName, Map<String, Object> objectMap );

  <E> OpResult update( E instance );

  OpResult update( String tableName, String query, Map<String, Object> objectMap );

  OpResult update( String tableName, List<HashMap<String, Object>> arrayOfHashMaps );

  <E> OpResult update( List<E> instances );

  <E> OpResult bulkUpdate( String query, E instances );
}
