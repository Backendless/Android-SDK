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
      throw createChannelNullException();

    final MessagingSubscription messagingSubscription = new MessagingSubscription( SubscriptionNames.PUB_SUB_CONNECT, callback );
    messagingSubscription.putOption( "channel", channel );

    return messagingSubscription;
  }

  private static IllegalArgumentException createChannelNullException()
  {
    return new IllegalArgumentException( "channel can't be null or empty" );
  }

  public static MessagingSubscription subscribe( String channel, RTCallback callback )
  {
    if(channel == null || channel.isEmpty())
      throw createChannelNullException();

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

  public static MessagingSubscription command( String channel,  RTCallback callback )
  {
    if(channel == null || channel.isEmpty())
      throw createChannelNullException();

    final MessagingSubscription messagingSubscription = new MessagingSubscription( SubscriptionNames.PUB_SUB_COMMANDS, callback );
    messagingSubscription.putOption( "channel", channel );

    return messagingSubscription;
  }

  public static MessagingSubscription userStatus( String channel,  RTCallback callback )
  {
    if(channel == null || channel.isEmpty())
      throw createChannelNullException();

    final MessagingSubscription messagingSubscription = new MessagingSubscription( SubscriptionNames.PUB_SUB_USERS, callback );
    messagingSubscription.putOption( "channel", channel );

    return messagingSubscription;
  }

  String getSelector()
  {
    return (String) getOption( "selector" );
  }

}
