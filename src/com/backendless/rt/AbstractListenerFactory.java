package com.backendless.rt;

import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractListenerFactory<E extends RTListener>
{
  private final ConcurrentHashMap<String, E> listeners = new ConcurrentHashMap<>();
  private final Object lock = new Object();

  protected E create( final String key, Provider<E> provider )
  {
    E listener = listeners.get( key );

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

  protected interface Provider<T extends RTListener>
  {
    T create();
  }
}
