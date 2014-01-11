package com.backendless.helpers;

import android.app.Activity;
import com.facebook.NonCachingTokenCachingStrategy;
import com.facebook.Session;
import com.facebook.SessionState;

import java.util.List;

public class NonCachingTokenFacebookSession
{
  public static Session openActiveSession( Activity activity, List<String> permissions,
                                           Session.StatusCallback callback )
  {
    return openActiveSession( activity, permissions, new Session.OpenRequest( activity ).setCallback( callback ) );
  }

  private static Session openActiveSession( final Activity context, final List<String> permissions,
                                            Session.OpenRequest openRequest )
  {
    Session session = new Session.Builder( context ).setTokenCachingStrategy( new NonCachingTokenCachingStrategy() ).build();
    session.addCallback( new Session.StatusCallback()
    {
      @Override
      public void call( Session session, SessionState sessionState, Exception e )
      {
        if( sessionState == SessionState.OPENED )
          session.requestNewReadPermissions( new Session.NewPermissionsRequest( context, permissions ) );
      }
    } );
    Session.setActiveSession( session );
    session.openForRead( openRequest );

    return session;
  }
}
