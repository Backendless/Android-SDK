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

package com.backendless.tests.junit.unitTests.usersService.syncTests;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.tests.junit.Defaults;
import org.junit.Test;

public class RolesTest extends TestsFrame
{
  @Test
  public void testAssignRole() throws Exception
  {
    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = getRandomLoggedInUser();
      Backendless.UserService.assignRole( (String) user.getProperty( "login" ), CUSTOM_ROLE );
    }
    catch( Exception t )
    {
      checkErrorCode( ExceptionMessage.ASSIGN_ROLE, t );
      //logTestFailed( t );
    }

//    try
//    {
//      Backendless.Geo.savePoint( new GeoPoint( 1, 0 ) );
//      logTestFailed( "Server allowed save point request" );
//    }
//    catch( Exception e )
//    {
//      logTestFinished();
//    }
  }

  @Test
  public void testAssignNullRole() throws Exception
  {
    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = getRandomLoggedInUser();
      Backendless.UserService.assignRole( (String) user.getProperty( "login" ), null );
      logTestFailed( "Client accepted null role" );
    }
    catch( Exception t )
    {
      checkErrorCode( ExceptionMessage.NULL_ROLE_NAME, t );
      logTestFinished();
    }
  }

  @Test
  public void testAssignRoleToNullIdentity() throws Exception
  {
    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      Backendless.UserService.assignRole( null, CUSTOM_ROLE );
      logTestFailed( "Client accepted null identity" );
    }
    catch( Exception t )
    {
      checkErrorCode( ExceptionMessage.NULL_IDENTITY, t );
      logTestFinished();
    }
  }

  @Test
  public void testAssignNotExistingRole() throws Exception
  {
    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = getRandomLoggedInUser();
      Backendless.UserService.assignRole( (String) user.getProperty( "login" ), "BlablaRole" + random.nextInt() );
      logTestFailed( "Server accepted not existing role" );
    }
    catch( Exception t )
    {
      checkErrorCode( ExceptionMessage.ASSIGN_ROLE, t );
      //checkErrorCode( 2005, t );
      logTestFinished();
    }
  }

  @Test
  public void testAssignSystemRole() throws Exception
  {
    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = getRandomLoggedInUser();
      Backendless.UserService.assignRole( (String) user.getProperty( "login" ), SYSTEM_ROLE );
      logTestFailed( "Server accepted system role" );
    }
    catch( Exception t )
    {
      checkErrorCode( ExceptionMessage.ASSIGN_ROLE, t );
      //checkErrorCode( 2007, t );
      logTestFinished();
    }
  }

  @Test
  public void testUnassignRole() throws Exception
  {
    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = getRandomLoggedInUser();
      Backendless.UserService.assignRole( (String) user.getProperty( "login" ), CUSTOM_ROLE );

//      try
//      {
//        Backendless.Geo.savePoint( new GeoPoint( 2, 0 ) );
//        logTestFailed( "Server allowed save point" );
//      }
//      catch( Exception e )
//      {
//        e.printStackTrace();
//      }
//
//      Backendless.UserService.unassignRole( (String) user.getProperty( "login" ), CUSTOM_ROLE );
//      Backendless.Geo.savePoint( new GeoPoint( 3, 0 ) );
      logTestFinished();
    }
    catch( Exception t )
    {
      checkErrorCode( ExceptionMessage.ASSIGN_ROLE, t );
      //logTestFailed( t );
    }
  }

  @Test
  public void testUnassignNullRole() throws Exception
  {
    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = getRandomLoggedInUser();
      Backendless.UserService.unassignRole( (String) user.getProperty( "login" ), null );
      logTestFailed( "Client accepted null role" );
    }
    catch( Exception t )
    {
      checkErrorCode( ExceptionMessage.NULL_ROLE_NAME, t );
      logTestFinished();
    }
  }

  @Test
  public void testUnassignRoleToNullIdentity() throws Exception
  {
    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      Backendless.UserService.unassignRole( null, CUSTOM_ROLE );
      logTestFailed( "Client accepted null identity" );
    }
    catch( Exception t )
    {
      checkErrorCode( ExceptionMessage.NULL_IDENTITY, t );
      logTestFinished();
    }
  }

  @Test
  public void testUnassignNotExistingRole() throws Exception
  {
    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = getRandomLoggedInUser();
      Backendless.UserService.unassignRole( (String) user.getProperty( "login" ), "BlablaRole" + random.nextInt() );
      logTestFailed( "Server accepted not existing role" );
    }
    catch( Exception t )
    {
      checkErrorCode( ExceptionMessage.ASSIGN_ROLE, t );
//      checkErrorCode( 2005, t );
      logTestFinished();
    }
  }

  @Test
  public void testUnassignSystemRole() throws Exception
  {
    try
    {
      Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
      BackendlessUser user = getRandomLoggedInUser();
      Backendless.UserService.unassignRole( (String) user.getProperty( "login" ), SYSTEM_ROLE );
      logTestFailed( "Server accepted system role" );
    }
    catch( Exception t )
    {
      checkErrorCode( ExceptionMessage.ASSIGN_ROLE, t );
      //checkErrorCode( 2008, t );
      logTestFinished();
    }
  }
}
