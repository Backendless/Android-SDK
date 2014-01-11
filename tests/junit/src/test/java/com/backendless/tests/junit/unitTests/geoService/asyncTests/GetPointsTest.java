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

package com.backendless.tests.junit.unitTests.geoService.asyncTests;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.geo.BackendlessGeoQuery;
import com.backendless.geo.GeoPoint;
import com.backendless.geo.SearchMatchesResult;
import com.backendless.geo.Units;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GetPointsTest extends TestsFrame
{
  @Test
  public void testGetPointsForRectangle() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        double startingLat = 10;
        double startingLong = 10;
        int maxPoints = 10;
        setDefinedCategory( getRandomCategory() );
        Map<String, String> meta = getRandomSimpleMetadata();
        final CountDownLatch latch = new CountDownLatch( maxPoints );

        for( int i = 0; i < maxPoints; i++ )
        {
          Backendless.Geo.savePoint( startingLat, startingLong + i, getDefinedCategories(), meta, new ResponseCallback<GeoPoint>()
          {
            @Override
            public void handleResponse( GeoPoint response )
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

        BackendlessGeoQuery geoQuery = new BackendlessGeoQuery( startingLat + 1, startingLong - 1, startingLat - 1, startingLong + maxPoints + 1 );
        getCollectionAndCheck( startingLat, startingLong, maxPoints, maxPoints, meta, geoQuery );
      }
    } );
  }

  @Test
  public void testGetPointsByMetadata() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        double startingLat = 20;
        double startingLong = 10;
        int maxPoints = 10;
        setDefinedCategory( getRandomCategory() );

        Map<String, String> meta = new HashMap<String, String>();
        meta.put( "object_type_sync", "office" );
        final CountDownLatch latch = new CountDownLatch( maxPoints );
        for( int i = 0; i < maxPoints; i++ )
        {
          Backendless.Geo.savePoint( startingLat, startingLong + i, getDefinedCategories(), meta,new ResponseCallback<GeoPoint>()
          {
            @Override
            public void handleResponse( GeoPoint response )
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

        getCollectionAndCheck( startingLat, startingLong, maxPoints, maxPoints, meta, new BackendlessGeoQuery( meta ) );
      }
    } );
  }

  @Test
  public void testGetPointsWithNegativeOffset() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.Geo.getPoints( new BackendlessGeoQuery( 50, 50, 10, -10 ), new AsyncCallback<BackendlessCollection<GeoPoint>>()
        {
          @Override
          public void handleResponse( BackendlessCollection<GeoPoint> response )
          {
            failCountDownWith( "Client send a query with negative offset" );
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
  public void testGetPointsWithBothRectAndRadiusQuery() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        BackendlessGeoQuery geoQuery = new BackendlessGeoQuery( 10d, 10d, 20d, 20d );
        geoQuery.setRadius( 10d );
        geoQuery.setLatitude( 10d );
        geoQuery.setLatitude( 10d );

        Backendless.Geo.getPoints( geoQuery, new AsyncCallback<BackendlessCollection<GeoPoint>>()
        {
          @Override
          public void handleResponse( BackendlessCollection<GeoPoint> response )
          {
            failCountDownWith( "Client send a query with both rectangle and radius search query" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( ExceptionMessage.INCONSISTENT_GEO_QUERY, fault );
          }
        } );
      }
    } );
  }

  @Test
  public void testGetPointsForCategory() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        double startingLat = 30;
        double startingLong = 10;
        int maxPoints = 10;
        setDefinedCategory( getRandomCategory() );
        Map<String, String> meta = getRandomSimpleMetadata();
        final CountDownLatch latch = new CountDownLatch( maxPoints );
        for( int i = 0; i < maxPoints; i++ )
        {
          Backendless.Geo.savePoint( startingLat, startingLong + i, getDefinedCategories(), meta, new ResponseCallback<GeoPoint>()
          {
            @Override
            public void handleResponse( GeoPoint response )
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

        getCollectionAndCheck( startingLat, startingLong, maxPoints, maxPoints, meta, new BackendlessGeoQuery( getDefinedCategories() ) );
      }
    } );
  }

  @Test
  public void testGetPointsForMultipleCategories() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        double startingLat = 80;
        double startingLong = 160;
        int maxPoints = 10;
        setDefinedCategories( getRandomCategoriesList( 10 ) );
        Map<String, String> meta = getRandomSimpleMetadata();
        final CountDownLatch latch = new CountDownLatch( maxPoints );
        for( int i = 0; i < maxPoints; i++ )
        {
          Backendless.Geo.savePoint( startingLat, startingLong + i, getDefinedCategories(), meta, new ResponseCallback<GeoPoint>()
          {
            @Override
            public void handleResponse( GeoPoint response )
            {
              latch.countDown();
            }
          } );
        }
        try
        {
          latch.await( 2, TimeUnit.MINUTES );
        }
        catch( InterruptedException e )
        {
          failCountDownWith( e );
        }

        getCollectionAndCheck( startingLat, startingLong, maxPoints, maxPoints, meta, new BackendlessGeoQuery( getDefinedCategories() ) );
      }
    } );
  }

  @Test
  public void testGetPointsWithoutMetadata() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        double startingLat = 50;
        double startingLong = 10;
        int maxPoints = 10;
        Map<String, String> meta = getRandomSimpleMetadata();
        setDefinedCategory( getRandomCategory() );
        final CountDownLatch latch = new CountDownLatch( maxPoints );
        for( int i = 0; i < maxPoints; i++ )
        {
          Backendless.Geo.savePoint( startingLat, startingLong + i, getDefinedCategories(), meta, new ResponseCallback<GeoPoint>()
          {
            @Override
            public void handleResponse( GeoPoint response )
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

        meta = null;
        BackendlessGeoQuery geoQuery = new BackendlessGeoQuery( getDefinedCategories() );
        geoQuery.setIncludeMeta( false );

        getCollectionAndCheck( startingLat, startingLong, maxPoints, maxPoints, meta, geoQuery );
      }
    } );
  }

  @Test
  public void testGetPointsWithOffsetGreaterThenPointsCount() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        double startingLat = 60;
        double startingLong = -60;
        int maxPoints = 1;
        setDefinedCategory( getRandomCategory() );
        Map<String, String> meta = getRandomSimpleMetadata();
        final CountDownLatch latch = new CountDownLatch( maxPoints );
        for( int i = 0; i < maxPoints; i++ )
        {
          Backendless.Geo.savePoint( startingLat, startingLong + i, getDefinedCategories(), meta, new ResponseCallback<GeoPoint>()
          {
            @Override
            public void handleResponse( GeoPoint response )
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

        BackendlessGeoQuery geoQuery = new BackendlessGeoQuery( getDefinedCategories() );
        geoQuery.setOffset( maxPoints * 2 );

        Backendless.Geo.getPoints( geoQuery, new AsyncCallback<BackendlessCollection<GeoPoint>>()
        {
          @Override
          public void handleResponse( BackendlessCollection<GeoPoint> response )
          {
            failCountDownWith( "Server accepted request" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( 4003, fault );
          }
        } );
      }
    } );
  }

  @Test
  public void testGetPointsByRadiusIn10Meters() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        double startingLat = 80;
        double startingLong = 10;
        int maxPoints = 10;
        double offset = 0;
        setDefinedCategory( getRandomCategory() );
        Map<String, String> meta = getRandomSimpleMetadata();
        final CountDownLatch latch = new CountDownLatch( maxPoints );
        for( int i = 0; i < maxPoints; i++ )
        {
          offset += METER;
          Backendless.Geo.savePoint( startingLat, startingLong + offset, getDefinedCategories(), meta, new ResponseCallback<GeoPoint>()
          {
            @Override
            public void handleResponse( GeoPoint response )
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

        BackendlessGeoQuery geoQuery = new BackendlessGeoQuery( startingLat, startingLong + (offset / 2), maxPoints, Units.METERS );
        getCollectionAndCheck( startingLat, startingLong, maxPoints, offset, meta, geoQuery );
      }
    } );
  }

  @Test
  public void testGetPointsByRadiusIn1Kilometer() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        double startingLat = 10;
        double startingLong = 15;
        int maxPoints = 10;
        double offset = 0;
        setDefinedCategory( getRandomCategory() );
        Map<String, String> meta = getRandomSimpleMetadata();
        final CountDownLatch latch = new CountDownLatch( maxPoints );
        for( int i = 0; i < maxPoints; i++ )
        {
          offset += METER * 100;
          Backendless.Geo.savePoint( startingLat, startingLong + offset, getDefinedCategories(), meta, new ResponseCallback<GeoPoint>()
          {
            @Override
            public void handleResponse( GeoPoint response )
            {
              latch.countDown();
            }
          } );
          try
          {
            latch.await( 20, TimeUnit.SECONDS );
          }
          catch( InterruptedException e )
          {
            failCountDownWith( e );
          }
        }

        BackendlessGeoQuery geoQuery = new BackendlessGeoQuery( startingLat, startingLong + offset / 2, 1, Units.KILOMETERS );
        getCollectionAndCheck( startingLat, startingLong, maxPoints, offset, meta, geoQuery );
      }
    } );
  }

  @Test
  public void testGetPointsByRadiusIn100Yards() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        double startingLat = 10;
        double startingLong = 30;
        int maxPoints = 10;
        double offset = 0;
        setDefinedCategory( getRandomCategory() );
        Map<String, String> meta = getRandomSimpleMetadata();
        final CountDownLatch latch = new CountDownLatch( maxPoints );
        for( int i = 0; i < maxPoints; i++ )
        {
          offset += METER * 0.914399998610112;
          Backendless.Geo.savePoint( startingLat, startingLong + offset, getDefinedCategories(), meta, new ResponseCallback<GeoPoint>()
          {
            @Override
            public void handleResponse( GeoPoint response )
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

        BackendlessGeoQuery geoQuery = new BackendlessGeoQuery( startingLat, startingLong + offset / 2, 100, Units.YARDS );
        getCollectionAndCheck( startingLat, startingLong, maxPoints, offset, meta, geoQuery );
      }
    } );
  }

  @Test
  public void testGetPointsWithRelativeSearch() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        double startingLat = 80;
        double startingLong = 160;
        final int maxPoints = 10;
        double offset = 0;
        setDefinedCategory( "relativeSearch" );
        final String key = "metaKey" + random.nextInt();
        final Map<String, String> relativeMeta = new HashMap<String, String>();
        final CountDownLatch latch = new CountDownLatch( maxPoints );

        for( int i = 0; i < maxPoints; i++ )
        {
          offset += METER * 0.914399998610112;
          final String meta = "someMeta" + random.nextInt() + i;
          Map<String, String> metaMap = new HashMap<String, String>();
          metaMap.put( key, meta );
          Backendless.Geo.savePoint( startingLat, startingLong + offset, getDefinedCategories(), metaMap, new ResponseCallback<GeoPoint>()
          {
            @Override
            public void handleResponse( GeoPoint response )
            {
              relativeMeta.put( key, meta );
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

        offset += METER * 0.914399998610112;
        String meta = "someMeta" + random.nextInt() + 16;
        Map<String, String> metaMap = new HashMap<String, String>();
        metaMap.put( key, meta );
        Backendless.Geo.savePoint( startingLat, startingLong + offset, getDefinedCategories(), metaMap, new ResponseCallback<GeoPoint>()
        {
          @Override
          public void handleResponse( GeoPoint response )
          {
            BackendlessGeoQuery geoQuery = new BackendlessGeoQuery( relativeMeta, maxPoints );
            Backendless.Geo.relativeFind( geoQuery, new ResponseCallback<BackendlessCollection<SearchMatchesResult>>()
            {
              @Override
              public void handleResponse( BackendlessCollection<SearchMatchesResult> response )
              {
                List<SearchMatchesResult> result = response.getCurrentPage();

                try
                {
                  Assert.assertNotNull( "Server returned null result", result );
                  Assert.assertFalse( "Server returned empty collection", result.isEmpty() );

                  for( SearchMatchesResult point : result )
                  {
                    Assert.assertTrue( "Result doesn't contain expected key", relativeMeta.containsKey( point.getGeoPoint().getMetadata().keySet().toArray()[ 0 ] ) );
                    relativeMeta.remove( point.getGeoPoint().getMetadata().keySet().toArray()[ 0 ] );
                  }

                  Assert.assertTrue( "Result contained not all expected points", relativeMeta.isEmpty() );
                }
                catch( Exception e )
                {
                  failCountDownWith( e );
                }

                countdownLogTestFinished();
              }
            } );
          }
        } );
      }
    } );
  }
}
