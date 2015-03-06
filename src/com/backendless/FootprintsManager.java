package com.backendless;

import com.backendless.exceptions.BackendlessException;
import weborb.reader.AnonymousObject;
import weborb.reader.ArrayType;
import weborb.reader.NamedObject;

import java.lang.reflect.Field;
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
     * @param entity    entity object
     * @param entityMap entity map
     */
    public void putMissingPropsToEntityMap( Object entity, Map entityMap )
    {
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
    }

    /**
     * When the object is created on server, client gets new instance of it. In order to remember the system fields
     * (objectId, __meta etc.) it is required to duplicate the old instance in cache.
     *
     * @param serialized entity's map used to iterate through fields and duplicate footprints recursively
     * @param newEntity  entity from server
     * @param oldEntity  entity on which a method was called (.save(), .create() etc.)
     */
    void duplicateFootprintForObject( Map serialized, Object newEntity, Object oldEntity )
    {
      //to avoid endless recursion
      if( marked.contains( newEntity ) )
        return;
      else
        marked.add( newEntity );

      try
      {
        //iterate through fields in the object and duplicate entities recursively
        Set<Map.Entry> entries = serialized.entrySet();
        for( Map.Entry entry : entries )
        {
          if( entry.getValue() instanceof Map )
          {
            //find read method for field and get object value
            Object newEntityField = newEntity.getClass().getField( (String) entry.getKey() ).get( newEntity );// new PropertyDescriptor( (String) entry.getKey(), newEntity.getClass() ).getReadMethod().invoke( newEntity );
            Object oldEntityField = oldEntity.getClass().getField( (String) entry.getKey() ).get( oldEntity );//new PropertyDescriptor( (String) entry.getKey(), oldEntity.getClass() ).getReadMethod().invoke( oldEntity );

            duplicateFootprintForObject( (Map) entry.getValue(), newEntityField, oldEntityField );
          }
          else if( entry.getValue() instanceof Collection )
          {
            Collection newObjectCollection = (Collection) newEntity.getClass().getField( (String) entry.getKey() ).get( newEntity );// new PropertyDescriptor( (String) entry.getKey(), newEntity.getClass() ).getReadMethod().invoke( newEntity );
            Collection oldObjectCollection = (Collection) oldEntity.getClass().getField( (String) entry.getKey() ).get( oldEntity );// new PropertyDescriptor( (String) entry.getKey(), oldEntity.getClass() ).getReadMethod().invoke( oldEntity );
            Collection mapCollection = (Collection) entry.getValue();

            Iterator newObjectCollectionIterator = newObjectCollection.iterator();
            Iterator oldObjectCollectionIterator = oldObjectCollection.iterator();
            Iterator mapCollectionIterator = mapCollection.iterator();
            while( oldObjectCollectionIterator.hasNext() )
            {
              duplicateFootprintForObject( (Map) mapCollectionIterator.next(), newObjectCollectionIterator.next(), oldObjectCollectionIterator.next() );
            }
          }
        }

        Footprint footprint = persistenceCache.get( oldEntity );

        if( footprint != null )
        {
          persistenceCache.put( newEntity, footprint );
        }
      }
      catch( IllegalAccessException e )
      {
        throw new BackendlessException( e );
      }
      catch( NoSuchFieldException e )
      {
        throw new BackendlessException( e );
      }
      finally
      {
        marked.remove( newEntity );
      }
    }

    /**
     * Updates footprint for entity.
     *
     * @param serialized entity's map used to iterate through fields and update footprints recursively
     * @param newEntity  entity from server
     * @param oldEntity  entity on which a method was called (.save(), .create() etc.)
     */
    void updateFootprintForObject( Map serialized, Object newEntity, Object oldEntity )
    {
      //to avoid endless recursion
      if( marked.contains( newEntity ) )
        return;
      else
        marked.add( newEntity );

      try
      {
        //update footprints recursively
        Set<Map.Entry> entries = serialized.entrySet();
        for( Map.Entry entry : entries )
        {
          String key = (String) entry.getKey();
          String upperKey = ((String) entry.getKey()).substring( 0, 1 ).toUpperCase().concat( ((String) entry.getKey()).substring( 1 ) );

          if( entry.getValue() instanceof Map )
          {
            //find getter method and call it to get object property
            Object newEntityField;
            try
            {
              Field declaredField = newEntity.getClass().getDeclaredField( key );
              declaredField.setAccessible( true );
              newEntityField = declaredField.get( newEntity );//new PropertyDescriptor( (String) entry.getKey(), newEntity.getClass() ).getReadMethod().invoke( newEntity );
            }
            catch( NoSuchFieldException nfe )
            {
              //try to find field with first letter in uppercase
              Field declaredField = newEntity.getClass().getDeclaredField( upperKey );
              declaredField.setAccessible( true );
              newEntityField = declaredField.get( newEntity );
            }

            Object oldEntityField;
            try
            {
              Field declaredField = oldEntity.getClass().getDeclaredField( key );
              declaredField.setAccessible( true );
              oldEntityField = declaredField.get( oldEntity );//new PropertyDescriptor( (String) entry.getKey(), oldEntity.getClass() ).getReadMethod().invoke( oldEntity );
            }
            catch( NoSuchFieldException nfe )
            {
              //try to find field with first letter in uppercase
              Field declaredField = oldEntity.getClass().getDeclaredField( upperKey );
              declaredField.setAccessible( true );
              oldEntityField = declaredField.get( oldEntity );
            }

            updateFootprintForObject( (Map) entry.getValue(), newEntityField, oldEntityField );
          }
          else if( entry.getValue() instanceof Collection )
          {
            Collection newObjectCollection;
            try
            {
              Field declaredField = newEntity.getClass().getDeclaredField( key );
              declaredField.setAccessible( true );
              newObjectCollection = (Collection) declaredField.get( newEntity );//new PropertyDescriptor( (String) entry.getKey(), newEntity.getClass() ).getReadMethod().invoke( newEntity );
            }
            catch( NoSuchFieldException nfe )
            {
              //try to find field with first letter in uppercase
              Field declaredField = newEntity.getClass().getDeclaredField( upperKey );
              declaredField.setAccessible( true );
              newObjectCollection = (Collection) declaredField.get( newEntity );
            }

            Collection oldObjectCollection;
            try
            {
              Field declaredField = oldEntity.getClass().getDeclaredField( key );
              declaredField.setAccessible( true );
              oldObjectCollection = (Collection) declaredField.get( oldEntity );//new PropertyDescriptor( (String) entry.getKey(), oldEntity.getClass() ).getReadMethod().invoke( oldEntity );
            }
            catch( NoSuchFieldException nfe )
            {
              //try to find field with first letter in uppercase
              Field declaredField = oldEntity.getClass().getDeclaredField( upperKey );
              declaredField.setAccessible( true );
              oldObjectCollection = (Collection) declaredField.get( oldEntity );
            }

            Collection mapCollection = (Collection) entry.getValue();

            Iterator mapCollectionIterator = mapCollection.iterator();
            Iterator oldObjectCollectionIterator = oldObjectCollection.iterator();
            Iterator newObjectCollectionIterator = newObjectCollection.iterator();
            while( oldObjectCollectionIterator.hasNext() )
            {
              updateFootprintForObject( (Map) mapCollectionIterator.next(), newObjectCollectionIterator.next(), oldObjectCollectionIterator.next() );
            }
          }
        }

        Footprint footprint = persistenceCache.get( newEntity );

        persistenceCache.put( oldEntity, footprint );
        removeFootprintForObject( serialized, newEntity );
      }
      catch( IllegalAccessException e )
      {
        throw new BackendlessException( e );
      }
      catch( NoSuchFieldException e )
      {
        throw new BackendlessException( e );
      }
      finally
      {
        marked.remove( newEntity );
      }
    }

    /**
     * Removes entity's footprint from cache.
     *
     * @param serialized entity's map used to iterate through fields and remove footprints recursively
     * @param entity     entity to be removed
     */
    void removeFootprintForObject( Map serialized, Object entity )
    {
      //to avoid endless recursion
      if( marked.contains( entity ) )
        return;
      else
        marked.add( entity );

      try
      {
        //iterate through object's properties and remove footprints recursively
        Set<Map.Entry> entries = serialized.entrySet();
        for( Map.Entry entry : entries )
        {
          if( entry.getValue() instanceof Map )
          {
            //find getter method and call it to retrieve object property
            Object entityField = entity.getClass().getField( (String) entry.getKey() ).get( entity );//new PropertyDescriptor( (String) entry.getKey(), entity.getClass() ).getReadMethod().invoke( entity );
            removeFootprintForObject( (Map) entry.getValue(), entityField );
          }
          else if( entry.getValue() instanceof Collection )
          {
            Collection objectCollection = (Collection) entity.getClass().getField( (String) entry.getKey() ).get( entity );//new PropertyDescriptor( (String) entry.getKey(), entity.getClass() ).getReadMethod().invoke( entity );
            Collection mapCollection = (Collection) entry.getValue();

            Iterator objectCollectionIterator = objectCollection.iterator();
            Iterator mapCollectionIterator = mapCollection.iterator();
            while( objectCollectionIterator.hasNext() )
            {
              removeFootprintForObject( (Map) mapCollectionIterator.next(), objectCollectionIterator.next() );
            }
          }
        }

        persistenceCache.remove( entity );
      }
      catch( IllegalAccessException e )
      {
        throw new BackendlessException( e );
      }
      catch( NoSuchFieldException e )
      {
        throw new BackendlessException( e );
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