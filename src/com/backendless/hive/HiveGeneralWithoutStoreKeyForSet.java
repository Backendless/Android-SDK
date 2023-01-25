package com.backendless.hive;

import com.backendless.core.responder.AdaptingResponder;

import java.util.Set;
import java.util.concurrent.CompletableFuture;


public class HiveGeneralWithoutStoreKeyForSet extends HiveGeneralWithoutStoreKey
{
  HiveGeneralWithoutStoreKeyForSet( String hiveName, StoreType storeType, HiveManagement hiveManagement )
  {
    super( hiveName, storeType, hiveManagement );
  }

  public <T> CompletableFuture<Set<T>> difference( Set<String> setKeys )
  {
    return makeRemoteCallWithoutStoreKey( HiveSet.HIVE_SET_ALIAS, "difference", new AdaptingResponder<>( Set.class ), new Object[] { setKeys } )
            .thenApply( result -> (Set<T>) result );
  }

  public <T> CompletableFuture<Set<T>> intersection( Set<String> setKeys )
  {
    return makeRemoteCallWithoutStoreKey( HiveSet.HIVE_SET_ALIAS, "intersection", new AdaptingResponder<>( Set.class ), new Object[] { setKeys } )
            .thenApply( result -> (Set<T>) result );
  }

  public <T> CompletableFuture<Set<T>> union( Set<String> setKeys )
  {
    return makeRemoteCallWithoutStoreKey( HiveSet.HIVE_SET_ALIAS, "union", new AdaptingResponder<>( Set.class ), new Object[] { setKeys } )
            .thenApply( result -> (Set<T>) result );
  }
}
