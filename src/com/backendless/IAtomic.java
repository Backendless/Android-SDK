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
  void reset();

  void reset( AsyncCallback responder );

  T get();

  void get( AsyncCallback<T> responder );

  T getAndIncrement();

  void getAndIncrement( AsyncCallback<T> responder );

  T incrementAndGet();

  void incrementAndGet( AsyncCallback<T> responder );

  T getAndDecrement();

  void getAndDecrement( AsyncCallback<T> responder );

  T decrementAndGet();

  void decrementAndGet( AsyncCallback<T> responder );

  T addAndGet( Number value );

  void addAndGet( Number value, AsyncCallback<T> responder );

  T getAndAdd( Number value );

  void getAndAdd( Number value, AsyncCallback<T> responder );

  boolean compareAndSet( Number expected, Number updated );

  void compareAndSet( Number expected, Number updated, AsyncCallback<Boolean> responder );
}
