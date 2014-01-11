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
import com.backendless.exceptions.BackendlessFault;

abstract class CollectionDownloadTask<E>
{
  AsyncCallback<BackendlessCollection<E>> responder;

  public CollectionDownloadTask( AsyncCallback<BackendlessCollection<E>> responder )
  {
    this.responder = responder;
  }

  public void executeThis()
  {
    executeThis( 0, 0 );
  }

  public void executeThis( final int pageSize, final int offset )
  {
    ThreadPoolService.getPoolExecutor().execute( new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          BackendlessCollection<E> result = doInBackground( pageSize, offset );
          ResponseCarrier.getInstance().deliverMessage( new AsyncMessage<BackendlessCollection<E>>( result, responder ) );
        }
        catch( Exception e )
        {
          ResponseCarrier.getInstance().deliverMessage( new AsyncMessage<BackendlessCollection<E>>( new BackendlessFault( e ), responder ) );
        }
      }
    } );
  }

  abstract BackendlessCollection<E> doInBackground( int pageSize, int offset ) throws Exception;
}