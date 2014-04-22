package com.backendless.servercode.extension;

import com.backendless.DeviceRegistration;
import com.backendless.messaging.*;
import com.backendless.servercode.ExecutionResult;
import com.backendless.servercode.RunnerContext;

import java.util.List;

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

  public MessageStatus afterPublish( RunnerContext context, Object Message, PublishOptions publishOptions,
                                     DeliveryOptions deliveryOptions,
                                     ExecutionResult<MessageStatus> status ) throws Exception
  {
    return status.getResult();
  }

  public void beforeSubscribe( RunnerContext context, String subscriptionId,
                               SubscriptionOptions options ) throws Exception
  {
  }

  public String afterSubscribe( RunnerContext context, String subscriptionId, SubscriptionOptions options,
                                ExecutionResult<String> subscriberId ) throws Exception
  {
    return subscriberId.getResult();
  }

  public void beforePoll( RunnerContext context, String subscriptionId ) throws Exception
  {
  }

  public List<Message> afterPoll( RunnerContext context, String subscriptionId, ExecutionResult<List<Message>> messages ) throws Exception
  {
    return messages.getResult();
  }

  public void beforeCancel( RunnerContext context, String subscriptionId ) throws Exception
  {
  }

  public MessageStatus afterCancel( RunnerContext context, String subscriptionId,
                                    ExecutionResult<MessageStatus> status ) throws Exception
  {
    return status.getResult();
  }

  public void beforeDeviceRegistration( RunnerContext context, DeviceRegistration registrationDto ) throws Exception
  {
  }

  public String afterDeviceRegistration( RunnerContext context, DeviceRegistration registrationDto,
                                         ExecutionResult<String> registrationId ) throws Exception
  {
    return registrationId.getResult();
  }
}

