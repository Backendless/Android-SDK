package com.backendless.rt;

import com.backendless.async.callback.AsyncCallback;
import weborb.types.IAdaptingType;
import weborb.v3types.GUID;

import java.util.HashMap;
import java.util.Map;

public class RTSubscription
{
  private final String id;
  private AsyncCallback<IAdaptingType> callback;
  private final SubscriptionNames subscriptionName;
  private final Map<String, Object> options = new HashMap<>(  );

  protected RTSubscription( SubscriptionNames subscriptionName )
  {
    this.subscriptionName = subscriptionName;
    this.id = new GUID().toString();
  }

  public String getId()
  {
    return id;
  }


  public AsyncCallback<IAdaptingType> getCallback()
  {
    return callback;
  }

  public RTSubscription setCallback( AsyncCallback<IAdaptingType> callback )
  {
    this.callback = callback;
    return this;
  }

  protected RTSubscription putOption(String key, Object value)
  {
    options.put( key, value );
    return this;
  }

  @Override
  public String toString()
  {
    return "RTSubscription{" + "id='" + id + '\'' + ", callback=" + callback + ", subscriptionName=" + subscriptionName + ", options=" + options + '}';
  }

  Map<String, Object> toArgs()
  {
    final Map<String, Object> args = new HashMap<>(  );

    args.put( "id", id );
    args.put( "name", subscriptionName.name()  );
    args.put( "options", options );

    return args;
  }
}
