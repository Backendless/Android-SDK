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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by baas on 21.04.15.
 */
public class LogBuffer
{
  private static final int NUM_OF_MESSAGES = 100;
  private static final int TIME_FREQUENCY = 1000 * 60 * 5; // 5 minutes
  private final static String LOGGING_SERVER_ALIAS = "com.backendless.services.logging.LogService";

  private int numOfMessages;
  private int timeFrequency;
  private Set<LogMessage> logMessages;

  private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
  private ScheduledFuture<?> scheduledFuture;

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
    timeFrequency = TIME_FREQUENCY;
    setupTimer();
  }

  public void setLogReportingPolicy( int numOfMessages, int timeFrequency )
  {
    if( numOfMessages <= 0 && timeFrequency <= 0 )
      throw new IllegalArgumentException( ExceptionMessage.INVALID_LOG_POLICY );

    this.numOfMessages = numOfMessages;
    this.timeFrequency = timeFrequency;
    setupTimer();
  }

  public void flush()
  {
    if( !logMessages.isEmpty() )
    {
      reportBatch( new ArrayList<LogMessage>( logMessages ) );
      logMessages.clear();
    }

    setupTimer();
  }

  private void setupTimer()
  {
    if( scheduledFuture != null )
    {
      scheduledFuture.cancel( true );
    }

    if( timeFrequency > 0 )
    {
      if( scheduledExecutorService == null )
      {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
      }

      scheduledTask();
    }
    else
    {
      if( scheduledExecutorService != null )
      {
        scheduledExecutorService.shutdown();
        scheduledExecutorService = null;
      }
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
      flush();
    }
  }

  private void scheduledTask()
  {
    scheduledFuture = scheduledExecutorService.schedule( new Runnable()
    {
      @Override
      public void run()
      {
        flush();
      }
    }, timeFrequency, TimeUnit.MILLISECONDS );
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
