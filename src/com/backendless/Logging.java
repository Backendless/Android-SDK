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

import com.backendless.logging.LogBuffer;
import com.backendless.logging.Logger;

/**
 * Created by baas on 20.04.15.
 */
public class Logging
{
  private static final Logging instance = new Logging();

  static Logging getInstance()
  {
    return instance;
  }

  private Logging()
  {
  }

  public void setLogReportingPolicy( int numOfMessages, int timeFrequencyInSeconds )
  {
    LogBuffer.getInstance().setLogReportingPolicy( numOfMessages, timeFrequencyInSeconds );
  }

  public Logger getLogger( Class clazz )
  {
    return Logger.getLogger( clazz );
  }

  public Logger getLogger( String loggerName )
  {
    return Logger.getLogger( loggerName );
  }

  public void flush()
  {
    LogBuffer.getInstance().flush();
  }
}
