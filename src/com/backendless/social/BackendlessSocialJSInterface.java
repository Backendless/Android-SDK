package com.backendless.social;

import android.app.Activity;
import android.app.Dialog;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import org.json.JSONException;
import org.json.JSONObject;

public class BackendlessSocialJSInterface
{
  private Dialog dialog;
  private Activity context;
  private AsyncCallback responder;

  public BackendlessSocialJSInterface( Activity context, AsyncCallback responder )
  {
    this.context = context;
    this.responder = responder;
  }

  public BackendlessSocialJSInterface( Activity context, Dialog dialog, AsyncCallback responder )
  {
    this.context = context;
    this.dialog = dialog;
    this.responder = responder;
  }

  public void processHTML( final String html )
  {
    context.runOnUiThread( new Runnable()
    {
      @Override
      public void run()
      {
        if( responder != null )
        {
          JSONObject parsedResult = null;

          try
          {
            parsedResult = new JSONObject( html );
          }
          catch( org.json.JSONException e )
          {
            responder.handleFault( new BackendlessFault( e ) );
          }

          try
          {
            String faultResult = parsedResult.getString( "fault" );
            responder.handleFault( new BackendlessFault( faultResult ) );
          }
          catch( JSONException e )
          {
            responder.handleResponse( parsedResult );
          }
        }

        if( dialog != null && dialog.isShowing() )
          dialog.dismiss();
      }
    } );
  }
}
