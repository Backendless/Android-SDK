package com.backendless.files;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.IBackendlessQuery;
import com.backendless.persistence.BackendlessDataQuery;

/**********************************************************************************************************************
 * BACKENDLESS.COM CONFIDENTIAL
 * <p>
 * *********************************************************************************************************************
 * <p>
 * Copyright 2012 BACKENDLESS.COM. All Rights Reserved.
 * <p>
 * NOTICE:  All information contained herein is, and remains the property of Backendless.com and its suppliers,
 * if any.  The intellectual and technical concepts contained herein are proprietary to Backendless.com and its
 * suppliers and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret
 * or copyright law. Dissemination of this information or reproduction of this material is strictly forbidden
 * unless prior written permission is obtained from Backendless.com.
 * <p>
 * CREATED ON: 6/28/16
 * AT: 11:36 AM
 **********************************************************************************************************************/
public class BackendlessFilesQuery implements IBackendlessQuery
{
  public static final int DEFAULT_PAGE_SIZE = 10;
  public static final int DEFAULT_OFFSET = 0;

  private String path;
  private String pattern;
  private boolean recursive;
  private int pagesize;
  private int offset;

  public BackendlessFilesQuery( String path, String pattern, boolean recursive, int pagesize, int offset )
  {
    this.path = path;
    this.pagesize = pagesize;
    this.offset = offset;
    this.recursive = recursive;
    this.pattern = pattern;
  }

  @Override
  public int getOffset()
  {
    return offset;
  }

  @Override
  public int getPageSize()
  {
    return pagesize;
  }

  @Override
  public void setPageSize( int pageSize )
  {
    this.pagesize = pageSize;
  }

  @Override
  public void setOffset( int offset )
  {
    this.offset = offset;
  }

  @Override
  public BackendlessFilesQuery newInstance()
  {
    return this;
  }

  @Override
  public BackendlessCollection getPage( BackendlessCollection sourceCollection, int pageSize, int offset )
  {
    return Backendless.Files.listing( path, pattern, recursive, pageSize, offset );
  }
}
