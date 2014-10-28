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
import com.backendless.atomic.AtomicCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;

public final class Counters
{
  protected static final String ATOMIC_MANAGER_SERVER_ALIAS = "com.backendless.services.redis.AtomicOperationService";
  private static final Counters instance = new Counters();

  public static Counters getInstance()
  {
    return instance;
  }

  private Counters()
  {
  }

  public static Number convertToType( Number value, Class type )
  {
    if( type == Short.class )
      return value.shortValue();
    else if( type == Integer.class )
      return value.intValue();
    else if( type == Long.class )
      return value.longValue();
    else if( type == Float.class )
      return value.floatValue();
    else if( type == Double.class )
      return value.doubleValue();
    else if( type == Byte.class )
      return value.byteValue();
    else
      throw new RuntimeException( "unsupported data type. Cannot adapt counter value to type - " + type );
  }

  public IAtomic of( String counterName )
  {
    if( counterName == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    return of( counterName, Long.class );
  }

  public <T> IAtomic<T> of( String counterName, Class<? extends T> type )
  {
    return AtomicOperationFactory.createAtomicCounter( counterName, type );
  }

  public void reset( String counterName )
  {
    Invoker.invokeSync( ATOMIC_MANAGER_SERVER_ALIAS, "reset", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), counterName } );
  }

  public void reset( String counterName, AsyncCallback callback )
  {
    Invoker.invokeAsync( ATOMIC_MANAGER_SERVER_ALIAS, "reset", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), counterName }, callback );
  }

  public Long get( String counterName )
  {
    return runGetOperation( "get", counterName );
  }

  public <T> void get( String counterName, AsyncCallback<T> responder )
  {
    try
    {
      Invoker.invokeAsync( ATOMIC_MANAGER_SERVER_ALIAS, "get", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), counterName }, new AtomicCallback( responder ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public Long getAndIncrement( String counterName )
  {
    return runGetOperation( "getAndIncrement", counterName );
  }

  public <T> void getAndIncrement( String counterName, AsyncCallback<T> responder )
  {
    try
    {
      Invoker.invokeAsync( ATOMIC_MANAGER_SERVER_ALIAS, "getAndIncrement", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), counterName }, new AtomicCallback( responder ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public Long incrementAndGet( String counterName )
  {
    return runGetOperation( "incrementAndGet", counterName );
  }

  public <T> void incrementAndGet( String counterName, AsyncCallback<T> responder )
  {
    try
    {
      Invoker.invokeAsync( ATOMIC_MANAGER_SERVER_ALIAS, "incrementAndGet", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), counterName }, new AtomicCallback( responder ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public Long getAndDecrement( String counterName )
  {
    return runGetOperation( "getAndDecrement", counterName );
  }

  public <T> void getAndDecrement( String counterName, AsyncCallback<T> responder )
  {
    try
    {
      Invoker.invokeAsync( ATOMIC_MANAGER_SERVER_ALIAS, "getAndDecrement", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), counterName }, new AtomicCallback( responder ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public Long decrementAndGet( String counterName )
  {
    return runGetOperation( "decrementAndGet", counterName );
  }

  public <T> void decrementAndGet( String counterName, AsyncCallback<T> responder )
  {
    try
    {
      Invoker.invokeAsync( ATOMIC_MANAGER_SERVER_ALIAS, "decrementAndGet", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), counterName }, new AtomicCallback( responder ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public Long addAndGet( String counterName, Number value )
  {
    return runGetOperation( "addAndGet", counterName, value );
  }

  public <T> void addAndGet( String counterName, Number value, AsyncCallback<T> responder )
  {
    try
    {
      Invoker.invokeAsync( ATOMIC_MANAGER_SERVER_ALIAS, "addAndGet", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), counterName, value }, new AtomicCallback( responder ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public Long getAndAdd( String counterName, Number value )
  {
    return runGetOperation( "getAndAdd", counterName, value );
  }

  public <T> void getAndAdd( String counterName, Number value, AsyncCallback<T> responder )
  {
    try
    {
      Invoker.invokeAsync( ATOMIC_MANAGER_SERVER_ALIAS, "getAndAdd", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), counterName, value }, new AtomicCallback( responder ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public boolean compareAndSet( String counterName, Number expected, Number updated )
  {
    return (Boolean) Invoker.invokeSync( ATOMIC_MANAGER_SERVER_ALIAS, "compareAndSet", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), counterName, expected, updated } );
  }

  public void compareAndSet( String counterName, Number expected, Number updated, AsyncCallback<Boolean> responder )
  {
    try
    {
      Invoker.invokeAsync( ATOMIC_MANAGER_SERVER_ALIAS, "compareAndSet", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), counterName, expected, updated }, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  private Long runGetOperation( String operationName, String counterName )
  {
    Number responseValue = Invoker.invokeSync( ATOMIC_MANAGER_SERVER_ALIAS, operationName, new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), counterName } );
    responseValue = convertToType( responseValue, Long.class );
    return (Long) responseValue;
  }

  private Long runGetOperation( String operationName, String counterName, Number value )
  {
    Number responseValue = Invoker.invokeSync( ATOMIC_MANAGER_SERVER_ALIAS, operationName, new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), counterName, value } );
    responseValue = convertToType( responseValue, Long.class );
    return (Long) responseValue;
  }
}
