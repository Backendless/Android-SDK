package com.backendless.rt;

public abstract class AbstractListenerFactory<E extends RTListener>
{
  protected E create( final String key, Provider<E> provider )
  {
    return provider.create();
  }

  protected interface Provider<T extends RTListener>
  {
    T create();
  }
}
