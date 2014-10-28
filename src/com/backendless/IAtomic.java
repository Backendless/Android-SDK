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

public interface IAtomic<T>
{
  public void reset();

  public void reset( AsyncCallback responder );

  public T get();

  public void get( AsyncCallback<T> responder );

  public T getAndIncrement();

  public void getAndIncrement( AsyncCallback<T> responder );

  public T incrementAndGet();

  public void incrementAndGet( AsyncCallback<T> responder );

  public T getAndDecrement();

  public void getAndDecrement( AsyncCallback<T> responder );

  public T decrementAndGet();

  public void decrementAndGet( AsyncCallback<T> responder );

  public T addAndGet( Number value );

  public void addAndGet( Number value, AsyncCallback<T> responder );

  public T getAndAdd( Number value );

  public void getAndAdd( Number value, AsyncCallback<T> responder );

  public boolean compareAndSet( Number expected, Number updated );

  public void compareAndSet( Number expected, Number updated, AsyncCallback<Boolean> responder );
}
