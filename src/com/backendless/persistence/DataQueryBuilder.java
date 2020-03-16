package com.backendless.persistence;

import java.util.ArrayList;
import java.util.List;


public class DataQueryBuilder
{
  private PagedQueryBuilder<DataQueryBuilder> pagedQueryBuilder;
  private QueryOptionsBuilder<DataQueryBuilder> queryOptionsBuilder;
  private ArrayList<String> properties;
  private ArrayList<String> excludeProperties;
  private String whereClause;
  private List<String> groupBy;
  private String havingClause;

  private DataQueryBuilder()
  {
    properties = new ArrayList<>();
    excludeProperties = new ArrayList<>();
    pagedQueryBuilder = new PagedQueryBuilder<>( this );
    queryOptionsBuilder = new QueryOptionsBuilder<>( this );
    groupBy = new ArrayList<>();
    havingClause = "";
  }

  public static DataQueryBuilder create()
  {
    return new DataQueryBuilder();
  }

  public BackendlessDataQuery build()
  {
    BackendlessDataQuery dataQuery = pagedQueryBuilder.build();

    dataQuery.setQueryOptions( queryOptionsBuilder.build() );
    dataQuery.setProperties( properties );
    dataQuery.setExcludeProperties( excludeProperties );
    dataQuery.setWhereClause( whereClause );
    dataQuery.setGroupBy( groupBy );
    dataQuery.setHavingClause( havingClause );

    return dataQuery;
  }

  /*--- Auto-generated code ---*/

  public DataQueryBuilder setPageSize( int pageSize )
  {
    return pagedQueryBuilder.setPageSize( pageSize );
  }

  public DataQueryBuilder setOffset( int offset )
  {
    return pagedQueryBuilder.setOffset( offset );
  }

  public DataQueryBuilder prepareNextPage()
  {
    return pagedQueryBuilder.prepareNextPage();
  }

  public DataQueryBuilder preparePreviousPage()
  {
    return pagedQueryBuilder.preparePreviousPage();
  }

  public List<String> getProperties()
  {
    return (List<String>) this.properties.clone();
  }

  public DataQueryBuilder setProperties( List<String> properties )
  {
    this.properties.clear();

    if (properties != null)
      for( String prop: properties )
        this.addProperty( prop );

    return this;
  }

  public DataQueryBuilder setProperties( String... properties )
  {
    this.properties.clear();
    this.addProperties( properties );
    return this;
  }

  public DataQueryBuilder addProperties( String... properties )
  {
    if( properties != null )
      for( String prop : properties )
        this.addProperty( prop );

    return this;
  }

  public DataQueryBuilder addProperty( String property )
  {
    if( property != null && !property.equals( "" ) )
      properties.add( property );

    return this;
  }

  public ArrayList<String> getExcludeProperties()
  {
    return (ArrayList<String>) excludeProperties.clone();
  }

  public DataQueryBuilder setExcludeProperties( ArrayList<String> excludeProps )
  {
    this.excludeProperties.clear();

    if( excludeProps != null )
      for( String exclProp: excludeProps )
        this.addExcludeProperty( exclProp );

    return this;
  }

  public DataQueryBuilder addExcludeProperty( String exclProperty )
  {
    if( exclProperty != null && !exclProperty.isEmpty() )
      excludeProperties.add( exclProperty );

    return this;
  }

  public String getWhereClause()
  {
    return whereClause;
  }

  public DataQueryBuilder setWhereClause( String whereClause )
  {
    this.whereClause = whereClause;
    return this;
  }

  public List<String> getSortBy()
  {
    return queryOptionsBuilder.getSortBy();
  }

  public DataQueryBuilder setSortBy( List<String> sortBy )
  {
    return queryOptionsBuilder.setSortBy( sortBy );
  }

  public DataQueryBuilder setSortBy( String... sortBy )
  {
    return queryOptionsBuilder.setSortBy( sortBy );
  }

  public DataQueryBuilder addSortBy( String sortBy )
  {
    return queryOptionsBuilder.addSortBy( sortBy );
  }

  public List<String> getRelated()
  {
    return queryOptionsBuilder.getRelated();
  }

  public DataQueryBuilder setRelated( List<String> related )
  {
    return queryOptionsBuilder.setRelated( related );
  }

  public DataQueryBuilder setRelated( String... related )
  {
    return queryOptionsBuilder.setRelated( related );
  }

  public DataQueryBuilder addRelated( List<String> related )
  {
    return queryOptionsBuilder.addRelated( related );
  }

  public DataQueryBuilder addRelated( String related )
  {
    return queryOptionsBuilder.addRelated( related );
  }

  public Integer getRelationsDepth()
  {
    return queryOptionsBuilder.getRelationsDepth();
  }

  public DataQueryBuilder setRelationsDepth( Integer relationsDepth )
  {
    return queryOptionsBuilder.setRelationsDepth( relationsDepth );
  }

  public List<String> getGroupBy()
  {
    return new ArrayList<>( this.groupBy );
  }

  public DataQueryBuilder setGroupBy( String... groupBy )
  {
    this.groupBy.clear();
    this.addGroupBy( groupBy );
    return this;
  }

  public DataQueryBuilder addGroupBy( String... groupBy )
  {
    for( String grb : groupBy )
    {
      if( grb != null && !grb.equals( "" ) )
        this.groupBy.add( grb );
    }

    return this;
  }

  public String getHavingClause()
  {
    return havingClause;
  }

  public DataQueryBuilder setHavingClause( String havingClause )
  {
    this.havingClause = havingClause;
    return this;
  }

  public DataQueryBuilder setRelationsPageSize( Integer relationsPageSize )
  {
    return queryOptionsBuilder.setRelationsPageSize( relationsPageSize );
  }

  public Integer getRelationPageSize()
  {
    return queryOptionsBuilder.getRelationsPageSize();
  }
}
