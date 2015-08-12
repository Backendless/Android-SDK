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

import com.backendless.persistence.BackendlessDataQuery;

public interface IBackendlessQuery
{
  int getOffset();

  int getPageSize();

  void setPageSize( int pageSize );

  void setOffset( int offset );

  IBackendlessQuery newInstance();

  BackendlessDataQuery pageSize( int pageSize );

  BackendlessDataQuery offset( int offset );

  BackendlessDataQuery where( String whereClause );

  BackendlessDataQuery having( String havingClause );

  BackendlessDataQuery addAggregationFn( IAggregationFunction function );

  BackendlessDataQuery addGroupBy( String property );

}
