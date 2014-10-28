package com.backendless;

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
  private static Set<Object> toBeCached = new HashSet<Object>(); //for cyclic entities
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
    void putMissingPropsToEntityMap( Object entity, Map entityMap )
    {
      if( !entityMap.containsKey( Footprint.OBJECT_ID_FIELD_NAME ) )
      {
        String objectId = getObjectId( entity );

        if( objectId != null )
          entityMap.put( Footprint.OBJECT_ID_FIELD_NAME, objectId );
      }

      if( !entityMap.containsKey( Footprint.META_FIELD_NAME ) )
      {
        String meta = getMeta( entity );

        if( meta != null )
          entityMap.put( Footprint.META_FIELD_NAME, meta );
      }

      for( Object key : entityMap.keySet() )
      {
        Object value = entityMap.get( key );

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
    }

    void duplicateFootprintForObject( Object newEntity, Object oldEntity )
    {
      Footprint footprint = persistenceCache.get( oldEntity );

      if( footprint != null )
        persistenceCache.put( newEntity, footprint );
    }

    void updateFootprintForObject( Object newEntity, Object oldEntity )
    {
      Footprint footprint = persistenceCache.get( newEntity );

      persistenceCache.put( oldEntity, footprint );
      removeFootprintForObject( newEntity );
    }

    void removeFootprintForObject( Object entity )
    {
      persistenceCache.remove( entity );
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
      toBeCached.add( entity );
      try
      {
        if( instance instanceof BackendlessCollection )
        {
          AnonymousObject typedObject = (AnonymousObject) ((NamedObject) entity).getTypedObject();
          ArrayType dataArray = (ArrayType) typedObject.getProperties().get( "data" );
          Object[] instances = ((BackendlessCollection) instance).getCurrentPage().toArray();
          if( !toBeCached.contains( dataArray ) )
            putEntityFootprintToCache( instances, dataArray );
        }
        else if( entity instanceof NamedObject )
        {
          if( !toBeCached.contains( ((NamedObject) entity).getTypedObject() ) )
            putEntityFootprintToCache( instance, ((NamedObject) entity).getTypedObject() );
        }
        else if( entity instanceof AnonymousObject )
        {
          if( !toBeCached.contains( ((AnonymousObject) entity).getProperties() ) )
            putEntityFootprintToCache( instance, ((AnonymousObject) entity).getProperties() );
        }
        else if( entity instanceof ArrayType )
        {
          Object[] entities = (Object[]) ((ArrayType) entity).getArray();
          Object[] arrayInstance = instance instanceof List ? ((List) instance).toArray() : (Object[]) instance;

          for( int i = 0; i < arrayInstance.length; i++ )
            if( !toBeCached.contains( entities[ i ] ) )
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
              if( !toBeCached.contains( entityEntry.getValue() ) )
                putEntityFootprintToCache( innerInstance, entityEntry.getValue() );
            }
          }

          Map<String, Object> cachedEntity = (Map<String, Object>) entity;
          persistenceCache.put( instance, Footprint.initFromEntity( cachedEntity ) );
          toBeCached.remove( cachedEntity );
        }
      }
      catch( Exception e )
      {/*Error in caching process should not fail application*/
      }
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