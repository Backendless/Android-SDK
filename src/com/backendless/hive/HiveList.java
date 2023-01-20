package com.backendless.hive;

import com.backendless.core.responder.AdaptingResponder;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public final class HiveList<T> extends HiveGeneralForComplexStore
{
  public final static String HIVE_LIST_ALIAS = "com.backendless.services.hive.HiveListService";

  HiveList( String hiveName, String storeKey )
  {
    super( hiveName, StoreType.List, storeKey );
  }


  public CompletableFuture<List<T>> get()
  {
    return this.<List<String>>makeRemoteCall( "get" ).thenApply( HiveSerializer::deserialize );
  }

  public CompletableFuture<List<T>> get( int start, int stop )
  {
    return this.<List<String>>makeRemoteCall( "get", start, stop ).thenApply( HiveSerializer::deserialize );
  }

  public CompletableFuture<T> get( int index )
  {
    return this.<String>makeRemoteCall( "get", index ).thenApply( HiveSerializer::deserialize );
  }

  public CompletableFuture<Long> set( List<T> values )
  {
    return makeRemoteCall( "set", new AdaptingResponder<>( Long.class ), HiveSerializer.serializeAsList( values ) );
  }

  public CompletableFuture<Void> set( int index, Object value )
  {
    return makeRemoteCall( "set", index, HiveSerializer.serialize( value ) );
  }

  public CompletableFuture<Long> insertBefore( Object targetValue, Object value )
  {
    return insert( targetValue, value, true );
  }

  public CompletableFuture<Long> insertAfter( Object targetValue, Object value, boolean before )
  {
    return insert( targetValue, value, false );
  }

  public CompletableFuture<Long> insert( Object targetValue, Object value, boolean before )
  {
    return makeRemoteCall( "insert", new AdaptingResponder<>( Long.class ), HiveSerializer.serialize( targetValue ), HiveSerializer.serialize( value ), before );
  }

  public CompletableFuture<Long> addFirst( T value )
  {
    return addFirst( Collections.singletonList( value ) );
  }

  public CompletableFuture<Long> addFirst( List<T> values )
  {
    return makeRemoteCall( "addFirst", new AdaptingResponder<>( Long.class ), HiveSerializer.serializeAsList( values ) );
  }

  public CompletableFuture<Long> addLast( T value )
  {
    return addLast( Collections.singletonList( value ) );
  }

  public CompletableFuture<Long> addLast( List<T> values )
  {
    return makeRemoteCall( "addLast", new AdaptingResponder<>( Long.class ), HiveSerializer.serializeAsList( values ) );
  }

  public CompletableFuture<T> deleteAndReturnFirst()
  {
    return this.<String>makeRemoteCall( "removeAndReturnFirst" ).thenApply( HiveSerializer::deserialize );
  }

  public CompletableFuture<T> deleteAndReturnLast()
  {
    return this.<String>makeRemoteCall( "removeAndReturnLast" ).thenApply( HiveSerializer::deserialize );
  }

  public CompletableFuture<List<T>> deleteAndReturnFirst( int count )
  {
    return this.<List<String>>makeRemoteCall( "removeAndReturnFirst", count ).thenApply( HiveSerializer::deserialize );
  }

  public CompletableFuture<List<T>> deleteAndReturnLast( int count )
  {
    return this.<List<String>>makeRemoteCall( "removeAndReturnLast", count ).thenApply( HiveSerializer::deserialize );
  }

  public CompletableFuture<Long> deleteValue( T value, int count )
  {
    return makeRemoteCall( "removeValue", new AdaptingResponder<>( Long.class ), HiveSerializer.serialize( value ), count );
  }

  public CompletableFuture<Long> length()
  {
    return makeRemoteCall( "length", new AdaptingResponder<>( Long.class ) );
  }

  // ----------------------------------------

  private <T> CompletableFuture<T> makeRemoteCall( String methodName, Object... args )
  {
    return makeRemoteCallWithStoreKey( HIVE_LIST_ALIAS, methodName, args );
  }

  private <T> CompletableFuture<T> makeRemoteCall( String methodName, AdaptingResponder<T> adaptingResponder, Object... args )
  {
    return makeRemoteCallWithStoreKey( HIVE_LIST_ALIAS, methodName, adaptingResponder, args );
  }
}
