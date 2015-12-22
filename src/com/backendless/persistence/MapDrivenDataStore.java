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

package com.backendless.persistence;

import com.backendless.*;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.utils.ResponderHelper;
import weborb.client.Fault;
import weborb.client.IRawResponder;
import weborb.client.IResponder;
import weborb.exceptions.AdaptingException;
import weborb.reader.AnonymousObject;
import weborb.reader.NamedObject;
import weborb.reader.StringType;
import weborb.types.IAdaptingType;
import weborb.v3types.ErrMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapDrivenDataStore implements IDataStore<Map>
{
  private final static String PERSISTENCE_MANAGER_SERVER_ALIAS = "com.backendless.services.persistence.PersistenceService";
  private static final List<String> emptyRelations = new ArrayList<String>();
  private String tableName;

  public MapDrivenDataStore( String tableName )
  {
   this.tableName = tableName;
  }

  @Override
  public Map save( Map entity ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    Object[] args = new Object[]{Backendless.getApplicationId(), Backendless.getVersion(), tableName, entity};
    Map newEntity = Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "save", args, new MapDrivenResponder() );
    return newEntity;
  }

  @Override
  public void save( Map entity, final AsyncCallback<Map> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), tableName, entity };
      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "save", args, responder, new MapDrivenResponder() );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  @Override
  public Long remove( Map entity ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), tableName, entity };
    Object result = Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "remove", args );
    return ((Number) result).longValue();
  }

  @Override
  public void remove( Map entity, final AsyncCallback<Long> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      AsyncCallback<Object> removalCallback = new AsyncCallback<Object>()
      {
        @Override
        public void handleResponse( Object response )
        {
          responder.handleResponse( ((Number) response).longValue() );
        }

        @Override
        public void handleFault( BackendlessFault fault )
        {
          if( responder != null )
            responder.handleFault( fault );
        }
      };

      Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), tableName, entity };
      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "remove", args, removalCallback );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  @Override
  public Map findFirst() throws BackendlessException
  {
    Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), tableName };
    return (Map) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "first", args );
 }

  @Override
  public Map findFirst( int relationsDepth ) throws BackendlessException
  {
    return findFirst( emptyRelations, relationsDepth );
  }

  @Override
  public Map findFirst( List<String> relations ) throws BackendlessException
  {
    return findFirst( relations, 0 );
  }

  private Map findFirst( List<String> relations, int relationsDepth ) throws BackendlessException
  {
    Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), tableName, relations, relationsDepth };
    return (Map) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "first", args );
  }

  @Override
  public void findFirst( AsyncCallback<Map> responder )
  {
    try
    {
      Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), tableName };
      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "first", args, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  @Override
  public void findFirst( int relationsDepth, AsyncCallback<Map> responder )
  {
    findFirst( emptyRelations, relationsDepth, responder );
  }

  @Override
  public void findFirst( List<String> relations, AsyncCallback<Map> responder )
  {
    findFirst( relations, 0, responder );
  }

  private void findFirst( List<String> relations, int relationsDepth, AsyncCallback<Map> responder )
  {
    try
    {
      Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), tableName, relations, relationsDepth };
      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "first", args, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  @Override
  public Map findLast() throws BackendlessException
  {
    Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), tableName };
    return (Map) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "last", args );
  }

  @Override
  public Map findLast( int relationsDepth ) throws BackendlessException
  {
    return findLast( emptyRelations, relationsDepth );
  }

  @Override
  public Map findLast( List<String> relations ) throws BackendlessException
  {
    return findLast( relations, 0 );
  }

  private Map findLast( List<String> relations, int relationsDepth ) throws BackendlessException
  {
    Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), tableName, relations, relationsDepth };
    return (Map) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "last", args );
  }

  @Override
  public void findLast( AsyncCallback<Map> responder )
  {
    try
    {
      Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), tableName };
      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "last", args, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  @Override
  public void findLast( int relationsDepth, AsyncCallback<Map> responder )
  {
    findLast( emptyRelations, relationsDepth, responder );
  }

  @Override
  public void findLast( List<String> relations, AsyncCallback<Map> responder )
  {
    findLast( relations, 0, responder );
  }

  private void findLast( List<String> relations, int relationsDepth, AsyncCallback<Map> responder )
  {
    try
    {
      Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), tableName, relations, relationsDepth };
      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "last", args, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  @Override
  public BackendlessCollection<Map> find() throws BackendlessException
  {
    return find( new BackendlessDataQuery() );
  }

  @Override
  public BackendlessCollection<Map> find( BackendlessDataQuery dataQuery ) throws BackendlessException
  {
    Persistence.checkPageSizeAndOffset( dataQuery );
    Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), tableName, dataQuery };
    BackendlessCollection<Map> result = Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "find", args, ResponderHelper.getCollectionAdaptingResponder( HashMap.class ) );
    result.setQuery( dataQuery );
    result.setTableName( tableName );
    return result;
  }

  @Override
  public void find( AsyncCallback<BackendlessCollection<Map>> responder )
  {
     find( new BackendlessDataQuery(), responder );
  }

  @Override
  public void find( final BackendlessDataQuery dataQuery, final AsyncCallback<BackendlessCollection<Map>> responder )
  {
    try
    {
      Persistence.checkPageSizeAndOffset( dataQuery );

      AsyncCallback<BackendlessCollection<Map>> callback = new AsyncCallback<BackendlessCollection<Map>>()
      {
        @Override
        public void handleResponse( BackendlessCollection<Map> response )
        {
          if( responder != null )
          {
            response.setQuery( dataQuery );
            response.setTableName( tableName );
            responder.handleResponse( response );
          }
        }

        @Override
        public void handleFault( BackendlessFault fault )
        {
          if( responder != null )
            responder.handleFault( fault );
        }
      };

      Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), tableName, dataQuery };
      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "find", args, callback );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  @Override
  public Map findById( String id ) throws BackendlessException
  {
    return findById( id, emptyRelations );
  }

  @Override
  public Map findById( String id, List<String> relations ) throws BackendlessException
  {
    if( id == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ID );

    Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), tableName, id, relations };
    return (Map) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", args );
  }

  @Override
  public Map findById( String id, int relationsDepth ) throws BackendlessException
  {
    return findById( id, emptyRelations, relationsDepth );
  }

  @Override
  public Map findById( String id, List<String> relations, int relationsDepth ) throws BackendlessException
  {
    if( id == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ID );

    Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), tableName, id, relations, relationsDepth };
    return (Map) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", args );
  }

  @Override
  public Map findById( Map entity ) throws BackendlessException
  {
    return findById( entity, emptyRelations, 0 );
  }

  @Override
  public Map findById( Map entity, List<String> relations ) throws BackendlessException
  {
    return findById( entity, relations, 0 );
  }

  @Override
  public Map findById( Map entity, int relationsDepth ) throws BackendlessException
  {
    return findById( entity, emptyRelations, relationsDepth );
  }

  @Override
  public Map findById( Map entity, List<String> relations, int relationsDepth ) throws BackendlessException
  {
    Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), tableName, entity, relations, relationsDepth };
    return (Map) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", args );
  }

  @Override
  public void findById( String id, AsyncCallback<Map> responder )
  {
    findById( id, emptyRelations, responder );
  }

  @Override
  public void findById( String id, List<String> relations, AsyncCallback<Map> responder )
  {
    findById( id, relations, 0, responder );
  }

  @Override
  public void findById( String id, int relationsDepth, AsyncCallback<Map> responder )
  {
    findById( id, emptyRelations, relationsDepth, responder );
  }

  @Override
  public void findById( String id, List<String> relations, int relationsDepth, AsyncCallback<Map> responder )
  {
    try
    {
      if( id == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ID );

      Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), tableName, id, relations, relationsDepth };
      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", args, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  @Override
  public void findById( Map entity, AsyncCallback<Map> responder )
  {
    findById( entity, emptyRelations, responder );
  }

  @Override
  public void findById( Map entity, List<String> relations, AsyncCallback<Map> responder )
  {
    findById( entity, relations, 0, responder );
  }

  @Override
  public void findById( Map entity, int relationsDepth, AsyncCallback<Map> responder )
  {
    findById( entity, emptyRelations, relationsDepth, responder );
  }

  @Override
  public void findById( Map entity, List<String> relations, int relationsDepth, AsyncCallback<Map> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), tableName, entity, relations, relationsDepth };
      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", args, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  @Override
  public void loadRelations( Map entity, List<String> relations ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), tableName, entity, relations };
    Map loadedRelations = Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "loadRelations", args );
    entity.putAll( loadedRelations );
  }

  @Override
  public void loadRelations( final Map entity, List<String> relations, final AsyncCallback<Map> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), tableName, entity, relations };
      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "loadRelations", args, new AsyncCallback<Map>()
      {
        @Override
        public void handleResponse( Map loadedRelations )
        {
          try
          {
            entity.putAll( loadedRelations );

            if( responder != null )
              responder.handleResponse( entity );
          }
          catch( Exception e )
          {
            if( responder != null )
              responder.handleFault( new BackendlessFault( e ) );
          }
        }

        @Override
        public void handleFault( BackendlessFault fault )
        {
          if( responder != null )
            responder.handleFault( fault );
        }
      } );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  private class MapDrivenResponder implements IRawResponder
  {
    private IResponder nextResponder;

    @Override
    public void setNextResponder( IResponder iResponder )
    {
      this.nextResponder = iResponder;
    }

    @Override
    public void responseHandler( Object adaptingType )
    {
      IAdaptingType type = (IAdaptingType) adaptingType;
      IAdaptingType bodyHolder = ((NamedObject) type).getTypedObject();

     if( ((IAdaptingType) adaptingType).getDefaultType().equals( ErrMessage.class ) )
     {
       handledAsFault( (AnonymousObject) bodyHolder, nextResponder );
     }
     else
     {
       IAdaptingType entity = (IAdaptingType) ((AnonymousObject) bodyHolder).getProperties().get( "body" );
       try
       {
         Object adaptedEntity = entity.adapt( HashMap.class );

         if( nextResponder != null )
           nextResponder.responseHandler( adaptedEntity );
       }
       catch( AdaptingException e )
       {
         errorHandler( new BackendlessFault( e ) );
       }
     }
    }

    @Override
    public void errorHandler( Fault fault )
    {
      nextResponder.errorHandler( fault );
    }

    private void handledAsFault( AnonymousObject bodyHolder, IResponder responder )
    {
      if( responder != null )
      {
        StringType faultMessage = (StringType) bodyHolder.getProperties().get( "faultString" );
        StringType faultDetail = (StringType) bodyHolder.getProperties().get( "faultDetail" );
        StringType faultCode = (StringType) bodyHolder.getProperties().get( "faultCode" );

        Fault fault = new Fault( (String) faultMessage.defaultAdapt(), (String) faultDetail.defaultAdapt(), (String) faultCode.defaultAdapt() );
        responder.errorHandler( fault );
      }
    }
  }
}
