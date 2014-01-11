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
import com.backendless.geo.BackendlessGeoQuery;
import com.backendless.geo.GeoPoint;
import com.backendless.tests.junit.Defaults;
import com.backendless.tests.junit.IAsyncTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import java.util.*;

public class TestsFrame extends IAsyncTest
{
  public final static String DEFAULT_CATEGORY_NAME = "Default";
  public static final double METER = 0.00001d;
  public final static List<GeoPoint> usedGeoPoints = new ArrayList<GeoPoint>();

  public Random random = new Random();

  public final static String META_KEY = "meta_key";
  public final static String META_VALUE = "meta_value";

  private List<String> definedCategories = new ArrayList<String>();
  private String definedCategory;

  private int counter;

  public void setDefinedCategory( String category )
  {

    definedCategory = "sync_" + category;
    definedCategories.add( category );
  }

  public String getDefinedCategory()
  {
    return definedCategory;
  }

  public double getRandomDistance()
  {
    double distance = new Random().nextDouble();
    return distance;
  }

  public List<String> getDefinedCategories()
  {
    return definedCategories;
  }

  public void setDefinedCategories( List<String> definedCategories )
  {
    this.definedCategories = definedCategories;
  }

  public String getRandomCategory()
  {
    return "testcategory" + Math.abs(random.nextInt());
  }

  public List<String> getRandomCategoriesList( int size )
  {
    List<String> result = new ArrayList<String>();

    for( int i = 0; i < size; i++ )
    {
      result.add( getRandomCategory() + i );
    }

    return result;
  }

  public HashMap<String, String> getRandomSimpleMetadata()
  {
    String timestamp = new java.sql.Timestamp( Calendar.getInstance().getTime().getTime() ).toString().replaceAll( "\\D*", "" );

    HashMap<String, String> result = new HashMap<String, String>();
    result.put( "metadata_key" + timestamp, "metadata_value" + timestamp );

    return result;
  }

  public HashMap<String, String> getRandomMetadata()
  {
    String timestamp = new java.sql.Timestamp( Calendar.getInstance().getTime().getTime() ).toString().replaceAll( "\\D*", "" );

    HashMap<String, String> result = new HashMap<String, String>();
    result.put( META_KEY, "metadata_key" + timestamp );
    result.put( META_VALUE, "metadata_value" + timestamp );

    return result;
  }

  public void getCollectionAndCheck( final double startingLat, final double startingLong, int maxPoints,
                                     final double offset, final Map<String, String> meta, BackendlessGeoQuery geoQuery )
  {
    counter = maxPoints;
    if( geoQuery.getCategories().isEmpty() && getDefinedCategories() != null )
    {
      geoQuery.setCategories( getDefinedCategories() );
    }

    Backendless.Geo.getPoints( geoQuery, new ResponseCallback<BackendlessCollection<GeoPoint>>()
    {
      @Override
      public void handleResponse( BackendlessCollection<GeoPoint> geoPointBackendlessCollection )
      {
        try
        {
          Assert.assertNotNull( "Server returned a null collection", geoPointBackendlessCollection );

          for( GeoPoint geoPoint : geoPointBackendlessCollection.getCurrentPage() )
          {
            if( geoPoint.getCategories().containsAll( getDefinedCategories() ) )
            {
              if( meta == null || meta.isEmpty() )
                Assert.assertTrue( "Server returned points with unexpected metadata", geoPoint.getMetadata().isEmpty() );
              else
                Assert.assertTrue( "Server returned points with unexpected metadata", geoPoint.getMetadata().equals( meta ) );

              Assert.assertEquals( "Server returned points from unexpected latitude range", startingLat, geoPoint.getLatitude(), 0.0000000001d );
              Assert.assertTrue( "Server returned points from unexpected longtitude range", geoPoint.getLongitude() >= startingLong && geoPoint.getLongitude() <= startingLong + offset );

              counter--;
            }
          }

          Assert.assertEquals( "Server found wrong total points count", counter, 0 );
        }
        catch( Throwable t )
        {
          failCountDownWith( t );
        }

        countDown();
      }
    } );
  }

  @BeforeClass
  public static void setUp() throws Throwable
  {
    Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
  }

  @Before
  public void cleanVars()
  {
    definedCategories.clear();
    definedCategory = null;
  }
}
