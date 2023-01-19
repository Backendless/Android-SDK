package com.backendless.hive;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Getter
@ToString
@EqualsAndHashCode
public class ScoreValuePair<T>
{
  final private double score;
  final private T value;

  public ScoreValuePair( double score, T value )
  {
    if( value == null )
      throw new NullPointerException( "Value is null for score: " + score );

    this.score = score;
    this.value = value;
  }

  public static <T> List<ScoreValuePair<T>> from( Object[] arrayItems )
  {
    if( arrayItems.length % 2 != 0 )
      throw new IllegalArgumentException( "Wrong length of incoming array for ScoreValuePair. It should contain even numbers of elements." );

    List<ScoreValuePair<T>> items = new ArrayList<>();
    for( int i = 0; i < arrayItems.length; i += 2 )
      items.add( new ScoreValuePair<>( ((Number) arrayItems[ i ]).doubleValue(), (T) arrayItems[ i + 1 ] ) );

    return items;
  }

  static <T> Object[] toObjectArray( List<ScoreValuePair<T>> items )
  {
    Object[] arrayItems = new Object[ items.size() * 2 ];
    for( int i = 0; i < items.size(); i++ )
    {
      arrayItems[ i * 2 ] = items.get( i ).getScore();
      arrayItems[ i * 2 + 1 ] = HiveSerializer.serialize( items.get( i ).getValue() );
    }

    return arrayItems;
  }

  static <T> List<ScoreValuePair<T>> fromObjectArray( Object[] arrayItems )
  {
    if( arrayItems.length % 2 != 0 )
      throw new IllegalArgumentException( "Wrong length of incoming array for ScoreValuePair. It should contain even numbers of elements." );

    List<ScoreValuePair<T>> items = new ArrayList<>();
    for( int i = 0; i < arrayItems.length; i += 2 )
      items.add( new ScoreValuePair<>( ((Number) arrayItems[ i ]).doubleValue(), HiveSerializer.deserialize( (String) arrayItems[ i + 1 ] ) ) );

    return items;
  }
}
