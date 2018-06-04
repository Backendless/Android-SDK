package com.backendless.rt.rso;

import com.backendless.rt.RTCallback;
import com.backendless.rt.RTSubscription;
import com.backendless.rt.SubscriptionNames;

class RSOSubscription extends RTSubscription
{

  private RSOSubscription( String name, SubscriptionNames subscriptionName, RTCallback callback )
  {
    super( subscriptionName, callback );
    
    if(name == null || name.isEmpty())
      throw new IllegalArgumentException( "name can't be or empty" );
    
    putOption( "name", name );
  }

  public static RSOSubscription connect( String name, RTCallback callback )
  {
    return new RSOSubscription( name, SubscriptionNames.RSO_CONNECT, callback );
  }

  public static RSOSubscription changes( String name, RTCallback callback )
  {
    return new RSOSubscription( name, SubscriptionNames.RSO_CHANGES, callback );
  }

  public static RSOSubscription command( String name, RTCallback callback )
  {
    return new RSOSubscription( name, SubscriptionNames.RSO_COMMANDS, callback );
  }

  public static RSOSubscription userStatus( String name, RTCallback callback )
  {
    return new RSOSubscription( name, SubscriptionNames.RSO_USERS, callback );
  }

  public static RSOSubscription cleared( String name, RTCallback callback )
  {
    return new RSOSubscription( name, SubscriptionNames.RSO_CLEARED, callback );
  }

  public static RSOSubscription invoke( String name, RTCallback callback )
  {
    return new RSOSubscription( name, SubscriptionNames.RSO_INVOKE, callback );
  }

}
