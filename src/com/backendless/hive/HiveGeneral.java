package com.backendless.hive;

import com.backendless.Invoker;
import com.backendless.async.callback.BackendlessCallback;

import java.util.List;
import java.util.concurrent.CompletableFuture;


class HiveGeneral
{
  public final static String HIVE_GENERAL_KEY_ALIAS = "com.backendless.services.hive.HiveGeneralKeyService";
  protected final String hiveName;
  protected final StoreType storeType;
  protected final String storeKey;
  protected final HiveManagement hiveManagement;

  public HiveGeneral( String hiveName, StoreType storeType, String storeKey, HiveManagement hiveManagement )
  {
    this.hiveName = hiveName;
    this.storeType = storeType;
    this.storeKey = storeKey;
    this.hiveManagement = hiveManagement;
  }

  protected CompletableFuture<Long> del()
  {
    return makeRemoteCall( "del" );
  }

  protected CompletableFuture<Long> del( String key )
  {
    return makeRemoteCall( "del", key );
  }

  protected CompletableFuture<Void> rename( String newKey )
  {
    return makeRemoteCall( "rename", newKey );
  }

  protected CompletableFuture<Void> rename( String key, String newKey )
  {
    return makeRemoteCall( "rename", key, newKey );
  }

  protected CompletableFuture<Boolean> renameIfNotExists( String newKey )
  {
    return makeRemoteCall( "renameIfNotExists", newKey );
  }

  protected CompletableFuture<Boolean> renameIfNotExists( String key, String newKey )
  {
    return makeRemoteCall( "renameIfNotExists", key, newKey );
  }

  protected CompletableFuture<Void> expire( Integer ttlSeconds )
  {
    return makeRemoteCall( "expire", ttlSeconds );
  }

  protected CompletableFuture<Void> expire( String key, Integer ttlSeconds )
  {
    return makeRemoteCall( "expire", key, ttlSeconds );
  }

  protected CompletableFuture<Void> expireAt( Integer unixTimeSeconds )
  {
    return makeRemoteCall( "expireAt", unixTimeSeconds );
  }

  protected CompletableFuture<Void> expireAt( String key, Integer unixTimeSeconds )
  {
    return makeRemoteCall( "expireAt", key, unixTimeSeconds );
  }

  protected CompletableFuture<Long> getExpirationTTL()
  {
    return makeRemoteCall( "getExpirationTTL" );
  }

  protected CompletableFuture<Long> getExpirationTTL( String key )
  {
    return makeRemoteCall( "getExpirationTTL", key );
  }

  protected CompletableFuture<Void> clearExpiration()
  {
    return makeRemoteCall( "clearExpiration" );
  }

  protected CompletableFuture<Void> clearExpiration( String key )
  {
    return makeRemoteCall( "clearExpiration", key );
  }

  protected CompletableFuture<Long> secondsSinceLastOperation()
  {
    return makeRemoteCall( "secondsSinceLastOperation" );
  }

  protected CompletableFuture<Long> secondsSinceLastOperation( String key )
  {
    return makeRemoteCall( "secondsSinceLastOperation", key );
  }

  // ----------------------------------------

  public CompletableFuture<Long> del( List<String> keys )
  {
    return makeRemoteCall( "del", keys );
  }

  public CompletableFuture<Long> exists( List<String> keys )
  {
    return makeRemoteCall( "exists", keys );
  }

  public CompletableFuture<Long> touch( List<String> keys )
  {
    return makeRemoteCall( "touch", keys );
  }

  public CompletableFuture<ScanResult> retrieveHiveKeys( String filterPattern, String cursor, int pageSize )
  {
    return hiveManagement.retrieveHiveKeys( hiveName, storeType, filterPattern, cursor, pageSize );
  }

  // ----------------------------------------

  private <T> CompletableFuture<T> makeRemoteCall( String methodName, Object... args )
  {
    return makeRemoteCall( HIVE_GENERAL_KEY_ALIAS, methodName, args );
  }

  protected <T> CompletableFuture<T> makeRemoteCall( String remoteServiceName, String methodName, Object[] args )
  {
    final Object[] combinedArgs;
    final int dstPos;
    if ( storeKey == null )
    {
      dstPos = 2;
      combinedArgs = new Object[ dstPos + args.length ];
      combinedArgs[ 0 ] = hiveName;
      combinedArgs[ 1 ] = storeType;
    }
    else
    {
      dstPos = 3;
      combinedArgs = new Object[ dstPos + args.length ];
      combinedArgs[ 0 ] = hiveName;
      combinedArgs[ 1 ] = storeType;
      combinedArgs[ 2 ] = storeKey;
    }
    
    System.arraycopy( args, 0, combinedArgs, dstPos, args.length );

    CompletableFuture<T> futureResult = new CompletableFuture<>();
    Invoker.invokeAsync( remoteServiceName, methodName, combinedArgs, new BackendlessCallback<T>()
    {
      @Override
      public void handleResponse( T response )
      {
        futureResult.complete( response );
      }
    } );
    return futureResult;
  }
}
