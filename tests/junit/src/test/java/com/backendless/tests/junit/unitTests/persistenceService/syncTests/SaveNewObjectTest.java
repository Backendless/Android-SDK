package com.backendless.tests.junit.unitTests.persistenceService.syncTests;

import com.backendless.Backendless;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.tests.junit.unitTests.persistenceService.entities.UniqueAndroidPerson;
import com.backendless.tests.junit.unitTests.persistenceService.entities.primitiveEntities.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class SaveNewObjectTest extends TestsFrame
{
  @Test
  public void testSaveEntityToANewDataBase() throws Throwable
  {
    UniqueAndroidPerson uniqueAndroidPerson = new UniqueAndroidPerson();
    uniqueAndroidPerson.setAge( 16 );
    uniqueAndroidPerson.setName( "John" );
    uniqueAndroidPerson.setBirthday( Calendar.getInstance().getTime() );

    UniqueAndroidPerson savedUniqueAndroidPerson = Backendless.Persistence.save( uniqueAndroidPerson );
    Assert.assertNotNull( "Server returned a null result", savedUniqueAndroidPerson );
    Assert.assertNotNull( "Returned object doesn't have expected field", savedUniqueAndroidPerson.getName() );
    Assert.assertNotNull( "Returned object doesn't have expected field id", savedUniqueAndroidPerson.getObjectId() );
    Assert.assertNotNull( "Returned object doesn't have expected field created", savedUniqueAndroidPerson.getCreated() );
    Assert.assertEquals( "Returned object has wrong field value", uniqueAndroidPerson.getName(), savedUniqueAndroidPerson.getName() );
    Assert.assertEquals( "Returned object has wrong field value", uniqueAndroidPerson.getAge(), savedUniqueAndroidPerson.getAge() );
    Assert.assertTrue( "Returned object has wrong field value", (savedUniqueAndroidPerson.getBirthday().getTime() - uniqueAndroidPerson.getBirthday().getTime()) < 1000 );
  }

  @Test
  public void testSaveNullEntity() throws Throwable
  {
    try
    {
      Backendless.Persistence.save( null );
      logTestFailed( "Client saved a null entity" );
    }
    catch( Throwable e )
    {
      checkErrorCode( ExceptionMessage.NULL_ENTITY, e );
    }
  }

  @Test
  public void testSaveStringEntity() throws Throwable
  {
    StringEntity entity = new StringEntity();
    entity.setStringEntity( "foobar" );

    StringEntity savedEntity = Backendless.Persistence.save( entity );
    Assert.assertNotNull( "Server returned a null result", savedEntity );
    Assert.assertNotNull( "Returned object doesn't have expected field", savedEntity.getStringEntity() );
    Assert.assertNotNull( "Returned object doesn't have expected field id", savedEntity.getObjectId() );
    Assert.assertNotNull( "Returned object doesn't have expected field created", savedEntity.getCreated() );
    Assert.assertEquals( "Returned object has wrong field value", entity.getStringEntity(), savedEntity.getStringEntity() );
  }

  @Test
  public void testSaveBooleanEntity() throws Throwable
  {
    BooleanEntity entity = new BooleanEntity();
    entity.setBooleanField( false );

    BooleanEntity savedEntity = Backendless.Persistence.save( entity );
    Assert.assertNotNull( "Server returned a null result", savedEntity );
    Assert.assertNotNull( "Returned object doesn't have expected field id", savedEntity.getObjectId() );
    Assert.assertNotNull( "Returned object doesn't have expected field created", savedEntity.getCreated() );
    Assert.assertEquals( "Returned object has wrong field value", entity.isBooleanField(), savedEntity.isBooleanField() );
  }

  @Test
  public void testSaveDateEntity() throws Throwable
  {
    DateEntity entity = new DateEntity();
    entity.setDateField( Calendar.getInstance().getTime() );

    DateEntity savedEntity = Backendless.Persistence.save( entity );
    Assert.assertNotNull( "Server returned a null result", savedEntity );
    Assert.assertNotNull( "Returned object doesn't have expected field", savedEntity.getDateField() );
    Assert.assertNotNull( "Returned object doesn't have expected field id", savedEntity.getObjectId() );
    Assert.assertNotNull( "Returned object doesn't have expected field created", savedEntity.getCreated() );
    Assert.assertTrue( "Returned object has wrong field value", savedEntity.getDateField().getTime() - entity.getDateField().getTime() < 1000 );
  }

  @Test
  public void testSaveIntEntity() throws Throwable
  {
    IntEntity entity = new IntEntity();
    entity.setIntField( 16 );

    IntEntity savedEntity = Backendless.Persistence.save( entity );
    Assert.assertNotNull( "Server returned a null result", savedEntity );
    Assert.assertNotNull( "Returned object doesn't have expected field id", savedEntity.getObjectId() );
    Assert.assertNotNull( "Returned object doesn't have expected field created", savedEntity.getCreated() );
    Assert.assertEquals( "Returned object has wrong field value", entity.getIntField(), savedEntity.getIntField() );
  }

  @Test
  public void testSaveDoubleEntity() throws Throwable
  {
    DoubleEntity entity = new DoubleEntity();
    entity.setDoubleEntity( 16.1616d );

    DoubleEntity savedEntity = Backendless.Persistence.save( entity );
    Assert.assertNotNull( "Server returned a null result", savedEntity );
    Assert.assertNotNull( "Returned object doesn't have expected field id", savedEntity.getObjectId() );
    Assert.assertNotNull( "Returned object doesn't have expected field created", savedEntity.getCreated() );
    Assert.assertEquals( "Returned object has wrong field value", entity.getDoubleEntity(), savedEntity.getDoubleEntity(), 0.000000d );
  }
}
