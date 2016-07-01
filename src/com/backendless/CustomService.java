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
import com.backendless.core.responder.AdaptingResponder;
import com.backendless.core.responder.policy.CollectionAdaptingPolicy;
import com.backendless.core.responder.policy.IAdaptingPolicy;
import com.backendless.core.responder.policy.PoJoAdaptingPolicy;

public class CustomService
{
  private static final String CUSTOM_SERVICE_ALIAS = "com.backendless.services.servercode.CustomServiceHandler";
  private static final String METHOD_NAME_ALIAS = "dispatchService";

  private static final CustomService instance = new CustomService();

  private CustomService()
  {

  }

  static CustomService getInstance()
  {
    return instance;
  }

  public <T> T invoke( String serviceName, String serviceVersion, String method, Object[] arguments )
  {
    Object[] args =  new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), serviceName, serviceVersion, method, arguments };
    return (T) Invoker.invokeSync( CUSTOM_SERVICE_ALIAS, METHOD_NAME_ALIAS, args );
  }

   public <T> T invoke( String serviceName, String serviceVersion, String method, Object[] arguments, Class<?> clazz )
  {
    IAdaptingPolicy adaptingPolicy;

    if( BackendlessCollection.class.isAssignableFrom( clazz ) )
      adaptingPolicy = new CollectionAdaptingPolicy();
    else
      adaptingPolicy = new PoJoAdaptingPolicy();

    Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), serviceName, serviceVersion, method, arguments };
    return (T) Invoker.invokeSync( CUSTOM_SERVICE_ALIAS, METHOD_NAME_ALIAS, args, new AdaptingResponder( clazz, adaptingPolicy ) );
  }

  public <E> void invoke( String serviceName, String serviceVersion, String method, Object[] arguments, AsyncCallback<E> callback )
  {
    Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), serviceName, serviceVersion, method, arguments };
    Invoker.invokeAsync( CUSTOM_SERVICE_ALIAS, METHOD_NAME_ALIAS, args, callback );
  }

  public <E> void invoke( String serviceName, String serviceVersion, String method, Object[] arguments, Class<?> clazz, AsyncCallback<E> callback )
  {
    IAdaptingPolicy adaptingPolicy;

    if( BackendlessCollection.class.isAssignableFrom( clazz ) )
      adaptingPolicy = new CollectionAdaptingPolicy();
    else
      adaptingPolicy = new PoJoAdaptingPolicy();

    Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), serviceName, serviceVersion, method, arguments };
    Invoker.invokeAsync( CUSTOM_SERVICE_ALIAS, METHOD_NAME_ALIAS, args, callback, new AdaptingResponder( clazz, adaptingPolicy ) );
  }
}
