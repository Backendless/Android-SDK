package com.backendless.rt.messaging;

import com.backendless.rt.AbstractListenerFactory;

public class MessagingListenerFactory extends AbstractListenerFactory<MessagingListener>
{
  public MessagingListener create( final String channel )
  {
     return create( channel, new Provider<MessagingListener>()
     {
       @Override
       public MessagingListener create()
       {
         return new MessagingListenerImpl( channel );
       }
     });
  }
}
