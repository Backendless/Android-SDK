package com.backendless.files;

import com.backendless.persistence.AbstractBackendlessQuery;

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
public class BackendlessFilesQuery extends AbstractBackendlessQuery
{
  public static final int DEFAULT_PAGE_SIZE = 20;
  public static final int DEFAULT_OFFSET = 0;

  private String path;
  private String pattern;
  private boolean recursive;

  {
    setPageSize( DEFAULT_PAGE_SIZE );
    setOffset( DEFAULT_OFFSET );
  }

  public BackendlessFilesQuery( String path, String pattern, boolean recursive )
  {
    this.path = path;
    this.recursive = recursive;
    this.pattern = pattern;
  }

  @Override
  public BackendlessFilesQuery newInstance()
  {
    return this;
  }
}
