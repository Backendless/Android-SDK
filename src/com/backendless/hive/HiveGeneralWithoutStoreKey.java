package com.backendless.hive;

import com.backendless.core.responder.AdaptingResponder;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public class HiveGeneralWithoutStoreKey extends HiveGeneral
{
  protected final HiveManagement hiveManagement;

  HiveGeneralWithoutStoreKey( String hiveName, StoreType storeType, HiveManagement hiveManagement )
  {
    super( hiveName, storeType, null );
    this.hiveManagement = hiveManagement;
  }


  public CompletableFuture<Long> delete( List<String> keys )
  {
    return makeRemoteCallForGeneral( HiveGeneral.HIVE_GENERAL_KEY_ALIAS, "del", new AdaptingResponder<>( Long.class ), new Object[] { keys } );
  }

  public CompletableFuture<Long> exists( List<String> keys )
  {
    return makeRemoteCallForGeneral( HiveGeneral.HIVE_GENERAL_KEY_ALIAS, "exists", new AdaptingResponder<>( Long.class ), new Object[] { keys } );
  }

  public CompletableFuture<Long> touch( List<String> keys )
  {
    return makeRemoteCallForGeneral( HiveGeneral.HIVE_GENERAL_KEY_ALIAS, "touch", new AdaptingResponder<>( Long.class ), new Object[] { keys } );
  }

  public CompletableFuture<ScanResult> keys( String filterPattern, String cursor, int pageSize )
  {
    return hiveManagement.keys( hiveName, storeType, filterPattern, cursor, pageSize );
  }
}