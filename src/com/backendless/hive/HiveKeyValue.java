package com.backendless.hive;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;


public final class HiveKeyValue extends HiveGeneralForKeyValue
{
  public final static String HIVE_KEY_VALUE_ALIAS = "com.backendless.services.hive.HiveKeyValueService";

  public HiveKeyValue( String hiveName, HiveManagement hiveManagement )
  {
    super( hiveName, StoreType.KeyValue, null, hiveManagement );
  }

  public <T> CompletableFuture<T> get( String key )
  {
    return this.<String>makeRemoteCall( "get", key ).thenApply( jsonValue -> (T) HiveSerializer.deserialize( jsonValue ) );
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

  private <T> CompletableFuture<T> makeRemoteCall( String methodName, Object... args )
  {
    return makeRemoteCall( HIVE_KEY_VALUE_ALIAS, methodName, args );
  }
}
