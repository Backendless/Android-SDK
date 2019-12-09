package com.backendless.transaction;

import java.util.List;
import java.util.Map;

class UnitOfWorkDeleteImpl implements UnitOfWorkDelete
{
  @Override
  public OpResult delete( String tableName, Map<String, Object> objectMap )
  {
    return null;
  }

  @Override
  public <E> OpResult delete( E instance )
  {
    return null;
  }

  @Override
  public OpResult delete( String tableName, OpResult result )
  {
    return null;
  }

  @Override
  public OpResult delete( String tableName, OpResult result, int opResultIndex )
  {
    return null;
  }

  @Override
  public OpResult delete( String tableName, List<Map<String, Object>> arrayOfObjectMaps )
  {
    return null;
  }

  @Override
  public <E> OpResult delete( List<E> instances )
  {
    return null;
  }

  @Override
  public OpResult bulkDelete( String tableName, String query, OpResult result )
  {
    return null;
  }
}
