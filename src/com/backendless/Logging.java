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

  /**
   * Configuring a Log Buffer Policy
   * The log buffer policy controls the following aspects of log message submission from the client app to the server:
   * - the maximum number of log messages stored in the buffer
   * - the maximum time interval (in seconds) between message transmissions
   * The log exhibits the behavior defined below based on the configuration parameters established with the API:
   * - flushes the log messages to the server when the maximum of messages have been collected
   * - flushes the messages after the configured time interval elapses
   * - delivers log messages immediately if the number of messages is set to 0 (zero)
   * @param numOfMessages sets the maximum limit for the number of messages
   * @param timeFrequencyInSeconds time frequency/interval in seconds defining how often log messages from the buffer should be flushed to the server.
   *                               The value of zero indicates immediate delivery of messages to the server, bypassing the buffer.
   *                               This parameter will be ignored if you are using this method from Business Logic code. Log messages flushes to the server when the maximum of messages have been collected
   */
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
