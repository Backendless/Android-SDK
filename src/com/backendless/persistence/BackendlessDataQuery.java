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

package com.backendless.persistence;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.IBackendlessQuery;

import java.util.ArrayList;
import java.util.List;

public class BackendlessDataQuery implements IBackendlessQuery
{
  private List<String> properties;
  private String whereClause;
  private QueryOptions queryOptions;

  public BackendlessDataQuery()
  {
  }

  public BackendlessDataQuery( List<String> properties )
  {
    this.properties = properties;
  }

  public BackendlessDataQuery( String whereClause )
  {
    this.whereClause = whereClause;
  }

  public BackendlessDataQuery( QueryOptions queryOptions )
  {
    this.queryOptions = queryOptions;
  }

  public BackendlessDataQuery( List<String> properties, String whereClause, QueryOptions queryOptions )
  {
    this.properties = properties;
    this.whereClause = whereClause;
    this.queryOptions = queryOptions;
  }

  public List<String> getProperties()
  {
    if( properties == null )
      return properties = new ArrayList<String>();

    return new ArrayList<String>( properties );
  }

  public void setProperties( List<String> properties )
  {
    this.properties = properties;
  }

  public void addProperty( String property )
  {
    if( property == null || property.equals( "" ) )
      return;

    if( properties == null )
      properties = new ArrayList<String>();

    properties.add( property );
  }

  public String getWhereClause()
  {
    return whereClause;
  }

  public void setWhereClause( String whereClause )
  {
    this.whereClause = whereClause;
  }

  public QueryOptions getQueryOptions()
  {
    if(queryOptions == null)
      return null;

    return queryOptions.newInstance();
  }

  public void setQueryOptions( QueryOptions queryOptions )
  {
    this.queryOptions = queryOptions;
  }

  //PageSize properties added, because DataQuery and GeoQuery has different architecture
  public int getPageSize()
  {
    return queryOptions == null ? 10 : queryOptions.getPageSize();
  }

  public void setPageSize( int pageSize )
  {
    if( queryOptions == null )
      queryOptions = new QueryOptions();

    queryOptions.setPageSize( pageSize );
  }

  public int getOffset()
  {
    return queryOptions == null ? 0 : queryOptions.getOffset();
  }

  public void setOffset( int offset )
  {
    if( queryOptions == null )
      queryOptions = new QueryOptions();

    queryOptions.setOffset( offset );
  }

  @Override
  public BackendlessDataQuery newInstance()
  {
    BackendlessDataQuery result = new BackendlessDataQuery();
    result.setProperties( getProperties() );
    result.setWhereClause( whereClause );
    result.setQueryOptions( getQueryOptions() );

    return result;
  }

  @Override
  public BackendlessCollection getPage( BackendlessCollection sourceCollection, int pageSize, int offset )
  {
    BackendlessDataQuery tempQuery = newInstance();
    tempQuery.setOffset( offset );
    tempQuery.setPageSize( pageSize );
    String tableName = sourceCollection.getTableName();

    if( tableName != null )
      return Backendless.Persistence.of( tableName ).find( tempQuery );
    else
      return Backendless.Persistence.find( sourceCollection.getType(), tempQuery );
  }
}