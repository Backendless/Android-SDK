package com.backendless.tests.junit.unitTests.usersService.syncTests;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessException;
import com.backendless.tests.junit.Defaults;
import org.junit.Assert;
import org.junit.Test;

public class LogoutTest extends TestsFrame
{
  @Test
  public void testUserLogout() throws Throwable
  {
    logTestStarted( "testUserLogout" );

    BackendlessUser user = null;

    Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
    getRandomLoggedInUser();
    Backendless.UserService.logout();

    Assert.assertNull( "Current user was not empty", Backendless.UserService.CurrentUser() );
  }
}
