package com.backendless.commerce;

/**
 * Created by scadge on 6/24/14.
 */
public class GooglePlayPurchaseStatus
{
  private String kind;
  private Long purchaseTime;
  private Integer purchaseState;
  private Integer consumptionState;
  private String developerPayload;

  public String getKind()
  {
    return kind;
  }

  public void setKind( String kind )
  {
    this.kind = kind;
  }

  public Long getPurchaseTime()
  {
    return purchaseTime;
  }

  public void setPurchaseTime( Long purchaseTime )
  {
    this.purchaseTime = purchaseTime;
  }

  public Integer getPurchaseState()
  {
    return purchaseState;
  }

  public void setPurchaseState( Integer purchaseState )
  {
    this.purchaseState = purchaseState;
  }

  public Integer getConsumptionState()
  {
    return consumptionState;
  }

  public void setConsumptionState( Integer consumptionState )
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
