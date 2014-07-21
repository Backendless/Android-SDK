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

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;

public class CacheService<T> implements ICache<T>
{
  private Class<? extends T> clazz;

  private String key;

  public CacheService( Class<? extends T> clazz, String key )
  {
    this.clazz = clazz;
    this.key = key;
  }

  @Override
  public void put( T value, AsyncCallback<Object> callback )
  {
    Backendless.Cache.put( key, value, callback );
  }

  @Override
  public void put( T value, int expire, AsyncCallback<Object> callback )
  {
    Backendless.Cache.put( key, value, expire, callback );
  }

  @Override
  public void get( AsyncCallback<T> callback )
  {
    Backendless.Cache.get( key, clazz, callback );
  }

  @Override
  public void contains( AsyncCallback<Boolean> callback )
  {
    Backendless.Cache.contains( key, callback );
  }

  @Override
  public void expire( int seconds, AsyncCallback<Object> callback )
  {
    Backendless.Cache.expire( key, seconds, callback );
  }

  @Override
  public void expire( int seconds )
  {
    Backendless.Cache.expire( key, seconds );
  }

  @Override
  public void delete( AsyncCallback<Object> callback )
  {
    Backendless.Cache.delete( key, callback );
  }

  @Override
  public void delete()
  {
    Backendless.Cache.delete( key );
  }

  @Override
  public void put( T value )
  {
    Backendless.Cache.put( key, value );
  }

  @Override
  public void put( T value, int expire )
  {
    Backendless.Cache.put( key, value, expire );
  }

  @Override
  public T get()
  {
    return Backendless.Cache.get( key, clazz );
  }

  @Override
  public Boolean contains()
  {
    return Backendless.Cache.contains( key );
  }
}
