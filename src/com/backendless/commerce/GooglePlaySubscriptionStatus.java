package com.backendless.commerce;

/**
 * Created by scadge on 6/24/14.
 */
public class GooglePlaySubscriptionStatus
{
  private boolean autoRenewing;
  private long startTimeMillis;
  private String kind;
  private long expiryTimeMillis;

  public boolean isAutoRenewing()
  {
    return autoRenewing;
  }

  public void setAutoRenewing( boolean autoRenewing )
  {
    this.autoRenewing = autoRenewing;
  }

  public long getStartTimeMillis()
  {
    return startTimeMillis;
  }

  public void setStartTimeMillis( long startTimeMillis )
  {
    this.startTimeMillis = startTimeMillis;
  }

  public String getKind()
  {
    return kind;
  }

  public void setKind( String kind )
  {
    this.kind = kind;
  }

  public long getExpiryTimeMillis()
  {
    return expiryTimeMillis;
  }

  public void setExpiryTimeMillis( long expiryTimeMillis )
  {
    this.expiryTimeMillis = expiryTimeMillis;
  }
}
