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

package com.backendless.tests.junit.unitTests.usersService.asyncTests;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.tests.junit.Defaults;
import org.junit.Test;

public class RolesTest extends TestsFrame
{
  @Test
  public void testAssignRole() throws Exception
  {
    Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        getRandomLoggedInUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser response )
          {
            Backendless.UserService.assignRole( (String) response.getProperty( "login" ), CUSTOM_ROLE, new ResponseCallback<Void>()
            {
              @Override
              public void handleResponse( Void response )
              {
                failCountDownWith( "Server allowed assignRole request" );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( ExceptionMessage.ASSIGN_ROLE, fault );
              }
//              @Override
//              public void handleResponse( Void response )
//              {
//                Backendless.Geo.savePoint( new GeoPoint( 1, 1 ), new AsyncCallback<GeoPoint>()
//                {
//                  @Override
//                  public void handleResponse( GeoPoint response )
//                  {
//                    failCountDownWith( "Server allowed savePoint request" );
//                  }
//
//                  @Override
//                  public void handleFault( BackendlessFault fault )
//                  {
//                    countdownLogTestFinished();
//                  }
//                } );
//              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testAssignNullRole() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        getRandomLoggedInUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser user )
          {
            Backendless.UserService.assignRole( (String) user.getProperty( "login" ), null, new AsyncCallback<Void>()
            {
              @Override
              public void handleResponse( Void response )
              {
                failCountDownWith( "Client accepted null role" );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( ExceptionMessage.NULL_ROLE_NAME, fault );
              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testAssignRoleToNullIdentity() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        Backendless.UserService.assignRole( null, CUSTOM_ROLE, new AsyncCallback<Void>()
        {
//          @Override
//          public void handleResponse( Void response )
//          {
//            failCountDownWith( "Server allowed assignRole request" );
//          }
//
//          @Override
//          public void handleFault( BackendlessFault fault )
//          {
//            checkErrorCode( ExceptionMessage.ASSIGN_ROLE, fault );
//          }
          @Override
          public void handleResponse( Void response )
          {
            failCountDownWith( "Client accepted null identity" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( ExceptionMessage.NULL_IDENTITY, fault );
          }
        } );
      }
    } );
  }

  @Test
  public void testAssignNotExistingRole() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        getRandomLoggedInUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser user )
          {
            Backendless.UserService.assignRole( (String) user.getProperty( "login" ), "BlablaRole" + random.nextInt(), new AsyncCallback<Void>()
            {
              @Override
              public void handleResponse( Void response )
              {
                failCountDownWith( "Server allowed assignRole request" );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( ExceptionMessage.ASSIGN_ROLE, fault );
              }
//              @Override
//              public void handleResponse( Void response )
//              {
//                failCountDownWith( "Server accepted not existing role" );
//              }
//
//              @Override
//              public void handleFault( BackendlessFault fault )
//              {
//                checkErrorCode( 2005, fault );
//              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testAssignSystemRole() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        getRandomLoggedInUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser response )
          {
            Backendless.UserService.assignRole( (String) response.getProperty( "login" ), SYSTEM_ROLE, new AsyncCallback<Void>()
            {
              @Override
              public void handleResponse( Void response )
              {
                failCountDownWith( "Server allowed assignRole request" );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( ExceptionMessage.ASSIGN_ROLE, fault );
              }
//              @Override
//              public void handleResponse( Void response )
//              {
//                failCountDownWith( "Server accepted system role" );
//              }
//
//              @Override
//              public void handleFault( BackendlessFault fault )
//              {
//                checkErrorCode( 2007, fault );
//              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testUnassignRole() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        getRandomLoggedInUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( final BackendlessUser user )
          {
            Backendless.UserService.assignRole( (String) user.getProperty( "login" ), CUSTOM_ROLE, new ResponseCallback<Void>()
            {
              @Override
              public void handleResponse( Void response )
              {
                failCountDownWith( "Server allowed assignRole request" );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( ExceptionMessage.ASSIGN_ROLE, fault );
              }
//              @Override
//              public void handleResponse( Void response )
//              {
//                Backendless.Geo.savePoint( new GeoPoint( 2, 1 ), new AsyncCallback<GeoPoint>()
//                {
//                  @Override
//                  public void handleResponse( GeoPoint response )
//                  {
//                    failCountDownWith( "Server allowed savePoint" );
//                  }
//
//                  @Override
//                  public void handleFault( BackendlessFault fault )
//                  {
//                    Backendless.UserService.unassignRole( (String) user.getProperty( "login" ), CUSTOM_ROLE, new ResponseCallback<Void>() );
//                    Backendless.Geo.savePoint( new GeoPoint( 2, 2 ), new ResponseCallback<GeoPoint>() );
//                  }
//                } );
//              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testUnassignNullRole() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        getRandomLoggedInUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser user )
          {
            Backendless.UserService.unassignRole( (String) user.getProperty( "login" ), null, new AsyncCallback<Void>()
            {
//              @Override
//              public void handleResponse( Void response )
//              {
//                failCountDownWith( "Server allowed assignRole request" );
//              }
//
//              @Override
//              public void handleFault( BackendlessFault fault )
//              {
//                checkErrorCode( ExceptionMessage.ASSIGN_ROLE, fault );
//              }
              @Override
              public void handleResponse( Void response )
              {
                failCountDownWith( "Client accepted null role" );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( ExceptionMessage.NULL_ROLE_NAME, fault );
              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testUnassignRoleToNullIdentity() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        Backendless.UserService.unassignRole( null, CUSTOM_ROLE, new AsyncCallback<Void>()
        {
//          @Override
//          public void handleResponse( Void response )
//          {
//            failCountDownWith( "Server allowed assignRole request" );
//          }
//
//          @Override
//          public void handleFault( BackendlessFault fault )
//          {
//            checkErrorCode( ExceptionMessage.ASSIGN_ROLE, fault );
//          }
          @Override
          public void handleResponse( Void response )
          {
            failCountDownWith( "Client accepted null identity" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( ExceptionMessage.NULL_IDENTITY, fault );
          }
        } );
      }
    } );
  }

  @Test
  public void testUnassignNotExistingRole() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        getRandomLoggedInUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser user )
          {
            Backendless.UserService.unassignRole( (String) user.getProperty( "login" ), "BlablaRole" + random.nextInt(), new AsyncCallback<Void>()
            {
              @Override
              public void handleResponse( Void response )
              {
                failCountDownWith( "Server allowed assignRole request" );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( ExceptionMessage.ASSIGN_ROLE, fault );
              }
//              @Override
//              public void handleResponse( Void response )
//              {
//                failCountDownWith( "Server accepted not existing role" );
//              }
//
//              @Override
//              public void handleFault( BackendlessFault fault )
//              {
//                checkErrorCode( 2005, fault );
//              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testUnassignSystemRole() throws Exception
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
        getRandomLoggedInUser( new ResponseCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser user )
          {
            Backendless.UserService.unassignRole( (String) user.getProperty( "login" ), SYSTEM_ROLE, new AsyncCallback<Void>()
            {
              @Override
              public void handleResponse( Void response )
              {
                failCountDownWith( "Server allowed assignRole request" );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( ExceptionMessage.ASSIGN_ROLE, fault );
              }
//              @Override
//              public void handleResponse( Void response )
//              {
//                failCountDownWith( "Server accepted system role" );
//              }
//
//              @Override
//              public void handleFault( BackendlessFault fault )
//              {
//                checkErrorCode( 2008, fault );
//              }
            } );
          }
        } );
      }
    } );
  }
}
