package com.backendless.tests.junit.unitTests.persistenceService.asyncTests;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.tests.junit.Defaults;
import com.backendless.tests.junit.IAsyncTest;
import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.AndroidPersonAsync;
import org.junit.Assert;
import org.junit.BeforeClass;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class TestsFrame extends IAsyncTest
{
  Random random = new Random();
  public static final String LOGIN_KEY = "login";
  public static final String EMAIL_KEY = "email";
  public static final String PASSWORD_KEY = "password";
  public static final String ID_KEY = "id";

  public AndroidPersonAsync getRandomAndroidPersonAsync()
  {
    AndroidPersonAsync androidPerson = new AndroidPersonAsync();
    androidPerson.setAge( random.nextInt( 80 ) );
    androidPerson.setName( new java.sql.Timestamp( Calendar.getInstance().getTime().getTime() ).toString().replaceAll( "\\D*", "" ) + random.nextInt() );

    return androidPerson;
  }

  public <E> void assertArgumentAndResultCollections( List<E> entities, BackendlessCollection<E> backendlessCollection )
  {
    try
    {
      Assert.assertEquals( "Server found wrong number of objects", entities.size(), backendlessCollection.getTotalObjects() );
      Assert.assertEquals( "Server returned wrong number of objects", entities.size(), backendlessCollection.getCurrentPage().size() );

      for( E entity : entities )
      {
        Assert.assertTrue( "Server result didn't contain expected entity", backendlessCollection.getCurrentPage().contains( entity ) );
      }
    }
    catch( Throwable t )
    {
      failCountDownWith( t );
    }
  }

  @BeforeClass
  public static void setUp() throws Throwable
  {
    Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
  }
}
