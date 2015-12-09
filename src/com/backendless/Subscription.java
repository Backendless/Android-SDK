/*
 * ********************************************************************************************************************
 *  <p/>
 *  BACKENDLESS.COM CONFIDENTIAL
 *  <p/>
 *  ********************************************************************************************************************
 *  <p/>
 *  Copyright 2012 BACKENDLESS.COM. All Rights Reserved.
 *  <p/>
 *  NOTICE: All information contained herein is, and remains the property of Backendless.com and its suppliers,
 *  if any. The intellectual and technical concepts contained herein are proprietary to Backendless.com and its
 *  suppliers and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret
 *  or copyright law. Dissemination of this information or reproduction of this material is strictly forbidden
 *  unless prior written permission is obtained from Backendless.com.
 *  <p/>
 *  ********************************************************************************************************************
 */

package com.backendless;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.messaging.AndroidHandler;
import com.backendless.messaging.GenericMessagingHandler;
import com.backendless.messaging.IMessageHandler;
import com.backendless.messaging.Message;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@SuppressWarnings( "unchecked" )
public class Subscription
{
  private String subscriptionId;
  private String channelName;
  private String bcklsSubscriptionIdentity;

  private int pollingInterval = 1000;

  private IMessageHandler handler;
  private ScheduledFuture<?> currentTask;
  private ScheduledExecutorService executor;

  private AsyncCallback<List<Message>> pushSubscriptionCallback;

  public Subscription()
  {
  }

  protected Subscription( final AsyncCallback<List<Message>> responder )
  {
    this.pushSubscriptionCallback = responder;
  }

  public Subscription( int pollingInterval )
  {
    this.pollingInterval = pollingInterval;
  }

  public String getSubscriptionId()
  {
    return subscriptionId;
  }

  protected synchronized void setSubscriptionId( String subscriptionId )
  {
    this.subscriptionId = subscriptionId;
  }

  public String getChannelName()
  {
    return channelName;
  }

  protected synchronized void setChannelName( String channelName )
  {
    this.channelName = channelName;
  }

  protected String getBcklsSubscriptionIdentity()
  {
    return bcklsSubscriptionIdentity;
  }

  protected synchronized void setBcklsSubscriptionIdentity( final String identity )
  {
    this.bcklsSubscriptionIdentity = identity;
  }

  public int getPollingInterval()
  {
    return pollingInterval;
  }

  public synchronized void setPollingInterval( int pollingInterval )
  {
    this.pollingInterval = pollingInterval;
  }

  protected void handlePushMessage( final List<Message> messages )
  {
    pushSubscriptionCallback.handleResponse( messages );
  }

  public synchronized boolean cancelSubscription()
  {
    if ( pushSubscriptionCallback != null )
    {
      // TODO: remove subscription from server

      Backendless.Messaging.removeSubscriptionCallback( bcklsSubscriptionIdentity );
    }

    if( currentTask != null )
    {
      currentTask.cancel( true );
      currentTask = null;
    }

    handler = null;
    subscriptionId = null;

    return true;
  }

  protected synchronized void onSubscribe( final AsyncCallback<List<Message>> subscriptionResponder )
  {
    executor = Executors.newSingleThreadScheduledExecutor( ThreadFactoryService.getThreadFactory() );
    handler = Backendless.isAndroid() ? new AndroidHandler( subscriptionResponder, this ) : new GenericMessagingHandler( subscriptionResponder, this );
    executor.scheduleWithFixedDelay( handler.getSubscriptionThread(), 0, pollingInterval, TimeUnit.MILLISECONDS );
  }

  public synchronized void pauseSubscription()
  {
    // TODO: implement pause logic for pubsub through push

    if( executor == null || executor.isShutdown() )
      return;

    executor.shutdown();
  }

  public synchronized void resumeSubscription()
  {
    // TODO: implement resume logic for pubsub through push

    Runnable subscriptionThread = handler.getSubscriptionThread();

    if( subscriptionId == null || channelName == null || handler == null || subscriptionThread == null )
      throw new IllegalStateException( ExceptionMessage.WRONG_SUBSCRIPTION_STATE );

    if( (executor == null || executor.isShutdown()) && subscriptionThread != null )
    {
      executor = Executors.newSingleThreadScheduledExecutor( ThreadFactoryService.getThreadFactory() );
      executor.scheduleWithFixedDelay( subscriptionThread, 0, pollingInterval, TimeUnit.MILLISECONDS );
    }
  }
}