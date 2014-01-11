package com.backendless.tests.junit.unitTests.usersService.syncTests;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.tests.junit.Defaults;
import org.junit.Assert;
import org.junit.Test;

public class LoginTest extends TestsFrame
{
  @Test
  public void testLoginWithNullLogin() throws Exception
  {
    logTestStarted( "testLoginWithNullLogin" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      Backendless.UserService.login( null, getRandomRegisteredUser().getPassword() );
      Backendless.UserService.logout();
      logTestFailed( "UserService accepted null login" );
    }
    catch( Exception t )
    {
      checkErrorCode( ExceptionMessage.NULL_LOGIN, t );
      logTestFinished();
    }
  }

  @Test
  public void testLoginWithNullPassword() throws Exception
  {
    logTestStarted( "testLoginWithNullPassword" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      Backendless.UserService.login( (String) getRandomRegisteredUser().getProperty( LOGIN_KEY ), null );
      logTestFailed( "UserService accepted null password" );
    }
    catch( Exception t )
    {
      checkErrorCode( ExceptionMessage.NULL_PASSWORD, t );
      logTestFinished();
    }
  }

  @Test
  public void testLoginWithWrongCredentials() throws Exception
  {
    logTestStarted( "testLoginWithWrongCredentials" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = getRandomRegisteredUser();
      Backendless.UserService.login( user.getProperty( LOGIN_KEY ) + "foobar", user.getPassword() + "foobar" );
      logTestFailed( "Server accepted wrong credentials" );
    }
    catch( Exception t )
    {
      checkErrorCode( 3003, t );
      logTestFinished();
    }
  }

  @Test
  public void testLoginWithDisabledAppLogin() throws Exception
  {
    logTestStarted( "testLoginWithDisabledAppLogin" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, "v2_nata" );
      BackendlessUser user = getRandomRegisteredUser();
      Backendless.UserService.login( (String) user.getProperty( LOGIN_KEY ), user.getPassword() );
      logTestFailed( "Server accepted login" );
    }
    catch( Exception t )
    {
      checkErrorCode( 3034, t );
      logTestFinished();
    }
  }

  @Test
  public void testLoginWithProperCredentials() throws Exception
  {
    logTestStarted( "testLoginWithProperCredentials" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = getRandomRegisteredUser();
      Backendless.UserService.login( (String) user.getProperty( LOGIN_KEY ), user.getPassword() );
      Backendless.UserService.describeUserClass();
    }
    catch( Throwable t )
    {
      logTestFailed( t );
    }

    logTestFinished();
  }

  @Test
  public void testLoginWithExternalAuth() throws Exception
  {
    logTestStarted( "testLoginWithExternalAuth" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, "v_nata6" );
      BackendlessUser user = getRandomRegisteredUser();
      Backendless.UserService.login( (String) user.getProperty( LOGIN_KEY ), "password123" );

      logTestFailed( "Server accepted registration for an invalid external Auth" );
    }
    catch( Exception t )
    {
      checkErrorCode( 3052, t );
      logTestFinished();
    }
  }

  @Test
  public void testLoginWithoutFailedLoginsLock() throws Throwable
  {
    logTestStarted( "testLoginWithoutFailedLoginsLock" );

    Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, "v3_nata" );

    BackendlessUser user = getRandomRegisteredUser();

    try
    {
      Backendless.UserService.login( (String) user.getProperty( LOGIN_KEY ), user.getPassword() + "foo" );
    }
    catch( Exception t )
    {
    }

    try
    {
      Backendless.UserService.login( (String) user.getProperty( LOGIN_KEY ), user.getPassword() );
    }
    catch( Exception t )
    {
      logTestFailed( t );
    }
  }

  @Test
  public void testLoginWithFailedLoginsLock() throws Throwable
  {
    logTestStarted( "testLoginWithoutFailedLoginsLock" );

    Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, "v1" );

    BackendlessUser user = getRandomRegisteredUser();

    try
    {
      Backendless.UserService.login( (String) user.getProperty( LOGIN_KEY ), user.getPassword() + "foo" );
    }
    catch( Exception t )
    {
    }

    try
    {
      Backendless.UserService.login( (String) user.getProperty( LOGIN_KEY ), user.getPassword() );
      logTestFailed( "Server accepted login after lock" );
    }
    catch( Exception t )
    {
    }
  }

  @Test
  public void testLoginWithUserFromAnotherVersion() throws Exception
  {
    logTestStarted( "testLoginWithUserFromAnotherVersion" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = getRandomRegisteredUser();

      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, "v3_nata" );
      Backendless.UserService.login( (String) user.getProperty( LOGIN_KEY ), user.getPassword() );

      logTestFailed( "Server accepted login for the user from another version" );
    }
    catch( Exception t )
    {
      checkErrorCode( 3003, t );
      logTestFinished();
    }
  }

  @Test
  public void testCurrentUserAfterLogin() throws Exception
  {
    logTestStarted( "testCurrentUserAfterLogin" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser notRegisteredUseruser = getRandomNotRegisteredUser();
      String propertyKey = "property_key" + Math.abs( random.nextInt() );
      String propertyValue = "property_value" + Math.abs( random.nextInt() );
      notRegisteredUseruser.setProperty( propertyKey, propertyValue );

      BackendlessUser user = Backendless.UserService.register( notRegisteredUseruser );
      usedProperties.add( propertyKey );

      Backendless.UserService.login( (String) user.getProperty( LOGIN_KEY ), user.getPassword() );

      Assert.assertNotNull( "Current user was null", Backendless.UserService.CurrentUser() );

      for( String key : user.getProperties().keySet() )
      {
        if( key.equals( "password" ) )
        {
          continue;
        }

        Assert.assertTrue( "Current user didn`t contain expected property " + key, Backendless.UserService.CurrentUser().getProperties().containsKey( key ) );
        Assert.assertEquals( "UserService.register changed property " + key, user.getProperty( key ), Backendless.UserService.CurrentUser().getProperty( key ) );
      }
    }
    catch( Throwable t )
    {
      logTestFailed( t );
    }

    logTestFinished();
  }
}
