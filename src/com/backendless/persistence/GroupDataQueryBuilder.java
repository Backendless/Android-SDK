package com.backendless.persistence;

import com.backendless.commons.persistence.group.GroupingColumnValue;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors( chain = true )
public class GroupDataQueryBuilder
{
  private PagedGroupQueryBuilder<GroupDataQueryBuilder> pagedQueryBuilder;
  private QueryOptionsBuilder<GroupDataQueryBuilder> queryOptionsBuilder;
  @Setter
  private boolean distinct = false;
  private List<GroupingColumnValue> groupPath;
  @Setter @Getter
  private int groupDepth;
  private ArrayList<String> properties;
  private ArrayList<String> excludeProperties;
  @Setter @Getter
  private String whereClause;
  private List<String> groupBy;

  private GroupDataQueryBuilder()
  {
    properties = new ArrayList<>();
    excludeProperties = new ArrayList<>();
    pagedQueryBuilder = new PagedGroupQueryBuilder<>( this );
    queryOptionsBuilder = new QueryOptionsBuilder<>( this );
    groupBy = new ArrayList<>();
    groupPath = new ArrayList<>();
  }

  public static GroupDataQueryBuilder create()
  {
    return new GroupDataQueryBuilder();
  }

  public BackendlessGroupDataQuery build()
  {
    BackendlessGroupDataQuery dataQuery = pagedQueryBuilder.build();
    dataQuery.setDistinct( getDistinct() );
    dataQuery.setQueryOptions( queryOptionsBuilder.build() );
    dataQuery.setProperties( properties );
    dataQuery.setExcludeProperties( excludeProperties );
    dataQuery.setWhereClause( whereClause );
    dataQuery.setGroupBy( groupBy );
    dataQuery.setGroupPath( groupPath );
    dataQuery.setGroupDepth( groupDepth );

    return dataQuery;
  }

  /*--- Auto-generated code ---*/

  public GroupDataQueryBuilder setPageSize( int pageSize )
  {
    return pagedQueryBuilder.setPageSize( pageSize );
  }

  public GroupDataQueryBuilder setGroupPageSize( int groupPageSize )
  {
    return pagedQueryBuilder.setGroupPageSize( groupPageSize );
  }

  public GroupDataQueryBuilder setRecordsPageSize( int recordsPageSize )
  {
    return pagedQueryBuilder.setRecordsPageSize( recordsPageSize );
  }

  public GroupDataQueryBuilder setOffset( int offset )
  {
    return pagedQueryBuilder.setOffset( offset );
  }

  public GroupDataQueryBuilder prepareNextPage()
  {
    return pagedQueryBuilder.prepareNextPage();
  }

  public GroupDataQueryBuilder preparePreviousPage()
  {
    return pagedQueryBuilder.preparePreviousPage();
  }
  
  public boolean getDistinct()
  {
    return distinct;
  }

  public List<GroupingColumnValue> getGroupPath()
  {
    return new ArrayList<>( groupPath );
  }

  public GroupDataQueryBuilder setGroupPath( List<GroupingColumnValue> groupPath )
  {
    this.groupPath = new ArrayList<>( groupPath );
    return this;
  }

  public List<String> getProperties()
  {
    return (List<String>) this.properties.clone();
  }

  public GroupDataQueryBuilder setProperties( List<String> properties )
  {
    this.properties.clear();

    if (properties != null)
      for( String prop: properties )
        this.addProperty( prop );

    return this;
  }

  public GroupDataQueryBuilder setProperties( String... properties )
  {
    this.properties.clear();
    this.addProperties( properties );
    return this;
  }

  public GroupDataQueryBuilder addProperties( String... properties )
  {
    if( properties != null )
      for( String prop : properties )
        this.addProperty( prop );

    return this;
  }

  public GroupDataQueryBuilder addProperty( String property )
  {
    if( property != null && !property.equals( "" ) )
      properties.add( property );

    return this;
  }

  public GroupDataQueryBuilder addAllProperties()
  {
    this.addProperty( "*" );
    return this;
  }

  public ArrayList<String> getExcludedProperties()
  {
    return (ArrayList<String>) excludeProperties.clone();
  }

  public GroupDataQueryBuilder excludeProperties( ArrayList<String> excludeProperties )
  {
    this.excludeProperties.clear();

    if( excludeProperties != null )
      for( String exclProp: excludeProperties )
        this.excludeProperty( exclProp );

    return this;
  }

  public GroupDataQueryBuilder excludeProperties( String... excludeProperties )
  {
    this.excludeProperties.clear();

    if( excludeProperties != null )
      for( String exclProp: excludeProperties )
        this.excludeProperty( exclProp );

    return this;
  }

  public GroupDataQueryBuilder excludeProperty( String excludeProperty )
  {
    if( excludeProperty != null && !excludeProperty.isEmpty() )
      this.excludeProperties.add( excludeProperty );

    return this;
  }

  public List<String> getSortBy()
  {
    return queryOptionsBuilder.getSortBy();
  }

  public GroupDataQueryBuilder setSortBy( List<String> sortBy )
  {
    return queryOptionsBuilder.setSortBy( sortBy );
  }

  public GroupDataQueryBuilder setSortBy( String... sortBy )
  {
    return queryOptionsBuilder.setSortBy( sortBy );
  }

  public GroupDataQueryBuilder addSortBy( String sortBy )
  {
    return queryOptionsBuilder.addSortBy( sortBy );
  }

  public List<String> getRelated()
  {
    return queryOptionsBuilder.getRelated();
  }

  public GroupDataQueryBuilder setRelated( List<String> related )
  {
    return queryOptionsBuilder.setRelated( related );
  }

  public GroupDataQueryBuilder setRelated( String... related )
  {
    return queryOptionsBuilder.setRelated( related );
  }

  public GroupDataQueryBuilder addRelated( List<String> related )
  {
    return queryOptionsBuilder.addRelated( related );
  }

  public GroupDataQueryBuilder addRelated( String related )
  {
    return queryOptionsBuilder.addRelated( related );
  }

  public Integer getRelationsDepth()
  {
    return queryOptionsBuilder.getRelationsDepth();
  }

  public GroupDataQueryBuilder setRelationsDepth( Integer relationsDepth )
  {
    return queryOptionsBuilder.setRelationsDepth( relationsDepth );
  }

  public List<String> getGroupBy()
  {
    return new ArrayList<>( this.groupBy );
  }

  public GroupDataQueryBuilder setGroupBy( String... groupBy )
  {
    this.groupBy.clear();
    this.addGroupBy( groupBy );
    return this;
  }

  public GroupDataQueryBuilder addGroupBy( String... groupBy )
  {
    for( String grb : groupBy )
    {
      if( grb != null && !grb.equals( "" ) )
        this.groupBy.add( grb );
    }

    return this;
  }

  public GroupDataQueryBuilder setRelationsPageSize( Integer relationsPageSize )
  {
    return queryOptionsBuilder.setRelationsPageSize( relationsPageSize );
  }

  public Integer getRelationPageSize()
  {
    return queryOptionsBuilder.getRelationsPageSize();
  }
}
