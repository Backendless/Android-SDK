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

public class QueryOptions
{
  private List<String> sortBy = new ArrayList<String>();
  private List<String> related  = new ArrayList<String>();
  private Integer relationsDepth;
  private Integer relationPageSize;

  public QueryOptions()
  {
  }

  public QueryOptions( String sortBy )
  {
    addSortByOption( sortBy );
  }

  public void setSortBy( List<String> sortBy )
  {
    this.sortBy = sortBy;
  }

  public void setRelated( List<String> related )
  {
    this.related = related;
  }

  public void addSortByOption( String sortBy )
  {
    if( sortBy == null || sortBy.equals( "" ) )
      return;

    if( this.sortBy == null )
      this.sortBy = new ArrayList<String>();

    this.sortBy.add( sortBy );
  }

  public void addRelated( String related )
  {
    if( related == null || related.equals( "" ) )
      return;

    if( this.related == null )
      this.related = new ArrayList<String>();

    this.related.add( related );
  }

  public List<String> getSortBy()
  {
    if( sortBy == null )
      return sortBy = new ArrayList<String>();

    return new ArrayList<String>( sortBy );
  }

  public List<String> getRelated()
  {
    if( related == null )
      return related = new ArrayList<String>();

    return new ArrayList<String>( related );
  }

  public Integer getRelationPageSize()
  {
    return relationPageSize;
  }

  public void setRelationPageSize( Integer relationPageSize )
  {
    this.relationPageSize = relationPageSize;
  }

  public QueryOptions newInstance()
  {
    QueryOptions result = new QueryOptions();
    result.setSortBy( sortBy );
    result.setRelated( related );
    result.setRelationsDepth( relationsDepth );
    result.setRelationPageSize( relationPageSize );

    return result;
  }

  public void setRelationsDepth ( Integer relationsDepth )
  {
    this.relationsDepth = relationsDepth;
  }

  public Integer getRelationsDepth()
  {
    return relationsDepth;
  }
}
