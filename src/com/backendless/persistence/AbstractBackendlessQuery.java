package com.backendless.persistence;

import com.backendless.IBackendlessQuery;

/**
 * Created by oleg on 9/14/16.
 */
abstract public class AbstractBackendlessQuery implements IBackendlessQuery
{
  private int pageSize;
  private int offset;

  @Override
  public int getPageSize()
  {
    return pageSize;
  }

  @Override
  public void setPageSize( int pageSize )
  {
    this.pageSize = pageSize;
  }

  @Override
  public int getOffset()
  {
    return offset;
  }

  @Override
  public void setOffset( int offset )
  {
    this.offset = offset;
  }

  @Override
  public void prepareForNextPage()
  {
    offset+=pageSize;
  }

  @Override
  public void prepareForPreviousPage()
  {
    offset-=pageSize;
  }

  abstract public IBackendlessQuery newInstance();
}
