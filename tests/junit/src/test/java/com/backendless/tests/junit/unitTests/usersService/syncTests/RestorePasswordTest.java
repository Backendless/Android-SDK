package com.backendless.tests.junit.unitTests.usersService.syncTests;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessException;
import com.backendless.tests.junit.Defaults;
import org.junit.Test;

public class RestorePasswordTest extends TestsFrame
{
  @Test
  public void testRestoreUserPassword() throws Exception
  {
    logTestStarted( "testRestoreUserPassword" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = getRandomLoggedInUser();
      Backendless.UserService.restorePassword( (String) user.getProperty( LOGIN_KEY ) );
      logTestFinished();
    }
    catch( Throwable t )
    {
      logTestFailed( t );
    }
  }

  @Test
  public void testRestoreUserPasswordWithWrongLogin() throws Exception
  {
    logTestStarted( "testRestoreUserPasswordWithWrongLogin" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = getRandomLoggedInUser();
      Backendless.UserService.restorePassword( "fake_login_" + user.getProperty( LOGIN_KEY ) );
      logTestFailed( "Server accepted wrong login." );
    }
    catch( BackendlessException t )
    {
      checkErrorCode( 3020, t );
      logTestFinished();
    }
  }
}
