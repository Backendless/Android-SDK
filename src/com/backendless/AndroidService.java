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

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

@Deprecated
public class AndroidService extends Service
{
  @Override
  public void onCreate()
  {
    super.onCreate();
  }

  @Override
  public int onStartCommand( Intent intent, int flags, int startId )
  {
    return START_NOT_STICKY;
  }

  @Override
  public void onDestroy()
  {
    super.onDestroy();
  }

  @Override
  public IBinder onBind( Intent intent )
  {
    //stub
    return null;
  }

}