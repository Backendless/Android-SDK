package com.backendless.hive;

import java.util.concurrent.CompletableFuture;


class HiveGeneralForKeyValue extends HiveGeneral
{
  HiveGeneralForKeyValue( String hiveName, StoreType storeType )
  {
    super( hiveName, storeType, null );
  }

  @Override
  public CompletableFuture<Long> del( String key )
  {
    return super.del( key );
  }

  @Override
  public CompletableFuture<Void> rename( String key, String newKey )
  {
    return super.rename( key, newKey );
  }

  @Override
  public CompletableFuture<Boolean> renameIfNotExists( String key, String newKey )
  {
    return super.renameIfNotExists( key, newKey );
  }

  @Override
  public CompletableFuture<Void> expire( String key, Integer ttlSeconds )
  {
    return super.expire( key, ttlSeconds );
  }

  @Override
  public CompletableFuture<Void> expireAt( String key, Integer unixTimeSeconds )
  {
    return super.expireAt( key, unixTimeSeconds );
  }

  @Override
  public CompletableFuture<Long> getExpirationTTL( String key )
  {
    return super.getExpirationTTL( key );
  }

  @Override
  public CompletableFuture<Void> clearExpiration( String key )
  {
    return super.clearExpiration( key );
  }

  @Override
  public CompletableFuture<Long> secondsSinceLastOperation( String key )
  {
    return super.secondsSinceLastOperation( key );
  }
}
