package com.backendless.commerce;

/**
 * Created by scadge on 6/24/14.
 */
public class GooglePlayPurchaseStatus
{
  private String kind;
  private long purchaseTimeMillis;
  private int purchaseState;
  private int consumptionState;
  private String developerPayload;

  public String getKind()
  {
    return kind;
  }

  public void setKind( String kind )
  {
    this.kind = kind;
  }

  public long getPurchaseTimeMillis()
  {
    return purchaseTimeMillis;
  }

  public void setPurchaseTimeMillis( long purchaseTimeMillis )
  {
    this.purchaseTimeMillis = purchaseTimeMillis;
  }

  public int getPurchaseState()
  {
    return purchaseState;
  }

  public void setPurchaseState( int purchaseState )
  {
    this.purchaseState = purchaseState;
  }

  public int getConsumptionState()
  {
    return consumptionState;
  }

  public void setConsumptionState( int consumptionState )
  {
    this.consumptionState = consumptionState;
  }

  public String getDeveloperPayload()
  {
    return developerPayload;
  }

  public void setDeveloperPayload( String developerPayload )
  {
    this.developerPayload = developerPayload;
  }
}
