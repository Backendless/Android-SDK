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
import com.backendless.persistence.DataQueryBuilder;
import com.backendless.persistence.LoadRelationsQueryBuilder;
import com.backendless.rt.data.EventHandler;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IDataStore<E>
{
  List<String> create( List<E> objects ) throws BackendlessException;

  void create( List<E> objects, AsyncCallback<List<String>> responder ) throws BackendlessException;

  E save( E entity ) throws BackendlessException;

  void save( E entity, AsyncCallback<E> responder );

  Long remove( E entity ) throws BackendlessException;

  void remove( E entity, AsyncCallback<Long> responder );

  int remove( String whereClause ) throws BackendlessException;

  void remove( String whereClause, AsyncCallback<Integer> responder ) throws BackendlessException;

  int update( String whereClause, Map<String, Object> changes ) throws BackendlessException;

  void update( String whereClause, Map<String, Object> changes, AsyncCallback<Integer> responder ) throws BackendlessException;

  E findFirst() throws BackendlessException;

  E findFirst( Integer relationsDepth ) throws BackendlessException;

  E findFirst( List<String> relations ) throws BackendlessException;

  void findFirst( AsyncCallback<E> responder );

  void findFirst( Integer relationsDepth, AsyncCallback<E> responder );

  void findFirst( List<String> relations, AsyncCallback<E> responder );

  E findLast() throws BackendlessException;

  E findLast( Integer relationsDepth ) throws BackendlessException;

  E findLast( List<String> relations ) throws BackendlessException;

  void findLast( AsyncCallback<E> responder );

  void findLast( Integer relationsDepth, AsyncCallback<E> responder );

  void findLast( List<String> relations, AsyncCallback<E> responder );

  List<E> find() throws BackendlessException;

  List<E> find( DataQueryBuilder dataQueryBuilder ) throws BackendlessException;

  void find( AsyncCallback<List<E>> responder );

  void find( DataQueryBuilder dataQueryBuilder, AsyncCallback<List<E>> responder );

  E findById( String id ) throws BackendlessException;

  E findById( String id, List<String> relations ) throws BackendlessException;

  E findById( String id, Integer relationsDepth ) throws BackendlessException;

  E findById( String id, List<String> relations, Integer relationsDepth ) throws BackendlessException;

  E findById( String id, DataQueryBuilder queryBuilder ) throws BackendlessException;

  E findById( E entity ) throws BackendlessException;

  E findById( E entity, List<String> relations ) throws BackendlessException;

  E findById( E entity, Integer relationsDepth ) throws BackendlessException;

  E findById( E entity, List<String> relations, Integer relationsDepth ) throws BackendlessException;

  E findById( E entity, DataQueryBuilder queryBuilder ) throws BackendlessException;

  int getObjectCount();

  int getObjectCount( DataQueryBuilder dataQueryBuilder );

  void findById( String id, AsyncCallback<E> responder );

  void findById( String id, List<String> relations, AsyncCallback<E> responder );

  void findById( String id, Integer relationsDepth, AsyncCallback<E> responder );

  void findById( String id, List<String> relations, Integer relationsDepth, AsyncCallback<E> responder );

  void findById( String id, DataQueryBuilder queryBuilder, AsyncCallback<E> responder );

  void findById( E entity, AsyncCallback<E> responder );

  void findById( E entity, List<String> relations, AsyncCallback<E> responder );

  void findById( E entity, Integer relationsDepth, AsyncCallback<E> responder );

  void findById( E entity, List<String> relations, Integer relationsDepth, AsyncCallback<E> responder );

  void findById( E entity, DataQueryBuilder queryBuilder, AsyncCallback<E> responder );

  /**
   * @see com.backendless.persistence.LoadRelationsQueryBuilder
   *
   * @param objectId parentObjectId
   * @param <R>       child relation type
   */
  <R> List<R> loadRelations( String objectId, LoadRelationsQueryBuilder<R> queryBuilder );

   /**
   * @see com.backendless.persistence.LoadRelationsQueryBuilder
   *
   * @param objectId  parentObjectId
   * @param <R>       child relation type
   * @param responder asynchronous callback
   */
  <R> void loadRelations( String objectId, LoadRelationsQueryBuilder<R> queryBuilder, AsyncCallback<List<R>> responder );

  void getObjectCount( AsyncCallback<Integer> responder );

  void getObjectCount( DataQueryBuilder dataQueryBuilder, AsyncCallback<Integer> responder );

  <R> int addRelation( E parent, String relationColumnName, Collection<R> children );

  <R> void addRelation( E parent, String relationColumnName, Collection<R> children, AsyncCallback<Integer> callback );

  int addRelation( E parent, String relationColumnName, String whereClause );

  void addRelation( E parent, String relationColumnName, String whereClause, AsyncCallback<Integer> callback );

  <R> int setRelation( E parent, String relationColumnName, Collection<R> children );

  <R> void setRelation( E parent, String relationColumnName, Collection<R> children, AsyncCallback<Integer> callback );

  int setRelation( E parent, String relationColumnName, String whereClause );

  void setRelation( E parent, String relationColumnName, String whereClause, AsyncCallback<Integer> callback );

  <R> int deleteRelation( E parent, String relationColumnName, Collection<R> children );

  <R> void deleteRelation( E parent, String relationColumnName, Collection<R> children, AsyncCallback<Integer> callback );

  int deleteRelation( E parent, String relationColumnName, String whereClause );

  void deleteRelation( E parent, String relationColumnName, String whereClause, AsyncCallback<Integer> callback );

  EventHandler<E> rt();
}
