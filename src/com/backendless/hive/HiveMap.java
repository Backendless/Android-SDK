package com.backendless.hive;

import com.backendless.core.responder.AdaptingResponder;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public final class HiveMap<T> extends HiveGeneralForComplexStore
{
  public final static String HIVE_MAP_ALIAS = "com.backendless.services.hive.HiveMapService";

  HiveMap( String hiveName, String storeKey )
  {
    super( hiveName, StoreType.Map, storeKey );
  }


  public CompletableFuture<Map<String, T>> get()
  {
    return this.<Map<String, String>>makeRemoteCall( "get" ).thenApply( HiveSerializer::deserialize );
  }

  public CompletableFuture<T> get( String objKey )
  {
    return this.<String>makeRemoteCall( "get", objKey ).thenApply( HiveSerializer::deserialize );
  }

  public CompletableFuture<Map<String, T>> get( List<String> objKeys )
  {
    return this.<Map<String, String>>makeRemoteCall( "multiGet", objKeys ).thenApply( HiveSerializer::deserialize );
  }

  public CompletableFuture<List<String>> keys()
  {
    return makeRemoteCall( "getKeys" );
  }

  public CompletableFuture<List<T>> values()
  {
    return this.<List<String>>makeRemoteCall( "getValues" ).thenApply( HiveSerializer::deserialize );
  }

  public CompletableFuture<Long> set( Map<String, T> values )
  {
    return makeRemoteCall( "set", new AdaptingResponder<>( Long.class ), HiveSerializer.serialize( values ) );
  }

  public CompletableFuture<Boolean> set( String objKey, String value )
  {
    return makeRemoteCall( "set", objKey, HiveSerializer.serialize( value ) );
  }

  public CompletableFuture<Boolean> setWithOverwrite( String objKey, String value, boolean overwrite )
  {
    if( overwrite )
      return set( objKey, value );
    else
      return makeRemoteCall( "setIfNotExist", objKey, HiveSerializer.serialize( value ) );
  }

  public CompletableFuture<Long> increment( String objKey, Integer value )
  {
    return makeRemoteCall( "incrementBy", new AdaptingResponder<>( Long.class ), objKey, value );
  }

  public CompletableFuture<Long> decrement( String objKey, Integer value )
  {
    return increment( objKey, -value );
  }

  public CompletableFuture<Boolean> keyExists( String objKey )
  {
    return makeRemoteCall( "exists", objKey );
  }

  public CompletableFuture<Long> length()
  {
    return makeRemoteCall( "length", new AdaptingResponder<>( Long.class ) );
  }

  public CompletableFuture<Long> delete( String objKey )
  {
    return delete( Collections.singletonList( objKey ) );
  }

  public CompletableFuture<Long> delete( List<String> objKeys )
  {
    return makeRemoteCall( "del", new AdaptingResponder<>( Long.class ), objKeys );
  }

  // ----------------------------------------

  private <T> CompletableFuture<T> makeRemoteCall( String methodName, Object... args )
  {
    return makeRemoteCallWithStoreKey( HIVE_MAP_ALIAS, methodName, args );
  }

  private <T> CompletableFuture<T> makeRemoteCall( String methodName, AdaptingResponder<T> adaptingResponder, Object... args )
  {
    return makeRemoteCallWithStoreKey( HIVE_MAP_ALIAS, methodName, adaptingResponder, args );
  }
}
