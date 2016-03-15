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
  private static ThreadLocal<InvocationContext> threadLocal = new InheritableThreadLocal<>();
  public static InvocationContext getCurrentContext()
  {
    return threadLocal.get();
  }

  private Map<String, String> httpHeaders;

  private InvocationContext( String appId, String userId, String userToken, List<String> userRoles,
                            String deviceType, Map<String, String> httpHeaders )
  {
    this.appId = appId;
    this.userId = userId;
    this.userToken = userToken;
    this.userRoles = userRoles;
    this.deviceType = DeviceType.valueOf( deviceType );;
    this.httpHeaders = httpHeaders;
  }

  public Map<String, String> getHttpHeaders()
  {
    return httpHeaders;
  }

  public void setHttpHeaders( Map<String, String> httpHeaders )
  {
    this.httpHeaders = httpHeaders;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder( "InvocationContext{" );
    sb.append( "httpHeaders=" ).append( httpHeaders );
    sb.append( ", " ).append( super.toString() );
    sb.append( "}" );
    return sb.toString();
  }
}
