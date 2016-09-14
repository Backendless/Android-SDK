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

import java.util.ArrayList;
import java.util.List;

public class BackendlessDataQuery extends AbstractBackendlessQuery
{
  public static final int DEFAULT_PAGE_SIZE = 10;
  public static final int DEFAULT_OFFSET = 0;

  private List<String> properties;
  private String whereClause;
  private QueryOptions queryOptions;

  {
    setPageSize( DEFAULT_PAGE_SIZE );
    setOffset( DEFAULT_OFFSET );
  }

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
      return properties = new ArrayList<>();

    return new ArrayList<>( properties );
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
      properties = new ArrayList<>();

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
    if( queryOptions == null )
      return null;

    return queryOptions.newInstance();
  }

  public void setQueryOptions( QueryOptions queryOptions )
  {
    this.queryOptions = queryOptions;
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
}