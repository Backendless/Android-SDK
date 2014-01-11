package com.backendless.tests.junit.unitTests.geoService.asyncTests;

import com.backendless.Backendless;
import com.backendless.geo.GeoCategory;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class RetrievingCategoriesTest extends TestsFrame
{
  @Test
  public void testRetrieveCategoriesList() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.Geo.getCategories( new ResponseCallback<List<GeoCategory>>()
        {
          @Override
          public void handleResponse( List<GeoCategory> geoCategories )
          {
            try
            {
              Assert.assertNotNull( "Server returned a null list" );
              Assert.assertTrue( "Server returned an empty list", !geoCategories.isEmpty() );

              for( GeoCategory geoCategory : geoCategories )
              {
                Assert.assertNotNull( "Server returned a category with null id", geoCategory.getId() );
                Assert.assertNotNull( "Server returned a category with null name", geoCategory.getName() );
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
