package com.backendless.hive;

import com.backendless.utils.WeborbSerializationHelper;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


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

  static Map<String, String> serializeAsMap( Map<String, ?> mapOfObjects )
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

  static <T> Map<String, T> deserialize( Map<String, String> mapOfJsonStrings )
  {
    HashMap<String, T> result = new HashMap<>();

    for( Map.Entry<String, String> entry : mapOfJsonStrings.entrySet() )
      result.put( entry.getKey(), deserialize( entry.getValue() ) );

    return result;
  }

  static List<String> serializeAsList( List<?> listOfObjects )
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

  static <T> List<T> deserialize( List<String> listOfJsonStrings )
  {
    ArrayList<T> result = new ArrayList<>();

    for( String listOfJsonString : listOfJsonStrings )
      result.add( deserialize( listOfJsonString ) );

    return result;
  }

  static <T> Set<T> deserialize( Set<String> listOfJsonStrings )
  {
    Set<T> result = new HashSet<>();

    for( String listOfJsonString : listOfJsonStrings )
      result.add( deserialize( listOfJsonString ) );

    return result;
  }
}
