package com.backendless.rt;

import com.backendless.Invoker;
import com.backendless.exceptions.BackendlessException;

import java.util.logging.Logger;

class RTLookupService
{
  private static final Logger logger = Logger.getLogger( "RTLookupService" );

  private static final int INITIAL_TIMEOUT = 100;
  private int retryTimeout = INITIAL_TIMEOUT;

  synchronized String lookup()
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

      return lookup();
    }
  }
}
