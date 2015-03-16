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

import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.ExceptionMessage;

/**
 * Created by baas on 20.01.15.
 */
public class GeoCluster extends GeoPoint
{
  private int totalPoints;
  private BackendlessGeoQuery geoQuery = null;

  public int getTotalPoints()
  {
    return totalPoints;
  }

  public void setTotalPoints( int totalPoints )
  {
    this.totalPoints = totalPoints;
  }

  public BackendlessGeoQuery getGeoQuery()
  {
    return geoQuery;
  }

  public void setGeoQuery( BackendlessGeoQuery geoQuery )
  {
    if( this.geoQuery == null )
    {
      this.geoQuery = geoQuery;
    }
    else
    {
      throw new BackendlessException( ExceptionMessage.GEO_QUERY_SET_PERMISSION );
    }
  }

  @Override
  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( o == null || this.getClass() != o.getClass() )
    {
      return false;
    }

    GeoCluster that = (GeoCluster) o;

    if( objectId != null ? !objectId.equals( that.objectId ) : that.objectId != null )
      return false;
    if( totalPoints == that.totalPoints )
      return false;
    if( !latitude.equals( that.latitude ) )
      return false;
    if( !longitude.equals( that.longitude ) )
      return false;
    if( metadata != null ? !metadata.equals( that.metadata ) : that.metadata != null )
      return false;
    if( categories != null ? !categories.equals( that.categories ) : that.categories != null )
      return false;

    return true;
  }

  @Override
  public int hashCode()
  {
    int result;
    long temp;
    result = objectId.hashCode();
    result = 31 * result + totalPoints;
    temp = Double.doubleToLongBits( latitude );
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits( longitude );
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + (categories != null ? categories.hashCode() : 0);
    result = 31 * result + (metadata != null ? metadata.hashCode() : 0);
    return result;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder( "GeoCluster{" );
    sb.append( "objectId='" ).append( objectId ).append( '\'' );
    sb.append( ", latitude=" ).append( latitude );
    sb.append( ", longitude=" ).append( longitude );
    sb.append( ", categories=" ).append( categories );
    sb.append( ", metadata=" ).append( metadata );
    sb.append( ", distance=" ).append( distance );
    sb.append( ", totalPoints=" ).append( totalPoints );
    sb.append( '}' );
    return sb.toString();
  }
}
