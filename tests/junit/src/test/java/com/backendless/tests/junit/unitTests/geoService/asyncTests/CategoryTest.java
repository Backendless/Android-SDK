package com.backendless.tests.junit.unitTests.geoService.asyncTests;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.geo.GeoCategory;
import org.junit.Assert;
import org.junit.Test;

public class CategoryTest extends TestsFrame
{
  @Test
  public void testAddNullCategory() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.Geo.addCategory( null, new AsyncCallback<GeoCategory>()
        {
          @Override
          public void handleResponse( GeoCategory response )
          {
            failCountDownWith( "Client have send a null category" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( ExceptionMessage.NULL_CATEGORY_NAME, fault );
          }
        } );
      }
    } );
  }

  @Test
  public void testDeleteNullCategory() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.Geo.deleteCategory( null, new AsyncCallback<Boolean>()
        {
          @Override
          public void handleResponse( Boolean response )
          {
            failCountDownWith( "Client have send a null category" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( ExceptionMessage.NULL_CATEGORY_NAME, fault );
          }
        } );
      }
    } );
  }

  @Test
  public void testAddEmptyCategory() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.Geo.addCategory( "", new AsyncCallback<GeoCategory>()
        {
          @Override
          public void handleResponse( GeoCategory response )
          {
            failCountDownWith( "Client have send an empty category" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( ExceptionMessage.NULL_CATEGORY_NAME, fault );
          }
        } );
      }
    } );
  }

  @Test
  public void testDeleteEmptyCategory() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.Geo.deleteCategory( "", new AsyncCallback<Boolean>()
        {
          @Override
          public void handleResponse( Boolean response )
          {
            failCountDownWith( "Client have send an empty category" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( ExceptionMessage.NULL_CATEGORY_NAME, fault );
          }
        } );
      }
    } );
  }

  @Test
  public void testAddDefaultCategory() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.Geo.addCategory( DEFAULT_CATEGORY_NAME, new AsyncCallback<GeoCategory>()
        {
          @Override
          public void handleResponse( GeoCategory response )
          {
            failCountDownWith( "Client have send a default category" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( ExceptionMessage.DEFAULT_CATEGORY_NAME, fault );
          }
        } );
      }
    } );
  }

  @Test
  public void testDeleteDefaultCategory() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.Geo.deleteCategory( DEFAULT_CATEGORY_NAME, new AsyncCallback<Boolean>()
        {
          @Override
          public void handleResponse( Boolean response )
          {
            failCountDownWith( "Client have send a default category" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( ExceptionMessage.DEFAULT_CATEGORY_NAME, fault );
          }
        } );
      }
    } );
  }

  @Test
  public void testAddProperCategory() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final String categoryName = getRandomCategory();

        Backendless.Geo.addCategory( categoryName, new ResponseCallback<GeoCategory>()
        {
          @Override
          public void handleResponse( GeoCategory geoCategory )
          {
            try
            {
              Assert.assertNotNull( "Server returned a null category", geoCategory );
              Assert.assertEquals( "Server returned a category with a wrong name", categoryName, geoCategory.getName() );
              Assert.assertNotNull( "Server returned a category with a null id", geoCategory.getId() );
              Assert.assertTrue( "Server returned a category with a wrong size", geoCategory.getSize() == 0 );
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
  public void testAddSameCategoryTwice() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final String categoryName = getRandomCategory();
        Backendless.Geo.addCategory( categoryName, new ResponseCallback<GeoCategory>()
        {
          @Override
          public void handleResponse( GeoCategory response )
          {
            Backendless.Geo.addCategory( categoryName, new ResponseCallback<GeoCategory>()
            {
              @Override
              public void handleResponse( GeoCategory geoCategory )
              {
                try
                {
                  Assert.assertNotNull( "Server returned a null category", geoCategory );
                  Assert.assertEquals( "Server returned a category with a wrong name", categoryName, geoCategory.getName() );
                  Assert.assertNotNull( "Server returned a category with a wrong id", geoCategory.getId() );
                  Assert.assertTrue( "Server returned a category with a wrong size", geoCategory.getSize() == 0 );
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
  public void testRemoveCategory() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final String categoryName = getRandomCategory();
        Backendless.Geo.addCategory( categoryName, new ResponseCallback<GeoCategory>()
        {
          @Override
          public void handleResponse( GeoCategory response )
          {
            Backendless.Geo.deleteCategory( categoryName, new ResponseCallback<Boolean>()
            {
              @Override
              public void handleResponse( Boolean response )
              {
                try
                {
                  Assert.assertTrue( "Server returned wrong status", response );
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
  public void testRemoveUnexistingCategory() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final String categoryName = getRandomCategory();

        Backendless.Geo.deleteCategory( categoryName, new AsyncCallback<Boolean>()
        {
          @Override
          public void handleResponse( Boolean response )
          {
            failCountDownWith( "Server deleted unexisting category" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( 4001, fault );
          }
        } );
      }
    } );
  }
}
