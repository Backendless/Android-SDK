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
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class BackendlessBroadcastReceiver extends BroadcastReceiver
{
  private static final String TAG = "BackendlessBroadcastReceiver";
  private static final String EXTRA_WAKE_LOCK_ID = "android.support.content.wakelockid";
  private static final Map<Integer, PowerManager.WakeLock> activeWakeLocks = new HashMap<>();
  public static final String INTERNAL_ID_KEY = "internal-id";

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
    Intent service = new Intent( context, getServiceClass() );
    service.putExtras( intent );
    service.putExtra( INTERNAL_ID_KEY, mNextId );
    service.setAction( intent.getAction() );
    startWakefulService( context, service );
  }

  protected static ComponentName startWakefulService( Context context, Intent intent )
  {
    synchronized ( activeWakeLocks )
    {
      int id = mNextId++;
      if( mNextId <= 0 )
      {
        mNextId = 1;
      }

      intent.putExtra( EXTRA_WAKE_LOCK_ID, id );
      ComponentName comp = context.startService( intent );
      if( comp == null )
      {
        return null;
      }
      else
      {
        PowerManager pm = (PowerManager) context.getSystemService( "power" );
        PowerManager.WakeLock wl = pm.newWakeLock( 1, "wake:" + comp.flattenToShortString() );
        wl.setReferenceCounted( false );
        wl.acquire( 60000L );
        activeWakeLocks.put( id, wl );
        return comp;
      }
    }
  }

  public static boolean completeWakefulIntent( Intent intent )
  {
    int id = intent.getIntExtra( EXTRA_WAKE_LOCK_ID, 0 );
    if( id == 0 )
    {
      return false;
    }
    else
    {
      synchronized ( activeWakeLocks )
      {
        PowerManager.WakeLock wl = activeWakeLocks.get( id );
        if( wl != null )
        {
          wl.release();
          activeWakeLocks.remove( id );
          return true;
        }
        else
        {
          Log.w( TAG, "No active wake lock id #" + id );
          return true;
        }
      }
    }
  }

}
