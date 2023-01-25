package com.backendless.hive;

import com.backendless.core.responder.AdaptingResponder;

import java.util.Set;
import java.util.concurrent.CompletableFuture;


public class HiveGeneralWithoutStoreKeyForSortedSet extends HiveGeneralWithoutStoreKey
{
  HiveGeneralWithoutStoreKeyForSortedSet( String hiveName, StoreType storeType, HiveManagement hiveManagement )
  {
    super( hiveName, storeType, hiveManagement );
  }

  public <T> CompletableFuture<Set<ScoreValuePair<T>>> difference( Set<String> sortedSetKeys )
  {
    return makeRemoteCallWithoutStoreKey( HiveSortedSet.HIVE_SORTED_SET_ALIAS, "difference", new AdaptingResponder<>( Set.class ), new Object[] { sortedSetKeys } )
            .thenApply( result -> (Set<ScoreValuePair<T>>) result );
  }

  public <T> CompletableFuture<Set<ScoreValuePair<T>>> intersection( Set<String> sortedSetKeys )
  {
    return makeRemoteCallWithoutStoreKey( HiveSortedSet.HIVE_SORTED_SET_ALIAS, "intersection", new AdaptingResponder<>( Set.class ), new Object[] { sortedSetKeys } )
            .thenApply( result -> (Set<ScoreValuePair<T>>) result );
  }

  public <T> CompletableFuture<Set<ScoreValuePair<T>>> union( Set<String> sortedSetKeys )
  {
    return makeRemoteCallWithoutStoreKey( HiveSortedSet.HIVE_SORTED_SET_ALIAS, "union", new AdaptingResponder<>( Set.class ), new Object[] { sortedSetKeys } )
            .thenApply( result -> (Set<ScoreValuePair<T>>) result );
  }
}
