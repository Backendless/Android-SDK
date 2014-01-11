package com.backendless.tests.junit.unitTests.persistenceService.syncTests;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.IDataStore;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.backendless.tests.junit.unitTests.persistenceService.entities.findEntities.*;
import com.backendless.tests.junit.unitTests.persistenceService.entities.primitiveEntities.StringEntity;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FindObjectTest extends TestsFrame
{
  @Test
  public void testFindRecordById() throws Throwable
  {
    StringEntity entity = new StringEntity();
    entity.setStringEntity( "foobar" );

    StringEntity savedEntity = Backendless.Persistence.save( entity );
    Assert.assertNotNull( "Server returned a null result", savedEntity );
    Assert.assertNotNull( "Returned object doesn't have expected field", savedEntity.getStringEntity() );
    Assert.assertNotNull( "Returned object doesn't have expected field id", savedEntity.getObjectId() );
    Assert.assertNotNull( "Returned object doesn't have expected field created", savedEntity.getCreated() );
    Assert.assertEquals( "Returned object has wrong field value", entity.getStringEntity(), savedEntity.getStringEntity() );

    StringEntity foundEntity = Backendless.Persistence.of( StringEntity.class ).findById( savedEntity.getObjectId() );
    Assert.assertEquals( "Found object contain wrong created date", savedEntity.getCreated(), foundEntity.getCreated() );
    Assert.assertEquals( "Found object contain wrong objectId", savedEntity.getObjectId(), foundEntity.getObjectId() );
    Assert.assertEquals( "Found object contain wrong field value", savedEntity.getStringEntity(), foundEntity.getStringEntity() );
  }

  @Test
  public void testRetrieveObjectWithWrongObjectId()
  {
    try
    {
      Backendless.Persistence.save( new StringEntity() );
      Backendless.Persistence.of( StringEntity.class ).findById( "foobar" );
      logTestFailed( "Server didn't throw an exception" );
    }
    catch( Throwable e )
    {
      checkErrorCode( 1000, e );
    }
  }

  @Test
  public void testFindAllEntities() throws Throwable
  {
    List<FindAllEntity> entities = new ArrayList();

    for( int i = 0; i < 10; i++ )
    {
      FindAllEntity findAllEntity = new FindAllEntity();
      findAllEntity.setName( "bot_#" + i );
      findAllEntity.setAge( 20 + i );

      Backendless.Persistence.save( findAllEntity );
      entities.add( findAllEntity );
    }

    BackendlessCollection<FindAllEntity> backendlessCollection = Backendless.Persistence.of( FindAllEntity.class ).find();

    assertArgumentAndResultCollections( entities, backendlessCollection );
  }

  @Test
  public void testFindWithWhereEntities() throws Throwable
  {
    List<FindWithWhereEntity> entities = new ArrayList();

    for( int i = 0; i < 10; i++ )
    {
      FindWithWhereEntity findWithWhereEntity = new FindWithWhereEntity();
      findWithWhereEntity.setName( "bot_#" + i );
      findWithWhereEntity.setAge( 20 + i );

      Backendless.Persistence.save( findWithWhereEntity );

      if( i < 5 )
      {
        entities.add( findWithWhereEntity );
      }
    }

    BackendlessDataQuery dataQuery = new BackendlessDataQuery( "age < 25" );
    BackendlessCollection<FindWithWhereEntity> backendlessCollection = Backendless.Persistence.of( FindWithWhereEntity.class ).find( dataQuery );

    assertArgumentAndResultCollections( entities, backendlessCollection );
  }

  @Test
  public void testFindWithNegativeOffset() throws Throwable
  {
    try
    {
      Backendless.Persistence.of( Object.class ).find( new BackendlessDataQuery( new QueryOptions( 2, -1 ) ) );
      logTestFailed( "Client accepted a negative offset" );
    }
    catch( Throwable e )
    {
      checkErrorCode( ExceptionMessage.WRONG_OFFSET, e );
    }
  }

  @Test
  public void testFindWithNegativePageSize() throws Throwable
  {
    try
    {
      Backendless.Persistence.of( Object.class ).find( new BackendlessDataQuery( new QueryOptions( -1, 2 ) ) );
      logTestFailed( "Client accepted a negative pagesize" );
    }
    catch( Throwable e )
    {
      checkErrorCode( ExceptionMessage.WRONG_PAGE_SIZE, e );
    }
  }

  @Test
  public void testFindWithMissingProperties() throws Throwable
  {
    FindWithPropertiesEntity findWithPropertiesEntity = new FindWithPropertiesEntity();
    findWithPropertiesEntity.setName( "bot_#foobar" );
    findWithPropertiesEntity.setAge( 20 );
    Backendless.Persistence.save( findWithPropertiesEntity );

    List<String> properties = new ArrayList();
    properties.add( "foobar" );

    BackendlessDataQuery dataQuery = new BackendlessDataQuery( properties );

    try
    {
      Backendless.Persistence.of( FindWithPropertiesEntity.class ).find( dataQuery );
      logTestFailed( "Server didn't throw an exception" );
    }
    catch( Throwable e )
    {
      checkErrorCode( 1006, e );
    }
  }

  @Test
  public void testFindWithNullPropertiesQuery() throws Throwable
  {
    FindWithPropertiesEntity findWithPropertiesEntity = new FindWithPropertiesEntity();
    findWithPropertiesEntity.setName( "bot_#foobar" );
    findWithPropertiesEntity.setAge( 20 );
    Backendless.Persistence.save( findWithPropertiesEntity );

    List<String> properties = null;

    BackendlessDataQuery dataQuery = new BackendlessDataQuery( properties );

    BackendlessCollection<FindWithPropertiesEntity> backendlessCollection = Backendless.Persistence.of( FindWithPropertiesEntity.class ).find( dataQuery );

    Assert.assertTrue( "Server found wrong number of objects", backendlessCollection.getTotalObjects() > 0 );
    Assert.assertTrue( "Server returned wrong number of objects", backendlessCollection.getCurrentPage().size() > 0 );

    for( FindWithPropertiesEntity entity : backendlessCollection.getCurrentPage() )
    {
      Assert.assertTrue( "Server result contained wrong age field value", entity.getAge() > 0 );
      Assert.assertNotNull( "Server result contained non null field", entity.getName() );
      Assert.assertNotNull( "Server result contained non null field", entity.getObjectId() );
      Assert.assertNotNull( "Server result contained non null field", entity.getCreated() );
    }
  }

  @Test
  public void testFindWithExtraPropertiesQuery() throws Throwable
  {
    FindWithPropertiesEntity findWithPropertiesEntity = new FindWithPropertiesEntity();
    findWithPropertiesEntity.setName( "bot_#foobar" );
    findWithPropertiesEntity.setAge( 20 );
    Backendless.Persistence.save( findWithPropertiesEntity );

    List<String> properties = new ArrayList<String>();
    properties.add( "age" );
    properties.add( "foobar" );

    BackendlessDataQuery dataQuery = new BackendlessDataQuery( properties );

    BackendlessCollection<FindWithPropertiesEntity> backendlessCollection = Backendless.Persistence.of( FindWithPropertiesEntity.class ).find( dataQuery );

    Assert.assertTrue( "Server found wrong number of objects", backendlessCollection.getTotalObjects() > 0 );
    Assert.assertTrue( "Server returned wrong number of objects", backendlessCollection.getCurrentPage().size() > 0 );

    for( FindWithPropertiesEntity entity : backendlessCollection.getCurrentPage() )
    {
      Assert.assertTrue( "Server result contained wrong age field value", entity.getAge() > 0 );
      Assert.assertNull( "Server result contained non null field", entity.getName() );
      Assert.assertNull( "Server result contained non null field", entity.getObjectId() );
      Assert.assertNull( "Server result contained non null field", entity.getCreated() );
    }
  }

  @Test
  public void testFindFirstEntity() throws Throwable
  {
    FindFirstEntity firstEntity = new FindFirstEntity();
    firstEntity.setName( "bot_#first" );
    firstEntity.setAge( 20 );

    FindFirstEntity secondEntity = new FindFirstEntity();
    secondEntity.setName( "bot_#second" );
    secondEntity.setAge( 30 );

    Backendless.Persistence.save( firstEntity );
    Thread.sleep( 3000 );
    Backendless.Persistence.save( secondEntity );

    FindFirstEntity foundEntity = Backendless.Persistence.of( FindFirstEntity.class ).findFirst();

    Assert.assertEquals( "Server found unexpected entity", firstEntity, foundEntity );
  }

  @Test
  public void testFindFirstUnknownEntity() throws Throwable
  {
    try
    {
      Backendless.Persistence.of( this.getClass() ).findFirst();
      logTestFailed( "Server didn't throw an exception" );
    }
    catch( Throwable e )
    {
      checkErrorCode( 1009, e );
    }
  }

  @Test
  public void testFindLastEntity() throws Throwable
  {
    FindLastEntity firstEntity = new FindLastEntity();
    firstEntity.setName( "bot_#first" );
    firstEntity.setAge( 20 );

    FindLastEntity secondEntity = new FindLastEntity();
    secondEntity.setName( "bot_#second" );
    secondEntity.setAge( 30 );

    Backendless.Persistence.save( firstEntity );
    Thread.sleep( 3000 );
    Backendless.Persistence.save( secondEntity );

    FindLastEntity foundEntity = Backendless.Persistence.of( FindLastEntity.class ).findLast();

    Assert.assertEquals( "Server found unexpected entity", secondEntity, foundEntity );
  }

  @Test
  public void testFindLastUnknownEntity() throws Throwable
  {
    try
    {
      Backendless.Persistence.of( this.getClass() ).findLast();
      logTestFailed( "Server didn't throw an exception" );
    }
    catch( Throwable e )
    {
      checkErrorCode( 1009, e );
    }
  }

  @Test
  public void testFindFirstEntityInEmptyTable() throws Throwable
  {
    FindEmptyTableEntity entity = new FindEmptyTableEntity();
    entity.setName( "bot_#first" );
    entity.setAge( 20 );

    IDataStore<FindEmptyTableEntity> connection = Backendless.Persistence.of( FindEmptyTableEntity.class );

    FindEmptyTableEntity foundEntity = connection.save( entity );
    connection.remove( foundEntity );

    try
    {
      connection.findFirst();
      logTestFailed( "Server didn't throw an exception" );
    }
    catch( Throwable e )
    {
      checkErrorCode( 1010, e );
    }
  }

  @Test
  public void testFindLastEntityInEmptyTable() throws Throwable
  {
    FindEmptyTableEntity entity = new FindEmptyTableEntity();
    entity.setName( "bot_#last" );
    entity.setAge( 20 );

    IDataStore<FindEmptyTableEntity> connection = Backendless.Persistence.of( FindEmptyTableEntity.class );

    FindEmptyTableEntity foundEntity = connection.save( entity );
    connection.remove( foundEntity );

    try
    {
      Object o = connection.findLast();
      logTestFailed( "Server didn't throw an exception" );
    }
    catch( Throwable e )
    {
      checkErrorCode( 1010, e );
    }
  }
}
