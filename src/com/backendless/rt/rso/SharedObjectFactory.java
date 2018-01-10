package com.backendless.rt.rso;

import com.backendless.rt.AbstractListenerFactory;
import com.backendless.rt.messaging.Channel;
import com.backendless.rt.messaging.ChannelImpl;

public class SharedObjectFactory extends AbstractListenerFactory<SharedObject>
{
  public SharedObject get( final String name )
  {
     return create( name, new Provider<SharedObject>()
     {
       @Override
       public SharedObject create()
       {
         return new SharedObjectImpl( name );
       }
     });
  }
}
