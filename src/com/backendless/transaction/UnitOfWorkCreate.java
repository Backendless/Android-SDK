package com.backendless.transaction;

import java.util.List;
import java.util.Map;

public interface UnitOfWorkCreate
{
  OpResult create( String tableName, Map<String, Object> objectMap );

  <E> OpResult create( E instance );

  OpResult create( String tableName, List<Map<String, Object>> arrayOfObjectMaps );

  <E> OpResult create( List<E> instances );
}
