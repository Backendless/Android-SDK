package com.backendless.tests.junit.unitTests.persistenceService.syncTests;

import com.backendless.Backendless;
import com.backendless.property.DateTypeEnum;
import com.backendless.property.ObjectProperty;
import com.backendless.tests.junit.unitTests.persistenceService.entities.AndroidPerson;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class RetrievePropertiesTest extends TestsFrame
{
  @Test
  public void testRetrieveEntityProperties() throws Throwable
  {
    AndroidPerson androidPerson = getRandomAndroidPerson();

    Backendless.Persistence.save( androidPerson );

    List<ObjectProperty> properties = Backendless.Persistence.describe( AndroidPerson.class.getSimpleName() );

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
        logTestFailed( "Got unexpected property: " + property.getName() );
      }
    }
  }

  @Test
  public void testRetrievePropertiesForUnknownObject() throws Throwable
  {
    try
    {
      Backendless.Persistence.describe( this.getClass().getCanonicalName() );
      logTestFailed( "Server didn't throw an exception" );
    }
    catch( Throwable e )
    {
      checkErrorCode( 1009, e );
    }
  }
}
