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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class QueryOptions
{
  @Setter
  private List<String> sortBy = new ArrayList<>();
  @Setter
  private List<String> related  = new ArrayList<>();
  @Getter @Setter
  private Integer relationsDepth;
  @Getter @Setter
  private Integer relationsPageSize;
  @Getter @Setter
  private String fileReferencePrefix;

  public QueryOptions( String sortBy )
  {
    addSortByOption( sortBy );
  }

  public void addSortByOption( String sortBy )
  {
    if( sortBy == null || sortBy.equals( "" ) )
      return;

    if( this.sortBy == null )
      this.sortBy = new ArrayList<>();

    this.sortBy.add( sortBy );
  }

  public void addRelated( String related )
  {
    if( related == null || related.equals( "" ) )
      return;

    if( this.related == null )
      this.related = new ArrayList<>();

    this.related.add( related );
  }

  public List<String> getSortBy()
  {
    if( sortBy == null )
      return sortBy = new ArrayList<>();

    return new ArrayList<>( sortBy );
  }

  public List<String> getRelated()
  {
    if( related == null )
      return related = new ArrayList<>();

    return new ArrayList<>( related );
  }

  public QueryOptions newInstance()
  {
    QueryOptions result = new QueryOptions();
    result.setSortBy( sortBy );
    result.setRelated( related );
    result.setRelationsDepth( relationsDepth );
    result.setRelationsPageSize( relationsPageSize );
    result.setFileReferencePrefix( fileReferencePrefix );

    return result;
  }
}
