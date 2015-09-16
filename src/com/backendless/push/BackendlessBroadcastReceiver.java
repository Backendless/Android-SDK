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

import android.R;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.PublishOptions;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BackendlessBroadcastReceiver extends BroadcastReceiver
{
  private static final String TAG = "BackendlessBroadcastReceiver";
  private static final Random random = new Random();

  private static final int MAX_BACKOFF_MS = (int) TimeUnit.SECONDS.toMillis( 3600 );

  private static final String TOKEN = Long.toBinaryString( random.nextLong() );
  private static final String EXTRA_TOKEN = "token";

  private static final String WAKELOCK_KEY = "GCM_LIB";
  private static PowerManager.WakeLock wakeLock;
  private static final Object LOCK = BackendlessBroadcastReceiver.class;

  //Fields are placed here because this class is most strongly referenced by android
  private static String persistedSenderId;
  private static long persistedRegistrationExpiration;
  private static String[] persistedChannels;
  private static int customLayout;
  private static int customLayoutTitle;
  private static int customLayoutDescription;
  private static int customLayoutImageContainer;
  private static int customLayoutImage;

  private static int notificationId = 1;

  public BackendlessBroadcastReceiver()
  {
  }

  public BackendlessBroadcastReceiver( String senderId )
  {
    BackendlessBroadcastReceiver.persistedSenderId = senderId;
  }

  protected static void setSenderId( String senderId )
  {
    BackendlessBroadcastReceiver.persistedSenderId = senderId;
  }

  protected static void setRegistrationExpiration( long registrationExpiration )
  {
    BackendlessBroadcastReceiver.persistedRegistrationExpiration = registrationExpiration;
  }

  protected static void setChannels( List<String> channels )
  {
    BackendlessBroadcastReceiver.persistedChannels = channels.toArray( new String[channels.size()] );
  }

  private static String getSenderId()
  {
    return persistedSenderId;
  }

  private static long getRegistrationExpiration()
  {
    if( persistedRegistrationExpiration == 0 )
      return System.currentTimeMillis() + GCMRegistrar.DEFAULT_ON_SERVER_LIFESPAN_MS;

    return persistedRegistrationExpiration;
  }

  private static List<String> getChannels()
  {
    if( persistedChannels == null )
      return null;

    return Arrays.asList( persistedChannels );
  }

  @Override
  public final void onReceive( Context context, Intent intent )
  {
    synchronized( LOCK )
    {
      if( wakeLock == null )
      {
        PowerManager pm = (PowerManager) context.getSystemService( Context.POWER_SERVICE );
        wakeLock = pm.newWakeLock( PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_KEY );
      }
    }

    wakeLock.acquire();
    this.handleIntent( context, intent );
    setResult( Activity.RESULT_OK, null, null );
  }

  //API block
  public void onRegistered( Context context, String registrationId )
  {
  }

  public void onUnregistered( Context context, Boolean unregistered )
  {
  }

  public boolean onMessage( Context context, Intent intent )
  {
    return true;
  }

  public void onError( Context context, String message )
  {
    throw new RuntimeException( message );
  }

  //Internal block
  private void handleIntent( Context context, Intent intent )
  {
    try
    {
      String action = intent.getAction();

      if( action.equals( GCMConstants.INTENT_FROM_GCM_REGISTRATION_CALLBACK ) )
        handleRegistration( context, intent );
      else if( action.equals( GCMConstants.INTENT_FROM_GCM_MESSAGE ) )
        handleMessage( context, intent );
      else if( action.equals( GCMConstants.INTENT_FROM_GCM_LIBRARY_RETRY ) )
      {
        String token = intent.getStringExtra( EXTRA_TOKEN );
        if( !TOKEN.equals( token ) )
          return;
        // retry last call
        if( GCMRegistrar.isRegistered( context ) )
          GCMRegistrar.internalUnregister( context );
        else
          GCMRegistrar.internalRegister( context, getSenderId() );
      }
    }
    finally
    {
      synchronized( LOCK )
      {
        if( wakeLock != null )
          wakeLock.release();
      }
    }
  }

  public static void registerResources( Context context )
  {
    customLayout = context.getResources().getIdentifier( "notification", "layout", context.getPackageName() );
    customLayoutTitle = context.getResources().getIdentifier( "title", "id", context.getPackageName() );
    customLayoutDescription = context.getResources().getIdentifier( "text", "id", context.getPackageName() );
    customLayoutImageContainer = context.getResources().getIdentifier( "image", "id", context.getPackageName() );
    customLayoutImage = context.getResources().getIdentifier( "push_icon", "drawable", context.getPackageName() );
  }

  private void handleMessage( final Context context, Intent intent )
  {

    try
    {
      boolean showPushNotification = onMessage( context, intent );

      if( showPushNotification )
      {
        CharSequence tickerText = intent.getStringExtra( PublishOptions.ANDROID_TICKER_TEXT_TAG );
        CharSequence contentTitle = intent.getStringExtra( PublishOptions.ANDROID_CONTENT_TITLE_TAG );
        CharSequence contentText = intent.getStringExtra( PublishOptions.ANDROID_CONTENT_TEXT_TAG );

        if( tickerText != null && tickerText.length() > 0 )
        {
          int appIcon = context.getApplicationInfo().icon;
          if( appIcon == 0 )
            appIcon = R.drawable.sym_def_app_icon;

          Intent notificationIntent = context.getPackageManager().getLaunchIntentForPackage( context.getApplicationInfo().packageName );
          PendingIntent contentIntent = PendingIntent.getActivity( context, 0, notificationIntent, 0 );
          Notification notification = new Notification( appIcon, tickerText, System.currentTimeMillis() );
          notification.flags |= Notification.FLAG_AUTO_CANCEL;
          notification.setLatestEventInfo( context, contentTitle, contentText, contentIntent );

          registerResources( context );
          if( customLayout > 0 && customLayoutTitle > 0 && customLayoutDescription > 0 && customLayoutImageContainer > 0 )
          {
            NotificationLookAndFeel lookAndFeel = new NotificationLookAndFeel();
            lookAndFeel.extractColors( context );
            RemoteViews contentView = new RemoteViews( context.getPackageName(), customLayout );
            contentView.setTextViewText( customLayoutTitle, contentTitle );
            contentView.setTextViewText( customLayoutDescription, contentText );
            contentView.setTextColor( customLayoutTitle, lookAndFeel.getTextColor() );
            contentView.setFloat( customLayoutTitle, "setTextSize", lookAndFeel.getTextSize() );
            contentView.setTextColor( customLayoutDescription, lookAndFeel.getTextColor() );
            contentView.setFloat( customLayoutDescription, "setTextSize", lookAndFeel.getTextSize() );
            contentView.setImageViewResource( customLayoutImageContainer, customLayoutImage );
            notification.contentView = contentView;
          }

          NotificationManager notificationManager = (NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE );
          notificationManager.notify( notificationId++, notification );
        }
      }
    }
    catch( Throwable throwable )
    {
      Log.e( TAG, "Error processing push notification", throwable );
    }
  }

  private void handleRegistration( final Context context, Intent intent )
  {
    String registrationId = intent.getStringExtra( GCMConstants.EXTRA_REGISTRATION_ID );
    String error = intent.getStringExtra( GCMConstants.EXTRA_ERROR );
    String unregistered = intent.getStringExtra( GCMConstants.EXTRA_UNREGISTERED );
    boolean isInternal = intent.getBooleanExtra( GCMConstants.EXTRA_IS_INTERNAL, false );

    // registration succeeded
    if( registrationId != null )
    {
      if( isInternal )
      {
        onRegistered( context, registrationId );
      }

      GCMRegistrar.resetBackoff( context );
      GCMRegistrar.setGCMdeviceToken( context, registrationId );
      registerFurther( context, registrationId );
      return;
    }

    // unregistration succeeded
    if( unregistered != null )
    {
      // Remember we are unregistered
      GCMRegistrar.resetBackoff( context );
      GCMRegistrar.setGCMdeviceToken( context, "" );
      unregisterFurther( context );
      return;
    }

    // Registration failed
    if( error.equals( GCMConstants.ERROR_SERVICE_NOT_AVAILABLE ) )
    {
      int backoffTimeMs = GCMRegistrar.getBackoff( context );
      int nextAttempt = backoffTimeMs / 2 + random.nextInt( backoffTimeMs );
      Intent retryIntent = new Intent( GCMConstants.INTENT_FROM_GCM_LIBRARY_RETRY );
      retryIntent.putExtra( EXTRA_TOKEN, TOKEN );
      PendingIntent retryPendingIntent = PendingIntent.getBroadcast( context, 0, retryIntent, 0 );
      AlarmManager am = (AlarmManager) context.getSystemService( Context.ALARM_SERVICE );
      am.set( AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + nextAttempt, retryPendingIntent );
      // Next retry should wait longer.
      if( backoffTimeMs < MAX_BACKOFF_MS )
        GCMRegistrar.setBackoff( context, backoffTimeMs * 2 );
    }
    else
    {
      onError( context, error );
    }
  }

  private void registerFurther( final Context context, String GCMregistrationId )
  {
    Backendless.Messaging.registerDeviceOnServer( GCMregistrationId, getChannels(), getRegistrationExpiration(), new AsyncCallback<String>()
    {
      @Override
      public void handleResponse( String registrationId )
      {
        GCMRegistrar.setRegistrationId( context, registrationId, getRegistrationExpiration() );
        onRegistered( context, registrationId );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        onError( context, "Could not register device on Backendless server: " + fault.getMessage() );
      }
    } );
  }

  private void unregisterFurther( final Context context )
  {
    Backendless.Messaging.unregisterDeviceOnServer( new AsyncCallback<Boolean>()
    {
      @Override
      public void handleResponse( Boolean unregistered )
      {
        GCMRegistrar.setRegistrationId( context, "", 0 );
        onUnregistered( context, unregistered );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        onError( context, "Could not unregister device on Backendless server: " + fault.getMessage() );
      }
    } );
  }
}
