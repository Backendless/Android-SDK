package com.backendless.hive;

import com.backendless.Invoker;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.core.responder.AdaptingResponder;

import java.util.Set;
import java.util.concurrent.CompletableFuture;


public final class HiveManagement
{
  public final static String HIVE_SERVICE_ALIAS = "com.backendless.services.hive.HiveService";
  private static final HiveManagement HIVE_MANAGEMENT = new HiveManagement();

  public static HiveManagement getInstance()
  {
    return HIVE_MANAGEMENT;
  }

  HiveManagement()
  {}

  public CompletableFuture<Set<String>> getNames()
  {
    return makeRemoteCall( "getHiveNames", new AdaptingResponder<>( Set.class ) )
            .thenApply( set -> (Set<String>)set );
  }

  public CompletableFuture<Void> create( String name )
  {
    return makeRemoteCall( "addHive", name );
  }

  public CompletableFuture<Void> rename( String name, String newName )
  {
    return makeRemoteCall( "renameHive", name, newName );
  }

  public CompletableFuture<Long> delete( String name )
  {
    return makeRemoteCall( "deleteHive", new AdaptingResponder<>( Long.class ), name );
  }

  public CompletableFuture<Long> deleteAll()
  {
    return makeRemoteCall( "deleteAllHives", new AdaptingResponder<>( Long.class ) );
  }

  public CompletableFuture<ScanResult> keys( String name, StoreType storeType, String filterPattern, String cursor, int pageSize )
  {
    return makeRemoteCall( "retrieveHiveKeys", name, storeType, filterPattern, cursor, pageSize );
  }

  private <T> CompletableFuture<T> makeRemoteCall( String methodName, Object... args )
  {
    return makeRemoteCall( methodName, null, args );
  }

  private <T> CompletableFuture<T> makeRemoteCall( String methodName, AdaptingResponder<T> adaptingResponder, Object... args )
  {
    CompletableFuture<T> futureResult = new CompletableFuture<>();
    Invoker.invokeAsync( HIVE_SERVICE_ALIAS, methodName, args, new BackendlessCallback<T>()
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
