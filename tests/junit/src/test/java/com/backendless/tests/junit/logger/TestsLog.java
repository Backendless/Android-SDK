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

package com.backendless.tests.junit.logger;

import org.junit.Assert;

public class TestsLog
{
  public static void logTestSuiteStarted( String name )
  {
    //System.out.println( "##teamcity[testSuiteStarted name='" + name + "']" );
  }

  public static void logTestSuiteFinished( String name )
  {
    //System.out.println( "##teamcity[testSuiteFinished name='" + name + "']" );
  }

  public static void logTestStarted( String name )
  {
    //System.out.println( "##teamcity[testStarted name='" + name + "']" );
  }

  public static void logTestFinished( String name )
  {
    //System.out.println( "##teamcity[testFinished name='" + name + "']" );
  }

  public static void logTestFailed( String name, AssertionError e )
  {
    logTestFailed( name, e.toString() );
  }

  public static void logTestFailed( String name, Throwable t )
  {
    logTestFailed( name, t.toString() );
  }

  public static void logTestFailed( String name, String error )
  {
    Assert.fail( name + "\n" + error );
    //System.out.println( "##teamcity[testFailed name='" + name + "' message='" + name + ":|n#######|n" + escapeString( error ) + "|n#######|n']" );
  }

  private static String escapeString( String input )
  {
    return input.replaceAll( "\\[+", "|[" ).replaceAll( "\\]+", "|]" ).
            replaceAll( "`+", "|`" ).replaceAll( "\\n+", "|n" ).replaceAll( "\\r+", "|r" ).
            replaceAll( "\\u0085+", "|x" ).replaceAll( "\\u2028+", "|l" ).
            replaceAll( "\\u2029+", "|p" );
  }
}