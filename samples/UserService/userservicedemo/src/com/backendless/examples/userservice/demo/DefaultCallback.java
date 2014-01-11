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

package com.backendless.examples.userservice.demo;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;

public class DefaultCallback<T> extends BackendlessCallback<T>
{
  Context context;
  ProgressDialog progressDialog;

  public DefaultCallback( Context context )
  {
    this.context = context;
    progressDialog = ProgressDialog.show( context, "", "Loading", true );
  }

  @Override
  public void handleResponse( T response )
  {
    progressDialog.cancel();
  }

  //This override is optional
  @Override
  public void handleFault( BackendlessFault fault )
  {
    progressDialog.cancel();
    Toast.makeText( context, fault.getMessage(), Toast.LENGTH_SHORT ).show();
  }
}
