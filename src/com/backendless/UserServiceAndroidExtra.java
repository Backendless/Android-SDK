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

package com.backendless;

import android.os.Bundle;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.core.responder.AdaptingResponder;
import com.backendless.core.responder.policy.BackendlessUserAdaptingPolicy;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.social.AbstractSocialLoginStrategy;
import com.backendless.utils.JSONObjectConverter;
import com.facebook.*;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

class UserServiceAndroidExtra
{
  private static final UserServiceAndroidExtra instance = new UserServiceAndroidExtra();
  private static final String GOOGLE_ACCOUNT_TYPE = "com.google";

  static UserServiceAndroidExtra getInstance()
  {
    return instance;
  }

  private UserServiceAndroidExtra()
  {
  }

  void loginWithFacebookSdk( final android.app.Activity context, CallbackManager callbackManager, final AsyncCallback<BackendlessUser> responder )
  {
    List<String> permissions = new ArrayList<String>();
    permissions.add( "email" );
    permissions.add( "public_profile" );

    Map<String, String> facebookFieldsMappings = new HashMap<String, String>(  );
    facebookFieldsMappings.put( "email", "email" );

    loginWithFacebookSdk( context, facebookFieldsMappings, permissions, callbackManager, responder );
  }

  void loginWithFacebookSdk( final android.app.Activity context, final Map<String, String> facebookFieldsMappings,
                             List<String> permissions, CallbackManager callbackManager, final AsyncCallback<BackendlessUser> responder )
  {
    LoginManager.getInstance().registerCallback( callbackManager, new FacebookCallback<LoginResult>()
    {
      @Override
      public void onSuccess( LoginResult loginResult )
      {
        getBackendlessUser( loginResult.getAccessToken(), facebookFieldsMappings, responder );
      }

      @Override
      public void onCancel()
      {
        responder.handleFault( new BackendlessFault( ExceptionMessage.FACEBOOK_LOGINNING_CANCELED ) );
      }

      @Override
      public void onError( FacebookException exception )
      {
        responder.handleFault( new BackendlessFault( ExceptionMessage.NULL_GRAPH_USER ) );
         }
       } );



    LoginManager.getInstance().logInWithReadPermissions( context, permissions );

  }

  private void getBackendlessUser( final AccessToken accessToken, final Map<String, String> facebookFieldsMappings, final AsyncCallback<BackendlessUser> responder )
  {
    GraphRequest request = GraphRequest.newMeRequest( accessToken, new GraphRequest.GraphJSONObjectCallback()
      {
        @Override
        public void onCompleted( JSONObject object,
                                 GraphResponse response )
        {
          FacebookBundle facebookBundle = new FacebookBundle( response, accessToken );
          Object[] requestData = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), facebookBundle.socialUserId, facebookBundle.accessToken, facebookBundle.expirationDate, facebookBundle.permissions, facebookFieldsMappings };
          Invoker.invokeAsync( UserService.USER_MANAGER_SERVER_ALIAS, "loginWithFacebook", requestData, responder, new AdaptingResponder( BackendlessUser.class, new BackendlessUserAdaptingPolicy() ) );
        }
      } );

    Bundle parameters = new Bundle();
    parameters.putString("fields", "id,name,link");
    request.setParameters( parameters );
    request.executeAsync();
  }

  void loginWithFacebook( android.app.Activity context, android.webkit.WebView webView,
                          Map<String, String> facebookFieldsMappings, List<String> permissions,
                          final AsyncCallback<BackendlessUser> responder )
  {
    new AbstractSocialLoginStrategy.Builder( context, webView, AbstractSocialLoginStrategy.SocialType.FACEBOOK, facebookFieldsMappings, permissions, getSocialDialogResponder( responder ) ).build().run();
  }

  void loginWithTwitter( android.app.Activity context, android.webkit.WebView webView,
                         Map<String, String> twitterFieldsMappings, AsyncCallback<BackendlessUser> responder )
  {
    new AbstractSocialLoginStrategy.Builder( context, webView, AbstractSocialLoginStrategy.SocialType.TWITTER, twitterFieldsMappings, null, getSocialDialogResponder( responder ) ).build().run();
  }

  void loginWithGooglePlusSdk(  String tokenId, String accessToken, final Map<String, String> fieldsMappings,
                             List<String> permissions, final AsyncCallback<BackendlessUser> responder )
  {
     Invoker.invokeAsync( UserService.USER_MANAGER_SERVER_ALIAS, "loginWithGooglePlus", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), tokenId, accessToken, permissions, fieldsMappings }, new AsyncCallback<BackendlessUser>()
    {
      @Override
      public void handleResponse( BackendlessUser response )
      {
        if( responder != null )
          responder.handleResponse( response );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        if( responder != null )
          responder.handleFault( fault );
      }
    } );
  }

  void loginWithGooglePlus( android.app.Activity context, android.webkit.WebView webView,
                          Map<String, String> googlePlusFieldsMappings, List<String> permissions,
                          final AsyncCallback<BackendlessUser> responder )
  {
    new AbstractSocialLoginStrategy.Builder( context, webView, AbstractSocialLoginStrategy.SocialType.GOOGLE_PLUS, googlePlusFieldsMappings, permissions, getSocialDialogResponder( responder ) ).build().run();
  }

  private AsyncCallback<JSONObject> getSocialDialogResponder( final AsyncCallback<BackendlessUser> responder )
  {
    return new AsyncCallback<JSONObject>()
    {
      @Override
      public void handleResponse( JSONObject response )
      {
        try
        {
          BackendlessUser result = new BackendlessUser();

          Iterator keys = response.keys();
          while( keys.hasNext() )
          {
            String key = String.valueOf( keys.next() );
            result.setProperty( key, JSONObjectConverter.fromJson(response.get(key)) );
          }

          if( responder != null )
            responder.handleResponse( result );
        }
        catch( Exception e )
        {
          if( responder != null )
            responder.handleFault( new BackendlessFault( e ) );
        }
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        if( responder != null )
          responder.handleFault( fault );
      }
    };
  }

  private static class FacebookBundle
  {
    String accessToken;
    Date expirationDate;
    Set<String> permissions;
    String socialUserId;

    FacebookBundle( GraphResponse response, AccessToken accessToken )
    {

      JSONObject jsonObj = response.getJSONObject();
      if ( jsonObj == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_FACEBOOK_RESPONSE_OBJECT );

      expirationDate = accessToken.getExpires();
      this.accessToken  = accessToken.getToken();
      permissions = accessToken.getPermissions();
      try
      {
        socialUserId = jsonObj.getString( "id" );
      }
      catch( JSONException e )
      {
        throw new IllegalArgumentException( ExceptionMessage.NULL_FACEBOOK_USER_ID );
      }
    }
  }
}
