package com.backendless.hive;

import com.backendless.core.responder.AdaptingResponder;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

  public static final class Options
  {
    private int expirationSeconds = 0;
    private Expiration expiration = Expiration.None;
    private Condition condition = Condition.Always;

    private Options()
    {
    }

    public static Options create()
    {
      return new Options();
    }

    public Options expireAt( LocalDateTime localDateTime )
    {
      this.expiration = Expiration.UnixTimestamp;
      this.expirationSeconds = (int) localDateTime.toEpochSecond( ZoneOffset.UTC );
      return this;
    }

    public Options expireAt( int timeStampInSeconds )
    {
      this.expiration = Expiration.UnixTimestamp;
      this.expirationSeconds = timeStampInSeconds;
      return this;
    }

    public Options expireAfter( int seconds )
    {
      this.expiration = Expiration.TTL;
      this.expirationSeconds = seconds;
      return this;
    }

    public Options condition( Condition condition )
    {
      this.condition = condition;
      return this;
    }
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

  public CompletableFuture<Boolean> set( String key, Object value, int expirationSeconds, Expiration expirationType )
  {
    return set( key, value, expirationSeconds, expirationType, Condition.Always );
  }

  public CompletableFuture<Boolean> set( String key, Object value, Condition condition )
  {
    return set( key, value, 0, Expiration.None, condition );
  }

  public CompletableFuture<Boolean> set( String key, Object value, int expirationSeconds, Expiration expirationType, Condition condition )
  {
    return makeRemoteCall( "set", key, HiveSerializer.serialize( value ), expirationSeconds, expirationType, condition );
  }

  public CompletableFuture<Boolean> set( String key, Object value, Options options )
  {
    return set( key, value, options.expirationSeconds, options.expiration, options.condition );
  }

  public CompletableFuture<Void> multiSet( Map<String, ?> keyValues )
  {
    return makeRemoteCall( "multiSet", HiveSerializer.serializeAsMap( keyValues ) );
  }

  public CompletableFuture<Long> increment( String key, int amount )
  {
    return makeRemoteCall( "incrementBy", new AdaptingResponder<>( Long.class ), key, amount );
  }

  public CompletableFuture<Long> decrement( String key, int amount )
  {
    return makeRemoteCall( "decrementBy", new AdaptingResponder<>( Long.class ), key, amount );
  }

  // ----------------------------------------

  public CompletableFuture<Long> delete( List<String> keys )
  {
    return generalOps.delete( keys );
  }

  public CompletableFuture<Long> exists( List<String> keys )
  {
    return generalOps.exists( keys );
  }

  public CompletableFuture<Long> touch( List<String> keys )
  {
    return generalOps.touch( keys );
  }

  public CompletableFuture<ScanResult> keys( String filterPattern, String cursor, int pageSize )
  {
    return generalOps.keys( filterPattern, cursor, pageSize );
  }

  // ----------------------------------------

  private <T> CompletableFuture<T> makeRemoteCall( String methodName, Object... args )
  {
    return makeRemoteCallWithoutStoreKey( HIVE_KEY_VALUE_ALIAS, methodName, args );
  }

  private <T> CompletableFuture<T> makeRemoteCall( String methodName, AdaptingResponder<T> adaptingResponder, Object... args )
  {
    return makeRemoteCallWithoutStoreKey( HIVE_KEY_VALUE_ALIAS, methodName, adaptingResponder, args );
  }
}
