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
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.property.AbstractProperty;
import com.backendless.property.UserProperty;
import weborb.types.Types;

import java.util.*;

public final class UserService
{
  final static String USER_MANAGER_SERVER_ALIAS = "com.backendless.services.users.UserService";
  private final static String PREFS_NAME = "backendless_pref";

  private final static BackendlessUser currentUser = new BackendlessUser();
  private final static Object currentUserLock = new Object();

  private static final UserService instance = new UserService();

  static UserService getInstance()
  {
    return instance;
  }

  private UserService()
  {
    Types.addClientClassMapping( "com.backendless.services.users.property.AbstractProperty", AbstractProperty.class );
    Types.addClientClassMapping( "com.backendless.services.users.property.UserProperty", UserProperty.class );
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
    user.putProperties( (HashMap<String, Object>) Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "register", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), user.getProperties() } ) );

    return user;
  }

  public void register( final BackendlessUser user, final AsyncCallback<BackendlessUser> responder )
  {
    try
    {
      checkUserToBeProper( user );

      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "register", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), user.getProperties() }, new AsyncCallback<HashMap<String, Object>>()
      {
        @Override
        public void handleResponse( HashMap<String, Object> response )
        {
          user.putProperties( response );

          if( responder != null )
            responder.handleResponse( user );
        }

        @Override
        public void handleFault( BackendlessFault fault )
        {
          if( responder != null )
            responder.handleFault( fault );
        }
      } );
    }
    catch( BackendlessException e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public BackendlessUser update( final BackendlessUser user ) throws BackendlessException
  {
    checkUserToBeProper( user );

    if( user.getUserId() != null && user.getUserId().equals( "" ) )
      throw new IllegalArgumentException( ExceptionMessage.WRONG_USER_ID );

    user.putProperties( (HashMap<String, Object>) Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "update", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), user.getProperties() } ) );

    return user;
  }

  public void update( final BackendlessUser user, final AsyncCallback<BackendlessUser> responder )
  {
    try
    {
      checkUserToBeProper( user );

      if( user.getUserId() != null && user.getUserId().equals( "" ) )
        throw new IllegalArgumentException( ExceptionMessage.WRONG_USER_ID );

      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "update", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), user.getProperties() }, new AsyncCallback<HashMap<String, Object>>()
      {
        @Override
        public void handleResponse( HashMap<String, Object> response )
        {
          user.putProperties( response );

          if( responder != null )
            responder.handleResponse( user );
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

  public BackendlessUser login( final String login, final String password ) throws BackendlessException
  {
    synchronized( currentUserLock )
    {
      if( !currentUser.getProperties().isEmpty() )
        logout();

      if( login == null || login.equals( "" ) )
        throw new IllegalArgumentException( ExceptionMessage.NULL_LOGIN );

      if( password == null || password.equals( "" ) )
        throw new IllegalArgumentException( ExceptionMessage.NULL_PASSWORD );

      handleUserLogin( (HashMap<String, Object>) Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "login", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), login, password } ) );

      return currentUser;
    }
  }

  public void login( final String login, final String password, final AsyncCallback<BackendlessUser> responder )
  {
    if( !currentUser.getProperties().isEmpty() )
      logout( new AsyncCallback<Void>()
      {
        @Override
        public void handleResponse( Void response )
        {
          login( login, password, responder );
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
            Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "login", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), login, password }, getUserLoginAsyncHandler( responder ) );
        }
      }
      catch( Throwable e )
      {
        if( responder != null )
          responder.handleFault( new BackendlessFault( e ) );
      }
  }

  public BackendlessUser loginWithFacebookSession( com.facebook.Session facebookSession,
                                                   com.facebook.model.GraphUser facebookUser )
  {
    return loginWithFacebookSession( facebookSession, facebookUser, new HashMap<String, String>() );
  }

  public BackendlessUser loginWithFacebookSession( com.facebook.Session facebookSession,
                                                   com.facebook.model.GraphUser facebookUser,
                                                   Map<String, String> facebookFieldsMappings )
  {
    synchronized( currentUserLock )
    {
      if( !currentUser.getProperties().isEmpty() )
        logout();

      handleUserLogin( getUserServiceAndroidExtra().loginWithFacebookSession( facebookSession, facebookUser, facebookFieldsMappings ) );

      return currentUser;
    }
  }

  public void loginWithFacebookSession( final com.facebook.Session facebookSession,
                                        final com.facebook.model.GraphUser facebookUser,
                                        final AsyncCallback<BackendlessUser> responder )
  {
    loginWithFacebookSession( facebookSession, facebookUser, null, responder );
  }

  public void loginWithFacebookSession( final com.facebook.Session facebookSession,
                                        final com.facebook.model.GraphUser facebookUser,
                                        final Map<String, String> facebookFieldsMappings,
                                        final AsyncCallback<BackendlessUser> responder )
  {
    if( !currentUser.getProperties().isEmpty() )
      logout( new AsyncCallback<Void>()
      {
        @Override
        public void handleResponse( Void response )
        {
          loginWithFacebookSession( facebookSession, facebookUser, facebookFieldsMappings, responder );
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
          getUserServiceAndroidExtra().loginWithFacebookSession( facebookSession, facebookUser, facebookFieldsMappings, getUserLoginAsyncHandler( responder ) );
        }
      }
      catch( Throwable e )
      {
        if( responder != null )
          responder.handleFault( new BackendlessFault( e ) );
      }
  }

  public void loginWithFacebookSdk( android.app.Activity context, final AsyncCallback<BackendlessUser> responder )
  {
    loginWithFacebookSdk( context, null, null, responder );
  }

  public void loginWithFacebookSdk( android.app.Activity context, final Map<String, String> facebookFieldsMappings,
                                    List<String> permissions, final AsyncCallback<BackendlessUser> responder )
  {
    getUserServiceAndroidExtra().loginWithFacebookSdk( context, facebookFieldsMappings, permissions, responder );
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
                                 Map<String, String> facebookFieldsMappings, List<String> permissions,
                                 final AsyncCallback<BackendlessUser> responder )
  {
    getUserServiceAndroidExtra().loginWithFacebook( context, webView, facebookFieldsMappings, permissions, getUserLoginAsyncHandler( responder ) );
  }

  public void loginWithTwitter( android.app.Activity context, AsyncCallback<BackendlessUser> responder )
  {
    loginWithTwitter( context, null, null, responder );
  }

  public void loginWithTwitter( android.app.Activity context, android.webkit.WebView webView,
                                Map<String, String> twitterFieldsMappings, AsyncCallback<BackendlessUser> responder )
  {
    getUserServiceAndroidExtra().loginWithTwitter( context, webView, twitterFieldsMappings, getUserLoginAsyncHandler( responder ) );
  }

  public void logout() throws BackendlessException
  {
    synchronized( currentUserLock )
    {
      Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "logout", new Object[] { Backendless.getApplicationId(), Backendless.getVersion() } );
      currentUser.clearProperties();
      HeadersManager.getInstance().removeHeader( HeadersManager.HeadersEnum.USER_TOKEN_KEY );
    }
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
          currentUser.clearProperties();
          try
          {
            HeadersManager.getInstance().removeHeader( HeadersManager.HeadersEnum.USER_TOKEN_KEY );
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
          if( responder != null )
            responder.handleFault( fault );
        }
      } );
    }
  }

  public <E extends BackendlessUser> BackendlessCollection<E> find(
          Class<E> userClass ) throws BackendlessException
  {
    return find( userClass, (BackendlessDataQuery) null );
  }

  public <E extends BackendlessUser> BackendlessCollection<E> find( Class<E> userClass,
                                                                    BackendlessDataQuery backendlessDataQuery ) throws BackendlessException
  {
    BackendlessCollection<HashMap> response = Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "find", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), "Users", backendlessDataQuery } );
    BackendlessCollection<E> result = convertResponse( response, userClass );
    return result;
  }

  public <E extends BackendlessUser> void find( Class<E> userClass,
                                                final AsyncCallback<BackendlessCollection<E>> responder )
  {
    find( userClass, null, responder );
  }

  public <E extends BackendlessUser> void find( final Class<E> userClass, BackendlessDataQuery backendlessDataQuery,
                                                final AsyncCallback<BackendlessCollection<E>> responder )
  {
    try
    {
      Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "find", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), "Users", backendlessDataQuery }, new AsyncCallback<BackendlessCollection<HashMap>>()
      {
        @Override
        public void handleResponse( BackendlessCollection<HashMap> response )
        {
          BackendlessCollection<E> result = convertResponse( response, userClass );
          if( responder != null )
            responder.handleResponse( result );
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

  private <E extends BackendlessUser> BackendlessCollection<E> convertResponse(
          BackendlessCollection<HashMap> backendlessCollection, Class<E> userClass )
  {
    List<E> data = new ArrayList<E>();
    for( HashMap e : backendlessCollection.getCurrentPage() )
    {
      try
      {
        E user = userClass.newInstance();
        user.setProperties( e );
        E t = (E) user;
        data.add( (E) user );
      }
      catch( Exception ignored )
      {
      }
    }
    return backendlessCollection.newInstance( data, userClass );
  }

  public BackendlessUser findByIdentity( String identity ) throws BackendlessException
  {
    if( identity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_IDENTITY );

    BackendlessUser result = new BackendlessUser();
    result.putProperties( (HashMap<String, Object>) Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "findByIdentity", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), identity } ) );

    return result;
  }

  public void findByIdentity( final String identity, final AsyncCallback<BackendlessUser> responder )
  {
    try
    {
      if( identity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_IDENTITY );

      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "findByIdentity", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), identity }, new AsyncCallback<HashMap<String, Object>>()
      {
        @Override
        public void handleResponse( HashMap<String, Object> response )
        {
          BackendlessUser result = new BackendlessUser();
          result.putProperties( response );

          if( responder != null )
            responder.handleResponse( result );
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

  private static void checkUserToBeProper( BackendlessUser user ) throws BackendlessException
  {
    if( user == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_USER );

    if( user.getPassword() == null || user.getPassword().equals( "" ) )
      throw new IllegalArgumentException( ExceptionMessage.NULL_PASSWORD );
  }

  private void handleUserLogin( Map<String, Object> invokeResult )
  {
    HeadersManager.getInstance().addHeader( HeadersManager.HeadersEnum.USER_TOKEN_KEY, (String) invokeResult.get( HeadersManager.HeadersEnum.USER_TOKEN_KEY.getHeader() ) );

    for( String key : invokeResult.keySet() )
      if( !key.equals( HeadersManager.HeadersEnum.USER_TOKEN_KEY.getHeader() ) )
        currentUser.setProperty( key, invokeResult.get( key ) );
  }

  private AsyncCallback<HashMap<String, Object>> getUserLoginAsyncHandler(
          final AsyncCallback<BackendlessUser> responder )
  {
    return new AsyncCallback<HashMap<String, Object>>()
    {
      @Override
      public void handleResponse( HashMap<String, Object> response )
      {
        try
        {
          HeadersManager.getInstance().addHeader( HeadersManager.HeadersEnum.USER_TOKEN_KEY, (String) response.get( HeadersManager.HeadersEnum.USER_TOKEN_KEY.getHeader() ) );
        }
        catch( Throwable e )
        {
          if( responder != null )
            responder.handleFault( new BackendlessFault( e ) );
        }

        for( String key : response.keySet() )
        {
          if( !key.equals( HeadersManager.HeadersEnum.USER_TOKEN_KEY.getHeader() ) )
            currentUser.setProperty( key, response.get( key ) );
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
}