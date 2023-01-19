package com.backendless.hive;

import com.backendless.core.responder.AdaptingResponder;

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
    return makeRemoteCall( "add", new AdaptingResponder<>( Long.class ), ScoreValuePair.toObjectArray( items ), duplicateBehaviour, scoreUpdateMode, resultType );
  }

  public CompletableFuture<Long> set( List<ScoreValuePair<T>> items, DuplicateBehaviour duplicateBehaviour, ScoreUpdateMode scoreUpdateMode, ResultType resultType )
  {
    return makeRemoteCall( "set", new AdaptingResponder<>( Long.class ), ScoreValuePair.toObjectArray( items ), duplicateBehaviour, scoreUpdateMode, resultType );
  }

  public CompletableFuture<Double> increment( double scoreAmount, String member )
  {
    return makeRemoteCall( "increment", scoreAmount, member );
  }

  public CompletableFuture<List<ScoreValuePair<T>>> getAndRemoveMax( int count )
  {
    return makeRemoteCall( "getAndRemoveMax", count );
  }

  public CompletableFuture<List<ScoreValuePair<T>>> getAndRemoveMin( int count )
  {
    return makeRemoteCall( "getAndRemoveMin", count );
  }

  public CompletableFuture<List<String>> getRandom( int count )
  {
    return makeRemoteCall( "getRandom", count );
  }

  public CompletableFuture<List<ScoreValuePair<T>>> getRandomWithScores( int count )
  {
    return makeRemoteCall( "getRandomWithScores", count );
  }

  public CompletableFuture<Double> getScore( String member )
  {
    return makeRemoteCall( "getScore", member );
  }

  public CompletableFuture<Long> getRank( String member )
  {
    return makeRemoteCall( "getRank", new AdaptingResponder<>( Long.class ), member );
  }

  public CompletableFuture<Long> getReverseRank( String member )
  {
    return makeRemoteCall( "getReverseRank", new AdaptingResponder<>( Long.class ), member );
  }

  public CompletableFuture<List<String>> getRangeByRank( long startRank, long stopRank )
  {
    return makeRemoteCall( "getRangeByRank", startRank, stopRank );
  }

  public CompletableFuture<List<ScoreValuePair<T>>> getRangeWithScoresByRank( long startRank, long stopRank )
  {
    return makeRemoteCall( "getRangeWithScoresByRank", startRank, stopRank );
  }

  public CompletableFuture<List<String>> getReverseRangeByRank( long startRank, long stopRank )
  {
    return makeRemoteCall( "getReverseRangeByRank", startRank, stopRank );
  }

  public CompletableFuture<List<ScoreValuePair<T>>> getReverseRangeWithScoresByRank( long startRank, long stopRank )
  {
    return makeRemoteCall( "getReverseRangeWithScoresByRank", startRank, stopRank );
  }

  public CompletableFuture<List<String>> getRangeByScore( double minScore, ValueBound minBound, double maxScore, ValueBound maxBound, long offset, long pageSize )
  {
    return makeRemoteCall( "getRangeByScore", minScore, minBound, maxScore, maxBound, offset, pageSize );
  }

  public CompletableFuture<List<ScoreValuePair<T>>> getRangeWithScoresByScore( double minScore, ValueBound minBound, double maxScore, ValueBound maxBound, long offset, long pageSize )
  {
    return makeRemoteCall( "getRangeWithScoresByScore", minScore, minBound, maxScore, maxBound, offset, pageSize );
  }

  public CompletableFuture<List<String>> getReverseRangeByScore( double minScore, ValueBound minBound, double maxScore, ValueBound maxBound, long offset, long pageSize )
  {
    return makeRemoteCall( "getReverseRangeByScore", minScore, minBound, maxScore, maxBound, offset, pageSize );
  }

  public CompletableFuture<List<ScoreValuePair<T>>> getReverseRangeWithScoresByScore( double minScore, ValueBound minBound, double maxScore, ValueBound maxBound, long offset, long pageSize )
  {
    return makeRemoteCall( "getReverseRangeWithScoresByScore", minScore, minBound, maxScore, maxBound, offset, pageSize );
  }

  public CompletableFuture<Long> remove( List<String> values )
  {
    return makeRemoteCall( "remove", new AdaptingResponder<>( Long.class ), values );
  }

  public CompletableFuture<Long> removeByRank( long startRank, long stopRank )
  {
    return makeRemoteCall( "removeByRank", new AdaptingResponder<>( Long.class ), startRank, stopRank );
  }

  public CompletableFuture<Long> removeByScore( double minScore, ValueBound minBound, double maxScore, ValueBound maxBound )
  {
    return makeRemoteCall( "removeByScore", new AdaptingResponder<>( Long.class ), minScore, minBound, maxScore, maxBound );
  }

  public CompletableFuture<Long> size( )
  {
    return makeRemoteCall( "size", new AdaptingResponder<>( Long.class ) );
  }

  public CompletableFuture<Long> count( double minScore, ValueBound minBound, double maxScore, ValueBound maxBound )
  {
    return makeRemoteCall( "count", new AdaptingResponder<>( Long.class ), minScore, minBound, maxScore, maxBound );
  }

  // ----------------------------------------

  private <T> CompletableFuture<T> makeRemoteCall( String methodName, Object... args )
  {
    return makeRemoteCallWithStoreKey( HIVE_SORTED_SET_ALIAS, methodName, args );
  }

  private <T> CompletableFuture<T> makeRemoteCall( String methodName, AdaptingResponder<T> adaptingResponder, Object... args )
  {
    return makeRemoteCallWithStoreKey( HIVE_SORTED_SET_ALIAS, methodName, adaptingResponder, args );
  }
}
