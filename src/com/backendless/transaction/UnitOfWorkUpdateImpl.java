package com.backendless.transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class UnitOfWorkUpdateImpl implements UnitOFWorkUpdate
{
  @Override
  public OpResult update( String tableName, Map<String, Object> objectMap )
  {
    return null;
  }

  @Override
  public <E> OpResult update( E instance )
  {
    return null;
  }

  @Override
  public OpResult update( String tableName, String query, Map<String, Object> objectMap )
  {
    return null;
  }

  @Override
  public OpResult update( String tableName, List<HashMap<String, Object>> arrayOfHashMaps )
  {
    return null;
  }

  @Override
  public <E> OpResult update( List<E> instances )
  {
    return null;
  }

  @Override
  public <E> OpResult bulkUpdate( String query, E instances )
  {
    return null;
  }
}
