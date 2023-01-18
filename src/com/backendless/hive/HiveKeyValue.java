package com.backendless.hive;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;


public final class HiveKeyValue extends HiveGeneralForKeyValue
{
  public final static String HIVE_KEY_VALUE_ALIAS = "com.backendless.services.hive.HiveKeyValueService";
  private final HiveGeneralWithoutStoreKey generalOps;

  HiveKeyValue( String hiveName, HiveGeneralWithoutStoreKey generalOps )
  {
    super( hiveName, StoreType.KeyValue );
    this.generalOps = generalOps;
  }

  public <T> CompletableFuture<T> get( String key )
  {
    return this.<String>makeRemoteCall( "get", key ).thenApply( HiveSerializer::deserialize );
  }

  public CompletableFuture<Map<String, ?>> multiGet( Set<String> keys )
  {
    return this.<Map<String, String>>makeRemoteCall( "multiGet", keys ).thenApply( HiveSerializer::deserialize );
  }

  public CompletableFuture<Void> set( String key, Object value )
  {
    return makeRemoteCall( "set", key, HiveSerializer.serialize( value ) );
  }

  public CompletableFuture<Boolean> set( String key, Object value, int expirationSeconds, Expiration expiration, Condition condition )
  {
    return makeRemoteCall( "set", key, HiveSerializer.serialize( value ), expirationSeconds, expiration, condition );
  }

  public CompletableFuture<Void> multiSet( Map<String, ?> keyValues )
  {
    return makeRemoteCall( "multiSet", HiveSerializer.serializeAsMap( keyValues ) );
  }

  public CompletableFuture<Long> incrementBy( String key, int amount )
  {
    return makeRemoteCall( "incrementBy", key, amount );
  }

  public CompletableFuture<Long> decrementBy( String key, int amount )
  {
    return makeRemoteCall( "decrementBy", key, amount );
  }

  // ----------------------------------------

  public CompletableFuture<Long> del( List<String> keys )
  {
    return generalOps.del( keys );
  }

  public CompletableFuture<Long> exists( List<String> keys )
  {
    return generalOps.exists( keys );
  }

  public CompletableFuture<Long> touch( List<String> keys )
  {
    return generalOps.touch( keys );
  }

  public CompletableFuture<ScanResult> retrieveHiveKeys( String filterPattern, String cursor, int pageSize )
  {
    return generalOps.retrieveHiveKeys( filterPattern, cursor, pageSize );
  }

  // ----------------------------------------

  private <T> CompletableFuture<T> makeRemoteCall( String methodName, Object... args )
  {
    return makeRemoteCallWithoutStoreKey( HIVE_KEY_VALUE_ALIAS, methodName, args );
  }
}
