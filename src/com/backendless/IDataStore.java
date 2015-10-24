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
  E save( E entity ) throws BackendlessException;

  void save( E entity, AsyncCallback<E> responder );

  Long remove( E entity ) throws BackendlessException;

  void remove( E entity, AsyncCallback<Long> responder );

  E findFirst() throws BackendlessException;

  E findFirst( int relationsDepth ) throws BackendlessException;

  E findFirst( List<String> relations ) throws BackendlessException;

  void findFirst( AsyncCallback<E> responder );

  void findFirst( int relationsDepth, AsyncCallback<E> responder );

  void findFirst( List<String> relations, AsyncCallback<E> responder );

  E findLast() throws BackendlessException;

  E findLast( int relationsDepth ) throws BackendlessException;

  E findLast( List<String> relations ) throws BackendlessException;

  void findLast( AsyncCallback<E> responder );

  void findLast( int relationsDepth, AsyncCallback<E> responder );

  void findLast( List<String> relations, AsyncCallback<E> responder );

  BackendlessCollection<E> find() throws BackendlessException;

  BackendlessCollection<E> find( BackendlessDataQuery dataQueryOptions ) throws BackendlessException;

  void find( AsyncCallback<BackendlessCollection<E>> responder );

  void find( BackendlessDataQuery dataQueryOptions, AsyncCallback<BackendlessCollection<E>> responder );

  E findById( String id ) throws BackendlessException;

  E findById( String id, List<String> relations ) throws BackendlessException;

  E findById( String id, int relationsDepth ) throws BackendlessException;

  E findById( String id, List<String> relations, int relationsDepth ) throws BackendlessException;

  E findById( E entity ) throws BackendlessException;

  E findById( E entity, List<String> relations ) throws BackendlessException;

  E findById( E entity, int relationsDepth ) throws BackendlessException;

  E findById( E entity, List<String> relations, int relationsDepth ) throws BackendlessException;

  void findById( String id, AsyncCallback<E> responder );

  void findById( String id, List<String> relations, AsyncCallback<E> responder );

  void findById( String id, int relationsDepth, AsyncCallback<E> responder );

  void findById( String id, List<String> relations, int relationsDepth, AsyncCallback<E> responder );

  void findById( E entity, AsyncCallback<E> responder );

  void findById( E entity, List<String> relations, AsyncCallback<E> responder );

  void findById( E entity, int relationsDepth, AsyncCallback<E> responder );

  void findById( E entity, List<String> relations, int relationsDepth, AsyncCallback<E> responder );

  void loadRelations( E entity, List<String> relations ) throws BackendlessException;

  void loadRelations( E entity, List<String> relations, AsyncCallback<E> responder );
}
