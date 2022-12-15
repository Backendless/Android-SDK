package com.backendless;

import com.backendless.hive.HiveKeyValue;
import com.backendless.hive.HiveList;
import com.backendless.hive.HiveManagement;
import com.backendless.hive.HiveMap;
import com.backendless.hive.HiveSet;
import com.backendless.hive.HiveSortedSet;

import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;


public final class Hive
{
  private final static WeakHashMap<String, Hive> hives = new WeakHashMap<>();
  private final static WeakHashMap<String, HiveList> listHives = new WeakHashMap<>();
  private final static WeakHashMap<String, HiveSet> setHives = new WeakHashMap<>();
  private final static WeakHashMap<String, HiveSortedSet> sortedSetHives = new WeakHashMap<>();
  private final static WeakHashMap<String, HiveMap> mapHives = new WeakHashMap<>();

  private final String hiveName;
  private final HiveManagement hiveManagement;
  private final HiveKeyValue hiveKeyValue;

  private Hive( String name )
  {
    this.hiveName = name;
    this.hiveManagement = new HiveManagement();
    this.hiveKeyValue = new HiveKeyValue( hiveName, hiveManagement );
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

  public HiveList ListStore( String storeKey )
  {
    return ListStore( storeKey, Object.class );
  }

  public <T> HiveList<T> ListStore( String storeKey, Class<T> tClass )
  {
    final String hiveStoreKey = getComplexKey( storeKey );
    HiveList hiveList = listHives.get( hiveStoreKey );
    if( hiveList == null )
    {
      hiveList = new HiveList( hiveName, storeKey, hiveManagement );
      listHives.put( hiveStoreKey, hiveList );
    }
    return hiveList;
  }

  public HiveSet SetStore( String storeKey )
  {
    return SetStore( storeKey, Object.class );
  }

  public <T> HiveSet SetStore( String storeKey, Class<T> tClass )
  {
    final String hiveStoreKey = getComplexKey( storeKey );
    HiveSet hiveSet = setHives.get( hiveStoreKey );
    if( hiveSet == null )
    {
      hiveSet = new HiveSet( hiveName, storeKey, hiveManagement );
      setHives.put( hiveStoreKey, hiveSet );
    }
    return hiveSet;
  }

  public HiveSortedSet SortedSetStore( String storeKey )
  {
    return SortedSetStore( storeKey, Object.class );
  }

  public <T> HiveSortedSet<T> SortedSetStore( String storeKey, Class<T> tClass )
  {
    final String hiveStoreKey = getComplexKey( storeKey );
    HiveSortedSet hiveSortedSet = sortedSetHives.get( hiveStoreKey );
    if( hiveSortedSet == null )
    {
      hiveSortedSet = new HiveSortedSet( hiveName, storeKey, hiveManagement );
      sortedSetHives.put( hiveStoreKey, hiveSortedSet );
    }
    return hiveSortedSet;
  }

  public HiveMap MapStore( String storeKey )
  {
    return MapStore( storeKey, Object.class );
  }

  public <T> HiveMap<T> MapStore( String storeKey, Class<T> tClass )
  {
    final String hiveStoreKey = getComplexKey( storeKey );
    HiveMap hiveMap = mapHives.get( hiveStoreKey );
    if( hiveMap == null )
    {
      hiveMap = new HiveMap( hiveName, storeKey, hiveManagement );
      mapHives.put( hiveStoreKey, hiveMap );
    }
    return hiveMap;
  }

  private String getComplexKey( String storeKey )
  {
    return hiveName + "-" + storeKey;
  }
}
