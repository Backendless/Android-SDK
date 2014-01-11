package com.backendless.tests.junit.unitTests.usersService.syncTests;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessException;
import com.backendless.tests.junit.ITest;
import org.junit.After;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

@Ignore
public class TestsFrame extends ITest
{
  public Random random = new Random();
  public List<String> usedProperties = new ArrayList<String>();

  public static final String LOGIN_KEY = "login";
  public static final String EMAIL_KEY = "email";
  public static final String PASSWORD_KEY = "password";
  public static final String ID_KEY = "objectId";
  public static final String CREATED_KEY = "created";
  public static final String UPDATED_KEY = "updated";
  public static final String CUSTOM_ROLE = "TestRole";
  public static final String SYSTEM_ROLE = "AuthenticatedUser";

  public BackendlessUser getRandomNotRegisteredUser()
  {
    String timestamp = new java.sql.Timestamp( Calendar.getInstance().getTime().getTime() ).toString().replaceAll( "\\D*", "" );
    BackendlessUser result = new BackendlessUser();
    result.setProperty( LOGIN_KEY, "bot" + timestamp );
    result.setProperty( EMAIL_KEY, result.getProperty( LOGIN_KEY ) + "@gmail.com" );
    result.setPassword( "somepass_" + timestamp );

    return result;
  }

  public BackendlessUser getRandomRegisteredUser() throws BackendlessException
  {
    return Backendless.UserService.register( getRandomNotRegisteredUser() );
  }

  public BackendlessUser getRandomLoggedInUser() throws BackendlessException
  {
    BackendlessUser user = getRandomRegisteredUser();
    Backendless.UserService.login( (String) user.getProperty( LOGIN_KEY ), user.getPassword() );

    return user;
  }

  @After
  public void tearDown() throws Throwable
  {
    if( Backendless.UserService.CurrentUser() != null )
      Backendless.UserService.logout();
  }
}
