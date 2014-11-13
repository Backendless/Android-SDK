package com.backendless.servercode.extension;

import com.backendless.DeviceRegistration;
import com.backendless.messaging.*;
import com.backendless.servercode.ExecutionResult;
import com.backendless.servercode.RunnerContext;

/**
 * Created with IntelliJ IDEA.
 * User: ivanlappo
 * Date: 5/20/13
 * Time: 12:40 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MessagingExtender
{
  public MessagingExtender()
  {
  }

  public void beforePublish( RunnerContext context, Object Message, PublishOptions publishOptions,
                             DeliveryOptions deliveryOptions ) throws Exception
  {
  }

  public void afterPublish( RunnerContext context, Object Message, PublishOptions publishOptions,
                            DeliveryOptions deliveryOptions,
                                     ExecutionResult<MessageStatus> status ) throws Exception
  {
  }

  public void beforeSubscribe( RunnerContext context,
                               String subscriptionId,
                               String channel,
                               SubscriptionOptions options ) throws Exception
  {
  }

  public void afterSubscribe( RunnerContext context, String subscriptionId,
                                String channel,
                                SubscriptionOptions options,
                                ExecutionResult<String> subscriberId ) throws Exception
  {
  }

  public void beforePoll( RunnerContext context, String subscriptionId ) throws Exception
  {
  }

  public void afterPoll( RunnerContext context, String subscriptionId,
                         ExecutionResult<Message[]> messages ) throws Exception
  {
  }

  public void beforeCancel( RunnerContext context, String subscriptionId ) throws Exception
  {
  }

  public void afterCancel( RunnerContext context, String subscriptionId, ExecutionResult<MessageStatus> status ) throws Exception
  {
  }

  public void beforeDeviceRegistration( RunnerContext context, DeviceRegistration registrationDto ) throws Exception
  {
  }

  public void afterDeviceRegistration( RunnerContext context, DeviceRegistration registrationDto,
                                       ExecutionResult<String> registrationId ) throws Exception
  {
  }
}

