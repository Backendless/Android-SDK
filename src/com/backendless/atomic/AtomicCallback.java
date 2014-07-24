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

package com.backendless.atomic;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class AtomicCallback implements AsyncCallback<Object>
{
  private final AsyncCallback<Long> realCallback;

  public AtomicCallback( AsyncCallback<Long> realCallback )
  {
    this.realCallback = realCallback;
  }

  @Override
  public void handleResponse( Object response )
  {
    if( response instanceof Integer )
      response = Long.valueOf( response.toString() );

    if( response instanceof Double )
      response = Double.valueOf( response.toString() );

    realCallback.handleResponse( (Long) response );
  }

  @Override
  public void handleFault( BackendlessFault Object )
  {
    realCallback.handleFault( Object );
  }
}
