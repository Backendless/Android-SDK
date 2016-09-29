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

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.util.Log;
import com.backendless.exceptions.BackendlessFault;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackendlessBroadcastReceiver extends BroadcastReceiver implements PushReceiverCallback
{
  private static final String EXTRA_WAKE_LOCK_ID = "com.backendless.wakelockid";
  static final String EXTRA_MESSAGE_ID = "com.backendless.messageid";
  private static final Map<Integer, PowerManager.WakeLock> activeWakeLocks = new HashMap<>();

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
    Log.e( BackendlessPushService.TAG, "Error processing push message: " + message );
  }

  @Override
  public final void onReceive( Context context, Intent intent )
  {
    intent.putExtra( EXTRA_MESSAGE_ID, mNextId );
    //backward compatibility
    if( isPushServiceAvailable( context ) )
    {
      ComponentName comp = new ComponentName( context, getServiceClass() );
      startWakefulService( context, ( intent.setComponent( comp ) ) );
    }
    else
    {
      try
      {
        aquireLock( context, intent );
        BackendlessPushService service = new BackendlessPushService( this );
        service.handleIntent( context, intent );
      }
      finally
      {
        completeWakefulIntent( intent );
      }
    }
    setResultCode( Activity.RESULT_OK );
  }

  private static void aquireLock( Context context, Intent intent )
  {
    synchronized ( activeWakeLocks )
    {
      int id = mNextId++;
      if( mNextId <= 0 )
      {
        mNextId = 1;
      }

      intent.putExtra( EXTRA_WAKE_LOCK_ID, id );

      PowerManager pm = (PowerManager) context.getSystemService( "power" );
      PowerManager.WakeLock wl = pm.newWakeLock( PowerManager.PARTIAL_WAKE_LOCK, "wake:com:backendless:push:" + mNextId );
      wl.setReferenceCounted( false );
      wl.acquire( 60000L );
      activeWakeLocks.put( id, wl );
    }
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
          Log.w( BackendlessPushService.TAG, "No active wake lock id #" + id );
          return true;
        }
      }
    }
  }

  private boolean isPushServiceAvailable( Context context )
  {
    final PackageManager packageManager = context.getPackageManager();
    final Intent intent = new Intent( context, getServiceClass() );
    List resolveInfo = packageManager.queryIntentServices( intent,
        PackageManager.MATCH_DEFAULT_ONLY );
    return resolveInfo.size() > 0;
  }

}
