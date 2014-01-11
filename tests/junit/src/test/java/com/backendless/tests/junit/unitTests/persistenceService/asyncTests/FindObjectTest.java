package com.backendless.tests.junit.unitTests.persistenceService.asyncTests;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.IDataStore;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.findEntities.*;
import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.primitiveEntities.StringEntityAsync;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class FindObjectTest extends TestsFrame
{
  @Test
  public void testFindRecordById() throws Throwable
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
          public void handleResponse( final StringEntityAsync savedEntity )
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

            Backendless.Persistence.of( StringEntityAsync.class ).findById( savedEntity.getObjectId(), new ResponseCallback<StringEntityAsync>()
            {
              @Override
              public void handleResponse( StringEntityAsync foundEntity )
              {
                try
                {
                  Assert.assertEquals( "Found object contain wrong created date", savedEntity.getCreated(), foundEntity.getCreated() );
                  Assert.assertEquals( "Found object contain wrong objectId", savedEntity.getObjectId(), foundEntity.getObjectId() );
                  Assert.assertEquals( "Found object contain wrong field value", savedEntity.getStringEntity(), foundEntity.getStringEntity() );
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
  public void testRetrieveObjectWithWrongObjectId()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.Persistence.save( new StringEntityAsync(), new ResponseCallback<StringEntityAsync>()
        {
          @Override
          public void handleResponse( StringEntityAsync response )
          {
            Backendless.Persistence.of( StringEntityAsync.class ).findById( "foobar", new AsyncCallback<StringEntityAsync>()
            {
              @Override
              public void handleResponse( StringEntityAsync response )
              {
                failCountDownWith( "Server didn't throw an exception" );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( 1000, fault );
              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testFindAllEntities()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final List<FindAllEntityAsync> entities = new ArrayList();
        final CountDownLatch latch = new CountDownLatch( 10 );

        for( int i = 0; i < 10; i++ )
        {
          FindAllEntityAsync findAllEntityAsync = new FindAllEntityAsync();
          findAllEntityAsync.setName( "bot_#" + i );
          findAllEntityAsync.setAge( 20 + i );

          Backendless.Persistence.save( findAllEntityAsync, new ResponseCallback<FindAllEntityAsync>()
          {
            @Override
            public void handleResponse( FindAllEntityAsync response )
            {
              latch.countDown();
            }
          } );
          entities.add( findAllEntityAsync );
        }
        try
        {
          latch.await( 20, TimeUnit.SECONDS );
        }
        catch( InterruptedException e )
        {
          failCountDownWith( e );
        }

        Backendless.Persistence.of( FindAllEntityAsync.class ).find( new ResponseCallback<BackendlessCollection<FindAllEntityAsync>>()
        {
          @Override
          public void handleResponse( BackendlessCollection<FindAllEntityAsync> backendlessCollection )
          {
            assertArgumentAndResultCollections( entities, backendlessCollection );
            countDown();
          }
        } );
      }
    } );
  }

  @Test
  public void testFindWithWhereEntities()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final List<FindWithWhereEntityAsync> entities = new ArrayList();
        final CountDownLatch latch = new CountDownLatch( 10 );

        for( int i = 0; i < 10; i++ )
        {
          FindWithWhereEntityAsync findWithWhereEntityAsync = new FindWithWhereEntityAsync();
          findWithWhereEntityAsync.setName( "bot_#" + i );
          findWithWhereEntityAsync.setAge( 20 + i );

          Backendless.Persistence.save( findWithWhereEntityAsync, new ResponseCallback<FindWithWhereEntityAsync>()
          {
            @Override
            public void handleResponse( FindWithWhereEntityAsync response )
            {
              latch.countDown();
            }
          } );

          if( i < 5 )
          {
            entities.add( findWithWhereEntityAsync );
          }
        }
        try
        {
          latch.await( 20, TimeUnit.SECONDS );
        }
        catch( InterruptedException e )
        {
          failCountDownWith( e );
        }

        BackendlessDataQuery dataQuery = new BackendlessDataQuery( "age < 25" );
        Backendless.Persistence.of( FindWithWhereEntityAsync.class ).find( dataQuery, new ResponseCallback<BackendlessCollection<FindWithWhereEntityAsync>>()
        {
          @Override
          public void handleResponse( BackendlessCollection<FindWithWhereEntityAsync> backendlessCollection )
          {
            assertArgumentAndResultCollections( entities, backendlessCollection );
            countDown();
          }
        } );
      }
    } );
  }

  @Test
  public void testFindWithNegativeOffset()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.Persistence.of( Object.class ).find( new BackendlessDataQuery( new QueryOptions( 2, -1 ) ), new AsyncCallback<BackendlessCollection<Object>>()
        {
          @Override
          public void handleResponse( BackendlessCollection<Object> response )
          {
            failCountDownWith( "Client accepted a negative offset" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( ExceptionMessage.WRONG_OFFSET, fault );
          }
        } );
      }
    } );
  }

  @Test
  public void testFindWithNegativePageSize()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.Persistence.of( Object.class ).find( new BackendlessDataQuery( new QueryOptions( -1, 2 ) ), new AsyncCallback<BackendlessCollection<Object>>()
        {
          @Override
          public void handleResponse( BackendlessCollection<Object> response )
          {
            failCountDownWith( "Client accepted a negative pagesize" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( ExceptionMessage.WRONG_PAGE_SIZE, fault );
          }
        } );
      }
    } );
  }

  @Test
  public void testFindWithMissingProperties()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        FindWithPropertiesEntityAsync findWithPropertiesEntityAsync = new FindWithPropertiesEntityAsync();
        findWithPropertiesEntityAsync.setName( "bot_#foobar" );
        findWithPropertiesEntityAsync.setAge( 20 );
        Backendless.Persistence.save( findWithPropertiesEntityAsync, new ResponseCallback<FindWithPropertiesEntityAsync>()
        {
          @Override
          public void handleResponse( FindWithPropertiesEntityAsync response )
          {
            List<String> properties = new ArrayList<String>();
            properties.add( "foobar" );

            BackendlessDataQuery dataQuery = new BackendlessDataQuery( properties );
            Backendless.Persistence.of( FindWithPropertiesEntityAsync.class ).find( dataQuery, new AsyncCallback<BackendlessCollection<FindWithPropertiesEntityAsync>>()
            {
              @Override
              public void handleResponse( BackendlessCollection<FindWithPropertiesEntityAsync> response )
              {
                failCountDownWith( "Server didn't throw an exception" );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( 1006, fault );
              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testFindWithNullPropertiesQuery()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        FindWithPropertiesEntityAsync findWithPropertiesEntityAsync = new FindWithPropertiesEntityAsync();
        findWithPropertiesEntityAsync.setName( "bot_#foobar" );
        findWithPropertiesEntityAsync.setAge( 20 );
        Backendless.Persistence.save( findWithPropertiesEntityAsync, new ResponseCallback<FindWithPropertiesEntityAsync>()
        {
          @Override
          public void handleResponse( FindWithPropertiesEntityAsync response )
          {
            List<String> properties = null;
            BackendlessDataQuery dataQuery = new BackendlessDataQuery( properties );

            Backendless.Persistence.of( FindWithPropertiesEntityAsync.class ).find( dataQuery, new ResponseCallback<BackendlessCollection<FindWithPropertiesEntityAsync>>()
            {
              @Override
              public void handleResponse( BackendlessCollection<FindWithPropertiesEntityAsync> backendlessCollection )
              {
                try
                {
                  Assert.assertTrue( "Server found wrong number of objects", backendlessCollection.getTotalObjects() > 0 );
                  Assert.assertTrue( "Server returned wrong number of objects", backendlessCollection.getCurrentPage().size() > 0 );

                  for( FindWithPropertiesEntityAsync entity : backendlessCollection.getCurrentPage() )
                  {
                    Assert.assertTrue( "Server result contained wrong age field value", entity.getAge() > 0 );
                    Assert.assertNotNull( "Server result contained non null field", entity.getName() );
                    Assert.assertNotNull( "Server result contained non null field", entity.getObjectId() );
                    Assert.assertNotNull( "Server result contained non null field", entity.getCreated() );
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
  public void testFindWithExtraPropertiesQuery()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        FindWithPropertiesEntityAsync findWithPropertiesEntityAsync = new FindWithPropertiesEntityAsync();
        findWithPropertiesEntityAsync.setName( "bot_#foobar" );
        findWithPropertiesEntityAsync.setAge( 20 );
        Backendless.Persistence.save( findWithPropertiesEntityAsync, new ResponseCallback<FindWithPropertiesEntityAsync>()
        {
          @Override
          public void handleResponse( FindWithPropertiesEntityAsync response )
          {
            List<String> properties = new ArrayList<String>();
            properties.add( "age" );
            properties.add( "foobar" );
            BackendlessDataQuery dataQuery = new BackendlessDataQuery( properties );

            Backendless.Persistence.of( FindWithPropertiesEntityAsync.class ).find( dataQuery, new ResponseCallback<BackendlessCollection<FindWithPropertiesEntityAsync>>()
            {
              @Override
              public void handleResponse( BackendlessCollection<FindWithPropertiesEntityAsync> backendlessCollection )
              {
                try
                {
                  Assert.assertTrue( "Server found wrong number of objects", backendlessCollection.getTotalObjects() > 0 );
                  Assert.assertTrue( "Server returned wrong number of objects", backendlessCollection.getCurrentPage().size() > 0 );

                  for( FindWithPropertiesEntityAsync entity : backendlessCollection.getCurrentPage() )
                  {
                    Assert.assertTrue( "Server result contained wrong age field value", entity.getAge() > 0 );
                    Assert.assertNull( "Server result contained non null field", entity.getName() );
                    Assert.assertNull( "Server result contained non null field", entity.getObjectId() );
                    Assert.assertNull( "Server result contained non null field", entity.getCreated() );
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
  public void testFindFirstEntity()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final FindFirstEntityAsync findFirstEntityAsync = new FindFirstEntityAsync();
        findFirstEntityAsync.setName( "bot_#first" );
        findFirstEntityAsync.setAge( 20 );

        final FindFirstEntityAsync secondEntityAsync = new FindFirstEntityAsync();
        secondEntityAsync.setName( "bot_#second" );
        secondEntityAsync.setAge( 30 );

        Backendless.Persistence.save( findFirstEntityAsync, new ResponseCallback<FindFirstEntityAsync>()
        {
          @Override
          public void handleResponse( FindFirstEntityAsync response )
          {
            try
            {
              Thread.sleep( 3000 );
            }
            catch( InterruptedException e )
            {
              failCountDownWith( e );
            }

            Backendless.Persistence.save( secondEntityAsync, new ResponseCallback<FindFirstEntityAsync>()
            {
              @Override
              public void handleResponse( FindFirstEntityAsync response )
              {
                Backendless.Persistence.of( FindFirstEntityAsync.class ).findFirst( new ResponseCallback<FindFirstEntityAsync>()
                {
                  @Override
                  public void handleResponse( FindFirstEntityAsync foundEntityAsync )
                  {
                    try
                    {
                      Assert.assertEquals( "Server found unexpected entity", findFirstEntityAsync, foundEntityAsync );
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
  public void testFindFirstUnknownEntity()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.Persistence.of( FindObjectTest.class ).findFirst( new AsyncCallback<FindObjectTest>()
        {
          @Override
          public void handleResponse( FindObjectTest response )
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

  @Test
  public void testFindLastEntity()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        FindLastEntityAsync firstEntityAsync = new FindLastEntityAsync();
        firstEntityAsync.setName( "bot_#first" );
        firstEntityAsync.setAge( 20 );

        final FindLastEntityAsync secondEntityAsync = new FindLastEntityAsync();
        secondEntityAsync.setName( "bot_#second" );
        secondEntityAsync.setAge( 30 );

        Backendless.Persistence.save( firstEntityAsync, new ResponseCallback<FindLastEntityAsync>()
        {
          @Override
          public void handleResponse( FindLastEntityAsync response )
          {
            try
            {
              Thread.sleep( 3000 );
            }
            catch( InterruptedException e )
            {
              failCountDownWith( e );
            }

            Backendless.Persistence.save( secondEntityAsync, new ResponseCallback<FindLastEntityAsync>()
            {
              @Override
              public void handleResponse( FindLastEntityAsync response )
              {
                Backendless.Persistence.of( FindLastEntityAsync.class ).findLast( new ResponseCallback<FindLastEntityAsync>()
                {
                  @Override
                  public void handleResponse( FindLastEntityAsync foundEntityAsync )
                  {
                    try
                    {
                      Assert.assertEquals( "Server found unexpected entity", secondEntityAsync, foundEntityAsync );
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
  public void testFindLastUnknownEntity()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.Persistence.of( FindObjectTest.class ).findLast( new AsyncCallback<FindObjectTest>()
        {
          @Override
          public void handleResponse( FindObjectTest response )
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

  @Test
  public void testFindFirstEntityInEmptyTable()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        FindEmptyTableEntityAsync entity = new FindEmptyTableEntityAsync();
        entity.setName( "bot_#first" );
        entity.setAge( 20 );

        final IDataStore<FindEmptyTableEntityAsync> connection = Backendless.Persistence.of( FindEmptyTableEntityAsync.class );

        connection.save( entity, new ResponseCallback<FindEmptyTableEntityAsync>()
        {
          @Override
          public void handleResponse( FindEmptyTableEntityAsync foundEntity )
          {
            connection.remove( foundEntity, new ResponseCallback<Long>()
            {
              @Override
              public void handleResponse( Long response )
              {
                connection.findFirst( new AsyncCallback<FindEmptyTableEntityAsync>()
                {
                  @Override
                  public void handleResponse( FindEmptyTableEntityAsync response )
                  {
                    failCountDownWith( "Server didn't throw an exception" );
                  }

                  @Override
                  public void handleFault( BackendlessFault fault )
                  {
                    checkErrorCode( 1010, fault );
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
  public void testFindLastEntityInEmptyTable()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        FindEmptyTableEntityAsync entity = new FindEmptyTableEntityAsync();
        entity.setName( "bot_#last" );
        entity.setAge( 20 );

        final IDataStore<FindEmptyTableEntityAsync> connection = Backendless.Persistence.of( FindEmptyTableEntityAsync.class );

        connection.save( entity, new ResponseCallback<FindEmptyTableEntityAsync>()
        {
          @Override
          public void handleResponse( FindEmptyTableEntityAsync foundEntity )
          {
            connection.remove( foundEntity, new ResponseCallback<Long>()
            {
              @Override
              public void handleResponse( Long response )
              {
                connection.findLast( new AsyncCallback<FindEmptyTableEntityAsync>()
                {
                  @Override
                  public void handleResponse( FindEmptyTableEntityAsync response )
                  {
                    failCountDownWith( "Server didn't throw an exception" );
                  }

                  @Override
                  public void handleFault( BackendlessFault fault )
                  {
                    checkErrorCode( 1010, fault );
                  }
                } );
              }
            } );
          }
        } );
      }
    } );
  }
}
