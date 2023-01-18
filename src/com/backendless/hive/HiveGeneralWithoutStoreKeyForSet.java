package com.backendless.hive;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;


public class HiveGeneralWithoutStoreKeyForSet extends HiveGeneralWithoutStoreKey
{
  HiveGeneralWithoutStoreKeyForSet( String hiveName, StoreType storeType, HiveManagement hiveManagement )
  {
    super( hiveName, storeType, hiveManagement );
  }

  public <T> CompletableFuture<Set<T>> difference( List<String> setKeys )
  {
    return makeRemoteCallWithoutStoreKey( HiveSet.HIVE_SET_ALIAS, "difference", new Object[] { setKeys } );
  }

  public <T> CompletableFuture<Set<T>> intersection( List<String> setKeys )
  {
    return makeRemoteCallWithoutStoreKey( HiveSet.HIVE_SET_ALIAS, "intersection", new Object[] { setKeys } );
  }

  public <T> CompletableFuture<Set<T>> union( List<String> setKeys )
  {
    return makeRemoteCallWithoutStoreKey( HiveSet.HIVE_SET_ALIAS, "union", new Object[] { setKeys } );
  }
}
