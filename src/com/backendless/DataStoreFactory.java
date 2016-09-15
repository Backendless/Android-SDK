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
import com.backendless.persistence.BackendlessSerializer;
import com.backendless.persistence.LoadRelationsQueryBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class DataStoreFactory
{
  private static final List<String> emptyRelations = new ArrayList<String>();

  protected static <E> IDataStore<E> createDataStore( final Class<E> entityClass )
  {
    return new IDataStore<E>()
    {

      @Override
      public E save( final E entity ) throws BackendlessException
      {
        return Backendless.Persistence.save( entity );
      }

      @Override
      public void save( final E entity, final AsyncCallback<E> responder )
      {
        Backendless.Persistence.save( entity, responder );
      }

      @Override
      public Long remove( final E entity ) throws BackendlessException
      {
        return Backendless.Persistence.remove( entity );
      }

      @Override
      public void remove( final E entity, final AsyncCallback<Long> responder )
      {
        Backendless.Persistence.remove( entity, responder );
      }

      @Override
      public E findFirst() throws BackendlessException
      {
        return Backendless.Persistence.first( entityClass );
      }

      @Override
      public E findFirst( int relationsDepth ) throws BackendlessException
      {
        return findFirst( emptyRelations, relationsDepth );
      }

      @Override
      public E findFirst( List<String> relations ) throws BackendlessException
      {
        return findFirst( relations, 0 );
      }

      private E findFirst( List<String> relations, int relationsDepth ) throws BackendlessException
      {
        return Backendless.Persistence.first( entityClass, relations, relationsDepth );
      }

      @Override
      public int getObjectCount()
      {
        return Backendless.Persistence.getObjectCount( entityClass );
      }

      @Override
      public int getObjectCount( BackendlessDataQuery query )
      {
        return Backendless.Persistence.getObjectCount( entityClass, query );
      }

      public void findFirst( final AsyncCallback<E> responder )
      {
        Backendless.Persistence.first( entityClass, responder );
      }

      @Override
      public void findFirst( int relationsDepth, final AsyncCallback<E> responder )
      {
        findFirst( emptyRelations, relationsDepth, responder );
      }

      @Override
      public void findFirst( List<String> relations, AsyncCallback<E> responder )
      {
        findFirst( relations, 0, responder );
      }

      private void findFirst( List<String> relations, int relationsDepth, final AsyncCallback<E> responder )
      {
        Backendless.Persistence.first( entityClass, relations, relationsDepth, responder );
      }

      @Override
      public E findLast() throws BackendlessException
      {
        return Backendless.Persistence.last( entityClass );
      }

      @Override
      public E findLast( int relationsDepth ) throws BackendlessException
      {
        return findLast( emptyRelations, relationsDepth );
      }

      @Override
      public E findLast( List<String> relations ) throws BackendlessException
      {
        return findLast( relations, 0 );
      }

      private E findLast( List<String> relations, int relationsDepth ) throws BackendlessException
      {
        return Backendless.Persistence.last( entityClass, relations, relationsDepth );
      }

      @Override
      public void findLast( final AsyncCallback<E> responder )
      {
        Backendless.Persistence.last( entityClass, responder );
      }

      @Override
      public void findLast( int relationsDepth, final AsyncCallback<E> responder )
      {
        findLast( emptyRelations, relationsDepth, responder );
      }

      @Override
      public void findLast( List<String> relations, AsyncCallback<E> responder )
      {
        findLast( relations, 0, responder );
      }

      private void findLast( List<String> relations, int relationsDepth, final AsyncCallback<E> responder )
      {
        Backendless.Persistence.last( entityClass, relations, relationsDepth, responder );
      }

      @Override
      public Collection<E> find() throws BackendlessException
      {
        return Backendless.Persistence.find( entityClass, new BackendlessDataQuery() );
      }

      @Override
      public Collection<E> find( BackendlessDataQuery dataQueryOptions ) throws BackendlessException
      {
        return Backendless.Persistence.find( entityClass, dataQueryOptions );
      }

      @Override
      public void find( AsyncCallback<Collection<E>> responder )
      {
        Backendless.Persistence.find( entityClass, new BackendlessDataQuery(), responder );
      }

      @Override
      public void find( BackendlessDataQuery dataQueryOptions, AsyncCallback<Collection<E>> responder )
      {
        Backendless.Persistence.find( entityClass, dataQueryOptions, responder );
      }

      @Override
      public E findById( String objectId ) throws BackendlessException
      {
        return findById( objectId, emptyRelations );
      }

      @Override
      public E findById( String objectId, List<String> relations ) throws BackendlessException
      {
        return Backendless.Persistence.findById( entityClass, objectId, relations );
      }

      @Override
      public E findById( String objectId, int relationsDepth ) throws BackendlessException
      {
        return Backendless.Persistence.findById( entityClass, objectId, emptyRelations, relationsDepth );
      }

      @Override
      public E findById( String objectId, List<String> relations, int relationsDepth ) throws BackendlessException
      {
        return Backendless.Persistence.findById( entityClass, objectId, relations, relationsDepth );
      }

      @Override
      public E findById( E entity )
      {
        return findById( entity, emptyRelations );
      }

      @Override
      public E findById( E entity, List<String> relations )
      {
        return findById( entity, relations, 0 );
      }

      @Override
      public E findById( E entity, int relationsDepth )
      {
        return findById( entity, emptyRelations, relationsDepth );
      }

      @Override
      public E findById( E entity, List<String> relations, int relationsDepth )
      {
        return Backendless.Data.findById( entity, relations, relationsDepth );
      }

      @Override
      public void findById( String objectId, AsyncCallback<E> responder )
      {
        findById( objectId, emptyRelations, responder );
      }

      @Override
      public void findById( String objectId, List<String> relations, AsyncCallback<E> responder )
      {
        Backendless.Persistence.findById( entityClass, objectId, relations, responder );
      }

      @Override
      public void findById( String objectId, int relationsDepth, AsyncCallback<E> responder )
      {
        findById( objectId, emptyRelations, relationsDepth, responder );
      }

      @Override
      public void findById( String objectId, List<String> relations, int relationsDepth, AsyncCallback<E> responder )
      {
        Backendless.Persistence.findById( entityClass, objectId, relations, relationsDepth, responder );
      }

      @Override
      public void findById( E entity, AsyncCallback<E> responder )
      {
        findById( entity, emptyRelations, responder );
      }

      @Override
      public void findById( E entity, List<String> relations, AsyncCallback<E> responder )
      {
        findById( entity, relations, 0, responder );
      }

      @Override
      public void findById( E entity, int relationsDepth, AsyncCallback<E> responder )
      {
        findById( entity, emptyRelations, relationsDepth, responder );
      }

      @Override
      public void findById( E entity, List<String> relations, int relationsDepth, AsyncCallback<E> responder )
      {
        Backendless.Data.findById( entity, relations, relationsDepth, responder );
      }

      @Override
      public <R> List<R> loadRelations( String objectId, LoadRelationsQueryBuilder<R> queryBuilder )
      {
        String typeName = BackendlessSerializer.getSimpleName( entityClass );
        return Backendless.Persistence.loadRelations( typeName, objectId, queryBuilder, queryBuilder.getRelationType() );
      }

      @Override
      public <R> void loadRelations( String objectId, LoadRelationsQueryBuilder<R> queryBuilder, AsyncCallback<List<R>> responder )
      {
        String typeName = BackendlessSerializer.getSimpleName( entityClass );
        Backendless.Persistence.loadRelations( typeName, objectId, queryBuilder, queryBuilder.getRelationType(), responder );
      }

      @Override
      public void getObjectCount( AsyncCallback<Integer> responder )
      {
        Backendless.Persistence.getObjectCount( entityClass, responder );
      }

      @Override
      public void getObjectCount( BackendlessDataQuery query, AsyncCallback<Integer> responder )
      {
        Backendless.Persistence.getObjectCount( entityClass, query, responder );
      }
    };
  }
}