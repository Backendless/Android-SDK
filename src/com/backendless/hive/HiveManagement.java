package com.backendless.hive;

import com.backendless.Invoker;
import com.backendless.async.callback.BackendlessCallback;

import java.util.Set;
import java.util.concurrent.CompletableFuture;


public final class HiveManagement
{
  public final static String HIVE_SERVICE_ALIAS = "com.backendless.services.hive.HiveService";

  public CompletableFuture<Set<String>> getHiveNames()
  {
    return makeRemoteCall( "getHiveNames" );
  }

  public CompletableFuture<Void> addHive( String name )
  {
    return makeRemoteCall( "addHive", name );
  }

  public CompletableFuture<Void> renameHive( String name, String newName )
  {
    return makeRemoteCall( "renameHive", name, newName );
  }

  public CompletableFuture<Long> deleteHive( String name )
  {
    return makeRemoteCall( "deleteHive", name );
  }

  public CompletableFuture<Long> deleteAllHives()
  {
    return makeRemoteCall( "deleteAllHives" );
  }

  public CompletableFuture<ScanResult> retrieveHiveKeys( String name, StoreType storeType, String filterPattern, String cursor, int pageSize )
  {
    return makeRemoteCall( "retrieveHiveKeys", name, storeType, filterPattern, cursor, pageSize );
  }

  private <T> CompletableFuture<T> makeRemoteCall( String methodName, Object... args )
  {
    CompletableFuture<T> futureResult = new CompletableFuture<>();
    Invoker.invokeAsync( HIVE_SERVICE_ALIAS, methodName, args, new BackendlessCallback<T>()
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
