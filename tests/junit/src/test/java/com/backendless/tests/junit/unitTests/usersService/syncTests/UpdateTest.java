package com.backendless.tests.junit.unitTests.usersService.syncTests;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.property.DateTypeEnum;
import com.backendless.property.UserProperty;
import com.backendless.tests.junit.Defaults;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class UpdateTest extends TestsFrame
{
  @Test
  public void testUpdateUserWithEmptyCredentials() throws Exception
  {
    logTestStarted( "testUpdateUserWithEmptyCredentials" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = getRandomLoggedInUser();
      user.setProperty( LOGIN_KEY, "" );
      user.setPassword( "" );

      Backendless.UserService.update( user );

      logTestFailed( "User with empty credentials accepted" );
    }
    catch( Exception t )
    {
      checkErrorCode( ExceptionMessage.NULL_PASSWORD, t );
      logTestFinished();
    }
  }

  @Test
  public void testUpdateUserWithNullCredentials() throws Exception
  {
    logTestStarted( "testUpdateUserWithNullCredentials" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = getRandomLoggedInUser();
      user.setProperty( LOGIN_KEY, null );
      user.setPassword( null );

      Backendless.UserService.update( user );
      logTestFailed( "User with null credentials accepted" );
    }
    catch( Exception t )
    {
      checkErrorCode( ExceptionMessage.NULL_PASSWORD, t );
      logTestFinished();
    }
  }

  @Test
  public void testUpdateUserWithNullUserId() throws Exception
  {
    logTestStarted( "testUpdateUserWithNullUserId" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = getRandomLoggedInUser();
      user.setProperty( ID_KEY, null );

      Backendless.UserService.update( user );
      logTestFailed( "User with null id accepted" );
    }
    catch( Exception t )
    {
      checkErrorCode( 3029, t );
      logTestFinished();
    }
  }

  @Test
  public void testUpdateUserWithEmptyUserId() throws Exception
  {
    logTestStarted( "testUpdateUserWithEmptyUserId" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = getRandomLoggedInUser();
      user.setProperty( ID_KEY, "" );

      Backendless.UserService.update( user );
      logTestFailed( "User with empty id accepted" );
    }
    catch( Exception t )
    {
      checkErrorCode( ExceptionMessage.WRONG_USER_ID, t );
      logTestFinished();
    }
  }

  @Test
  public void testUpdateUserWithWrongUserId() throws Exception
  {
    logTestStarted( "testUpdateUserWithWrongUserId" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = getRandomLoggedInUser();
      user.setProperty( ID_KEY, "foobar" );

      Backendless.UserService.update( user );
      logTestFailed( "User with wrong id accepted" );
    }
    catch( BackendlessException t )
    {
      checkErrorCode( 3029, t );
      logTestFinished();
    }
  }

  @Test
  public void testUpdateUserForVersionWithDisabledDynamicPropertis() throws Exception
  {
    logTestStarted( "testUpdateUserForVersionWithDisabledDynamicPropertis" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, "v7" );
      BackendlessUser user = getRandomLoggedInUser();
      user.setProperty( "somePropertyKey", "somePropertyValue" );

      Backendless.UserService.update( user );

      logTestFailed( "Server updated user with a dynamic property for a version with disabled dynamic properties." );
    }
    catch( BackendlessException t )
    {
      checkErrorCode( 3031, t );
      logTestFinished();
    }
  }

  @Test
  public void testUpdateUserForVersionWithEnabledDynamicPropertis() throws Exception
  {
    logTestStarted( "testUpdateUserForVersionWithEnabledDynamicPropertis" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = getRandomLoggedInUser();
      String propertyKey = "somePropertyKey" + Math.abs( random.nextInt() );
      String propertyValue = "somePropertyValue" + random.nextInt();
      user.setProperty( propertyKey, propertyValue );

      for( String usedProperty : usedProperties )
        user.setProperty( usedProperty, "someValue" );

      Backendless.UserService.update( user );

      usedProperties.add( propertyKey );

      List<UserProperty> userProperties = Backendless.UserService.describeUserClass();
      Assert.assertNotNull( "Server returned null user properties", userProperties );
      Assert.assertTrue( "Server returned empty user properties", !userProperties.isEmpty() );

      boolean flag = false;

      for( UserProperty userProperty : userProperties )
        if( userProperty.getName().equals( propertyKey ) )
        {
          flag = true;
          Assert.assertTrue( "Property had wrong type", userProperty.getType().equals( DateTypeEnum.STRING ) );
        }

      Assert.assertTrue( "Expected property was not found", flag );

      logTestFinished();
    }
    catch( BackendlessException t )
    {
      logTestFailed( t );
    }
    catch( Throwable t )
    {
      logTestFailed( t );
    }
  }

  @Test
  public void testUpdateRegisteredUserEmailAndPassword() throws Exception
  {
    logTestStarted( "testUpdateRegisteredUserEmailAndPassword" );

    try
    {
      String newpassword = "some_new_password";
      String newemail = "some_new_email@gmail.com";
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = getRandomLoggedInUser();
      user.setPassword( newpassword );
      user.setProperty( EMAIL_KEY, newemail );

      Backendless.UserService.update( user );

      Assert.assertEquals( "Updated used has a wrong passwrod", newpassword, user.getPassword() );
      Assert.assertEquals( "Updated used has a wrong email", newemail, user.getProperty( EMAIL_KEY ) );

      logTestFinished();
    }
    catch( BackendlessException t )
    {
      logTestFailed( t );
    }
  }

  @Test
  public void testUpdateRegisteredUserIdentity() throws Exception
  {
    logTestStarted( "testUpdateRegisteredUserEmailAndPassword" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = getRandomLoggedInUser();
      user.setProperty( LOGIN_KEY, "some_new_login_" + user.getProperty( LOGIN_KEY ) );

      Backendless.UserService.update( user );

      logTestFinished();
    }
    catch( BackendlessException t )
    {
      logTestFailed( t );
    }
  }
}
