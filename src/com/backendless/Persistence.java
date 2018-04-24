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
import com.backendless.core.responder.AdaptingResponder;
import com.backendless.core.responder.policy.PoJoAdaptingPolicy;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.BackendlessSerializer;
import com.backendless.persistence.DataQueryBuilder;
import com.backendless.persistence.LoadRelationsQueryBuilder;
import com.backendless.persistence.MapDrivenDataStore;
import com.backendless.persistence.QueryOptions;
import com.backendless.property.ObjectProperty;
import com.backendless.utils.MapEntityUtil;
import com.backendless.utils.ReflectionUtil;
import com.backendless.utils.ResponderHelper;
import com.backendless.utils.StringUtils;
import weborb.client.IChainedResponder;
import weborb.types.Types;
import weborb.writer.IObjectSubstitutor;
import weborb.writer.MessageWriter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class Persistence
{
  public final static String PERSISTENCE_MANAGER_SERVER_ALIAS = "com.backendless.services.persistence.PersistenceService";
  public final static String DATA_MANAGEMENT_SERVER_ALIAS = "com.backendless.management.persistence.DataManagementApi";
  private final static String DEFAULT_OBJECT_ID_GETTER = "getObjectId";
  public final static String DEFAULT_OBJECT_ID_FIELD = "objectId";
  public final static String DEFAULT_CREATED_FIELD = "created";
  public final static String DEFAULT_UPDATED_FIELD = "updated";
  public final static String DEFAULT_META_FIELD = "__meta";
  public final static String REST_CLASS_FIELD = "___class";
  public final static String PARCELABLE_CREATOR_FIELD_NAME = "CREATOR";

  public final static String LOAD_ALL_RELATIONS = "*";
  public final static DataPermission Permissions = new DataPermission();

  private static final Persistence instance = new Persistence();

  static Persistence getInstance()
  {
    return instance;
  }

  private Persistence()
  {
    Types.addClientClassMapping( "com.backendless.services.persistence.BackendlessDataQuery", BackendlessDataQuery.class );
    Types.addClientClassMapping( "com.backendless.services.persistence.ObjectProperty", ObjectProperty.class );
    Types.addClientClassMapping( "com.backendless.services.persistence.QueryOptions", QueryOptions.class );
  }

  public void mapTableToClass( String tableName, Class clazz )
  {
    weborb.types.Types.addClientClassMapping( tableName, clazz );
  }

  public <E> List<String> create( List<E> objects ) throws BackendlessException
  {
     return create( objects, null, false );
  }

  public <E> void create( List<E> objects, final AsyncCallback<List<String>> responder )
  {
    create( objects, responder, true );
  }

  private <E> List<String> create( List<E> objects, final AsyncCallback<List<String>> responder, boolean async )
  {
    if( objects == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_BULK );

    if( objects.isEmpty() )
      return new ArrayList<>();
    
    String tableName =  BackendlessSerializer.getSimpleName( objects.get( 0 ).getClass() );
    Object[] args = new Object[] { tableName , objects };

    if( async )
      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "createBulk", args, responder, ResponderHelper.getCollectionAdaptingResponder( String.class ) );
    else
      return Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "createBulk", args, ResponderHelper.getCollectionAdaptingResponder( String.class ) );

    return null;
  }

  public <E> E save( final E entity ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    checkDeclaredType( entity.getClass() );
    final Map<String, Object> serializedEntity = BackendlessSerializer.serializeToMap( entity );
    MapEntityUtil.removeNullsAndRelations( serializedEntity);

    MessageWriter.setObjectSubstitutor( new IObjectSubstitutor()
    {
      @Override
      public Object substitute( Object o )
      {
        if( o == entity )
          return serializedEntity;
        else
          return o;
      }
    } );

    try
    {
      String method = "create";

      if( serializedEntity.containsKey( Persistence.DEFAULT_OBJECT_ID_FIELD ) &&
              serializedEntity.get( Persistence.DEFAULT_OBJECT_ID_FIELD ) != null )
        method = "update";

      E newEntity = Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, method,
                                        new Object[] {
                                                BackendlessSerializer.getSimpleName( entity.getClass() ),
                                                serializedEntity },
                                        ResponderHelper.getPOJOAdaptingResponder( entity.getClass() ) );

      if( serializedEntity.get( Persistence.DEFAULT_OBJECT_ID_FIELD ) == null )
        FootprintsManager.getInstance().Inner.duplicateFootprintForObject( serializedEntity, newEntity, entity );
      else
        FootprintsManager.getInstance().Inner.updateFootprintForObject( serializedEntity, newEntity, entity );

      //put or update footprint's properties to user's properties, if exists
      Footprint footprint = FootprintsManager.getInstance().getEntityFootprint( newEntity );
      if( footprint != null )
        footprint.initObjectId( entity );

      return newEntity;
    }
    finally
    {
      MessageWriter.setObjectSubstitutor( null );
    }
  }

  public <E> void save( final E entity, final AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      checkDeclaredType( entity.getClass() );
      final Map<String, Object> serializedEntity = BackendlessSerializer.serializeToMap( entity );
      MapEntityUtil.removeNullsAndRelations( serializedEntity );

      MessageWriter.setObjectSubstitutor( new IObjectSubstitutor()
      {
        @Override
        public Object substitute( Object o )
        {
          if( o == entity )
            return serializedEntity;
          else
            return o;
        }
      } );

      AsyncCallback<E> callbackOverrider;
      if( serializedEntity.get( Persistence.DEFAULT_OBJECT_ID_FIELD ) == null )
      {
        callbackOverrider = new AsyncCallback<E>()
        {
          @Override
          public void handleResponse( E newEntity )
          {
            MessageWriter.setObjectSubstitutor( null );
            FootprintsManager.getInstance().Inner.duplicateFootprintForObject( serializedEntity, newEntity, entity );
            Footprint footprint = FootprintsManager.getInstance().getEntityFootprint( newEntity );
            if( footprint != null )
              footprint.initObjectId( entity );

            if( responder != null )
              responder.handleResponse( newEntity );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            MessageWriter.setObjectSubstitutor( null );

            if( responder != null )
              responder.handleFault( fault );
          }
        };
      }
      else
      {
        callbackOverrider = new AsyncCallback<E>()
        {
          @Override
          public void handleResponse( E newEntity )
          {
            FootprintsManager.getInstance().Inner.updateFootprintForObject( serializedEntity, newEntity, entity );
            Footprint footprint = FootprintsManager.getInstance().getEntityFootprint( newEntity );
            if( footprint != null )
              footprint.initObjectId( entity );

            if( responder != null )
              responder.handleResponse( newEntity );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            if( responder != null )
              responder.handleFault( fault );
          }
        };
      }

      String method = "create";

      if( serializedEntity.containsKey( Persistence.DEFAULT_OBJECT_ID_FIELD ) &&
              serializedEntity.get( Persistence.DEFAULT_OBJECT_ID_FIELD ) != null )
        method = "save";

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, method, new Object[] { BackendlessSerializer.getSimpleName( entity.getClass() ), entity }, callbackOverrider, ResponderHelper.getPOJOAdaptingResponder( entity.getClass() ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public <E> E create( Class<E> aClass, Map entity ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "create", new Object[] { BackendlessSerializer.getSimpleName( aClass ), entity }, ResponderHelper.getPOJOAdaptingResponder( aClass ) );
  }

  public <E> void create( final Class<E> aClass, final Map entity, final AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "create", new Object[] { BackendlessSerializer.getSimpleName( aClass ), entity }, responder, ResponderHelper.getPOJOAdaptingResponder( aClass ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  private <E> E update( Class<E> aClass, Map entity ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "update", new Object[] { BackendlessSerializer.getSimpleName( aClass ), entity }, ResponderHelper.getPOJOAdaptingResponder( aClass ) );
  }

  private <E> void update( final Class<E> aClass, final Map entity, final AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "update", new Object[] { BackendlessSerializer.getSimpleName( aClass ), entity }, responder, ResponderHelper.getPOJOAdaptingResponder( aClass ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  protected <E> Long remove( final E entity ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    Map<String, Object> entityMap = BackendlessSerializer.serializeToMap( entity );
    FootprintsManager.getInstance().Inner.putMissingPropsToEntityMap( entity, entityMap );

    Object result = Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "remove", new Object[] { BackendlessSerializer.getSimpleName( entity.getClass() ), entityMap } );

    FootprintsManager.getInstance().Inner.removeFootprintForObject( entityMap, entity );

    return ((Number) result).longValue();
  }

  protected <E> void remove( final E entity, final AsyncCallback<Long> responder )
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
          FootprintsManager.getInstance().Inner.removeFootprintForObject( BackendlessSerializer.serializeToMap( entity ), entity );

          if( responder == null )
            return;

          responder.handleResponse( ((Number) response).longValue() );
        }

        @Override
        public void handleFault( BackendlessFault fault )
        {
          if( responder != null )
            responder.handleFault( fault );
        }
      };

      Map<String, Object> entityMap = BackendlessSerializer.serializeToMap( entity );
      FootprintsManager.getInstance().Inner.putMissingPropsToEntityMap( entity, entityMap );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "remove", new Object[] { BackendlessSerializer.getSimpleName( entity.getClass() ), entityMap }, removalCallback );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  int remove( final String entityName, final String whereClause ) throws BackendlessException
  {
    Object[] args = new Object[] { entityName, whereClause };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "removeBulk", args );
  }

  void remove( final String entityName, final String whereClause, AsyncCallback<Integer> responder ) throws BackendlessException
  {
    Object[] args = new Object[] { entityName, whereClause };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "removeBulk", args, responder );
  }

  int update( final String entityName, final String whereClause, Map<String, Object> changes ) throws BackendlessException
  {
    Object[] args = new Object[] { entityName, whereClause, changes };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "updateBulk", args );
  }

  void update( final String entityName, final String whereClause, Map<String, Object> changes, AsyncCallback<Integer> responder ) throws BackendlessException
  {
    Object[] args = new Object[] { entityName, whereClause, changes };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "updateBulk", args, responder );
  }

  protected <E> E findById( final Class<E> entity, final String id,
                            final List<String> relations ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY_NAME );

    if( id == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ID );

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", new Object[] { BackendlessSerializer.getSimpleName( entity ), id, relations }, ResponderHelper.getPOJOAdaptingResponder( entity ) );
  }

  protected <E> E findById( final Class<E> entity, final String id, final List<String> relations,
                            final Integer relationsDepth ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY_NAME );

    if( id == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ID );

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", new Object[] { BackendlessSerializer.getSimpleName( entity ), id, relations, relationsDepth }, ResponderHelper.getPOJOAdaptingResponder( entity ) );
  }

  protected <E> E findById( final E entity, List<String> relations, Integer relationsDepth )
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    Object entityArg = ReflectionUtil.hasField( entity.getClass(), Persistence.DEFAULT_OBJECT_ID_FIELD ) ?
            entity :
            FootprintsManager.getInstance().getObjectId( entity );

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById",
                                   new Object[] {
                                           BackendlessSerializer.getSimpleName( entity.getClass() ), entityArg,
                                           relations, relationsDepth },
                                   ResponderHelper.getPOJOAdaptingResponder( entity.getClass() ) );
  }

  protected <E> E findById( final E entity, final DataQueryBuilder queryBuilder )
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    if( queryBuilder == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_FIELD( "queryBuilder" ) );

    Object entityArg = ReflectionUtil.hasField( entity.getClass(), Persistence.DEFAULT_OBJECT_ID_FIELD ) ?
            entity :
            FootprintsManager.getInstance().getObjectId( entity );

    Object[] args = new Object[] { BackendlessSerializer.getSimpleName( entity.getClass() ), entityArg, queryBuilder.build() };

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", args, ResponderHelper.getPOJOAdaptingResponder( entity.getClass() ) );
  }

  protected <E> E findById( final Class<E> entity, final String id, final DataQueryBuilder queryBuilder )
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY_NAME );

    if( id == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ID );

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", new Object[] { BackendlessSerializer.getSimpleName( entity ), id, queryBuilder.build() }, ResponderHelper.getPOJOAdaptingResponder( entity ) );
  }

  protected <E> void findById( final Class<E> entity, final String id, final List<String> relations,
                               AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      if( id == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ID );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", new Object[] { BackendlessSerializer.getSimpleName( entity ), id, relations }, responder, ResponderHelper.getPOJOAdaptingResponder( entity ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  protected <E> void findById( final Class<E> entity, final String id, final List<String> relations,
                               final Integer relationsDepth, AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      if( id == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ID );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", new Object[] { BackendlessSerializer.getSimpleName( entity ), id, relations, relationsDepth }, responder, ResponderHelper.getPOJOAdaptingResponder( entity ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  protected <E> void findById( E entity, List<String> relations, Integer relationsDepth, AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      IChainedResponder chainedResponder = new AdaptingResponder<E>( (Class<E>) entity.getClass(), new PoJoAdaptingPolicy<E>() );

      Object entityArg = ReflectionUtil.hasField( entity.getClass(), Persistence.DEFAULT_OBJECT_ID_FIELD ) ? entity : FootprintsManager.getInstance().getObjectId( entity );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", new Object[] { BackendlessSerializer.getSimpleName( entity.getClass() ), entityArg, relations, relationsDepth }, responder, chainedResponder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  protected <E> void findById( final E entity, final DataQueryBuilder queryBuilder, AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      if( queryBuilder == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_FIELD( "queryBuilder" ) );

      Object entityArg = ReflectionUtil.hasField( entity.getClass(), Persistence.DEFAULT_OBJECT_ID_FIELD ) ?
              entity :
              FootprintsManager.getInstance().getObjectId( entity );

      Object[] args = new Object[] { BackendlessSerializer.getSimpleName( entity.getClass() ), entityArg, queryBuilder.build() };

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", args, responder, ResponderHelper.getPOJOAdaptingResponder( entity.getClass() ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  protected <E> void findById( final Class<E> entity, final String id,
                               final DataQueryBuilder queryBuilder, AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY_NAME );

      if( id == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ID );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", new Object[] { BackendlessSerializer.getSimpleName( entity ), id, queryBuilder.build() }, responder, ResponderHelper.getPOJOAdaptingResponder( entity ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public <T> List<T> loadRelations( String parentType, String objectId, LoadRelationsQueryBuilder queryBuilder,
                                    Class<T> relatedType ) throws BackendlessException
  {
    StringUtils.checkEmpty( objectId, ExceptionMessage.NULL_ENTITY );
    if( queryBuilder == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_FIELD( "queryBuilder" ) );

    BackendlessDataQuery dataQuery = queryBuilder.build();
    String relationName = dataQuery.getQueryOptions().getRelated().iterator().next();
    int pageSize = dataQuery.getPageSize();
    int offset = dataQuery.getOffset();

    Object[] args = new Object[] { parentType, objectId, relationName, pageSize, offset };
    return Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "loadRelations", args,
                               ResponderHelper.getCollectionAdaptingResponder( relatedType ) );
  }

  public <T> void loadRelations( String parentType, String objectId, LoadRelationsQueryBuilder queryBuilder,
                                 Class<T> relatedType, final AsyncCallback<List<T>> responder )
  {
    StringUtils.checkEmpty( objectId, ExceptionMessage.NULL_ENTITY );
    if( queryBuilder == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_FIELD( "queryBuilder" ) );

    BackendlessDataQuery dataQuery = queryBuilder.build();
    String relationName = dataQuery.getQueryOptions().getRelated().iterator().next();
    int pageSize = dataQuery.getPageSize();
    int offset = dataQuery.getOffset();

    try
    {
      Object[] args = new Object[] { parentType, objectId, relationName, pageSize, offset };
      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "loadRelations", args, responder, ResponderHelper.getCollectionAdaptingResponder( relatedType ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  private <E> void loadRelationsToEntity( E entity, E loadedRelations, List<String> relations )
  {
    if( entity.getClass().equals( BackendlessUser.class ) )
    {
      BackendlessUser userWithRelations = (BackendlessUser) loadedRelations;
      BackendlessUser sourceUser = (BackendlessUser) entity;
      sourceUser.putProperties( userWithRelations.getProperties() );
    }
    else
    {
      Field[] declaredFields = entity.getClass().getDeclaredFields();

      for( Field declaredField : declaredFields )
      {
        if( !relations.contains( declaredField.getName() ) )
          continue;

        if( !declaredField.isAccessible() )
          declaredField.setAccessible( true );

        try
        {
          Object fieldValue = declaredField.get( loadedRelations );
          declaredField.set( entity, fieldValue );
        }
        catch( IllegalAccessException e )
        {
          //actually, won't be ever thrown because field was set accessible several lines above
          String message = String.format( ExceptionMessage.FIELD_NOT_ACCESSIBLE, declaredField.getName() ) + ": " + e.getMessage();
          throw new BackendlessException( message );
        }
      }
    }
  }

  public List<ObjectProperty> describe( String classSimpleName ) throws BackendlessException
  {
    if( classSimpleName == null || classSimpleName.equals( "" ) )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY_NAME );

    ObjectProperty[] response = Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "describe", new Object[] { classSimpleName } );

    return Arrays.asList( response );
  }

  public void describe( String classSimpleName, final AsyncCallback<List<ObjectProperty>> responder )
  {
    try
    {
      if( classSimpleName == null || classSimpleName.equals( "" ) )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY_NAME );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "describe", new Object[] { classSimpleName }, new AsyncCallback<ObjectProperty[]>()
      {
        @Override
        public void handleResponse( ObjectProperty[] response )
        {
          if( responder != null )
            responder.handleResponse( Arrays.asList( response ) );
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

  public <E> List<E> find( Class<E> entity, DataQueryBuilder queryBuilder ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );
    if( queryBuilder == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_FIELD( "queryBuilder" ) );

    Object[] args = new Object[] { BackendlessSerializer.getSimpleName( entity ), queryBuilder.build() };

    return Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "find", args, ResponderHelper.getCollectionAdaptingResponder( entity ) );
  }

  public <E> void find( Class<E> entity, DataQueryBuilder queryBuilder, AsyncCallback<List<E>> responder )
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );
    if( queryBuilder == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_FIELD( "queryBuilder" ) );

    BackendlessDataQuery dataQuery = queryBuilder.build();
    try
    {
      Object[] args = new Object[] { BackendlessSerializer.getSimpleName( entity ), dataQuery };
      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "find", args, responder, ResponderHelper.getCollectionAdaptingResponder( entity ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  protected <E> E first( final Class<E> entity ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "first", new Object[] { BackendlessSerializer.getSimpleName( entity ) }, ResponderHelper.getPOJOAdaptingResponder( entity ) );
  }

  protected <E> E first( final Class<E> entity, final List<String> relations,
                         final Integer relationsDepth ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "first", new Object[] { BackendlessSerializer.getSimpleName( entity ), relations, relationsDepth }, ResponderHelper.getPOJOAdaptingResponder( entity ) );
  }

  protected <E> void first( final Class<E> entity, final AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "first", new Object[] { BackendlessSerializer.getSimpleName( entity ) }, responder, ResponderHelper.getPOJOAdaptingResponder( entity ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  protected <E> void first( final Class<E> entity, final List<String> relations, final Integer relationsDepth,
                            final AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "first", new Object[] { BackendlessSerializer.getSimpleName( entity ), relations, relationsDepth }, responder, ResponderHelper.getPOJOAdaptingResponder( entity ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  protected <E> E last( final Class<E> entity ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "last", new Object[] { BackendlessSerializer.getSimpleName( entity ) }, ResponderHelper.getPOJOAdaptingResponder( entity ) );
  }

  protected <E> E last( final Class<E> entity, final List<String> relations,
                        final Integer relationsDepth ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "last", new Object[] { BackendlessSerializer.getSimpleName( entity ), relations, relationsDepth }, ResponderHelper.getPOJOAdaptingResponder( entity ) );
  }

  protected <E> void last( final Class<E> entity, final AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "last", new Object[] { BackendlessSerializer.getSimpleName( entity ) }, responder, ResponderHelper.getPOJOAdaptingResponder( entity ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  protected <E> void last( final Class<E> entity, final List<String> relations, final Integer relationsDepth,
                           final AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "last", new Object[] { BackendlessSerializer.getSimpleName( entity ), relations, relationsDepth }, responder, ResponderHelper.getPOJOAdaptingResponder( entity ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public IDataStore<Map> of( String tableName )
  {
    if( tableName.equalsIgnoreCase( "users" ) )
      throw new IllegalArgumentException( "Table 'Users' is not accessible through this signature. Use Backendless.Data.of( BackendlessUser.class ) instead" );

    return new MapDrivenDataStore( tableName );
  }

  public <E> IDataStore<E> of( final Class<E> entityClass )
  {
    if( entityClass == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    if( entityClass.getName().contains( "$" ) )
      throw new IllegalArgumentException( ExceptionMessage.INVALID_CLASS );

    try
    {
      Constructor defaultConstructor = entityClass.getConstructor();

      if( defaultConstructor == null || !Modifier.isPublic( defaultConstructor.getModifiers() ) )
        throw new IllegalArgumentException( ExceptionMessage.ENTITY_MISSING_DEFAULT_CONSTRUCTOR );
    }
    catch( NoSuchMethodException e )
    {
      throw new IllegalArgumentException( ExceptionMessage.ENTITY_MISSING_DEFAULT_CONSTRUCTOR );
    }

    return DataStoreFactory.createDataStore( entityClass );
  }

  public static String getEntityId( Object entity ) throws BackendlessException
  {
    String id = null;

    if( entity instanceof Map )
      return (String) ((Map) entity).get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    if( ReflectionUtil.hasField( entity.getClass(), Persistence.DEFAULT_OBJECT_ID_FIELD ) )
    {
      try
      {
        Field field = ReflectionUtil.getField( entity.getClass(), Persistence.DEFAULT_OBJECT_ID_FIELD );
        field.setAccessible( true );
        id = (String) field.get( entity );
      }
      catch( NoSuchFieldException | IllegalAccessException e )
      {
      }
    }
    else
    {
      try
      {
        Method declaredMethod = entity.getClass().getMethod( DEFAULT_OBJECT_ID_GETTER );

        if( !declaredMethod.isAccessible() )
          declaredMethod.setAccessible( true );

        id = (String) declaredMethod.invoke( entity );
      }
      catch( Exception e )
      {
        id = null;
      }
    }

    if( id == null )
      id = FootprintsManager.getInstance().getObjectId( entity );

    return id;
  }

  <E> int getObjectCount( final Class<E> entity )
  {
    Object[] args = new Object[] { BackendlessSerializer.getSimpleName( entity ) };
    return Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "count", args );
  }

  <E> int getObjectCount( final Class<E> entity, DataQueryBuilder queryBuilder )
  {
    if( queryBuilder == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_FIELD( "queryBuilder" ) );

    BackendlessDataQuery dataQuery = queryBuilder.build();
    Object[] args = new Object[] { BackendlessSerializer.getSimpleName( entity ), dataQuery };
    return Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "count", args );
  }

  <E> void getObjectCount( final Class<E> entity, AsyncCallback<Integer> responder )
  {
    try
    {
      Object[] args = new Object[] { BackendlessSerializer.getSimpleName( entity ) };
      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "count", args, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  <E> void getObjectCount( final Class<E> entity, DataQueryBuilder queryBuilder, AsyncCallback<Integer> responder )
  {
    if( queryBuilder == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_FIELD( "queryBuilder" ) );

    BackendlessDataQuery dataQuery = queryBuilder.build();
    try
    {
      Object[] args = new Object[] { BackendlessSerializer.getSimpleName( entity ), dataQuery };
      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "count", args, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  private <T> void checkDeclaredType( Class<T> entityClass )
  {
    if( entityClass.isArray() || entityClass.isAssignableFrom( Iterable.class ) || entityClass.isAssignableFrom( Map.class ) )
      throw new IllegalArgumentException( ExceptionMessage.WRONG_ENTITY_TYPE );

    try
    {
      Constructor[] constructors = entityClass.getConstructors();

      if( constructors.length > 0 )
        entityClass.getConstructor();
    }
    catch( NoSuchMethodException e )
    {
      throw new IllegalArgumentException( ExceptionMessage.ENTITY_MISSING_DEFAULT_CONSTRUCTOR );
    }
  }

  public List<Map<String, Object>> getView( String viewName, DataQueryBuilder queryBuilder )
  {
    if( queryBuilder == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_FIELD( "queryBuilder" ) );

    BackendlessDataQuery dataQuery = queryBuilder.build();
    Object[] args = new Object[] { viewName, dataQuery };
    return Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "callStoredView", args );
  }

  public void getView( String viewName, DataQueryBuilder queryBuilder, AsyncCallback<Map<String, Object>> responder )
  {
    if( queryBuilder == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_FIELD( "queryBuilder" ) );

    BackendlessDataQuery dataQuery = queryBuilder.build();

    Object[] args = new Object[] { viewName, dataQuery };
    Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "callStoredView", args, responder );
  }

  public List<Map> callStoredProcedure( String spName, Map<String, Object> arguments )
  {
    Object[] args = new Object[] { spName, arguments };

    return Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "callStoredProcedure", args );
  }

  public void callStoredProcedure( String procedureName, Map<String, Object> arguments, AsyncCallback<Map> responder )
  {
    Object[] args = new Object[] { procedureName, arguments };
    Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "callStoredProcedure", args, responder );
  }
}