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
import android.content.Context;
import android.content.Intent;
import com.backendless.Backendless;

public class BackendlessBroadcastReceiver extends BroadcastReceiver implements PushReceiverCallback
{
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

    BackendlessPushService.enqueueWork( context, getServiceClass(), intent );
  }
}
