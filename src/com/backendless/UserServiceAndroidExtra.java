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

import com.backendless.async.callback.AsyncCallback;
import com.backendless.core.responder.AdaptingResponder;
import com.backendless.core.responder.policy.BackendlessUserAdaptingPolicy;
import com.backendless.exceptions.BackendlessFault;

import java.util.*;

class UserServiceAndroidExtra
{
  private static final UserServiceAndroidExtra instance = new UserServiceAndroidExtra();

  static UserServiceAndroidExtra getInstance()
  {
    return instance;
  }

  private UserServiceAndroidExtra()
  {
  }

  void loginWithOAuth1(
          String authProviderName, String authToken, BackendlessUser guestUser, String authTokenSecret, Map<String, String> fieldsMappings,
          final AsyncCallback<BackendlessUser> responder )
  {
    if( !authProviderName.equals( "Twitter" ) )
      throw new IllegalArgumentException( "OAuth1 provider '" + authProviderName + "' is not supported" );

    if( fieldsMappings == null )
      fieldsMappings = new HashMap<>();

    Invoker.invokeAsync(
            UserService.USER_MANAGER_SERVER_ALIAS,
            "loginWithTwitter",
            new Object[] { authToken, authTokenSecret, fieldsMappings, guestUser == null ? null : guestUser.getProperties() },
            new AsyncCallback<BackendlessUser>()
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
            }
    );
  }

  void loginWithOAuth2(
          String authProviderName, String accessToken, BackendlessUser guestUser, Map<String, String> fieldsMappings,
          final AsyncCallback<BackendlessUser> responder )
  {
    if (fieldsMappings == null)
      fieldsMappings = new HashMap<>();

    Invoker.invokeAsync(
            UserService.USER_MANAGER_SERVER_ALIAS,
            "loginWithOAuth2",
            new Object[] { authProviderName, accessToken, fieldsMappings, guestUser == null ? null : guestUser.getProperties() },
            new AsyncCallback<BackendlessUser>()
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
            },
            new AdaptingResponder<>( BackendlessUser.class, new BackendlessUserAdaptingPolicy() )
    );
  }
}
