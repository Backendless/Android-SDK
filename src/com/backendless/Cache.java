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
import com.backendless.async.message.AsyncMessage;
import com.backendless.cache.CacheService;
import com.backendless.cache.ICache;
import com.backendless.core.ResponseCarrier;
import com.backendless.core.responder.AdaptingResponder;
import com.backendless.core.responder.policy.PoJoAdaptingPolicy;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.utils.ReflectionUtil;
import weborb.client.IChainedResponder;
import weborb.types.IAdaptingType;
import weborb.util.io.ISerializer;

import java.lang.reflect.Type;
import java.util.Date;

public class Cache
{
  private final static String CACHE_SERVER_ALIAS = "com.backendless.services.redis.CacheService";

  private final static Cache instance = new Cache();

  static Cache getInstance()
  {
    return instance;
  }

  private Cache()
  {
  }

  public <T> ICache<T> with( String key, Class<? extends T> type )
  {
    return new CacheService<T>( type, key );
  }

  public void put( String key, Object object, int timeToLive, AsyncCallback<Object> callback )
  {
    byte[] bytes = serialize( object );
    Invoker.invokeAsync( CACHE_SERVER_ALIAS, "putBytes", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), key, bytes, timeToLive }, callback );
  }

  public void put( String key, Object object, AsyncCallback<Object> callback )
  {
    put( key, object, 0, callback );
  }

  public void put( String key, Object object )
  {
    put( key, object, 0 );
  }

  public void put( String key, Object object, int timeToLive )
  {
    byte[] bytes = serialize( object );
    Invoker.invokeSync( CACHE_SERVER_ALIAS, "putBytes", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), key, bytes, timeToLive }, getChainedResponder() );
  }

  public <T> T get( String key, Class<? extends T> type )
  {
    byte[] bytes = Invoker.invokeSync( CACHE_SERVER_ALIAS, "getBytes", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), key }, new AdaptingResponder<byte[]>( byte[].class, new PoJoAdaptingPolicy<byte[]>() ) );

    if( bytes == null )
      return null;

    //Class argType = type.getClass();
    return (T) deserialize( bytes, type );
  }

  public <T> void get( final String key, final AsyncCallback<T> callback )
  {
    final Type asyncCallbackType = ReflectionUtil.getCallbackGenericType( callback );

    ThreadPoolService.getPoolExecutor().execute( new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          T result = (T) get( key, (Class) asyncCallbackType );
          ResponseCarrier.getInstance().deliverMessage( new AsyncMessage<T>( result, callback ) );
        }
        catch( BackendlessException e )
        {
          ResponseCarrier.getInstance().deliverMessage( new AsyncMessage<T>( new BackendlessFault( e ), callback ) );
        }
      }
    } );
  }

  public Boolean contains( String key )
  {
    return Invoker.invokeSync( CACHE_SERVER_ALIAS, "containsKey", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), key }, getChainedResponder() );
  }

  public void contains( String key, AsyncCallback<Boolean> callback )
  {
    Invoker.invokeAsync( CACHE_SERVER_ALIAS, "containsKey", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), key }, callback );
  }

  public void expireIn( String key, int seconds )
  {
    Invoker.invokeSync( CACHE_SERVER_ALIAS, "expireIn", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), key, seconds }, getChainedResponder() );
  }

  public void expireIn( String key, int seconds, AsyncCallback<Object> callback )
  {
    Invoker.invokeAsync( CACHE_SERVER_ALIAS, "expireIn", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), key, seconds }, callback );
  }

  public void expireAt( String key, Date date )
  {
    expireAt( key, date.getTime() );
  }

  public void expireAt( String key, long timestamp )
  {
    Invoker.invokeSync( CACHE_SERVER_ALIAS, "expireAt", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), key, timestamp }, getChainedResponder() );
  }

  public void expireAt( String key, Date date, AsyncCallback<Object> callback )
  {
    expireAt( key, date.getTime(), callback );
  }

  public void expireAt( String key, long timestamp, AsyncCallback<Object> callback )
  {
    Invoker.invokeAsync( CACHE_SERVER_ALIAS, "expireAt", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), key, timestamp }, callback );
  }

  public void delete( String key )
  {
    Invoker.invokeSync( CACHE_SERVER_ALIAS, "delete", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), key }, getChainedResponder() );
  }

  public void delete( final String key, final AsyncCallback<Object> callback )
  {
    Invoker.invokeAsync( CACHE_SERVER_ALIAS, "delete", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), key }, callback );
  }

  private static <T> IChainedResponder getChainedResponder()
  {
    return new AdaptingResponder<T>();
  }

  private static Object deserialize( byte[] bytes, Type type )
  {
    Object object = null;
    try
    {
      object = weborb.util.io.Serializer.fromBytes( bytes, ISerializer.AMF3, true );

      if( object instanceof IAdaptingType )
        return type == null ? ((IAdaptingType) object).defaultAdapt() : ((IAdaptingType) object).adapt( type );
    }
    catch( Exception e )
    {
      throw new BackendlessException( e );
    }

    return object;
  }

  private static byte[] serialize( Object object )
  {
    byte[] bytes;
    try
    {
      bytes = weborb.util.io.Serializer.toBytes( object, ISerializer.AMF3 );
    }
    catch( Exception e )
    {
      throw new BackendlessException( e );
    }
    return bytes;
  }
}

