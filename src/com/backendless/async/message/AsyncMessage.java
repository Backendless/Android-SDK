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

package com.backendless.async.message;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

final public class AsyncMessage<E> implements IAsyncMessage<E>
{
  private IHandler handler;

  public AsyncMessage( E response, AsyncCallback<E> callback )
  {
    handler = new ResponseHandler<E>( response, callback );
  }

  public AsyncMessage( BackendlessFault fault, AsyncCallback<E> callback )
  {
    handler = new FaultHandler<E>( fault, callback );
  }

  @Override
  public void handleCallback()
  {
    handler.handle();
  }

  private static interface IHandler
  {
    void handle();
  }

  private static class ResponseHandler<E> implements IHandler
  {
    private AsyncCallback<E> callback;
    private E response;

    private ResponseHandler( E response, AsyncCallback<E> callback )
    {
      this.response = response;
      this.callback = callback;
    }

    @Override
    public void handle()
    {
      if( callback != null )
        callback.handleResponse( response );
    }
  }

  private static class FaultHandler<E> implements IHandler
  {
    private AsyncCallback<E> callback;
    private BackendlessFault fault;

    private FaultHandler( BackendlessFault fault, AsyncCallback<E> callback )
    {
      this.fault = fault;
      this.callback = callback;
    }

    @Override
    public void handle()
    {
      if( callback != null )
        callback.handleFault( fault );
    }
  }
}
