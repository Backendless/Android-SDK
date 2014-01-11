package com.backendless.social;

import android.app.Activity;
import android.webkit.WebView;
import com.backendless.async.callback.AsyncCallback;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class SocialWebViewLoginStrategy extends AbstractSocialLoginStrategy
{
  public SocialWebViewLoginStrategy( Activity context, WebView webView, SocialType socialType,
                                        Map<String, String> fieldsMappings, List<String> permissions,
                                        AsyncCallback<JSONObject> responder )
  {
    super( context, webView, socialType, fieldsMappings, permissions, responder );
  }

  @Override
  public BackendlessSocialJSInterface getJSInterface( AsyncCallback<JSONObject> responder )
  {
    try
    {
      return new BackendlessSocialJSInterfaceAnnotated( getContext(), responder );
    }
    catch( Exception e )
    {
      return new BackendlessSocialJSInterface( getContext(), responder );
    }
  }

  @Override
  public void createLayout()
  {}
}
