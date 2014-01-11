package com.backendless.tests.junit.unitTests.usersService.asyncTests;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.tests.junit.Defaults;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class RegistrationTest extends TestsFrame
{
  @Test(expected = IllegalArgumentException.class)
  public void testRegisterNewUserFromNull()
  {
    setLatch();
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        Backendless.UserService.register( null, new AsyncCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser response )
          {
            failCountDownWith( "UserService accepted a null user" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            failCountDownWith( fault );
          }
        } );
      }
    } );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterNewUserFromEmptyUser()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        Backendless.UserService.register( new BackendlessUser(), new AsyncCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser response )
          {
            failCountDownWith( "UserService accepted a null user" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            failCountDownWith( fault );
          }
        } );
      }
    } );
  }

  @Test
  public void testRegisterNewUserWithPartialFields()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, "v5" );

        BackendlessUser partialUser = new BackendlessUser();
        String login = "bot" + random.nextInt();
        partialUser.setProperty( LOGIN_KEY, login );
        partialUser.setProperty( EMAIL_KEY, login + "@gmail.com" );
        partialUser.setPassword( "somepass" );
        Backendless.UserService.register( partialUser, new AsyncCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser response )
          {
            failCountDownWith( "UserService accepted a user without required fields" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( 3012, fault );
          }
        } );
      }
    } );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterNewUserWithNulls()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        BackendlessUser user = new BackendlessUser();
        user.setProperty( LOGIN_KEY, null );
        user.setProperty( EMAIL_KEY, null );
        user.setPassword( null );
        user.setProperty( null, "foo" );
        user.setProperty( "foo", null );

        Backendless.UserService.register( user, new AsyncCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser response )
          {
            failCountDownWith( "UserService accepted null values" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            failCountDownWith( fault );
          }
        } );
      }
    } );
  }

  @Test
  public void testRegisterNewUserWithNullPropertyValue() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, "v5" );
        BackendlessUser user = getRandomNotRegisteredUser();
        user.setProperty( "gender", null );
        Backendless.UserService.register( user, new AsyncCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser response )
          {
            failCountDownWith( "UserService accepted null value for a property" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( 3041, fault );
          }
        } );
      }
    } );
  }

  @Test
  public void testRegisterNewUserWithEmptyPropertyValue() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, "v5" );
        BackendlessUser user = getRandomNotRegisteredUser();
        user.setProperty( "gender", "" );

        Backendless.UserService.register( user, new AsyncCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser response )
          {
            failCountDownWith( "UserService accepted empty value for a property" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( 3041, fault );
          }
        } );
      }
    } );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterNewUserWithEmptyCredentials() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        BackendlessUser user = new BackendlessUser();
        user.setProperty( LOGIN_KEY, "" );
        user.setProperty( EMAIL_KEY, "" );
        user.setPassword( "" );
        user.setProperty( "", "foo" );
        user.setProperty( "foo", "" );

        Backendless.UserService.register( user, new AsyncCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser response )
          {
            failCountDownWith( "BackendlessUser accepted empty values" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            failCountDownWith( fault );
          }
        } );
      }
    } );
  }

  @Test
  public void testRegisterNewUser() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        final BackendlessUser user = getRandomNotRegisteredUser();
        final String propertyKey = "property_key" + Math.abs( random.nextInt() );
        String propertyValue = "property_value" + random.nextInt();
        user.setProperty( propertyKey, propertyValue );
        Backendless.UserService.register( user, new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser registeredUser )
          {
            try
            {
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
              failCountDownWith( t );
            }

            countDown();
          }
        } );
      }
    } );
  }

  @Test
  public void testRegisterNewUserWithDuplicateIdentity() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        Backendless.UserService.register( getRandomNotRegisteredUser(), new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser user )
          {
            BackendlessUser fakeUser = getRandomNotRegisteredUser();
            fakeUser.setProperty( LOGIN_KEY, user.getProperty( LOGIN_KEY ) );

            Backendless.UserService.register( fakeUser, new AsyncCallback<BackendlessUser>()
            {
              @Override
              public void handleResponse( BackendlessUser response )
              {
                failCountDownWith( "Server accepted a user with id value" );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( 3033, fault );
              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testRegisterNewUserWithId() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        Backendless.UserService.register( getRandomNotRegisteredUser(), new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser user )
          {
            BackendlessUser fakeUser = getRandomNotRegisteredUser();
            fakeUser.setProperty( ID_KEY, user.getProperty( ID_KEY ) );

            Backendless.UserService.register( fakeUser, new AsyncCallback<BackendlessUser>()
            {
              @Override
              public void handleResponse( BackendlessUser response )
              {
                failCountDownWith( "Server accepted a user with id value" );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( 3039, fault );
              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testRegisterNewUserAtAppWithDisabledRegistration() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, "v6" );

        Backendless.UserService.register( getRandomNotRegisteredUser(), new AsyncCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser response )
          {
            failCountDownWith( "Server accepted registration for an application with disabled registration" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( 3009, fault );
          }
        } );
      }
    } );
  }

  @Test
  public void testRegisterNewUserAtAppWithDisabledDynamicProperties() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, "v7" );
        BackendlessUser user = getRandomNotRegisteredUser();
        user.setProperty( "somedynamicpropertykey", "somedynamicpropertyvalue" );

        Backendless.UserService.register( user, new AsyncCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser response )
          {
            failCountDownWith( "BackendlessUser accepted registration for an application with disabled dynamic properties" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( 3010, fault );
          }
        } );
      }
    } );
  }

  @Test
  public void testRegisterNewUserWithoutIdentity() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        String timestamp = new java.sql.Timestamp( Calendar.getInstance().getTime().getTime() ).toString().replaceAll( "\\D*", "" );
        BackendlessUser user = new BackendlessUser();
        user.setProperty( EMAIL_KEY, "bot" + timestamp + "@gmail.com" );
        user.setPassword( "somepass_" + timestamp );

        Backendless.UserService.register( user, new AsyncCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser response )
          {
            failCountDownWith( "Server accepted user without identity" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( 3013, fault );
          }
        } );
      }
    } );
  }
}
