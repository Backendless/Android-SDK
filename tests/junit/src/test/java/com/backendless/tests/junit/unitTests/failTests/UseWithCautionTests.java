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

package com.backendless.tests.junit.unitTests.failTests;

import com.backendless.Backendless;
import com.backendless.Messaging;
import com.backendless.exceptions.BackendlessException;
import com.backendless.messaging.SubscriptionOptions;
import com.backendless.tests.junit.Defaults;
import com.backendless.tests.junit.unitTests.utils.ReflectionHelpers;
import junit.framework.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: Michael Cheremuhin
 * Date: 10/17/13
 * Time: 22:15
 */
public class UseWithCautionTests
{
  @Test
  public void serverDeathAfterMessagingPolls() throws Exception
  {
    int pollsCount = 1000;
    String channelName = "TestChannel";
    Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
    ReflectionHelpers.loadAndroidSdk();

    Messaging messaging = (Messaging) ReflectionHelpers.getObjectInstanceWithPrivateConstructor( "com.backendless.Messaging", null );
    Method subscriptionMethod = ReflectionHelpers.getObjectPrivateMethod( messaging.getClass(), "subscribeForPollingAccess", String.class, SubscriptionOptions.class );
    String subscriptionId = (String) subscriptionMethod.invoke( messaging, channelName, null );

    for( int i = 0; i < pollsCount; i++ )
    {
      System.out.println("Until server death: " + (pollsCount - i) + " calls");
      try{
        messaging.pollMessages( channelName, subscriptionId );
      }
      catch( BackendlessException e )
      {
        Assert.fail( "Server is dead now :(" );
      }
    }

    Assert.assertTrue( "It`s alive!!! Try more polls ;)", true );
  }
}
