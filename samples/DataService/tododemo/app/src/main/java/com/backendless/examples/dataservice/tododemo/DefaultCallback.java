package com.backendless.examples.dataservice.tododemo;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.backendless.async.callback.BackendlessCallback;
import com.backendless.example.dataservice.tododemo.R;
import com.backendless.exceptions.BackendlessFault;

public class DefaultCallback<T> extends BackendlessCallback<T>
{
  private Context context;
  private ProgressBar progressBar;

  public DefaultCallback( Context context )
  {
    this.context = context;

    LinearLayout layout = ((Activity) context).findViewById( R.id.parentLayout );
    progressBar = new ProgressBar( context, null, android.R.attr.progressBarStyleLarge );
    progressBar.setIndeterminate( true );
    progressBar.setVisibility( View.VISIBLE );
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( 1000, 100 );
    params.addRule( RelativeLayout.CENTER_IN_PARENT );
    params.addRule( RelativeLayout.CENTER_HORIZONTAL );
    layout.addView( progressBar, params );
  }

  @Override
  public void handleResponse( T response )
  {
    progressBar.setVisibility( View.GONE );
  }

  @Override
  public void handleFault( BackendlessFault fault )
  {
    progressBar.setVisibility( View.GONE );
    Toast.makeText( context, fault.getMessage(), Toast.LENGTH_SHORT ).show();
  }
}
