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
  private int pageSize = 10;
  private int offset;
  private List<String> sortBy = new ArrayList<String>();
  private List<String> related  = new ArrayList<String>();
  private int relationsDepth;

  public QueryOptions()
  {
  }

  public QueryOptions( int pageSize, int offset )
  {
    this.pageSize = pageSize;
    this.offset = offset;
  }

  public QueryOptions( int pageSize, int offset, String sortBy )
  {
    this.pageSize = pageSize;
    this.offset = offset;
    addSortByOption( sortBy );
  }

  public QueryOptions( String sortBy )
  {
    addSortByOption( sortBy );
  }

  public void setPageSize( Integer pageSize )
  {
    this.pageSize = pageSize;
  }

  public void setOffset( Integer offset )
  {
    this.offset = offset;
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

  public int getPageSize()
  {
    return pageSize;
  }

  public int getOffset()
  {
    return offset;
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

  public QueryOptions newInstance()
  {
    QueryOptions result = new QueryOptions();
    result.setPageSize( pageSize );
    result.setOffset( offset );
    result.setSortBy( sortBy );
    result.setRelated( related );
    result.setRelationsDepth( relationsDepth );

    return result;
  }

  public void setRelationsDepth ( int relationsDepth )
  {
    this.relationsDepth = relationsDepth;
  }

  public int getRelationsDepth()
  {
    return relationsDepth;
  }
}
