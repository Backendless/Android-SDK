package com.backendless.hive;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public final class HiveList<T> extends HiveGeneralForComplexStore
{
  public final static String HIVE_LIST_ALIAS = "com.backendless.services.hive.HiveListService";

  public HiveList( String hiveName, String storeKey, HiveManagement hiveManagement )
  {
    super( hiveName, StoreType.List, storeKey, hiveManagement );
  }

  public CompletableFuture<List<T>> get()
  {
    return this.<List<String>>makeRemoteCall( "get" ).thenApply( jsonValues -> (List<T>) HiveSerializer.deserialize( jsonValues ) );
  }

  public CompletableFuture<List<T>> get( int start, int stop )
  {
    return this.<List<String>>makeRemoteCall( "get", start, stop ).thenApply( jsonValues -> (List<T>) HiveSerializer.deserialize( jsonValues ) );
  }

  public CompletableFuture<T> get( int index )
  {
    return this.<String>makeRemoteCall( "get", index ).thenApply( jsonValue -> (T) HiveSerializer.deserialize( jsonValue ) );
  }

  public CompletableFuture<Long> set( List<T> values )
  {
    return makeRemoteCall( "set", HiveSerializer.serializeAsList( values ) );
  }

  public CompletableFuture<Void> set( int index, Object value )
  {
    return makeRemoteCall( "set", index, HiveSerializer.serialize( value ) );
  }

  public CompletableFuture<Long> insert( Object targetValue, Object value, boolean before )
  {
    return makeRemoteCall( "insert", HiveSerializer.serialize( targetValue ), HiveSerializer.serialize( value ), before );
  }

  public CompletableFuture<Long> addFirst( List<T> values )
  {
    return makeRemoteCall( "addFirst", HiveSerializer.serializeAsList( values ) );
  }

  public CompletableFuture<Long> addLast( List<T> values )
  {
    return makeRemoteCall( "addLast", HiveSerializer.serializeAsList( values ) );
  }

  public CompletableFuture<T> removeAndReturnFirst()
  {
    return this.<String>makeRemoteCall( "removeAndReturnFirst" ).thenApply( jsonValue -> (T) HiveSerializer.deserialize( jsonValue ) );
  }

  public CompletableFuture<T> removeAndReturnLast()
  {
    return this.<String>makeRemoteCall( "removeAndReturnLast" ).thenApply( jsonValue -> (T) HiveSerializer.deserialize( jsonValue ) );
  }

  public CompletableFuture<List<T>> removeAndReturnFirst( int count )
  {
    return this.<List<String>>makeRemoteCall( "removeAndReturnFirst", count ).thenApply( jsonValues -> (List<T>) HiveSerializer.deserialize( jsonValues ) );
  }

  public CompletableFuture<List<T>> removeAndReturnLast( int count )
  {
    return this.<List<String>>makeRemoteCall( "removeAndReturnLast", count ).thenApply( jsonValues -> (List<T>) HiveSerializer.deserialize( jsonValues ) );
  }

  public CompletableFuture<Long> removeValue( T value, int count )
  {
    return makeRemoteCall( "removeValue", HiveSerializer.serialize( value ), count );
  }

  public CompletableFuture<Long> length()
  {
    return makeRemoteCall( "length" );
  }

  // ----------------------------------------

  private <T> CompletableFuture<T> makeRemoteCall( String methodName, Object... args )
  {
    return makeRemoteCall( HIVE_LIST_ALIAS, methodName, args );
  }
}
