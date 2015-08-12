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

package com.backendless;

/**
 * Created by baas on 12.08.15.
 */
public class BackendlessSimpleQuery implements IBackendlessQuery
{
  public static final int DEFAULT_PAGE_SIZE = 10;
  public static final int DEFAULT_OFFSET = 0;
  public static final IBackendlessQuery DEFAULT = new BackendlessSimpleQuery( DEFAULT_PAGE_SIZE, DEFAULT_OFFSET );

  private int pagesize;
  private int offset;

  public BackendlessSimpleQuery()
  {
  }

  public BackendlessSimpleQuery( int pagesize, int offset )
  {
    this.pagesize = pagesize;
    this.offset = offset;
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
  public IBackendlessQuery newInstance()
  {
    return new BackendlessSimpleQuery(pagesize, offset);
  }
}
