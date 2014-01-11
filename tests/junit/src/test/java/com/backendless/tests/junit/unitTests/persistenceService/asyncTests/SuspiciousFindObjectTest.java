/*
 * ********************************************************************************************************************
 *  <p/>
 *  BACKENDLESS.COM CONFIDENTIAL
 *  <p/>
 *  ********************************************************************************************************************
 *  <p/>
 *  Copyright 2012 BACKENDLESS.COM. All Rights Reserved.
 *  <p/>
 *  NOTICE: All information contained herein is, and remains the property of Backendless.com and its suppliers,
 *  if any. The intellectual and technical concepts contained herein are proprietary to Backendless.com and its
 *  suppliers and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret
 *  or copyright law. Dissemination of this information or reproduction of this material is strictly forbidden
 *  unless prior written permission is obtained from Backendless.com.
 *  <p/>
 *  ********************************************************************************************************************
 */

package com.backendless.tests.junit.unitTests.persistenceService.asyncTests;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.findEntities.FindBasePropertiesAsync;
import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.findEntities.FindTooBigOffsetAsync;
import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.findEntities.FindTooBigPagesizeAsync;
import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.findEntities.FindWithPageSizeAndOffsetEntityAsync;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SuspiciousFindObjectTest extends TestsFrame
{
  //This tests are failing if move to main FindObjectTest class
  //Needs investigation

  @Test
  public void testFindWithPageSizeAndOffset()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final List<FindWithPageSizeAndOffsetEntityAsync> entities = new ArrayList();
        final CountDownLatch latch = new CountDownLatch( 10 );

        for( int i = 0; i < 10; i++ )
        {
          FindWithPageSizeAndOffsetEntityAsync findWithPageSizeAndOffsetEntityAsync = new FindWithPageSizeAndOffsetEntityAsync();
          findWithPageSizeAndOffsetEntityAsync.setName( "bot_#" + i );
          findWithPageSizeAndOffsetEntityAsync.setAge( 20 + i );

          Backendless.Persistence.save( findWithPageSizeAndOffsetEntityAsync, new ResponseCallback<FindWithPageSizeAndOffsetEntityAsync>()
          {
            @Override
            public void handleResponse( FindWithPageSizeAndOffsetEntityAsync response )
            {
              latch.countDown();
            }
          } );

          if( i == 1 || i == 2 )
          {
            entities.add( findWithPageSizeAndOffsetEntityAsync );
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

        BackendlessDataQuery dataQuery = new BackendlessDataQuery( new QueryOptions( 2, 1, "age" ) );
        Backendless.Persistence.of( FindWithPageSizeAndOffsetEntityAsync.class ).find( dataQuery, new ResponseCallback<BackendlessCollection<FindWithPageSizeAndOffsetEntityAsync>>()
        {
          @Override
          public void handleResponse(
                  BackendlessCollection<FindWithPageSizeAndOffsetEntityAsync> backendlessCollection )
          {
            try
            {
              Assert.assertEquals( "Server returned wrong number of objects", entities.size(), backendlessCollection.getCurrentPage().size() );

              for( FindWithPageSizeAndOffsetEntityAsync entity : entities )
              {
                Assert.assertTrue( "Server result didn't contain expected entity", backendlessCollection.getCurrentPage().contains( entity ) );
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
  public void testFindWithTooBigOffset()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        FindTooBigOffsetAsync findTooBigOffsetAsync = new FindTooBigOffsetAsync();
        findTooBigOffsetAsync.setName( "bot_#toobigoffset" );
        findTooBigOffsetAsync.setAge( 20 );
        Backendless.Persistence.save( findTooBigOffsetAsync, new ResponseCallback<FindTooBigOffsetAsync>()
        {
          @Override
          public void handleResponse( FindTooBigOffsetAsync response )
          {
            Backendless.Persistence.of( FindTooBigOffsetAsync.class ).find( new ResponseCallback<BackendlessCollection<FindTooBigOffsetAsync>>()
            {
              @Override
              public void handleResponse( BackendlessCollection<FindTooBigOffsetAsync> response )
              {
                final int totalObjects = response.getTotalObjects();
                BackendlessDataQuery dataQuery = new BackendlessDataQuery( new QueryOptions( 1, totalObjects + 1 ) );

                Backendless.Persistence.of( FindTooBigOffsetAsync.class ).find( dataQuery, new AsyncCallback<BackendlessCollection<FindTooBigOffsetAsync>>()
                {
                  @Override
                  public void handleResponse( BackendlessCollection<FindTooBigOffsetAsync> result )
                  {
                    Assert.assertEquals( "Server return wrong result", 0, result.getCurrentPage().size() );
                  }

                  @Override
                  public void handleFault( BackendlessFault fault )
                  {
                    checkErrorCode( 1004, fault );
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
  public void testFindWithTooBigPagesize()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        FindTooBigPagesizeAsync findTooBigPagesizeAsync = new FindTooBigPagesizeAsync();
        findTooBigPagesizeAsync.setName( "bot_#toobigpagesize" );
        findTooBigPagesizeAsync.setAge( 20 );
        Backendless.Persistence.save( findTooBigPagesizeAsync, new ResponseCallback<FindTooBigPagesizeAsync>()
        {
          @Override
          public void handleResponse( FindTooBigPagesizeAsync response )
          {
            BackendlessDataQuery dataQuery = new BackendlessDataQuery( new QueryOptions( 101, 0 ) );
            Backendless.Persistence.of( FindTooBigPagesizeAsync.class ).find( dataQuery, new AsyncCallback<BackendlessCollection<FindTooBigPagesizeAsync>>()
            {
              @Override
              public void handleResponse( BackendlessCollection<FindTooBigPagesizeAsync> result )
              {
                failCountDownWith( "Server didn't throw an exception. Requested pagesize 101, but found " + result.getCurrentPage().size() );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( 1005, fault );
              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testFindWithProperties()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final List<Integer> entities = new ArrayList();
        final CountDownLatch latch = new CountDownLatch( 20 );

        for( int i = 0; i < 20; i++ )
        {
          FindBasePropertiesAsync findBaseProperties = new FindBasePropertiesAsync();
          findBaseProperties.setName( "bot_#" + i );
          int age = 20 + i;
          entities.add( age );
          findBaseProperties.setAge( age );
          Backendless.Persistence.save( findBaseProperties, new ResponseCallback<FindBasePropertiesAsync>()
          {
            @Override
            public void handleResponse( FindBasePropertiesAsync response )
            {
              latch.countDown();
            }
          } );
        }
        try
        {
          latch.await( 20, TimeUnit.SECONDS );
        }
        catch( InterruptedException e )
        {
          failCountDownWith( e );
        }

        List<String> properties = new ArrayList();
        properties.add( "age" );

        BackendlessDataQuery dataQuery = new BackendlessDataQuery( properties );
        QueryOptions queryOptions = new QueryOptions( 20, 0, "age" );
        dataQuery.setQueryOptions( queryOptions );

        Backendless.Persistence.of( FindBasePropertiesAsync.class ).find( dataQuery, new ResponseCallback<BackendlessCollection<FindBasePropertiesAsync>>()
        {
          @Override
          public void handleResponse( BackendlessCollection<FindBasePropertiesAsync> backendlessCollection )
          {
            try
            {
              Assert.assertEquals( "Server found wrong number of objects", entities.size(), backendlessCollection.getTotalObjects() );
              Assert.assertEquals( "Server returned wrong number of objects", entities.size(), backendlessCollection.getCurrentPage().size() );

              for( FindBasePropertiesAsync entity : backendlessCollection.getCurrentPage() )
              {
                Assert.assertTrue( "Server result contained wrong age field value", entities.contains( entity.getAge() ) );
                entities.remove( (Integer) entity.getAge() );
                Assert.assertNull( "Server result contained non null name", entity.getName() );
                Assert.assertNull( "Server result contained non null objectid", entity.getObjectId() );
                Assert.assertNull( "Server result contained non null created date", entity.getCreated() );
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
}
