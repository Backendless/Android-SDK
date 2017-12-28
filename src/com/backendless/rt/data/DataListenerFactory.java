package com.backendless.rt.data;

import com.backendless.rt.AbstractListenerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataListenerFactory extends AbstractListenerFactory<DataListener>
{
  @SuppressWarnings( "unchecked" )
  public <T> DataListener<T> of( final Class<T> entity )
  {
    return create( entity.getName(), new Provider()
    {
      @Override
      public DataListener<T> create()
      {
        return new DataListener( entity );
      }
    } );
  }

  @SuppressWarnings( "unchecked" )
  public DataListener<Map> of( final String tableName )
  {
    return create( tableName, new Provider()
    {
      @Override
      public DataListener<Map> create()
      {
        return new DataListener( tableName );
      }
    } );
  }
}
