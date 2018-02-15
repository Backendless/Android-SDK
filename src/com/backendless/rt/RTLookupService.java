package com.backendless.rt;

import com.backendless.Invoker;
import com.backendless.async.callback.Result;
import com.backendless.exceptions.BackendlessException;

import java.util.logging.Logger;

class RTLookupService
{
  private static final Logger logger = Logger.getLogger( "RTLookupService" );

  private static final int INITIAL_TIMEOUT = 100;
  private static final int MAX_TIMEOUT = 60 * 1000; // 60 sec
  private int retryTimeout = INITIAL_TIMEOUT;

  private Result<ReconnectAttempt> reconnectAttemptListener;

  synchronized String lookup( int retry )
  {
    try
    {
      final String rtServer = Invoker.invokeSync( "com.backendless.rt.RTService", "lookup", new Object[] {} );
      retryTimeout = INITIAL_TIMEOUT;
      return rtServer;
    }
    catch( BackendlessException e )
    {
      logger.severe( "Lookup failed " + e.toString() );
      logger.info( "Wait before lookup for " + retryTimeout );
      try
      {
        Thread.sleep( retryTimeout );
      }
      catch( InterruptedException e1 )
      {
        throw new RuntimeException( e1 );
      }

      retryTimeout *= 2;

      reconnectAttemptListener.handle( new ReconnectAttempt( retry, retryTimeout ) );

      if( retryTimeout > MAX_TIMEOUT )
        retryTimeout = MAX_TIMEOUT;

      return lookup( ++retry );
    }
  }

  public Result<ReconnectAttempt> getReconnectAttemptListener()
  {
    return reconnectAttemptListener;
  }

  public RTLookupService setReconnectAttemptListener( Result<ReconnectAttempt> reconnectAttemptListener )
  {
    this.reconnectAttemptListener = reconnectAttemptListener;
    return this;
  }
}
