package com.backendless.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class QueryOptionsBuilder<Builder>
{
  private List<String> sortBy;
  private List<String> related;
  private Integer relationsDepth;
  private Integer relationPageSize;
  private Builder builder;

  QueryOptionsBuilder( Builder builder )
  {
    sortBy = new ArrayList<>();
    related = new ArrayList<>();
    this.builder = builder;
  }
  
  QueryOptions build()
  {
    QueryOptions queryOptions = new QueryOptions();
    queryOptions.setRelated( related );
    queryOptions.setRelationsDepth( relationsDepth );
    queryOptions.setSortBy( sortBy );
    queryOptions.setRelationPageSize( relationPageSize );
    return queryOptions;
  }
  
  /*--- Auto-generated code ---*/

  public List<String> getSortBy()
  {
    return sortBy;
  }

  public Builder setSortBy( List<String> sortBy )
  {
    this.sortBy = sortBy;
    return builder;
  }

  public Builder setSortBy( String... sortBy )
  {
    this.sortBy = new ArrayList<>( Arrays.asList( sortBy ) );
    return builder;
  }

  public Builder addSortBy( String sortBy )
  {
    this.sortBy.add( sortBy );
    return builder;
  }

  public List<String> getRelated()
  {
    return related;
  }

  public Builder setRelated( List<String> related )
  {
    this.related = related;
    return builder;
  }

  public Builder setRelated( String... related )
  {
    this.related = new ArrayList<>( Arrays.asList( related ) );
    return builder;
  }

  public Builder addRelated( List<String> related )
  {
    this.related.addAll( related );
    return builder;
  }

  public Builder addRelated( String related )
  {
    this.related.add( related );
    return builder;
  }

  public Integer getRelationsDepth()
  {
    return relationsDepth;
  }

  public Builder setRelationsDepth( Integer relationsDepth )
  {
    this.relationsDepth = relationsDepth;
    return builder;
  }

  public Integer getRelationPageSize()
  {
    return relationPageSize;
  }

  public Builder setRelationPageSize( Integer relationPageSize )
  {
    this.relationPageSize = relationPageSize;
    return builder;
  }
}
