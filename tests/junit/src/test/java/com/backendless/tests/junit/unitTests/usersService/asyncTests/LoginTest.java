package com.backendless.tests.junit.unitTests.usersService.asyncTests;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.property.UserProperty;
import com.backendless.tests.junit.Defaults;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class LoginTest extends TestsFrame
{
  @Test
  public void testLoginWithNullLogin()
  {
    setLatch();

    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        getRandomRegisteredUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser response )
          {
            Backendless.UserService.login( null, response.getPassword(), new AsyncCallback<BackendlessUser>()
            {
              @Override
              public void handleResponse( BackendlessUser response )
              {
                failCountDownWith( "UserService accepted null login" );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( ExceptionMessage.NULL_LOGIN, fault );
              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testLoginWithNullPassword()
  {
    setLatch();

    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        getRandomRegisteredUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser response )
          {
            Backendless.UserService.login( (String) response.getProperty( LOGIN_KEY ), null, new AsyncCallback<BackendlessUser>()
            {
              @Override
              public void handleResponse( BackendlessUser response )
              {
                failCountDownWith( "UserService accepted null password" );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( ExceptionMessage.NULL_PASSWORD, fault );
              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testLoginWithWrongCredentials()
  {
    setLatch();
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        getRandomRegisteredUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser response )
          {
            Backendless.UserService.login( response.getProperty( LOGIN_KEY ) + "foobar", response.getPassword() + "foobar", new AsyncCallback<BackendlessUser>()
            {
              @Override
              public void handleResponse( BackendlessUser response )
              {
                failCountDownWith( "Server accepted wrong credentials" );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( 3003, fault );
              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testLoginWithDisabledAppLogin()
  {
    setLatch();
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, "v2_nata" );
        getRandomRegisteredUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser user )
          {
            Backendless.UserService.login( (String) user.getProperty( LOGIN_KEY ), user.getPassword(), new AsyncCallback<BackendlessUser>()
            {
              @Override
              public void handleResponse( BackendlessUser response )
              {
                failCountDownWith( "Server accepted login" );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( 3034, fault );
              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testLoginWithProperCredentials()
  {
    setLatch();
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        getRandomRegisteredUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser user )
          {
            Backendless.UserService.login( (String) user.getProperty( LOGIN_KEY ), user.getPassword(), new ResponseCallback<BackendlessUser>()
            {
              @Override
              public void handleResponse( BackendlessUser response )
              {
                Backendless.UserService.describeUserClass( new ResponseCallback<List<UserProperty>>() );
              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testLoginWithExternalAuth()
  {
    setLatch();
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, "v_nata6" );
        getRandomRegisteredUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser user )
          {
            Backendless.UserService.login( (String) user.getProperty( LOGIN_KEY ), "password123", new AsyncCallback<BackendlessUser>()
            {
              @Override
              public void handleResponse( BackendlessUser response )
              {
                failCountDownWith( "Server accepted registration for an invalid external Auth" );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( 3052, fault );
              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testLoginWithoutFailedLoginsLock()
  {
    setLatch();
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, "v3_nata" );
        getRandomRegisteredUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( final BackendlessUser user )
          {
            Backendless.UserService.login( (String) user.getProperty( LOGIN_KEY ), user.getPassword() + "foo", new AsyncCallback<BackendlessUser>()
            {
              @Override
              public void handleResponse( BackendlessUser response )
              {
                failCountDownWith( "Server didn't locked login" );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                Backendless.UserService.login( (String) user.getProperty( LOGIN_KEY ), user.getPassword(), new ResponseCallback<BackendlessUser>()
                {
                  @Override
                  public void handleResponse( BackendlessUser response )
                  {
                    countDown();
                  }
                } );
              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testLoginWithUserFromAnotherVersion()
  {
    setLatch();
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        getRandomRegisteredUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser user )
          {
            Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, "v3_nata" );
            Backendless.UserService.login( (String) user.getProperty( LOGIN_KEY ), user.getPassword(), new AsyncCallback<BackendlessUser>()
            {
              @Override
              public void handleResponse( BackendlessUser response )
              {
                failCountDownWith( "Server accepted login for the user from another version" );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( 3003, fault );
              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testCurrentUserAfterLogin()
  {
    setLatch();
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        BackendlessUser notRegisteredUseruser = getRandomNotRegisteredUser();
        final String propertyKey = "property_key" + Math.abs( random.nextInt() );
        String propertyValue = "property_value" + random.nextInt();
        notRegisteredUseruser.setProperty( propertyKey, propertyValue );

        Backendless.UserService.register( notRegisteredUseruser, new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( final BackendlessUser user )
          {
            usedProperties.add( propertyKey );

            Backendless.UserService.login( (String) user.getProperty( LOGIN_KEY ), user.getPassword(), new ResponseCallback<BackendlessUser>()
            {
              @Override
              public void handleResponse( BackendlessUser response )
              {
                try
                {
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
                  failCountDownWith( t );
                }

                countDown();
              }
            } );
          }
        } );
      }
    } );
  }
}
