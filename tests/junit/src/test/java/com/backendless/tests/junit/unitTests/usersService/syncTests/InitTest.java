package com.backendless.tests.junit.unitTests.usersService.syncTests;

import com.backendless.Backendless;
import com.backendless.tests.junit.Defaults;
import org.junit.Assert;
import org.junit.Test;

public class InitTest extends TestsFrame
{
  @Test
  public void testUserServiceInitialized() throws Exception
  {
    logTestStarted( "testUserServiceInitialized" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
    }
    catch( Throwable t )
    {
      logTestFailed( t );
    }

    logTestFinished();
  }

  @Test
  public void testCurrentUserIsEmpty() throws Exception
  {
    logTestStarted( "testCurrentUserIsNull" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      Assert.assertNull( "Current user was not empty", Backendless.UserService.CurrentUser() );
    }
    catch( Throwable t )
    {
      logTestFailed( t );
    }

    logTestFinished();
  }
}
