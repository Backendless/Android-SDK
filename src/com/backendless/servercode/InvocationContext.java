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

package com.backendless.servercode;

import com.backendless.commons.DeviceType;

import java.util.List;
import java.util.Map;

/**
 * Created by oleg on 22.07.15.
 */
public class InvocationContext extends AbstractContext
{
  private static ThreadLocal<InvocationContext> threadLocal = new InheritableThreadLocal<InvocationContext>(){
    @Override
    protected InvocationContext initialValue()
    {
      return new InvocationContext( );
    }
  };

  private static InvocationContext getCurrentContext()
  {
    return threadLocal.get();
  }

  private String httpPath;
  private Map<String, String> httpPathParams;
  private Map<String, String> httpQueryParams;
  private Map<String, String> httpResponseHeaders;

  private InvocationContext( String appId, String userId, String userToken, String userLocale, List<String> userRoles,
                             String deviceType, String httpPath, Map<String, String> httpHeaders,
                             Map<String, String> httpPathParams, Map<String, String> httpQueryParams, Map<String, String> httpResponseHeaders )
  {
    this.appId = appId;
    this.userId = userId;
    this.userToken = userToken;
    this.userLocale = userLocale;
    this.userRoles = userRoles;
    this.deviceType = DeviceType.valueOf( deviceType );
    this.httpHeaders = httpHeaders;
    this.httpPath = httpPath;
    this.httpPathParams = httpPathParams;
    this.httpQueryParams = httpQueryParams;
    this.httpResponseHeaders = httpResponseHeaders;
  }

  private InvocationContext( ) {}

  public static String asString()
  {
    return getCurrentContext().toString();
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder( "InvocationContext{" );
    sb.append( super.toString() );
    sb.append( "}" );
    return sb.toString();
  }

  public static String getAppId()
  {
    return getCurrentContext().appId;
  }

  public static void setAppId( String appId )
  {
    getCurrentContext().appId = appId;
  }

  public static String getUserId()
  {
    return getCurrentContext().userId;
  }

  public static void setUserId( String userId )
  {
    getCurrentContext().userId = userId;
  }

  public static String getUserToken()
  {
    return getCurrentContext().userToken;
  }

  public static void setUserToken( String userToken )
  {
    getCurrentContext().userToken = userToken;
  }

  public static List<String> getUserRoles()
  {
    return getCurrentContext().userRoles;
  }

  public static void setUserRoles( List<String> userRoles )
  {
    getCurrentContext().userRoles = userRoles;
  }

  public static String getUserLocale()
  {
    return getCurrentContext().userLocale;
  }

  public static void setUserLocale( String userLocale )
  {
    getCurrentContext().userLocale = userLocale;
  }

  public static DeviceType getDeviceType()
  {
    return getCurrentContext().deviceType;
  }

  public static void setDeviceType( DeviceType deviceType )
  {
    getCurrentContext().deviceType = deviceType;
  }

  public static Map<String, String> getHttpHeaders()
  {
    return getCurrentContext().httpHeaders;
  }

  public static void setHttpHeaders( Map<String, String> httpHeaders )
  {
    getCurrentContext().httpHeaders = httpHeaders;
  }

  public static String getHttpPath()
  {
    return getCurrentContext().httpPath;
  }

  public static void setHttpPath( String httpPath )
  {
    getCurrentContext().httpPath = httpPath;
  }

  public static Map<String, String> getHttpPathParams()
  {
    return getCurrentContext().httpPathParams;
  }

  public static void setHttpPathParams( Map<String, String> httpPathParams )
  {
    getCurrentContext().httpPathParams = httpPathParams;
  }

  public static Map<String, String> getHttpQueryParams()
  {
    return getCurrentContext().httpQueryParams;
  }

  public static void setHttpQueryParams( Map<String, String> httpQueryParams )
  {
    getCurrentContext().httpQueryParams = httpQueryParams;
  }

  public static Map<String, String> getHttpResponseHeaders()
  {
    return getCurrentContext().httpResponseHeaders;
  }

  public static void setHttpResponseHeaders( Map<String, String> httpResponseHeaders )
  {
    getCurrentContext().httpResponseHeaders = httpResponseHeaders;
  }
}
