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
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.BackendlessSerializer;
import com.backendless.persistence.local.UserIdStorageFactory;
import com.backendless.persistence.local.UserTokenStorageFactory;
import com.backendless.property.AbstractProperty;
import com.backendless.property.UserProperty;
import com.facebook.CallbackManager;
import weborb.types.Types;

import java.util.*;

public final class UserService
{
  final static String USER_MANAGER_SERVER_ALIAS = "com.backendless.services.users.UserService";
  private final static String PREFS_NAME = "backendless_pref";

  private static BackendlessUser currentUser = new BackendlessUser();
  private final static Object currentUserLock = new Object();

  public static final String USERS_TABLE_NAME = "Users";

  private static final UserService instance = new UserService();

  static UserService getInstance()
  {
    return instance;
  }

  private UserService()
  {
    Types.addClientClassMapping( "com.backendless.services.users.property.AbstractProperty", AbstractProperty.class );
    Types.addClientClassMapping( "com.backendless.services.users.property.UserProperty", UserProperty.class );
    Types.addClientClassMapping( "Users", BackendlessUser.class );
  }

  public BackendlessUser CurrentUser()
  {
    if( currentUser != null && currentUser.getProperties().isEmpty() )
      return null;

    return currentUser;
  }

  private UserServiceAndroidExtra getUserServiceAndroidExtra()
  {
    if( !Backendless.isAndroid() )
      throw new RuntimeException( ExceptionMessage.NOT_ANDROID );

    return UserServiceAndroidExtra.getInstance();
  }

  public BackendlessUser register( BackendlessUser user ) throws BackendlessException
  {
    checkUserToBeProper( user );

    BackendlessSerializer.serializeUserProperties( user );
    String password = user.getPassword();
    BackendlessUser userToReturn = Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "register", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), user.getProperties() }, new AdaptingResponder( BackendlessUser.class, new BackendlessUserAdaptingPolicy() ) );
    user.clearProperties();
    userToReturn.setPassword( password );
    user.putProperties( userToReturn.getProperties() );

    return userToReturn;
  }

  public void register( final BackendlessUser user, final AsyncCallback<BackendlessUser> responder )
  {
    try
    {
      checkUserToBeProper( user );

      BackendlessSerializer.serializeUserProperties( user );

      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "register", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), user.getProperties() }, new AsyncCallback<BackendlessUser>()
      {
        @Override
        public void handleResponse( BackendlessUser response )
        {
          response.setPassword( user.getPassword() );
          user.clearProperties();
          user.putProperties( response.getProperties() );

          if( responder != null )
            responder.handleResponse( response );
        }

        @Override
        public void handleFault( BackendlessFault fault )
        {
          if( responder != null )
            responder.handleFault( fault );
        }
      }, new AdaptingResponder( BackendlessUser.class, new BackendlessUserAdaptingPolicy() ) );
    }
    catch( BackendlessException e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public BackendlessUser update( final BackendlessUser user ) throws BackendlessException
  {
    checkUserToBeProperForUpdate( user );

    BackendlessSerializer.serializeUserProperties( user );

    if( user.getUserId() != null && user.getUserId().equals( "" ) )
      throw new IllegalArgumentException( ExceptionMessage.WRONG_USER_ID );

    BackendlessUser userToReturn = Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "update", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), user.getProperties() }, new AdaptingResponder( BackendlessUser.class, new BackendlessUserAdaptingPolicy() ) );
    user.clearProperties();
    user.putProperties( userToReturn.getProperties() );

    return userToReturn;
  }

  public void update( final BackendlessUser user, final AsyncCallback<BackendlessUser> responder )
  {
    try
    {
      checkUserToBeProperForUpdate( user );

      BackendlessSerializer.serializeUserProperties( user );

      if( user.getUserId() != null && user.getUserId().equals( "" ) )
        throw new IllegalArgumentException( ExceptionMessage.WRONG_USER_ID );

      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "update", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), user.getProperties() }, new AsyncCallback<BackendlessUser>()
      {
        @Override
        public void handleResponse( BackendlessUser response )
        {
          user.clearProperties();
          user.putProperties( response.getProperties() );

          if( responder != null )
            responder.handleResponse( response );
        }

        @Override
        public void handleFault( BackendlessFault fault )
        {
          if( responder != null )
            responder.handleFault( fault );
        }
      }, new AdaptingResponder( BackendlessUser.class, new BackendlessUserAdaptingPolicy() ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public BackendlessUser login( final String login, final String password ) throws BackendlessException
  {
    return login( login, password, false );
  }

  public BackendlessUser login( final String login, final String password,
                                boolean stayLoggedIn ) throws BackendlessException
  {
    synchronized( currentUserLock )
    {
      if( !currentUser.getProperties().isEmpty() )
        logout();

      if( login == null || login.equals( "" ) )
        throw new IllegalArgumentException( ExceptionMessage.NULL_LOGIN );

      if( password == null || password.equals( "" ) )
        throw new IllegalArgumentException( ExceptionMessage.NULL_PASSWORD );

      handleUserLogin( Invoker.<BackendlessUser>invokeSync( USER_MANAGER_SERVER_ALIAS, "login", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), login, password }, new AdaptingResponder( BackendlessUser.class, new BackendlessUserAdaptingPolicy() ) ), stayLoggedIn );

      return currentUser;
    }
  }

  public void login( final String login, final String password, final AsyncCallback<BackendlessUser> responder )
  {
    login( login, password, responder, false );
  }

  public void login( final String login, final String password, final AsyncCallback<BackendlessUser> responder,
                     final boolean stayLoggedIn )
  {
    if( !currentUser.getProperties().isEmpty() )
      logout( new AsyncCallback<Void>()
      {
        @Override
        public void handleResponse( Void response )
        {
          login( login, password, responder, stayLoggedIn );
        }

        @Override
        public void handleFault( BackendlessFault fault )
        {
          if( responder != null )
            responder.handleFault( fault );
        }
      } );
    else
      try
      {
        synchronized( currentUserLock )
        {
          if( login == null || login.equals( "" ) )
            throw new IllegalArgumentException( ExceptionMessage.NULL_LOGIN );

          if( password == null || password.equals( "" ) )
            throw new IllegalArgumentException( ExceptionMessage.NULL_PASSWORD );
          else
            Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "login", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), login, password }, getUserLoginAsyncHandler( responder, stayLoggedIn ) , new AdaptingResponder( BackendlessUser.class, new BackendlessUserAdaptingPolicy() ) );
        }
      }
      catch( Throwable e )
      {
        if( responder != null )
          responder.handleFault( new BackendlessFault( e ) );
      }
  }

  public void loginWithFacebookSdk( android.app.Activity context, CallbackManager callbackManager, final AsyncCallback<BackendlessUser> responder )
  {
    loginWithFacebookSdk(context, callbackManager, responder, false );
  }

  public void loginWithFacebookSdk( android.app.Activity context, CallbackManager callbackManager, final
                                    AsyncCallback<BackendlessUser> responder, boolean stayLoggedIn  )
  {
    AsyncCallback<BackendlessUser> internalResponder = getUserLoginAsyncHandler( responder, stayLoggedIn );
    getUserServiceAndroidExtra().loginWithFacebookSdk( context, callbackManager, internalResponder );
  }

  public void loginWithFacebookSdk( android.app.Activity context, final Map<String, String> facebookFieldsMappings,
                                    final List<String> permissions, CallbackManager callbackManager, final AsyncCallback<BackendlessUser> responder )
  {
    loginWithFacebookSdk( context, facebookFieldsMappings, permissions, callbackManager, responder, false );
  }

  public void loginWithFacebookSdk( android.app.Activity context, final Map<String, String> facebookFieldsMappings,
                             final List<String> permissions, CallbackManager callbackManager, final
                                    AsyncCallback<BackendlessUser> responder, boolean stayLoggedIn )
  {
    AsyncCallback<BackendlessUser> internalResponder = getUserLoginAsyncHandler( responder, stayLoggedIn );
    getUserServiceAndroidExtra().loginWithFacebookSdk( context, facebookFieldsMappings, permissions, callbackManager,
                                                       internalResponder );
  }

  public void loginWithFacebook( android.app.Activity context, final AsyncCallback<BackendlessUser> responder )
  {
    loginWithFacebook( context, null, null, null, responder );
  }

  public void loginWithFacebook( android.app.Activity context, android.webkit.WebView webView,
                                 final AsyncCallback<BackendlessUser> responder )
  {
    loginWithFacebook( context, webView, null, null, responder );
  }

  public void loginWithFacebook( android.app.Activity context, android.webkit.WebView webView,
                                 final AsyncCallback<BackendlessUser> responder, boolean stayLoggedIn )
  {
    loginWithFacebook( context, webView, null, null, responder, stayLoggedIn );
  }

  public void loginWithFacebook( android.app.Activity context, android.webkit.WebView webView,
                                 Map<String, String> facebookFieldsMappings, List<String> permissions,
                                 final AsyncCallback<BackendlessUser> responder )
  {
    loginWithFacebook( context, webView, facebookFieldsMappings, permissions, responder, false );
  }

  public void loginWithFacebook( android.app.Activity context, android.webkit.WebView webView,
                                 Map<String, String> facebookFieldsMappings, List<String> permissions,
                                 final AsyncCallback<BackendlessUser> responder, boolean stayLoggedIn )
  {
    getUserServiceAndroidExtra().loginWithFacebook( context, webView, facebookFieldsMappings, permissions, getUserLoginAsyncHandler( responder, stayLoggedIn ) );
  }

  public void loginWithTwitter( android.app.Activity context, AsyncCallback<BackendlessUser> responder )
  {
    loginWithTwitter( context, null, null, responder, false );
  }

  public void loginWithTwitter( android.app.Activity context, android.webkit.WebView webView,
                                AsyncCallback<BackendlessUser> responder )
  {
    loginWithTwitter( context, webView, null, responder, false );
  }

  public void loginWithTwitter( android.app.Activity context, Map<String, String> twitterFieldsMappings,
                                AsyncCallback<BackendlessUser> responder )
  {
    loginWithTwitter( context, null, twitterFieldsMappings, responder, false );
  }

  public void loginWithTwitter( android.app.Activity context, AsyncCallback<BackendlessUser> responder,
                                boolean stayLoggedIn )
  {
    loginWithTwitter( context, null, null, responder, stayLoggedIn );
  }

  public void loginWithTwitter( android.app.Activity context, android.webkit.WebView webView,
                                Map<String, String> twitterFieldsMappings, AsyncCallback<BackendlessUser> responder )
  {
    loginWithTwitter( context, webView, twitterFieldsMappings, responder, false );
  }

  public void loginWithTwitter( android.app.Activity context, android.webkit.WebView webView,
                                AsyncCallback<BackendlessUser> responder, boolean stayLoggedIn )
  {
    loginWithTwitter( context, webView, null, responder, stayLoggedIn );
  }

  public void loginWithTwitter( android.app.Activity context, Map<String, String> twitterFieldsMappings,
                                AsyncCallback<BackendlessUser> responder, boolean stayLoggedIn )
  {
    loginWithTwitter( context, null, twitterFieldsMappings, responder, stayLoggedIn );
  }

  public void loginWithTwitter( android.app.Activity context, android.webkit.WebView webView,
                                Map<String, String> twitterFieldsMappings, AsyncCallback<BackendlessUser> responder,
                                boolean stayLoggedIn )
  {
    getUserServiceAndroidExtra().loginWithTwitter( context, webView, twitterFieldsMappings, getUserLoginAsyncHandler( responder, stayLoggedIn ) );
  }

  public void loginWithGooglePlus( android.app.Activity context, final AsyncCallback<BackendlessUser> responder )
  {
    loginWithGooglePlus( context, null, null, null, responder );
  }

  public void loginWithGooglePlus( android.app.Activity context, android.webkit.WebView webView,
                                 final AsyncCallback<BackendlessUser> responder )
  {
    loginWithGooglePlus( context, webView, null, null, responder );
  }

  public void loginWithGooglePlus( android.app.Activity context, android.webkit.WebView webView,
                                 final AsyncCallback<BackendlessUser> responder, boolean stayLoggedIn )
  {
    loginWithGooglePlus( context, webView, null, null, responder, stayLoggedIn );
  }

  public void loginWithGooglePlus( android.app.Activity context, android.webkit.WebView webView,
                                 Map<String, String> googlePlusFieldsMappings, List<String> permissions,
                                 final AsyncCallback<BackendlessUser> responder )
  {
    loginWithGooglePlus( context, webView, googlePlusFieldsMappings, permissions, responder, false );
  }

  public void loginWithGooglePlus( android.app.Activity context, android.webkit.WebView webView,
                                 Map<String, String> googlePlusFieldsMappings, List<String> permissions,
                                 final AsyncCallback<BackendlessUser> responder, boolean stayLoggedIn )
  {
    getUserServiceAndroidExtra().loginWithGooglePlus( context, webView, googlePlusFieldsMappings, permissions, getUserLoginAsyncHandler( responder, stayLoggedIn ) );
  }

  public void loginWithGooglePlusSdk( String tokenId, String accessToken, final AsyncCallback<BackendlessUser> responder )
  {
    loginWithGooglePlusSdk( tokenId, accessToken, null, null, responder );
  }

  public void loginWithGooglePlusSdk( String tokenId, String accessToken, final AsyncCallback<BackendlessUser> responder, boolean stayLoggedIn )
  {
    loginWithGooglePlusSdk( tokenId, accessToken, null, null, responder, stayLoggedIn );
  }

  public void loginWithGooglePlusSdk( String tokenId, String accessToken, final Map<String, String> fieldsMappings,
                                    List<String> permissions, final AsyncCallback<BackendlessUser> responder )
  {
      loginWithGooglePlusSdk( tokenId, accessToken, fieldsMappings, permissions, responder, false );
  }

  public void loginWithGooglePlusSdk( String tokenId, String accessToken, final Map<String, String> fieldsMappings,
                                      List<String> permissions, final AsyncCallback<BackendlessUser> responder, boolean stayLoggedIn )
  {
    AsyncCallback<BackendlessUser> internalResponder = getUserLoginAsyncHandler( responder, stayLoggedIn );
    getUserServiceAndroidExtra().loginWithGooglePlusSdk( tokenId, accessToken, fieldsMappings, permissions, internalResponder );
  }

  public void logout() throws BackendlessException
  {
    synchronized( currentUserLock )
    {
      try
      {
        Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "logout", new Object[] { Backendless.getApplicationId(), Backendless.getVersion() } );
      }
      catch( BackendlessException fault )
      {
        if( !isLogoutFaultAllowed( fault.getCode() ) )
          throw fault;
        //else everything is OK
      }

      handleLogout();
    }
  }

  private void handleLogout()
  {
    currentUser = new BackendlessUser();
    HeadersManager.getInstance().removeHeader( HeadersManager.HeadersEnum.USER_TOKEN_KEY );
    UserTokenStorageFactory.instance().getStorage().set( "" );
    UserIdStorageFactory.instance().getStorage().set( "" );
  }

  public void logout( final AsyncCallback<Void> responder )
  {
    synchronized( currentUserLock )
    {
      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "logout", new Object[] { Backendless.getApplicationId(), Backendless.getVersion() }, new AsyncCallback<Void>()
      {
        @Override
        public void handleResponse( Void response )
        {
          try
          {
            handleLogout();
          }
          catch( Throwable e )
          {
            if( responder != null )
              responder.handleFault( new BackendlessFault( e ) );
          }

          if( responder != null )
            responder.handleResponse( response );
        }

        @Override
        public void handleFault( BackendlessFault fault )
        {
          if( isLogoutFaultAllowed( fault.getCode() ) )
          {
            handleResponse( null );
            return;
          }

          if( responder != null )
            responder.handleFault( fault );
        }
      } );
    }
  }

  public void restorePassword( String identity ) throws BackendlessException
  {
    if( identity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_IDENTITY );

    Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "restorePassword", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), identity } );
  }

  public void restorePassword( final String identity, final AsyncCallback<Void> responder )
  {
    try
    {
      if( identity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_IDENTITY );

      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "restorePassword", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), identity }, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public void resendEmailConfirmation( String email ) throws BackendlessException
  {
    if( email == null || email.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMAIL );

    Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "resendEmailConfirmation", new Object[]{ Backendless.getApplicationId(), Backendless.getVersion(), email } );
  }

  public void resendEmailConfirmation( String email, AsyncCallback<Void> responder )
  {
    try
    {
      if( email == null || email.isEmpty() )
        throw new IllegalArgumentException( ExceptionMessage.NULL_EMAIL );

      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "resendEmailConfirmation", new Object[]{ Backendless.getApplicationId(), Backendless.getVersion(), email }, responder );    }
    catch ( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public BackendlessUser findById( String id ) throws BackendlessException
  {
    if( id == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_IDENTITY );

    return Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "findById", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), id, new ArrayList() } );
  }

  public void findById( final String id, final AsyncCallback<BackendlessUser> responder )
  {
    try
    {
      if( id == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_IDENTITY );

      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "findById", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), id, new ArrayList() }, new AsyncCallback<BackendlessUser>()
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
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public void assignRole( String identity, String roleName ) throws BackendlessException
  {
    if( identity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_IDENTITY );

    if( roleName == null || roleName.equals( "" ) )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ROLE_NAME );

    Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "assignRole", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), identity, roleName } );
  }

  public void assignRole( final String identity, final String roleName, final AsyncCallback<Void> responder )
  {
    try
    {
      if( identity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_IDENTITY );

      if( roleName == null || roleName.equals( "" ) )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ROLE_NAME );

      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "assignRole", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), identity, roleName }, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public void unassignRole( String identity, String roleName ) throws BackendlessException
  {
    if( identity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_IDENTITY );

    if( roleName == null || roleName.equals( "" ) )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ROLE_NAME );

    Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "unassignRole", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), identity, roleName } );
  }

  public void unassignRole( final String identity, final String roleName, final AsyncCallback<Void> responder )
  {
    try
    {
      if( identity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_IDENTITY );

      if( roleName == null || roleName.equals( "" ) )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ROLE_NAME );

      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "unassignRole", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), identity, roleName }, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public List<String> getUserRoles() throws BackendlessException
  {
    return Arrays.asList( (String[]) Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "getUserRoles", new Object[] { Backendless.getApplicationId(), Backendless.getVersion() } ) );
  }

  public void getUserRoles( final AsyncCallback<List<String>> responder )
  {
    try
    {
      AsyncCallback<String[]> callback = new AsyncCallback<String[]>()
      {
        @Override
        public void handleResponse( String[] response )
        {
          if( responder != null )
            responder.handleResponse( Arrays.asList( response ) );
        }

        @Override
        public void handleFault( BackendlessFault fault )
        {
          if( responder != null )
            responder.handleFault( fault );
        }
      };
      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "getUserRoles", new Object[] { Backendless.getApplicationId(), Backendless.getVersion() }, callback );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public List<UserProperty> describeUserClass() throws BackendlessException
  {
    UserProperty[] response = Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "describeUserClass", new Object[] { Backendless.getApplicationId(), Backendless.getVersion() } );

    return Arrays.asList( response );
  }

  public void describeUserClass( final AsyncCallback<List<UserProperty>> responder )
  {
    Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "describeUserClass", new Object[] { Backendless.getApplicationId(), Backendless.getVersion() }, new AsyncCallback<UserProperty[]>()
    {
      @Override
      public void handleResponse( UserProperty[] response )
      {
        if( responder != null )
          responder.handleResponse( Arrays.asList( response ) );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        if( responder != null )
          responder.handleFault( fault );
      }
    } );
  }

  public void getFacebookServiceAuthorizationUrlLink( Map<String, String> facebookFieldsMappings,
                                                      List<String> permissions,
                                                      AsyncCallback<String> responder ) throws BackendlessException
  {
    if( facebookFieldsMappings == null )
      facebookFieldsMappings = new HashMap<String, String>();

    if( permissions == null )
      permissions = new ArrayList<String>();

    Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "getFacebookServiceAuthorizationUrlLink", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), HeadersManager.getInstance().getHeader( HeadersManager.HeadersEnum.APP_TYPE_NAME ), facebookFieldsMappings, permissions }, responder );
  }

  public void getTwitterServiceAuthorizationUrlLink( Map<String, String> twitterFieldsMapping,
                                                     AsyncCallback<String> responder )
  {
    if( twitterFieldsMapping == null )
      twitterFieldsMapping = new HashMap<String, String>();

    Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "getTwitterServiceAuthorizationUrlLink", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), HeadersManager.getInstance().getHeader( HeadersManager.HeadersEnum.APP_TYPE_NAME ), twitterFieldsMapping }, responder );
  }

  public void getGooglePlusServiceAuthorizationUrlLink( Map<String, String> googlePlusFieldsMappings,
                                                      List<String> permissions,
                                                      AsyncCallback<String> responder ) throws BackendlessException
  {
    if( googlePlusFieldsMappings == null )
      googlePlusFieldsMappings = new HashMap<String, String>();

    if( permissions == null )
      permissions = new ArrayList<String>();

    Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "getGooglePlusServiceAuthorizationUrlLink", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), HeadersManager.getInstance().getHeader( HeadersManager.HeadersEnum.APP_TYPE_NAME ), googlePlusFieldsMappings, permissions }, responder );
  }

  private static void checkUserToBeProper( BackendlessUser user ) throws BackendlessException
  {
    checkUserToBeProperForUpdate( user );

    if( user.getPassword() == null || user.getPassword().equals( "" ) )
      throw new IllegalArgumentException( ExceptionMessage.NULL_PASSWORD );
  }

  private static void checkUserToBeProperForUpdate( BackendlessUser user )
  {
    if( user == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_USER );
  }

  /**
   * Returns user ID of the logged in user or empty string if user is not logged in.
   *
   * @return user id, if the user is logged in; else empty string
   */
  public String loggedInUser()
  {
    return UserIdStorageFactory.instance().getStorage().get();
  }

  /**
   * Sets the properties of the given user to current one.
   *
   * @param user a user from which properties should be taken
   */
  public void setCurrentUser( BackendlessUser user )
  {
    if( currentUser == null )
      currentUser = user;
    else
      currentUser.setProperties( user.getProperties() );
  }

  private void handleUserLogin( BackendlessUser invokeResult, boolean stayLoggedIn )
  {
    String userToken = (String) invokeResult.getProperty( HeadersManager.HeadersEnum.USER_TOKEN_KEY.getHeader() );
    HeadersManager.getInstance().addHeader( HeadersManager.HeadersEnum.USER_TOKEN_KEY, userToken );

    currentUser = invokeResult;
    currentUser.removeProperty( HeadersManager.HeadersEnum.USER_TOKEN_KEY.getHeader() );

    if( stayLoggedIn )
    {
      UserTokenStorageFactory.instance().getStorage().set( userToken );
      UserIdStorageFactory.instance().getStorage().set( Backendless.UserService.CurrentUser().getUserId() );
    }
  }

  private AsyncCallback<BackendlessUser> getUserLoginAsyncHandler(
          final AsyncCallback<BackendlessUser> responder, final boolean stayLoggedIn )
  {
    return new AsyncCallback<BackendlessUser>()
    {
      @Override
      public void handleResponse( BackendlessUser response )
      {
        try
        {
          handleUserLogin( response, stayLoggedIn );
        }
        catch( Throwable e )
        {
          if( responder != null )
            responder.handleFault( new BackendlessFault( e ) );
        }

        if( responder != null )
          responder.handleResponse( currentUser );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        if( responder != null )
          responder.handleFault( fault );
      }
    };
  }

  /**
   * if user token has been saved from previous logins, the method checks if the user token is still valid;
   * if user token is not saved, but user logged in, the method checks if the user session is still valid
   *
   * @return true, if user token is valid or user is logged in, else false
   */
  public boolean isValidLogin()
  {
    String userToken = UserTokenStorageFactory.instance().getStorage().get();
    if( userToken != null && !userToken.equals( "" ) )
    {
      return Invoker.<Boolean>invokeSync( USER_MANAGER_SERVER_ALIAS, "isValidUserToken", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), userToken } );
    }
    else
    {
      return CurrentUser() != null;
    }
  }

  /**
   * if user token has been saved from previous logins, the method checks if the user token is still valid;
   * if user token is not saved, but user logged in, the method checks if the user session is still valid
   */
  public void isValidLogin( AsyncCallback<Boolean> responder )
  {
    String userToken = UserTokenStorageFactory.instance().getStorage().get();
    if( userToken != null && !userToken.equals( "" ) )
    {
      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "isValidUserToken", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), userToken }, responder );
    }
    else
    {
      responder.handleResponse( CurrentUser() != null );
    }
  }

  private boolean isLogoutFaultAllowed( String errorCode )
  {
    return errorCode.equals( "3064" ) || errorCode.equals( "3091" ) || errorCode.equals( "3090" ) || errorCode.equals( "3023" );
  }
}