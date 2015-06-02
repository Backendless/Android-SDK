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

import java.util.Date;

/**
 * Created by baas on 21.04.15.
 */
public class LogMessage
{
  private String logger;
  private Level level;
  private Date timestamp;
  private String message;
  private String exception;

  public LogMessage()
  {
  }

  public LogMessage( String logger, Level level, Date timestamp, String message, String exception )
  {
    setLogger( logger );
    setLevel( level );
    setTimestamp( timestamp );
    setMessage( message );
    setException( exception );
  }

  public String getLogger()
  {
    return logger;
  }

  public void setLogger( String logger )
  {
    this.logger = logger.intern();
  }

  public Level getLevel()
  {
    return level;
  }

  public void setLevel( Level level )
  {
    this.level = level;
  }

  public Date getTimestamp()
  {
    return timestamp;
  }

  public void setTimestamp( Date timestamp )
  {
    this.timestamp = timestamp;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

  public String getException()
  {
    return exception;
  }

  public void setException( String exception )
  {
    this.exception = exception;
  }
}
