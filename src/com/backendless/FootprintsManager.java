package com.backendless;

import com.backendless.exceptions.BackendlessException;
import weborb.reader.AnonymousObject;
import weborb.reader.ArrayType;
import weborb.reader.NamedObject;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

  static FootprintsManager getInstance()
  {
    return instance;
  }

  public Footprint getEntityFootprint( Object entity )
  {
    return persistenceCache.get( entity );
  }

  public String getObjectId( Object entity )
  {
    if( persistenceCache.containsKey( entity ) )
      return getEntityFootprint( entity ).getObjectId();

    return null;
  }

  public String getMeta( Object entity )
  {
    if( persistenceCache.containsKey( entity ) )
      return getEntityFootprint( entity ).get__meta();

    return null;
  }

  public Date getCreated( Object entity )
  {
    if( persistenceCache.containsKey( entity ) )
      return getEntityFootprint( entity ).getCreated();

    return null;
  }

  public Date getUpdated( Object entity )
  {
    if( persistenceCache.containsKey( entity ) )
      return getEntityFootprint( entity ).getUpdated();

    return null;
  }

  public class Inner
  {
    /**
     * Puts missing properties like objectId, ___meta etc. into entity map.
     *
     * @param entity entity object
     * @param entityMap entity map
     */
    void putMissingPropsToEntityMap( Object entity, Map entityMap )
    {
      //to avoid endless recursion
      if( marked.contains( entity ) )
        return;
      else
        marked.add( entity );

      //put objectId if exists in cache
      if( !entityMap.containsKey( Footprint.OBJECT_ID_FIELD_NAME ) )
      {
        String objectId = getObjectId( entity );

        if( objectId != null )
          entityMap.put( Footprint.OBJECT_ID_FIELD_NAME, objectId );
      }

      //put __meta if exists in cache
      if( !entityMap.containsKey( Footprint.META_FIELD_NAME ) )
      {
        String meta = getMeta( entity );

        if( meta != null )
          entityMap.put( Footprint.META_FIELD_NAME, meta );
      }

      for( Object key : entityMap.keySet() )
      {
        Object value = entityMap.get( key );

        //recursively restore inner objects' properties
        if( value instanceof Map )
        {
          try
          {
            //get inner object
            //looks for getter method and invokes it
            Object entityField = new PropertyDescriptor( (String) key, entity.getClass() ).getReadMethod().invoke( entity );

            putMissingPropsToEntityMap( entityField, (Map) value );
          }
          catch( IllegalAccessException e )
          {
            throw new BackendlessException( e );
          }
          catch( InvocationTargetException e )
          {
            throw new BackendlessException( e );
          }
          catch( IntrospectionException e )
          {
            throw new BackendlessException( e );
          }
        }

        if( value instanceof Collection )
        {
          Collection coll = (Collection) value;
          ArrayList newCollection = new ArrayList();
          entityMap.put( key, newCollection );

          for( Object relatedEntity : coll )
          {
            Map map = Persistence.serializeToMap( relatedEntity );
            putMissingPropsToEntityMap( relatedEntity, map );
            map.put( "___class", relatedEntity.getClass().getCanonicalName() );
            newCollection.add( map );
          }
        }
      }

      marked.remove( entity );
    }

    /**
     * When the object is created on server, client gets new instance of it. In order to remember the system fields
     * (objectId, __meta etc.) it is required to duplicate the old instance in cache.
     *
     * @param serialized entity's map used to iterate through fields and duplicate footprints recursively
     * @param newEntity entity from server
     * @param oldEntity old entity (the one on which a method was called)
     */
    void duplicateFootprintForObject( Map serialized, Object newEntity, Object oldEntity )
    {
      //to avoid endless recursion
      if( marked.contains( newEntity ) )
        return;
      else
        marked.add( newEntity );

      //iterate through fields in the object and duplicate entities recursively
      Set<Map.Entry> entries = serialized.entrySet();
      for( Map.Entry entry : entries )
      {
        if( entry.getValue() instanceof Map )
        {
          try
          {
            //find read method for field and get object value
            Object newEntityField = new PropertyDescriptor( (String) entry.getKey(), newEntity.getClass() ).getReadMethod().invoke( newEntity );
            Object oldEntityField = new PropertyDescriptor( (String) entry.getKey(), oldEntity.getClass() ).getReadMethod().invoke( oldEntity );

            duplicateFootprintForObject( (Map) entry.getValue(), newEntityField, oldEntityField );
          }
          catch( IllegalAccessException e )
          {
            throw new BackendlessException( e );
          }
          catch( InvocationTargetException e )
          {
            throw new BackendlessException( e );
          }
          catch( IntrospectionException e )
          {
            throw new BackendlessException( e );
          }
        }
      }

      Footprint footprint = persistenceCache.get( oldEntity );

      if( footprint != null )
        persistenceCache.put( newEntity, footprint );

      marked.remove( newEntity );
    }

    /**
     * Updates footprint for entity.
     *
     * @param serialized entity's map used to iterate through fields and update footprints recursively
     * @param newEntity entity from server
     * @param oldEntity entity on which a method was called (.save(), .create() etc.)
     */
    void updateFootprintForObject( Map serialized, Object newEntity, Object oldEntity )
    {
      //to avoid endless recursion
      if( marked.contains( newEntity ) )
        return;
      else
        marked.add( newEntity );

      //update footprints recursively
      Set<Map.Entry> entries = serialized.entrySet();
      for( Map.Entry entry : entries )
      {
        if( entry.getValue() instanceof Map )
        {
          try
          {
            //find getter method and call it to get object property
            Object newEntityField = new PropertyDescriptor( (String) entry.getKey(), newEntity.getClass() ).getReadMethod().invoke( newEntity );
            Object oldEntityField = new PropertyDescriptor( (String) entry.getKey(), oldEntity.getClass() ).getReadMethod().invoke( oldEntity );

            updateFootprintForObject( (Map) entry.getValue(), newEntityField, oldEntityField );
          }
          catch( IllegalAccessException e )
          {
            throw new BackendlessException( e );
          }
          catch( InvocationTargetException e )
          {
            throw new BackendlessException( e );
          }
          catch( IntrospectionException e )
          {
            throw new BackendlessException( e );
          }
        }
      }

      Footprint footprint = persistenceCache.get( newEntity );

      persistenceCache.put( oldEntity, footprint );
      removeFootprintForObject( serialized, newEntity );

      marked.remove( newEntity );
    }

    /**
     * Removes entity's footprint from cache.
     *
     * @param serialized entity's map used to iterate through fields and remove footprints recursively
     * @param entity entity to be removed
     */
    void removeFootprintForObject( Map serialized, Object entity )
    {
      //to avoid endless recursion
      if( marked.contains( entity ) )
        return;
      else
        marked.add( entity );

      //iterate through object's properties and remove footprints recursively
      Set<Map.Entry> entries = serialized.entrySet();
      for( Map.Entry entry : entries )
      {
        if( entry.getValue() instanceof Map )
        {
          try
          {
            //find getter method and call it to retrieve object property
            Object entityField = new PropertyDescriptor( (String) entry.getKey(), entity.getClass() ).getReadMethod().invoke( entity );

            removeFootprintForObject( (Map) entry.getValue(), entityField );
          }
          catch( IllegalAccessException e )
          {
            throw new BackendlessException( e );
          }
          catch( InvocationTargetException e )
          {
            throw new BackendlessException( e );
          }
          catch( IntrospectionException e )
          {
            throw new BackendlessException( e );
          }
        }
      }

      persistenceCache.remove( entity );

      marked.remove( entity );
    }

    /**
     * Puts saved entity's footprint to cache.
     * Key - <code>instance</code>, value - footprint, initialized from <code>instance</code>.
     * Footprint is just an object, containing only fields
     * <code>objectId</code>, <code>created</code>, <code>updated</code>, <code>meta</code>.
     *
     * @param instance saved object received from server
     * @param entity IAdaptingType
     */
    public void putEntityFootprintToCache( Object instance, Object entity )
    {
      //to avoid endless recursion
      if( marked.contains( entity ) )
        return;
      else
        marked.add( entity );

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
            putEntityFootprintToCache( arrayInstance[ i ], entities[ i ] );
        }
        else
        {
          Map<String, Object> e = (Map<String, Object>) entity;
          for( Map.Entry<String, Object> entityEntry : e.entrySet() )
          {
            Object entityEntryValue = entityEntry.getValue();

            if( entityEntryValue instanceof NamedObject || entityEntryValue instanceof ArrayType )
            {
              Object innerInstance = getObjectFieldByName( instance, entityEntry.getKey() );
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

    private Object getObjectFieldByName( Object instance,
                                         String fieldName ) throws NoSuchFieldException, IllegalAccessException
    {
      Field field = instance.getClass().getDeclaredField( fieldName );
      field.setAccessible( true );

      return field.get( instance );
    }
  }
}