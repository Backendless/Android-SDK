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

package com.backendless.servercode.logging;

import com.backendless.Backendless;

public class Logger
{
  private boolean async;
  private Class clazz;

  private com.backendless.logging.Logger logger;

  public static Logger getLogger( Class clazz )
  {
    return new Logger( clazz, false );
  }

  /**
   * @deprecated async argument is ignored, use {@link #getLogger(Class)} instead
   */
  @Deprecated
  public static Logger getLogger( Class clazz, boolean async )
  {
    return new Logger( clazz, async );
  }

  Logger( Class clazz, boolean async )
  {
    this.clazz = clazz;
    this.async = async;
    this.logger = Backendless.Logging.getLogger( clazz );
  }

  public void debug( String message )
  {
    logger.debug( message );
  }

  public void info( String message )
  {
    logger.info( message );
  }

  public void warn( String message )
  {
    logger.warn( message );
  }

  public void warn( String message, Throwable t )
  {
    logger.warn( message, t );
  }

  public void error( String message )
  {
    logger.error( message );
  }

  public void error( String message, Throwable t )
  {
    logger.error( message, t );
  }

  @Override
  public String toString()
  {
    return "Logger{" +
            "async=" + async +
            ", clazz=" + clazz +
            '}';
  }
}
