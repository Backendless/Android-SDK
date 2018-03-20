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

package com.backendless.push;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import com.backendless.Backendless;

import java.util.HashMap;
import java.util.Map;

public class BackendlessBroadcastReceiver extends BroadcastReceiver implements PushReceiverCallback
{
  private static final String TAG = BackendlessBroadcastReceiver.class.getSimpleName();
  private static final String EXTRA_WAKE_LOCK_ID = "com.backendless.wakelockid";
  private static final Map<Integer, PowerManager.WakeLock> activeWakeLocks = new HashMap<>();
  public static final String EXTRA_MESSAGE_ID = "com.backendless.messageid";

  private static int mNextId = 1;

  public BackendlessBroadcastReceiver()
  {
  }

  public Class<? extends BackendlessPushService> getServiceClass()
  {
    return BackendlessPushService.class;
  }

  /**
   * @deprecated Extend {@link BackendlessPushService} instead.
   */
  @Deprecated
  public void onRegistered( Context context, String registrationId )
  {
  }

  /**
   * @deprecated Extend {@link BackendlessPushService} instead.
   */
  @Deprecated
  public void onUnregistered( Context context, Boolean unregistered )
  {
  }

  /**
   * @deprecated Extend {@link BackendlessPushService} instead.
   */
  @Deprecated
  public boolean onMessage( Context context, Intent intent )
  {
    return true;
  }

  /**
   * @deprecated Extend {@link BackendlessPushService} instead.
   */
  @Deprecated
  public void onError( Context context, String message )
  {
    throw new RuntimeException( message );
  }

  @Override
  public final void onReceive( Context context, Intent intent )
  {
    if( !Backendless.isInitialized() )
      Backendless.initApplicationFromProperties( context );

    ComponentName comp = new ComponentName( context, getServiceClass() );
    BackendlessPushService.enqueueWork( context, intent.setComponent( comp ) );
  }
}
