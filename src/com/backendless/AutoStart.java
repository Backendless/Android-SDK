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

package com.backendless;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.backendless.geo.LocationTracker;

/**
 * Created by baas on 26.05.15.
 */
public class AutoStart extends BroadcastReceiver
{

  @Override
  public void onReceive( Context context, Intent intent )
  {
    if( intent.getAction().equals( Intent.ACTION_REBOOT ) )
    {
      Intent serviceIntent = new Intent( context.getApplicationContext(), LocationTracker.class );
      context.startService( serviceIntent );
    }
  }
}
