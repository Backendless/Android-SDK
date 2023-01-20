package com.backendless.hive;

import com.backendless.core.responder.AdaptingResponder;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;


public final class HiveSet<T> extends HiveGeneralForComplexStore
{
  public final static String HIVE_SET_ALIAS = "com.backendless.services.hive.HiveSetService";

  HiveSet( String hiveName, String storeKey )
  {
    super( hiveName, StoreType.Set, storeKey );
  }


  public CompletableFuture<Set<T>> get()
  {
    return this.<Set<String>>makeRemoteCall( "get" ).thenApply( HiveSerializer::deserialize );
  }

  public CompletableFuture<List<T>> getRandom( int count )
  {
    return this.<List<String>>makeRemoteCall( "getRandom", count ).thenApply( HiveSerializer::deserialize );
  }

  public CompletableFuture<Set<T>> getRandomAndDel( int count )
  {
    return this.<Set<String>>makeRemoteCall( "getRandomAndDel", count ).thenApply( HiveSerializer::deserialize );
  }

  public CompletableFuture<Boolean> isValueMember( T value )
  {
    return makeRemoteCall( "contains", HiveSerializer.serialize( value ) );
  }

  public CompletableFuture<List<Boolean>> isValueMember( List<T> values )
  {
    return makeRemoteCall( "contains", HiveSerializer.serialize( values ) );
  }

  public CompletableFuture<Long> length()
  {
    return this.makeRemoteCall( "size", new AdaptingResponder<>( Long.class ) );
  }

  public CompletableFuture<Long> add( T value )
  {
    return add( Collections.singletonList( value ) );
  }

  public CompletableFuture<Long> add( List<T> values )
  {
    return makeRemoteCall( "add", new AdaptingResponder<>( Long.class ), HiveSerializer.serialize( values ) );
  }

  public CompletableFuture<Long> delete( List<String> values )
  {
    return this.makeRemoteCall( "del", new AdaptingResponder<>( Long.class ), values );
  }

  // ----------------------------------------

  private <T> CompletableFuture<T> makeRemoteCall( String methodName, Object... args )
  {
    return makeRemoteCallWithStoreKey( HIVE_SET_ALIAS, methodName, args );
  }

  private <T> CompletableFuture<T> makeRemoteCall( String methodName, AdaptingResponder<T> adaptingResponder, Object... args )
  {
    return makeRemoteCallWithStoreKey( HIVE_SET_ALIAS, methodName, adaptingResponder, args );
  }
}