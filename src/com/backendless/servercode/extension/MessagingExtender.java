package com.backendless.servercode.extension;

import com.backendless.DeviceRegistration;
import com.backendless.messaging.BodyParts;
import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.Message;
import com.backendless.messaging.MessageStatus;
import com.backendless.messaging.PublishOptions;
import com.backendless.messaging.SubscriptionOptions;
import com.backendless.servercode.ExecutionResult;
import com.backendless.servercode.RunnerContext;

import java.util.List;


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

  public void beforeSubscribe( RunnerContext context, String channel, SubscriptionOptions options ) throws Exception
  {
  }

  public void afterSubscribe( RunnerContext context, String channel, SubscriptionOptions options, ExecutionResult<String> subscriberId ) throws Exception
  {
  }

  public void beforePoll( RunnerContext context ) throws Exception
  {
  }

  public void afterPoll( RunnerContext context, ExecutionResult<List<Message>> messages ) throws Exception
  {
  }

  public void beforeCancel( RunnerContext context, String messageId ) throws Exception
  {
  }

  public void afterCancel( RunnerContext context, String messageId, ExecutionResult<MessageStatus> status ) throws Exception
  {
  }

  public void beforePush( RunnerContext context, String templateDescription ) throws Exception
  {
  }

  public void afterPush( RunnerContext context, String templateDescription, ExecutionResult<MessageStatus> status ) throws Exception
  {
  }

  public void beforePushWithTemplate( RunnerContext context, String templateName ) throws Exception
  {
  }

  public void afterPushWithTemplate( RunnerContext context, String templateName, ExecutionResult<MessageStatus> status ) throws Exception
  {
  }

  public void beforeDeviceRegistration( RunnerContext context, DeviceRegistration registrationDto ) throws Exception
  {
  }

  public void afterDeviceRegistration( RunnerContext context, DeviceRegistration registrationDto,
                                       ExecutionResult<String> registrationId ) throws Exception
  {
  }

  public void beforeSendEmail(RunnerContext context, String subject, BodyParts bodyParts, List<String> recipients, List<String> attachments ) throws Exception
  {
  }

  public void afterSendEmail( RunnerContext context, String subject, BodyParts bodyParts, List<String> recipients, List<String> attachments, ExecutionResult<MessageStatus> msgStatus ) throws Exception
  {
  }

  public void beforeGetMessageStatus( RunnerContext context, String messageId ) throws Exception
  {
  }

  public void afterGetMessageStatus( RunnerContext context, String messageId, ExecutionResult<MessageStatus> msgStatus ) throws Exception
  {
  }
}

