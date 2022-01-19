package com.backendless.rt.data;

import com.backendless.rt.RTEvent;

public enum ObjectEvents implements RTEvent
{
  created( "created" ),
  bulk_created( "bulk-created" ),
  updated( "updated" ),
  bulk_updated( "bulk-updated" ),
  upserted( "upserted" ),
  bulk_upserted( "bulk-upserted" ),
  deleted( "deleted" ),
  bulk_deleted( "bulk-deleted" );

  private final String eventName;

  private ObjectEvents( String eventName )
  {
    this.eventName = eventName;
  }

  @Override
  public String eventName()
  {
    return this.eventName;
  }

  public static ObjectEvents forName( String eventName )
  {
    for( ObjectEvents objectEvents : ObjectEvents.values() )
    {
      if( objectEvents.eventName().equals( eventName ) )
        return objectEvents;
    }

    throw new IllegalArgumentException( "There is no object event with name " + eventName );
  }
}
