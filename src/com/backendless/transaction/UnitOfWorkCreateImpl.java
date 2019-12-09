package com.backendless.transaction;

import java.util.List;
import java.util.Map;

class UnitOfWorkCreateImpl implements UnitOfWorkCreate
{
  @Override
  public OpResult create( String tableName, Map<String, Object> objectMap )
  {
    return null;
  }

  @Override
  public <E> OpResult create( E instance )
  {
    return null;
  }

  @Override
  public OpResult create( String tableName, List<Map<String, Object>> arrayOfObjectMaps )
  {
    return null;
  }

  @Override
  public <E> OpResult create( List<E> instances )
  {
    return null;
  }
}
