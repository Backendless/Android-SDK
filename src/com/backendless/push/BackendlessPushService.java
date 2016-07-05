package com.backendless.push;

import android.app.*;
import android.content.Context;
import android.content.Intent;
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

public class BackendlessPushService extends IntentService
{
  private static final String TAG = "BackendlessPushService";
  private static final Random random = new Random();

  private static final int MAX_BACKOFF_MS = (int) TimeUnit.SECONDS.toMillis( 3600 );
  private static final String TOKEN = Long.toBinaryString( random.nextLong() );
  private static final String EXTRA_TOKEN = "token";

  //todo move to shared prefs
  private static String persistedSenderId;
  private static long persistedRegistrationExpiration;
  private static String[] persistedChannels;

  public BackendlessPushService()
  {
    super( "BackendlessPushService" );
  }

  public BackendlessPushService( String senderId )
  {
    this();
    persistedSenderId = senderId;
  }

  /**
   * At this point {@link com.backendless.push.BackendlessBroadcastReceiver}
   * is still holding a wake lock
   * for us.  We can do whatever we need to here and then tell it that
   * it can release the wakelock.  This sample just does some slow work,
   * but more complicated implementations could take their own wake
   * lock here before releasing the receiver's.
   * <p/>
   * Note that when using this approach you should be aware that if your
   * service gets killed and restarted while in the middle of such work
   * (so the Intent gets re-delivered to perform the work again), it will
   * at that point no longer be holding a wake lock since we are depending
   * on SimpleWakefulReceiver to that for us.  If this is a concern, you can
   * acquire a separate wake lock here.
   */
  @Override
  protected void onHandleIntent( Intent intent )
  {
    handleIntent( this, intent );
    BackendlessBroadcastReceiver.completeWakefulIntent( intent );
  }

  protected static void setSenderId( String senderId )
  {
    persistedSenderId = senderId;
  }

  protected static void setRegistrationExpiration( long registrationExpiration )
  {
    persistedRegistrationExpiration = registrationExpiration;
  }

  protected static void setChannels( List<String> channels )
  {
    persistedChannels = channels.toArray( new String[ channels.size() ] );
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

  protected void onRegistered( Context context, String registrationId )
  {
  }

  protected void onUnregistered( Context context, Boolean unregistered )
  {
  }

  protected boolean onMessage( Context context, Intent intent )
  {
    return true;
  }

  protected void onError( Context context, String message )
  {
    throw new RuntimeException( message );
  }

  private void handleIntent( Context context, Intent intent )
  {
    String action = intent.getAction();

    switch ( action )
    {
      case GCMConstants.INTENT_FROM_GCM_REGISTRATION_CALLBACK:
        handleRegistration( context, intent );
        break;
      case GCMConstants.INTENT_FROM_GCM_MESSAGE:
        handleMessage( context, intent );
        break;
      case GCMConstants.INTENT_FROM_GCM_LIBRARY_RETRY:
        handleRetry( context, intent );
        break;
    }
  }

  private void handleRetry( Context context, Intent intent )
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
            appIcon = android.R.drawable.sym_def_app_icon;

          Intent notificationIntent = context.getPackageManager().getLaunchIntentForPackage( context.getApplicationInfo().packageName );
          PendingIntent contentIntent = PendingIntent.getActivity( context, 0, notificationIntent, 0 );
          Notification notification = new Notification.Builder( context )
              .setSmallIcon( appIcon )
              .setTicker( tickerText )
              .setContentTitle( contentTitle )
              .setContentText( contentText )
              .setContentIntent( contentIntent )
              .setWhen( System.currentTimeMillis() )
              .build();
          notification.flags |= Notification.FLAG_AUTO_CANCEL;

          int customLayout = context.getResources().getIdentifier( "notification", "layout", context.getPackageName() );
          int customLayoutTitle = context.getResources().getIdentifier( "title", "id", context.getPackageName() );
          int customLayoutDescription = context.getResources().getIdentifier( "text", "id", context.getPackageName() );
          int customLayoutImageContainer = context.getResources().getIdentifier( "image", "id", context.getPackageName() );
          int customLayoutImage = context.getResources().getIdentifier( "push_icon", "drawable", context.getPackageName() );

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
          notificationManager.notify( intent.getIntExtra( BackendlessBroadcastReceiver.INTERNAL_ID_KEY, 0 ), notification );
        }
      }
    }
    catch ( Throwable throwable )
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
