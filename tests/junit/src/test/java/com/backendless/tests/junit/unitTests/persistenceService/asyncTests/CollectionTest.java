package com.backendless.tests.junit.unitTests.persistenceService.asyncTests;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.collectionEntities.GetPageEntityAsync;
import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.collectionEntities.NextPageEntityAsync;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CollectionTest extends TestsFrame
{
  @Test
  public void testCollectionNextPage()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final List<NextPageEntityAsync> nextPageEntities = new ArrayList<NextPageEntityAsync>();
        int n = 20;
        final CountDownLatch latch = new CountDownLatch( n );

        for( int i = 10; i < 30; i++ )
        {
          NextPageEntityAsync entity = new NextPageEntityAsync();
          entity.setName( "name#" + i );
          entity.setAge( 20 + i );

          Backendless.Persistence.save( entity, new ResponseCallback<NextPageEntityAsync>()
          {
            @Override
            public void handleResponse( NextPageEntityAsync response )
            {
              latch.countDown();
            }
          } );

          if( i >= 20 )
          {
            nextPageEntities.add( entity );
          }

          try
          {
            Thread.sleep( 1000 );
          }
          catch( InterruptedException e )
          {
            failCountDownWith( e );
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

        BackendlessDataQuery dataQuery = new BackendlessDataQuery( new QueryOptions( 10, 0, "age" ) );
        Backendless.Persistence.of( NextPageEntityAsync.class ).find( dataQuery, new ResponseCallback<BackendlessCollection<NextPageEntityAsync>>()
        {
          @Override
          public void handleResponse( BackendlessCollection<NextPageEntityAsync> response )
          {
            response.nextPage( new ResponseCallback<BackendlessCollection<NextPageEntityAsync>>()
            {
              @Override
              public void handleResponse( BackendlessCollection<NextPageEntityAsync> collection )
              {
                try
                {
                  Assert.assertNotNull( "Next page returned a null object", collection );
                  Assert.assertNotNull( "Next page contained a wrong data size", collection.getCurrentPage() );
                  Assert.assertEquals( "Next page returned a wrong size", nextPageEntities.size(), collection.getCurrentPage().size() );

                  for( NextPageEntityAsync entity : nextPageEntities )
                  {
                    Assert.assertTrue( "Server result didn't contain expected entity", collection.getCurrentPage().contains( entity ) );
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
  public void testCollectionGetPage()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final List<GetPageEntityAsync> getPageEntities = new ArrayList<GetPageEntityAsync>();
        int n = 20;
        final CountDownLatch latch = new CountDownLatch( n );

        for( int i = 10; i < 30; i++ )
        {
          GetPageEntityAsync entity = new GetPageEntityAsync();
          entity.setName( "name#" + i );
          entity.setAge( 20 + i );

          Backendless.Persistence.save( entity, new ResponseCallback<GetPageEntityAsync>()
          {
            @Override
            public void handleResponse( GetPageEntityAsync response )
            {
              latch.countDown();
            }
          } );

          if( i > 19 && i < 30 )
          {
            getPageEntities.add( entity );
          }

          try
          {
            Thread.sleep( 1000 );
          }
          catch( InterruptedException e )
          {
            failCountDownWith( e );
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

        BackendlessDataQuery dataQuery = new BackendlessDataQuery( new QueryOptions( 10, 0, "age" ) );
        Backendless.Persistence.of( GetPageEntityAsync.class ).find( dataQuery, new ResponseCallback<BackendlessCollection<GetPageEntityAsync>>()
        {
          @Override
          public void handleResponse( BackendlessCollection<GetPageEntityAsync> response )
          {
            response.getPage( 10, 10, new ResponseCallback<BackendlessCollection<GetPageEntityAsync>>()
            {
              @Override
              public void handleResponse( BackendlessCollection<GetPageEntityAsync> collection )
              {
                try
                {
                  Assert.assertNotNull( "Next page returned a null object", collection );
                  Assert.assertNotNull( "Next page contained a wrong data size", collection.getCurrentPage() );
                  Assert.assertEquals( "Next page returned a wrong size", getPageEntities.size(), collection.getCurrentPage().size() );

                  for( GetPageEntityAsync entity : getPageEntities )
                  {
                    Assert.assertTrue( "Server result didn't contain expected entity", collection.getCurrentPage().contains( entity ) );
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
}
