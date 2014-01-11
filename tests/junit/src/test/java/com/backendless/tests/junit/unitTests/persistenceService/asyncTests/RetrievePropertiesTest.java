package com.backendless.tests.junit.unitTests.persistenceService.asyncTests;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.property.DateTypeEnum;
import com.backendless.property.ObjectProperty;
import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.AndroidPersonAsync;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class RetrievePropertiesTest extends TestsFrame
{
  @Test
  public void testRetrieveEntityProperties()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        AndroidPersonAsync androidPersonAsync = getRandomAndroidPersonAsync();

        Backendless.Persistence.save( androidPersonAsync, new ResponseCallback<AndroidPersonAsync>()
        {
          @Override
          public void handleResponse( AndroidPersonAsync response )
          {
            Backendless.Persistence.describe( AndroidPersonAsync.class.getSimpleName(), new ResponseCallback<List<ObjectProperty>>()
            {
              @Override
              public void handleResponse( List<ObjectProperty> properties )
              {
                try
                {
                  Assert.assertNotNull( "Server returned null", properties );
                  Assert.assertEquals( "Server returned unexpected amount of properties", properties.size(), 5 );

                  for( ObjectProperty property : properties )
                  {
                    if( property.getName().equals( "age" ) )
                    {
                      Assert.assertEquals( "Property was of unexpected type", DateTypeEnum.INT, property.getType() );
                      Assert.assertFalse( "Property had a wrong required value", property.isRequired() );
                    }
                    else if( property.getName().equals( "name" ) )
                    {
                      Assert.assertEquals( "Property was of unexpected type", DateTypeEnum.STRING, property.getType() );
                      Assert.assertFalse( "Property had a wrong required value", property.isRequired() );
                    }
                    else if( property.getName().equals( "created" ) )
                    {
                      Assert.assertEquals( "Property was of unexpected type", DateTypeEnum.DATETIME, property.getType() );
                      Assert.assertFalse( "Property had a wrong required value", property.isRequired() );
                    }
                    else if( property.getName().equals( "objectId" ) )
                    {
                      Assert.assertEquals( "Property was of unexpected type", DateTypeEnum.STRING_ID, property.getType() );
                      Assert.assertFalse( "Property had a wrong required value", property.isRequired() );
                    }
                    else if( property.getName().equals( "updated" ) )
                    {
                      Assert.assertEquals( "Property was of unexpected type", DateTypeEnum.DATETIME, property.getType() );
                      Assert.assertFalse( "Property had a wrong required value", property.isRequired() );
                    }
                    else
                    {
                      Assert.fail( "Got unexpected property: " + property.getName() );
                    }
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

  @Test
  public void testRetrievePropertiesForUnknownObject()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.Persistence.describe( RetrievePropertiesTest.class.getCanonicalName(), new AsyncCallback<List<ObjectProperty>>()
        {
          @Override
          public void handleResponse( List<ObjectProperty> response )
          {
            failCountDownWith( "Server didn't throw an exception" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( 1009, fault );
          }
        } );
      }
    } );
  }
}
