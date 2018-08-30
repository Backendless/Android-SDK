package com.backendless.push;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.AndroidPushTemplate;
import com.backendless.messaging.PublishOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>Firstly you should inherit this class for your own needs. For example to handle push messages manually.
 *
 * <p>Secondary you should declare this service in 'AndroidManifest.xml' like this:<br/>
 * <pre>{@code
 * <service android:name="full.qualified.class.name"
 *           android:permission="android.permission.BIND_JOB_SERVICE">
 * </service>
 * }
 * </pre>
 * Where {@code 'full.qualified.class.name'} is {@code 'com.backendless.push.BackendlessPushService'} or your own class that inherit it.
 */
public class BackendlessPushService extends JobIntentService implements PushReceiverCallback
{
  public static final String IMMEDIATE_MESSAGE = "ImmediateMessage";
  private static final String TAG = BackendlessPushService.class.getSimpleName();
  private static final int JOB_ID = 1000;
  private static final Random random = new Random();

  private static final int MAX_BACKOFF_MS = (int) TimeUnit.SECONDS.toMillis( 3600 );
  private static final String TOKEN = Long.toBinaryString( random.nextLong() );
  private static final String EXTRA_TOKEN = "token";
  private static AtomicInteger notificationIdGenerator;

  private PushReceiverCallback callback;

  static void enqueueWork( final Context context, final Class cls, final Intent work )
  {
    ComponentName comp = new ComponentName( context, cls );
    try
    {
      JobIntentService.enqueueWork( context, cls, JOB_ID, work.setComponent( comp ) );
    }
    catch( RuntimeException e )
    {
      if( e.getMessage() != null && e.getMessage().contains( "does not require android.permission.BIND_JOB_SERVICE" ) )
      {
        Log.e( BackendlessPushService.class.getSimpleName(), "You should set the 'android.permission.BIND_JOB_SERVICE' permission in 'AndroidManifest.xml' for '" + cls.getName() + "'. See Android documentation on https://developer.android.com/reference/android/app/job/JobService.html#PERMISSION_BIND" );

        Handler handler = new Handler( Looper.getMainLooper() );
        for( int i = 1; i < 5; i++ )
          handler.postDelayed( new Runnable()
          {
            @Override
            public void run()
            {
              Toast.makeText( context.getApplicationContext(), "Configuration error in AndroidManifest for '" + cls.getSimpleName() + "'! See logcat.", Toast.LENGTH_LONG ).show();
            }
          }, 2000 * i );
      }
      else
        throw e;
    }
  }

  public BackendlessPushService()
  {
    this.callback = this;
    if( BackendlessPushService.notificationIdGenerator == null )
      BackendlessPushService.notificationIdGenerator = new AtomicInteger( Backendless.getNotificationIdGeneratorInitValue() );
  }

  public BackendlessPushService( PushReceiverCallback callback )
  {
    this.callback = callback;
    if( BackendlessPushService.notificationIdGenerator == null )
      BackendlessPushService.notificationIdGenerator = new AtomicInteger( Backendless.getNotificationIdGeneratorInitValue() );
  }

  @Override
  public void onDestroy()
  {
    super.onDestroy();
    Backendless.saveNotificationIdGeneratorState( BackendlessPushService.notificationIdGenerator.get() );
  }

  @Override
  final protected void onHandleWork( @NonNull Intent intent )
  {
    handleIntent( this, intent );
  }

  public void onRegistered( Context context, String registrationId )
  {
  }

  public void onUnregistered( Context context, Boolean unregistered )
  {
  }

  public boolean onMessage( Context context, Intent intent )
  {
    Log.i( TAG, "A silent notification has been received by Backendless Push Service. The notification has not been handled since it requires a custom push service class which extends from com.backendless.push.BackendlessPushService. The notification payload can be found within intent extras: intent.getStringExtra(PublishOptions.<CONSTANT_VALUE>)." );
    return true;
  }

  public void onError( Context context, String message )
  {
    throw new RuntimeException( message );
  }

  void handleIntent( Context context, Intent intent )
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
      GCMRegistrar.internalRegister( context, GCMRegistrar.getSenderId( context ) );
  }

  private void handleMessage( final Context context, Intent intent )
  {
    final String messageId = intent.getStringExtra( PublishOptions.MESSAGE_ID );
    final String message = intent.getStringExtra( PublishOptions.MESSAGE_TAG );
    final String contentTitle = intent.getStringExtra( PublishOptions.ANDROID_CONTENT_TITLE_TAG );
    final String summarySubText = intent.getStringExtra( PublishOptions.ANDROID_SUMMARY_SUBTEXT_TAG );

    int notificationId = BackendlessPushService.notificationIdGenerator.getAndIncrement();

    try
    {
      final String templateName = intent.getStringExtra( PublishOptions.TEMPLATE_NAME );
      if( templateName != null )
      {
        if( PushTemplateHelper.getPushNotificationTemplates() == null )
          PushTemplateHelper.restorePushTemplates();

        AndroidPushTemplate androidPushTemplate = PushTemplateHelper.getPushNotificationTemplates().get( templateName );
        if( androidPushTemplate != null )
        {
          if( androidPushTemplate.getContentAvailable() != null && androidPushTemplate.getContentAvailable() == 1 )
          {
            callback.onMessage( context, intent );
            return;
          }

          Notification notification = PushTemplateHelper.convertFromTemplate( context, androidPushTemplate, message, messageId, contentTitle, summarySubText, notificationId );
          PushTemplateHelper.showNotification( context, notification, androidPushTemplate.getName(), notificationId );
        }
        return;
      }

      String immediatePush = intent.getStringExtra( PublishOptions.ANDROID_IMMEDIATE_PUSH );
      if( immediatePush != null )
      {
        AndroidPushTemplate androidPushTemplate = (AndroidPushTemplate) weborb.util.io.Serializer.fromBytes( immediatePush.getBytes(), weborb.util.io.Serializer.JSON, false );

        if( androidPushTemplate.getContentAvailable() != null && androidPushTemplate.getContentAvailable() == 1 )
        {
          callback.onMessage( context, intent );
          return;
        }

        if( androidPushTemplate.getName() == null || androidPushTemplate.getName().isEmpty() )
          androidPushTemplate.setName( BackendlessPushService.IMMEDIATE_MESSAGE );

        if( android.os.Build.VERSION.SDK_INT > 25 )
        {
          if( PushTemplateHelper.getNotificationChannel( context, androidPushTemplate.getName() ) == null )
            androidPushTemplate.setName( BackendlessPushService.IMMEDIATE_MESSAGE );
        }

        Notification notification = PushTemplateHelper.convertFromTemplate( context, androidPushTemplate, message, messageId, contentTitle, summarySubText, notificationId );
        PushTemplateHelper.showNotification( context, notification, androidPushTemplate.getName(), notificationId );
        return;
      }

      boolean showPushNotification = callback.onMessage( context, intent );

      if( showPushNotification )
        fallBackMode( context, message, contentTitle, summarySubText, notificationId );
    }
    catch ( Throwable throwable )
    {
      Log.e( TAG, "Error processing push notification", throwable );
    }
  }

  private void fallBackMode( Context context, String message, String contentTitle, String summarySubText, final int notificationId )
  {
    final String channelName = "Fallback";
    final NotificationCompat.Builder notificationBuilder;

    // android.os.Build.VERSION_CODES.O == 26
    if( android.os.Build.VERSION.SDK_INT > 25 )
    {
      final String channelId = Backendless.getApplicationId() + ":" + channelName;
      NotificationManager notificationManager = (NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE );
      NotificationChannel notificationChannel = notificationManager.getNotificationChannel( channelId );

      if( notificationChannel == null )
      {
        notificationChannel = new NotificationChannel( channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT );
        notificationManager.createNotificationChannel( notificationChannel );
      }

      notificationBuilder = new NotificationCompat.Builder( context, notificationChannel.getId() );
    }
    else
      notificationBuilder = new NotificationCompat.Builder( context );

    int appIcon = context.getApplicationInfo().icon;
    if( appIcon == 0 )
      appIcon = android.R.drawable.sym_def_app_icon;

    Intent notificationIntent = context.getPackageManager().getLaunchIntentForPackage( context.getApplicationInfo().packageName );
    PendingIntent contentIntent = PendingIntent.getActivity( context, 0, notificationIntent, 0 );

    notificationBuilder.setContentIntent( contentIntent )
            .setSmallIcon( appIcon )
            .setContentTitle( contentTitle )
            .setSubText( summarySubText )
            .setContentText( message )
            .setWhen( System.currentTimeMillis() )
            .setAutoCancel( true )
            .build();

    final NotificationManagerCompat notificationManager = NotificationManagerCompat.from( context );
    Handler handler = new Handler( Looper.getMainLooper() );
    handler.post( new Runnable()
    {
      @Override
      public void run()
      {
        notificationManager.notify( channelName, notificationId, notificationBuilder.build() );
      }
    } );
  }

  private void handleRegistration( final Context context, Intent intent )
  {
    String registrationIds = intent.getStringExtra( GCMConstants.EXTRA_REGISTRATION_IDS );
    String error = intent.getStringExtra( GCMConstants.EXTRA_ERROR );
    String unregistered = intent.getStringExtra( GCMConstants.EXTRA_UNREGISTERED );
    boolean isInternal = intent.getBooleanExtra( GCMConstants.EXTRA_IS_INTERNAL, false );

    // registration succeeded
    if( registrationIds != null )
    {
      if( isInternal )
      {
        callback.onRegistered( context, registrationIds );
      }

      GCMRegistrar.resetBackoff( context );
      GCMRegistrar.setGCMdeviceToken( context, registrationIds );
      registerFurther( context, registrationIds );
      return;
    }

    // unregistration succeeded
    if( unregistered != null )
    {
      // Remember we are unregistered
      GCMRegistrar.resetBackoff( context );
      GCMRegistrar.setGCMdeviceToken( context, "" );
      GCMRegistrar.setChannels( context, Collections.<String>emptyList() );
      GCMRegistrar.setRegistrationExpiration( context, null );
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
      callback.onError( context, error );
    }
  }

  private void registerFurther( final Context context, String GCMregistrationId )
  {
    final long registrationExpiration = GCMRegistrar.getRegistrationExpiration( context );
    Backendless.Messaging.registerDeviceOnServer( GCMregistrationId, new ArrayList<>( GCMRegistrar.getChannels( context ) ), registrationExpiration, new AsyncCallback<String>()
    {
      @Override
      public void handleResponse( String registrationInfo )
      {
        String ids;
        try
        {
          Object[] obj = (Object[]) weborb.util.io.Serializer.fromBytes( registrationInfo.getBytes(), weborb.util.io.Serializer.JSON, false );
          ids = (String) obj[0];
          PushTemplateHelper.deleteNotificationChannel( context );
          Map<String,AndroidPushTemplate> templates = (Map<String,AndroidPushTemplate>) obj[1];

          if( android.os.Build.VERSION.SDK_INT > 25 )
            for( AndroidPushTemplate templ : templates.values() )
              PushTemplateHelper.getOrCreateNotificationChannel( context.getApplicationContext(), templ );

          PushTemplateHelper.setPushNotificationTemplates( templates, registrationInfo.getBytes() );
        }
        catch( IOException e )
        {
          callback.onError( context, "Could not deserialize server response: " + e.getMessage() );
          return;
        }
        GCMRegistrar.setRegistrationIds( context, ids, registrationExpiration );
        callback.onRegistered( context, registrationInfo );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        callback.onError( context, "Could not register device on Backendless server: " + fault.toString() );
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
        GCMRegistrar.setRegistrationIds( context, "", 0 );
        callback.onUnregistered( context, unregistered );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        callback.onError( context, "Could not unregister device on Backendless server: " + fault.toString() );
      }
    } );
  }
}
