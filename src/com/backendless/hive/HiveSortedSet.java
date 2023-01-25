package com.backendless.hive;

import com.backendless.core.responder.AdaptingResponder;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public final class HiveSortedSet<T> extends HiveGeneralForComplexStore
{
  public final static String HIVE_SORTED_SET_ALIAS = "com.backendless.services.hive.HiveSortedSetService";

  HiveSortedSet( String hiveName, String storeKey )
  {
    super( hiveName, StoreType.SortedSet, storeKey );
  }


  public static final class ScoreRangeOptions
  {
    private double minScore;
    private ValueBound minBound;
    private double maxScore;
    private ValueBound maxBound;

    public ScoreRangeOptions setMinScore( double minScore )
    {
      this.minScore = minScore;
      return this;
    }

    public ScoreRangeOptions setMinBound( ValueBound minBound )
    {
      this.minBound = minBound;
      return this;
    }

    public ScoreRangeOptions setMaxScore( double maxScore )
    {
      this.maxScore = maxScore;
      return this;
    }

    public ScoreRangeOptions setMaxBound( ValueBound maxBound )
    {
      this.maxBound = maxBound;
      return this;
    }
  }

  public CompletableFuture<Long> add( ScoreValuePair<T> item )
  {
    return add( Collections.singletonList( item ), null, null, null );
  }

  public CompletableFuture<Long> add( List<ScoreValuePair<T>> items )
  {
    return add( items, null, null, null );
  }

  public CompletableFuture<Long> add( List<ScoreValuePair<T>> items, DuplicateBehaviour duplicateBehaviour, ScoreUpdateMode scoreUpdateMode, ResultType resultType )
  {
    return makeRemoteCall( "add", new AdaptingResponder<>( Long.class ), ScoreValuePair.toObjectArray( items ), duplicateBehaviour, scoreUpdateMode, resultType );
  }

  public CompletableFuture<Double> incrementScore( double scoreAmount, String member )
  {
    return makeRemoteCall( "increment", scoreAmount, member );
  }

  public CompletableFuture<Double> decrementScore( double scoreAmount, String member )
  {
    return incrementScore( -scoreAmount, member );
  }

  public CompletableFuture<List<ScoreValuePair<T>>> getAndDeleteMaxScore( int count )
  {
    return this.<Object[]>makeRemoteCall( "getAndRemoveMax", count )
            .thenApply( ScoreValuePair::fromObjectArray );
  }

  public CompletableFuture<List<ScoreValuePair<T>>> getAndDeleteMinScore( int count )
  {
    return this.<Object[]>makeRemoteCall( "getAndRemoveMin", count )
            .thenApply( ScoreValuePair::fromObjectArray );
  }

  public CompletableFuture<List<T>> getRandom( int count )
  {
    return this.<String[]>makeRemoteCall( "getRandom", count )
            .thenApply( HiveSerializer::deserialize );
  }

  public CompletableFuture<List<ScoreValuePair<T>>> getRandomWithScores( int count )
  {
    return this.<Object[]>makeRemoteCall( "getRandomWithScores", count )
            .thenApply( ScoreValuePair::fromObjectArray );
  }

  public CompletableFuture<Double> getScore( String member )
  {
    return makeRemoteCall( "getScore", member );
  }

  public CompletableFuture<Long> getRank( String member, boolean reverse )
  {
    if( reverse )
      return getReverseRank( member );
    else
      return getRank( member );
  }

  private CompletableFuture<Long> getRank( String member )
  {
    return makeRemoteCall( "getRank", new AdaptingResponder<>( Long.class ), member );
  }

  private CompletableFuture<Long> getReverseRank( String member )
  {
    return makeRemoteCall( "getReverseRank", new AdaptingResponder<>( Long.class ), member );
  }

  public CompletableFuture<List<T>> getRangeByRank( long startRank, long stopRank, boolean reverse )
  {
    if( reverse )
      return getReverseRangeByRank( startRank, stopRank );
    else
      return getRangeByRank( startRank, stopRank );
  }

  private CompletableFuture<List<T>> getRangeByRank( long startRank, long stopRank )
  {
    return this.<String[]>makeRemoteCall( "getRangeByRank", startRank, stopRank )
            .thenApply( HiveSerializer::deserialize );
  }

  private CompletableFuture<List<T>> getReverseRangeByRank( long startRank, long stopRank )
  {
    return this.<String[]>makeRemoteCall( "getReverseRangeByRank", startRank, stopRank )
            .thenApply( HiveSerializer::deserialize );
  }

  public CompletableFuture<List<ScoreValuePair<T>>> getRangeWithScoresByRank( long startRank, long stopRank, boolean reverse )
  {
    if( reverse )
      return getReverseRangeWithScoresByRank( startRank, stopRank );
    else
      return getRangeWithScoresByRank( startRank, stopRank );
  }

  private CompletableFuture<List<ScoreValuePair<T>>> getRangeWithScoresByRank( long startRank, long stopRank )
  {
    return this.<Object[]>makeRemoteCall( "getRangeWithScoresByRank", startRank, stopRank )
            .thenApply( ScoreValuePair::fromObjectArray );
  }

  private CompletableFuture<List<ScoreValuePair<T>>> getReverseRangeWithScoresByRank( long startRank, long stopRank )
  {
    return this.<Object[]>makeRemoteCall( "getReverseRangeWithScoresByRank", startRank, stopRank )
            .thenApply( ScoreValuePair::fromObjectArray );
  }

  public CompletableFuture<List<T>> getRangeByScore( ScoreRangeOptions options, long offset, long pageSize, boolean reverse )
  {
    if( reverse )
      return getReverseRangeByScore( options.minScore, options.minBound, options.maxScore, options.maxBound, offset, pageSize );
    else
      return getRangeByScore( options.minScore, options.minBound, options.maxScore, options.maxBound, offset, pageSize );
  }

  private CompletableFuture<List<T>> getRangeByScore( double minScore, ValueBound minBound, double maxScore, ValueBound maxBound, long offset, long pageSize )
  {
    return this.<String[]>makeRemoteCall( "getRangeByScore", minScore, minBound, maxScore, maxBound, offset, pageSize )
            .thenApply( HiveSerializer::deserialize );
  }

  private CompletableFuture<List<T>> getReverseRangeByScore( double minScore, ValueBound minBound, double maxScore, ValueBound maxBound, long offset, long pageSize )
  {
    return this.<String[]>makeRemoteCall( "getReverseRangeByScore", minScore, minBound, maxScore, maxBound, offset, pageSize )
            .thenApply( HiveSerializer::deserialize );
  }

  public CompletableFuture<List<ScoreValuePair<T>>> getRangeWithScoresByScore( ScoreRangeOptions options, long offset, long pageSize, boolean reverse )
  {
    if( reverse )
      return getReverseRangeWithScoresByScore( options.minScore, options.minBound, options.maxScore, options.maxBound, offset, pageSize );
    else
      return getRangeWithScoresByScore( options.minScore, options.minBound, options.maxScore, options.maxBound, offset, pageSize );
  }

  private CompletableFuture<List<ScoreValuePair<T>>> getRangeWithScoresByScore( double minScore, ValueBound minBound, double maxScore, ValueBound maxBound, long offset, long pageSize )
  {
    return this.<Object[]>makeRemoteCall( "getRangeWithScoresByScore", minScore, minBound, maxScore, maxBound, offset, pageSize )
            .thenApply( ScoreValuePair::fromObjectArray );
  }

  private CompletableFuture<List<ScoreValuePair<T>>> getReverseRangeWithScoresByScore( double minScore, ValueBound minBound, double maxScore, ValueBound maxBound, long offset, long pageSize )
  {
    return this.<Object[]>makeRemoteCall( "getReverseRangeWithScoresByScore", minScore, minBound, maxScore, maxBound, offset, pageSize )
            .thenApply( ScoreValuePair::fromObjectArray );
  }

  public CompletableFuture<Long> delete( List<String> values )
  {
    return makeRemoteCall( "remove", new AdaptingResponder<>( Long.class ), values );
  }

  public CompletableFuture<Long> deleteByRank( long startRank, long stopRank )
  {
    return makeRemoteCall( "removeByRank", new AdaptingResponder<>( Long.class ), startRank, stopRank );
  }

  public CompletableFuture<Long> deleteByScore( ScoreRangeOptions options )
  {
    return deleteByScore( options.minScore, options.minBound, options.maxScore, options.maxBound );
  }

  public CompletableFuture<Long> deleteByScore( double minScore, ValueBound minBound, double maxScore, ValueBound maxBound )
  {
    return makeRemoteCall( "removeByScore", new AdaptingResponder<>( Long.class ), minScore, minBound, maxScore, maxBound );
  }

  public CompletableFuture<Long> length( )
  {
    return makeRemoteCall( "size", new AdaptingResponder<>( Long.class ) );
  }

  public CompletableFuture<Long> countBetweenScores( ScoreRangeOptions options )
  {
    return countBetweenScores( options.minScore, options.minBound, options.maxScore, options.maxBound );
  }

  private CompletableFuture<Long> countBetweenScores( double minScore, ValueBound minBound, double maxScore, ValueBound maxBound )
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
