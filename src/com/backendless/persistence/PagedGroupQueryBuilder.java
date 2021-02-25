package com.backendless.persistence;

import com.backendless.exceptions.ExceptionMessage;

class PagedGroupQueryBuilder<Builder>
{
  private int pageSize;
  private int groupPageSize;
  private int recordsPageSize;
  private int offset;

  private Builder builder;

  PagedGroupQueryBuilder( Builder builder )
  {
    pageSize = BackendlessDataQuery.DEFAULT_PAGE_SIZE;
    groupPageSize = BackendlessDataQuery.DEFAULT_PAGE_SIZE;
    recordsPageSize = BackendlessDataQuery.DEFAULT_PAGE_SIZE;
    offset = BackendlessDataQuery.DEFAULT_OFFSET;
    this.builder = builder;
  }

  Builder setPageSize( int pageSize )
  {
    validatePageSize( pageSize );
    this.pageSize = pageSize;
    return builder;
  }

  Builder setGroupPageSize( int groupPageSize )
  {
    validatePageSize( groupPageSize );
    this.groupPageSize = groupPageSize;
    return builder;
  }

  Builder setRecordsPageSize( int recordsPageSize )
  {
    validatePageSize( recordsPageSize );
    this.recordsPageSize = recordsPageSize;
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

  BackendlessGroupDataQuery build()
  {
    validateOffset( offset );
    validatePageSize( pageSize );
    validatePageSize( groupPageSize );
    validatePageSize( recordsPageSize );

    BackendlessGroupDataQuery dataQuery = new BackendlessGroupDataQuery();
    dataQuery.setPageSize( pageSize );
    dataQuery.setGroupPageSize( groupPageSize );
    dataQuery.setRecordsPageSize( recordsPageSize );
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
