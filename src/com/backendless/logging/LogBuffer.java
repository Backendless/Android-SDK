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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.*;

public class LogBuffer
{
  private static final int NUM_OF_MESSAGES = 100;
  private static final int NUM_OF_MESSAGES_CODERUNNER = 1;
  private static final int TIME_FREQUENCY = 60 * 5; // 5 minutes
  private static final int TIME_FREQUENCY_CODERUNNER = -1;
  private static final String LOGGING_SERVER_ALIAS = "com.backendless.services.logging.LogService";

  private ScheduledExecutorService scheduledExecutorService;
  private int numOfMessages;
  private int timeFrequency;

  private final Queue<LogMessage> logMessages;
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
    numOfMessages = Backendless.isCodeRunner() ? NUM_OF_MESSAGES_CODERUNNER : NUM_OF_MESSAGES;
    timeFrequency = Backendless.isCodeRunner() ? TIME_FREQUENCY_CODERUNNER : TIME_FREQUENCY;

    logMessages = new ConcurrentLinkedQueue<>();

    if( !Backendless.isCodeRunner() )
      scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

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
      reportBatch( new ArrayList<>( logMessages ) );
      logMessages.clear();
    }
  }

  private void setupTimer()
  {
    if( timeFrequency > 0 )
    {
      scheduledTask();
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
    if( Backendless.isCodeRunner() )
      return;

    if( !scheduledExecutorService.isShutdown() )
    {
      if( scheduledFuture != null )
        scheduledFuture.cancel(false);

      scheduledFuture = scheduledExecutorService.scheduleAtFixedRate( new Runnable()
      {
        @Override
        public void run()
        {
          flush();
        }
      }, 0, timeFrequency, TimeUnit.SECONDS );
    }
  }

  private String getStackTrace( Throwable t )
  {
    if( t == null )
      return null;

    try ( StringWriter errors = new StringWriter();
          PrintWriter s1 = new PrintWriter( errors ) )
    {
      t.printStackTrace( s1 );
      return errors.toString();
    }
    catch ( IOException e )
    {
      return null;
    }
  }

  public void reportSingleLogMessage( String logger, Level loglevel, String message, String exception )
  {
    if( Backendless.isCodeRunner() )
    {
      Invoker.invokeSync( LOGGING_SERVER_ALIAS, "log", new Object[]{ loglevel.name(), logger, message, exception } );
    }
    else
    {
      Invoker.invokeAsync( LOGGING_SERVER_ALIAS, "log", new Object[]{ loglevel.name(), logger, message, exception }, new AsyncCallback<Void>()
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

  public void reportBatch( List<LogMessage> logBatches )
  {
    if( Backendless.isCodeRunner() )
    {
      Invoker.invokeSync( LOGGING_SERVER_ALIAS, "batchLog", new Object[]{ logBatches } );
    }
    else
    {
      Invoker.invokeAsync( LOGGING_SERVER_ALIAS, "batchLog", new Object[]{ logBatches }, new AsyncCallback<Void>()
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

  public void close()
  {
    scheduledFuture.cancel( true );
    scheduledExecutorService.shutdownNow();
  }
}
