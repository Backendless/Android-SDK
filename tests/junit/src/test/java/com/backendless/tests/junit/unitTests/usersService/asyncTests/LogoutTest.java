package com.backendless.tests.junit.unitTests.usersService.asyncTests;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.tests.junit.Defaults;
import org.junit.Assert;
import org.junit.Test;

public class LogoutTest extends TestsFrame
{
  @Test
  public void testUserLogout()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        getRandomLoggedInUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( final BackendlessUser user )
          {
            Backendless.UserService.logout( new ResponseCallback<Void>()
            {
              @Override
              public void handleResponse( Void response )
              {
                try
                {
                  Assert.assertNull( "Current user was not empty", Backendless.UserService.CurrentUser() );
                  countdownLogTestFinished();
                }
                catch( Throwable t )
                {
                  failCountDownWith( t );
                }
              }
            } );
          }
        } );
      }
    } );
  }
}
