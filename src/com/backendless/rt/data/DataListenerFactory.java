package com.backendless.rt.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataListenerFactory
{
  private final ConcurrentHashMap<String, DataListener> listeners = new ConcurrentHashMap<>();
  private final Object lock = new Object();

  public <T> DataListener<T> of( final Class<T> entity )
  {
    return of( entity.getName(), new Provider<T>()
    {
      @Override
      public DataListener<T> create()
      {
        return new DataListener( entity );
      }
    } );
  }

  public DataListener<Map> of( final String tableName )
  {
    return of( tableName, new Provider()
    {
      @Override
      public DataListener create()
      {
        return new DataListener( tableName );
      }
    } );
  }

  private <T> DataListener<T> of( final String key, Provider provider )
  {
    DataListener listener = listeners.get( key );

    if( listener == null )
    {
      synchronized( lock )
      {
        listener = listeners.get( key );

        if( listener == null )
        {
          listener = provider.create();
          listeners.put( key, listener );
        }
      }
    }

    return listener;
  }

  private interface Provider<T>
  {
    DataListener<T> create();
  }
}
