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

import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.tests.junit.logger.TestsLog;
import org.junit.Assert;
import weborb.client.Fault;

public abstract class ITest
{
  public String testName;

  public void logTestStarted( String testname )
  {
    this.testName = testname;
    TestsLog.logTestStarted( this.testName );
  }

  public void logTestFinished()
  {
    TestsLog.logTestFinished( testName );
  }

  public void logTestFailed( BackendlessFault fault )
  {
    TestsLog.logTestFailed( testName, fault.toString() );
  }

  public void logTestFailed( AssertionError e )
  {
    TestsLog.logTestFailed( testName, e );
  }

  public void logTestFailed( String error )
  {
    TestsLog.logTestFailed( testName, error );
  }

  public void logTestFailed( Throwable t )
  {
    TestsLog.logTestFailed( testName, t );
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
    checkStringExpectation( expectedCode, resultFault.getCode() );
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
    }
    catch( Throwable t )
    {
      failTest( "Server returned a wrong error code. \n" +
                        "Expected: " + expectedMessage + "\n" +
                        "Got: " + actualMessage );
    }
  }

  public void checkCodeExpectation( int expectedCode, String actualCode, String message )
  {
    try
    {
      Assert.assertEquals( expectedCode, (int) Integer.valueOf( actualCode ) );
    }
    catch( Throwable t )
    {
      failTest( "Server returned a wrong error code. \n" +
                        "Expected: " + expectedCode + "\n" +
                        "Got: { " + actualCode + "\n" +
                        "       " + message + " }" );
    }
  }

  public void failTest( String message )
  {
    logTestFailed( message );
  }
}
