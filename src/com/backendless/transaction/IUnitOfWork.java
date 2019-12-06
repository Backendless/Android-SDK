package com.backendless.transaction;

import java.util.List;
import java.util.Map;

public interface IUnitOfWork
{
  OpResult create( String tableName, Map<String, Object> objectMap );

  <E> OpResult create( E instance );

  OpResult bulkCreate( String tableName, List<Map<String, Object>> arrayOfObjectMaps );

  <E> OpResult bulkCreate( List<E> instances );

  OpResult update( String tableName, Map<String, Object> objectMap );

  <E> OpResult update( E instance );

  OpResult bulkUpdate( String tableName, List<Map<String, Object>> arrayOfObjectMaps );

  <E> OpResult bulkUpdate( List<E> instances );

  OpResult delete( String tableName, Map<String, Object> objectMap );

  <E> OpResult delete( E instance );

  OpResult delete( String tableName, OpResult result );

  OpResult delete( String tableName, OpResult result, int opResultIndex );

  OpResult bulkDelete( String tableName, List<Map<String, Object>> arrayOfObjectMaps );

  <E> OpResult bulkDelete( List<E> instances );

  OpResult bulkDelete( String tableName, OpResult result );

  UnitOfWorkStatus execute();
}
