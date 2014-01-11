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

package com.backendless.tests.junit.unitTests.messagingService.syncTests;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.*;
import com.backendless.tests.junit.unitTests.persistenceService.entities.AndroidPerson;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PublishTest extends TestsFrame implements AsyncCallback<List<Message>>
{
  public void handleResponse( List<Message> messages )
  {
    if( messageStatus == null )
      return;

    Assert.assertNotNull( "Server returned a null object instead of messages list", messages );

    for( Message resultMessage : messages )
    {
      if( resultMessage.getMessageId().startsWith( messageStatus.getMessageId() ) )
      {
        try
        {
          if( message instanceof AndroidPerson[] )
          {
            Assert.assertTrue( "Server returned wrong object type", resultMessage.getData() instanceof AndroidPerson[] );

            for( AndroidPerson androidPerson : (AndroidPerson[]) message )
            {
              Assert.assertTrue( "Server return didn't contain expected object", Arrays.asList( (AndroidPerson[]) resultMessage.getData() ).contains( androidPerson ) );
            }
          }
          else
          {
            Assert.assertEquals( "Server returned a message with a wrong message data", message, resultMessage.getData() );
          }

          if( publisher != null )
          {
            Assert.assertEquals( "Server returned a message with a wrong publisher", publisher, resultMessage.getPublisherId() );
          }

          if( headers != null )
          {
            Assert.assertTrue( "Server returned a message with wrong headers", resultMessage.getHeaders().containsKey( headersEntry.getKey() ) );
            Assert.assertEquals( "Server returned a message with wrong headers", headersEntry.getValue(), resultMessage.getHeaders().get( headersEntry.getKey() ) );
          }
        }
        catch( Throwable t )
        {
          failCountDownWith( t );
        }

        latch.countDown();
      }
    }
  }

  public void handleFault( BackendlessFault fault )
  {
    failCountDownWith( fault );
  }

  @Test
  public void testBasicMessagePublish() throws Throwable
  {
    setLatch();
    setMessage();

    subscription = Backendless.Messaging.subscribe( TEST_CHANNEL, this );
    messageStatus = Backendless.Messaging.publish( TEST_CHANNEL, message );

    Assert.assertNotNull( "Server didn't set a messageId for the message", messageStatus.getMessageId() );
    Assert.assertTrue( "Message status was not set as published", messageStatus.getStatus().equals( PublishStatusEnum.PUBLISHED ) );

    await();
    checkResult();
  }

  @Test
  public void testMessagePublishWithOptions() throws Throwable
  {
    setLatch();
    setMessage();
    setPublisher();

    subscription = Backendless.Messaging.subscribe( TEST_CHANNEL, this );

    PublishOptions publishOptions = new PublishOptions();
    publishOptions.setPublisherId( publisher );

    messageStatus = Backendless.Messaging.publish( TEST_CHANNEL, message, publishOptions );

    Assert.assertNotNull( "Server didn't set a messageId for the message", messageStatus.getMessageId() );
    Assert.assertTrue( "Message status was not set as published", messageStatus.getStatus().equals( PublishStatusEnum.PUBLISHED ) );

    await();
    checkResult();
  }

  @Test
  public void testMessagePublishWithHeaders() throws Throwable
  {
    setLatch();
    setMessage();
    setHeaders();

    subscription = Backendless.Messaging.subscribe( TEST_CHANNEL, this );

    PublishOptions publishOptions = new PublishOptions();
    publishOptions.setHeaders( headers );

    messageStatus = Backendless.Messaging.publish( TEST_CHANNEL, message, publishOptions );

    Assert.assertNotNull( "Server didn't set a messageId for the message", messageStatus.getMessageId() );
    Assert.assertTrue( "Message status was not set as published", messageStatus.getStatus().equals( PublishStatusEnum.PUBLISHED ) );

    await();
    checkResult();
  }

  @Test
  public void testMessagePublishWithHeadersAndSubtopics() throws Throwable
  {
    setLatch();
    setMessage();
    setHeaders();

    String subtopic = "foo.bar";

    SubscriptionOptions subscriptionOptions = new SubscriptionOptions();
    subscriptionOptions.setSubtopic( subtopic );

    subscription = Backendless.Messaging.subscribe( TEST_CHANNEL, new AsyncCallback<List<Message>>()
    {
      public void handleResponse( List<Message> messages )
      {
        try
        {
          Assert.assertNotNull( "Server returned a null object instead of messages list", messages );

          for( Message resultMessage : messages )
          {
            if( resultMessage.getMessageId().equals( messageStatus.getMessageId() ) )
            {

              Assert.assertEquals( "Server returned a message with a wrong message data", message, resultMessage.getData() );
              Assert.assertTrue( "Server returned a message with wrong headers", resultMessage.getHeaders().containsKey( headersEntry.getKey() ) );
              Assert.assertEquals( "Server returned a message with wrong headers", headersEntry.getValue(), resultMessage.getHeaders().get( headersEntry.getKey() ) );

              latch.countDown();
            }
          }
        }
        catch( Throwable t )
        {
          failCountDownWith( t );
        }
      }

      public void handleFault( BackendlessFault fault )
      {
        failCountDownWith( fault );
      }
    }, subscriptionOptions );

    PublishOptions publishOptions = new PublishOptions();
    publishOptions.setHeaders( headers );
    publishOptions.setSubtopic( subtopic );

    messageStatus = Backendless.Messaging.publish( TEST_CHANNEL, message, publishOptions );

    Assert.assertNotNull( "Server didn't set a messageId for the message", messageStatus.getMessageId() );
    Assert.assertTrue( "Message status was not set as published", messageStatus.getStatus().equals( PublishStatusEnum.PUBLISHED ) );

    await();
    checkResult();
  }

  @Test
  public void testMessagePublishPrimitiveValue() throws Throwable
  {
    setLatch();
    setMessage( 16 );

    subscription = Backendless.Messaging.subscribe( TEST_CHANNEL, this );
    messageStatus = Backendless.Messaging.publish( TEST_CHANNEL, message );

    Assert.assertNotNull( "Server didn't set a messageId for the message", messageStatus.getMessageId() );
    Assert.assertTrue( "Message status was not set as published", messageStatus.getStatus().equals( PublishStatusEnum.PUBLISHED ) );

    await();
    checkResult();
  }

  @Test
  public void testMessagePublishStringValue() throws Throwable
  {
    setLatch();
    setMessage( "foomessage" );

    subscription = Backendless.Messaging.subscribe( TEST_CHANNEL, this );
    messageStatus = Backendless.Messaging.publish( TEST_CHANNEL, message );

    Assert.assertNotNull( "Server didn't set a messageId for the message", messageStatus.getMessageId() );
    Assert.assertTrue( "Message status was not set as published", messageStatus.getStatus().equals( PublishStatusEnum.PUBLISHED ) );

    await();
    checkResult();
  }

  @Test
  public void testMessagePublishDateValue() throws Throwable

  {
    setLatch();
    setMessage( new Date() );

    subscription = Backendless.Messaging.subscribe( TEST_CHANNEL, this );
    messageStatus = Backendless.Messaging.publish( TEST_CHANNEL, message );

    Assert.assertNotNull( "Server didn't set a messageId for the message", messageStatus.getMessageId() );
    Assert.assertTrue( "Message status was not set as published", messageStatus.getStatus().equals( PublishStatusEnum.PUBLISHED ) );

    await();
    checkResult();
  }

  @Test
  public void testMessagePublishBoolValue() throws Throwable
  {
    setLatch();
    setMessage( true );

    subscription = Backendless.Messaging.subscribe( TEST_CHANNEL, this );
    messageStatus = Backendless.Messaging.publish( TEST_CHANNEL, message );

    Assert.assertNotNull( "Server didn't set a messageId for the message", messageStatus.getMessageId() );
    Assert.assertTrue( "Message status was not set as published", messageStatus.getStatus().equals( PublishStatusEnum.PUBLISHED ) );

    await();
    checkResult();
  }

  @Test
  public void testMessagePublishPOJOValue() throws Throwable
  {
    setLatch();

    AndroidPerson androidPerson = new AndroidPerson();
    androidPerson.setAge( 22 );
    androidPerson.setName( "Vasya" );

    setMessage( androidPerson );

    subscription = Backendless.Messaging.subscribe( TEST_CHANNEL, this );
    messageStatus = Backendless.Messaging.publish( TEST_CHANNEL, message );

    Assert.assertNotNull( "Server didn't set a messageId for the message", messageStatus.getMessageId() );
    Assert.assertTrue( "Message status was not set as published", messageStatus.getStatus().equals( PublishStatusEnum.PUBLISHED ) );

    await();
    checkResult();
  }

  @Test
  public void testMessagePublishArrayOfPOJOValue() throws Throwable
  {
    setLatch();

    AndroidPerson androidPerson123451 = new AndroidPerson();
    androidPerson123451.setAge( 22 );
    androidPerson123451.setName( "Vasya" );

    AndroidPerson androidPerson123452 = new AndroidPerson();
    androidPerson123452.setAge( 35 );
    androidPerson123452.setName( "Petya" );

    setMessage( new AndroidPerson[] { androidPerson123451, androidPerson123452 } );

    subscription = Backendless.Messaging.subscribe( TEST_CHANNEL, this );
    messageStatus = Backendless.Messaging.publish( TEST_CHANNEL, message );

    Assert.assertNotNull( "Server didn't set a messageId for the message", messageStatus.getMessageId() );
    Assert.assertTrue( "Message status was not set as published", messageStatus.getStatus().equals( PublishStatusEnum.PUBLISHED ) );

    await();
    checkResult();
  }

  @Test
  public void testMessagePublishSinglecast() throws Throwable
  {
    setLatch();
    setMessage();

    subscription = Backendless.Messaging.subscribe( TEST_CHANNEL, this );

    DeliveryOptions deliveryOptions = new DeliveryOptions();
    deliveryOptions.setPushSinglecast( Arrays.asList( deviceRegistrationId ) );
    messageStatus = Backendless.Messaging.publish( TEST_CHANNEL, message, null, deliveryOptions );

    Assert.assertNotNull( "Server didn't set a messageId for the message", messageStatus.getMessageId() );
    Assert.assertTrue( "Message status was not set as published", messageStatus.getStatus().equals( PublishStatusEnum.PUBLISHED ) );

    await();
    checkResult();
  }

  @Test
  public void testMessagesScheduling() throws Throwable
  {
    setLatch( 5 );
    setMessage();

    String channel = "channel" + getRandomStringMessage();

    subscription = Backendless.Messaging.subscribe( channel, this );

    DeliveryOptions deliveryOptions = new DeliveryOptions();
    deliveryOptions.setRepeatEvery( 2 );
    messageStatus = Backendless.Messaging.publish( channel, message, null, deliveryOptions );

    Assert.assertNotNull( "Server didn't set a messageId for the message", messageStatus.getMessageId() );
    Assert.assertTrue( "Message status was not set as published", messageStatus.getStatus().equals( PublishStatusEnum.SCHEDULED ) );

    await();
    checkResult();
  }

  @Test
  public void testMessagesScheduleCancel() throws Throwable
  {
    setLatch( 1 );
    setMessage();

    subscription = Backendless.Messaging.subscribe( TEST_CHANNEL, new AsyncCallback<List<Message>>()
    {
      public void handleResponse( List<Message> messages )
      {
        for( Message msg : messages )
        {
          if( msg.getMessageId().contains( messageStatus.getMessageId() ) )
            try
            {
              Assert.assertTrue( "Server returned a wrong result status", Backendless.Messaging.cancel( messageStatus.getMessageId().toString() ) );
              latch.countDown();
            }
            catch( Throwable t )
            {
              failCountDownWith( t );
            }
        }
      }

      public void handleFault( BackendlessFault fault )
      {
        failCountDownWith( fault );
      }
    } );

    DeliveryOptions deliveryOptions = new DeliveryOptions();
    Calendar calendar = Calendar.getInstance();
    calendar.roll( Calendar.DAY_OF_MONTH, true );
    deliveryOptions.setRepeatExpiresAt( calendar.getTime() );
    deliveryOptions.setRepeatEvery( 30 );
    messageStatus = Backendless.Messaging.publish( TEST_CHANNEL, message, null, deliveryOptions );

    Assert.assertNotNull( "Server didn't set a messageId for the message", messageStatus.getMessageId() );
    Assert.assertTrue( "Message status was not set as published", messageStatus.getStatus().equals( PublishStatusEnum.SCHEDULED ) );

    await();
    checkResult();
  }
}
