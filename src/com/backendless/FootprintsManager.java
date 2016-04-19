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

import com.backendless.geo.GeoPoint;
import com.backendless.utils.ReflectionUtil;
import weborb.reader.AnonymousObject;
import weborb.reader.ArrayType;
import weborb.reader.NamedObject;

import java.util.*;

/**
 * If server sends object <code>object</code> with fields <code>a</code>, <code>b</code>, <code>c</code>
 * and the client's class declares only fields <code>a</code>, <code>b</code>, then this class saves
 * field <code>c</code> in memory in order to send it with the next user's request.
 */
public class FootprintsManager
{
  private static final FootprintsManager instance = new FootprintsManager();
  private static Set<Object> marked = new HashSet<Object>(); //for cyclic entities
  public final Inner Inner = new Inner();
  private final Map<Object, Footprint> persistenceCache = new WeakHashMap<Object, Footprint>();

  private FootprintsManager()
  {
  }

  public static FootprintsManager getInstance()
  {
    return instance;
  }

  public Footprint getEntityFootprint( Object entity )
  {
    return persistenceCache.get( entity );
  }

  public String getObjectId( Object entity )
  {
    Footprint footprint = getEntityFootprint( entity );

    if( footprint != null )
      return footprint.getObjectId();
    else if( entity instanceof BackendlessUser )
      return ((BackendlessUser) entity).getObjectId();

    return null;
  }

  public String getMeta( Object entity )
  {
    if( persistenceCache.containsKey( entity ) )
    {
      return getEntityFootprint( entity ).get__meta();
    }

    return null;
  }

  public Date getCreated( Object entity )
  {
    if( persistenceCache.containsKey( entity ) )
    {
      return getEntityFootprint( entity ).getCreated();
    }

    return null;
  }

  public Date getUpdated( Object entity )
  {
    if( persistenceCache.containsKey( entity ) )
    {
      return getEntityFootprint( entity ).getUpdated();
    }

    return null;
  }

  public class Inner
  {
    /**
     * Puts missing properties like objectId, ___meta etc. into entity map.
     *
     * @param entity    entity object
     * @param entityMap entity map
     */
    public void putMissingPropsToEntityMap( Object entity, Map entityMap )
    {
      //put objectId if exists in cache
      if( !entityMap.containsKey( Persistence.DEFAULT_OBJECT_ID_FIELD ) )
      {
        String objectId = getObjectId( entity );

        if( objectId != null )
        {
          entityMap.put( Persistence.DEFAULT_OBJECT_ID_FIELD, objectId );
        }
      }

      //put __meta if exists in cache
      if( !entityMap.containsKey( Persistence.DEFAULT_META_FIELD ) )
      {
        String meta = getMeta( entity );

        if( meta != null )
        {
          entityMap.put( Persistence.DEFAULT_META_FIELD, meta );
        }
      }
    }

    /**
     * When the object is created on server, client gets new instance of it. In order to remember the system fields
     * (objectId, __meta etc.) it is required to duplicate the old instance in cache.
     *
     * @param serializedEntity entity's map used to iterate through fields and duplicate footprints recursively
     * @param persistedEntity  entity from server
     * @param initialEntity    entity on which a method was called (.save(), .create() etc.)
     */
    void duplicateFootprintForObject( Map<String, Object> serializedEntity, Object persistedEntity,
                                      Object initialEntity )
    {
      //to avoid endless recursion
      if( marked.contains( persistedEntity ) )
      {
        return;
      }
      else
      {
        marked.add( persistedEntity );
      }

      try
      {
        //iterate through fields in the object and duplicate entities recursively
        Set<Map.Entry<String, Object>> entries = serializedEntity.entrySet();
        for( Map.Entry<String, Object> entry : entries )
        {
          if( entry.getValue() instanceof Map )
          {
            // retrieve persisted entity's field value
            Object persistedEntityFieldValue = getFieldValue( persistedEntity, entry.getKey() );

            // retrieve initial entity's field value
            Object initialEntityFieldValue = getFieldValue( initialEntity, entry.getKey() );

            // duplicate footprint recursively
            duplicateFootprintForObject((Map<String, Object>) entry.getValue(), persistedEntityFieldValue, initialEntityFieldValue);
          }
          else if( entry.getValue() instanceof Collection )
          {
            // TODO: discuss and decide what to do with GeoPoints here
            Collection collection = (Collection) entry.getValue();

            if( collection.size() > 0 && collection.iterator().next() instanceof GeoPoint )
              continue;

            // retrieve persisted entity's field value (which is collection)
            Collection persistedEntityFieldValue = getFieldValueAsCollection(persistedEntity, entry.getKey());
            // retrieve initial entity's field value (which is collection)
            Collection initialEntityFieldValue = getFieldValueAsCollection(initialEntity, entry.getKey());

            Collection mapCollection = (Collection) entry.getValue();

            // recursively duplicate footprint for each object in collection
            Iterator persistedEntityFieldValueIterator = persistedEntityFieldValue.iterator();
            Iterator initialEntityFieldValueIterator = initialEntityFieldValue.iterator();
            Iterator mapCollectionIterator = mapCollection.iterator();
            while( initialEntityFieldValueIterator.hasNext() )
            {
              duplicateFootprintForObject( (Map) mapCollectionIterator.next(), persistedEntityFieldValueIterator.next(), initialEntityFieldValueIterator.next() );
            }
          }
        }

        Footprint footprint = persistenceCache.get( persistedEntity );

        if( footprint != null )
        {
          persistenceCache.put( initialEntity, footprint );
        }
      }
      finally
      {
        marked.remove( persistedEntity );
      }
    }

    /**
     * Recursively sets Footprint objects from OLD entities to NEW ones.
     *
     * @param serialized entity's map used to iterate through fields and update footprints recursively
     * @param newEntity  entity from server
     * @param oldEntity  entity on which a method was called (.save(), .create() etc.)
     */
    void updateFootprintForObject( Map<String, Object> serialized, Object newEntity, Object oldEntity )
    {
      //to avoid endless recursion
      if( marked.contains( newEntity ) )
      {
        return;
      }
      else
      {
        marked.add( newEntity );
      }

      try
      {
        //update footprints recursively
        Set<Map.Entry<String, Object>> entries = serialized.entrySet();
        for( Map.Entry<String, Object> entry : entries )
        {
          String key = entry.getKey();

          if( entry.getValue() instanceof Map )
          {
            Object newEntityField = getFieldValue( newEntity, key );
            Object oldEntityField = getFieldValue( oldEntity, key );
            updateFootprintForObject((Map) entry.getValue(), newEntityField, oldEntityField);
          }
          else if( entry.getValue() instanceof Collection )
          {
            Collection valueCollection = (Collection) entry.getValue();
            Iterator valueIterator = valueCollection.iterator();

            if( valueIterator.hasNext() && valueIterator.next() instanceof GeoPoint )
              continue;

            Collection newObjectCollection= getFieldValueAsCollection( newEntity, key );
            Collection oldObjectCollection = getFieldValueAsCollection( oldEntity, key );
            Collection mapCollection = (Collection) entry.getValue();

            Iterator mapCollectionIterator = mapCollection.iterator();
            Iterator oldObjectCollectionIterator = oldObjectCollection.iterator();
            Iterator newObjectCollectionIterator = newObjectCollection.iterator();

            while( oldObjectCollectionIterator.hasNext() )
              updateFootprintForObject((Map) mapCollectionIterator.next(), newObjectCollectionIterator.next(), oldObjectCollectionIterator.next());
          }
        }

        Footprint footprint = persistenceCache.get( newEntity );
        persistenceCache.put(oldEntity, footprint);
      }
      finally
      {
        marked.remove( newEntity );
      }
    }

    /**
     * Removes entity's footprint from cache.
     *
     * @param serializedEntity entity's map used to iterate through fields and remove footprints recursively
     * @param entity           entity to be removed
     */
    void removeFootprintForObject( Map<String, Object> serializedEntity, Object entity )
    {
      //to avoid endless recursion
      if( marked.contains( entity ) )
      {
        return;
      }
      else
      {
        marked.add( entity );
      }

      try
      {
        //iterate through object's properties and remove footprints recursively
        Set<Map.Entry<String, Object>> entries = serializedEntity.entrySet();

        for( Map.Entry<String, Object> entry : entries )
        {
          String key = entry.getKey();

          if( entry.getValue() instanceof Map )
          {
            Object entityFieldValue = getFieldValue( entity, key );

            // remove footprints recursively
            removeFootprintForObject( (Map<String, Object>) entry.getValue(), entityFieldValue );
          }
          else if( entry.getValue() instanceof Collection )
          {
            Collection objectCollection = getFieldValueAsCollection( entity, key );
            Collection mapCollection = (Collection) entry.getValue();

            // remove footprints recursively for each object in collection
            Iterator objectCollectionIterator = objectCollection.iterator();
            Iterator mapCollectionIterator = mapCollection.iterator();

            while( objectCollectionIterator.hasNext() )
              removeFootprintForObject( (Map<String, Object>) mapCollectionIterator.next(), objectCollectionIterator.next() );
          }
        }

        persistenceCache.remove( entity );
      }
      finally
      {
        marked.remove( entity );
      }
    }

    /**
     * Puts saved entity's footprint to cache.
     * Key - <code>instance</code>, value - footprint, initialized from <code>instance</code>.
     * Footprint is just an object, containing only fields
     * <code>objectId</code>, <code>created</code>, <code>updated</code>, <code>meta</code>.
     *
     * @param instance saved object received from server
     * @param entity   IAdaptingType
     */
    public void putEntityFootprintToCache( Object instance, Object entity )
    {
      //to avoid endless recursion
      if( marked.contains( entity ) )
      {
        return;
      }
      else
      {
        marked.add( entity );
      }

      try
      {
        if( instance instanceof BackendlessCollection )
        {
          AnonymousObject typedObject = (AnonymousObject) ((NamedObject) entity).getTypedObject();
          ArrayType dataArray = (ArrayType) typedObject.getProperties().get( "data" );
          Object[] instances = ((BackendlessCollection) instance).getCurrentPage().toArray();
          putEntityFootprintToCache( instances, dataArray );
        }
        else if( entity instanceof NamedObject )
        {
          putEntityFootprintToCache( instance, ((NamedObject) entity).getTypedObject() );
        }
        else if( entity instanceof AnonymousObject )
        {
          putEntityFootprintToCache( instance, ((AnonymousObject) entity).getProperties() );
        }
        else if( entity instanceof ArrayType )
        {
          Object[] entities = (Object[]) ((ArrayType) entity).getArray();
          Object[] arrayInstance = instance instanceof List ? ((List) instance).toArray() : (Object[]) instance;

          for( int i = 0; i < arrayInstance.length; i++ )
          {
            putEntityFootprintToCache( arrayInstance[ i ], entities[ i ] );
          }
        }
        else
        {
          Map<String, Object> e = (Map<String, Object>) entity;
          for( Map.Entry<String, Object> entityEntry : e.entrySet() )
          {
            Object entityEntryValue = entityEntry.getValue();

            if( entityEntryValue instanceof NamedObject || entityEntryValue instanceof ArrayType )
            {
              Object innerInstance = getFieldValue( instance, entityEntry.getKey() );
              putEntityFootprintToCache( innerInstance, entityEntry.getValue() );
            }
          }

          Map<String, Object> cachedEntity = (Map<String, Object>) entity;
          persistenceCache.put( instance, Footprint.initFromEntity( cachedEntity ) );
        }
      }
      catch( Exception e )
      {/*Error in caching process should not fail application*/
      }

      marked.remove( entity );
    }

    /**
     * Retrieves value for 'fieldName' field from the 'entity' object in a form of Collection.
     *
     * @param entity    object to retrieve 'key' field from
     * @param fieldName field name to retrieve from 'entity' object
     */
    private Collection getFieldValueAsCollection( Object entity, String fieldName )
    {
      Object rawEntity = getFieldValue( entity, fieldName );

      if( rawEntity == null )
        return new ArrayList();
      else if( rawEntity instanceof Collection )
        return (Collection) rawEntity;
      else if( rawEntity.getClass().isArray() )
        return Arrays.asList( (Object[]) rawEntity );
      else
        throw new RuntimeException( "unknown data type - " + rawEntity.getClass() );
    }

    /**
     * Retrieves value for 'fieldName' field from the 'entity' object without casting it to any type.
     * Use #getFieldValueAsCollection to get result in a form of Collection.
     *
     * @param entity    object to retrieve 'fieldName' field from
     * @param fieldName field name to retrieve from 'entity' object
     */
    private Object getFieldValue( Object entity, String fieldName )
    {
      Object entityFieldValue;
      String firstLetter = fieldName.substring( 0, 1 );
      String remainder = fieldName.substring( 1 );
      String lowerKey = firstLetter.toLowerCase().concat( remainder );
      String upperKey = firstLetter.toUpperCase().concat( remainder );

      if( entity instanceof BackendlessUser )
      {
        entityFieldValue = ((BackendlessUser) entity).getProperty( lowerKey );

        if( entityFieldValue == null )
          entityFieldValue = ((BackendlessUser) entity).getProperty( upperKey );
      }
      else
      {
          // retrieve entity field value
          entityFieldValue = ReflectionUtil.getFieldValue( entity, lowerKey, upperKey );
      }
      return entityFieldValue;
    }
  }
}