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

package com.backendless.cache;

import com.backendless.async.callback.AsyncCallback;

import java.util.Date;

public interface ICache<T>
{
  void put( T value, AsyncCallback<Object> callback );

  void put( T value, int timeToLive, AsyncCallback<Object> callback );

  void put( T value );

  void put( T value, int timeToLive );

  void get( AsyncCallback<T> callback );

  T get();

  void contains( AsyncCallback<Boolean> callback );

  Boolean contains();

  void expireIn( int seconds, AsyncCallback<Object> callback );

  void expireIn( int seconds );

  void expireAt( Date date, AsyncCallback<Object> callback );

  void expireAt( long timestamp, AsyncCallback<Object> callback );

  void expireAt( Date date );

  void expireAt( long timestamp );

  void delete( AsyncCallback<Object> callback );

  void delete();
}
