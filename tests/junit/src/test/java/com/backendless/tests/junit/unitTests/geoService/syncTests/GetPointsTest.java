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

package com.backendless.tests.junit.unitTests.geoService.syncTests;

import com.backendless.Backendless;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.geo.BackendlessGeoQuery;
import com.backendless.geo.SearchMatchesResult;
import com.backendless.geo.Units;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetPointsTest extends TestsFrame
{
  @Test
  public void testGetPointsForRectangle() throws Throwable
  {
    double startingLat = 10;
    double startingLong = 10;
    int maxPoints = 10;
    setDefinedCategory( getRandomCategory() );
    Map<String, String> meta = getRandomSimpleMetadata();

    for( int i = 0; i < maxPoints; i++ )
    {
      Backendless.Geo.savePoint( startingLat, startingLong + i, getDefinedCategories(), meta );
    }

    BackendlessGeoQuery geoQuery = new BackendlessGeoQuery( startingLat + 1, startingLong - 1, startingLat - 1, startingLong + maxPoints + 1 );
    getCollectionAndCheck( startingLat, startingLong, maxPoints, maxPoints, meta, geoQuery );
  }

  @Test
  public void testGetPointsByMetadata() throws Throwable
  {
    double startingLat = 20;
    double startingLong = 10;
    int maxPoints = 10;
    setDefinedCategory( getRandomCategory() );

    Map<String, String> meta = new HashMap<String, String>();
    meta.put( "object_type_sync", "office" );

    for( int i = 0; i < maxPoints; i++ )
    {
      Backendless.Geo.savePoint( startingLat, startingLong + i, getDefinedCategories(), meta );
    }

    getCollectionAndCheck( startingLat, startingLong, maxPoints, maxPoints, meta, new BackendlessGeoQuery( meta ) );
  }

  @Test
  public void testGetPointsWithNegativeOffset() throws Throwable
  {
    try
    {
      Backendless.Geo.getPoints( new BackendlessGeoQuery( 50, 50, 10, -10 ) );

      logTestFailed( "Client send a query with negative offset" );
    }
    catch( Throwable e )
    {
      checkErrorCode( ExceptionMessage.WRONG_OFFSET, e );
    }
  }

  @Test
  public void testGetPointsWithBothRectAndRadiusQuery() throws Throwable
  {
    try
    {
      BackendlessGeoQuery geoQuery = new BackendlessGeoQuery( 10d, 10d, 20d, 20d );
      geoQuery.setRadius( 10d );
      geoQuery.setLatitude( 10d );
      geoQuery.setLatitude( 10d );

      Backendless.Geo.getPoints( geoQuery );

      logTestFailed( "Client send a query with both rectangle and radius search query" );
    }
    catch( Throwable e )
    {
      checkErrorCode( ExceptionMessage.INCONSISTENT_GEO_QUERY, e );
    }
  }

  @Test
  public void testGetPointsForCategory() throws Throwable
  {
    double startingLat = 30;
    double startingLong = 10;
    int maxPoints = 10;
    setDefinedCategory( getRandomCategory() );
    Map<String, String> meta = getRandomSimpleMetadata();

    for( int i = 0; i < maxPoints; i++ )
    {
      Backendless.Geo.savePoint( startingLat, startingLong + i, getDefinedCategories(), meta );
    }

    getCollectionAndCheck( startingLat, startingLong, maxPoints, maxPoints, meta, new BackendlessGeoQuery( getDefinedCategories() ) );
  }

  @Test
  public void testGetPointsForMultipleCategories() throws Throwable
  {
    double startingLat = 80;
    double startingLong = 160;
    int maxPoints = 10;
    setDefinedCategories( getRandomCategoriesList( 10 ) );
    Map<String, String> meta = getRandomSimpleMetadata();

    for( int i = 0; i < maxPoints; i++ )
    {
      Backendless.Geo.savePoint( startingLat, startingLong + i, getDefinedCategories(), meta );
    }

    getCollectionAndCheck( startingLat, startingLong, maxPoints, maxPoints, meta, new BackendlessGeoQuery( getDefinedCategories() ) );
  }

  @Test
  public void testGetPointsWithoutMetadata() throws Throwable
  {
    double startingLat = 50;
    double startingLong = 10;
    int maxPoints = 10;
    Map<String, String> meta = getRandomSimpleMetadata();
    setDefinedCategory( getRandomCategory() );

    for( int i = 0; i < maxPoints; i++ )
    {
      Backendless.Geo.savePoint( startingLat, startingLong + i, getDefinedCategories(), meta );
    }

    meta = null;
    BackendlessGeoQuery geoQuery = new BackendlessGeoQuery( getDefinedCategories() );
    geoQuery.setIncludeMeta( false );

    getCollectionAndCheck( startingLat, startingLong, maxPoints, maxPoints, meta, geoQuery );
  }

  @Test
  public void testGetPointsWithOffsetGreaterThenPointsCount() throws Throwable
  {
    double startingLat = 60;
    double startingLong = 10;
    int maxPoints = 10;
    setDefinedCategory( getRandomCategory() );
    Map<String, String> meta = getRandomSimpleMetadata();

    for( int i = 0; i < maxPoints; i++ )
    {
      Backendless.Geo.savePoint( startingLat, startingLong + i, getDefinedCategories(), meta );
    }

    BackendlessGeoQuery geoQuery = new BackendlessGeoQuery( getDefinedCategories() );
    geoQuery.setOffset( maxPoints * 2 );

    try
    {
      Backendless.Geo.getPoints( geoQuery );
      logTestFailed( "Server accepted request" );
    }
    catch( Throwable e )
    {
      checkErrorCode( 4003, e );
    }
  }

  @Test
  public void testGetPointsByRadiusIn10Meters() throws Throwable
  {
    double startingLat = 80;
    double startingLong = 10;
    int maxPoints = 10;
    double offset = 0;
    setDefinedCategory( getRandomCategory() );
    Map<String, String> meta = getRandomSimpleMetadata();

    for( int i = 0; i < maxPoints; i++ )
    {
      offset += METER;
      Backendless.Geo.savePoint( startingLat, startingLong + offset, getDefinedCategories(), meta );
    }

    BackendlessGeoQuery geoQuery = new BackendlessGeoQuery( startingLat, startingLong + (offset / 2), maxPoints, Units.METERS );
    getCollectionAndCheck( startingLat, startingLong, maxPoints, offset, meta, geoQuery );
  }

  @Test
  public void testGetPointsByRadiusIn1Kilometer() throws Throwable
  {
    double startingLat = 10;
    double startingLong = 15;
    int maxPoints = 10;
    double offset = 0;
    setDefinedCategory( getRandomCategory() );
    Map<String, String> meta = getRandomSimpleMetadata();

    for( int i = 0; i < maxPoints; i++ )
    {
      offset += METER * 100;
      Backendless.Geo.savePoint( startingLat, startingLong + offset, getDefinedCategories(), meta );
    }

    BackendlessGeoQuery geoQuery = new BackendlessGeoQuery( startingLat, startingLong + offset / 2, 1, Units.KILOMETERS );
    getCollectionAndCheck( startingLat, startingLong, maxPoints, offset, meta, geoQuery );
  }

  @Test
  public void testGetPointsByRadiusIn100Yards() throws Throwable
  {
    double startingLat = 10;
    double startingLong = 30;
    int maxPoints = 10;
    double offset = 0;
    setDefinedCategory( getRandomCategory() );
    Map<String, String> meta = getRandomSimpleMetadata();

    for( int i = 0; i < maxPoints; i++ )
    {
      offset += METER * 0.914399998610112;
      Backendless.Geo.savePoint( startingLat, startingLong + offset, getDefinedCategories(), meta );
    }

    BackendlessGeoQuery geoQuery = new BackendlessGeoQuery( startingLat, startingLong + offset / 2, 100, Units.YARDS );
    getCollectionAndCheck( startingLat, startingLong, maxPoints, offset, meta, geoQuery );
  }

  @Ignore
  @Test
  public void testGetPointsWithRelativeSearch() throws Throwable
  {
    double startingLat = 80;
    double startingLong = 160;
    int maxPoints = 10;
    double offset = 0;
    Map<String, String> relativeMeta = new HashMap<String, String>();

    for( int i = 0; i < maxPoints; i++ )
    {
      offset += METER * 0.914399998610112;
      String key = "metaKey" + random.nextInt() + i;
      String meta = "someMeta" + random.nextInt() + i;
      relativeMeta.put( key, meta );

      Backendless.Geo.savePoint( startingLat, startingLong + offset, relativeMeta );
    }

    BackendlessGeoQuery geoQuery = new BackendlessGeoQuery( relativeMeta, maxPoints);
    geoQuery.setPageSize( 50 );
    geoQuery.setIncludeMeta( true );
    geoQuery.setRelativeFindPercentThreshold( 100 );

    List<SearchMatchesResult> result = Backendless.Geo.relativeFind( geoQuery ).getCurrentPage();

    Assert.assertNotNull( "Server returned null result", result );
    Assert.assertEquals( "Server returned wrong number of points", 1, result.size() );

    for( Map.Entry<String, String> entry : relativeMeta.entrySet() )
    {
      Assert.assertTrue( "Point didn't contain expected meta key", result.get( 0 ).getGeoPoint().getMetadata().containsKey( entry.getKey() ) );
      Assert.assertEquals( "Point didn't contain expected meta key", result.get( 0 ).getGeoPoint().getMetadata().get( entry.getKey() ), entry.getValue() );
    }
  }
}
