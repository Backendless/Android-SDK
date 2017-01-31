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

import android.content.res.Resources;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.core.responder.AdaptingResponder;
import com.backendless.core.responder.policy.CollectionAdaptingPolicy;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.geo.*;
import com.backendless.geo.geofence.*;
import weborb.types.Types;

import java.util.*;

public final class Geo
{
  public final static String GEO_MANAGER_SERVER_ALIAS = "com.backendless.services.geo.GeoService";
  private final static String DEFAULT_CATEGORY_NAME = "Default";

  private static final Geo instance = new Geo();

  static Geo getInstance()
  {
    return instance;
  }

  private Geo()
  {
    Types.addClientClassMapping( "com.backendless.geo.model.GeoPoint", GeoPoint.class );
    Types.addClientClassMapping( "com.backendless.geo.model.GeoCluster", GeoCluster.class );
    Types.addClientClassMapping( "com.backendless.geo.model.SearchMatchesResult", SearchMatchesResult.class );
    Types.addClientClassMapping( "com.backendless.geo.BackendlessGeoQuery", BackendlessGeoQuery.class );
    Types.addClientClassMapping( "com.backendless.geo.model.GeoCategory", GeoCategory.class );
    Types.addClientClassMapping( "com.backendless.geo.Units", Units.class );
    Types.addClientClassMapping( "com.backendless.geofence.model.GeoFenceAMF", GeoFence.class );
    Types.addClientClassMapping( "com.backendless.geofence.model.FenceType", FenceType.class );
  }

  public GeoCategory addCategory( String categoryName ) throws BackendlessException
  {
    checkCategoryName( categoryName );

    return (GeoCategory) Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "addCategory", new Object[] { categoryName } );
  }

  public void addCategory( String categoryName, AsyncCallback<GeoCategory> responder )
  {
    try
    {
      checkCategoryName( categoryName );

      Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "addCategory", new Object[] { categoryName }, responder );
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

    return (Boolean) Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "deleteCategory", new Object[] { categoryName } );
  }

  public void deleteCategory( String categoryName, AsyncCallback<Boolean> responder )
  {
    try
    {
      checkCategoryName( categoryName );

      Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "deleteCategory", new Object[] { categoryName }, responder );
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

    return (GeoPoint) Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, remoteMethod, new Object[] { geoPoint } );
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

      Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, remoteMethod, new Object[] { geoPoint }, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public void removePoint( GeoPoint geoPoint )
  {
    Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "removePoint", new Object[] { geoPoint.getObjectId() } );
  }

  public void removePoint( GeoPoint geoPoint, AsyncCallback<Void> responder )
  {
    Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "removePoint", new Object[] { geoPoint.getObjectId() }, responder );
  }

  public List<GeoPoint> getPoints( BackendlessGeoQuery geoQuery ) throws BackendlessException
  {
    checkGeoQuery( geoQuery );
    CollectionAdaptingPolicy<GeoPoint> adaptingPolicy = new CollectionAdaptingPolicy<>();
    List<GeoPoint> result = Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "getPoints", new Object[] { geoQuery }, new AdaptingResponder<>( GeoPoint.class, adaptingPolicy ) );

    if( geoQuery.getDpp() != null && geoQuery.getDpp() > 0 )
      setReferenceToCluster( result, geoQuery );

    return result;
  }

  public void getPoints( final BackendlessGeoQuery geoQuery, final AsyncCallback<List<GeoPoint>> responder )
  {
    try
    {
      checkGeoQuery( geoQuery );
      CollectionAdaptingPolicy<GeoPoint> adaptingPolicy = new CollectionAdaptingPolicy<GeoPoint>();

      Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "getPoints", new Object[] { geoQuery }, new AsyncCallback<List<GeoPoint>>()
      {
        @Override
        public void handleResponse( List<GeoPoint> response )
        {
          if( geoQuery.getDpp() != null && geoQuery.getDpp() > 0 )
            setReferenceToCluster( response, geoQuery );

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

  public List<GeoPoint> getPoints( final GeoCluster geoCluster )
  {
    CollectionAdaptingPolicy<GeoPoint> adaptingPolicy = new CollectionAdaptingPolicy<GeoPoint>();
    List<GeoPoint> result = Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "loadGeoPoints", new Object[] { geoCluster.getObjectId(), geoCluster.getGeoQuery() }, new AdaptingResponder<GeoPoint>( GeoPoint.class, adaptingPolicy ) );

    return result;
  }

  public void getPoints( final GeoCluster geoCluster, final AsyncCallback<List<GeoPoint>> responder )
  {
    try
    {
      CollectionAdaptingPolicy<GeoPoint> adaptingPolicy = new CollectionAdaptingPolicy<GeoPoint>();

      Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "loadGeoPoints", new Object[] { geoCluster.getObjectId(), geoCluster.getGeoQuery() }, new AsyncCallback<List<GeoPoint>>()
      {
        @Override
        public void handleResponse( List<GeoPoint> response )
        {
          if( responder != null )
            responder.handleResponse( response );
        }

        @Override
        public void handleFault( BackendlessFault fault )
        {
          if( responder != null )
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

  public List<SearchMatchesResult> relativeFind( BackendlessGeoQuery geoQuery ) throws BackendlessException
  {
    if( geoQuery == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_GEO_QUERY );

    if( geoQuery.getRelativeFindMetadata().isEmpty() || geoQuery.getRelativeFindPercentThreshold() == 0 )
      throw new IllegalArgumentException( ExceptionMessage.INCONSISTENT_GEO_RELATIVE );

    CollectionAdaptingPolicy<SearchMatchesResult> adaptingPolicy = new CollectionAdaptingPolicy<>();
    List<SearchMatchesResult> result = Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "relativeFind", new Object[] { geoQuery }, new AdaptingResponder<>( SearchMatchesResult.class, adaptingPolicy ) );

    return result;
  }

  public List<GeoPoint> getPoints( String geofenceName, BackendlessGeoQuery query ) throws BackendlessException
  {
    checkGeoQuery( query );
    CollectionAdaptingPolicy<GeoPoint> adaptingPolicy = new CollectionAdaptingPolicy<>();
    List<GeoPoint> result = Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "getPoints", new Object[] { geofenceName, query }, new AdaptingResponder<>( GeoPoint.class, adaptingPolicy ) );

    return result;
  }

  public void getPoints( final String geofenceName, final BackendlessGeoQuery query,
                         final AsyncCallback<List<GeoPoint>> responder )
  {
    try
    {
      checkGeoQuery( query );
      CollectionAdaptingPolicy<GeoPoint> adaptingPolicy = new CollectionAdaptingPolicy<>();

      Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "getPoints", new Object[] { geofenceName, query }, new AsyncCallback<List<GeoPoint>>()
      {
        @Override
        public void handleResponse( List<GeoPoint> response )
        {
          if( responder != null )
            responder.handleResponse( response );
        }

        @Override
        public void handleFault( BackendlessFault fault )
        {
          responder.handleFault( fault );
        }
      }, new AdaptingResponder<>( GeoPoint.class, adaptingPolicy ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public List<GeoPoint> getPoints( String geofenceName ) throws BackendlessException
  {
    return getPoints( geofenceName, new BackendlessGeoQuery() );
  }

  public void getPoints( final String geofenceName, final AsyncCallback<List<GeoPoint>> responder )
  {
    getPoints( geofenceName, new BackendlessGeoQuery(), responder );
  }

  public void relativeFind( final BackendlessGeoQuery geoQuery,
                            final AsyncCallback<List<SearchMatchesResult>> responder )
  {
    try
    {
      if( geoQuery == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_GEO_QUERY );

      if( geoQuery.getRelativeFindMetadata().isEmpty() || geoQuery.getRelativeFindPercentThreshold() == 0 )
        throw new IllegalArgumentException( ExceptionMessage.INCONSISTENT_GEO_RELATIVE );

      CollectionAdaptingPolicy<SearchMatchesResult> adaptingPolicy = new CollectionAdaptingPolicy<>();

      Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "relativeFind", new Object[] { geoQuery }, new AsyncCallback<List<SearchMatchesResult>>()
      {
        @Override
        public void handleResponse( List<SearchMatchesResult> response )
        {
          if( responder != null )
            responder.handleResponse( response );
        }

        @Override
        public void handleFault( BackendlessFault fault )
        {
          if( responder != null )
            responder.handleFault( fault );
        }
      }, new AdaptingResponder<>( SearchMatchesResult.class, adaptingPolicy ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public List<GeoCategory> getCategories() throws BackendlessException
  {
    CollectionAdaptingPolicy<GeoCategory> adaptingPolicy = new CollectionAdaptingPolicy<>();
    return Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "getCategories", new Object[] {}, new AdaptingResponder<>( GeoCategory.class, adaptingPolicy )  );
  }

  public void getCategories( final AsyncCallback<List<GeoCategory>> responder )
  {
    CollectionAdaptingPolicy<GeoCategory> adaptingPolicy = new CollectionAdaptingPolicy<>();
    Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "getCategories", new Object[] {}, new AsyncCallback<List<GeoCategory>>()
    {
      @Override
      public void handleResponse( final List<GeoCategory> response )
      {
        if( responder != null )
          responder.handleResponse( response );
      }

      @Override
      public void handleFault( final BackendlessFault fault )
      {
        if( responder != null )
          responder.handleFault( fault );
      }
    }, new AdaptingResponder<>( GeoCategory.class, adaptingPolicy ) );
  }

  public GeoPoint loadMetadata( final GeoPoint geoPoint )
  {
    Map<String, Object> metadata;
    if( geoPoint instanceof GeoCluster )
    {
      metadata = Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "loadMetadata", new Object[] { geoPoint.getObjectId(), ((GeoCluster) geoPoint).getGeoQuery() } );
    }
    else
    {
      metadata = Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "loadMetadata", new Object[] { geoPoint.getObjectId(), null } );
    }

    geoPoint.setMetadata( metadata );
    return geoPoint;
  }

  public void loadMetadata( final GeoPoint geoPoint, final AsyncCallback<GeoPoint> responder )
  {
    AsyncCallback<Map<String, Object>> invoker = new AsyncCallback<Map<String, Object>>()
    {
      @Override
      public void handleResponse( Map<String, Object> response )
      {
        geoPoint.setMetadata( response );

        if( responder != null )
          responder.handleResponse( geoPoint );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        if( responder != null )
          responder.handleFault( fault );
      }
    };

    if( geoPoint instanceof GeoCluster )
    {
      Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "loadMetadata", new Object[] { geoPoint.getObjectId(), ((GeoCluster) geoPoint).getGeoQuery() }, invoker );
    }
    else
    {
      Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "loadMetadata", new Object[] { geoPoint.getObjectId(), null }, invoker );
    }
  }

  public int runOnEnterAction( String geoFenceName ) throws BackendlessException
  {
    return (Integer) Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "runOnEnterAction", new Object[] { geoFenceName } );
  }

  public void runOnEnterAction( String geoFenceName,
                                final AsyncCallback<Integer> responder ) throws BackendlessException
  {
    Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "runOnEnterAction", new Object[] { geoFenceName }, new AsyncCallback<Integer>()
    {
      @Override
      public void handleResponse( Integer response )
      {
        if( responder != null )
          responder.handleResponse( response );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        if( responder != null )
          responder.handleFault( fault );
      }
    } );
  }

  public void runOnEnterAction( String geoFenceName, GeoPoint geoPoint ) throws BackendlessException
  {
    Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "runOnEnterAction", new Object[] { geoFenceName, geoPoint } );
  }

  public void runOnEnterAction( String geoFenceName, GeoPoint geoPoint,
                                final AsyncCallback<Void> responder ) throws BackendlessException
  {
    Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "runOnEnterAction", new Object[] { geoFenceName, geoPoint }, new AsyncCallback<Void>()
    {
      @Override
      public void handleResponse( Void response )
      {
        if( responder != null )
          responder.handleResponse( response );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        if( responder != null )
          responder.handleFault( fault );
      }
    } );
  }

  public int runOnStayAction( String geoFenceName ) throws BackendlessException
  {
    return (Integer) Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "runOnStayAction", new Object[] { geoFenceName } );
  }

  public void runOnStayAction( String geoFenceName, final AsyncCallback<Integer> responder ) throws BackendlessException
  {
    Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "runOnStayAction", new Object[] { geoFenceName }, new AsyncCallback<Integer>()
    {
      @Override
      public void handleResponse( Integer response )
      {
        if( responder != null )
          responder.handleResponse( response );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        if( responder != null )
          responder.handleFault( fault );
      }
    } );
  }

  public void runOnStayAction( String geoFenceName, GeoPoint geoPoint ) throws BackendlessException
  {
    Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "runOnStayAction", new Object[] { geoFenceName, geoPoint } );
  }

  public void runOnStayAction( String geoFenceName, GeoPoint geoPoint,
                               final AsyncCallback<Void> responder ) throws BackendlessException
  {
    Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "runOnStayAction", new Object[] { geoFenceName, geoPoint }, new AsyncCallback<Void>()
    {
      @Override
      public void handleResponse( Void response )
      {
        if( responder != null )
          responder.handleResponse( response );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        if( responder != null )
          responder.handleFault( fault );
      }
    } );
  }

  public int runOnExitAction( String geoFenceName ) throws BackendlessException
  {
    return (Integer) Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "runOnExitAction", new Object[] { geoFenceName } );
  }

  public void runOnExitAction( String geoFenceName, final AsyncCallback<Integer> responder ) throws BackendlessException
  {
    Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "runOnExitAction", new Object[] { geoFenceName }, new AsyncCallback<Integer>()
    {
      @Override
      public void handleResponse( Integer response )
      {
        if( responder != null )
          responder.handleResponse( response );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        if( responder != null )
          responder.handleFault( fault );
      }
    } );
  }

  public void runOnExitAction( String geoFenceName, GeoPoint geoPoint ) throws BackendlessException
  {
    Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "runOnExitAction", new Object[] { geoFenceName, geoPoint } );
  }

  public void runOnExitAction( String geoFenceName, GeoPoint geoPoint,
                               final AsyncCallback<Void> responder ) throws BackendlessException
  {
    Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "runOnExitAction", new Object[] { geoFenceName, geoPoint }, new AsyncCallback<Void>()
    {
      @Override
      public void handleResponse( Void response )
      {
        if( responder != null )
          responder.handleResponse( response );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        if( responder != null )
          responder.handleFault( fault );
      }
    } );
  }

  public void setLocationTrackerParameters( int minTime, int minDistance, int acceptedDistanceAfterReboot )
  {
    LocationTracker.setLocationTrackerParameters( minTime, minDistance, acceptedDistanceAfterReboot );
  }

  public void startGeofenceMonitoring( GeoPoint geoPoint,
                                       final AsyncCallback<Void> responder ) throws BackendlessException
  {
    ICallback bCallback = new ServerCallback( geoPoint );

    startGeofenceMonitoring( bCallback, responder );
  }

  public void startGeofenceMonitoring( IGeofenceCallback callback,
                                       final AsyncCallback<Void> responder ) throws BackendlessException
  {
    ICallback bCallback = new ClientCallback( callback );

    startGeofenceMonitoring( bCallback, responder );
  }

  public void startGeofenceMonitoring( String geofenceName, GeoPoint geoPoint,
                                       final AsyncCallback<Void> responder ) throws BackendlessException
  {
    ICallback bCallback = new ServerCallback( geoPoint );

    startGeofenceMonitoring( bCallback, geofenceName, responder );
  }

  public void startGeofenceMonitoring( String geofenceName, IGeofenceCallback callback,
                                       final AsyncCallback<Void> responder ) throws BackendlessException
  {
    ICallback bCallback = new ClientCallback( callback );

    startGeofenceMonitoring( bCallback, geofenceName, responder );
  }

  public void stopGeofenceMonitoring() throws NullPointerException
  {
    if( LocationTracker.getInstance() == null )
      throw new NullPointerException( ExceptionMessage.NOT_ADD_SERVICE_TO_MANIFEST );

    GeoFenceMonitoring geoFenceMonitoring = ((GeoFenceMonitoring) LocationTracker.getInstance().getListener( GeoFenceMonitoring.NAME ));
    if( geoFenceMonitoring == null )
      return;

    geoFenceMonitoring.removeGeoFences();
    LocationTracker.getInstance().removeListener( GeoFenceMonitoring.NAME );
  }

  public void stopGeofenceMonitoring( String geofenceName ) throws NullPointerException
  {
    if( LocationTracker.getInstance() == null )
      throw new NullPointerException( ExceptionMessage.NOT_ADD_SERVICE_TO_MANIFEST );

    GeoFenceMonitoring geoFenceMonitoring = ((GeoFenceMonitoring) LocationTracker.getInstance().getListener( GeoFenceMonitoring.NAME ));
    if( geoFenceMonitoring == null )
    {
      return;
    }
    geoFenceMonitoring.removeGeoFence( geofenceName );
    if( !((GeoFenceMonitoring) LocationTracker.getInstance().getListener( GeoFenceMonitoring.NAME )).isMonitoring() )
    {
      LocationTracker.getInstance().removeListener( GeoFenceMonitoring.NAME );
    }
  }

  private void startGeofenceMonitoring( final ICallback callback, final AsyncCallback<Void> responder )
  {
    Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "getFences", new Object[] {}, new AsyncCallback<Object[]>()
    {
      @Override
      public void handleResponse( Object[] geoFences )
      {
        try
        {
          if( geoFences.length == 0 || !(geoFences instanceof GeoFence[]) )
            throw new Resources.NotFoundException( ExceptionMessage.NOT_FOUND_GEOFENCE );

          addFenceMonitoring( callback, (GeoFence[]) geoFences );

          if( responder != null )
            responder.handleResponse( null );
        }
        catch( Exception ex )
        {
          if( responder != null )
            responder.handleFault( new BackendlessFault( ex ) );
        }
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        responder.handleFault( fault );
      }
    } );
  }

  private void startGeofenceMonitoring( final ICallback callback, String geofenceName,
                                        final AsyncCallback<Void> responder )
  {
//     if (GeoFenceMonitoring.getInstance().containsGeoFence( geofenceName ))
//       throw new BackendlessException( String.format( ExceptionMessage.GEOFENCE_ALREADY_MONITORING, geofenceName ) );

    if( GeoFenceMonitoring.getInstance().containsGeoFence( geofenceName ) && responder != null )
    {
      responder.handleFault( new BackendlessFault( String.format( ExceptionMessage.GEOFENCE_ALREADY_MONITORING, geofenceName ) ) );
      return;
    }

    Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "getFence", new Object[] { geofenceName }, new AsyncCallback<GeoFence>()
    {
      @Override
      public void handleResponse( GeoFence geoFences )
      {
        try
        {
          addFenceMonitoring( callback, geoFences );

          if( responder != null )
            responder.handleResponse( null );
        }
        catch( Exception ex )
        {
          if( responder != null )
            responder.handleFault( new BackendlessFault( ex ) );
        }
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        responder.handleFault( fault );
      }
    } );
  }

  private void addFenceMonitoring( ICallback callback, GeoFence... geoFences ) throws NullPointerException
  {

    if( geoFences.length == 0 )
    {
      return;
    }

    GeoFenceMonitoring geoFenceMonitoring = getGeoFenceMonitoring();

    if( geoFences.length == 1 )
    {
      geoFenceMonitoring.addGeoFence( geoFences[ 0 ], callback );
    }
    else
    {
      geoFenceMonitoring.addGeoFences( new HashSet<GeoFence>( Arrays.asList( geoFences ) ), callback );
    }

    try
    {
      LocationTracker.getInstance().addListener( GeoFenceMonitoring.NAME, geoFenceMonitoring );
    }
    catch( RuntimeException e )
    {
      GeoFenceMonitoring.getInstance().removeGeoFences();
      throw e;
    }
  }

  private GeoFenceMonitoring getGeoFenceMonitoring() throws NullPointerException
  {
    if( LocationTracker.getInstance() == null )
      throw new NullPointerException( ExceptionMessage.NOT_ADD_SERVICE_TO_MANIFEST );

    GeoFenceMonitoring geoFenceMonitoring = (GeoFenceMonitoring) LocationTracker.getInstance().getListener( GeoFenceMonitoring.NAME );
    if( geoFenceMonitoring == null )
    {
      geoFenceMonitoring = GeoFenceMonitoring.getInstance();
    }

    return geoFenceMonitoring;
  }

  private void setReferenceToCluster( List<GeoPoint> collection, BackendlessGeoQuery geoQuery )
  {
    BackendlessGeoQuery protectedQuery = new ProtectedBackendlessGeoQuery( geoQuery );
    for( GeoPoint geoPoint : collection )
    {
      if( geoPoint instanceof GeoCluster )
        ((GeoCluster) geoPoint).setGeoQuery( protectedQuery );
    }
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

  private void checkCoordinates( Double latitude, Double longitude ) throws BackendlessException
  {
    if( latitude == null && longitude == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_COORDINATES );

    if( latitude == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_LATITUDE );

    if( longitude == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_LONGITUDE );

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

    if( geoQuery.getDpp() != null )
    {
      if( geoQuery.getDpp() < 0 || (geoQuery.getClusterGridSize() != null && geoQuery.getClusterGridSize() < 0) )
      {
        throw new IllegalArgumentException( ExceptionMessage.WRONG_CLUSTERISATION_QUERY );
      }
    }
  }

  public int getGeopointCount( BackendlessGeoQuery query )
  {
    Object[] args = new Object[] { query };
    return Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "count", args );
  }

  public int getGeopointCount( String geoFenceName, BackendlessGeoQuery query )
  {
    Object[] args = new Object[] { geoFenceName, query };
    return Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "count", args );
  }

  public void getGeopointCount( BackendlessGeoQuery query, AsyncCallback<Integer> responder )
  {
    try
    {
      Object[] args = new Object[] { query };
      Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "count", args, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public void getGeopointCount( String geoFenceName, BackendlessGeoQuery query, AsyncCallback<Integer> responder )
  {
    try
    {
      Object[] args = new Object[] { geoFenceName, query };
      Invoker.invokeAsync( GEO_MANAGER_SERVER_ALIAS, "count", args, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }
}
