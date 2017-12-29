package com.backendless.rt.messaging;

import com.backendless.rt.AbstractListenerFactory;

public class ChannelFactory extends AbstractListenerFactory<Channel>
{
  public Channel create( final String channel )
  {
     return create( channel, new Provider<Channel>()
     {
       @Override
       public Channel create()
       {
         return new ChannelImpl( channel );
       }
     });
  }
}
