package com.backendless.commerce;

/**
 * Created by scadge on 6/24/14.
 */
public class GooglePlaySubscriptionStatus
{
  private boolean autoRenewing;
  private long initiationTimestampMsec;
  private String kind;
  private long validUntilTimestampMsec;

  public GooglePlaySubscriptionStatus()
  {
  }

  public boolean isAutoRenewing()
  {
    return autoRenewing;
  }

  public void setAutoRenewing( boolean autoRenewing )
  {
    this.autoRenewing = autoRenewing;
  }

  public long getInitiationTimestampMsec()
  {
    return initiationTimestampMsec;
  }

  public void setInitiationTimestampMsec( long initiationTimestampMsec )
  {
    this.initiationTimestampMsec = initiationTimestampMsec;
  }

  public String getKind()
  {
    return kind;
  }

  public void setKind( String kind )
  {
    this.kind = kind;
  }

  public long getValidUntilTimestampMsec()
  {
    return validUntilTimestampMsec;
  }

  public void setValidUntilTimestampMsec( long validUntilTimestampMsec )
  {
    this.validUntilTimestampMsec = validUntilTimestampMsec;
  }
}
