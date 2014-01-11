package com.backendless.tests.junit.unitTests.messagingService.syncTests;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.Message;
import com.backendless.messaging.PublishOptions;
import com.backendless.messaging.PublishStatusEnum;
import com.backendless.messaging.SubscriptionOptions;
import org.junit.Assert;
import org.junit.Test;

import java.util.Hashtable;
import java.util.List;

public class SubscriptionTest extends TestsFrame
{
  @Test
  public void testBasicMessageSubscription() throws Throwable
  {
    setLatch();
    setMessage();

    subscription = Backendless.Messaging.subscribe( TEST_CHANNEL, new AsyncCallback<List<Message>>()
    {
      public void handleResponse( List<Message> messages )
      {
        try
        {
          Assert.assertNotNull( "Server returned a null object instead of messages list", messages );
          Assert.assertFalse( "Server returned an empty messages list", messages.isEmpty() );

          for( Message resultMessage : messages )
          {
            if( resultMessage.getMessageId().equals( messageStatus.getMessageId() ) )
            {
              Assert.assertEquals( "Server returned a message with a wrong message data", message, resultMessage.getData() );
              Assert.assertEquals( "Server returned a message with a wrong messageId", messageStatus.getMessageId(), resultMessage.getMessageId() );

              latch.countDown();
            }
          }
        }
        catch( Exception e )
        {
          failCountDownWith( e );
        }
      }

      public void handleFault( BackendlessFault fault )
      {
        failCountDownWith( fault );
      }
    } );

    messageStatus = Backendless.Messaging.publish( TEST_CHANNEL, message );

    Assert.assertNotNull( "Server didn't set a messageId for the message", messageStatus.getMessageId() );
    Assert.assertTrue( "Message status was not set as published", messageStatus.getStatus().equals( PublishStatusEnum.PUBLISHED ) );

    await();
    checkResult();
  }

  @Test
  public void testBasicMessageSubscriptionWithSubtopic() throws Throwable
  {
    setLatch();
    setMessage();

    String subtopic = "sub" + random.nextInt();

    SubscriptionOptions subscriptionOptions = new SubscriptionOptions();
    subscriptionOptions.setSubtopic( subtopic );

    subscription = Backendless.Messaging.subscribe( TEST_CHANNEL, new AsyncCallback<List<Message>>()
    {
      public void handleResponse( List<Message> messages )
      {
        try
        {
          Assert.assertNotNull( "Server returned a null object instead of messages list", messages );
          Assert.assertFalse( "Server returned an empty messages list", messages.isEmpty() );

          for( Message resultMessage : messages )
          {
            if( resultMessage.getMessageId().equals( messageStatus.getMessageId() ) )
            {
              Assert.assertEquals( "Server returned a message with a wrong message data", message, resultMessage.getData() );
              Assert.assertEquals( "Server returned a message with a wrong messageId", messageStatus.getMessageId(), resultMessage.getMessageId() );

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
    publishOptions.setSubtopic( subtopic );

    messageStatus = Backendless.Messaging.publish( TEST_CHANNEL, message, publishOptions );

    Assert.assertNotNull( "Server didn't set a messageId for the message", messageStatus.getMessageId() );
    Assert.assertTrue( "Message status was not set as published", messageStatus.getStatus().equals( PublishStatusEnum.PUBLISHED ) );

    await();
    checkResult();
  }

  @Test
  public void testBasicMessageSubscriptionWithSelector() throws Throwable
  {
    setLatch();
    setMessage();

    int i = random.nextInt();
    final String headerKey = "header_" + (i < 0 ? -i : i);
    final String headerValue = "someValue";
    headers = new Hashtable<String, String>();
    headers.put( headerKey, headerValue );

    SubscriptionOptions subscriptionOptions = new SubscriptionOptions();
    subscriptionOptions.setSelector( headerKey + "='" + headerValue + "'" );

    subscription = Backendless.Messaging.subscribe( TEST_CHANNEL, new AsyncCallback<List<Message>>()
    {
      public void handleResponse( List<Message> messages )
      {
        try
        {
          Assert.assertNotNull( "Server returned a null object instead of messages list", messages );
          Assert.assertFalse( "Server returned an empty messages list", messages.isEmpty() );

          for( Message resultMessage : messages )
          {
            if( resultMessage.getMessageId().equals( messageStatus.getMessageId() ) )
            {
              Assert.assertEquals( "Server returned a message with a wrong message data", message, resultMessage.getData() );

              Assert.assertTrue( "Server returned a message with wrong headers", resultMessage.getHeaders().containsKey( headerKey ) );

              Assert.assertEquals( "Server returned a message with wrong headers", headerValue, resultMessage.getHeaders().get( headerKey ) );

              Assert.assertEquals( "Server returned a message with a wrong messageId", messageStatus.getMessageId(), resultMessage.getMessageId() );

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
        logTestFailed( fault );
      }
    }, subscriptionOptions );

    PublishOptions publishOptions = new PublishOptions();
    publishOptions.setHeaders( headers );

    messageStatus = Backendless.Messaging.publish( TEST_CHANNEL, message, publishOptions );

    Assert.assertNotNull( "Server didn't set a messageId for the message", messageStatus.getMessageId() );
    Assert.assertTrue( "Message status was not set as published", messageStatus.getStatus().equals( PublishStatusEnum.PUBLISHED ) );

    await();
    checkResult();
  }

  @Test
  public void testMessageSubscriptionForUnknownChannel()
  {
    try
    {
      Backendless.Messaging.subscribe( getRandomStringMessage(), null );
    }
    catch( Throwable e )
    {
      checkErrorCode( 5010, e );
    }
  }
}
