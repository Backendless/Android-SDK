package com.backendless.tests.junit.unitTests.geoService.syncTests;

import com.backendless.Backendless;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.geo.GeoCategory;
import org.junit.Assert;
import org.junit.Test;

public class CategoryTest extends TestsFrame
{
  @Test
  public void testAddNullCategory() throws Exception
  {
    try
    {
      Backendless.Geo.addCategory( null );

      logTestFailed( "Client have send a null category" );
    }
    catch( Throwable e )
    {
      checkErrorCode( ExceptionMessage.NULL_CATEGORY_NAME, e );
    }
  }

  @Test
  public void testDeleteNullCategory() throws Exception
  {
    try
    {
      Backendless.Geo.deleteCategory( null );

      logTestFailed( "Client have send a null category" );
    }
    catch( Throwable e )
    {
      checkErrorCode( ExceptionMessage.NULL_CATEGORY_NAME, e );
    }
  }

  @Test
  public void testAddEmptyCategory() throws Exception
  {
    try
    {
      Backendless.Geo.addCategory( "" );

      logTestFailed( "Client have send an empty category" );
    }
    catch( Throwable e )
    {
      checkErrorCode( ExceptionMessage.NULL_CATEGORY_NAME, e );
    }
  }

  @Test
  public void testDeleteEmptyCategory() throws Exception
  {
    try
    {
      Backendless.Geo.deleteCategory( "" );

      logTestFailed( "Client have send an empty category" );
    }
    catch( Throwable e )
    {
      checkErrorCode( ExceptionMessage.NULL_CATEGORY_NAME, e );
    }
  }

  @Test
  public void testAddDefaultCategory() throws Exception
  {
    try
    {
      Backendless.Geo.addCategory( DEFAULT_CATEGORY_NAME );

      logTestFailed( "Client have send a default category" );
    }
    catch( Throwable e )
    {
      checkErrorCode( ExceptionMessage.DEFAULT_CATEGORY_NAME, e );
    }
  }

  @Test
  public void testDeleteDefaultCategory() throws Exception
  {
    try
    {
      Backendless.Geo.deleteCategory( DEFAULT_CATEGORY_NAME );

      logTestFailed( "Client have send a default category" );
    }
    catch( Throwable e )
    {
      checkErrorCode( ExceptionMessage.DEFAULT_CATEGORY_NAME, e );
    }
  }

  @Test
  public void testAddProperCategory() throws Throwable
  {
    String categoryName = getRandomCategory();
    GeoCategory geoCategory = Backendless.Geo.addCategory( categoryName );

    checkCategory( categoryName, geoCategory );
  }

  @Test
  public void testAddSameCategoryTwice() throws Throwable
  {
    String categoryName = getRandomCategory();
    Backendless.Geo.addCategory( categoryName );
    GeoCategory geoCategory = Backendless.Geo.addCategory( categoryName );

    checkCategory( categoryName, geoCategory );
  }

  @Test
  public void testRemoveCategory() throws Throwable
  {
    String categoryName = getRandomCategory();
    Backendless.Geo.addCategory( categoryName );
    Assert.assertTrue( "Server returned wrong status", Backendless.Geo.deleteCategory( categoryName ) );
  }

  @Test
  public void testRemoveUnexistingCategory() throws Throwable
  {
    String categoryName = getRandomCategory();

    try
    {
      Backendless.Geo.deleteCategory( categoryName );

      logTestFailed( "Server deleted unexisting category" );
    }
    catch( Throwable e )
    {
      checkErrorCode( 4001, e );
    }
  }

  private void checkCategory( String categoryName, GeoCategory geoCategory )
  {
    Assert.assertNotNull( "Server returned a null category", geoCategory );
    Assert.assertEquals( "Server returned a category with a wrong name", categoryName, geoCategory.getName() );
    Assert.assertNotNull( "Server returned a category with a wrong id", geoCategory.getId() );
    Assert.assertTrue( "Server returned a category with a wrong size", geoCategory.getSize() == 0 );
  }
}
