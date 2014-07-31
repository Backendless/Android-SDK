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

  public IAtomic of( String counterName )
  {
    if( counterName == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    return AtomicOperationFactory.createAtomicCounter( counterName );
  }

  public Long getAndIncrement( String counterName )
  {
    Object responseValue = Invoker.invokeSync( ATOMIC_MANAGER_SERVER_ALIAS, "getAndIncrement", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), counterName } );
    responseValue = castResponse( responseValue );
    return (Long) responseValue;
  }

  public void getAndIncrement( String counterName, AsyncCallback<Long> responder )
  {
    try
    {
      getAndIncrement( counterName );
      //Invoker.invokeAsync( ATOMIC_MANAGER_SERVER_ALIAS, "getAndIncrement", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), counterName }, new AtomicCallback( responder ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public Long incrementAndGet( String counterName )
  {
    Object responseValue = Invoker.invokeSync( ATOMIC_MANAGER_SERVER_ALIAS, "incrementAndGet", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), counterName } );
    responseValue = castResponse( responseValue );
    return (Long) responseValue;
  }

  public void incrementAndGet( String counterName, AsyncCallback<Long> responder )
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
    Object responseValue = Invoker.invokeSync( ATOMIC_MANAGER_SERVER_ALIAS, "getAndDecrement", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), counterName } );
    responseValue = castResponse( responseValue );
    return (Long) responseValue;
  }

  public void getAndDecrement( String counterName, AsyncCallback<Long> responder )
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
    Object responseValue = Invoker.invokeSync( ATOMIC_MANAGER_SERVER_ALIAS, "decrementAndGet", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), counterName } );
    responseValue = castResponse( responseValue );
    return (Long) responseValue;
  }

  public void decrementAndGet( String counterName, AsyncCallback<Long> responder )
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

  public Long addAndGet( String counterName, Long value )
  {
    Object responseValue = Invoker.invokeSync( ATOMIC_MANAGER_SERVER_ALIAS, "addAndGet", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), counterName, value } );
    responseValue = castResponse( responseValue );
    return (Long) responseValue;
  }

  public void addAndGet( String counterName, Long value, AsyncCallback<Long> responder )
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

  public Long getAndAdd( String counterName, Long value )
  {
    Object responseValue = Invoker.invokeSync( ATOMIC_MANAGER_SERVER_ALIAS, "getAndAdd", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), counterName, value } );
    responseValue = castResponse( responseValue );
    return (Long) responseValue;
  }

  public void getAndAdd( String counterName, Long value, AsyncCallback<Long> responder )
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

  public boolean compareAndSet( String counterName, Long expected, Long updated )
  {
    Object responseValue = Invoker.invokeSync( ATOMIC_MANAGER_SERVER_ALIAS, "compareAndSet", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), counterName, expected, updated } );
    responseValue = castResponse( responseValue );
    return (Boolean) responseValue;
  }

  public void compareAndSet( String counterName, Long expected, Long updated, AsyncCallback<Boolean> responder )
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

  private Object castResponse( Object response )
  {
    if( response instanceof Integer )
      response = Long.valueOf( response.toString() );

    if( response instanceof Double )
      response = Long.valueOf( response.toString() );

    if( response instanceof Boolean )
      response = Boolean.valueOf( response.toString() );

    return response;
  }
}
