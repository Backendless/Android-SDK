package com.backendless.hive;


public final class HiveMap<T> extends HiveGeneralForComplexStore
{
  public final static String HIVE_MAP_ALIAS = "com.backendless.services.hive.HiveMapService";

  public HiveMap( String hiveName, String storeKey, HiveManagement hiveManagement )
  {
    super( hiveName, StoreType.Map, storeKey, hiveManagement );
  }
}
