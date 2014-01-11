package com.backendless.examples.dataservice.tododemo;

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

  @Override
  public void handleFault( BackendlessFault fault )
  {
    progressDialog.cancel();
    Toast.makeText( context, fault.getMessage(), Toast.LENGTH_SHORT ).show();
  }
}
