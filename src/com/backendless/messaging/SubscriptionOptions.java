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

package com.backendless.messaging;

import weborb.v3types.GUID;

public class SubscriptionOptions
{
  private String subscriberId;
  private String subtopic;
  private String selector;

  public SubscriptionOptions()
  {
    this.subscriberId = new GUID().toString();
  }

  public SubscriptionOptions( String subscriberId )
  {
    this.subscriberId = subscriberId;
  }

  public SubscriptionOptions( String subscriberId, String subtopic )
  {
    this.subscriberId = subscriberId;
    this.subtopic = subtopic;
  }

  public SubscriptionOptions( String subscriberId, String subtopic, String selector )
  {
    this.subscriberId = subscriberId;
    this.subtopic = subtopic;
    this.selector = selector;
  }

  public String getSubscriberId()
  {
    return subscriberId;
  }

  public void setSubscriberId( String subscriberId )
  {
    this.subscriberId = subscriberId;
  }

  public String getSubtopic()
  {
    return subtopic;
  }

  public void setSubtopic( String subtopic )
  {
    this.subtopic = subtopic;
  }

  public String getSelector()
  {
    return selector;
  }

  public void setSelector( String selector )
  {
    this.selector = selector;
  }
}
