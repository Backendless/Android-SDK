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

import com.backendless.commons.geo.BaseGeoCategory;

public class GeoCategory extends BaseGeoCategory implements Comparable<GeoCategory>
{
  public String getId()
  {
    return objectId;
  }

  @Override
  public boolean equals( Object o )
  {
    if( this == o )
      return true;

    if( o == null || getClass() != o.getClass() )
      return false;

    GeoCategory that = (GeoCategory) o;

    if( !name.equals( that.name ) )
      return false;

    if( objectId != null ? !objectId.equals( that.objectId ) : that.objectId != null )
      return false;

    return true;
  }

  @Override
  public int hashCode()
  {
    int result = objectId != null ? objectId.hashCode() : 0;
    result = 31 * result + name.hashCode();
    return result;
  }

  @Override
  public String toString()
  {
    final StringBuffer sb = new StringBuffer();
    sb.append( "GeoCategory" );
    sb.append( "{name='" ).append( name ).append( '\'' );
    sb.append( ", size=" ).append( size );
    sb.append( '}' );
    return sb.toString();
  }

  @Override
  public int compareTo( GeoCategory arg )
  {
    if( this == arg )
      return 0;

    int nameDiff = this.name == null ? (arg.getName() == null ? 0 : -1) : this.name.compareTo( arg.getName() );
    if( nameDiff != 0 )
      return nameDiff;

    return size - arg.getSize();
  }
}