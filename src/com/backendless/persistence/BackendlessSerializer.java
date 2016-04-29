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

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.FootprintsManager;
import com.backendless.Persistence;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.geo.GeoPoint;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Handles object to Map serialization for Backendless services.
 */
public abstract class BackendlessSerializer
{
  private static HashMap<Class, IObjectSerializer> serializers = new HashMap<Class, IObjectSerializer>();
  private static IObjectSerializer DEFAULT_SERIALIZER = new DefaultSerializer();

  public abstract boolean shouldTraverse();

  public static void addSerializer( Class clazz, IObjectSerializer serializer )
  {
    serializers.put( clazz, serializer );
  }

  /**
   * Serializes Object to Map using WebOrb's serializer.
   *
   * @param entity object to be serialized
   * @return Map corresponding to given Object
   */
  public static Map<String, Object> serializeToMap( Object entity )
  {
    IObjectSerializer serializer = getSerializer( entity.getClass() );
    return (Map<String, Object>) serializer.serializeToMap( entity, new HashMap<Object, Map<String, Object>>() );
  }

  /**
   * Uses pluggable serializers to locate one for the class and get the name which should be used for serialization.
   * The name must match the table name where instance of clazz are persisted
   *
   * @param clazz
   * @return Backendless-friendly class/table name
   */
  public static String getSimpleName( Class clazz )
  {
    IObjectSerializer serializer = getSerializer( clazz );
    return serializer.getClassName( clazz );
  }

  public static Class getClassForDeserialization( Class clazz )
  {
    IObjectSerializer serializer = getSerializer( clazz );
    return serializer.getSerializationFriendlyClass( clazz );
  }

  public Object serializeToMap( Object entity, Map<Object, Map<String, Object>> serializedCache )
  {
    if( entity.getClass().isArray() )
      return serializeArray( entity, serializedCache );

    if( entity.getClass().isEnum() )
      return ((Enum) entity).name();

    Map<String, Object> serializedEntity = new HashMap<String, Object>();

    if( entity.getClass() == BackendlessUser.class )
      serializedEntity = ((BackendlessUser) entity).getProperties();
    else
      weborb.util.ObjectInspector.getObjectProperties( entity.getClass(), entity, (HashMap) serializedEntity, new ArrayList(), true, shouldTraverse() );

    serializedCache.put( entity, serializedEntity );

    FootprintsManager.getInstance().Inner.putMissingPropsToEntityMap( entity, serializedEntity );

    //put ___class field, otherwise server will not be able to detect class
    serializedEntity.put( Persistence.REST_CLASS_FIELD, getSimpleName( entity.getClass() ) );

    //recursively serialize object properties
    Iterator<Map.Entry<String, Object>> entityIterator = serializedEntity.entrySet().iterator();
    while( entityIterator.hasNext() )
    {
      Map.Entry<String, Object> entityEntry = entityIterator.next();

      // ignoring properties which contain $. This occurs in InstantRun in AndroidStudio - it injects $change property.
      if( entityEntry.getKey().contains( "$" ) )
      {
        entityIterator.remove();
        continue;
      }

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
        continue;

      // check for anonymous class entry
      if( entityEntryValue.getClass().isAnonymousClass() )
        throw new BackendlessException( String.format( ExceptionMessage.ANONYMOUS_CLASSES_PROHIBITED, entityEntry.getKey() ) );

      //check if entity entry is collection
      if( entityEntryValue instanceof List )
      {
        List listEntry = (List) entityEntryValue;

        // empty lists should not be sent to the server
        if( listEntry.isEmpty() )
        {
          // if there is no object id, remove empty list
          if( !serializedEntity.containsKey( Persistence.DEFAULT_OBJECT_ID_FIELD  ) ||
               serializedEntity.get( Persistence.DEFAULT_OBJECT_ID_FIELD ) == null )
            entityIterator.remove();

          continue;
        }

        //do nothing with lists of GeoPoints
        if( listEntry.iterator().next() instanceof GeoPoint )
          continue;

        // check for anonymous class entry
        if( listEntry.iterator().next().getClass().isAnonymousClass() )
          throw new BackendlessException( String.format( ExceptionMessage.ANONYMOUS_CLASSES_PROHIBITED, entityEntry.getKey() ) );

        List<Object> newCollection = new ArrayList<Object>();

        for( Object listEntryItem : listEntry )
          if( !isBelongsJdk( listEntryItem.getClass() ) )
            newCollection.add( getOrMakeSerializedObject( listEntryItem, serializedCache ) );
          else
            newCollection.add( listEntryItem );

        entityEntry.setValue( newCollection );
      }
      else if( entityEntryValue instanceof Object[] )
      {
        Object[] arrayEntry = (Object[]) entityEntryValue;

        //do nothing with empty arrays and arrays of GeoPoints
        if( arrayEntry.length == 0 || arrayEntry[ 0 ] instanceof GeoPoint )
          continue;

        // check for anonymous class entry
        if( arrayEntry[ 0 ].getClass().isAnonymousClass() )
          throw new BackendlessException( String.format( ExceptionMessage.ANONYMOUS_CLASSES_PROHIBITED, entityEntry.getKey() ) );

        List<Object> newCollection = new ArrayList<Object>();
        for( Object arrayEntryItem : arrayEntry )
          if( !isBelongsJdk( arrayEntryItem.getClass() ) )
            newCollection.add( getOrMakeSerializedObject( arrayEntryItem, serializedCache ) );
          else
            newCollection.add( arrayEntryItem );

        entityEntry.setValue( newCollection );
      }
      else //not collection
      {
        if( !isBelongsJdk( entityEntryValue.getClass() ) )
          entityEntry.setValue( getOrMakeSerializedObject( entityEntryValue, serializedCache ) );
      }
    }

    return serializedEntity;
  }

  private Object serializeArray( Object entity, Map<Object, Map<String, Object>> serializedCache )
  {
    int length = Array.getLength( entity );
    Object[] objects = new Object[ length ];
    for( int i = 0; i < length; i++ )
    {
      objects[ i ] = getOrMakeSerializedObject( Array.get( entity, i ), serializedCache );
    }

    return objects;
  }

  /**
   * Returns serialized object from cache or serializes object if it's not present in cache.
   *
   * @param entityEntryValue object to be serialized
   * @return Map formed from given object
   */
  private Object getOrMakeSerializedObject( Object entityEntryValue,
                                                   Map<Object, Map<String, Object>> serializedCache )
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
      if( propertyValue != null && !propertyValue.getClass().isArray() && !propertyValue.getClass().isEnum() && !isBelongsJdk( propertyValue.getClass() ) )
      {
        property.setValue( serializeToMap( propertyValue ) );
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

  /**
   * Returns a serializer for the class
   * @param clazz
   * @return
   */
  private static IObjectSerializer getSerializer( Class clazz )
  {
    Iterator<Map.Entry<Class, IObjectSerializer>> iterator = serializers.entrySet().iterator();
    IObjectSerializer serializer = DEFAULT_SERIALIZER;

    while( iterator.hasNext() )
    {
      Map.Entry<Class, IObjectSerializer> entry = iterator.next();

      if( entry.getKey().isAssignableFrom( clazz ) )
      {
        serializer = entry.getValue();
        break;
      }
    }

    return serializer;
  }
}
