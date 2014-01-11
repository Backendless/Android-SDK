package com.backendless.tests.junit.unitTests.usersService.syncTests;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.property.UserProperty;
import com.backendless.tests.junit.Defaults;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class UserPropertiesTest extends TestsFrame
{
  @Test
  public void testDescribeUserProperties() throws Exception
  {
    logTestStarted( "testDescribeUserProperties" );

    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, "v10" );
      BackendlessUser user = getRandomNotRegisteredUser();
      String propertyKeySync = "property_keySync";
      String propertyKeyAsync = "property_keyAsync";
      user.setProperty( propertyKeySync, "porperty_value#" + random.nextInt() );
      Backendless.UserService.register( user );
      Backendless.UserService.login( (String) user.getProperty( LOGIN_KEY ), user.getPassword() );
      List<UserProperty> userProperties = Backendless.UserService.describeUserClass();
      Assert.assertNotNull( "Server returned null user properties", userProperties );
      Assert.assertTrue( "Server returned empty user properties", !userProperties.isEmpty() );

      List<String> properties = new ArrayList<String>();
      properties.add( propertyKeySync );
      properties.add( propertyKeyAsync );
      properties.add( ID_KEY );
      properties.add( CREATED_KEY );
      properties.add( UPDATED_KEY );
      properties.add( LOGIN_KEY );
      properties.add( PASSWORD_KEY );
      properties.add( EMAIL_KEY );

      for( UserProperty userProperty : userProperties )
      {
        Assert.assertNotNull( "User property was null", userProperty );
        Assert.assertTrue( "User properties contained unexpected property " + userProperty.getName(), properties.contains( userProperty.getName() ) );
        Assert.assertNotNull( "User properties type was null", userProperty.getType() );
      }
    }
    catch( Throwable t )
    {
      logTestFailed( t );
    }

    logTestFinished();
  }
}
