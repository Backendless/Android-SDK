/*
 * ********************************************************************************************************************
 *  <p/>
 *  BACKENDLESS.COM CONFIDENTIAL
 *  <p/>
 *  ********************************************************************************************************************
 *  <p/>
 *  Copyright 2012 BACKENDLESS.COM. All Rights Reserved.
 *  <p/>
 *  NOTICE: All information contained herein is, and remains the property of Backendless.com and its suppliers,
 *  if any. The intellectual and technical concepts contained herein are proprietary to Backendless.com and its
 *  suppliers and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret
 *  or copyright law. Dissemination of this information or reproduction of this material is strictly forbidden
 *  unless prior written permission is obtained from Backendless.com.
 *  <p/>
 *  ********************************************************************************************************************
 */

package com.backendless;


public class Subscription
{
  private String subscriptionId;
  private String channelName;

  private int pollingInterval = 1000;

  public Subscription()
  {
  }

  public Subscription( String subscriptionId, String channelName )
  {
    this.subscriptionId = subscriptionId;
    this.channelName = channelName;
  }

  public Subscription( int pollingInterval )
  {
    this.pollingInterval = pollingInterval;
  }

  public String getSubscriptionId()
  {
    return subscriptionId;
  }

  public synchronized void setSubscriptionId( String subscriptionId )
  {
    this.subscriptionId = subscriptionId;
  }

  public String getChannelName()
  {
    return channelName;
  }

  public synchronized void setChannelName( String channelName )
  {
    this.channelName = channelName;
  }

  public int getPollingInterval()
  {
    return pollingInterval;
  }

  public synchronized void setPollingInterval( int pollingInterval )
  {
    this.pollingInterval = pollingInterval;
  }

}