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

package com.backendless.logging;

import com.backendless.exceptions.ExceptionMessage;

/**
 * Created by baas on 21.04.15.
 */
public class Logger
{
  private final String name;
  private static final LogBuffer logBufer = LogBuffer.getInstance();

  public static Logger getLogger( Class clazz )
  {
    return new Logger( clazz.getCanonicalName() );
  }

  public static Logger getLogger( String name )
  {
    if( name == null || name.trim().isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.EMPTY_LOG_NAME);

    return new Logger( name );
  }

  Logger( String name )
  {
    this.name = name;
  }

  public void debug( String message )
  {
    logBufer.enqueue( name, Level.DEBUG, message, null );
  }

  public void info( String message )
  {
    logBufer.enqueue( name, Level.INFO, message, null );
  }

  public void warn( String message )
  {
    logBufer.enqueue( name, Level.WARN, message, null );
  }

  public void warn( String message, Throwable t )
  {
    logBufer.enqueue( name, Level.WARN, message, t );
  }

  public void error( String message )
  {
    logBufer.enqueue( name, Level.ERROR, message, null );
  }

  public void error( String message, Throwable t )
  {
    logBufer.enqueue( name, Level.ERROR, message, t );
  }

  public void fatal( String message )
  {
    logBufer.enqueue( name, Level.FATAL, message, null );
  }

  public void fatal( String message, Throwable t )
  {
    logBufer.enqueue( name, Level.FATAL, message, t );
  }

  public void trace( String message )
  {
    logBufer.enqueue( name, Level.TRACE, message, null );
  }
}
