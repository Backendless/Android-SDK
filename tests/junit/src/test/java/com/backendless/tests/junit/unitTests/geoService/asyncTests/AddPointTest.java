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
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
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
  public void testAddPointWithExceedingLatitude()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.Geo.savePoint( 146, -36, getRandomSimpleMetadata(), new AsyncCallback<GeoPoint>()
        {
          @Override
          public void handleResponse( GeoPoint response )
          {
            failCountDownWith( "Client accepted a point with exceeding latitude" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( ExceptionMessage.WRONG_LATITUDE_VALUE, fault );
          }
        } );
      }
    } );
  }

  @Test
  public void testAddPointWithExceedingLongitude()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.Geo.savePoint( -45, 235, getRandomSimpleMetadata(), new AsyncCallback<GeoPoint>()
        {
          @Override
          public void handleResponse( GeoPoint response )
          {
            failCountDownWith( "Client accepted a point with exceeding longitude" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( ExceptionMessage.WRONG_LONGITUDE_VALUE, fault );
          }
        } );
      }
    } );
  }

  @Test
  public void testAddPointWithNullCategory() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        String category = null;
        final double latitude = -44;
        final double longtitude = -34;
        final HashMap<String, String> metadata = getRandomSimpleMetadata();

        List<String> categories = new ArrayList<String>();
        categories.add( category );

        Backendless.Geo.savePoint( latitude, longtitude, categories, metadata, new ResponseCallback<GeoPoint>()
        {
          @Override
          public void handleResponse( GeoPoint geoPoint )
          {
            try
            {
              Assert.assertNotNull( "Server returned a null geopoint", geoPoint );
              Assert.assertTrue( "Server returned a geopoint with wrong category", geoPoint.getCategories().contains( DEFAULT_CATEGORY_NAME ) );
              Assert.assertTrue( "Server returned a geopoint with wrong categories size", geoPoint.getCategories().size() == 1 );
              Assert.assertEquals( "Server returned a geopoint with wrong latitude", latitude, geoPoint.getLatitude(), 0.0000001 );
              Assert.assertEquals( "Server returned a geopoint with wrong longtitude", longtitude, geoPoint.getLongitude(), 0.0000001 );
              Assert.assertNotNull( "Server returned a geopoint with null id", geoPoint.getObjectId() );
              Assert.assertTrue( "Server returned a geopoint with wrong metadata", geoPoint.getMetadata().equals( metadata ) );
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
  public void testAddPointWithEmptyCategory() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        String category = "";
        final int latitude = -43;
        final int longtitude = -33;
        final HashMap<String, String> metadata = getRandomSimpleMetadata();

        List<String> categories = new ArrayList<String>();
        categories.add( category );

        Backendless.Geo.savePoint( latitude, longtitude, categories, metadata, new ResponseCallback<GeoPoint>()
        {
          @Override
          public void handleResponse( GeoPoint geoPoint )
          {
            try
            {
              Assert.assertNotNull( "Server returned a null geopoint", geoPoint );
              Assert.assertTrue( "Server returned a geopoint with wrong category", geoPoint.getCategories().contains( DEFAULT_CATEGORY_NAME ) );
              Assert.assertTrue( "Server returned a geopoint with wrong categories size", geoPoint.getCategories().size() == 1 );
              Assert.assertEquals( "Server returned a geopoint with wrong latitude", latitude, geoPoint.getLatitude(), 0.0000000001d );
              Assert.assertEquals( "Server returned a geopoint with wrong longtitude", longtitude, geoPoint.getLongitude(), 0.0000000001d );
              Assert.assertNotNull( "Server returned a geopoint with null id", geoPoint.getObjectId() );
              Assert.assertTrue( "Server returned a geopoint with wrong metadata", geoPoint.getMetadata().equals( metadata ) );
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
  public void testAddPointWithNullCategoriesList() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final int latitude = -42;
        final int longtitude = -32;
        final HashMap<String, String> metadata = getRandomSimpleMetadata();
        List<String> categories = null;

        Backendless.Geo.savePoint( latitude, longtitude, categories, metadata, new ResponseCallback<GeoPoint>()
        {
          @Override
          public void handleResponse( GeoPoint geoPoint )
          {
            try
            {
              Assert.assertNotNull( "Server returned a null geopoint", geoPoint );
              Assert.assertTrue( "Server returned a geopoint with wrong category", geoPoint.getCategories().contains( DEFAULT_CATEGORY_NAME ) );
              Assert.assertTrue( "Server returned a geopoint with wrong categories size", geoPoint.getCategories().size() == 1 );
              Assert.assertEquals( "Server returned a geopoint with wrong latitude", latitude, geoPoint.getLatitude(), 0.0000000001d );
              Assert.assertEquals( "Server returned a geopoint with wrong longtitude", longtitude, geoPoint.getLongitude(), 0.0000000001d );
              Assert.assertNotNull( "Server returned a geopoint with null id", geoPoint.getObjectId() );
              Assert.assertTrue( "Server returned a geopoint with wrong metadata", geoPoint.getMetadata().equals( metadata ) );
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
  public void testAddPointWithMetaData() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final int latitude = -41;
        final int longtitude = -31;
        final HashMap<String, String> metadata = getRandomSimpleMetadata();

        final List<String> categories = new ArrayList<String>();
        final String category = getRandomCategory();
        categories.add( category );

        Backendless.Geo.savePoint( latitude, longtitude, categories, metadata, new ResponseCallback<GeoPoint>()
        {
          @Override
          public void handleResponse( GeoPoint geoPoint )
          {
            try
            {
              Assert.assertNotNull( "Server returned a null geopoint", geoPoint );
              Assert.assertTrue( "Server returned a geopoint with wrong category", geoPoint.getCategories().equals( categories ) );
              Assert.assertEquals( "Server returned a geopoint with wrong latitude", latitude, geoPoint.getLatitude(), 0.0000000001d );
              Assert.assertEquals( "Server returned a geopoint with wrong longtitude", longtitude, geoPoint.getLongitude(), 0.0000000001d );
              Assert.assertNotNull( "Server returned a geopoint with null id", geoPoint.getObjectId() );
              Assert.assertTrue( "Server returned a geopoint with wrong metadata", geoPoint.getMetadata().equals( metadata ) );
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
  public void testAddPointToMultipleCategories() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final int latitude = -40;
        final int longtitude = -30;
        final HashMap<String, String> metadata = getRandomSimpleMetadata();
        final List<String> categories = getRandomCategoriesList( 10 );

        Backendless.Geo.savePoint( latitude, longtitude, categories, metadata, new ResponseCallback<GeoPoint>()
        {
          @Override
          public void handleResponse( GeoPoint geoPoint )
          {
            try
            {
              Assert.assertNotNull( "Server returned a null geopoint", geoPoint );
              Assert.assertTrue( "Server returned a geopoint with wrong category", geoPoint.getCategories().containsAll( categories ) );
              Assert.assertEquals( "Server returned a geopoint with wrong latitude", latitude, geoPoint.getLatitude(), 0.0000000001d );
              Assert.assertEquals( "Server returned a geopoint with wrong longtitude", longtitude, geoPoint.getLongitude(), 0.0000000001d );
              Assert.assertNotNull( "Server returned a geopoint with null id", geoPoint.getObjectId() );
              Assert.assertTrue( "Server returned a geopoint with wrong metadata", geoPoint.getMetadata().equals( metadata ) );
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
  public void testAddNullGeoPoint() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.Geo.savePoint( null, new AsyncCallback<GeoPoint>()
        {
          @Override
          public void handleResponse( GeoPoint response )
          {
            failCountDownWith( "Client accepted a null" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( ExceptionMessage.NULL_GEOPOINT, fault );
          }
        } );
      }
    } );
  }
}
