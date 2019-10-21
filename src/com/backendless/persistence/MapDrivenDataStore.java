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

import android.support.annotation.Nullable;

import com.backendless.Backendless;
import com.backendless.IDataStore;
import com.backendless.Invoker;
import com.backendless.Persistence;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.core.responder.AdaptingResponder;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.offline.LocalStorageManager;
import com.backendless.persistence.offline.OfflineAwareCallback;
import com.backendless.persistence.offline.SyncCompletionCallback;
import com.backendless.persistence.offline.SyncManager;
import com.backendless.persistence.offline.TransactionManager;
import com.backendless.rt.data.EventHandler;
import com.backendless.rt.data.EventHandlerFactory;
import com.backendless.utils.ResponderHelper;
import weborb.client.Fault;
import weborb.client.IRawResponder;
import weborb.client.IResponder;
import weborb.exceptions.AdaptingException;
import weborb.reader.AnonymousObject;
import weborb.reader.NamedObject;
import weborb.types.IAdaptingType;
import weborb.v3types.ErrMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapDrivenDataStore implements IDataStore<Map>
{
  private static final List<String> emptyRelations = new ArrayList<String>();
  private static final EventHandlerFactory eventHandlerFactory = new EventHandlerFactory();
  private String tableName;

  private final EventHandler<Map> eventHandler;

  /*
    TODO: OFFLINE SECTION
   */

  private static final TransactionManager transactionManager = TransactionManager.getInstance();
  private static final SyncManager syncManager = SyncManager.getInstance();
  private final LocalStorageManager storageManager;

  public MapDrivenDataStore( String tableName )
  {
    this.tableName = tableName;
    eventHandler = eventHandlerFactory.of( tableName );
    storageManager = new LocalStorageManager(tableName);
  }

  @Override
  public List<String> create( List<Map> objects ) throws BackendlessException
  {
    return create( objects, null, false );
  }

  @Override
  public void create( List<Map> objects, AsyncCallback<List<String>> responder )
  {
    create( objects, responder, true );
  }

  private List<String> create( List<Map> objects, AsyncCallback<List<String>> responder, boolean async ) throws BackendlessException
  {
    if( objects == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_BULK );

    if( objects.isEmpty() )
      return new ArrayList<>();

    Object[] args = new Object[]{tableName, objects};

    if( async )
      Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "createBulk", args, responder, ResponderHelper.getCollectionAdaptingResponder( String.class ) );
    else
      return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "createBulk", args, ResponderHelper.getCollectionAdaptingResponder( String.class ) );

    return null;
  }

  @Override
  public Map save( Map entity ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    Object[] args = new Object[] { tableName, entity };
    Map newEntity = Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "save", args, new MapDrivenResponder() );
    return newEntity;
  }

  @Override
  public void save( Map entity, final AsyncCallback<Map> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      Object[] args = new Object[] { tableName, entity };
      Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "save", args, responder, new MapDrivenResponder() );
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

    Object[] args = new Object[] { tableName, entity };
    Object result = Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "remove", args );
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

      Object[] args = new Object[] { tableName, entity };
      Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "remove", args, removalCallback );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  @Override
  public int remove( final String whereClause ) throws BackendlessException
  {
    Object[] args = new Object[] { tableName, whereClause };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "removeBulk", args );
  }

  @Override
  public void remove( final String whereClause, AsyncCallback<Integer> responder ) throws BackendlessException
  {
    Object[] args = new Object[] { tableName, whereClause };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "removeBulk", args, responder );
  }

  @Override
  public int update( final String whereClause, Map<String, Object> changes ) throws BackendlessException
  {
    Object[] args = new Object[] { tableName, whereClause, changes };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "updateBulk", args );
  }

  @Override
  public void update( final String whereClause, Map<String, Object> changes, AsyncCallback<Integer> responder ) throws BackendlessException
  {
    Object[] args = new Object[] { tableName, whereClause, changes };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "updateBulk", args, responder );
  }

  @Override
  public Map findFirst() throws BackendlessException
  {
    Object[] args = new Object[] { tableName };
    return (Map) Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "first", args,
                                     new MapDrivenResponder() );
  }

  @Override
  public Map findFirst( Integer relationsDepth ) throws BackendlessException
  {
    return findFirst( emptyRelations, relationsDepth );
  }

  @Override
  public Map findFirst( List<String> relations ) throws BackendlessException
  {
    return findFirst( relations, (Integer) null );
  }

  private Map findFirst( List<String> relations, int relationsDepth ) throws BackendlessException
  {
    Object[] args = new Object[] { tableName, relations, relationsDepth };
    return (Map) Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "first", args,
                                     new MapDrivenResponder() );
  }

  @Override
  public void findFirst( AsyncCallback<Map> callback )
  {
    try
    {
      Object[] args = new Object[] { tableName };
      Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "first", args, callback,
                           new MapDrivenResponder() );
    }
    catch( Throwable e )
    {
      if( callback != null )
        callback.handleFault( new BackendlessFault( e ) );
    }
  }

  @Override
  public void findFirst( Integer relationsDepth, AsyncCallback<Map> callback )
  {
    findFirst( emptyRelations, relationsDepth, callback );
  }

  @Override
  public void findFirst( List<String> relations, AsyncCallback<Map> callback )
  {
    findFirst( relations, null, callback );
  }

  private void findFirst( List<String> relations, Integer relationsDepth, AsyncCallback<Map> callback )
  {
    try
    {
      Object[] args = new Object[] { tableName, relations, relationsDepth };
      Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "first", args, callback,
                           new MapDrivenResponder() );
    }
    catch( Throwable e )
    {
      if( callback != null )
        callback.handleFault( new BackendlessFault( e ) );
    }
  }

  @Override
  public Map findLast() throws BackendlessException
  {
    Object[] args = new Object[] { tableName };
    return (Map) Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "last", args,
                                     new MapDrivenResponder() );
  }

  @Override
  public Map findLast( Integer relationsDepth ) throws BackendlessException
  {
    return findLast( emptyRelations, relationsDepth );
  }

  @Override
  public Map findLast( List<String> relations ) throws BackendlessException
  {
    return findLast( relations, (Integer) null );
  }

  private Map findLast( List<String> relations, Integer relationsDepth ) throws BackendlessException
  {
    Object[] args = new Object[] { tableName, relations, relationsDepth };
    return (Map) Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "last", args,
                                     new MapDrivenResponder() );
  }

  @Override
  public void findLast( AsyncCallback<Map> callback )
  {
    try
    {
      Object[] args = new Object[] { tableName };
      Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "last", args, callback,
                           new MapDrivenResponder() );
    }
    catch( Throwable e )
    {
      if( callback != null )
        callback.handleFault( new BackendlessFault( e ) );
    }
  }

  @Override
  public void findLast( Integer relationsDepth, AsyncCallback<Map> callback )
  {
    findLast( emptyRelations, relationsDepth, callback );
  }

  @Override
  public void findLast( List<String> relations, AsyncCallback<Map> callback )
  {
    findLast( relations, null, callback );
  }

  private void findLast( List<String> relations, Integer relationsDepth, AsyncCallback<Map> callback )
  {
    try
    {
      Object[] args = new Object[] { tableName, relations, relationsDepth };
      Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "last", args, callback,
                           new MapDrivenResponder() );
    }
    catch( Throwable e )
    {
      if( callback != null )
        callback.handleFault( new BackendlessFault( e ) );
    }
  }

  @Override
  public List<Map> find() throws BackendlessException
  {
    return find( DataQueryBuilder.create() );
  }

  @Override
  public List<Map> find( DataQueryBuilder dataQuery ) throws BackendlessException
  {
      if (!storageManager.shouldRetrieveOnline(dataQuery))
        return storageManager.find(Map.class, dataQuery);

      Object[] args = new Object[]{tableName, dataQuery.build()};

      List<Map> result = Invoker.invokeSync(Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "find", args,
          ResponderHelper.getCollectionAdaptingResponder(HashMap.class));
      storageManager.store(result, dataQuery);
      return result;
  }

  @Override
  public void find( AsyncCallback<List<Map>> callback )
  {
    find( DataQueryBuilder.create(), callback );
  }

  @Override
  public void find( final DataQueryBuilder dataQuery, final AsyncCallback<List<Map>> callback )
  {
    try
    {
      if (!storageManager.shouldRetrieveOnline(dataQuery)) {
        storageManager.find(Map.class, dataQuery, callback);
        return;
      }

      AsyncCallback<List<Map>> storeCallback = storageManager.getStoreCallback(dataQuery, callback);

      Object[] args = new Object[]{tableName, dataQuery.build()};
      Invoker.invokeAsync(Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "find", args, storeCallback,
          ResponderHelper.getCollectionAdaptingResponder(HashMap.class));

    }
    catch( Throwable e )
    {
      if( callback != null )
        callback.handleFault( new BackendlessFault( e ) );
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

    Object[] args = new Object[] { tableName, id, relations };
    return (Map) Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", args,
                                     new MapDrivenResponder() );
  }

  @Override
  public Map findById( String id, Integer relationsDepth ) throws BackendlessException
  {
    return findById( id, emptyRelations, relationsDepth );
  }

  @Override
  public Map findById( String id, List<String> relations, Integer relationsDepth ) throws BackendlessException
  {
    if( id == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ID );

    Object[] args = new Object[] { tableName, id, relations, relationsDepth };
    return (Map) Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", args,
                                     new MapDrivenResponder() );
  }

  @Override
  public Map findById( Map entity ) throws BackendlessException
  {
    return findById( entity, emptyRelations, (Integer) null );
  }

  @Override
  public Map findById( Map entity, List<String> relations ) throws BackendlessException
  {
    return findById( entity, relations, (Integer) null );
  }

  @Override
  public Map findById( Map entity, Integer relationsDepth ) throws BackendlessException
  {
    return findById( entity, emptyRelations, relationsDepth );
  }

  @Override
  public Map findById( Map entity, List<String> relations, Integer relationsDepth ) throws BackendlessException
  {
    Object[] args = new Object[] { tableName, entity, relations, relationsDepth };
    return (Map) Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", args,
                                     new MapDrivenResponder() );
  }

  @Override
  public void findById( String id, AsyncCallback<Map> callback )
  {
    findById( id, emptyRelations, callback );
  }

  @Override
  public void findById( String id, List<String> relations, AsyncCallback<Map> callback )
  {
    findById( id, relations, null, callback );
  }

  @Override
  public void findById( String id, Integer relationsDepth, AsyncCallback<Map> callback )
  {
    findById( id, emptyRelations, relationsDepth, callback );
  }

  @Override
  public void findById( String id, List<String> relations, Integer relationsDepth, AsyncCallback<Map> callback )
  {
    try
    {
      if( id == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ID );

      Object[] args = new Object[] { tableName, id, relations, relationsDepth };
      Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", args, callback,
                           new MapDrivenResponder() );
    }
    catch( Throwable e )
    {
      if( callback != null )
        callback.handleFault( new BackendlessFault( e ) );
    }
  }

  @Override
  public void findById( Map entity, AsyncCallback<Map> callback )
  {
    findById( entity, emptyRelations, callback );
  }

  @Override
  public void findById( Map entity, List<String> relations, AsyncCallback<Map> callback )
  {
    findById( entity, relations, null, callback );
  }

  @Override
  public void findById( Map entity, Integer relationsDepth, AsyncCallback<Map> callback )
  {
    findById( entity, emptyRelations, relationsDepth, callback );
  }

  @Override
  public void findById( Map entity, List<String> relations, Integer relationsDepth, AsyncCallback<Map> callback )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      Object[] args = new Object[] { tableName, entity, relations, relationsDepth };
      Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", args, callback,
                           new MapDrivenResponder() );
    }
    catch( Throwable e )
    {
      if( callback != null )
        callback.handleFault( new BackendlessFault( e ) );
    }
  }

  @Override
  public Map findById( String id, DataQueryBuilder queryBuilder ) throws BackendlessException
  {
    Object[] args = new Object[] { tableName, id, queryBuilder.build() };
    return (Map) Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", args,
                                     new MapDrivenResponder() );
  }

  @Override
  public Map findById( Map entity, DataQueryBuilder queryBuilder ) throws BackendlessException
  {
    Object[] args = new Object[] { tableName, entity, queryBuilder.build() };
    return (Map) Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", args,
                                     new MapDrivenResponder() );
  }

  @Override
  public void findById( String id, DataQueryBuilder queryBuilder, AsyncCallback<Map> callback )
  {
    try
    {
      Object[] args = new Object[] { tableName, id, queryBuilder.build() };
      Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", args, callback,
                           new MapDrivenResponder() );
    }
    catch( Throwable e )
    {
      if( callback != null )
        callback.handleFault( new BackendlessFault( e ) );
    }
  }

  @Override
  public void findById( Map entity, DataQueryBuilder queryBuilder, AsyncCallback<Map> callback )
  {
    try
    {
      Object[] args = new Object[] { tableName, entity, queryBuilder.build() };
      Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", args, callback,
                           new MapDrivenResponder() );
    }
    catch( Throwable e )
    {
      if( callback != null )
        callback.handleFault( new BackendlessFault( e ) );
    }
  }

  @Override
  public <R> List<R> loadRelations( String objectId, LoadRelationsQueryBuilder<R> queryBuilder )
  {
    return Backendless.Data.loadRelations( tableName, objectId, queryBuilder, queryBuilder.getRelationType() );
  }

  @Override
  public <R> void loadRelations( String objectId, LoadRelationsQueryBuilder<R> queryBuilder,
                                 AsyncCallback<List<R>> callback )
  {
    Backendless.Data.loadRelations( tableName, objectId, queryBuilder, queryBuilder.getRelationType(), callback );
  }

  @Override
  public int getObjectCount()
  {
    Object[] args = new Object[] { tableName };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "count", args );
  }

  @Override
  public int getObjectCount( DataQueryBuilder dataQueryBuilder )
  {
    if( dataQueryBuilder == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_FIELD( "dataQueryBuilder" ) );

    BackendlessDataQuery dataQuery = dataQueryBuilder.build();
    Object[] args = new Object[] { tableName, dataQuery };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "count", args );
  }

  @Override
  public void getObjectCount( AsyncCallback<Integer> responder )
  {
    try
    {
      Object[] args = new Object[] { tableName };
      Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "count", args, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  @Override
  public void getObjectCount( DataQueryBuilder dataQueryBuilder, AsyncCallback<Integer> responder )
  {
    try
    {
      if( dataQueryBuilder == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_FIELD( "dataQueryBuilder" ) );

      BackendlessDataQuery dataQuery = dataQueryBuilder.build();
      Object[] args = new Object[] { tableName, dataQuery };
      Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "count", args, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  @Override
  public <R> int addRelation( Map parent, String relationColumnName, Collection<R> childs )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Collection<String> childObjectIds = new ArrayList<>();
    for( R child : childs )
    {
      String childObjectId = child instanceof Map ? (String) ((Map) child).get( Persistence.DEFAULT_OBJECT_ID_FIELD ) : Persistence.getEntityId( child );
      childObjectIds.add( childObjectId );
    }

    Object[] args = new Object[] { tableName, relationColumnName, parentObjectId, childObjectIds };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "addRelation", args );
  }

  @Override
  public <R> void addRelation( Map parent, String relationColumnName, Collection<R> childs, AsyncCallback<Integer> callback )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Collection<String> childObjectIds = new ArrayList<>();
    for( R child : childs )
    {
      String childObjectId = child instanceof Map ? (String) ((Map) child).get( Persistence.DEFAULT_OBJECT_ID_FIELD ) : Persistence.getEntityId( child );
      childObjectIds.add( childObjectId );
    }

    Object[] args = new Object[] { tableName, relationColumnName, parentObjectId, childObjectIds };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "addRelation", args, callback );
  }

  @Override
  public int addRelation( Map parent, String relationColumnName, String whereClause )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Object[] args = new Object[] { tableName, relationColumnName, parentObjectId, whereClause };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "addRelation", args );
  }

  @Override
  public void addRelation( Map parent, String relationColumnName, String whereClause, AsyncCallback<Integer> callback )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Object[] args = new Object[] { tableName, relationColumnName, parentObjectId, whereClause };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "addRelation", args, callback );
  }

  @Override
  public <R> int setRelation( Map parent, String relationColumnName, Collection<R> children )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Collection<String> childObjectIds = new ArrayList<>();
    for( R child : children )
    {
      String childObjectId = child instanceof Map ? (String) ((Map) child).get( Persistence.DEFAULT_OBJECT_ID_FIELD ) : Persistence.getEntityId( child );
      childObjectIds.add( childObjectId );
    }

    Object[] args = new Object[] { tableName, relationColumnName, parentObjectId, childObjectIds };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "setRelation", args );
  }

  @Override
  public <R> void setRelation( Map parent, String relationColumnName, Collection<R> children, AsyncCallback<Integer> callback )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Collection<String> childObjectIds = new ArrayList<>();
    for( R child : children )
    {
      String childObjectId = child instanceof Map ? (String) ((Map) child).get( Persistence.DEFAULT_OBJECT_ID_FIELD ) : Persistence.getEntityId( child );
      childObjectIds.add( childObjectId );
    }

    Object[] args = new Object[] { tableName, relationColumnName, parentObjectId, childObjectIds };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "setRelation", args, callback );
  }

  @Override
  public int setRelation( Map parent, String relationColumnName, String whereClause )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Object[] args = new Object[] { tableName, relationColumnName, parentObjectId, whereClause };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "setRelation", args );
  }

  @Override
  public void setRelation( Map parent, String relationColumnName, String whereClause, AsyncCallback<Integer> callback )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Object[] args = new Object[] { tableName, relationColumnName, parentObjectId, whereClause };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "setRelation", args, callback );
  }

  @Override
  public <R> int deleteRelation( Map parent, String relationColumnName, Collection<R> children )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Collection<String> childObjectIds = new ArrayList<>();
    for( R child : children )
    {
      String childObjectId = child instanceof Map ? (String) ((Map) child).get( Persistence.DEFAULT_OBJECT_ID_FIELD ) : Persistence.getEntityId( child );
      childObjectIds.add( childObjectId );
    }

    Object[] args = new Object[] { tableName, relationColumnName, parentObjectId, childObjectIds };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "deleteRelation", args );
  }

  @Override
  public <R> void deleteRelation( Map parent, String relationColumnName, Collection<R> children, AsyncCallback<Integer> callback )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Collection<String> childObjectIds = new ArrayList<>();
    for( R child : children )
    {
      String childObjectId = child instanceof Map ? (String) ((Map) child).get( Persistence.DEFAULT_OBJECT_ID_FIELD ) : Persistence.getEntityId( child );
      childObjectIds.add( childObjectId );
    }

    Object[] args = new Object[] { tableName, relationColumnName, parentObjectId, childObjectIds };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "deleteRelation", args, callback );
  }

  @Override
  public int deleteRelation( Map parent, String relationColumnName, String whereClause )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Object[] args = new Object[] { tableName, relationColumnName, parentObjectId, whereClause };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "deleteRelation", args );
  }

  @Override
  public void deleteRelation( Map parent, String relationColumnName, String whereClause, AsyncCallback<Integer> callback )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Object[] args = new Object[] { tableName, relationColumnName, parentObjectId, whereClause };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "deleteRelation", args, callback );
  }

  @Override
  public EventHandler<Map> rt()
  {
    return eventHandler;
  }

  /*
    TODO: OFFLINE SECTION
   */

    @Override
    public void initLocalDatabase(@Nullable String whereClause, AsyncCallback<Integer> responder) {
        storageManager.initDatabase(whereClause, responder);
    }

    @Override
    public void clearLocalDatabase() {
        storageManager.clearLocalDatabase();
    }


  @Override
  public void saveEventually(Map entity) {
      saveEventually(entity, null);
  }

  @Override
  public void saveEventually(Map entity, OfflineAwareCallback<Map> responder) {
      int blLocalId = storageManager.save(entity, responder);
      entity.put("blLocalId", blLocalId);

      Object[] args = new Object[] { tableName, entity };

      transactionManager.scheduleOperation("save", args, null, responder);
  }

  @Override
  public void removeEventually(Map entity) {
      removeEventually(entity, null);
  }

  @Override
  public void removeEventually(Map entity, OfflineAwareCallback<Map> responder) {
      Object[] args = new Object[] { tableName, entity };

      transactionManager.scheduleOperation("remove", args, null, responder);
  }

  @Override
  public void onSave(AsyncCallback<Map> responder) {
    transactionManager.onSave(tableName, responder);
  }

  @Override
  public void onRemove(AsyncCallback<Map> responder) {
    transactionManager.onRemove(tableName, responder);
  }

  @Override
  public void enableAutoSync() {
      syncManager.enableAutoSync(tableName);
  }

  @Override
  public void disableAutoSync() {
    syncManager.disableAutoSync(tableName);
  }

  @Override
  public boolean isAutoSyncEnabled() {
      return syncManager.isAutoSyncEnabled(tableName);
  }

  @Override
  public void startOfflineSync(SyncCompletionCallback callback) {
      syncManager.startSemiAutoSynchronization(tableName, callback);
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
        if( nextResponder != null )
        {
          nextResponder.errorHandler( AdaptingResponder.adaptFault( (AnonymousObject) bodyHolder ) );
        }
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
  }
}
