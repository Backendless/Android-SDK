package com.backendless.rt;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RTListener
{
  private final Map<String, RTSubscription> subscriptions = new ConcurrentHashMap<>(  );

  private RTClient rt = RTClientFactory.get();

  protected void addEventListener( RTSubscription subscription )
  {
    subscriptions.put( subscription.getId(), subscription );
    rt.subscribe( subscription );
  }
}
