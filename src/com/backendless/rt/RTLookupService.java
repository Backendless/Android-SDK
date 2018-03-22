package com.backendless.rt;

import com.backendless.Invoker;
import com.backendless.async.callback.Result;
import com.backendless.exceptions.BackendlessException;
import com.backendless.utils.timeout.TimeOutManager;

import java.util.logging.Logger;

class RTLookupService
{
  private static final Logger logger = Logger.getLogger( "RTLookupService" );

  private final Result<ReconnectAttempt> reconnectAttemptListener;
  private final TimeOutManager timeOutManager;

  RTLookupService( Result<ReconnectAttempt> reconnectAttemptListener, TimeOutManager timeOutManager )
  {
    this.reconnectAttemptListener = reconnectAttemptListener;
    this.timeOutManager = timeOutManager;
  }

  synchronized String lookup( )
  {
    try
    {
      final String rtServer = Invoker.invokeSync( "com.backendless.rt.RTService", "lookup", new Object[] {} );
      return rtServer;
    }
    catch( BackendlessException e )
    {
      logger.severe( "Lookup failed " + e.toString() );
      final int retryTimeout  = timeOutManager.nextTimeout();
      logger.info( "Wait before lookup for " + retryTimeout );
      try
      {
        Thread.sleep( retryTimeout );
      }
      catch( InterruptedException e1 )
      {
        throw new RuntimeException( e1 );
      }

      if(reconnectAttemptListener != null)
        reconnectAttemptListener.handle( new ReconnectAttempt( timeOutManager.repeatedTimes(), retryTimeout, e.toString() ) );

      return lookup( );
    }
  }
}
