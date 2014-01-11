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

package com.backendless.tests.junit;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import org.junit.Assert;
import weborb.client.Fault;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class IAsyncTest extends ITest
{
  private static final String TEST_FAIL_MESSAGE = "test fail";
  public BackendlessFault testFault;
  public CountDownLatch testLatch;

  public void setLatch()
  {
    setLatch( 1 );
  }

  public void setLatch( int count )
  {
    testLatch = new CountDownLatch( count );
    testFault = null;
  }

  public void countDown()
  {
    testLatch.countDown();
  }

  public void failCountDownWith( String message )
  {
    testFault = new BackendlessFault( TEST_FAIL_MESSAGE, message );
    testLatch.countDown();
  }

  public void failCountDownWith( Throwable t )
  {
    testFault = new BackendlessFault( TEST_FAIL_MESSAGE, t.getMessage() );
    testLatch.countDown();
  }

  public void failCountDownWith( BackendlessFault backendlessFault )
  {
    testFault = backendlessFault;
    testLatch.countDown();
  }

  public void unlatchWith( Throwable t )
  {
    unlatchWith( new BackendlessFault( TEST_FAIL_MESSAGE, t.getMessage() ) );
  }

  public void unlatchWith( BackendlessFault backendlessFault )
  {
    testFault = backendlessFault;

    for( int i = 0; i < testLatch.getCount(); i++ )
      testLatch.countDown();
  }

  public void runInNewThreadAndAwait( Runnable runnable )
  {
    if( testLatch == null )
      setLatch();

    if( testLatch.getCount() == 0 )
      setLatch();

    runnable.run();

    try
    {
      testLatch.await( 3, TimeUnit.MINUTES );
    }
    catch( InterruptedException e )
    {
      logTestFailed( e );
    }

    if( testFault != null )
      super.logTestFailed( testFault.toString() );

    logTestFinished();
  }

  @Override
  public void logTestStarted( String testname )
  {
    super.logTestStarted( testname + "Async" );
  }

  public void countdownLogTestFinished()
  {
    countDown();
    super.logTestFinished();
  }

  //Added because of typos =)
  @Override
  public void logTestFailed( AssertionError e )
  {
    testFault = new BackendlessFault( TEST_FAIL_MESSAGE, e.getMessage() );
    testLatch.countDown();
  }

  @Override
  public void logTestFailed( String message )
  {
    testFault = new BackendlessFault( TEST_FAIL_MESSAGE, message );
    testLatch.countDown();
  }

  @Override
  public void logTestFailed( Throwable t )
  {
    testFault = new BackendlessFault( TEST_FAIL_MESSAGE, t.getMessage() );
    testLatch.countDown();
  }

  @Override
  public void failTest( String message )
  {
    failCountDownWith( message );
  }

  public void checkErrorCode( String expectedCode, BackendlessFault resultFault )
  {
    checkErrorCode( expectedCode, (Fault) resultFault );
  }

  public void checkErrorCode( String expectedCode, Throwable resultException )
  {
    checkStringExpectation( expectedCode, resultException.getMessage() );
  }

  public void checkErrorCode( String expectedCode, Fault resultFault )
  {
    checkStringExpectation( expectedCode, resultFault.getMessage() );
  }

  public void checkErrorCode( int expectedCode, Throwable resultException )
  {
    if( resultException instanceof BackendlessException )
      checkCodeExpectation( expectedCode, ((BackendlessException) resultException).getCode(), resultException.getMessage() );
    else
      checkStringExpectation( String.valueOf( expectedCode ), resultException.getMessage() );
  }

  public void checkErrorCode( int expectedCode, Fault resultFault )
  {
    checkCodeExpectation( expectedCode, resultFault.getCode(), resultFault.getMessage() );
  }

  public void checkStringExpectation( String expectedMessage, String actualMessage )
  {
    try
    {
      Assert.assertEquals( expectedMessage, actualMessage );
      countdownLogTestFinished();
    }
    catch( Throwable t )
    {
      failCountDownWith( "Server returned a wrong error code. \n" +
                                 "Expected: " + expectedMessage + "\n" +
                                 "Got: " + actualMessage );
    }
  }

  public void checkCodeExpectation( int expectedCode, String actualCode, String message )
  {
    try
    {
      Assert.assertEquals( expectedCode, (int) Integer.valueOf( actualCode ) );
      countdownLogTestFinished();
    }
    catch( Throwable t )
    {
      failCountDownWith( "Server returned a wrong error code. \n" +
                                 "Expected: " + expectedCode + "\n" +
                                 "Got: { " + actualCode + "\n" +
                                 "       " + message + " }" );
    }
  }

  public class ResponseCallback<E> implements AsyncCallback<E>
  {
    @Override
    public void handleResponse( E response )
    {
      countDown();
    }

    @Override
    public void handleFault( BackendlessFault fault )
    {
      failCountDownWith( fault );
    }
  }
}
