package com.backendless.transaction;

import java.util.List;
import java.util.Map;

public interface UnitOfWorkDelete
{
  <E> OpResult delete( E instance );

  OpResult delete( String tableName, Map<String, Object> objectMap );

  OpResult delete( String tableName, String objectId );

  OpResult delete( OpResult result );

  OpResult delete( OpResultIndex resultIndex );

  <E> OpResult bulkDelete( List<E> instances );

  <E> OpResult bulkDelete( String tableName, List<E> arrayOfObjects );

  OpResult bulkDelete( String tableName, String whereClause );

  OpResult bulkDelete( String tableName, OpResult result );
}
