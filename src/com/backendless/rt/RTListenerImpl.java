package com.backendless.rt;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RTListenerImpl implements RTListener
{
  private final Map<String, RTSubscription> subscriptions = new ConcurrentHashMap<>(  );

  private RTClient rt = RTClientFactory.get();

  protected void addEventListener( RTSubscription subscription )
  {
    subscriptions.put( subscription.getId(), subscription );
    rt.subscribe( subscription );
  }

  protected void removeEventListener( final RTSubscription subscription )
  {
    removeEventListener( new Predicate()
    {
      @Override
      public boolean test( RTSubscription other )
      {
        return other.equals( subscription );
      }
    } );
  }

  protected void removeEventListener( Predicate criteria )
  {
    Iterator<RTSubscription> iterator = subscriptions.values().iterator();

    while( iterator.hasNext() )
    {
      RTSubscription rtSubscription = iterator.next();

      if( criteria.test( rtSubscription ) )
      {
        rt.unsubscribe( rtSubscription.getId() );
        iterator.remove();
      }
    }
  }
}
