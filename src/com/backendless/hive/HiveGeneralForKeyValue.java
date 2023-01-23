package com.backendless.hive;

import java.util.concurrent.CompletableFuture;


abstract class HiveGeneralForKeyValue extends HiveGeneral
{
  HiveGeneralForKeyValue( String hiveName, StoreType storeType )
  {
    super( hiveName, storeType, null );
  }

  @Override
  public CompletableFuture<Long> delete( String key )
  {
    return super.delete( key );
  }

  @Override
  public CompletableFuture<Boolean> rename( String key, String newKey, boolean overwrite )
  {
    return super.rename( key, newKey, overwrite );
  }

  @Override
  public CompletableFuture<Void> expireAfter( String key, Integer ttlSeconds )
  {
    return super.expireAfter( key, ttlSeconds );
  }

  @Override
  public CompletableFuture<Void> expireAt( String key, Integer unixTimeSeconds )
  {
    return super.expireAt( key, unixTimeSeconds );
  }

  @Override
  public CompletableFuture<Long> getExpiration( String key )
  {
    return super.getExpiration( key );
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
