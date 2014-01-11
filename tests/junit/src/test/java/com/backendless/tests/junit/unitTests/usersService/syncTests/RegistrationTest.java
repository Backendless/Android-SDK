package com.backendless.tests.junit.unitTests.usersService.syncTests;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.tests.junit.Defaults;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class RegistrationTest extends TestsFrame
{
  @Test
  public void testRegisterNewUserFromNull() throws Exception
  {
    logTestStarted( "testRegisterNewUserFromNull" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      Backendless.UserService.register( null );
      logTestFailed( "UserService accepted a null user" );
    }
    catch( Exception t )
    {
      checkErrorCode( ExceptionMessage.NULL_USER, t );
      logTestFinished();
    }
  }

  @Test
  public void testRegisterNewUserFromEmptyUser() throws Exception
  {
    logTestStarted( "testRegisterNewUserFromEmptyUser" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      Backendless.UserService.register( new BackendlessUser() );
      logTestFailed( "UserService accepted a null user" );
    }
    catch( Exception t )
    {
      checkErrorCode( ExceptionMessage.NULL_PASSWORD, t );
      logTestFinished();
    }
  }

  @Test
  public void testRegisterNewUserWithPartialFields() throws Exception
  {
    logTestStarted( "testRegisterNewUserWithPartialFields" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, "v5" );

      BackendlessUser partialUser = new BackendlessUser();
      String login = "bot" + random.nextInt();
      partialUser.setProperty( LOGIN_KEY, login );
      partialUser.setProperty( EMAIL_KEY, login + "@gmail.com" );
      partialUser.setPassword( "somepass" );
      Backendless.UserService.register( partialUser );
      logTestFailed( "UserService accepted a user without required fields" );
    }
    catch( Exception t )
    {
      checkErrorCode( 3012, t );
      logTestFinished();
    }
  }

  @Test
  public void testRegisterNewUserWithNulls() throws Exception
  {
    logTestStarted( "testRegisterNewUserWithNulls" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = new BackendlessUser();
      user.setProperty( LOGIN_KEY, null );
      user.setProperty( EMAIL_KEY, null );
      user.setPassword( null );
      user.setProperty( null, "foo" );
      user.setProperty( "foo", null );

      Backendless.UserService.register( user );
      logTestFailed( "UserService accepted null values" );
    }
    catch( Throwable t )
    {
      logTestFinished();
    }
  }

  @Test
  public void testRegisterNewUserWithNullPropertyValue() throws Exception
  {
    logTestStarted( "testRegisterNewUserWithNullPropertyValue" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, "v5" );
      BackendlessUser user = getRandomNotRegisteredUser();
      user.setProperty( "gender", null );

      Backendless.UserService.register( user );

      logTestFailed( "UserService accepted null value for a property" );
    }
    catch( Exception t )
    {
      checkErrorCode( 3041, t );
      logTestFinished();
    }
  }

  @Test
  public void testRegisterNewUserWithEmptyPropertyValue() throws Exception
  {
    logTestStarted( "testRegisterNewUserWithEmptyPropertyValue" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, "v5" );
      BackendlessUser user = getRandomNotRegisteredUser();
      user.setProperty( "gender", "" );

      Backendless.UserService.register( user );

      logTestFailed( "UserService accepted empty value for a property" );
    }
    catch( Exception t )
    {
      checkErrorCode( 3041, t );
      logTestFinished();
    }
  }

  @Test
  public void testRegisterNewUserWithEmptyCredentials() throws Exception
  {
    logTestStarted( "testRegisterNewUserWithEmptyCredentials" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = new BackendlessUser();
      user.setProperty( LOGIN_KEY, "" );
      user.setProperty( EMAIL_KEY, "" );
      user.setPassword( "" );
      user.setProperty( "", "foo" );
      user.setProperty( "foo", "" );

      Backendless.UserService.register( user );
      logTestFailed( "BackendlessUser accepted empty values" );
    }
    catch( Exception t )
    {
      logTestFinished();
    }
  }

  @Test
  public void testRegisterNewUser() throws Exception
  {
    logTestStarted( "testRegisterNewUser" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = getRandomNotRegisteredUser();
      String propertyKey = "property_key" + Math.abs( random.nextInt() );
      String propertyValue = "property_value" + random.nextInt();
      user.setProperty( propertyKey, propertyValue );
      BackendlessUser registeredUser = Backendless.UserService.register( user );

      usedProperties.add( propertyKey );

      Assert.assertNotNull( "UserService.register didn't set user ID", registeredUser.getProperty( ID_KEY ) );

      for( String key : user.getProperties().keySet() )
      {
        Assert.assertTrue( "Registered user didn`t contain expected property " + key, registeredUser.getProperties().containsKey( key ) );
        Assert.assertEquals( "UserService.register changed property " + key, user.getProperty( key ), registeredUser.getProperty( key ) );
      }
    }
    catch( Throwable t )
    {
      logTestFailed( t );
    }

    logTestFinished();
  }

  @Test
  public void testRegisterNewUserWithDuplicateIdentity() throws Exception
  {
    logTestStarted( "testRegisterNewUserWithDuplicateIdentity" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = Backendless.UserService.register( getRandomNotRegisteredUser() );

      BackendlessUser fakeUser = getRandomNotRegisteredUser();
      fakeUser.setProperty( LOGIN_KEY, user.getProperty( LOGIN_KEY ) );

      Backendless.UserService.register( fakeUser );

      logTestFailed( "Server accepted a user with id value" );
    }
    catch( Exception t )
    {
      checkErrorCode( 3033, t );
      logTestFinished();
    }
  }

  @Test
  public void testRegisterNewUserWithId() throws Exception
  {
    logTestStarted( "testRegisterNewUserWithId" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = Backendless.UserService.register( getRandomNotRegisteredUser() );

      BackendlessUser fakeUser = getRandomNotRegisteredUser();
      fakeUser.setProperty( ID_KEY, user.getProperty( ID_KEY ) );

      Backendless.UserService.register( fakeUser );

      logTestFailed( "Server accepted a user with id value" );
    }
    catch( Exception t )
    {
      checkErrorCode( 3039, t );
      logTestFinished();
    }
  }

  @Test
  public void testRegisterNewUserAtAppWithDisabledRegistration() throws Exception
  {
    logTestStarted( "testRegisterNewUserAtAppWithDisabledRegistration" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, "v6" );

      Backendless.UserService.register( getRandomNotRegisteredUser() );

      logTestFailed( "Server accepted registration for an application with disabled registration" );
    }
    catch( Exception t )
    {
      checkErrorCode( 3009, t );
      logTestFinished();
    }
  }

  @Test
  public void testRegisterNewUserAtAppWithDisabledDynamicProperties() throws Exception
  {
    logTestStarted( "testRegisterNewUserAtAppWithDisabledDynamicProperties" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, "v7" );
      BackendlessUser user = getRandomNotRegisteredUser();
      user.setProperty( "somedynamicpropertykey", "somedynamicpropertyvalue" );

      Backendless.UserService.register( user );

      logTestFailed( "BackendlessUser accepted registration for an application with disabled dynamic properties" );
    }
    catch( Exception t )
    {
      checkErrorCode( 3010, t );
      logTestFinished();
    }
  }

  @Test
  public void testRegisterNewUserWithoutIdentity() throws Exception
  {
    logTestStarted( "testRegisterNewUserWithoutIdentity" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      String timestamp = new java.sql.Timestamp( Calendar.getInstance().getTime().getTime() ).toString().replaceAll( "\\D*", "" );
      BackendlessUser user = new BackendlessUser();
      user.setProperty( EMAIL_KEY, "bot" + timestamp + "@gmail.com" );
      user.setPassword( "somepass_" + timestamp );

      Backendless.UserService.register( user );

      logTestFailed( "Server accepted user without identity" );
    }
    catch( Exception t )
    {
      checkErrorCode( 3013, t );
      logTestFinished();
    }
  }
}
