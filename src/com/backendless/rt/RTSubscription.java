package com.backendless.rt;

import com.backendless.async.callback.AsyncCallback;
import weborb.v3types.GUID;

import java.util.ArrayList;
import java.util.Collection;

class RTSubscription<T>
{
  private final String id;
  private AsyncCallback<T> callback;
  private final RTTypes rtType;
  private final RTEvent rtEvent;
  private SubscriptionNames subscriptionName;
  private final Collection<String> options = new ArrayList<>(  );

  RTSubscription( RTTypes rtType, RTEvent rtEvent )
  {
    this.id = new GUID().toString();
    this.rtType = rtType;
    this.rtEvent = rtEvent;
  }

  public String getId()
  {
    return id;
  }


  public AsyncCallback<T> getCallback()
  {
    return callback;
  }

  public RTSubscription setCallback( AsyncCallback<T> callback )
  {
    this.callback = callback;
    return this;
  }

  public RTTypes getRtType()
  {
    return rtType;
  }

  public RTEvent getRtEvent()
  {
    return rtEvent;
  }

  public SubscriptionNames getSubscriptionName()
  {
    return subscriptionName;
  }

  public RTSubscription setSubscriptionName( SubscriptionNames subscriptionName )
  {
    this.subscriptionName = subscriptionName;
    return this;
  }

  public RTSubscription addOption( String option )
  {
     options.add( option );
     return this;
  }

  public Collection<String> getOptions()
  {
    return options;
  }

  @Override
  public String toString()
  {
    return "RTSubscription{" + "id='" + id + '\'' + ", callback=" + callback + ", rtType=" + rtType + ", rtEvent="
            + rtEvent + ", subscriptionName=" + subscriptionName + ", options=" + options + '}';
  }
}
