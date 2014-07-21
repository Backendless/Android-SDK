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
import com.backendless.persistence.QueryOptions;
import com.backendless.property.ObjectProperty;
import com.backendless.utils.ResponderHelper;
import weborb.types.Types;
import weborb.writer.IObjectSubstitutor;
import weborb.writer.MessageWriter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public final class Persistence
{
  private final static String PERSISTENCE_MANAGER_SERVER_ALIAS = "com.backendless.services.persistence.PersistenceService";
  private final static String DEFAULT_OBJECT_ID_GETTER = "getObjectId";
  public final static String DEFAULT_OBJECT_ID_FIELD = "objectId";
  public final static String DEFAULT_CREATED_FIELD = "created";
  public final static String DEFAULT_UPDATED_FIELD = "updated";
  public final static String DEFAULT_META_FIELD = "__meta";

  public final static String LOAD_ALL_RELATIONS = "*";
  public final static DataPermission Permissions = new DataPermission();

  private static final Persistence instance = new Persistence();
  private static final Class backendlessUserClass = BackendlessUser.class;

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
    final Map serializedEntity = serializeToMap( entity );
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

    FootprintsManager.getInstance().Inner.putMissingPropsToEntityMap( entity, serializedEntity );

    try
    {
      E newEntity;
      if( serializedEntity.get( Footprint.OBJECT_ID_FIELD_NAME ) == null )
      {
        newEntity = (E) create( entity.getClass(), serializedEntity );
        FootprintsManager.getInstance().Inner.duplicateFootprintForObject( entity, newEntity );
      }
      else
      {
        newEntity = (E) update( entity.getClass(), serializedEntity );
        FootprintsManager.getInstance().Inner.updateFootprintForObject( newEntity, entity );
      }

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
      final Map serializedEntity = serializeToMap( entity );

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

      FootprintsManager.getInstance().Inner.putMissingPropsToEntityMap( entity, serializedEntity );

      if( serializedEntity.get( Footprint.OBJECT_ID_FIELD_NAME ) == null )
        create( (Class<E>) entity.getClass(), serializedEntity, new AsyncCallback<E>()
        {
          @Override
          public void handleResponse( E newEntity )
          {
            MessageWriter.setObjectSubstitutor( null );
            FootprintsManager.getInstance().Inner.duplicateFootprintForObject( entity, newEntity );

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
        } );
      else
        update( (Class<E>) entity.getClass(), serializedEntity, new AsyncCallback<E>()
        {
          @Override
          public void handleResponse( E newEntity )
          {
            FootprintsManager.getInstance().Inner.updateFootprintForObject( newEntity, entity );

            if( responder != null )
              responder.handleResponse( newEntity );
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

  private <E> E create( Class<E> aClass, Map entity ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "create", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), getSimpleName( aClass ), entity }, ResponderHelper.getPOJOAdaptingResponder( aClass ) );
  }

  private <E> void create( final Class<E> aClass, final Map entity, final AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "create", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), getSimpleName( aClass ), entity }, responder, ResponderHelper.getPOJOAdaptingResponder( aClass ) );
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

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "update", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), getSimpleName( aClass ), entity }, ResponderHelper.getPOJOAdaptingResponder( aClass ) );
  }

  private <E> void update( final Class<E> aClass, final Map entity, final AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "update", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), getSimpleName( aClass ), entity }, responder, ResponderHelper.getPOJOAdaptingResponder( aClass ) );
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

    String id = getEntityId( entity );

    if( id == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ID );

    Object result = Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "remove", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), getSimpleName( entity.getClass() ), id } );
    FootprintsManager.getInstance().Inner.removeFootprintForObject( entity );

    return ((Number) result).longValue();
  }

  protected <E> void remove( final E entity, final AsyncCallback<Long> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      String id = getEntityId( entity );

      if( id == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ID );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "remove", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), getSimpleName( entity.getClass() ), id }, new AsyncCallback<Object>()
      {
        @Override
        public void handleResponse( Object response )
        {
          FootprintsManager.getInstance().Inner.removeFootprintForObject( entity );

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
      } );
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

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), getSimpleName( entity ), id, relations }, ResponderHelper.getPOJOAdaptingResponder( entity ) );
  }

  protected <E> E findById( final Class<E> entity, final String id, final List<String> relations,
                            final int relationsDepth ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY_NAME );

    if( id == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ID );

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), entity.getSimpleName(), id, relations, relationsDepth }, ResponderHelper.getPOJOAdaptingResponder( entity ) );
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

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), getSimpleName( entity ), id, relations }, responder, ResponderHelper.getPOJOAdaptingResponder( entity ) );
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

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "findById", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), entity.getSimpleName(), id, relations, relationsDepth }, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  protected <E> void loadRelations( final E entity, final List<String> relations ) throws Exception
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY_NAME );

    String id = getEntityId( entity );

    if( id == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ID );

    E loadedRelations = (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "loadRelations", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), getSimpleName( entity.getClass() ), id, relations }, new AdaptingResponder<E>( (Class<E>) entity.getClass(), new PoJoAdaptingPolicy<E>() ) );
    loadRelationsToEntity( entity, loadedRelations, relations );
  }

  protected <E> void loadRelations( final E entity, final List<String> relations, final AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      String id = getEntityId( entity );

      if( id == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ID );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "loadRelations", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), getSimpleName( entity.getClass() ), id, relations }, new AsyncCallback<E>()
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

  private <E> void loadRelationsToEntity( E entity, E loadedRelations,
                                          List<String> relations ) throws IllegalAccessException
  {
    if( entity.getClass().equals( backendlessUserClass ) )
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

        declaredField.set( entity, declaredField.get( loadedRelations ) );
      }
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

  protected <E> BackendlessCollection<E> find( Class<E> entity,
                                               BackendlessDataQuery dataQuery ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    checkPageSizeAndOffset( dataQuery );

    Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), getSimpleName( entity ), dataQuery };
    BackendlessCollection<E> result = (BackendlessCollection<E>) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "find", args, ResponderHelper.getCollectionAdaptingResponder( entity ) );
    result.setQuery( dataQuery );
    result.setType( entity );

    return result;
  }

  protected <E> void find( final Class<E> entity, final BackendlessDataQuery dataQuery,
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

      Object[] args = new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), getSimpleName( entity ), dataQuery };
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

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "first", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), getSimpleName( entity ) }, ResponderHelper.getPOJOAdaptingResponder( entity ) );
  }

  protected <E> E first( final Class<E> entity, final List<String> relations,
                         final int relationsDepth ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "first", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), entity.getSimpleName(), relations, relationsDepth }, ResponderHelper.getPOJOAdaptingResponder( entity ) );
  }

  protected <E> void first( final Class<E> entity, final AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "first", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), getSimpleName( entity ) }, responder, ResponderHelper.getPOJOAdaptingResponder( entity ) );
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

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "first", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), entity.getSimpleName(), relations, relationsDepth }, responder, ResponderHelper.getPOJOAdaptingResponder( entity ) );
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

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "last", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), getSimpleName( entity ) }, ResponderHelper.getPOJOAdaptingResponder( entity ) );
  }

  protected <E> E last( final Class<E> entity, final List<String> relations,
                        final int relationsDepth ) throws BackendlessException
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    return (E) Invoker.invokeSync( PERSISTENCE_MANAGER_SERVER_ALIAS, "last", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), entity.getSimpleName(), relations, relationsDepth }, ResponderHelper.getPOJOAdaptingResponder( entity ) );
  }

  protected <E> void last( final Class<E> entity, final AsyncCallback<E> responder )
  {
    try
    {
      if( entity == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "last", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), getSimpleName( entity ) }, responder, ResponderHelper.getPOJOAdaptingResponder( entity ) );
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

      Invoker.invokeAsync( PERSISTENCE_MANAGER_SERVER_ALIAS, "last", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), entity.getSimpleName(), relations, relationsDepth }, responder, ResponderHelper.getPOJOAdaptingResponder( entity ) );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public <E> IDataStore<E> of( final Class<E> entityClass )
  {
    if( entityClass == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    return DataStoreFactory.createDataStore( entityClass );
  }

  /*private Map<String,Object> getEntityMapWithFootprint( Object entity )
  {

  }*/

  static String getEntityId( Object entity ) throws BackendlessException
  {
    String id;

    try
    {
      Method declaredMethod = entity.getClass().getMethod( DEFAULT_OBJECT_ID_GETTER, new Class[ 0 ] );

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
         entityClass.getConstructor( new Class[ 0 ] );
    }
    catch( NoSuchMethodException e )
    {
      throw new IllegalArgumentException( ExceptionMessage.ENTITY_MISSING_DEFAULT_CONSTRUCTOR );
    }
  }

  private void checkPageSizeAndOffset( BackendlessDataQuery dataQuery ) throws BackendlessException
  {
    if( dataQuery != null )
    {
      if( dataQuery.getOffset() < 0 )
        throw new IllegalArgumentException( ExceptionMessage.WRONG_OFFSET );

      if( dataQuery.getPageSize() < 0 )
        throw new IllegalArgumentException( ExceptionMessage.WRONG_PAGE_SIZE );
    }
  }

  static <T> Map serializeToMap( T entity )
  {
    if( entity.getClass().equals( backendlessUserClass ) )
      return ((BackendlessUser) entity).getProperties();

    HashMap result = new HashMap();
    weborb.util.ObjectInspector.getObjectProperties( entity.getClass(), entity, result, new ArrayList(), true, true );

    return result;
  }

  private String getSimpleName( Class clazz )
  {
    if( clazz.equals( backendlessUserClass ) )
      return "Users";
    else
      return clazz.getSimpleName();
  }
}