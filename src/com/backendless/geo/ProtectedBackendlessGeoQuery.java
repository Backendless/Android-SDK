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

import java.util.*;

public class ProtectedBackendlessGeoQuery extends BackendlessGeoQuery
{
  private BackendlessGeoQuery geoQuery;

  public ProtectedBackendlessGeoQuery( BackendlessGeoQuery geoQuery )
  {
    this.geoQuery = geoQuery;
  }

  @Override
  public Double getLatitude()
  {
    return geoQuery.getLatitude();
  }

  @Override
  public void setLatitude( Double latitude )
  {
    throw new BackendlessException( String.format( ExceptionMessage.GEO_QUERY_METHOD_PERMISSION, "setLatitude" ) );
  }

  @Override
  public Double getLongitude()
  {
    return geoQuery.getLongitude();
  }

  @Override
  public void setLongitude( Double longitude )
  {
    throw new BackendlessException( String.format( ExceptionMessage.GEO_QUERY_METHOD_PERMISSION, "setLongitude" ) );
  }

  @Override
  public Double getRadius()
  {
    return geoQuery.getRadius();
  }

  @Override
  public void setRadius( Double radius )
  {
    throw new BackendlessException( String.format( ExceptionMessage.GEO_QUERY_METHOD_PERMISSION, "setRadius" ) );
  }

  @Override
  public Units getUnits()
  {
    return geoQuery.getUnits();
  }

  @Override
  public void setUnits( Units units )
  {
    throw new BackendlessException( String.format( ExceptionMessage.GEO_QUERY_METHOD_PERMISSION, "setUnits" ) );
  }

  @Override
  public List<String> getCategories()
  {
    return new ArrayList<String>( geoQuery.getCategories() );
  }

  @Override
  public void setCategories( Collection<String> categories )
  {
    throw new BackendlessException( String.format( ExceptionMessage.GEO_QUERY_METHOD_PERMISSION, "setCategories" ) );
  }

  @Override
  public void addCategory( String category )
  {
    throw new BackendlessException( String.format( ExceptionMessage.GEO_QUERY_METHOD_PERMISSION, "addCategory" ) );
  }

  @Override
  public Map<String, Object> getMetadata()
  {
    return new HashMap<String, Object>( geoQuery.getMetadata() );
  }

  @Override
  public void setMetadata( Map<String, Object> metadata )
  {
    throw new BackendlessException( String.format( ExceptionMessage.GEO_QUERY_METHOD_PERMISSION, "setMetadata" ) );
  }

  @Override
  public void putMetadata( String metadataKey, Object metadataValue )
  {
    throw new BackendlessException( String.format( ExceptionMessage.GEO_QUERY_METHOD_PERMISSION, "putMetadata" ) );
  }

  @Override
  public Map<String, String> getRelativeFindMetadata()
  {
    return new HashMap<String, String>( geoQuery.getRelativeFindMetadata() );
  }

  @Override
  public void setRelativeFindMetadata( Map<String, String> relativeFindMetadata )
  {
    throw new BackendlessException( String.format( ExceptionMessage.GEO_QUERY_METHOD_PERMISSION, "setRelativeFindMetadata" ) );
  }

  @Override
  public void putRelativeFindMetadata( String key, String value )
  {
    throw new BackendlessException( String.format( ExceptionMessage.GEO_QUERY_METHOD_PERMISSION, "putRelativeFindMetadata" ) );
  }

  @Override
  public double getRelativeFindPercentThreshold()
  {
    return geoQuery.getRelativeFindPercentThreshold();
  }

  @Override
  public void setRelativeFindPercentThreshold( double relativeFindPercentThreshold )
  {
    throw new BackendlessException( String.format( ExceptionMessage.GEO_QUERY_METHOD_PERMISSION, "setRelativeFindPercentThreshold" ) );
  }

  @Override
  public String getWhereClause()
  {
    return new String( geoQuery.getWhereClause() );
  }

  @Override
  public void setWhereClause( String whereClause )
  {
    throw new BackendlessException( String.format( ExceptionMessage.GEO_QUERY_METHOD_PERMISSION, "setWhereClause" ) );
  }

  @Override
  public double[] getSearchRectangle()
  {
    return geoQuery.getSearchRectangle().clone();
  }

  @Override
  public void setSearchRectangle( double[] searchRectangle )
  {
    throw new BackendlessException( String.format( ExceptionMessage.GEO_QUERY_METHOD_PERMISSION, "setSearchRectangle" ) );
  }

  @Override
  public void setSearchRectangle( GeoPoint topLeft, GeoPoint bottomRight )
  {
    throw new BackendlessException( String.format( ExceptionMessage.GEO_QUERY_METHOD_PERMISSION, "setSearchRectangle" ) );
  }

  @Override
  public Double getDpp()
  {
    return geoQuery.getDpp();
  }

  @Override
  public void setDpp( Double dpp )
  {
    throw new BackendlessException( String.format( ExceptionMessage.GEO_QUERY_METHOD_PERMISSION, "setDpp" ) );
  }

  @Override
  public Integer getClusterGridSize()
  {
    return geoQuery.getClusterGridSize();
  }

  @Override
  public void setClusterGridSize( Integer clusterSize )
  {
    throw new BackendlessException( String.format( ExceptionMessage.GEO_QUERY_METHOD_PERMISSION, "setClusterSize" ) );
  }

  @Override
  public void setClusteringParams( Double degreePerPixel, Integer clusterSize )
  {
    throw new BackendlessException( String.format( ExceptionMessage.GEO_QUERY_METHOD_PERMISSION, "setClusteringParams" ) );
  }

  @Override
  public void setClusteringParams( double westLongitude, double eastLongitude, int mapWidth )
  {
    throw new BackendlessException( String.format( ExceptionMessage.GEO_QUERY_METHOD_PERMISSION, "setClusteringParams" ) );
  }

  @Override
  public void setClusteringParams( double westLongitude, double eastLongitude, int mapWidth, int clusterGridSize )
  {
    throw new BackendlessException( String.format( ExceptionMessage.GEO_QUERY_METHOD_PERMISSION, "setClusteringParams" ) );
  }

  @Override
  public boolean isIncludeMeta()
  {
    return geoQuery.isIncludeMeta();
  }

  @Override
  public void setIncludeMeta( boolean includeMeta )
  {
    geoQuery.setIncludeMeta( includeMeta );
  }

  @Override
  public int getPageSize()
  {
    return geoQuery.getPageSize();
  }

  @Override
  public void setPageSize( int pageSize )
  {
    geoQuery.setPageSize( pageSize );
  }

  @Override
  public int getOffset()
  {
    return geoQuery.getOffset();
  }

  @Override
  public void setOffset( int offset )
  {
    geoQuery.setOffset( offset );
  }

  @Override
  public BackendlessGeoQuery newInstance()
  {
    return geoQuery.newInstance();
  }
}
