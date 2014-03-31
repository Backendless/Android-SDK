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

import java.util.ArrayList;
import java.util.List;

class DataStoreFactory
{
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
        List<String> relations = new ArrayList<String>();
        relations.add( "*" );
        return findFirst( relations, relationsDepth );
      }

      @Override
      public E findFirst( List<String> relations, int relationsDepth ) throws BackendlessException
      {
        return Backendless.Persistence.first( entityClass, relations, relationsDepth );
      }

      @Override
      public void findFirst( final AsyncCallback<E> responder )
      {
        Backendless.Persistence.first( entityClass, responder );
      }

      @Override
      public void findFirst( int relationsDepth, final AsyncCallback<E> responder )
      {
        List<String> relations = new ArrayList<String>();
        relations.add( "*" );
        findFirst( relations, relationsDepth, responder );
      }

      @Override
      public void findFirst( List<String> relations, int relationsDepth, final AsyncCallback<E> responder )
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
        List<String> relations = new ArrayList<String>();
        relations.add( "*" );
        return findLast( relations, relationsDepth );
      }

      @Override
      public E findLast( List<String> relations, int relationsDepth ) throws BackendlessException
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
        List<String> relations = new ArrayList<String>();
        relations.add( "*" );
        findLast( relations, relationsDepth, responder );
      }

      @Override
      public void findLast( List<String> relations, int relationsDepth, final AsyncCallback<E> responder )
      {
        Backendless.Persistence.last( entityClass, relations, relationsDepth, responder );
      }

      @Override
      public BackendlessCollection<E> find() throws BackendlessException
      {
        return Backendless.Persistence.find( entityClass, null );
      }

      @Override
      public BackendlessCollection<E> find( BackendlessDataQuery dataQueryOptions ) throws BackendlessException
      {
        return Backendless.Persistence.find( entityClass, dataQueryOptions );
      }

      @Override
      public void find( AsyncCallback<BackendlessCollection<E>> responder )
      {
        Backendless.Persistence.find( entityClass, null, responder );
      }

      @Override
      public void find( BackendlessDataQuery dataQueryOptions, AsyncCallback<BackendlessCollection<E>> responder )
      {
        Backendless.Persistence.find( entityClass, dataQueryOptions, responder );
      }

      @Override
      public E findById( String objectId ) throws BackendlessException
      {
        return findById( objectId, (List) null );
      }

      @Override
      public E findById( String objectId, List<String> relations ) throws BackendlessException
      {
        return Backendless.Persistence.findById( entityClass, objectId, relations );
      }
      
      @Override
      public E findById( String objectId, int relationsDepth ) throws BackendlessException
      {
        List<String> relations = new ArrayList<String>();
        relations.add( "*" );
        return findById( objectId, relations, relationsDepth );
      }

      @Override
      public E findById( String objectId, List<String> relations, int relationsDepth ) throws BackendlessException
      {
        return Backendless.Persistence.findById( entityClass, objectId, relations, relationsDepth );
      }

      @Override
      public void findById( String objectId, AsyncCallback<E> responder )
      {
        findById( objectId, (List) null, responder );
      }
      
      @Override
      public void findById( String objectId, List<String> relations, AsyncCallback<E> responder )
      {
        Backendless.Persistence.findById( entityClass, objectId, relations, responder );
      }
      
      @Override
      public void findById( String objectId, int relationsDepth, AsyncCallback<E> responder )
      {
        List<String> relations = new ArrayList<String>();
        relations.add( "*" );
        findById( objectId, relations, relationsDepth );
      }

      @Override
      public void findById( String objectId, List<String> relations, int relationsDepth, AsyncCallback<E> responder )
      {
        Backendless.Persistence.findById( entityClass, objectId, relations, relationsDepth, responder );
      }

      @Override
      public void loadRelations( E entity, List<String> relations ) throws Exception
      {
        Backendless.Persistence.loadRelations( entity, relations );
      }

      @Override
      public void loadRelations( E entity, List<String> relations, AsyncCallback<E> responder )
      {
        Backendless.Persistence.loadRelations( entity, relations, responder );
      }
    };
  }
}