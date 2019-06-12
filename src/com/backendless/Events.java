package com.backendless;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.servercode.ExecutionType;

import java.util.Map;


public class Events
{
  private static final String EVENTS_MANAGER_SERVER_ALIAS = "com.backendless.services.servercode.EventHandler";
  private static final Events instance = new Events();

  private Events()
  {

  }

  static Events getInstance()
  {
    return instance;
  }

  public Map dispatch( String eventName, Map eventArgs )
  {
    return (Map) Invoker.invokeSync( EVENTS_MANAGER_SERVER_ALIAS, "dispatchEvent", new Object[] { eventName, eventArgs } );
  }

  public Map dispatch( String eventName, Map eventArgs, ExecutionType executionType )
  {
    return (Map) Invoker.invokeSync( EVENTS_MANAGER_SERVER_ALIAS, "dispatchEvent", new Object[] { eventName, eventArgs, executionType } );
  }

  public void dispatch( String eventName, Map eventArgs, AsyncCallback<Map> callback )
  {
    Invoker.invokeAsync( EVENTS_MANAGER_SERVER_ALIAS, "dispatchEvent", new Object[] { eventName, eventArgs }, callback );
  }

  public void dispatch( String eventName, Map eventArgs, ExecutionType executionType, AsyncCallback<Map> callback )
  {
    Invoker.invokeAsync( EVENTS_MANAGER_SERVER_ALIAS, "dispatchEvent", new Object[] { eventName, eventArgs, executionType }, callback );
  }
}
