package com.backendless.hive;

import com.backendless.Invoker;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.core.responder.AdaptingResponder;

import java.util.concurrent.CompletableFuture;


abstract class HiveGeneral
{
  public final static String HIVE_GENERAL_KEY_ALIAS = "com.backendless.services.hive.HiveGeneralKeyService";
  protected final String hiveName;
  protected final StoreType storeType;
  protected final String storeKey;

  HiveGeneral( String hiveName, StoreType storeType, String storeKey )
  {
    this.hiveName = hiveName;
    this.storeType = storeType;
    this.storeKey = storeKey;
  }

  protected CompletableFuture<Long> del()
  {
    return makeRemoteCallForGeneral( "del", new AdaptingResponder<>( Long.class ) );
  }

  protected CompletableFuture<Long> del( String key )
  {
    return makeRemoteCallForGeneral( "del", new AdaptingResponder<>( Long.class ), key );
  }

  protected CompletableFuture<Void> rename( String newKey )
  {
    return makeRemoteCallForGeneral( "rename", newKey );
  }

  protected CompletableFuture<Void> rename( String key, String newKey )
  {
    return makeRemoteCallForGeneral( "rename", key, newKey );
  }

  protected CompletableFuture<Boolean> renameIfNotExists( String newKey )
  {
    return makeRemoteCallForGeneral( "renameIfNotExists", newKey );
  }

  protected CompletableFuture<Boolean> renameIfNotExists( String key, String newKey )
  {
    return makeRemoteCallForGeneral( "renameIfNotExists", key, newKey );
  }

  protected CompletableFuture<Void> expire( Integer ttlSeconds )
  {
    return makeRemoteCallForGeneral( "expire", ttlSeconds );
  }

  protected CompletableFuture<Void> expire( String key, Integer ttlSeconds )
  {
    return makeRemoteCallForGeneral( "expire", key, ttlSeconds );
  }

  protected CompletableFuture<Void> expireAt( Integer unixTimeSeconds )
  {
    return makeRemoteCallForGeneral( "expireAt", unixTimeSeconds );
  }

  protected CompletableFuture<Void> expireAt( String key, Integer unixTimeSeconds )
  {
    return makeRemoteCallForGeneral( "expireAt", key, unixTimeSeconds );
  }

  protected CompletableFuture<Long> getExpirationTTL()
  {
    return makeRemoteCallForGeneral( "getExpirationTTL", new AdaptingResponder<>( Long.class ) );
  }

  protected CompletableFuture<Long> getExpirationTTL( String key )
  {
    return makeRemoteCallForGeneral( "getExpirationTTL", new AdaptingResponder<>( Long.class ), key );
  }

  protected CompletableFuture<Void> clearExpiration()
  {
    return makeRemoteCallForGeneral( "clearExpiration" );
  }

  protected CompletableFuture<Void> clearExpiration( String key )
  {
    return makeRemoteCallForGeneral( "clearExpiration", key );
  }

  protected CompletableFuture<Long> secondsSinceLastOperation()
  {
    return makeRemoteCallForGeneral( "secondsSinceLastOperation", new AdaptingResponder<>( Long.class ) );
  }

  protected CompletableFuture<Long> secondsSinceLastOperation( String key )
  {
    return makeRemoteCallForGeneral( "secondsSinceLastOperation", new AdaptingResponder<>( Long.class ), key );
  }

  // ----------------------------------------

  protected <T> CompletableFuture<T> makeRemoteCallForGeneral( String methodName, Object... args )
  {
    return makeRemoteCallForGeneral( methodName, null, args );
  }

  protected <T> CompletableFuture<T> makeRemoteCallForGeneral( String methodName, AdaptingResponder<T> adaptingResponder, Object... args )
  {
    final Object[] combinedArgs;
    final int dstPos;
    if( storeKey == null )
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
    return makeRemoteCall( HIVE_GENERAL_KEY_ALIAS, methodName, adaptingResponder, combinedArgs );
  }

  protected <T> CompletableFuture<T> makeRemoteCallWithoutStoreKey( String remoteServiceName, String methodName, Object[] args )
  {
    return makeRemoteCallWithoutStoreKey( remoteServiceName, methodName, null, args );
  }

  protected <T> CompletableFuture<T> makeRemoteCallWithoutStoreKey( String remoteServiceName, String methodName, AdaptingResponder<T> adaptingResponder, Object[] args )
  {
    final Object[] combinedArgs = new Object[ 1 + args.length ];
    combinedArgs[ 0 ] = hiveName;
    System.arraycopy( args, 0, combinedArgs, 1, args.length );
    return makeRemoteCall( remoteServiceName, methodName, adaptingResponder, combinedArgs );
  }

  protected <T> CompletableFuture<T> makeRemoteCallWithStoreKey( String remoteServiceName, String methodName, Object[] args )
  {
    return makeRemoteCallWithStoreKey( remoteServiceName, methodName, null, args );
  }

  protected <T> CompletableFuture<T> makeRemoteCallWithStoreKey( String remoteServiceName, String methodName, AdaptingResponder<T> adaptingResponder, Object[] args )
  {
    final Object[] combinedArgs = new Object[ 2 + args.length ];
    combinedArgs[ 0 ] = hiveName;
    combinedArgs[ 1 ] = storeKey;
    System.arraycopy( args, 0, combinedArgs, 2, args.length );

    return makeRemoteCall( remoteServiceName, methodName, adaptingResponder, combinedArgs );
  }

  private <T> CompletableFuture<T> makeRemoteCall( String remoteServiceName, String methodName, AdaptingResponder<T> adaptingResponder, Object[] args )
  {
    CompletableFuture<T> futureResult = new CompletableFuture<>();
    Invoker.invokeAsync( remoteServiceName, methodName, args, new BackendlessCallback<T>()
    {
      @Override
      public void handleResponse( T response )
      {
        futureResult.complete( response );
      }
    }, adaptingResponder );
    return futureResult;
  }
}
