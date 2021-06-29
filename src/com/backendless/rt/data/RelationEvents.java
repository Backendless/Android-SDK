package com.backendless.rt.data;

import com.backendless.rt.RTEvent;

public enum RelationEvents implements RTEvent
{
  relations_added( "add" ),
  relations_set( "set" ),
  relations_removed( "delete" );

  private final String eventName;

  private RelationEvents( String eventName )
  {
    this.eventName = eventName;
  }

  @Override
  public String eventName()
  {
    return this.eventName;
  }

  public static RelationEvents forName( String eventName )
  {
    for( RelationEvents relationEvents : RelationEvents.values() )
    {
      if( relationEvents.eventName().equals( eventName ) )
        return relationEvents;
    }

    throw new IllegalArgumentException( "There is no relation event with name " + eventName );
  }
}
