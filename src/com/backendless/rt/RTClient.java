package com.backendless.rt;

public interface RTClient
{
  void subscribe( RTSubscription subscription );

  void unsubscribe( String subscriptionId );

  void userLoggedIn( String userToken );

  void userLoggedOut();

  void invoke( RTMethodRequest methodRequest );
}
