package com.backendless;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.social.AbstractSocialLoginStrategy;
import org.json.JSONObject;

public class SocialAsyncCallback implements AsyncCallback<String>
{
  private AbstractSocialLoginStrategy loginStrategy;
  private AsyncCallback<JSONObject> responder;

  public SocialAsyncCallback( AbstractSocialLoginStrategy loginStrategy, AsyncCallback<JSONObject> responder )
  {
    this.loginStrategy = loginStrategy;
    this.responder = responder;
    loginStrategy.createSpinner();
  }

  @Override
  public void handleResponse( String response )
  {
    WebView webView = loginStrategy.getWebView();
    final Context context = loginStrategy.getContext();
    final ProgressDialog spinner = loginStrategy.getSpinner();

    if( webView == null )
    {
      webView = new android.webkit.WebView( context );
      loginStrategy.setWebView( webView );
    }

    if( Build.VERSION.SDK_INT >= 11 )
      webView.setLayerType( View.LAYER_TYPE_SOFTWARE, null );

    String jsMethodName = "Backendless_Android_" + Backendless.getApplicationId().replace( "-", "_" ) + "_" + Backendless.getVersion();
    webView.addJavascriptInterface( loginStrategy.getJSInterface( responder ), jsMethodName );

    webView.getSettings().setJavaScriptEnabled( true );
    webView.getSettings().setLoadWithOverviewMode( false );
    webView.getSettings().setDefaultTextEncodingName( "utf-8" );
    webView.setHorizontalScrollBarEnabled( true );
    webView.setVerticalScrollBarEnabled( true );
    webView.setLayoutParams( new RelativeLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ) );
    webView.setWebViewClient( new android.webkit.WebViewClient()
    {
      @Override
      public void onPageFinished( android.webkit.WebView view, String url )
      {
        if( spinner.isShowing() )
          spinner.dismiss();
      }

      @Override
      public void onPageStarted( android.webkit.WebView view, String url, android.graphics.Bitmap favicon )
      {
        if( !loginStrategy.getContext().isFinishing() && !spinner.isShowing() )
          spinner.show();
      }

      @Override
      public boolean shouldOverrideUrlLoading( android.webkit.WebView view, String url )
      {
        return url.startsWith( "market://" );
      }
    } );

    webView.requestFocus( View.FOCUS_DOWN );
    loginStrategy.createLayout();
    webView.loadUrl( response );
  }

  @Override
  public void handleFault( BackendlessFault fault )
  {
    if( responder != null )
      responder.handleFault( fault );
  }
}