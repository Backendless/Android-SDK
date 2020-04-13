package com.backendless.transaction;

import java.util.List;
import java.util.Map;

interface UnitOfWorkCreate
{
  <E> OpResult create( E instance );

  OpResult create( String tableName, Map<String, Object> objectMap );

  <E> OpResult bulkCreate( List<E> instances );

  OpResult bulkCreate( String tableName, List<Map<String, Object>> arrayOfObjectMaps );
}
