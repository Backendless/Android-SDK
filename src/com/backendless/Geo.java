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

package com.backendless;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.core.responder.AdaptingResponder;
import com.backendless.core.responder.policy.CollectionAdaptingPolicy;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.geo.*;
import weborb.types.Types;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class Geo
{
  private final static String GEO_MANAGER_SERVER_ALIAS = "com.backendless.services.geo.GeoService";
  private final static String DEFAULT_CATEGORY_NAME = "Default";

  private static final Geo instance = new Geo();

  static Geo getInstance()
  {
    return instance;
  }

  private Geo()
  {
    Types.addClientClassMapping( "com.backendless.geo.model.GeoPoint", GeoPoint.class );
    Types.addClientClassMapping( "com.backendless.geo.model.SearchMatchesResult", SearchMatchesResult.class );
    Types.addClientClassMapping( "com.backendless.geo.BackendlessGeoQuery", BackendlessGeoQuery.class );
    Types.addClientClassMapping( "com.backendless.geo.model.GeoCategory", GeoCategory.class );
    Types.addClientClassMapping( "com.backendless.geo.Units", Units.class );
  }

  public GeoCategory addCategory( String categoryName ) throws BackendlessException
  {
    checkCategoryName( categoryName );

    return (GeoCategory) Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "addCategory", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), categoryName } );
  }

  public void addCategory( String categoryName, AsyncCallback<GeoCategory> responder )
  {
    try
    {
      checkCategoryName( categoryName );

      Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "addCategory", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), categoryName }, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public boolean deleteCategory( String categoryName ) throws BackendlessException
  {
    checkCategoryName( categoryName );

    return (Boolean) Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "deleteCategory", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), categoryName } );
  }

  public void deleteCategory( String categoryName, AsyncCallback<Boolean> responder )
  {
    try
    {
      checkCategoryName( categoryName );

      Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "deleteCategory", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), categoryName }, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public GeoPoint savePoint( final double latitude, final double longitude,
                             final Map<String, Object> metadata ) throws BackendlessException
  {
    return savePoint( latitude, longitude, null, metadata );
  }

  public GeoPoint savePoint( double latitude, double longitude, List<String> categories,
                             final Map<String, Object> metadata ) throws BackendlessException
  {
    return savePoint( new GeoPoint( latitude, longitude, categories, metadata ) );
  }

  public GeoPoint savePoint( GeoPoint geoPoint ) throws BackendlessException
  {
    if( geoPoint == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_GEOPOINT );

    checkCoordinates( geoPoint.getLatitude(), geoPoint.getLongitude() );

    String remoteMethod = geoPoint.getObjectId() == null ? "addPoint" : "updatePoint";

    return (GeoPoint) Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, remoteMethod, new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), geoPoint } );
  }

  public void savePoint( final double latitude, final double longitude, final Map<String, Object> metadata,
                         AsyncCallback<GeoPoint> responder )
  {
    savePoint( latitude, longitude, null, metadata, responder );
  }

  public void savePoint( double latitude, double longitude, List<String> categories, Map<String, Object> metadata,
                         AsyncCallback<GeoPoint> responder )
  {
    savePoint( new GeoPoint( latitude, longitude, categories, metadata ), responder );
  }

  public void savePoint( GeoPoint geoPoint, AsyncCallback<GeoPoint> responder )
  {
    try
    {
      if( geoPoint == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_GEOPOINT );

      checkCoordinates( geoPoint.getLatitude(), geoPoint.getLongitude() );

      String remoteMethod = geoPoint.getObjectId() == null ? "addPoint" : "updatePoint";

      Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, remoteMethod, new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), geoPoint }, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public void removePoint( GeoPoint geoPoint )
  {
    Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "removePoint", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), geoPoint.getObjectId() } );
  }

  public void removePoint( GeoPoint geoPoint, AsyncCallback<Void> responder )
  {
    Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "removePoint", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), geoPoint.getObjectId() }, responder );
  }

  public BackendlessCollection<GeoPoint> getPoints( BackendlessGeoQuery geoQuery ) throws BackendlessException
  {
    checkGeoQuery( geoQuery );
    CollectionAdaptingPolicy<GeoPoint> adaptingPolicy = new CollectionAdaptingPolicy<GeoPoint>();
    BackendlessCollection<GeoPoint> result = (BackendlessCollection<GeoPoint>) Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "getPoints", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), geoQuery }, new AdaptingResponder<GeoPoint>( GeoPoint.class, adaptingPolicy ) );

    result.setQuery( geoQuery );
    result.setType( GeoPoint.class );

    return result;
  }

  public void getPoints( final BackendlessGeoQuery geoQuery,
                         final AsyncCallback<BackendlessCollection<GeoPoint>> responder )
  {
    try
    {
      checkGeoQuery( geoQuery );
      CollectionAdaptingPolicy<GeoPoint> adaptingPolicy = new CollectionAdaptingPolicy<GeoPoint>();

      Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "getPoints", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), geoQuery }, new AsyncCallback<BackendlessCollection<GeoPoint>>()
      {
        @Override
        public void handleResponse( BackendlessCollection<GeoPoint> response )
        {
          response.setQuery( geoQuery );
          response.setType( GeoPoint.class );

          if( responder != null )
            responder.handleResponse( response );
        }

        @Override
        public void handleFault( BackendlessFault fault )
        {
          responder.handleFault( fault );
        }
      }, new AdaptingResponder<GeoPoint>( GeoPoint.class, adaptingPolicy ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public BackendlessCollection<SearchMatchesResult> relativeFind( BackendlessGeoQuery geoQuery ) throws BackendlessException
  {
    if( geoQuery == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_GEO_QUERY );

    if( geoQuery.getRelativeFindMetadata().isEmpty() || geoQuery.getRelativeFindPercentThreshold() == 0 )
      throw new IllegalArgumentException( ExceptionMessage.INCONSISTENT_GEO_RELATIVE );

    CollectionAdaptingPolicy<SearchMatchesResult> adaptingPolicy = new CollectionAdaptingPolicy<SearchMatchesResult>();
    BackendlessCollection<SearchMatchesResult> result = (BackendlessCollection<SearchMatchesResult>) Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "relativeFind", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), geoQuery }, new AdaptingResponder<SearchMatchesResult>( SearchMatchesResult.class, adaptingPolicy ) );

    result.setQuery( geoQuery );
    result.setType( SearchMatchesResult.class );

    return result;
  }

  public void relativeFind( final BackendlessGeoQuery geoQuery,
                            final AsyncCallback<BackendlessCollection<SearchMatchesResult>> responder )
  {
    try
    {
      if( geoQuery == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_GEO_QUERY );

      if( geoQuery.getRelativeFindMetadata().isEmpty() || geoQuery.getRelativeFindPercentThreshold() == 0 )
        throw new IllegalArgumentException( ExceptionMessage.INCONSISTENT_GEO_RELATIVE );

      CollectionAdaptingPolicy<SearchMatchesResult> adaptingPolicy = new CollectionAdaptingPolicy<SearchMatchesResult>();

      Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "relativeFind", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), geoQuery }, new AsyncCallback<BackendlessCollection<SearchMatchesResult>>()
      {
        @Override
        public void handleResponse( BackendlessCollection<SearchMatchesResult> response )
        {
          response.setQuery( geoQuery );
          response.setType( SearchMatchesResult.class );

          if( responder != null )
            responder.handleResponse( response );
        }

        @Override
        public void handleFault( BackendlessFault fault )
        {
          responder.handleFault( fault );
        }
      }, new AdaptingResponder<SearchMatchesResult>( SearchMatchesResult.class, adaptingPolicy ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public List<GeoCategory> getCategories() throws BackendlessException
  {
    GeoCategory[] response = Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "getCategories", new Object[] { Backendless.getApplicationId(), Backendless.getVersion() } );

    return Arrays.asList( response );
  }

  public void getCategories( final AsyncCallback<List<GeoCategory>> responder )
  {
    Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "getCategories", new Object[] { Backendless.getApplicationId(), Backendless.getVersion() }, new AsyncCallback<GeoCategory[]>()
    {
      @Override
      public void handleResponse( GeoCategory[] response )
      {
        if( responder != null )
          responder.handleResponse( Arrays.asList( response ) );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        if( responder != null )
          responder.handleFault( fault );
      }
    } );
  }

  private void checkCategoryName( String categoryName ) throws BackendlessException
  {
    if( categoryName == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_CATEGORY_NAME );

    if( categoryName.equals( "" ) )
      throw new IllegalArgumentException( ExceptionMessage.NULL_CATEGORY_NAME );

    if( categoryName.equals( DEFAULT_CATEGORY_NAME ) )
      throw new IllegalArgumentException( ExceptionMessage.DEFAULT_CATEGORY_NAME );
  }

  private void checkCoordinates( double latitude, double longitude ) throws BackendlessException
  {
    if( latitude > 90 || latitude < -90 )
      throw new IllegalArgumentException( ExceptionMessage.WRONG_LATITUDE_VALUE );

    if( longitude > 180 || longitude < -180 )
      throw new IllegalArgumentException( ExceptionMessage.WRONG_LONGITUDE_VALUE );
  }

  private void checkGeoQuery( BackendlessGeoQuery geoQuery ) throws BackendlessException
  {
    if( geoQuery == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_GEO_QUERY );

    if( geoQuery.getSearchRectangle() != null )
    {
      if( geoQuery.getSearchRectangle().length != 4 )
        throw new IllegalArgumentException( ExceptionMessage.WRONG_SEARCH_RECTANGLE_QUERY );

      if( geoQuery.getRadius() != null || geoQuery.getLatitude() != null || geoQuery.getLongitude() != null )
        throw new IllegalArgumentException( ExceptionMessage.INCONSISTENT_GEO_QUERY );
    }
    else if( geoQuery.getRadius() != null )
    {
      if( geoQuery.getRadius() == null || geoQuery.getRadius() <= 0 )
        throw new IllegalArgumentException( ExceptionMessage.WRONG_RADIUS );

      if( geoQuery.getLatitude() == null )
        throw new IllegalArgumentException( ExceptionMessage.WRONG_LATITUDE_VALUE );

      if( geoQuery.getLongitude() == null )
        throw new IllegalArgumentException( ExceptionMessage.WRONG_LONGITUDE_VALUE );

      checkCoordinates( geoQuery.getLatitude(), geoQuery.getLongitude() );

      if( geoQuery.getUnits() == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_UNIT );
    }
    else if( geoQuery.getCategories() == null && geoQuery.getMetadata() == null )
      throw new IllegalArgumentException( ExceptionMessage.WRONG_GEO_QUERY );

    if( geoQuery.getCategories() != null )
      for( String categoryName : geoQuery.getCategories() )
        checkCategoryName( categoryName );

    if( geoQuery.getOffset() < 0 )
      throw new IllegalArgumentException( ExceptionMessage.WRONG_OFFSET );

    if( geoQuery.getPageSize() < 0 )
      throw new IllegalArgumentException( ExceptionMessage.WRONG_PAGE_SIZE );

    if(geoQuery.getDpp() > 0 || geoQuery.getDpi() > 0 || geoQuery.getSize() > 0){
      if(geoQuery.getDpp() < 0 || geoQuery.getDpi() < 0 || geoQuery.getSize() < 0){
        throw new IllegalArgumentException( ExceptionMessage.WRONG_OFFSET );
      }
    }
  }
}
