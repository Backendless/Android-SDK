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
import weborb.client.IChainedResponder;
import weborb.types.IAdaptingType;
import weborb.util.io.ISerializer;

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

  public void put( final String key, final Object object, final int expire, final AsyncCallback<Object> callback )
  {
    byte[] bytes = serialize( object );
    Invoker.invokeAsync( CACHE_SERVER_ALIAS, "putBytes", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), key, bytes, expire }, callback );
  }

  public void put( final String key, final Object object, final AsyncCallback<Object> callback )
  {
    put( key, object, 0, callback );
  }

  public void put( final String key, final Object object )
  {
    put( key, object, 0 );
  }

  public void put( String key, Object object, int expire )
  {
    byte[] bytes = serialize( object );
    Invoker.invokeSync( CACHE_SERVER_ALIAS, "putBytes", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), key, bytes, expire }, getChainedResponder() );
  }

  public <T> T get( String key, Class<T> type )
  {
    byte[] bytes = Invoker.invokeSync( CACHE_SERVER_ALIAS, "getBytes", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), key }, new AdaptingResponder<byte[]>( byte[].class, new PoJoAdaptingPolicy<byte[]>() ) );

    if( bytes == null )
      return null;

    return (T) deserialize( bytes, type );
  }

  public void get( String key, AsyncCallback<Object> callback )
  {
    get( key, Object.class, callback );
  }

  public <T> void get( final String key, final Class<? extends T> type, final AsyncCallback<T> callback )
  {
    ThreadPoolService.getPoolExecutor().execute( new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          T result = get( key, type );
          ResponseCarrier.getInstance().deliverMessage( new AsyncMessage( result, callback ) );
        }
        catch( BackendlessException e )
        {
          ResponseCarrier.getInstance().deliverMessage( new AsyncMessage( new BackendlessFault( e ), callback ) );
        }
      }
    } );
  }

  public Boolean contains( String key )
  {
    return Invoker.invokeSync( CACHE_SERVER_ALIAS, "containsKey", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), key }, getChainedResponder() );
  }

  public void contains( final String key, final AsyncCallback<Boolean> callback )
  {
    Invoker.invokeAsync( CACHE_SERVER_ALIAS, "containsKey", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), key }, callback );
  }

  public void expire( String key, int expire )
  {
    Invoker.invokeSync( CACHE_SERVER_ALIAS, "extendLife", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), key, expire }, getChainedResponder() );
  }

  public void expire( final String key, final int expire, final AsyncCallback<Object> callback )
  {
    Invoker.invokeAsync( CACHE_SERVER_ALIAS, "extendLife", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), key, expire }, callback );
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

  private static Object deserialize( byte[] bytes, Class type )
  {
    Object object = null;
    try
    {
      object = weborb.util.io.Serializer.fromBytes( bytes, ISerializer.AMF3, false );

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

