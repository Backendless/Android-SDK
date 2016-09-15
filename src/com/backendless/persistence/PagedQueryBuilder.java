package com.backendless.persistence;

import com.backendless.exceptions.ExceptionMessage;

class PagedQueryBuilder<Builder>
{
  private int pageSize;
  private int offset;

  private Builder builder;

  PagedQueryBuilder( Builder builder )
  {
    pageSize = BackendlessDataQuery.DEFAULT_PAGE_SIZE;
    offset = BackendlessDataQuery.DEFAULT_OFFSET;
    this.builder = builder;
  }

  Builder setPageSize( int pageSize )
  {
    this.pageSize = pageSize;
    return builder;
  }

  Builder setOffset( int offset )
  {
    this.offset = offset;
    return builder;
  }

  /**
   * Updates offset to point at next data page by adding pageSize.
   */
  Builder prepareNextPage()
  {
    offset = +pageSize;
    return builder;
  }

  /**
   * Updates offset to point at previous data page by subtracting pageSize.
   */
  Builder preparePreviousPage()
  {
    offset = -pageSize;
    if( offset < 0 )
      offset = 0;
    return builder;
  }

  BackendlessDataQuery build()
  {
    if( pageSize < 0 )
      throw new IllegalArgumentException( ExceptionMessage.WRONG_OFFSET );
    if( offset < 0 )
      throw new IllegalArgumentException( ExceptionMessage.WRONG_PAGE_SIZE );

    BackendlessDataQuery dataQuery = new BackendlessDataQuery();
    dataQuery.setPageSize( pageSize );
    dataQuery.setOffset( offset );

    return dataQuery;
  }

}
