package com.backendless.rt;

public class RTSubscription extends AbstractRequest
{
  private final SubscriptionNames subscriptionName;

  protected RTSubscription( SubscriptionNames subscriptionName, RTCallback callback )
  {
    super( callback );
    if( callback == null )
      throw new IllegalArgumentException( "Callback can not be null" );

    this.subscriptionName = subscriptionName;
  }

  @Override
  public String getName()
  {
    return subscriptionName.name();
  }

  public SubscriptionNames getSubscriptionName()
  {
    return subscriptionName;
  }

  @Override
  public String toString()
  {
    return "RTSubscription{" + "id='" + getId() + '\'' + ", callback=" + getCallback() + ", subscriptionName=" + subscriptionName + ", options=" + getOptions() + '}';
  }
}
