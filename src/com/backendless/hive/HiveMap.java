package com.backendless.hive;

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

  public CompletableFuture<Map<String, T>> multiGet( List<String> objKeys )
  {
    return this.<Map<String, String>>makeRemoteCall( "multiGet", objKeys ).thenApply( HiveSerializer::deserialize );
  }

  public CompletableFuture<List<String>> getKeys()
  {
    return makeRemoteCall( "getKeys" );
  }

  public CompletableFuture<List<T>> getValues()
  {
    return this.<List<String>>makeRemoteCall( "getValues" ).thenApply( HiveSerializer::deserialize );
  }

  public CompletableFuture<Long> set( Map<String, T> values )
  {
    return makeRemoteCall( "set", HiveSerializer.serialize( values ) );
  }

  public CompletableFuture<Boolean> set( String objKey, String value )
  {
    return makeRemoteCall( "set", objKey, HiveSerializer.serialize( value ) );
  }

  public CompletableFuture<Boolean> setIfNotExist( String objKey, String value )
  {
    return makeRemoteCall( "setIfNotExist", objKey, HiveSerializer.serialize( value ) );
  }

  public CompletableFuture<Long> incrementBy( String objKey, Integer value )
  {
    return makeRemoteCall( "incrementBy", objKey, value );
  }

  public CompletableFuture<Boolean> exists( String objKey )
  {
    return makeRemoteCall( "exists", objKey );
  }

  public CompletableFuture<Long> length()
  {
    return makeRemoteCall( "length" );
  }

  public CompletableFuture<Long> del( List<String> objKeys )
  {
    return makeRemoteCall( "del", objKeys );
  }

  // ----------------------------------------

  private <T> CompletableFuture<T> makeRemoteCall( String methodName, Object... args )
  {
    return makeRemoteCallWithStoreKey( HIVE_MAP_ALIAS, methodName, args );
  }
}
