package com.backendless.hive;

import java.util.concurrent.CompletableFuture;


public class HiveGeneralForComplexStore extends HiveGeneral
{
  public HiveGeneralForComplexStore( String hiveName, StoreType storeType, String storeKey, HiveManagement hiveManagement )
  {
    super( hiveName, storeType, storeKey, hiveManagement );
  }

  @Override
  public CompletableFuture<Long> del()
  {
    return super.del();
  }

  @Override
  public CompletableFuture<Void> rename( String newKey )
  {
    return super.rename( newKey );
  }

  @Override
  public CompletableFuture<Boolean> renameIfNotExists( String newKey )
  {
    return super.renameIfNotExists( newKey );
  }

  @Override
  public CompletableFuture<Void> expire( Integer ttlSeconds )
  {
    return super.expire( ttlSeconds );
  }

  @Override
  public CompletableFuture<Void> expireAt( Integer unixTimeSeconds )
  {
    return super.expireAt( unixTimeSeconds );
  }

  @Override
  public CompletableFuture<Long> getExpirationTTL()
  {
    return super.getExpirationTTL();
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
