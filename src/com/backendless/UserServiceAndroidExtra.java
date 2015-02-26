package com.backendless;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.helpers.NonCachingTokenFacebookSession;
import com.backendless.social.AbstractSocialLoginStrategy;
import com.facebook.SessionState;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.*;

class UserServiceAndroidExtra
{
  private static final UserServiceAndroidExtra instance = new UserServiceAndroidExtra();

  static UserServiceAndroidExtra getInstance()
  {
    return instance;
  }

  private UserServiceAndroidExtra()
  {}

  BackendlessUser loginWithFacebookSession( com.facebook.Session facebookSession,
                                                    com.facebook.model.GraphUser facebookUser,
                                                    Map<String, String> facebookFieldsMappings )
  {
    FacebookBundle facebookBundle = getFacebookRequestBundle( facebookSession, facebookUser );
    Object[] requestData = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), facebookBundle.socialUserId, facebookBundle.accessToken, facebookBundle.expirationDate, facebookBundle.permissions, facebookFieldsMappings };
    BackendlessUser invokeResult = Invoker.invokeSync( UserService.USER_MANAGER_SERVER_ALIAS, "loginWithFacebook", requestData );

    HeadersManager.getInstance().addHeader( HeadersManager.HeadersEnum.USER_TOKEN_KEY, (String) invokeResult.getProperty( HeadersManager.HeadersEnum.USER_TOKEN_KEY.getHeader() ) );

    return invokeResult;
  }

  void loginWithFacebookSession( com.facebook.Session facebookSession, com.facebook.model.GraphUser facebookUser,
                                 Map<String, String> facebookFieldsMappings,
                                 AsyncCallback<BackendlessUser> responder )
  {

    FacebookBundle facebookBundle = getFacebookRequestBundle( facebookSession, facebookUser );
    Object[] requestData = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), facebookBundle.socialUserId, facebookBundle.accessToken, facebookBundle.expirationDate, facebookBundle.permissions, facebookFieldsMappings };
    Invoker.invokeAsync( UserService.USER_MANAGER_SERVER_ALIAS, "loginWithFacebook", requestData, responder );
  }

  void loginWithFacebookSdk( android.app.Activity context, final Map<String, String> facebookFieldsMappings,
                             List<String> permissions, final AsyncCallback<BackendlessUser> responder )
  {
    NonCachingTokenFacebookSession.openActiveSession( context, permissions, new com.facebook.Session.StatusCallback()
    {
      @Override
      public void call( final com.facebook.Session session, com.facebook.SessionState sessionState, Exception e )
      {
        if( sessionState == SessionState.OPENED )
          com.facebook.Request.executeMeRequestAsync( session, new com.facebook.Request.GraphUserCallback()
          {
            @Override
            public void onCompleted( com.facebook.model.GraphUser graphUser, com.facebook.Response response )
            {
              if( graphUser != null )
                Backendless.UserService.loginWithFacebookSession( session, graphUser, facebookFieldsMappings, responder );
              else
                responder.handleFault( new BackendlessFault( ExceptionMessage.NULL_GRAPH_USER ) );
            }
          } );
      }
    } );
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
            result.setProperty( key, response.get( key ) );
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

  private static com.facebook.AccessToken getAccessTokenFromSession( com.facebook.Session session ) throws Exception
  {
    Field f = session.getClass().getDeclaredField( "tokenInfo" );
    f.setAccessible( true );

    return (com.facebook.AccessToken) f.get( session );
  }

  private static void checkTokenCachingStrategy( com.facebook.Session facebookSession ) throws Exception
  {
    if( !getTokenCachingStrategyFromSession( facebookSession ).getClass().isAssignableFrom( com.facebook.NonCachingTokenCachingStrategy.class ) )
      throw new IllegalStateException( ExceptionMessage.WRONG_FACEBOOK_CACHING_STRATEGY );
  }

  private static com.facebook.TokenCachingStrategy getTokenCachingStrategyFromSession(
          com.facebook.Session session ) throws Exception
  {
    Field f = session.getClass().getDeclaredField( "tokenCachingStrategy" );
    f.setAccessible( true );

    return (com.facebook.TokenCachingStrategy) f.get( session );
  }

  private static FacebookBundle getFacebookRequestBundle( com.facebook.Session facebookSession,
                                                          com.facebook.model.GraphUser facebookUser )
  {
    return new FacebookBundle( facebookSession, facebookUser );
  }

  private static class FacebookBundle
  {
    String accessToken;
    Date expirationDate;
    List<String> permissions;
    String socialUserId;

    FacebookBundle( com.facebook.Session facebookSession, com.facebook.model.GraphUser facebookUser )
    {
      if( facebookSession == null || !facebookSession.isOpened() )
        throw new IllegalArgumentException( ExceptionMessage.NULL_FACEBOOK_SESSION );

      if( facebookUser == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_FACEBOOK_GRAPH_USER );

      com.facebook.AccessToken sessionAccessToken;
      try
      {
        checkTokenCachingStrategy( facebookSession );
        sessionAccessToken = getAccessTokenFromSession( facebookSession );
      }
      catch( IllegalStateException e )
      {
        throw e;
      }
      catch( Exception e )
      {
        throw new IllegalStateException( ExceptionMessage.FACEBOOK_SESSION_NO_ACCESS );
      }

      accessToken = sessionAccessToken.getToken();
      expirationDate = sessionAccessToken.getExpires();
      permissions = sessionAccessToken.getPermissions();
      socialUserId = facebookUser.getId();
    }
  }
}
