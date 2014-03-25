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

import com.backendless.IBackendlessQuery;
import com.backendless.commons.geo.AbstractBackendlessGeoQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackendlessGeoQuery extends AbstractBackendlessGeoQuery implements IBackendlessQuery
{
  private Units units;
  private boolean includeMeta = true;
  private double[] searchRectangle;
  private int pageSize = 100;
  private int offset;

  public BackendlessGeoQuery()
  {
  }

  public BackendlessGeoQuery( double latitude, double longitude, int pageSize, int offset )
  {
    this.latitude = latitude;
    this.longitude = longitude;
    this.pageSize = pageSize;
    this.offset = offset;
  }

  public BackendlessGeoQuery( List<String> categories )
  {
    this.categories = categories;
  }

  public BackendlessGeoQuery( double latitude, double longitude, double radius, Units units )
  {
    this.latitude = latitude;
    this.longitude = longitude;
    this.radius = radius;
    this.units = units;
  }

  public BackendlessGeoQuery( double latitude, double longitude, double radius, Units units, List<String> categories )
  {
    this.latitude = latitude;
    this.longitude = longitude;
    this.radius = radius;
    this.units = units;
    this.categories = categories;
  }

  public BackendlessGeoQuery( double latitude, double longitude, double radius, Units units, List<String> categories,
                              Map<String, String> metadata )
  {
    this.latitude = latitude;
    this.longitude = longitude;
    this.radius = radius;
    this.units = units;
    this.categories = categories;
    this.metadata = metadata;

    if( metadata != null )
      includeMeta = true;
  }

  public BackendlessGeoQuery( Map<String, String> relativeFindMetadata, double relativeFindPercentThreshold )
  {
    this.relativeFindMetadata = relativeFindMetadata;
    this.relativeFindPercentThreshold = relativeFindPercentThreshold;
  }

  public BackendlessGeoQuery( GeoPoint topLeft, GeoPoint bottomRight )
  {
    this.searchRectangle = new double[] { topLeft.getLatitude(), topLeft.getLongitude(), bottomRight.getLatitude(), bottomRight.getLongitude() };
  }

  public BackendlessGeoQuery( double NWLat, double NWLon, double SELat, double SWLon )
  {
    this.searchRectangle = new double[] { NWLat, NWLon, SELat, SWLon };
  }

  public BackendlessGeoQuery( double NWLat, double NWLon, double SELat, double SWLon, Units units,
                              List<String> categories )
  {
    this.searchRectangle = new double[] { NWLat, NWLon, SELat, SWLon };
    this.units = units;
    this.categories = categories;
  }

  public BackendlessGeoQuery( Map<String, String> metadata )
  {
    this.metadata = metadata;

    if( metadata != null )
      includeMeta = true;
  }

  public BackendlessGeoQuery( String metaKey, String metaValue )
  {
    HashMap<String, String> metadata = new HashMap<String, String>();
    metadata.put( metaKey, metaValue );

    this.metadata = metadata;
    includeMeta = true;
  }

  public Units getUnits()
  {
    return units;
  }

  public void setUnits( Units units )
  {
    this.units = units;
  }

  public List<String> getCategories()
  {
    if( categories == null )
      categories = new ArrayList<String>();

    return new ArrayList<String>( categories );
  }

  public void addCategory( String category )
  {
    if( category == null || category.equals( "" ) )
      return;

    if( categories == null )
      categories = new ArrayList<String>();

    categories.add( category );
  }

  public Map<String, String> getMetadata()
  {
    if( metadata == null )
      return metadata = new HashMap<String, String>();

    return new HashMap<String, String>( metadata );
  }

  public void putMetadata( String metadataKey, String metadataValue )
  {
    if( metadataKey == null || metadataKey.equals( "" ) || metadataValue == null )
      return;

    if( metadata == null )
      metadata = new HashMap<String, String>();

    metadata.put( metadataKey, metadataValue );
  }

  public boolean isIncludeMeta()
  {
    return includeMeta;
  }

  public void setIncludeMeta( boolean includeMeta )
  {
    this.includeMeta = includeMeta;
  }

  public double[] getSearchRectangle()
  {
    return searchRectangle;
  }

  public void setSearchRectangle( double[] searchRectangle )
  {
    this.searchRectangle = searchRectangle;
  }

  public void setSearchRectangle( GeoPoint topLeft, GeoPoint bottomRight )
  {
    setSearchRectangle( new double[] { topLeft.getLatitude(), topLeft.getLongitude(), bottomRight.getLatitude(), bottomRight.getLongitude() } );
  }

  public int getOffset()
  {
    return offset;
  }

  public int getPageSize()
  {
    return pageSize;
  }

  public void setPageSize( int pageSize )
  {
    this.pageSize = pageSize;
  }

  public void setOffset( int offset )
  {
    this.offset = offset;
  }

  public void putRelativeFindMetadata( String key, String value )
  {
    relativeFindMetadata.put( key, value );
  }

  @Override
  public BackendlessGeoQuery newInstance()
  {
    BackendlessGeoQuery result = new BackendlessGeoQuery();
    result.setLatitude( latitude );
    result.setLongitude( longitude );
    result.setRadius( radius );
    result.setUnits( units );
    result.setCategories( getCategories() );
    result.setIncludeMeta( isIncludeMeta() );
    result.setMetadata( getMetadata() );
    result.setSearchRectangle( searchRectangle );
    result.setPageSize( pageSize );
    result.setOffset( offset );
    result.setWhereClause( whereClause );
    result.setRelativeFindMetadata( relativeFindMetadata );
    result.setRelativeFindPercentThreshold( relativeFindPercentThreshold );

    return result;
  }
}