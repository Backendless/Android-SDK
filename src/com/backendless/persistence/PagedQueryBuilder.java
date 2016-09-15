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
    validatePageSize( pageSize );
    this.pageSize = pageSize;
    return builder;
  }

  Builder setOffset( int offset )
  {
    validateOffset( offset );
    this.offset = offset;
    return builder;
  }

  /**
   * Updates offset to point at next data page by adding pageSize.
   */
  Builder prepareNextPage()
  {
    int offset = this.offset + pageSize;
    validateOffset( offset );
    this.offset = offset;

    return builder;
  }

  /**
   * Updates offset to point at previous data page by subtracting pageSize.
   */
  Builder preparePreviousPage()
  {
    int offset = this.offset - pageSize;
    validateOffset( offset );
    this.offset = offset;

    return builder;
  }

  BackendlessDataQuery build()
  {
    validateOffset( offset );
    validatePageSize( pageSize );

    BackendlessDataQuery dataQuery = new BackendlessDataQuery();
    dataQuery.setPageSize( pageSize );
    dataQuery.setOffset( offset );

    return dataQuery;
  }


  private void validateOffset( int offset )
  {
    if( offset < 0 )
      throw new IllegalArgumentException( ExceptionMessage.WRONG_OFFSET );
  }

  private void validatePageSize( int pageSize )
  {
    if( pageSize <= 0 )
      throw new IllegalArgumentException( ExceptionMessage.WRONG_PAGE_SIZE );
  }

}
