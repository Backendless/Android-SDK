package com.backendless;

import weborb.reader.AnonymousObject;
import weborb.reader.ArrayType;
import weborb.reader.NamedObject;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class FootprintsManager
{
  private static final FootprintsManager instance = new FootprintsManager();
  private final Map<Object, Footprint> persistenceCache = new WeakHashMap<Object, Footprint>();
  public final Inner Inner = new Inner();

  static FootprintsManager getInstance()
  {
    return instance;
  }

  private FootprintsManager()
  {}

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

    public void putEntityFootprintToCache( Object instance, Object entity )
    {
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
          putEntityFootprintToCache( instance, ((NamedObject) entity).getTypedObject() );
        else if( entity instanceof AnonymousObject )
          putEntityFootprintToCache( instance, ((AnonymousObject) entity).getProperties() );
        else if( entity instanceof ArrayType )
        {
          Object[] entities = (Object[]) ((ArrayType) entity).getArray();
          Object[] arrayInstance = instance.getClass().isAssignableFrom( List.class ) ? ((List) instance).toArray() : (Object[]) instance;

          for( int i = 0; i < ((Object[]) instance).length; i++ )
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

          persistenceCache.put( instance, Footprint.initFromEntity( (Map<String, Object>) entity ) );
        }
      }
      catch( Exception e )
      {/*Error in caching process should not fail application*/}
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