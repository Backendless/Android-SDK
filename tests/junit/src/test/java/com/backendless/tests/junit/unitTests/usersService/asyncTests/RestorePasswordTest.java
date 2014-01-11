package com.backendless.tests.junit.unitTests.usersService.asyncTests;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.tests.junit.Defaults;
import org.junit.Test;

public class RestorePasswordTest extends TestsFrame
{
  @Test
  public void testRestoreUserPassword() throws Exception
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
          public void handleResponse( BackendlessUser user )
          {
            Backendless.UserService.restorePassword( (String) user.getProperty( LOGIN_KEY ), new ResponseCallback<Void>() );
          }
        } );
      }
    } );
  }

  @Test
  public void testRestoreUserPasswordWithWrongLogin() throws Exception
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
          public void handleResponse( BackendlessUser user )
          {
            Backendless.UserService.restorePassword( "fake_login_" + user.getProperty( LOGIN_KEY ), new AsyncCallback<Void>()
            {
              @Override
              public void handleResponse( Void response )
              {
                failCountDownWith( "Server accepted wrong login." );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( 3020, fault );
              }
            } );
          }
        } );
      }
    } );
  }
}
