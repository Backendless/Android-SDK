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

  private boolean distinct = false;
  private final ArrayList<String> properties = new ArrayList<>();
  private final ArrayList<String> excludeProperties = new ArrayList<>();
  private String whereClause;
  private QueryOptions queryOptions;
  private final List<String> groupBy = new ArrayList<>();
  private String havingClause = "";

  public BackendlessDataQuery()
  {
  }

  public BackendlessDataQuery( List<String> properties )
  {
    this.properties.addAll( properties );
  }

  public BackendlessDataQuery( String whereClause )
  {
    this.whereClause = whereClause;
  }

  public BackendlessDataQuery( QueryOptions queryOptions )
  {
    this.queryOptions = queryOptions;
  }

  public BackendlessDataQuery( List<String> properties, String whereClause, QueryOptions queryOptions,
                               List<String> groupBy, String havingClause )
  {
    this.setProperties( properties );
    this.whereClause = whereClause;
    this.queryOptions = queryOptions;
    this.setGroupBy( groupBy );
    this.havingClause = havingClause;
  }
  
  public boolean getDistinct()
  {
    return distinct;
  }
  
  public BackendlessDataQuery setDistinct(boolean distinct)
  {
    this.distinct = distinct;
    return this;
  }
  
  public List<String> getProperties()
  {
    return (List<String>) this.properties.clone();
  }

  public void setProperties( List<String> properties )
  {
    this.properties.clear();

    if (properties != null)
      for( String prop: properties )
        this.addProperty( prop );
  }

  public void addProperties( String... properties )
  {
    if( properties != null )
      for( String prop : properties )
        this.addProperty( prop );
  }

  public void addProperty( String property )
  {
    if( property != null && !property.equals( "" ) )
      properties.add( property );
  }

  public ArrayList<String> getExcludeProperties()
  {
    return (ArrayList<String>) excludeProperties.clone();
  }

  public void setExcludeProperties( ArrayList<String> excludeProperties )
  {
    this.excludeProperties.clear();

    if( excludeProperties != null )
      for( String exclProp: excludeProperties )
        this.setExcludeProperty( exclProp );
  }

  public void setExcludeProperties( String... excludeProperties )
  {
    this.excludeProperties.clear();

    if( excludeProperties != null )
      for( String exclProp: excludeProperties )
        this.setExcludeProperty( exclProp );
  }

  public void setExcludeProperty( String excludeProperty )
  {
    if( excludeProperty != null && !excludeProperty.isEmpty() )
      excludeProperties.add( excludeProperty );
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

  public List<String> getGroupBy()
  {
    return new ArrayList<>( this.groupBy );
  }

  public void setGroupBy( List<String> groupBy )
  {
    this.groupBy.clear();

    for( String grb : groupBy )
    {
      if( grb != null && !grb.equals( "" ) )
        this.groupBy.add( grb );
    }
  }

  public String getHavingClause()
  {
    return havingClause;
  }

  public void setHavingClause( String havingClause )
  {
    this.havingClause = havingClause;
  }

  @Override
  public BackendlessDataQuery newInstance()
  {
    BackendlessDataQuery result = new BackendlessDataQuery();
    result.setDistinct( getDistinct() );
    result.setProperties( getProperties() );
    result.setWhereClause( whereClause );
    result.setQueryOptions( getQueryOptions() );
    result.setGroupBy( groupBy );
    result.setHavingClause( havingClause );
    return result;
  }
}