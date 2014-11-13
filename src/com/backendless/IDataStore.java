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
import com.backendless.exceptions.BackendlessException;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.List;

public interface IDataStore<E>
{
  public E save( E entity ) throws BackendlessException;

  public void save( E entity, AsyncCallback<E> responder );

  public Long remove( E entity ) throws BackendlessException;

  public void remove( E entity, AsyncCallback<Long> responder );

  public E findFirst() throws BackendlessException;

  public E findFirst( int relationsDepth ) throws BackendlessException;

  public E findFirst( List<String> relations ) throws BackendlessException;

  public void findFirst( AsyncCallback<E> responder );

  public void findFirst( int relationsDepth, AsyncCallback<E> responder );

  public void findFirst( List<String> relations, AsyncCallback<E> responder );

  public E findLast() throws BackendlessException;

  public E findLast( int relationsDepth ) throws BackendlessException;

  public E findLast( List<String> relations ) throws BackendlessException;

  public void findLast( AsyncCallback<E> responder );

  public void findLast( int relationsDepth, AsyncCallback<E> responder );

  public void findLast( List<String> relations, AsyncCallback<E> responder );

  public BackendlessCollection<E> find() throws BackendlessException;

  public BackendlessCollection<E> find( BackendlessDataQuery dataQueryOptions ) throws BackendlessException;

  public void find( AsyncCallback<BackendlessCollection<E>> responder );

  public void find( BackendlessDataQuery dataQueryOptions, AsyncCallback<BackendlessCollection<E>> responder );

  public E findById( String id ) throws BackendlessException;

  public E findById( String id, List<String> relations ) throws BackendlessException;

  public E findById( String id, int relationsDepth ) throws BackendlessException;

  public E findById( String id, List<String> relations, int relationsDepth ) throws BackendlessException;

  public void findById( String id, AsyncCallback<E> responder );

  public void findById( String id, List<String> relations, AsyncCallback<E> responder );

  public void findById( String id, int relationsDepth, AsyncCallback<E> responder );

  public void findById( String id, List<String> relations, int relationsDepth, AsyncCallback<E> responder );

  public void loadRelations( E entity, List<String> relations ) throws Exception;

  public void loadRelations( E entity, List<String> relations, AsyncCallback<E> responder );
}
