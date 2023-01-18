package com.backendless.hive;

import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;


public final class Hive
{
  private final static WeakHashMap<String, Hive> hives = new WeakHashMap<>();
  private final static WeakHashMap<String, HiveList<?>> listHives = new WeakHashMap<>();
  private final static WeakHashMap<String, HiveSet<?>> setHives = new WeakHashMap<>();
  private final static WeakHashMap<String, HiveSortedSet<?>> sortedSetHives = new WeakHashMap<>();
  private final static WeakHashMap<String, HiveMap<?>> mapHives = new WeakHashMap<>();

  private final String hiveName;
  private final HiveManagement hiveManagement;
  private final HiveKeyValue hiveKeyValue;

  private final HiveGeneralWithoutStoreKey generalKeyValueOps;
  private final HiveGeneralWithoutStoreKey generalListOps;
  private final HiveGeneralWithoutStoreKeyForSet generalSetOps;
  private final HiveGeneralWithoutStoreKeyForSortedSet generalSortedSetOps;
  private final HiveGeneralWithoutStoreKey generalMapOps;

  private Hive( String name )
  {
    this.hiveName = name;
    this.hiveManagement = HiveManagement.getInstance();
    this.generalKeyValueOps = new HiveGeneralWithoutStoreKey( hiveName, StoreType.KeyValue, hiveManagement );
    this.hiveKeyValue = new HiveKeyValue( hiveName, generalKeyValueOps );
    this.generalListOps = new HiveGeneralWithoutStoreKey( hiveName, StoreType.List, hiveManagement );
    this.generalSetOps = new HiveGeneralWithoutStoreKeyForSet( hiveName, StoreType.Set, hiveManagement );
    this.generalSortedSetOps = new HiveGeneralWithoutStoreKeyForSortedSet( hiveName, StoreType.SortedSet, hiveManagement );
    this.generalMapOps = new HiveGeneralWithoutStoreKey( hiveName, StoreType.Map, hiveManagement );
  }

  public static Hive getOrCreate( String name )
  {
    Hive hive = hives.get( name );
    if( hive == null )
    {
      hive = new Hive( name );
      hives.put( name, hive );
    }
    return hive;
  }

  // ----------------------------------------

  public CompletableFuture<Set<String>> getHiveNames()
  {
    return hiveManagement.getHiveNames();
  }

  public CompletableFuture<Void> addHive( String name )
  {
    return hiveManagement.addHive( name );
  }

  public CompletableFuture<Void> renameHive( String name, String newName )
  {
    return hiveManagement.renameHive( name, newName );
  }

  public CompletableFuture<Long> deleteHive( String name )
  {
    return hiveManagement.deleteHive( name );
  }

  public CompletableFuture<Long> deleteAllHives()
  {
    return hiveManagement.deleteAllHives();
  }

  // ----------------------------------------

  public HiveKeyValue KeyValueStore()
  {
    return hiveKeyValue;
  }

  public HiveGeneralWithoutStoreKey ListStore()
  {
    return this.generalListOps;
  }

  public HiveList<Object> ListStore( String storeKey )
  {
    return ListStore( storeKey, Object.class );
  }

  public <T> HiveList<T> ListStore( String storeKey, Class<T> tClass )
  {
    final String hiveStoreKey = getComplexKey( storeKey );
    HiveList<?> hiveList = listHives.get( hiveStoreKey );
    if( hiveList == null )
    {
      hiveList = new HiveList<>( hiveName, storeKey );
      listHives.put( hiveStoreKey, hiveList );
    }
    return (HiveList<T>) hiveList;
  }

  public HiveGeneralWithoutStoreKeyForSet SetStore()
  {
    return this.generalSetOps;
  }

  public HiveSet<Object> SetStore( String storeKey )
  {
    return SetStore( storeKey, Object.class );
  }

  public <T> HiveSet<T> SetStore( String storeKey, Class<T> tClass )
  {
    final String hiveStoreKey = getComplexKey( storeKey );
    HiveSet<?> hiveSet = setHives.get( hiveStoreKey );
    if( hiveSet == null )
    {
      hiveSet = new HiveSet<>( hiveName, storeKey );
      setHives.put( hiveStoreKey, hiveSet );
    }
    return (HiveSet<T>) hiveSet;
  }

  public HiveGeneralWithoutStoreKeyForSortedSet SortedSetStore()
  {
    return this.generalSortedSetOps;
  }

  public HiveSortedSet<Object> SortedSetStore( String storeKey )
  {
    return SortedSetStore( storeKey, Object.class );
  }

  public <T> HiveSortedSet<T> SortedSetStore( String storeKey, Class<T> tClass )
  {
    final String hiveStoreKey = getComplexKey( storeKey );
    HiveSortedSet<?> hiveSortedSet = sortedSetHives.get( hiveStoreKey );
    if( hiveSortedSet == null )
    {
      hiveSortedSet = new HiveSortedSet<>( hiveName, storeKey );
      sortedSetHives.put( hiveStoreKey, hiveSortedSet );
    }
    return (HiveSortedSet<T>) hiveSortedSet;
  }

  public HiveGeneralWithoutStoreKey MapStore()
  {
    return this.generalMapOps;
  }

  public HiveMap<Object> MapStore( String storeKey )
  {
    return MapStore( storeKey, Object.class );
  }

  public <T> HiveMap<T> MapStore( String storeKey, Class<T> tClass )
  {
    final String hiveStoreKey = getComplexKey( storeKey );
    HiveMap<?> hiveMap = mapHives.get( hiveStoreKey );
    if( hiveMap == null )
    {
      hiveMap = new HiveMap<>( hiveName, storeKey );
      mapHives.put( hiveStoreKey, hiveMap );
    }
    return (HiveMap<T>) hiveMap;
  }

  private String getComplexKey( String storeKey )
  {
    return hiveName + "-" + storeKey;
  }
}
