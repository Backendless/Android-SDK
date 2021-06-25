package com.backendless.persistence;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class QueryOptionsBuilder<Builder>
{
  @Getter
  private List<String> sortBy;
  @Getter
  private List<String> related;
  @Getter
  private Integer relationsDepth;
  @Getter
  private Integer relationsPageSize;
  @Getter
  private String fileReferencePrefix;
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
    queryOptions.setRelationsPageSize( relationsPageSize );
    queryOptions.setFileReferencePrefix( fileReferencePrefix );
    return queryOptions;
  }
  
  /*--- Auto-generated code ---*/

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

  public Builder setRelationsDepth( Integer relationsDepth )
  {
    this.relationsDepth = relationsDepth;
    return builder;
  }

  public Builder setRelationsPageSize( Integer relationPageSize )
  {
    this.relationsPageSize = relationPageSize;
    return builder;
  }

  public Builder setFileReferencePrefix( String fileReferencePrefix )
  {
    this.fileReferencePrefix = fileReferencePrefix;
    return builder;
  }
}
