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

package com.backendless.geo.geofence;

import android.location.Location;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.geo.GeoMath;
import com.backendless.geo.GeoPoint;
import com.backendless.geo.IBackendlessLocationListener;
import com.backendless.geo.LocationTracker;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by baas on 01.04.15.
 */
public class GeoFenceMonitoring implements IBackendlessLocationListener
{
  public static String NAME = "GeoFenceMonitoring";

  private static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
  private Set<GeoFence> onStaySet = Collections.synchronizedSet( new HashSet<GeoFence>() );

  private Map<GeoFence, ICallback> fencesToCallback = Collections.synchronizedMap( new HashMap<GeoFence, ICallback>() );
  private Set<GeoFence> pointFences = new HashSet<GeoFence>();

  private volatile Location location;

  public GeoFenceMonitoring()
  {
  }

  public static class SingletonHolder
  {
    public static final GeoFenceMonitoring HOLDER_INSTANCE = new GeoFenceMonitoring();
  }

  public static GeoFenceMonitoring getInstance()
  {
    return SingletonHolder.HOLDER_INSTANCE;
  }

  @Override
  public void onLocationChanged( Location location )
  {
    Set<GeoFence> oldFences, newFences, currFence;

    synchronized( this )
    {
      this.location = location;
      oldFences = pointFences;
      currFence = findGeoPointsFence( new GeoPoint( location.getLatitude(), location.getLongitude() ), fencesToCallback.keySet() );
      newFences = new HashSet<GeoFence>( currFence );

      newFences.removeAll( oldFences );
      oldFences.removeAll( currFence );

      callOnEnter( newFences );
      callOnStay( newFences );
      callOnExit( oldFences );
      cancelOnStay( oldFences );

      pointFences = currFence;
    }
  }

  @Override
  public void onLocationChanged( Location oldLocation, Location newLocation )
  {
    if( newLocation == null )
    {
      pointFences.clear();
      onStaySet.clear();
      return;
    }

    if( oldLocation.distanceTo( newLocation ) > LocationTracker.ACCEPTABLE_DISTANCE )
    {
      pointFences.clear();
      onStaySet.clear();
    }

    onLocationChanged( newLocation );
  }

  private void callOnEnter( Set<GeoFence> geoFences )
  {
    for( GeoFence geoFence : geoFences )
    {
      fencesToCallback.get( geoFence ).callOnEnter( geoFence, location );
    }
  }

  private void callOnStay( Set<GeoFence> geoFences )
  {
    for( GeoFence geoFence : geoFences )
    {
      if( geoFence.getOnStayDuration() > 0 )
        addOnStay( geoFence );
    }
  }

  private void cancelOnStay( Set<GeoFence> geoFences )
  {
    for( GeoFence geoFence : geoFences )
    {
      cancelOnStay( geoFence );
    }
  }

  private void callOnExit( Set<GeoFence> geoFences )
  {
    for( GeoFence geoFence : geoFences )
    {
      fencesToCallback.get( geoFence ).callOnExit( geoFence, location );
    }
  }

  public void addGeoFences( Set<GeoFence> geoFences, ICallback callback )
  {
    if( !fencesToCallback.isEmpty() )
      throw new BackendlessException( ExceptionMessage.GEOFENCES_MONITORING );

    for( GeoFence geoFence : geoFences )
    {
      addGeoFence( geoFence, callback );
    }
  }

  public void addGeoFence( GeoFence geoFence, ICallback callback )
  {
    if( fencesToCallback.containsKey( geoFence ) )
    {
      if( !fencesToCallback.get( geoFence ).equalCallbackParameter( callback ) )
        throw new BackendlessException( String.format( ExceptionMessage.GEOFENCE_ALREADY_MONITORING, geoFence.getGeofenceName() ) );
      return;
    }

    if( !isDefiniteRect( geoFence.getNwPoint(), geoFence.getSePoint() ) )
    {
      definiteRect( geoFence );
    }
    this.fencesToCallback.put( geoFence, callback );

    if( location != null && isPointInFence( new GeoPoint( location.getLatitude(), location.getLongitude() ), geoFence ) )
    {
      pointFences.add( geoFence );
      callback.callOnEnter( geoFence, location );
      addOnStay( geoFence );
    }
  }

  public void removeGeoFence( String geoFenceName )
  {
    GeoFence removed = new GeoFence( geoFenceName );
    if( fencesToCallback.containsKey( removed ) )
    {
      fencesToCallback.remove( removed );
      cancelOnStay( removed );
      pointFences.remove( removed );
    }
  }

  public void removeGeoFences()
  {
    onStaySet.clear();
    pointFences.clear();
    fencesToCallback.clear();
  }

  public boolean isMonitoring()
  {
    return !fencesToCallback.isEmpty();
  }

  private Set<GeoFence> findGeoPointsFence( GeoPoint geoPoint, Set<GeoFence> geoFences )
  {
    Set<GeoFence> pointFences = new HashSet<GeoFence>();

    for( GeoFence geoFence : geoFences )
    {

      if( isPointInFence( geoPoint, geoFence ) )
      {
        pointFences.add( geoFence );
      }
    }

    return pointFences;
  }

  private boolean isPointInFence( GeoPoint geoPoint, GeoFence geoFence )
  {
    if(geoFence.getNwPoint() == null || geoFence.getSePoint() == null)
    {
      definiteRect( geoFence );
    }
    if( !GeoMath.isPointInRectangular( geoPoint, geoFence.getNwPoint(), geoFence.getSePoint() ) )
    {
      return false;
    }

    if( geoFence.getType() == FenceType.CIRCLE && !GeoMath.isPointInCircle( geoPoint, geoFence.getNodes().get( 0 ), GeoMath.distance( geoFence.getNodes().get( 0 ).getLatitude(), geoFence.getNodes().get( 0 ).getLongitude(), geoFence.getNodes().get( 1 ).getLatitude(), geoFence.getNodes().get( 1 ).getLongitude() ) ) )
    {
      return false;
    }

    if( geoFence.getType() == FenceType.SHAPE && !GeoMath.isPointInShape( geoPoint, geoFence.getNodes() ) )
    {
      return false;
    }

    return true;
  }

  private boolean isDefiniteRect( GeoPoint nwPoint, GeoPoint sePoint )
  {
    return nwPoint != null && sePoint != null;
  }

  private void definiteRect( GeoFence geoFence )
  {
    switch( geoFence.getType() )
    {
      case RECT:
      {
        GeoPoint nwPoint = geoFence.getNodes().get( 0 );
        GeoPoint sePoint = geoFence.getNodes().get( 1 );
        geoFence.setNwPoint( nwPoint );
        geoFence.setSePoint( sePoint );
        break;
      }
      case CIRCLE:
      {
        double[] outRect = GeoMath.getOutRectangle( geoFence.getNodes().get( 0 ), geoFence.getNodes().get( 1 ) );
        geoFence.setNwPoint( new GeoPoint( outRect[ 0 ], outRect[ 1 ] ) );
        geoFence.setSePoint( new GeoPoint( outRect[ 2 ], outRect[ 3 ] ) );
        break;
      }
      case SHAPE:
      {
        double[] outRect = GeoMath.getOutRectangle( geoFence.getNodes() );
        geoFence.setNwPoint( new GeoPoint( outRect[ 0 ], outRect[ 1 ] ) );
        geoFence.setSePoint( new GeoPoint( outRect[ 2 ], outRect[ 3 ] ) );
        break;
      }
      default:
    }
  }

  private void addOnStay( final GeoFence geoFence )
  {
    onStaySet.add( geoFence );
    scheduledExecutorService.schedule( new Runnable()
    {
      @Override
      public void run()
      {
        if( onStaySet.contains( geoFence ) )
        {
          fencesToCallback.get( geoFence ).callOnStay( geoFence, location );
          cancelOnStay( geoFence );
        }
      }
    }, geoFence.getOnStayDuration(), TimeUnit.SECONDS );
  }

  private void cancelOnStay( GeoFence geoFence )
  {
    onStaySet.remove( geoFence );
  }
}
