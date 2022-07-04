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
import com.backendless.commons.user.UserStatusEnum;
import com.backendless.core.responder.AdaptingResponder;
import com.backendless.core.responder.policy.BackendlessUserAdaptingPolicy;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.BackendlessSerializer;
import com.backendless.persistence.DataQueryBuilder;
import com.backendless.persistence.local.UserIdStorageFactory;
import com.backendless.persistence.local.UserTokenStorageFactory;
import com.backendless.property.AbstractProperty;
import com.backendless.property.UserProperty;
import com.backendless.rt.RTClientFactory;
import com.backendless.utils.ResponderHelper;
import weborb.types.Types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class UserService
{
  private static final String USER_STATUS_COLUMN = "userStatus";
  final static String USER_MANAGER_SERVER_ALIAS = "com.backendless.services.users.UserService";

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
    if( currentUser != null && currentUser.isEmpty() )
      return null;

    return currentUser;
  }

  public BackendlessUser CurrentUser( boolean reload )
  {
    if( reload )
    {
      if( currentUser != null && currentUser.getObjectId() != null && !currentUser.getObjectId().isEmpty() )
      {
        currentUser = Backendless.UserService.findById( currentUser.getObjectId() );
        return currentUser;
      }
      else
      {
        return null;
      }
    }
    else
    {
      return CurrentUser();
    }
  }

  public void CurrentUser( boolean reload, final AsyncCallback<BackendlessUser> responder )
  {
    if( reload )
    {
      if( currentUser != null && currentUser.getObjectId() != null && !currentUser.getObjectId().isEmpty() )
      {
        Backendless.UserService.findById( currentUser.getObjectId(), new AsyncCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser response )
          {
            currentUser = response;
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
      else
      {
        responder.handleResponse( null );
      }
    }
    else
    {
      responder.handleResponse( CurrentUser() );
    }
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
    BackendlessUser userToReturn = Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "register", new Object[] { user.getProperties() }, new AdaptingResponder<>( BackendlessUser.class, new BackendlessUserAdaptingPolicy() ) );
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

      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "register", new Object[] { user.getProperties() }, new AsyncCallback<BackendlessUser>()
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
      }, new AdaptingResponder<>( BackendlessUser.class, new BackendlessUserAdaptingPolicy() ) );
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

    BackendlessUser userToReturn = Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "update", new Object[] { user.getProperties() }, new AdaptingResponder<>( BackendlessUser.class, new BackendlessUserAdaptingPolicy() ) );
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

      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "update", new Object[] { user.getProperties() }, new AsyncCallback<BackendlessUser>()
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
      }, new AdaptingResponder<>( BackendlessUser.class, new BackendlessUserAdaptingPolicy() ) );
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
      if( currentUser != null && !currentUser.isEmpty() )
        logout();

      if( login == null || login.equals( "" ) )
        throw new IllegalArgumentException( ExceptionMessage.NULL_LOGIN );

      if( password == null || password.equals( "" ) )
        throw new IllegalArgumentException( ExceptionMessage.NULL_PASSWORD );

      handleUserLogin( Invoker.<BackendlessUser>invokeSync( USER_MANAGER_SERVER_ALIAS, "login", new Object[] { login, password }, new AdaptingResponder<>( BackendlessUser.class, new BackendlessUserAdaptingPolicy() ) ), stayLoggedIn );

      return currentUser;
    }
  }

  public BackendlessUser login( final String objectId ) throws BackendlessException
  {
    return login( objectId, false );
  }

  public BackendlessUser login( final String objectId,
                                boolean stayLoggedIn ) throws BackendlessException
  {
    synchronized( currentUserLock )
    {
      if( currentUser != null && !currentUser.isEmpty() )
        logout();

      if( objectId == null || objectId.equals( "" ) )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ID);


      handleUserLogin( Invoker.<BackendlessUser>invokeSync( USER_MANAGER_SERVER_ALIAS, "login", new Object[] { objectId }, new AdaptingResponder( BackendlessUser.class, new BackendlessUserAdaptingPolicy() ) ), stayLoggedIn );

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
    if( currentUser != null && !currentUser.isEmpty() )
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
            Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "login", new Object[] { login, password }, getUserLoginAsyncHandler( responder, stayLoggedIn ) , new AdaptingResponder<>( BackendlessUser.class, new BackendlessUserAdaptingPolicy() ) );
        }
      }
      catch( Throwable e )
      {
        if( responder != null )
          responder.handleFault( new BackendlessFault( e ) );
      }
  }

  public void login( final String objectId, final AsyncCallback<BackendlessUser> responder )
  {
    login( objectId, responder, false );
  }

  public void login( final String objectId, final AsyncCallback<BackendlessUser> responder,
                     final boolean stayLoggedIn )
  {
    if( currentUser != null && !currentUser.isEmpty() )
      logout( new AsyncCallback<Void>()
      {
        @Override
        public void handleResponse( Void response )
        {
          login( objectId, responder, stayLoggedIn );
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
          if( objectId == null || objectId.equals( "" ) )
            throw new IllegalArgumentException( ExceptionMessage.NULL_LOGIN );
          else
            Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "login", new Object[] { objectId }, getUserLoginAsyncHandler( responder, stayLoggedIn ) , new AdaptingResponder( BackendlessUser.class, new BackendlessUserAdaptingPolicy() ) );
        }
      }
      catch( Throwable e )
      {
        if( responder != null )
          responder.handleFault( new BackendlessFault( e ) );
      }
  }

  public void loginWithOAuth2(
          String authProviderCode, String accessToken, final Map<String, String> fieldsMappings,
          final AsyncCallback<BackendlessUser> responder, boolean stayLoggedIn )
  {
    AsyncCallback<BackendlessUser> internalResponder = getUserLoginAsyncHandler(responder, stayLoggedIn);
    getUserServiceAndroidExtra().loginWithOAuth2( authProviderCode, accessToken, null, fieldsMappings, internalResponder );
  }

  public void loginWithOAuth2(
          String authProviderCode, String accessToken, BackendlessUser guestUser, final Map<String, String> fieldsMappings,
          final AsyncCallback<BackendlessUser> responder, boolean stayLoggedIn )
  {
    AsyncCallback<BackendlessUser> internalResponder = getUserLoginAsyncHandler(responder, stayLoggedIn);
    getUserServiceAndroidExtra().loginWithOAuth2( authProviderCode, accessToken, guestUser, fieldsMappings, internalResponder );
  }

  public void loginWithOAuth1(
          String authProviderCode, String authToken, String authTokenSecret, Map<String, String> fieldsMappings,
          AsyncCallback<BackendlessUser> responder, boolean stayLoggedIn )
  {
    AsyncCallback<BackendlessUser> internalResponder = getUserLoginAsyncHandler( responder, stayLoggedIn );
    getUserServiceAndroidExtra().loginWithOAuth1( authProviderCode, authToken, null, authTokenSecret, fieldsMappings, internalResponder );
  }

  public void loginWithOAuth1(
          String authProviderCode, String authToken, String authTokenSecret, BackendlessUser guestUser, Map<String, String> fieldsMappings,
          AsyncCallback<BackendlessUser> responder, boolean stayLoggedIn )
  {
    AsyncCallback<BackendlessUser> internalResponder = getUserLoginAsyncHandler( responder, stayLoggedIn );
    getUserServiceAndroidExtra().loginWithOAuth1( authProviderCode, authToken, guestUser, authTokenSecret, fieldsMappings, internalResponder );
  }

  public void logout() throws BackendlessException
  {
    synchronized( currentUserLock )
    {
      try
      {
        Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "logout", new Object[] { } );
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
    RTClientFactory.get().userLoggedOut();
  }

  public void logout( final AsyncCallback<Void> responder )
  {
    synchronized( currentUserLock )
    {
      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "logout", new Object[] { }, new AsyncCallback<Void>()
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

  public void enableUser( String userId )
  {
    changeUserStatus( userId, UserStatusEnum.ENABLED );
  }

  public void enableUser( String userId, AsyncCallback<Void> responder )
  {
    changeUserStatus( userId, UserStatusEnum.ENABLED, responder );
  }

  public void disableUser( String userId )
  {
    changeUserStatus( userId, UserStatusEnum.DISABLED );
  }

  public void disableUser( String userId, AsyncCallback<Void> responder )
  {
    changeUserStatus( userId, UserStatusEnum.DISABLED, responder );
  }

  public void restorePassword( String identity ) throws BackendlessException
  {
    if( identity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_IDENTITY );

    Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "restorePassword", new Object[] { identity } );
  }

  public void restorePassword( final String identity, final AsyncCallback<Void> responder )
  {
    try
    {
      if( identity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_IDENTITY );

      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "restorePassword", new Object[] { identity }, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public boolean verifyPassword( String password )
  {
    if( password == null || password.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_PASSWORD );

    String userToken = UserTokenStorageFactory.instance().getStorage().get();
    if( userToken == null || userToken.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NO_EXISTING_USER_TOKEN );

    HeadersManager.getInstance().addHeader( HeadersManager.HeadersEnum.USER_TOKEN_KEY, userToken );
    return Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "verifyPassword", new Object[] { password } );
  }

  public String createEmailConfirmationURL( String identity )
  {
    if( identity == null || identity.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_IDENTITY );

    return Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "createEmailConfirmationURL", new Object[]{ identity } );
  }

  public void createEmailConfirmationURL( String identity, AsyncCallback<String> responder )
  {
    try
    {
      if( identity == null || identity.isEmpty() )
        throw new IllegalArgumentException( ExceptionMessage.NULL_IDENTITY );

      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "createEmailConfirmationURL", new Object[]{ identity }, responder );
    }
    catch ( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public void resendEmailConfirmation( String identity ) throws BackendlessException
  {
    if( identity == null || identity.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_IDENTITY );

    Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "resendEmailConfirmation", new Object[]{ identity } );
  }

  public void resendEmailConfirmation( String identity, AsyncCallback<Void> responder )
  {
    try
    {
      if( identity == null || identity.isEmpty() )
        throw new IllegalArgumentException( ExceptionMessage.NULL_IDENTITY );

      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "resendEmailConfirmation", new Object[]{ identity }, responder );
    }
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

    return Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "findById", new Object[] { id, new ArrayList<>() } );
  }

  public void findById( final String id, final AsyncCallback<BackendlessUser> responder )
  {
    try
    {
      if( id == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_IDENTITY );

      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "findById", new Object[] { id, new ArrayList<>() }, new AsyncCallback<BackendlessUser>()
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

    Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "assignRole", new Object[] { identity, roleName } );
  }

  public void assignRole( final String identity, final String roleName, final AsyncCallback<Void> responder )
  {
    try
    {
      if( identity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_IDENTITY );

      if( roleName == null || roleName.equals( "" ) )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ROLE_NAME );

      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "assignRole", new Object[] { identity, roleName }, responder );
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

    Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "unassignRole", new Object[] { identity, roleName } );
  }

  public void unassignRole( final String identity, final String roleName, final AsyncCallback<Void> responder )
  {
    try
    {
      if( identity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_IDENTITY );

      if( roleName == null || roleName.equals( "" ) )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ROLE_NAME );

      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "unassignRole", new Object[] { identity, roleName }, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public List<String> getUserRoles() throws BackendlessException
  {
    return Arrays.asList( (String[]) Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "getUserRoles", new Object[] { } ) );
  }

  public List<String> getUserRoles( String userId )
  {
    if( userId == null || userId.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_IDENTITY );

    return Arrays.asList( (String[]) Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "getUserRoles", new Object[] { userId } ) );
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
      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "getUserRoles", new Object[] { }, callback );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public void getUserRoles( String userId, final AsyncCallback<List<String>> responder )
  {
    try
    {
      if( userId == null || userId.isEmpty() )
        throw new IllegalArgumentException( ExceptionMessage.NULL_IDENTITY );

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
      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "getUserRoles", new Object[] { userId }, callback );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public List<BackendlessUser> findByRole( String roleName, boolean loadRoles, DataQueryBuilder queryBuilder )
  {
    if( roleName != null && roleName.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ROLE_NAME );

    return Arrays.asList( (BackendlessUser[]) Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "findByRole", new Object[] { roleName, loadRoles, queryBuilder.build() } ) );
  }

  public void findByRole( String roleName, boolean loadRoles, DataQueryBuilder queryBuilder, final AsyncCallback<List<BackendlessUser>> responder )
  {
    try
    {
      if( roleName != null && roleName.isEmpty() )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ROLE_NAME );

      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "findByRole", new Object[] { roleName, loadRoles, queryBuilder.build() },
                           responder, ResponderHelper.getCollectionAdaptingResponder( BackendlessUser.class ) );
    }
    catch ( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public List<UserProperty> describeUserClass() throws BackendlessException
  {
    UserProperty[] response = Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "describeUserClass", new Object[] { } );

    return Arrays.asList( response );
  }

  public void describeUserClass( final AsyncCallback<List<UserProperty>> responder )
  {
    Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "describeUserClass", new Object[] { }, new AsyncCallback<UserProperty[]>()
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

  public BackendlessUser loginAsGuest()
  {
    return loginAsGuest( false );
  }

  public BackendlessUser loginAsGuest( boolean stayLoggedIn )
  {
    synchronized( currentUserLock )
    {
      handleUserLogin( Invoker.<BackendlessUser>invokeSync( USER_MANAGER_SERVER_ALIAS, "loginAsGuest", new Object[] {  }, new AdaptingResponder<>( BackendlessUser.class, new BackendlessUserAdaptingPolicy() ) ), stayLoggedIn );

      return currentUser;
    }
  }

  public void loginAsGuest( final AsyncCallback<BackendlessUser> responder )
  {
    loginAsGuest( responder,false );
  }

  public void loginAsGuest( final AsyncCallback<BackendlessUser> responder, final boolean stayLoggedIn )
  {
    if( currentUser != null && !currentUser.isEmpty() )
      logout( new AsyncCallback<Void>()
      {
        @Override
        public void handleResponse( Void response )
        {
          loginAsGuest( responder, stayLoggedIn );
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
          Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "loginAsGuest", new Object[] { }, getUserLoginAsyncHandler( responder, stayLoggedIn ) , new AdaptingResponder<>( BackendlessUser.class, new BackendlessUserAdaptingPolicy() ) );
        }
      }
      catch( Throwable e )
      {
        if( responder != null )
          responder.handleFault( new BackendlessFault( e ) );
      }
  }

  public void getAuthorizationUrlLink( String authProviderCode, Map<String, String> fieldsMappings,
                                                        List<String> scope,
                                                        AsyncCallback<String> responder ) throws BackendlessException
  {
    if( fieldsMappings == null )
      fieldsMappings = new HashMap<>();

    if( scope == null )
      scope = new ArrayList<>();

    String origin = null;
    String deviceType = HeadersManager.getInstance().getHeader( HeadersManager.HeadersEnum.APP_TYPE_NAME );

    Invoker.invokeAsync(
            USER_MANAGER_SERVER_ALIAS,
            "getAuthorizationUrlLink",
            new Object[] { authProviderCode, origin, deviceType, fieldsMappings, scope },
            responder
    );
  }

  public void getAuthorizationUrlLink( String authProviderCode,
                                       String callbackUrlDomain,
                                       Map<String, String> fieldsMappings,
                                       List<String> scope,
                                       AsyncCallback<String> responder ) throws BackendlessException
  {
    if( fieldsMappings == null )
      fieldsMappings = new HashMap<>();

    if( scope == null )
      scope = new ArrayList<>();

    String origin = null;
    String deviceType = HeadersManager.getInstance().getHeader( HeadersManager.HeadersEnum.APP_TYPE_NAME );

    Invoker.invokeAsync(
            USER_MANAGER_SERVER_ALIAS,
            "getAuthorizationUrlLink",
            new Object[] { authProviderCode, origin, deviceType, fieldsMappings, scope, callbackUrlDomain },
            responder
    );
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
    else
    {
      UserTokenStorageFactory.instance().getStorage().set( "" );
      UserIdStorageFactory.instance().getStorage().set( "" );
    }

    RTClientFactory.get().userLoggedIn( userToken );
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
      return Invoker.<Boolean>invokeSync( USER_MANAGER_SERVER_ALIAS, "isValidUserToken", new Object[] { userToken } );
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
      Invoker.invokeAsync( USER_MANAGER_SERVER_ALIAS, "isValidUserToken", new Object[] { userToken }, responder );
    }
    else
    {
      responder.handleResponse( CurrentUser() != null );
    }
  }

  private void changeUserStatus( String userId, UserStatusEnum newUserStatus ) throws BackendlessException
  {
    synchronized( currentUserLock )
    {
      Invoker.invokeSync( USER_MANAGER_SERVER_ALIAS, "changeUserStatus", new Object[] { userId, newUserStatus } );

      if( currentUser != null && !currentUser.isEmpty() && currentUser.getObjectId().equals( userId ) )
        currentUser.setProperty( USER_STATUS_COLUMN, newUserStatus.toString() );
    }
  }

  private void changeUserStatus( final String userId, final UserStatusEnum newUserStatus, final AsyncCallback<Void> responder )
  {
    Invoker.invokeAsync(
            USER_MANAGER_SERVER_ALIAS,
            "changeUserStatus",
            new Object[] { userId, newUserStatus },
            new AsyncCallback<Void>()
            {
              @Override
              public void handleResponse( Void response )
              {
                if( currentUser != null && !currentUser.isEmpty() && currentUser.getObjectId().equals( userId ) )
                  currentUser.setProperty( USER_STATUS_COLUMN, newUserStatus.toString() );

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

  private boolean isLogoutFaultAllowed( String errorCode )
  {
    return errorCode.equals( "3064" ) || errorCode.equals( "3091" ) || errorCode.equals( "3090" ) || errorCode.equals( "3023" );
  }
}