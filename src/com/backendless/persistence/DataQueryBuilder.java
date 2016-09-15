package com.backendless.persistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataQueryBuilder
{
  private PagedQueryBuilder<DataQueryBuilder> pagedQueryBuilder;
  private QueryOptionsBuilder<DataQueryBuilder> queryOptionsBuilder;
  private List<String> properties;
  private String whereClause;

  private DataQueryBuilder()
  {
    properties = new ArrayList<>();
    pagedQueryBuilder = new PagedQueryBuilder<>( this );
    queryOptionsBuilder = new QueryOptionsBuilder<>( this );
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
    dataQuery.setWhereClause( whereClause );

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
    return properties;
  }

  public DataQueryBuilder setProperties( List<String> properties )
  {
    this.properties = properties;
    return this;
  }

  public DataQueryBuilder setProperties( String... properties )
  {
    Collections.addAll( this.properties, properties );
    return this;
  }

  public DataQueryBuilder addProperty( String property )
  {
    this.properties.add( property );
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

  public int getRelationsDepth()
  {
    return queryOptionsBuilder.getRelationsDepth();
  }

  public DataQueryBuilder setRelationsDepth( int relationsDepth )
  {
    return queryOptionsBuilder.setRelationsDepth( relationsDepth );
  }
}
