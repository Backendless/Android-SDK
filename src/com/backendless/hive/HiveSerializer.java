package com.backendless.hive;

import com.backendless.utils.WeborbSerializationHelper;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;


final class HiveSerializer
{
  static String serialize( Object obj )
  {
    return new String( WeborbSerializationHelper.serialize( obj ), StandardCharsets.UTF_8 );
  }

  static <T> T deserialize( String jsonString )
  {
    return (T) WeborbSerializationHelper.deserialize( jsonString.getBytes( StandardCharsets.UTF_8 ) );
  }

  static HashMap<String, String> serializeAsMap( Map<String, ?> mapOfObjects )
  {
    HashMap<String, String> result = new HashMap<>();

    for( Map.Entry<String, ?> entry : mapOfObjects.entrySet() )
    {
      if( entry.getValue() == null )
        throw new IllegalArgumentException( "null value for key '" + entry.getKey() + "'" );

      result.put( entry.getKey(), serialize( entry.getValue() ) );
    }

    return result;
  }

  static <T> HashMap<String, T> deserialize( Map<String, String> mapOfJsonStrings )
  {
    HashMap<String, T> result = new HashMap<>();

    for( Map.Entry<String, String> entry : mapOfJsonStrings.entrySet() )
      result.put( entry.getKey(), deserialize( entry.getValue() ) );

    return result;
  }

  static ArrayList<String> serializeAsList( List<?> listOfObjects )
  {
    ArrayList<String> result = new ArrayList<>();

    for( int i = 0; i < listOfObjects.size(); i++ )
    {
      Object obj = listOfObjects.get( i );

      if( obj == null )
        throw new IllegalArgumentException( "null value on index '" + i + "'" );

      result.add( serialize( obj ) );
    }

    return result;
  }

  static <T> ArrayList<T> deserializeAsList( String[] arrayOfJsonStrings )
  {
    ArrayList<T> result = new ArrayList<>();

    for( String listOfJsonString : arrayOfJsonStrings )
      result.add( deserialize( listOfJsonString ) );

    return result;
  }

  static <T> HashSet<T> deserializeAsSet( String[] listOfJsonStrings )
  {
    HashSet<T> result = new HashSet<>();

    for( String listOfJsonString : listOfJsonStrings )
      result.add( deserialize( listOfJsonString ) );

    return result;
  }

  static <T> LinkedHashSet<T> deserializeAsLinkedSet( String[] listOfJsonStrings )
  {
    LinkedHashSet<T> result = new LinkedHashSet<>();

    for( String listOfJsonString : listOfJsonStrings )
      result.add( deserialize( listOfJsonString ) );

    return result;
  }
}
