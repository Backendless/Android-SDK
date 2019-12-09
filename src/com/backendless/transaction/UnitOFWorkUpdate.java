package com.backendless.transaction;

import java.util.List;
import java.util.Map;

public interface UnitOFWorkUpdate
{
  <E> OpResult update( E instance );

  OpResult update( String tableName, Map<String, Object> objectMap );

  <E> OpResult update( List<E> instances );

  OpResult update( String tableName, List<Map<String, Object>> arrayOfHashMaps );

  <E> OpResult bulkUpdate( String whereClause, List<E> instances );

  OpResult bulkUpdate( String tableName, String whereClause, Map<String, Object> changes );
}
