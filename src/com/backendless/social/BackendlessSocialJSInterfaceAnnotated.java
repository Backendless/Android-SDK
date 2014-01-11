package com.backendless.social;

import android.app.Activity;
import android.app.Dialog;
import com.backendless.async.callback.AsyncCallback;

public class BackendlessSocialJSInterfaceAnnotated extends BackendlessSocialJSInterface
{
  public BackendlessSocialJSInterfaceAnnotated( Activity context, AsyncCallback responder )
  {
    super( context, responder );
  }

  public BackendlessSocialJSInterfaceAnnotated( Activity context, Dialog dialog, AsyncCallback responder )
  {
    super( context, dialog, responder );
  }

  @android.webkit.JavascriptInterface
  @Override
  public void processHTML( String html )
  {
    super.processHTML( html );
  }
}
