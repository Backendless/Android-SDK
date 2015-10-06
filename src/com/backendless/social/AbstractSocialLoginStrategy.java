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

package com.backendless.social;

import android.app.Activity;
import android.app.ProgressDialog;
import android.webkit.WebView;
import com.backendless.Backendless;
import com.backendless.SocialAsyncCallback;
import com.backendless.async.callback.AsyncCallback;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public abstract class AbstractSocialLoginStrategy
{
  private android.app.ProgressDialog spinner;
  private Activity context;
  private WebView webView;
  private AbstractSocialLoginStrategy.SocialType socialType;
  private Map<String, String> fieldsMappings;
  private java.util.List<String> permissions;
  private com.backendless.async.callback.AsyncCallback<org.json.JSONObject> responder;

  protected AbstractSocialLoginStrategy( Activity context, WebView webView,
                                         AbstractSocialLoginStrategy.SocialType socialType,
                                         Map<String, String> fieldsMappings, java.util.List<String> permissions,
                                         com.backendless.async.callback.AsyncCallback<org.json.JSONObject> responder )
  {
    this.context = context;
    this.webView = webView;
    this.socialType = socialType;
    this.fieldsMappings = fieldsMappings;
    this.permissions = permissions;
    this.responder = responder;
  }

  public void run()
  {
    SocialAsyncCallback authorizationUrlCallback = new SocialAsyncCallback( this, responder );

    switch( socialType )
    {
      case FACEBOOK:
        Backendless.UserService.getFacebookServiceAuthorizationUrlLink( fieldsMappings, permissions, authorizationUrlCallback );
        return;

      case TWITTER:
        Backendless.UserService.getTwitterServiceAuthorizationUrlLink( fieldsMappings, authorizationUrlCallback );
        return;

      case GOOGLE_PLUS:
        Backendless.UserService.getGooglePlusServiceAuthorizationUrlLink( fieldsMappings, permissions, authorizationUrlCallback );
        return;
    }
  }

  public void createSpinner()
  {
    spinner = new android.app.ProgressDialog( context );
    spinner.requestWindowFeature( android.view.Window.FEATURE_NO_TITLE );
    spinner.setMessage( "Loading..." );
    spinner.show();
  }

  public void dismiss()
  {
    if( getWebView() != null )
      getWebView().stopLoading();

    if( getSpinner().isShowing() )
      getSpinner().dismiss();
  }

  public ProgressDialog getSpinner()
  {
    return spinner;
  }

  public Activity getContext()
  {
    return context;
  }

  public WebView getWebView()
  {
    return webView;
  }

  public void setWebView( WebView webView )
  {
    this.webView = webView;
  }

  public abstract BackendlessSocialJSInterface getJSInterface( AsyncCallback<JSONObject> responder );

  public abstract void createLayout();

  public static enum SocialType
  {
    TWITTER, FACEBOOK, GOOGLE_PLUS
  }

  public static class Builder
  {
    private AbstractSocialLoginStrategy loginStrategy;

    public Builder( Activity context, WebView webView, SocialType socialType, Map<String, String> fieldsMappings,
                    List<String> permissions, AsyncCallback<JSONObject> responder )
    {
      if( webView != null )
        this.loginStrategy = new SocialWebViewLoginStrategy( context, webView, socialType, fieldsMappings, permissions, responder );
      else
        this.loginStrategy = new SocialDialogLoginStrategy( context, socialType, fieldsMappings, permissions, responder );
    }

    public AbstractSocialLoginStrategy build()
    {
      return loginStrategy;
    }
  }
}