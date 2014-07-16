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
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import weborb.client.Fault;
import weborb.client.IChainedResponder;
import weborb.client.IResponder;
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

  public <T> ICache<T> of( String key, Class<? extends T> type )
  {
    return new CacheService<T>( type, key );
  }

  public void put( final String key, final Object object, final Integer expire, final AsyncCallback<Object> callback )
  {
    ThreadPoolService.getPoolExecutor().execute( new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          putSync( key, object, expire );
          ResponseCarrier.getInstance().deliverMessage( new AsyncMessage( null, callback ) );
        }
        catch( BackendlessException e )
        {
          ResponseCarrier.getInstance().deliverMessage( new AsyncMessage( new BackendlessFault( e ), callback ) );
        }
      }
    } );
  }

  public void put( final String key, final Object object, final AsyncCallback<Object> callback )
  {
    put( key, object, null, callback );
  }

  public void put( String key, Object object )
  {
    put( key, object, (Integer) null );
  }

  public void put( String key, Object object, Integer expire )
  {
    put( key, object, expire, new AsyncCallback<Object>()
    {
      @Override
      public void handleResponse( Object response )
      {

      }

      @Override
      public void handleFault( BackendlessFault fault )
      {

      }
    } );
  }

  public void putSync( String key, Object object )
  {
    putSync( key, object, null );
  }

  public void putSync( String key, Object object, Integer expire )
  {
    byte[] bytes = serialize( object );

    Invoker.invokeSync( CACHE_SERVER_ALIAS, "putBytes", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), key, bytes, expire }, getChainedResponder() );
  }

  public <T> T getSync( String key, Class<? extends T> clazz )
  {
    byte[] bytes = Invoker.invokeSync( CACHE_SERVER_ALIAS, "getBytes", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), key }, getChainedResponder() );
    return (T) deserialize( bytes, clazz );
  }

  public Object getSync( String key )
  {
    return getSync( key, null );
  }

  public void get( String key, AsyncCallback<Object> callback )
  {
    get( key, callback );
  }

  public <T> void get( final String key, final Class<? extends T> clazz, final AsyncCallback<T> callback )
  {
    ThreadPoolService.getPoolExecutor().execute( new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          T result = getSync( key, clazz );
          ResponseCarrier.getInstance().deliverMessage( new AsyncMessage( result, callback ) );
        }
        catch( BackendlessException e )
        {
          ResponseCarrier.getInstance().deliverMessage( new AsyncMessage( new BackendlessFault( e ), callback ) );
        }
      }
    } );
  }

  public Boolean containsSync( String key )
  {
    return Invoker.invokeSync( CACHE_SERVER_ALIAS, "containsKey", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), key }, getChainedResponder() );
  }

  public void contains( final String key, final AsyncCallback<Boolean> callback )
  {
    ThreadPoolService.getPoolExecutor().execute( new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          Boolean isContains = containsSync( key );
          ResponseCarrier.getInstance().deliverMessage( new AsyncMessage<Boolean>( isContains, callback ) );
        }
        catch( BackendlessException e )
        {
          ResponseCarrier.getInstance().deliverMessage( new AsyncMessage( new BackendlessFault( e ), callback ) );
        }
      }
    } );
  }

  public void expireSync( String key, int expire )
  {
    Invoker.invokeSync( CACHE_SERVER_ALIAS, "extendLife", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), key, expire }, getChainedResponder() );
  }

  public void expire( final String key, final int expire )
  {
    expire( key, expire, new AsyncCallback<Object>()
    {
      @Override
      public void handleResponse( Object response )
      {

      }

      @Override
      public void handleFault( BackendlessFault fault )
      {

      }
    } );
  }

  public void expire( final String key, final int expire, final AsyncCallback<Object> callback )
  {
    ThreadPoolService.getPoolExecutor().execute( new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          expireSync( key, expire );
          ResponseCarrier.getInstance().deliverMessage( new AsyncMessage<Object>( null, callback ) );
        }
        catch( BackendlessException e )
        {
          ResponseCarrier.getInstance().deliverMessage( new AsyncMessage( new BackendlessFault( e ), callback ) );
        }
      }
    } );
  }

  public void deleteSync( String key )
  {
    Invoker.invokeSync( CACHE_SERVER_ALIAS, "delete", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), key }, getChainedResponder() );
  }

  public void delete( final String key )
  {
    delete( key, new AsyncCallback<Object>()
    {
      @Override
      public void handleResponse( Object response )
      {

      }

      @Override
      public void handleFault( BackendlessFault fault )
      {

      }
    } );
  }

  public void delete( final String key, final AsyncCallback<Object> callback )
  {
    ThreadPoolService.getPoolExecutor().execute( new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          deleteSync( key );
          ResponseCarrier.getInstance().deliverMessage( new AsyncMessage<Object>( null, callback ) );
        }
        catch( BackendlessException e )
        {
          ResponseCarrier.getInstance().deliverMessage( new AsyncMessage( new BackendlessFault( e ), callback ) );
        }
      }
    } );
  }

  private static IChainedResponder getChainedResponder()
  {
    return new IChainedResponder()
    {
      private IResponder responder;

      @Override
      public void setNextResponder( IResponder iResponder )
      {
        responder = iResponder;
      }

      @Override
      public void responseHandler( Object o )
      {
        responder.responseHandler( o );
      }

      @Override
      public void errorHandler( Fault fault )
      {
        responder.errorHandler( fault );
      }
    };
  }

  private static Object deserialize( byte[] bytes, Class clazz )
  {
    Object object = null;
    try
    {
      object = weborb.util.io.Serializer.fromBytes( bytes, ISerializer.AMF3, false );

      if( object instanceof IAdaptingType )
        return clazz == null ? ((IAdaptingType) object).defaultAdapt() : ((IAdaptingType) object).adapt( clazz );
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

