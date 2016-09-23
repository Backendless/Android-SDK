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
import com.backendless.persistence.MapDrivenDataStore;
import com.backendless.persistence.QueryOptions;
import com.backendless.property.ObjectProperty;
import com.backendless.utils.JSONObjectConverter;
import com.backendless.utils.ReflectionUtil;
import com.backendless.utils.ResponderHelper;
import weborb.client.IChainedResponder;
import weborb.types.Types;
import weborb.writer.IObjectSubstitutor;
import weborb.writer.MessageWriter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public final class Persistence
{
  private final static String PERSISTENCE_MANAGER_SERVER_ALIAS = "com.backendless.services.persistence.PersistenceService";
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
    Types.addClientClassMapping( "com.backendless.services.persistence.BackendlessCollection", BackendlessCollection.class );
    Types.addClientClassMapping( "com.backendless.services.persistence.ObjectProperty", ObjectProperty.class );
    Types.addClientClassMapping( "com.backendless.services.persistence.QueryOptions", QueryOptions.class );
  }

  public void mapTableToClass( String tableName, Class clazz )
 {
   weborb.types.Types.addClientClassMapping( tableName, clazz );
 }

  public <E> E save( final E entity ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    checkDeclaredType( entity.getClass() );
    final Map<String, Object> serializedEntity = BackendlessSerializer.serializeToMap( entity );
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

   // FootprintsManager.getInstance().Inner.putMissingPropsToEntityMap( entity, serializedEntity );

    try
    {
      String method = "create";

      if( serializedEntity.containsKey( Persistence.DEFAULT_OBJECT_ID_FIELD ) &&
              serializedEntity.get( Persistence.DEFAULT_OBJECT_ID_FIELD ) != null )
        method = "update";

      E newEntity = Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, method,
              new Object[] {
                Backendless.getApplicationId(),
                Backendless.getVersion(),
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

     // FootprintsManager.getInstance().Inner.putMissingPropsToEntityMap( entity, serializedEntity );

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

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, method, new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( entity.getClass() ), entity }, callbackOverrider, ResponderHelper.getPOJOAdaptingResponder( entity.getClass() ) );
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

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "create", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( aClass ), entity }, ResponderHelper.getPOJOAdaptingResponder( aClass ) );
  }

  public <E> void create( final Class<E> aClass, final Map entity, final AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "create", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( aClass ), entity }, responder, ResponderHelper.getPOJOAdaptingResponder( aClass ) );
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

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "update", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( aClass ), entity }, ResponderHelper.getPOJOAdaptingResponder( aClass ) );
  }

  private <E> void update( final Class<E> aClass, final Map entity, final AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "update", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( aClass ), entity }, responder, ResponderHelper.getPOJOAdaptingResponder( aClass ) );
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

    Object result = Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "remove", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( entity.getClass() ), entityMap } );

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

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "remove", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( entity.getClass() ), entityMap }, removalCallback );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  protected <E> E findById( final Class<E> entity, final String id,
                            final List<String> relations ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY_NAME );

    if( id == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ID );

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( entity ), id, relations }, ResponderHelper.getPOJOAdaptingResponder( entity ) );
  }

  protected <E> E findById( final Class<E> entity, final String id, final List<String> relations,
                            final int relationsDepth ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY_NAME );

    if( id == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ID );

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( entity ), id, relations, relationsDepth }, ResponderHelper.getPOJOAdaptingResponder( entity ) );
  }

  protected <E> E findById( final E entity, List<String> relations, int relationsDepth )
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    Object entityArg =  ReflectionUtil.hasField( entity.getClass(), Persistence.DEFAULT_OBJECT_ID_FIELD ) ? entity :FootprintsManager.getInstance().getObjectId( entity );

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( entity.getClass() ), entityArg, relations, relationsDepth }, ResponderHelper.getPOJOAdaptingResponder( entity.getClass() ) );
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

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( entity ), id, relations }, responder, ResponderHelper.getPOJOAdaptingResponder( entity ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  protected <E> void findById( final Class<E> entity, final String id, final List<String> relations,
                               final int relationsDepth, AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      if( id == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ID );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( entity ), id, relations, relationsDepth }, responder, ResponderHelper.getPOJOAdaptingResponder( entity ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  protected <E> void findById( E entity, List<String> relations, int relationsDepth, AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      IChainedResponder chainedResponder = new AdaptingResponder<E>( (Class<E>) entity.getClass(), new PoJoAdaptingPolicy<E>() );

      Object entityArg = ReflectionUtil.hasField( entity.getClass(), Persistence.DEFAULT_OBJECT_ID_FIELD ) ? entity : FootprintsManager.getInstance().getObjectId( entity );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( entity.getClass() ), entityArg, relations, relationsDepth }, responder, chainedResponder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  protected <E> void loadRelations( final E entity, final List<String> relations ) throws BackendlessException
  {
    if( entity == null )
    {
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );
    }

    checkDeclaredType( entity.getClass() );
    final Map<String, Object> serializedEntity = BackendlessSerializer.serializeToMap( entity );
    MessageWriter.setObjectSubstitutor( new IObjectSubstitutor()
    {
      @Override
      public Object substitute( Object o )
      {
        if( o == entity )
        {
          return serializedEntity;
        }
        else
        {
          return o;
        }
      }
    } );

    Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( entity.getClass() ), serializedEntity, relations };
    IChainedResponder chainedResponder = new AdaptingResponder<E>( (Class<E>) entity.getClass(), new PoJoAdaptingPolicy<E>() );
    E loadedRelations = Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "loadRelations", args, chainedResponder );
    loadRelationsToEntity( entity, loadedRelations, relations );
  }

  protected <E> void loadRelations( final E entity, final List<String> relations, final AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      checkDeclaredType( entity.getClass() );
      final Map<String, Object> serializedEntity = BackendlessSerializer.serializeToMap( entity );

      MessageWriter.setObjectSubstitutor( new IObjectSubstitutor()
      {
        @Override
        public Object substitute( Object o )
        {
          if( o == entity )
          {
            return serializedEntity;
          }
          else
          {
            return o;
          }
        }
      } );

      Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( entity.getClass() ), serializedEntity, relations };
      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "loadRelations", args, new AsyncCallback<E>()
      {
        @Override
        public void handleResponse( E loadedRelations )
        {
          try
          {
            loadRelationsToEntity( entity, loadedRelations, relations );

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
      }, new AdaptingResponder<E>( (Class<E>) entity.getClass(), new PoJoAdaptingPolicy<E>() ) );
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
        if( declaredField.getName().equals( DEFAULT_META_FIELD ) )
        {
          mergeRelatedObjectsFromMeta( loadedRelations, entity );
          continue;
        }

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

  private <E> void mergeRelatedObjectsFromMeta( E loadedRelations, E entity )
  {
    try
    {
      Field metaField = loadedRelations.getClass().getDeclaredField(  DEFAULT_META_FIELD );
      metaField.setAccessible( true );
      String updatedMeta = (String) metaField.get( loadedRelations );
      String sourceMeta = (String) metaField.get( entity );

      HashMap sourceJSONObject = JSONObjectConverter.getJSONObject( sourceMeta );
      HashMap updatedJSONObject = JSONObjectConverter.getJSONObject( updatedMeta );

      HashMap sourceRelatedObjects = (HashMap) sourceJSONObject.get( "relatedObjects" );
      HashMap updatedRelatedObjects = (HashMap) updatedJSONObject.get( "relatedObjects" );

      if( sourceRelatedObjects == null || sourceRelatedObjects.size() == 0 )
      {
        sourceJSONObject.put( "relatedObjects", updatedRelatedObjects );
      }
      else
      {
        String[] updatedMenuItems = (String[]) updatedRelatedObjects.get( "menuItems" );
        sourceRelatedObjects.put( "menuItems", updatedMenuItems );
        sourceJSONObject.put( "relatedObjects", sourceRelatedObjects );
      }

      sourceMeta = JSONObjectConverter.toJSONString( sourceJSONObject );
      metaField.set( entity, sourceMeta );
      /*
      JSONObject updatedMetaObject = new JSONObject( updatedMeta );
      JSONObject sourceMetaObject = new JSONObject( sourceMeta );
      JSONObject updatedRelatedObjects = updatedMetaObject.optJSONObject( "relatedObjects" );
      JSONObject sourceRelatedObjects = sourceMetaObject.optJSONObject( "relatedObjects" );

      if( sourceRelatedObjects == null || sourceRelatedObjects.length() == 0 )
      {
        sourceMetaObject.put( "relatedObjects", updatedRelatedObjects );
      }
      else
      {
        Iterator<String> updatedRelatedObjectsIterator = updatedRelatedObjects.keys();

        while( updatedRelatedObjectsIterator.hasNext() )
        {
          String relatedProperty = updatedRelatedObjectsIterator.next();
          sourceRelatedObjects.put( relatedProperty, updatedRelatedObjects.get( relatedProperty ) );
        }

        sourceMetaObject.put( "relatedObjects", sourceRelatedObjects );
      }

      metaField.set( entity, sourceMetaObject.toString() );
      */
    }
    catch( NoSuchFieldException e )
    {
      // should not happen
    }
    catch( IllegalAccessException e )
    {
      // should not happen
    }
  }

  public List<ObjectProperty> describe( String classSimpleName ) throws BackendlessException
  {
    if( classSimpleName == null || classSimpleName.equals( "" ) )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY_NAME );

    ObjectProperty[] response = Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "describe", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), classSimpleName } );

    return Arrays.asList( response );
  }

  public void describe( String classSimpleName, final AsyncCallback<List<ObjectProperty>> responder )
  {
    try
    {
      if( classSimpleName == null || classSimpleName.equals( "" ) )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY_NAME );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "describe", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), classSimpleName }, new AsyncCallback<ObjectProperty[]>()
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

  public <E> BackendlessCollection<E> find( Class<E> entity,
                                               BackendlessDataQuery dataQuery ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    checkPageSizeAndOffset( dataQuery );

    Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( entity ), dataQuery };
    BackendlessCollection<E> result = Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "find", args, ResponderHelper.getCollectionAdaptingResponder( entity ) );
    result.setQuery( dataQuery );
    result.setType( entity );

    return result;
  }

  public <E> void find( final Class<E> entity, final BackendlessDataQuery dataQuery,
                           final AsyncCallback<BackendlessCollection<E>> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      checkPageSizeAndOffset( dataQuery );

      AsyncCallback<BackendlessCollection<E>> callback = new AsyncCallback<BackendlessCollection<E>>()
      {
        @Override
        public void handleResponse( BackendlessCollection<E> response )
        {
          if( responder != null )
          {
            response.setQuery( dataQuery );
            response.setType( entity );
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

      Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( entity ), dataQuery };
      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "find", args, callback, ResponderHelper.getCollectionAdaptingResponder( entity ) );
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

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "first", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( entity ) }, ResponderHelper.getPOJOAdaptingResponder( entity ) );
  }

  protected <E> E first( final Class<E> entity, final List<String> relations,
                         final int relationsDepth ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "first", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( entity ), relations, relationsDepth }, ResponderHelper.getPOJOAdaptingResponder( entity ) );
  }

  protected <E> void first( final Class<E> entity, final AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "first", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( entity ) }, responder, ResponderHelper.getPOJOAdaptingResponder( entity ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  protected <E> void first( final Class<E> entity, final List<String> relations, final int relationsDepth,
                            final AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "first", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( entity ), relations, relationsDepth }, responder, ResponderHelper.getPOJOAdaptingResponder( entity ) );
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

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "last", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( entity ) }, ResponderHelper.getPOJOAdaptingResponder( entity ) );
  }

  protected <E> E last( final Class<E> entity, final List<String> relations,
                        final int relationsDepth ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "last", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( entity ), relations, relationsDepth }, ResponderHelper.getPOJOAdaptingResponder( entity ) );
  }

  protected <E> void last( final Class<E> entity, final AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "last", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( entity ) }, responder, ResponderHelper.getPOJOAdaptingResponder( entity ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  protected <E> void last( final Class<E> entity, final List<String> relations, final int relationsDepth,
                           final AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "last", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), BackendlessSerializer.getSimpleName( entity ), relations, relationsDepth }, responder, ResponderHelper.getPOJOAdaptingResponder( entity ) );
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

    return new MapDrivenDataStore( tableName  );
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
        throw new IllegalArgumentException(  ExceptionMessage.ENTITY_MISSING_DEFAULT_CONSTRUCTOR );
    }
    catch( NoSuchMethodException e )
    {
      throw new IllegalArgumentException(  ExceptionMessage.ENTITY_MISSING_DEFAULT_CONSTRUCTOR );
    }

    return DataStoreFactory.createDataStore( entityClass );
  }

  static String getEntityId( Object entity ) throws BackendlessException
  {
    String id;

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

    if( id == null )
      id = FootprintsManager.getInstance().getObjectId( entity );

    return id;
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

  public static void checkPageSizeAndOffset( BackendlessDataQuery dataQuery ) throws BackendlessException
  {
    if( dataQuery != null )
    {
      if( dataQuery.getOffset() < 0 )
        throw new IllegalArgumentException( ExceptionMessage.WRONG_OFFSET );

      if( dataQuery.getPageSize() < 0 )
        throw new IllegalArgumentException( ExceptionMessage.WRONG_PAGE_SIZE );
    }
  }

  /*
  public static String getSimpleName( Class clazz )
  {
    if( clazz == BackendlessUser.class )
    {
      return UserService.USERS_TABLE_NAME;
    }
    else
    {
      String mappedName = weborb.types.Types.getMappedClientClass( clazz.getName() );

      if( mappedName != null )
        return mappedName;
      else
        return clazz.getSimpleName();
    }
  }
  */

  public BackendlessCollection<Map<String, Object>> getView( String viewName, BackendlessDataQuery dataQuery )
  {
    checkPageSizeAndOffset( dataQuery );

    Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), viewName, dataQuery };
    return Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "callStoredView", args );
  }

  public void getView( String viewName, BackendlessDataQuery query, AsyncCallback<Map<String, Object>> responder )
  {
    checkPageSizeAndOffset( query );

    Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), viewName, query };
    Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "callStoredView", args, responder );
  }


  public BackendlessCollection<Map> callStoredProcedure( String spName, Map<String, Object> arguments )
  {
    Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), spName, arguments };

    return Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "callStoredProcedure", args );
  }

  public void callStoredProcedure( String procedureName, Map<String, Object> arguments, AsyncCallback<Map> responder )
  {
    Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), procedureName, arguments };
    Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "callStoredProcedure", args, responder );
  }
}