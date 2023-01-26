package com.backendless.hive;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;


@Getter
@ToString
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

  public static <T> LinkedHashSet<ScoreValuePair<T>> from( Object[] arrayItems )
  {
    if( arrayItems.length % 2 != 0 )
      throw new IllegalArgumentException( "Wrong length of incoming array for ScoreValuePair. It should contain even numbers of elements." );

    LinkedHashSet<ScoreValuePair<T>> items = new LinkedHashSet<>();
    for( int i = 0; i < arrayItems.length; i += 2 )
      items.add( new ScoreValuePair<>( ((Number) arrayItems[ i ]).doubleValue(), (T) arrayItems[ i + 1 ] ) );

    return items;
  }

  static <T> Object[] toObjectArray( Collection<ScoreValuePair<T>> items )
  {
    Object[] arrayItems = new Object[ items.size() * 2 ];
    int i = 0;
    for( ScoreValuePair<T> item : items )
    {
      arrayItems[ i * 2 ] = item.getScore();
      arrayItems[ i * 2 + 1 ] = HiveSerializer.serialize( item.getValue() );
      i++;
    }

    return arrayItems;
  }

  static <T> ArrayList<ScoreValuePair<T>> fromObjectArrayToList( Object[] arrayItems )
  {
    if( arrayItems.length % 2 != 0 )
      throw new IllegalArgumentException( "Wrong length of incoming array for ScoreValuePair. It should contain even numbers of elements." );

    ArrayList<ScoreValuePair<T>> items = new ArrayList<>();
    for( int i = 0; i < arrayItems.length; i += 2 )
      items.add( new ScoreValuePair<>( ((Number) arrayItems[ i ]).doubleValue(), HiveSerializer.deserialize( (String) arrayItems[ i + 1 ] ) ) );

    return items;
  }

  static <T> LinkedHashSet<ScoreValuePair<T>> fromObjectArray( Object[] arrayItems )
  {
    if( arrayItems.length % 2 != 0 )
      throw new IllegalArgumentException( "Wrong length of incoming array for ScoreValuePair. It should contain even numbers of elements." );

    LinkedHashSet<ScoreValuePair<T>> items = new LinkedHashSet<>();
    for( int i = 0; i < arrayItems.length; i += 2 )
      items.add( new ScoreValuePair<>( ((Number) arrayItems[ i ]).doubleValue(), HiveSerializer.deserialize( (String) arrayItems[ i + 1 ] ) ) );

    return items;
  }

  @Override
  public boolean equals( Object o )
  {
    if( this == o )
      return true;
    if( !(o instanceof ScoreValuePair) )
      return false;
    ScoreValuePair<?> that = (ScoreValuePair<?>) o;
    return Double.compare( that.score, score ) == 0 && value.equals( that.value );
  }

  @Override
  public int hashCode()
  {
    return Objects.hash( value );
  }
}
