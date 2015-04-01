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

package com.backendless.geo;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baas on 01.04.15.
 */
public class GeoFenceMonitoring
{
  private GeofenceCallback geofenceCallback;
  private GeoPoint geoPoint;

  private List<GeoFence> geoFences;
  private List<GeoFence> pointFences;

  private void process( Location location )
  {
    geoPoint.setLatitude( location.getLatitude() );
    geoPoint.setLongitude( location.getLongitude() );

    List<GeoFence> oldFences, newFences;

    oldFences = pointFences;
    pointFences = findPointsFence( geoPoint, geoFences );
    newFences = pointFences;

    newFences.removeAll( oldFences );
    oldFences.removeAll( pointFences );
  }

  private List<GeoFence> findPointsFence( GeoPoint geoPoint, List<GeoFence> geoFences )
  {
    List<GeoFence> pointFences = new ArrayList<GeoFence>();

    for( GeoFence geoFence : geoFences )
    {
      boolean defRect = isDefiniteRect( geoFence.getNwPoint(), geoFence.getSePoint() );

      if( !defRect )
      {
        definiteRect( geoFence );
      }

      if( !GeoMath.isPointInRectangular( geoPoint, geoFence.getNwPoint(), geoFence.getSePoint() ) )
      {
        continue;
      }

      if( geoFence.getType() == FenceType.CIRCLE && !GeoMath.isPointInCircle( geoPoint, geoFence.getNodes().get( 0 ), GeoMath.distance( geoFence.getNodes().get( 0 ).getLatitude(), geoFence.getNodes().get( 0 ).getLongitude(), geoFence.getNodes().get( 1 ).getLatitude(), geoFence.getNodes().get( 1 ).getLongitude() ) ) )
      {
        continue;
      }

      if( geoFence.getType() == FenceType.SHAPE && !GeoMath.isPointInShape( geoPoint, geoFence.getNodes() ) )
      {
        continue;
      }

      pointFences.add( geoFence );
    }

    return pointFences;
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
}
