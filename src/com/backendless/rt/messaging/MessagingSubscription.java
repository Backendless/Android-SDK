package com.backendless.rt.messaging;

import com.backendless.rt.RTCallback;
import com.backendless.rt.RTSubscription;
import com.backendless.rt.SubscriptionNames;

public class MessagingSubscription extends RTSubscription
{

  protected MessagingSubscription( SubscriptionNames subscriptionName, RTCallback callback )
  {
    super( subscriptionName, callback );
  }

  public static MessagingSubscription connect( String channel, RTCallback callback )
  {
    if(channel == null || channel.isEmpty())
      throw new IllegalArgumentException( "channel can't be or empty" );

    final MessagingSubscription messagingSubscription = new MessagingSubscription( SubscriptionNames.PUB_SUB_CONNECT, callback );
    messagingSubscription.putOption( "channel", channel );

    return messagingSubscription;
  }

  public static MessagingSubscription subscribe( String channel, RTCallback callback )
  {
    if(channel == null || channel.isEmpty())
      throw new IllegalArgumentException( "channel can't be or empty" );

    final MessagingSubscription messagingSubscription = new MessagingSubscription( SubscriptionNames.PUB_SUB_MESSAGES, callback );
    messagingSubscription.putOption( "channel", channel );

    return messagingSubscription;
  }

  public static MessagingSubscription subscribe( String channel, String selector, RTCallback callback )
  {
    final MessagingSubscription messagingSubscription = subscribe( channel, callback );
    messagingSubscription.putOption( "selector", selector );
    return messagingSubscription;
  }

}
