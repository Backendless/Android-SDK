package com.backendless.tests.junit.unitTests.usersService.asyncTests;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.tests.junit.IAsyncTest;
import org.junit.After;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class TestsFrame extends IAsyncTest
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
    String timestamp = new java.sql.Timestamp( Calendar.getInstance().getTime().getTime() ).toString().replaceAll( "\\D*", "" ) + random.nextInt();
    BackendlessUser result = new BackendlessUser();
    result.setProperty( LOGIN_KEY, "bot" + timestamp );
    result.setProperty( EMAIL_KEY, result.getProperty( LOGIN_KEY ) + "@gmail.com" );
    result.setPassword( "somepass_" + timestamp );

    return result;
  }

  public void getRandomRegisteredUser( AsyncCallback<BackendlessUser> callback )
  {
    Backendless.UserService.register( getRandomNotRegisteredUser(), callback );
  }

  public void getRandomLoggedInUser( final AsyncCallback<BackendlessUser> callback )
  {
    getRandomRegisteredUser( new AsyncCallback<BackendlessUser>()
    {
      @Override
      public void handleResponse( final BackendlessUser registeredUser )
      {
        Backendless.UserService.login( (String) registeredUser.getProperty( LOGIN_KEY ), registeredUser.getPassword(), new AsyncCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser response )
          {
            if( callback != null )
            {
              callback.handleResponse( registeredUser );
            }
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            if( callback != null )
            {
              callback.handleFault( fault );
            }
          }
        } );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        callback.handleFault( fault );
      }
    } );
  }

  @After
  public void tearDown() throws Throwable
  {
    if( Backendless.UserService.CurrentUser() != null )
      Backendless.UserService.logout();
  }
}
