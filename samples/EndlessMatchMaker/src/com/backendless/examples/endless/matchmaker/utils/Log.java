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

package com.backendless.examples.endless.matchmaker.utils;

import com.backendless.exceptions.BackendlessFault;

public class Log
{
  public static void logLine( Throwable e )
  {
    logLine( e.getMessage() );
  }

  public static void logLine( BackendlessFault fault )
  {
    logLine( fault.toString() );
  }

  public static void logLine( String message )
  {
    android.util.Log.e( Defaults.APPLICATION_VERSION, message );

    throw new RuntimeException( message );
  }
}
