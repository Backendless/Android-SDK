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
 * Created by oleg on 21.07.15.
 */
public class InvocationContext
{
  private static RunnerContext runnerContext;

  public static RunnerContext getRunnerContext()
  {
    return runnerContext;
  }

  public static void setRunnerContext( RunnerContext runnerContext )
  {
    InvocationContext.runnerContext = runnerContext;
  }

  public static void setRunnerContext(String appId, String userId, List<String> userRoles, DeviceType deviceType)
  {
    RunnerContext runnerContext = new RunnerContext();
    runnerContext.setAppId( appId );
    runnerContext.setUserId( userId );
    runnerContext.setUserRole( userRoles );
    runnerContext.setDeviceType( deviceType );
    InvocationContext.runnerContext = runnerContext;
  }
}
