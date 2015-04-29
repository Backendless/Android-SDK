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

import java.util.List;

/**
 * Created by baas on 01.04.15.
 */
public class GeoMath
{
  public static final double EARTH_RADIUS = 6378100.0; //meters

  public static double distance( double lat1, double lon1, double lat2, double lon2 )
  {
    double deltaLon = lon1 - lon2;

    deltaLon = (deltaLon * Math.PI) / 180;
    lat1 = (lat1 * Math.PI) / 180;
    lat2 = (lat2 * Math.PI) / 180;

    return EARTH_RADIUS * Math.acos( Math.sin( lat1 ) * Math.sin( lat2 ) + Math.cos( lat1 ) * Math.cos( lat2 ) * Math.cos( deltaLon ) );
  }

  // for circle
  public static double[] getOutRectangle( double latitude, double longitude, double r )
  {
    double boundLat = latitude + (180 * r) / (Math.PI * EARTH_RADIUS) * (latitude > 0 ? 1 : -1);
    double littleRadius = countLittleRadius( boundLat );
    double westLong, eastLong, northLat, southLat;

    if( littleRadius > r )
    {
      westLong = longitude - (180 * r) / littleRadius;
      eastLong = 2 * longitude - westLong;

      westLong = updateDegree( westLong );
      eastLong = eastLong % 360 == 180 ? 180 : updateDegree( eastLong );
    }
    else
    {
      westLong = -180;
      eastLong = 180;
    }

    if( latitude > 0 )
    {
      northLat = boundLat;
      southLat = 2 * latitude - boundLat;
    }
    else
    {
      southLat = boundLat;
      northLat = 2 * latitude - boundLat;
    }

    return new double[] { Math.min( northLat, 90 ), westLong, Math.max( southLat, -90 ), eastLong };
  }

  private static double countLittleRadius( double latitude )
  {
    double h = Math.abs( latitude ) / 180 * EARTH_RADIUS;
    double diametre = 2 * EARTH_RADIUS;
    double l_2 = (Math.pow( diametre, 2 ) - diametre * Math.sqrt( Math.pow( diametre, 2 ) - 4 * Math.pow( h, 2 ) )) / 2;
    double littleRadius = diametre / 2 - Math.sqrt( l_2 - Math.pow( h, 2 ) );

    return littleRadius;
  }

  public static double[] getOutRectangle( GeoPoint center, GeoPoint bounded )
  {
    return getOutRectangle( center.getLatitude(), center.getLongitude(), distance( center.getLatitude(), center.getLongitude(), bounded.getLatitude(), bounded.getLongitude() ) );
  }

  // for shape
  public static double[] getOutRectangle( List<GeoPoint> geoPoints )
  {
    double nwLat = geoPoints.get( 0 ).getLatitude();
    double nwLon = geoPoints.get( 0 ).getLongitude();
    double seLat = geoPoints.get( 0 ).getLatitude();
    double seLon = geoPoints.get( 0 ).getLongitude();
    double minLon = 0, maxLon = 0, lon = 0;

    for( int i = 1; i < geoPoints.size(); i++ )
    {
      if( geoPoints.get( i ).getLatitude() > nwLat )
      {
        nwLat = geoPoints.get( i ).getLatitude();
      }

      if( geoPoints.get( i ).getLatitude() < seLat )
      {
        seLat = geoPoints.get( i ).getLatitude();
      }

      double deltaLon = geoPoints.get( i ).getLongitude() - geoPoints.get( i - 1 ).getLongitude();

      if( deltaLon < 0 && deltaLon > -180 || deltaLon > 270 )
      {
        if( deltaLon > 270 )
          deltaLon -= 360;
        lon += deltaLon;

        if( lon < minLon )
          minLon = lon;
      }
      else if( deltaLon > 0 && deltaLon <= 180 || deltaLon <= -270 )
      {
        if( deltaLon <= -270 )
          deltaLon += 360;
        lon += deltaLon;

        if( lon > maxLon )
          maxLon = lon;
      }
    }
    nwLon += minLon;
    seLon += maxLon;

    if( seLon - nwLon >= 360 )
    {
      seLon = 180;
      nwLon = -180;
    }
    else
    {
      seLon = updateDegree( seLon );
      nwLon = updateDegree( nwLon );
    }

    return new double[] { nwLat, nwLon, seLat, seLon };
  }

  public static double updateDegree( double degree )
  {
    degree += 180;
    while( degree < 0 )
    {
      degree += 360;
    }
    return degree == 0 ? 180 : degree % 360 - 180;
  }

  public static boolean isPointInCircle( GeoPoint point, GeoPoint center, double radius )
  {
    return distance( point.getLatitude(), point.getLongitude(), center.getLatitude(), center.getLongitude() ) <= radius;
  }

  public static boolean isPointInRectangular( GeoPoint point, GeoPoint nwPoint, GeoPoint sePoint )
  {
    if( point.getLatitude() > nwPoint.getLatitude() || point.getLatitude() < sePoint.getLatitude() )
    {
      return false;
    }

    if( nwPoint.getLongitude() > sePoint.getLongitude() )
    {
      return point.getLongitude() >= nwPoint.getLongitude() || point.getLongitude() <= sePoint.getLongitude();
    }
    else
    {

      return point.getLongitude() >= nwPoint.getLongitude() && point.getLongitude() <= sePoint.getLongitude();
    }
  }

  public static boolean isPointInShape( GeoPoint point, List<GeoPoint> shape )
  {
    int count = 0;
    for( int i = 0; i < shape.size(); i++ )
    {
      PointPosition position = getPointPosition( point, shape.get( i ), shape.get( (i + 1) % shape.size() ) );
      switch( position )
      {
        case INTERSECT:
        {
          count++;
          break;
        }
        case ON_LINE:
        case NO_INTERSECT:
        default:
          break;
      }
    }

    return count % 2 == 1;
  }

  private static PointPosition getPointPosition( GeoPoint point, GeoPoint first, GeoPoint second )
  {
    double delta = second.getLongitude() - first.getLongitude();
    if( delta < 0 && delta > -180 || delta > 180 )
    {
      GeoPoint tmp = first;
      first = second;
      second = tmp;
    }

    if( point.getLatitude() < first.getLatitude() == point.getLatitude() < second.getLatitude() )
    {
      return PointPosition.NO_INTERSECT;
    }

    double x = point.getLongitude() - first.getLongitude();
    if( x < 0 && x > -180 || x > 180 )
    {
      x = (x - 360) % 360;
    }
    double x2 = (second.getLongitude() - first.getLongitude() + 360) % 360;
    double result = x2 * (point.getLatitude() - first.getLatitude()) / (second.getLatitude() - first.getLatitude()) - x;

    if( result > 0 )
    {
      return PointPosition.INTERSECT;
    }

    return PointPosition.NO_INTERSECT;
  }

  private enum PointPosition
  {
    ON_LINE, INTERSECT, NO_INTERSECT
  }
}
