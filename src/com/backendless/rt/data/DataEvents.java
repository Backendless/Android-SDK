package com.backendless.rt.data;

import com.backendless.rt.RTEvent;

public enum DataEvents implements RTEvent
{
  created( "created" ),
  bulk_created( "bulk-created" ),
  updated( "updated" ),
  bulk_updated( "bulk-updated" ),
  deleted( "deleted" ),
  bulk_deleted( "bulk-deleted" ),
  relations_added( "add" ),
  relations_set( "set" ),
  relations_removed( "delete" );

  private final String eventName;

  private DataEvents( String eventName )
  {
    this.eventName = eventName;
  }

  @Override
  public String eventName()
  {
    return this.eventName;
  }

  public static DataEvents forName( String eventName )
  {
    for( DataEvents dataEvents : DataEvents.values() )
    {
      if( dataEvents.eventName().equals( eventName ) )
        return dataEvents;
    }

    throw new IllegalArgumentException( "There is no event with name " + eventName );
  }
}
