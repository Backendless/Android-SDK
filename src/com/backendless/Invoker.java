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

import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.message.AsyncMessage;
import com.backendless.core.ResponseCarrier;
import com.backendless.core.responder.AdaptingResponder;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import weborb.client.Fault;
import weborb.client.IChainedResponder;
import weborb.client.IResponder;
import weborb.client.WeborbClient;
import weborb.util.ThreadContext;

@SuppressWarnings( "unchecked" )
public class Invoker
{
  private static final String DESTINATION = "GenericDestination";
  private static final int DEFAULT_TIMEOUT = 100500;
  private static WeborbClient weborbClient;

  static void reinitialize()
  {
    String urlEnding = Backendless.getApplicationUrl() + "/binary";
    weborbClient = new WeborbClient( urlEnding, DEFAULT_TIMEOUT, DESTINATION );
    weborbClient.setCookiesDateFormat( "EEE, dd-MMM-yy HH:mm:ss z" );

    if( Backendless.isAndroid() )
      weborbClient.setHostnameVerifier( new org.apache.http.conn.ssl.StrictHostnameVerifier() );
  }

  public static WeborbClient getWebOrbClient()
  {
    return weborbClient;
  }

  public static <T> void invokeAsync( String className, String methodName, Object[] args, AsyncCallback<T> callback )
  {
    invokeAsync( className, methodName, args, callback, null );
  }

  public static <T> void invokeAsync( final String className, final String methodName, final Object[] args,
                                      final AsyncCallback<T> callback, final IChainedResponder responder )
  {
    ThreadPoolService.getThreadPoolExecutor().execute(new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          T result = Invoker.invokeSync( className, methodName, args, responder );
          ResponseCarrier.getInstance().deliverMessage( new AsyncMessage<>( result, callback ) );
        }
        catch( BackendlessException e )
        {
          ResponseCarrier.getInstance().deliverMessage( new AsyncMessage<>( new BackendlessFault( e ), callback ) );
        }
      }
    } );
  }

  public static <T> T invokeSync( String className, String methodName, Object[] args,
                                  IChainedResponder chainedResponder ) throws BackendlessException
  {
    SyncResponder invokeResponder = new SyncResponder();

    if( chainedResponder == null )
      chainedResponder = new AdaptingResponder<T>();

    chainedResponder.setNextResponder( invokeResponder );

    try
    {
      ThreadContext.cleanup();
      getWebOrbClient().invoke( className, methodName, args, Backendless.getForwardableHeaders(), null, HeadersManager.getInstance().getHeaders(), chainedResponder );
    }
    catch( Exception e )
    {
      throw new BackendlessException( ExceptionMessage.SERVER_ERROR, e.getMessage() );
    }

    return (T) invokeResponder.getResult();
  }

  public static <T> T invokeSync( String className, String methodName, Object[] args ) throws BackendlessException
  {
    return invokeSync( className, methodName, args, null );
  }

  static class SyncResponder implements IResponder
  {
    private Object result;
    private BackendlessException exception;

    public void responseHandler( Object o )
    {
      result = o;
    }

    public void errorHandler( Fault fault )
    {
      this.exception = (fault instanceof BackendlessFault)
              ? new BackendlessException( (BackendlessFault) fault )
              : new BackendlessException( new BackendlessFault( fault ) );
    }

    public Object getResult() throws BackendlessException
    {
      if( exception == null )
        return result;

      throw exception;
    }
  }
}
