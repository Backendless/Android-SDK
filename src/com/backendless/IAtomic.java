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

import com.backendless.async.callback.AsyncCallback;

public interface IAtomic
{
  public Long get();

  public void get( AsyncCallback<Long> responder );

  public Long getAndIncrement();

  public void getAndIncrement( AsyncCallback<Long> responder );

  public Long incrementAndGet();

  public void incrementAndGet( AsyncCallback<Long> responder );

  public Long getAndDecrement();

  public void getAndDecrement( AsyncCallback<Long> responder );

  public Long decrementAndGet();

  public void decrementAndGet( AsyncCallback<Long> responder );

  public Long addAndGet( Long value );

  public void addAndGet( Long value, AsyncCallback<Long> responder );

  public Long getAndAdd( Long value );

  public void getAndAdd( Long value, AsyncCallback<Long> responder );

  public boolean compareAndSet( Long expected, Long updated );

  public void compareAndSet( Long expected, Long updated, AsyncCallback<Boolean> responder );
}
