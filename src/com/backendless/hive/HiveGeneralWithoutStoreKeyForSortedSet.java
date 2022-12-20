package com.backendless.hive;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public class HiveGeneralWithoutStoreKeyForSortedSet extends HiveGeneralWithoutStoreKey
{
  HiveGeneralWithoutStoreKeyForSortedSet( String hiveName, StoreType storeType, HiveManagement hiveManagement )
  {
    super( hiveName, storeType, hiveManagement );
  }

  public <T> CompletableFuture<List<ScoreValuePair<T>>> difference( List<String> sortedSetKeys )
  {
    return makeRemoteCallWithoutStoreKey( HiveSortedSet.HIVE_SORTED_SET_ALIAS, "difference", new Object[] { sortedSetKeys } );
  }

  public <T> CompletableFuture<List<ScoreValuePair<T>>> intersection( List<String> sortedSetKeys )
  {
    return makeRemoteCallWithoutStoreKey( HiveSortedSet.HIVE_SORTED_SET_ALIAS, "intersection", new Object[] { sortedSetKeys } );
  }

  public <T> CompletableFuture<List<ScoreValuePair<T>>> union( List<String> sortedSetKeys )
  {
    return makeRemoteCallWithoutStoreKey( HiveSortedSet.HIVE_SORTED_SET_ALIAS, "union", new Object[] { sortedSetKeys } );
  }
}
