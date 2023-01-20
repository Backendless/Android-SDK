package com.backendless.hive;

import java.util.concurrent.CompletableFuture;


abstract class HiveGeneralForComplexStore extends HiveGeneral
{
  HiveGeneralForComplexStore( String hiveName, StoreType storeType, String storeKey )
  {
    super( hiveName, storeType, storeKey );
  }

  @Override
  public CompletableFuture<Long> delete()
  {
    return super.delete();
  }

  @Override
  public CompletableFuture<Boolean> rename( String newKey, boolean overwrite )
  {
    return super.rename( newKey, overwrite );
  }

  @Override
  public CompletableFuture<Void> expireAfter( Integer ttlSeconds )
  {
    return super.expireAfter( ttlSeconds );
  }

  @Override
  public CompletableFuture<Void> expireAt( Integer unixTimeSeconds )
  {
    return super.expireAt( unixTimeSeconds );
  }

  @Override
  public CompletableFuture<Long> getExpiration()
  {
    return super.getExpiration();
  }

  @Override
  public CompletableFuture<Void> clearExpiration()
  {
    return super.clearExpiration();
  }

  @Override
  public CompletableFuture<Long> secondsSinceLastOperation()
  {
    return super.secondsSinceLastOperation();
  }
}
