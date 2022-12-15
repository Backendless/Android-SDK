package com.backendless.hive;


public final class HiveSet<T> extends HiveGeneralForComplexStore
{
  public final static String HIVE_SET_ALIAS = "com.backendless.services.hive.HiveSetService";

  public HiveSet( String hiveName, String storeKey, HiveManagement hiveManagement )
  {
    super( hiveName, StoreType.Set, storeKey, hiveManagement );
  }
}
