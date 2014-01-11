package com.backendless.tests.junit.unitTests.usersService.asyncTests;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
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
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        getRandomLoggedInUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser user )
          {
            user.setProperty( LOGIN_KEY, "" );
            user.setPassword( "" );

            Backendless.UserService.update( user, new AsyncCallback<BackendlessUser>()
            {
              @Override
              public void handleResponse( BackendlessUser response )
              {
                failCountDownWith( "User with empty credentials accepted" );
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
  public void testUpdateUserWithNullCredentials() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        getRandomLoggedInUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser user )
          {
            user.setProperty( LOGIN_KEY, null );
            user.setPassword( null );

            Backendless.UserService.update( user, new AsyncCallback<BackendlessUser>()
            {
              @Override
              public void handleResponse( BackendlessUser response )
              {
                failCountDownWith( "User with null credentials accepted" );
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
  public void testUpdateUserWithNullUserId() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        getRandomLoggedInUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser user )
          {
            user.setProperty( ID_KEY, null );

            Backendless.UserService.update( user, new AsyncCallback<BackendlessUser>()
            {
              @Override
              public void handleResponse( BackendlessUser response )
              {
                failCountDownWith( "User with null id accepted" );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( 3029, fault );
              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testUpdateUserWithEmptyUserId() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        getRandomLoggedInUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser user )
          {
            user.setProperty( ID_KEY, "" );

            Backendless.UserService.update( user, new AsyncCallback<BackendlessUser>()
            {
              @Override
              public void handleResponse( BackendlessUser response )
              {
                failCountDownWith( "User with empty id accepted" );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( ExceptionMessage.WRONG_USER_ID, fault );
              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testUpdateUserWithWrongUserId() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        getRandomLoggedInUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser user )
          {
            user.setProperty( ID_KEY, "foobar" );

            Backendless.UserService.update( user, new AsyncCallback<BackendlessUser>()
            {
              @Override
              public void handleResponse( BackendlessUser response )
              {
                failCountDownWith( "User with wrong id accepted" );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( 3029, fault );
              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testUpdateUserForVersionWithDisabledDynamicPropertis() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, "v7" );
        getRandomLoggedInUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser user )
          {
            user.setProperty( "somePropertyKey", "somePropertyValue" );

            Backendless.UserService.update( user, new AsyncCallback<BackendlessUser>()
            {
              @Override
              public void handleResponse( BackendlessUser response )
              {
                failCountDownWith( "Server updated user with a dynamic property for a version with disabled dynamic properties." );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( 3031, fault );
              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testUpdateUserForVersionWithEnabledDynamicPropertis() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        getRandomLoggedInUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( final BackendlessUser user )
          {
            final String propertyKey = "somePropertyKey" + Math.abs( random.nextInt() );
            String propertyValue = "somePropertyValue" + random.nextInt();
            user.setProperty( propertyKey, propertyValue );

            for( String usedProperty : usedProperties )
            {
              user.setProperty( usedProperty, "someValue" );
            }

            Backendless.UserService.update( user, new ResponseCallback<BackendlessUser>()
            {
              @Override
              public void handleResponse( BackendlessUser response )
              {
                usedProperties.add( propertyKey );

                Backendless.UserService.describeUserClass( new ResponseCallback<List<UserProperty>>()
                {
                  @Override
                  public void handleResponse( List<UserProperty> userProperties )
                  {
                    try
                    {
                      Assert.assertNotNull( "Server returned null user properties", userProperties );
                      Assert.assertTrue( "Server returned empty user properties", !userProperties.isEmpty() );

                      boolean flag = false;

                      for( UserProperty userProperty : userProperties )
                      {
                        if( userProperty.getName().equals( propertyKey ) )
                        {
                          flag = true;
                          Assert.assertTrue( "Property had wrong type", userProperty.getType().equals( DateTypeEnum.STRING ) );
                        }
                      }

                      Assert.assertTrue( "Expected property was not found", flag );
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
    } );
  }

  @Test
  public void testUpdateRegisteredUserEmailAndPassword() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final String newpassword = "some_new_password";
        final String newemail = "some_new_email@gmail.com";
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        getRandomLoggedInUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( final BackendlessUser user )
          {
            user.setPassword( newpassword );
            user.setProperty( EMAIL_KEY, newemail );

            Backendless.UserService.update( user, new ResponseCallback<BackendlessUser>()
            {
              @Override
              public void handleResponse( BackendlessUser response )
              {
                try
                {
                  Assert.assertEquals( "Updated used has a wrong passwrod", newpassword, user.getPassword() );
                  Assert.assertEquals( "Updated used has a wrong email", newemail, user.getProperty( EMAIL_KEY ) );
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

  @Test
  public void testUpdateRegisteredUserIdentity() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        getRandomLoggedInUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser user )
          {
            user.setProperty( LOGIN_KEY, "some_new_login_" + user.getProperty( LOGIN_KEY ) );
            Backendless.UserService.update( user, new ResponseCallback<BackendlessUser>() );
          }
        } );
      }
    } );
  }
}
