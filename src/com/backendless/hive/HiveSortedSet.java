package com.backendless.hive;


public final class HiveSortedSet<T> extends HiveGeneralForComplexStore
{
  public final static String HIVE_SORTED_SET_ALIAS = "com.backendless.services.hive.HiveSortedSetService";

  public HiveSortedSet( String hiveName, String storeKey, HiveManagement hiveManagement )
  {
    super( hiveName, StoreType.SortedSet, storeKey, hiveManagement );
  }
}
