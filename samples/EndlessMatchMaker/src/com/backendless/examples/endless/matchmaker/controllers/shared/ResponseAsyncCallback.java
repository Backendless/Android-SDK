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

package com.backendless.examples.endless.matchmaker.controllers.shared;

import android.content.Context;
import android.widget.Toast;
import com.backendless.examples.endless.matchmaker.utils.Log;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;

public class ResponseAsyncCallback<E> implements AsyncCallback<E>
{
  private Context context;

  public ResponseAsyncCallback( Context context )
  {
    this.context = context;
  }

  public void handleResponse( E response )
  {
  }

  public final void handleFault( BackendlessFault fault )
  {
    if( fault.getCode().equals( String.valueOf( ExceptionMessage.NOT_INITIALIZED ) ) )
      Lifecycle.runLoginActivity( context );

    Toast.makeText( context, fault.getMessage(), Toast.LENGTH_SHORT ).show();
    Log.logLine( fault );
  }
}