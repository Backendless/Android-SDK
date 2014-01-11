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
import com.backendless.geo.GeoPoint;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddPointTest extends TestsFrame
{
  @Test
  public void testAddPointWithExceedingLatitude() throws Exception
  {
    try
    {
      Backendless.Geo.savePoint( 146, -36, getRandomSimpleMetadata() );

      logTestFailed( "Client accepted a point with exceeding latitude" );
    }
    catch( Throwable e )
    {
      checkErrorCode( ExceptionMessage.WRONG_LATITUDE_VALUE, e );
    }
  }

  @Test
  public void testAddPointWithExceedingLongitude() throws Exception
  {
    try
    {
      Backendless.Geo.savePoint( -45, 235, getRandomSimpleMetadata() );

      logTestFailed( "Client accepted a point with exceeding longitude" );
    }
    catch( Throwable e )
    {
      checkErrorCode( ExceptionMessage.WRONG_LONGITUDE_VALUE, e );
    }
  }

  @Test
  public void testAddPointWithNullCategory() throws Throwable
  {
    String category = null;
    double latitude = -44;
    double longtitude = -34;
    HashMap<String, String> metadata = getRandomSimpleMetadata();

    List<String> categories = new ArrayList<String>();
    categories.add( category );

    try
    {
      Backendless.Geo.savePoint( latitude, longtitude, categories, metadata );
      logTestFailed( "Client accepted null category" );
    }
    catch( Exception e )
    {
      checkErrorCode( ExceptionMessage.NULL_CATHEGORY, e );
    }
  }

  @Test
  public void testAddPointWithEmptyCategory() throws Throwable
  {
    String category = "";
    int latitude = -43;
    int longtitude = -33;
    HashMap<String, String> metadata = getRandomSimpleMetadata();

    List<String> categories = new ArrayList<String>();
    categories.add( category );

    try
    {
      Backendless.Geo.savePoint( latitude, longtitude, categories, metadata );
      logTestFailed( "Client accepted empty category" );
    }
    catch( Exception e )
    {
      checkErrorCode( ExceptionMessage.EMPTY_CATEGORY_NAME, e );
    }
  }

  @Test
  public void testAddPointWithNullCategoriesList() throws Throwable
  {
    int latitude = -42;
    int longtitude = -32;
    HashMap<String, String> metadata = getRandomSimpleMetadata();

    List<String> categories = null;

    GeoPoint geoPoint = Backendless.Geo.savePoint( latitude, longtitude, categories, metadata );

    Assert.assertNotNull( "Server returned a null geopoint", geoPoint );
    Assert.assertTrue( "Server returned a geopoint with wrong category", geoPoint.getCategories().contains( DEFAULT_CATEGORY_NAME ) );
    Assert.assertTrue( "Server returned a geopoint with wrong categories size", geoPoint.getCategories().size() == 1 );
    Assert.assertEquals( "Server returned a geopoint with wrong latitude", latitude, geoPoint.getLatitude(), 0.0000000001d );
    Assert.assertEquals( "Server returned a geopoint with wrong longtitude", longtitude, geoPoint.getLongitude(), 0.0000000001d );
    Assert.assertNotNull( "Server returned a geopoint with null id", geoPoint.getObjectId() );
    Assert.assertTrue( "Server returned a geopoint with wrong metadat", geoPoint.getMetadata().equals( metadata ) );
  }

  @Test
  public void testAddPointWithMetaData() throws Throwable
  {
    String category = getRandomCategory();
    int latitude = -41;
    int longtitude = -31;
    HashMap<String, String> metadata = getRandomSimpleMetadata();

    List<String> categories = new ArrayList<String>();
    categories.add( category );

    GeoPoint geoPoint = Backendless.Geo.savePoint( latitude, longtitude, categories, metadata );

    Assert.assertNotNull( "Server returned a null geopoint", geoPoint );
    Assert.assertTrue( "Server returned a geopoint with wrong category", geoPoint.getCategories().equals( categories ) );
    Assert.assertEquals( "Server returned a geopoint with wrong latitude", latitude, geoPoint.getLatitude(), 0.0000000001d );
    Assert.assertEquals( "Server returned a geopoint with wrong longtitude", longtitude, geoPoint.getLongitude(), 0.0000000001d );
    Assert.assertNotNull( "Server returned a geopoint with null id", geoPoint.getObjectId() );
    Assert.assertTrue( "Server returned a geopoint with wrong metadat", geoPoint.getMetadata().equals( metadata ) );
  }

  @Test
  public void testAddPointToMultipleCategories() throws Throwable
  {
    int latitude = -40;
    int longtitude = -30;
    HashMap<String, String> metadata = getRandomSimpleMetadata();
    List<String> categories = getRandomCategoriesList( 10 );

    GeoPoint geoPoint = Backendless.Geo.savePoint( latitude, longtitude, categories, metadata );

    Assert.assertNotNull( "Server returned a null geopoint", geoPoint );
    Assert.assertTrue( "Server returned a geopoint with wrong category", geoPoint.getCategories().containsAll( categories ) );
    Assert.assertEquals( "Server returned a geopoint with wrong latitude", latitude, geoPoint.getLatitude(), 0.0000000001d );
    Assert.assertEquals( "Server returned a geopoint with wrong longtitude", longtitude, geoPoint.getLongitude(), 0.0000000001d );
    Assert.assertNotNull( "Server returned a geopoint with null id", geoPoint.getObjectId() );
    Assert.assertTrue( "Server returned a geopoint with wrong metadat", geoPoint.getMetadata().equals( metadata ) );
  }

  @Test
  public void testAddNullGeoPoint() throws Exception
  {
    try
    {
      Backendless.Geo.savePoint( null );

      logTestFailed( "Client accepted a null" );
    }
    catch( Throwable e )
    {
      checkErrorCode( ExceptionMessage.NULL_GEOPOINT, e );
    }
  }
}
