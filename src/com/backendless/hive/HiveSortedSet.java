package com.backendless.hive;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public final class HiveSortedSet<T> extends HiveGeneralForComplexStore
{
  public final static String HIVE_SORTED_SET_ALIAS = "com.backendless.services.hive.HiveSortedSetService";

  HiveSortedSet( String hiveName, String storeKey )
  {
    super( hiveName, StoreType.SortedSet, storeKey );
  }


  public CompletableFuture<Long> add( List<ScoreValuePair<T>> items, DuplicateBehaviour duplicateBehaviour, ScoreUpdateMode scoreUpdateMode, ResultType resultType )
  {
    return makeRemoteCall( "add", ScoreValuePair.toObjectArray( items ), duplicateBehaviour, scoreUpdateMode, resultType );
  }

  public CompletableFuture<Long> set( List<ScoreValuePair> items, DuplicateBehaviour duplicateBehaviour, ScoreUpdateMode scoreUpdateMode, ResultType resultType )
  {
    return null;
  }


  public CompletableFuture<Double> increment( double scoreAmount, String member )
  {
    return null;
  }


  public CompletableFuture<List<ScoreValuePair>> getAndRemoveMax( int count )
  {
    return null;
  }


  public CompletableFuture<List<ScoreValuePair>> getAndRemoveMin( int count )
  {
    return null;
  }


  public CompletableFuture<List<String>> getRandom( int count )
  {
    return null;
  }


  public CompletableFuture<List<ScoreValuePair>> getRandomWithScores( int count )
  {
    return null;
  }


  public CompletableFuture<Double> getScore( String member )
  {
    return null;
  }


  public CompletableFuture<Long> getRank( String member )
  {
    return null;
  }


  public CompletableFuture<Long> getReverseRank( String member )
  {
    return null;
  }


  public CompletableFuture<List<String>> getRangeByRank( long startRank, long stopRank )
  {
    return null;
  }


  public CompletableFuture<List<ScoreValuePair>> getRangeWithScoresByRank( long startRank, long stopRank )
  {
    return null;
  }


  public CompletableFuture<List<String>> getReverseRangeByRank( long startRank, long stopRank )
  {
    return null;
  }


  public CompletableFuture<List<ScoreValuePair>> getReverseRangeWithScoresByRank( long startRank, long stopRank )
  {
    return null;
  }


  public CompletableFuture<List<String>> getRangeByScore( double minScore, ValueBound minBound, double maxScore, ValueBound maxBound, long offset, long pageSize )
  {
    return null;
  }


  public CompletableFuture<List<ScoreValuePair>> getRangeWithScoresByScore( double minScore, ValueBound minBound, double maxScore, ValueBound maxBound, long offset, long pageSize )
  {
    return null;
  }


  public CompletableFuture<List<String>> getReverseRangeByScore( double minScore, ValueBound minBound, double maxScore, ValueBound maxBound, long offset, long pageSize )
  {
    return null;
  }


  public CompletableFuture<List<ScoreValuePair>> getReverseRangeWithScoresByScore( double minScore, ValueBound minBound, double maxScore, ValueBound maxBound, long offset, long pageSize )
  {
    return null;
  }


  public CompletableFuture<Long> remove( List<String> values )
  {
    return null;
  }


  public CompletableFuture<Long> removeByRank( long startRank, long stopRank )
  {
    return null;
  }


  public CompletableFuture<Long> removeByScore( double minScore, ValueBound minBound, double maxScore, ValueBound maxBound )
  {
    return null;
  }


  public CompletableFuture<Long> size( )
  {
    return null;
  }


  public CompletableFuture<Long> count( double minScore, ValueBound minBound, double maxScore, ValueBound maxBound )
  {
    return null;
  }

  // ----------------------------------------

  private <T> CompletableFuture<T> makeRemoteCall( String methodName, Object... args )
  {
    return makeRemoteCallWithStoreKey( HIVE_SORTED_SET_ALIAS, methodName, args );
  }
}
