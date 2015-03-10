package com.backendless.persistence;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.FootprintsManager;
import com.backendless.Persistence;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.geo.GeoPoint;

import java.util.*;

/**
 * Handles object to Map serialization for Backendless services.
 */
public class BackendlessSerializer
{
  //used to serialize objects with cyclic relations
  static Map<Object, Map<String, Object>> serializedCache = new HashMap<Object, Map<String, Object>>();

  /**
   * Serializes Object to Map using WebOrb's serializer.
   *
   * @param entity object to be serialized
   * @return Map corresponding to given Object
   */
  public static Map<String, Object> serializeToMap( Object entity )
  {
    Map<String, Object> serializedEntity = new HashMap<String, Object>();

    if( entity.getClass().equals( BackendlessUser.class ) )
    {
      serializedEntity = ((BackendlessUser) entity).getProperties();
    }
    else
    {
      weborb.util.ObjectInspector.getObjectProperties( entity.getClass(), entity, (HashMap) serializedEntity, new ArrayList(), true, true );
    }

    serializedCache.put( entity, serializedEntity );

    FootprintsManager.getInstance().Inner.putMissingPropsToEntityMap( entity, serializedEntity );

    //put ___class field, otherwise server will not be able to detect class
    serializedEntity.put( Persistence.REST_CLASS_FIELD, Persistence.getSimpleName( entity.getClass() ) );

    //recursively serialize object properties
    Iterator<Map.Entry<String, Object>> entityIterator = serializedEntity.entrySet().iterator();
    while( entityIterator.hasNext() )
    {
      Map.Entry<String, Object> entityEntry = entityIterator.next();

      // ignore Parcelable CREATOR field on Android
      // http://developer.android.com/reference/android/os/Parcelable.html
      if( Backendless.isAndroid() && entityEntry.getKey().equals( Persistence.PARCELABLE_CREATOR_FIELD_NAME ) )
      {
        entityIterator.remove();
        continue;
      }

      Object entityEntryValue = entityEntry.getValue();

      // ignore null entries and GeoPoints
      if( entityEntryValue == null || entityEntryValue instanceof GeoPoint )
      {
        continue;
      }

      // check for anonymous class entry
      if( entityEntryValue.getClass().isAnonymousClass() )
      {
        throw new BackendlessException( String.format( ExceptionMessage.ANONYMOUS_CLASSES_PROHIBITED, entityEntry.getKey() ) );
      }

      //check if entity entry is collection
      if( entityEntryValue instanceof List )
      {
        List listEntry = (List) entityEntryValue;

        //do nothing with empty lists and lists of GeoPoints
        if( listEntry.isEmpty() || listEntry.iterator().next() instanceof GeoPoint )
        {
          continue;
        }

        // check for anonymous class entry
        if( listEntry.iterator().next().getClass().isAnonymousClass() )
        {
          throw new BackendlessException( String.format( ExceptionMessage.ANONYMOUS_CLASSES_PROHIBITED, entityEntry.getKey() ) );
        }

        List<Map<String, Object>> newCollection = new ArrayList<Map<String, Object>>();

        for( Object listEntryItem : listEntry )
        {
          if( !isBelongsJdk( listEntryItem.getClass() ) )
          {
            newCollection.add( getOrMakeSerializedObject( listEntryItem ) );
          }
        }

        entityEntry.setValue( newCollection );
      }
      else //not collection
      {
        if( !isBelongsJdk( entityEntryValue.getClass() ) )
        {
          entityEntry.setValue( getOrMakeSerializedObject( entityEntryValue ) );
        }
      }
    }

    serializedCache.remove( entity );

    return serializedEntity;
  }

  /**
   * Returns serialized object from cache or serializes object if it's not present in cache.
   *
   * @param entityEntryValue object to be serialized
   * @return Map formed from given object
   */
  private static Map<String, Object> getOrMakeSerializedObject( Object entityEntryValue )
  {
    if( serializedCache.containsKey( entityEntryValue ) ) //cyclic relation
    {
      //take from cache and substitute
      return serializedCache.get( entityEntryValue );
    }
    else //not cyclic relation
    {
      //serialize and put into result
      return serializeToMap( entityEntryValue );
    }
  }

  /**
   * Serializes entities inside BackendlessUser properties.
   *
   * @param user BackendlessUser whose properties need to be serialized
   */
  public static void serializeUserProperties( BackendlessUser user )
  {
    Map<String, Object> serializedProperties = user.getProperties();

    Set<Map.Entry<String, Object>> properties = serializedProperties.entrySet();
    for( Map.Entry<String, Object> property : properties )
    {
      if( !isBelongsJdk( property.getValue().getClass() ) )
      {
        property.setValue( serializeToMap( property.getValue() ) );
      }
    }

    user.setProperties( serializedProperties );
  }

  /**
   * Checks whether class is defined in JDK or it is user-defined class.
   * http://stackoverflow.com/questions/8703678/how-can-i-check-if-a-class-belongs-to-java-jdk
   *
   * @param clazz Class to be checked
   * @return true if this class is from JDK, false if class is user-defined
   */
  public static boolean isBelongsJdk( Class clazz )
  {
    return clazz.getClassLoader() == "".getClass().getClassLoader();
  }
}
