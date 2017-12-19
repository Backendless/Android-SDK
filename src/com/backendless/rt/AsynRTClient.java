package com.backendless.rt;

import com.backendless.ThreadPoolService;

class AsynRTClient implements RTClient
{
  private static final RTClient rtClient = new RTClientSocketIO();

  @Override
  public void subscribe( final RTSubscription subscription )
  {
    ThreadPoolService.getPoolExecutor().execute( new Runnable()
    {
      @Override
      public void run()
      {
        rtClient.subscribe( subscription );
      }
    } );
  }

  @Override
  public void unsubscribe( final String subscriptionId )
  {
    ThreadPoolService.getPoolExecutor().execute( new Runnable()
    {
      @Override
      public void run()
      {
        rtClient.unsubscribe( subscriptionId );
      }
    } );
  }

  @Override
  public void userLoggedIn( final String userToken )
  {
    ThreadPoolService.getPoolExecutor().execute( new Runnable()
    {
      @Override
      public void run()
      {
        rtClient.userLoggedIn( userToken );
      }
    } );
  }

  @Override
  public void userLoggedOut()
  {
    ThreadPoolService.getPoolExecutor().execute( new Runnable()
    {
      @Override
      public void run()
      {
        rtClient.userLoggedOut();
      }
    } );
  }

  @Override
  public void invoke( final RTMethodRequest methodRequest )
  {
    ThreadPoolService.getPoolExecutor().execute( new Runnable()
    {
      @Override
      public void run()
      {
        rtClient.invoke( methodRequest );
      }
    } );
  }
}
