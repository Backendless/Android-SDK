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

import com.backendless.Counters;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.utils.ReflectionUtil;

public class AtomicCallback<T> implements AsyncCallback<Object>
{
  private final AsyncCallback<T> realCallback;

  public AtomicCallback( AsyncCallback<T> realCallback )
  {
    this.realCallback = realCallback;
  }

  @Override
  public void handleResponse( Object response )
  {
    Number numberResult = null;

    if( response instanceof Integer )
      numberResult = Long.valueOf( response.toString() );
    else if( response instanceof Double )
      numberResult = Double.valueOf( response.toString() );
    else
      realCallback.handleFault( new BackendlessFault( "Result is not a number. Expecting either Integer or Double, but received " + response.getClass() ) );

    Class counterType = (Class) ReflectionUtil.getCallbackGenericType( realCallback );

    Number result = Counters.convertToType( numberResult, counterType );
    realCallback.handleResponse( (T) result );
  }

  @Override
  public void handleFault( BackendlessFault Object )
  {
    realCallback.handleFault( Object );
  }
}
