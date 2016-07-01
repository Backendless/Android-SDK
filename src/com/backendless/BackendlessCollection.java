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

import com.backendless.async.callback.AsyncCallback;
import com.backendless.commons.AbstractBackendlessCollection;
import com.backendless.exceptions.BackendlessException;
import com.backendless.geo.BackendlessGeoQuery;
import com.backendless.persistence.BackendlessDataQuery;
import weborb.service.ExcludeProperties;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ExcludeProperties( propertyNames = {"type"} )
public class BackendlessCollection<E> extends AbstractBackendlessCollection<E>
{
  private final Object dataLock = new Object();
  private Class<E> type;
  private volatile IBackendlessQuery query;
  private String tableName;

  public BackendlessCollection()
  {
    data = new CopyOnWriteArrayList<E>();
  }

  public Class<E> getType()
  {
    return type;
  }

  public void setType( Class<E> type )
  {
    this.type = type;
  }

  public void setTableName( String tableName )
  {
    this.tableName = tableName;
  }

  public String getTableName() { return this.tableName; }

  public void setPageSize( int pageSize )
  {
    query.setPageSize( pageSize );
  }

  public List<E> getCurrentPage()
  {
    return data;
  }

  //Sync methods
  public BackendlessCollection<E> nextPage() throws BackendlessException
  {
    int offset = query.getOffset();
    int pageSize = query.getPageSize();

    return getPage( pageSize, offset + pageSize );
  }

  public BackendlessCollection<E> getPage( int pageSize, int offset ) throws BackendlessException
  {
    return downloadPage( pageSize, offset );
  }

  //Download page logic
  private BackendlessCollection<E> downloadPage( int pageSize, int offset )  throws BackendlessException
  {
    checkQuery();
    return (BackendlessCollection<E>) query.getPage( this, pageSize, offset );
  }

  public BackendlessCollection<E> previousPage() throws BackendlessException
  {
    checkQuery();
    int offset = query.getOffset();
    int pageSize = query.getPageSize();

    if( (offset - pageSize) >= 0 )
    {
      return getPage( pageSize, offset - pageSize );
    }
    else
    {
      return this.newInstance();
    }
  }

  protected BackendlessCollection<E> newInstance()
  {
    BackendlessCollection<E> result = new BackendlessCollection<E>();
    result.setData( data );
    result.setQuery( query );
    result.setType( type );
    result.setTableName( tableName );
    result.setTotalObjects( totalObjects );

    return result;
  }

  public void setData( List<E> data )
  {
    synchronized( dataLock )
    {
      this.data = new CopyOnWriteArrayList<E>( data );
    }
  }

  public void setQuery( IBackendlessQuery query )
  {
    if( query == null )
    {
      this.query = null;
    }
    else
    {
      this.query = query.newInstance();
    }
  }

  public IBackendlessQuery getQuery()
  {
    return query;
  }

  //Async methods
  public void nextPage( AsyncCallback<BackendlessCollection<E>> responder )
  {
    new CollectionDownloadTask( responder )
    {
      @Override
      protected BackendlessCollection<E> doInBackground( int pageSize, int offset ) throws Exception
      {
        return nextPage();
      }
    }.executeThis();
  }

  public void previousPage( AsyncCallback<BackendlessCollection<E>> responder )
  {
    new CollectionDownloadTask( responder )
    {
      @Override
      protected BackendlessCollection<E> doInBackground( int pageSize, int offset ) throws Exception
      {
        return previousPage();
      }
    }.executeThis();
  }

  public void getPage( int pageSize, int offset, AsyncCallback<BackendlessCollection<E>> responder )
  {
    new CollectionDownloadTask( responder )
    {
      @Override
      protected BackendlessCollection<E> doInBackground( int pageSize, int offset ) throws Exception
      {
        return getPage( pageSize, offset );
      }
    }.executeThis( pageSize, offset );
  }

  private void checkQuery()
  {
    if( query != null )
      return;

    BackendlessDataQuery backendlessDataQuery = new BackendlessDataQuery();
    backendlessDataQuery.setOffset( 0 );
    backendlessDataQuery.setPageSize( data.size() );
    query = backendlessDataQuery;
  }
}