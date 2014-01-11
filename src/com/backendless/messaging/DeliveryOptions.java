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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeliveryOptions
{
  private int pushBroadcast;
  private List<String> pushSinglecast;
  private PushPolicyEnum pushPolicy = PushPolicyEnum.ALSO;

  private Date publishAt;
  private long repeatEvery;
  private Date repeatExpiresAt;

  public int getPushBroadcast()
  {
    return pushBroadcast;
  }

  public void setPushBroadcast( int pushBroadcast )
  {
    this.pushBroadcast = pushBroadcast;
  }

  public List<String> getPushSinglecast()
  {
    if( pushSinglecast == null )
      return pushSinglecast = new ArrayList<String>();

    return new ArrayList<String>( pushSinglecast );
  }

  public void setPushSinglecast( List<String> pushSinglecast )
  {
    this.pushSinglecast = pushSinglecast;
  }

  public void addPushSinglecast( String pushSinglecast )
  {
    if( pushSinglecast == null || pushSinglecast.equals( "" ) )
      return;

    if( this.pushSinglecast == null )
      this.pushSinglecast = new ArrayList<String>();

    this.pushSinglecast.add( pushSinglecast );
  }

  public Date getPublishAt()
  {
    return publishAt;
  }

  public void setPublishAt( Date publishAt )
  {
    this.publishAt = publishAt;
  }

  public long getRepeatEvery()
  {
    return repeatEvery;
  }

  public void setRepeatEvery( long repeatEvery )
  {
    this.repeatEvery = repeatEvery;
  }

  public Date getRepeatExpiresAt()
  {
    return repeatExpiresAt;
  }

  public void setRepeatExpiresAt( Date repeatExpiresAt )
  {
    this.repeatExpiresAt = repeatExpiresAt;
  }

  public PushPolicyEnum getPushPolicy()
  {
    return pushPolicy;
  }

  public void setPushPolicy( PushPolicyEnum pushPolicy )
  {
    this.pushPolicy = pushPolicy;
  }
}
