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

import android.util.Log;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.FootprintsManager;
import com.backendless.Persistence;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.geo.GeoPoint;
import weborb.util.io.ISerializer;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Handles object to Map serialization for Backendless services.
 */
public class BackendlessSerializer
{

  private static final String TAG = "BackendlessSerializer";

  /**
   * Serializes Object to Map using WebOrb's serializer.
   *
   * @param entity object to be serialized
   * @return Map corresponding to given Object
   */
  public static Map<String, Object> serializeToMap( Object entity )
  {
    return (Map<String, Object>) serializeToMap( entity, new HashMap<Object, Map<String, Object>>(  ) );
  }


  private static Object serializeToMap( Object entity,  Map<Object, Map<String, Object>> serializedCache )
  {
    if(entity.getClass().isArray())
    {
      return serializeArray( entity, serializedCache );
    }

    if( entity.getClass().isEnum() )
    {
      return ((Enum) entity).name();
    }

    Map<String, Object> serializedEntity = new HashMap<String, Object>();

    if( entity.getClass() == BackendlessUser.class )
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

        List<Object> newCollection = new ArrayList<Object>();

        for( Object listEntryItem : listEntry )
        {
          if( !isBelongsJdk( listEntryItem.getClass() ) )
          {
            newCollection.add( getOrMakeSerializedObject( listEntryItem, serializedCache ) );
          }
        }

        entityEntry.setValue( newCollection );
      }
      else //not collection
      {
        if( !isBelongsJdk( entityEntryValue.getClass() ) )
        {
          entityEntry.setValue( getOrMakeSerializedObject( entityEntryValue, serializedCache ) );
        }
      }
    }

    return serializedEntity;
  }

  private static Object serializeArray( Object entity, Map<Object, Map<String, Object>> serializedCache )
  {
    int length = Array.getLength( entity );
    Object[] objects = new Object[length];
    for( int i = 0; i < length; i++ )
    {
      objects[i] = getOrMakeSerializedObject( Array.get( entity, i ), serializedCache );
    }

    return objects;
  }

  /**
   * Returns serialized object from cache or serializes object if it's not present in cache.
   *
   * @param entityEntryValue object to be serialized
   * @return Map formed from given object
   */
  private static Object getOrMakeSerializedObject( Object entityEntryValue,  Map<Object, Map<String, Object>> serializedCache )
  {
    if( serializedCache.containsKey( entityEntryValue ) ) //cyclic relation
    {
      //take from cache and substitute
      return serializedCache.get( entityEntryValue );
    }
    else //not cyclic relation
    {
      //serialize and put into result
      return serializeToMap( entityEntryValue, serializedCache );
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
      Object propertyValue = property.getValue();
      if( propertyValue != null && !isBelongsJdk( propertyValue.getClass() ) )
      {
        property.setValue( serializeToMap( propertyValue, new HashMap<Object, Map<String, Object>>() ) );
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

  public static <T> T deserializeAMF(byte[] arr)
  {
    return deserializeAMF( arr, true );
  }

  public static <T> T deserializeAMF( byte[] arr, boolean adapt )
  {
    Object object = null;

    if( arr == null )
      return null;

    try
    {
      object = weborb.util.io.Serializer.fromBytes( arr, ISerializer.AMF3, !adapt );
    }
    catch( Exception e )
    {
      Log.e( TAG, "Could not deserialize message", e );
    }

    return (T) object;
  }

  public static byte[] serializeAMF( Object object )
  {
    byte[] arr = null;

    try
    {
      arr = weborb.util.io.Serializer.toBytes( object, ISerializer.AMF3 );
    }
    catch( Exception e )
    {
      Log.e( TAG, "Could not serialize message", e );
    }

    return arr;
  }
}
