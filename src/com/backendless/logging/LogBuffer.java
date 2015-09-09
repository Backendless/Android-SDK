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

import com.backendless.Backendless;
import com.backendless.Invoker;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.utils.ScheduledExecutor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/**
 * Created by baas on 21.04.15.
 */
public class LogBuffer extends ScheduledExecutor
{
  private static final int NUM_OF_MESSAGES = 100;
  private static final int TIME_FREQUENCY = 60 * 5; // 5 minutes
  private static final String LOGGING_SERVER_ALIAS = "com.backendless.services.logging.LogService";

  private int numOfMessages;

  private Set<LogMessage> logMessages;

  public static class SingletonHolder
  {
    public static final LogBuffer HOLDER_INSTANCE = new LogBuffer();
  }

  public static LogBuffer getInstance()
  {
    return SingletonHolder.HOLDER_INSTANCE;
  }

  private LogBuffer()
  {
    logMessages = Collections.synchronizedSet( new HashSet<LogMessage>() );
    numOfMessages = NUM_OF_MESSAGES;
    super.setTimeFrequency( TIME_FREQUENCY );
  }

  public void setLogReportingPolicy( int numOfMessages, int timeFrequency )
  {
    if( numOfMessages <= 0 && timeFrequency <= 0 )
      throw new IllegalArgumentException( ExceptionMessage.INVALID_LOG_POLICY );

    this.numOfMessages = numOfMessages;
    super.setTimeFrequency( timeFrequency );
  }

  public void flush()
  {
    super.doTask();
  }

  @Override
  protected void calculate()
  {
    if( !logMessages.isEmpty() )
    {
      reportBatch( new ArrayList<LogMessage>( logMessages ) );
      logMessages.clear();
    }
  }

  void enqueue( String logger, Level level, String message, Throwable exception )
  {
    if( numOfMessages == 1 )
    {
      reportSingleLogMessage( logger, level, message, exception != null ? getStackTrace( exception ) : null );
      return;
    }

    logMessages.add( new LogMessage( logger, level, new Date( System.currentTimeMillis() ), message, exception != null ? getStackTrace( exception ) : null ) );

    if( numOfMessages > 1 && logMessages.size() >= numOfMessages )
    {
      super.doTask();
    }
  }

  private String getStackTrace( Throwable t )
  {
    if( t == null )
      return null;

    StringWriter errors = new StringWriter();

    t.printStackTrace( new PrintWriter( errors ) );

    return errors.toString();
  }

  public void reportSingleLogMessage( String logger, Level loglevel, String message, String exception )
  {
    Invoker.invokeAsync( LOGGING_SERVER_ALIAS, "log", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), loglevel.name(), logger, message, exception }, new AsyncCallback<Void>()
    {
      @Override
      public void handleResponse( Void response )
      {
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
      }
    } );
  }

  public void reportBatch( List<LogMessage> logBatches )
  {
    Invoker.invokeAsync( LOGGING_SERVER_ALIAS, "batchLog", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), logBatches }, new AsyncCallback<Void>()
    {
      @Override
      public void handleResponse( Void response )
      {
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
      }
    } );
  }
}
