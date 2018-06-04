package com.backendless.rt.data;

import com.backendless.rt.AbstractListenerFactory;

import java.util.Map;

public class EventHandlerFactory extends AbstractListenerFactory<EventHandler>
{
  @SuppressWarnings( "unchecked" )
  public <T> EventHandler<T> of( final Class<T> entity )
  {
    return create( entity.getName(), new Provider()
    {
      @Override
      public EventHandlerImpl create()
      {
        return new EventHandlerImpl( entity );
      }
    } );
  }

  @SuppressWarnings( "unchecked" )
  public EventHandler<Map> of( final String tableName )
  {
    return create( tableName, new Provider()
    {
      @Override
      public EventHandlerImpl create()
      {
        return new EventHandlerImpl( tableName );
      }
    } );
  }
}
