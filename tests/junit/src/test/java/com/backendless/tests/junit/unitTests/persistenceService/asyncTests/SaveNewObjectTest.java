package com.backendless.tests.junit.unitTests.persistenceService.asyncTests;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.UniqueAndroidPersonAsync;
import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.primitiveEntities.*;
import com.backendless.tests.junit.unitTests.persistenceService.multilevelTestEntities.AddressNew;
import com.backendless.tests.junit.unitTests.persistenceService.multilevelTestEntities.Employee;
import com.backendless.tests.junit.unitTests.persistenceService.multilevelTestEntities.Organization;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SaveNewObjectTest extends TestsFrame
{
  @Test
  public void testSaveEntityToANewDataBase()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final UniqueAndroidPersonAsync uniqueAndroidPersonAsync = new UniqueAndroidPersonAsync();
        uniqueAndroidPersonAsync.setAge( 16 );
        uniqueAndroidPersonAsync.setName( "John" );
        uniqueAndroidPersonAsync.setBirthday( Calendar.getInstance().getTime() );

        Backendless.Persistence.save( uniqueAndroidPersonAsync, new ResponseCallback<UniqueAndroidPersonAsync>()
        {
          @Override
          public void handleResponse( UniqueAndroidPersonAsync savedUniqueAndroidPerson )
          {
            try
            {
              Assert.assertNotNull( "Server returned a null result", savedUniqueAndroidPerson );
              Assert.assertNotNull( "Returned object doesn't have expected field", savedUniqueAndroidPerson.getName() );
              Assert.assertNotNull( "Returned object doesn't have expected field id", savedUniqueAndroidPerson.getObjectId() );
              Assert.assertNotNull( "Returned object doesn't have expected field created", savedUniqueAndroidPerson.getCreated() );
              Assert.assertEquals( "Returned object has wrong field value", uniqueAndroidPersonAsync.getName(), savedUniqueAndroidPerson.getName() );
              Assert.assertEquals( "Returned object has wrong field value", uniqueAndroidPersonAsync.getAge(), savedUniqueAndroidPerson.getAge() );
              Assert.assertTrue( "Returned object has wrong field value", (savedUniqueAndroidPerson.getBirthday().getTime() - uniqueAndroidPersonAsync.getBirthday().getTime()) < 1000 );
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
  public void testSaveNullEntity()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.Persistence.save( null, new AsyncCallback<Object>()
        {
          @Override
          public void handleResponse( Object response )
          {
            failCountDownWith( "Client saved a null entity" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( ExceptionMessage.NULL_ENTITY, fault );
          }
        } );
      }
    } );
  }

  @Test
  public void testSaveStringEntity() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final StringEntityAsync entity = new StringEntityAsync();
        entity.setStringEntity( "foobar" );

        Backendless.Persistence.save( entity, new ResponseCallback<StringEntityAsync>()
        {
          @Override
          public void handleResponse( StringEntityAsync savedEntity )
          {
            try
            {
              Assert.assertNotNull( "Server returned a null result", savedEntity );
              Assert.assertNotNull( "Returned object doesn't have expected field", savedEntity.getStringEntity() );
              Assert.assertNotNull( "Returned object doesn't have expected field id", savedEntity.getObjectId() );
              Assert.assertNotNull( "Returned object doesn't have expected field created", savedEntity.getCreated() );
              Assert.assertEquals( "Returned object has wrong field value", entity.getStringEntity(), savedEntity.getStringEntity() );
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
  public void testSaveBooleanEntity() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final BooleanEntityAsync entity = new BooleanEntityAsync();
        entity.setBooleanField( false );

        Backendless.Persistence.save( entity, new ResponseCallback<BooleanEntityAsync>()
        {
          @Override
          public void handleResponse( BooleanEntityAsync savedEntity )
          {
            try
            {
              Assert.assertNotNull( "Server returned a null result", savedEntity );
              Assert.assertNotNull( "Returned object doesn't have expected field id", savedEntity.getObjectId() );
              Assert.assertNotNull( "Returned object doesn't have expected field created", savedEntity.getCreated() );
              Assert.assertEquals( "Returned object has wrong field value", entity.isBooleanField(), savedEntity.isBooleanField() );
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
  public void testSaveDateEntity() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final DateEntityAsync entity = new DateEntityAsync();
        entity.setDateField( Calendar.getInstance().getTime() );

        Backendless.Persistence.save( entity, new ResponseCallback<DateEntityAsync>()
        {
          @Override
          public void handleResponse( DateEntityAsync savedEntity )
          {
            try
            {
              Assert.assertNotNull( "Server returned a null result", savedEntity );
              Assert.assertNotNull( "Returned object doesn't have expected field", savedEntity.getDateField() );
              Assert.assertNotNull( "Returned object doesn't have expected field id", savedEntity.getObjectId() );
              Assert.assertNotNull( "Returned object doesn't have expected field created", savedEntity.getCreated() );
              Assert.assertTrue( "Returned object has wrong field value", savedEntity.getDateField().getTime() - entity.getDateField().getTime() < 1000 );
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
  public void testSaveIntEntity() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final IntEntityAsync entity = new IntEntityAsync();
        entity.setIntField( 16 );

        Backendless.Persistence.save( entity, new ResponseCallback<IntEntityAsync>()
        {
          @Override
          public void handleResponse( IntEntityAsync savedEntity )
          {
            try
            {
              Assert.assertNotNull( "Server returned a null result", savedEntity );
              Assert.assertNotNull( "Returned object doesn't have expected field id", savedEntity.getObjectId() );
              Assert.assertNotNull( "Returned object doesn't have expected field created", savedEntity.getCreated() );
              Assert.assertEquals( "Returned object has wrong field value", entity.getIntField(), savedEntity.getIntField() );
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
  public void testSaveDoubleEntity() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final DoubleEntityAsync entity = new DoubleEntityAsync();
        entity.setDoubleEntity( 16.1616d );

        Backendless.Persistence.save( entity, new ResponseCallback<DoubleEntityAsync>()
        {
          @Override
          public void handleResponse( DoubleEntityAsync savedEntity )
          {
            try
            {
              Assert.assertNotNull( "Server returned a null result", savedEntity );
              Assert.assertNotNull( "Returned object doesn't have expected field id", savedEntity.getObjectId() );
              Assert.assertNotNull( "Returned object doesn't have expected field created", savedEntity.getCreated() );
              Assert.assertEquals( "Returned object has wrong field value", entity.getDoubleEntity(), savedEntity.getDoubleEntity(), 0.000000d );
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
  public void testSaveCircularMultipleChildrenRelation() throws Throwable
  {
    Backendless.setUrl( "http://localhost:9000" );
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final List<PartEntity> related = new ArrayList<PartEntity>();
        final PartEntity intEntity1 = new PartEntity();
        final PartEntity intEntity2 = new PartEntity();

        related.add( intEntity1 );
        related.add( intEntity2 );

        final AggregatorAsync aggregate = new AggregatorAsync();
        aggregate.setEntities( related );
        intEntity1.setParentEntity( aggregate );
        intEntity2.setParentEntity( aggregate );

        Backendless.Persistence.save( aggregate, new ResponseCallback<AggregatorAsync>()
        {
          @Override
          public void handleResponse( AggregatorAsync savedEntity )
          {
            try
            {
              Assert.assertNotNull( "Server returned a null result", savedEntity );
              Assert.assertNotNull( "Returned object doesn't have expected field id", savedEntity.getObjectId() );
              Assert.assertNotNull( "Returned object doesn't have expected field created", savedEntity.getCreated() );
              Assert.assertTrue( "Related objects was not saved", related.size() == aggregate.getEntities().size() );
              Assert.assertTrue( intEntity1.equals( aggregate.getEntities().get( 0 ) ) );
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
  public void testSaveCircularEntity() throws Throwable
  {
    Backendless.setUrl( "http://localhost:9000" );
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final PartEntity intEntity = new PartEntity();

        final AggregatorAsync aggregate = new AggregatorAsync();
        aggregate.setEntity( intEntity );
        intEntity.setParentEntity( aggregate );

        Backendless.Persistence.save( aggregate, new ResponseCallback<AggregatorAsync>()
        {
          @Override
          public void handleResponse( AggregatorAsync savedEntity )
          {
            try
            {
              Assert.assertNotNull( "Server returned a null result", savedEntity );
              Assert.assertNotNull( "Returned object doesn't have expected field id", savedEntity.getObjectId() );
              Assert.assertNotNull( "Returned object doesn't have expected field created", savedEntity.getCreated() );
              Assert.assertTrue( intEntity.equals( aggregate.getEntity() ) );
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
  public void testSaveComplexEntity() throws Throwable
  {
    Backendless.setUrl( "http://localhost:9000" );
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final List<IntEntityAsync> related = new ArrayList<IntEntityAsync>();
        final IntEntityAsync intEntity = new IntEntityAsync();
        intEntity.setIntField( 1 );

        related.add( intEntity );

        final ComplexEntityAsync entity = new ComplexEntityAsync();
        entity.setDoubleEntity( 16.1616d );
        entity.setEntities( related );

        Backendless.Persistence.save( entity, new ResponseCallback<ComplexEntityAsync>()
        {
          @Override
          public void handleResponse( ComplexEntityAsync savedEntity )
          {
            try
            {
              Assert.assertNotNull( "Server returned a null result", savedEntity );
              Assert.assertNotNull( "Returned object doesn't have expected field id", savedEntity.getObjectId() );
              Assert.assertNotNull( "Returned object doesn't have expected field created", savedEntity.getCreated() );
              Assert.assertEquals( "Returned object has wrong field value", entity.getDoubleEntity(), savedEntity.getDoubleEntity(), 0.000000d );
              Assert.assertTrue( "Related objects was not saved", related.size() == entity.getEntities().size() );
              Assert.assertTrue( intEntity.equals( entity.getEntities().get( 0 ) ) );
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
  public void test3levelEntity() throws Throwable
  {
    Backendless.setUrl( "http://localhost:9000" );
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Organization organization = new Organization();

        Employee employee = new Employee();

        AddressNew jasonsAddress = new AddressNew();
        jasonsAddress.setCity( "New York" );
        jasonsAddress.setStreet( "Lincoln Pl" );
        jasonsAddress.setHouse( "1460" );

        employee.setAddress( jasonsAddress );

        organization.addEmployee( employee );

        Backendless.Persistence.save( organization, new ResponseCallback<Organization>()
        {
          @Override
          public void handleResponse( Organization savedEntity )
          {
            try
            {
              Assert.assertNotNull( "Server returned a null result", savedEntity );
              Employee object = savedEntity.getEmployees().get( 0 );
              Assert.assertNotNull( "Returned object doesn't have expected child", object );
              AddressNew address = object.getAddress();
              Assert.assertNotNull( "Returned object doesn't have expected child of second level", address );
              Assert.assertNull( "Returned object has wrong field value", address.getEmployee() );
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
  public void test3levelEntityWithCyclicRelations() throws Throwable
  {
    Backendless.setUrl( "http://localhost:9000" );
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Organization organization = new Organization();

        Employee employee = new Employee();

        AddressNew jasonsAddress = new AddressNew();
        jasonsAddress.setCity( "New York" );
        jasonsAddress.setStreet( "Lincoln Pl" );
        jasonsAddress.setHouse( "1460" );

        employee.setAddress( jasonsAddress );
        jasonsAddress.setEmployee( employee );

        organization.addEmployee( employee );

        Backendless.Persistence.save( organization, new ResponseCallback<Organization>()
        {
          @Override
          public void handleResponse( Organization savedEntity )
          {
            try
            {
              Assert.assertNotNull( "Server returned a null result", savedEntity );
              Employee object = savedEntity.getEmployees().get( 0 );
              Assert.assertNotNull( "Returned object doesn't have expected child", object );
              AddressNew address = object.getAddress();
              Assert.assertNotNull( "Returned object doesn't have expected child of second level", address );
              Employee innerEmployee = address.getEmployee();
              Assert.assertNotNull( "Returned object has wrong field value", innerEmployee );
              Assert.assertTrue( "Returned cyclic relations are invalid", innerEmployee == object );
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
}
