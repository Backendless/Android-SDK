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

/**
 * Created by oleg on 22.07.15.
 */
public class InvocationContext
{
  static private String appId;
  static private String userId;
  static private List<String> userRoles;
  static private DeviceType deviceType;

  private InvocationContext()
  {
  }

  private static void setContext( String appId, String userId, List<String> userRoles, DeviceType deviceType )
  {
    InvocationContext.appId = appId;
    InvocationContext.userId = userId;
    InvocationContext.userRoles = userRoles;
    InvocationContext.deviceType = deviceType;
  }

  public static String getAppId()
  {
    return appId;
  }

  public static String getUserId()
  {
    return userId;
  }

  public static List<String> getUserRoles()
  {
    return userRoles;
  }

  public static DeviceType getDeviceType()
  {
    return deviceType;
  }
}
