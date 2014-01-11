package com.backendless.tests.junit.unitTests.messagingService.syncTests;

import com.backendless.Backendless;
import com.backendless.Subscription;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.MessageStatus;
import com.backendless.tests.junit.Defaults;
import com.backendless.tests.junit.ITest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TestsFrame extends ITest
{
  public final static String TEST_CHANNEL = "TestChannel";
  public final static String TEST_CHANNEL_2 = "TestChannel2";
  public final static String SENDER_ID = "386814968753";

  public static String deviceRegistrationId;
  public CountDownLatch latch;
  public MessageStatus messageStatus;
  public Object message;
  public String publisher;
  public Hashtable<String, String> headers;
  public Map.Entry<String, String> headersEntry;
  public Subscription subscription;

  public String testResult;

  public Random random = new Random();

  public void setTestResult( Throwable t )
  {
    testResult = t.getMessage();
  }

  public void setTestResult( String result )
  {
    testResult = result;
  }

  public void checkResult()
  {
    if( testResult != null )
      logTestFailed( testResult );
  }

  public void setLatch()
  {
    setLatch( 1 );
  }

  public void setLatch( int count )
  {
    latch = new CountDownLatch( count );
  }

  public void failCountDownWith( Throwable e )
  {
    setTestResult( e );
    countDownAll();
  }

  public void failCountDownWith( BackendlessFault fault )
  {
    setTestResult( fault.toString() );
    countDownAll();
  }

  private void countDownAll()
  {
    for( int i = 0; i < latch.getCount(); i++ )
      latch.countDown();
  }

  public void setMessage()
  {
    message = getRandomStringMessage();
  }

  public void setMessage( Object value )
  {
    message = value;
  }

  public void setPublisher()
  {
    publisher = getRandomPublisher();
  }

  public void setHeaders()
  {
    headersEntry = getRandomHeaders();
    headers = new Hashtable<String, String>();
    headers.put( headersEntry.getKey(), headersEntry.getValue() );
  }

  public void await() throws Exception
  {
    Assert.assertTrue( "Server didn't receive a message in time.", latch.await( 2, TimeUnit.MINUTES ) );
  }

  public String getRandomStringMessage()
  {
    String timestamp = new java.sql.Timestamp( Calendar.getInstance().getTime().getTime() ).toString().replaceAll( "\\D*", "" );

    return "message#" + timestamp;
  }

  public String getRandomPublisher()
  {
    String timestamp = new java.sql.Timestamp( Calendar.getInstance().getTime().getTime() ).toString().replaceAll( "\\D*", "" );

    return "publisher#" + timestamp;
  }

  public Map.Entry<String, String> getRandomHeaders()
  {
    String timestamp = new java.sql.Timestamp( Calendar.getInstance().getTime().getTime() ).toString().replaceAll( "\\D*", "" );

    return new AbstractMap.SimpleEntry<String, String>( "key#" + timestamp, "value#" + timestamp );
  }

  @Before
  public void cleanUp() throws Throwable
  {
    Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );

    messageStatus = null;
    latch = null;
    message = null;
    publisher = null;
    headers = null;
    headersEntry = null;
    testResult = null;
  }

  @After
  public void tearDown()
  {
    if( subscription != null )
      subscription.cancelSubscription();
  }
}
