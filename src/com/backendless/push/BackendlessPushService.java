package com.backendless.push;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.media.AudioAttributes;
import android.media.AudioManager;
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
import java.util.HashMap;
import java.util.List;
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
  private static final String TAG = BackendlessPushService.class.getSimpleName();

  public static final String ACTION_FCM_REGISTRATION = "BackendlessPushService.fcm.registration";
  public static final String ACTION_FCM_UNREGISTRATION = "BackendlessPushService.fcm.unregistration";
  public static final String ACTION_FCM_REFRESH_TOKEN = "BackendlessPushService.fcm.refresh_token";
  public static final String ACTION_FCM_ONMESSAGE = "BackendlessPushService.fcm.onMessage";

  public static final String KEY_DEVICE_TOKEN = "BackendlessPushService.deviceToken";
  public static final String KEY_CHANNELS = "BackendlessPushService.channels";
  public static final String KEY_EXPIRATION = "BackendlessPushService.expiration";

  public static final String IMMEDIATE_MESSAGE = "ImmediateMessage";
  private static final int JOB_ID = 1000;
  private static final Random random = new Random( System.currentTimeMillis() );

  private static final int MAX_BACKOFF_MS = (int) TimeUnit.SECONDS.toMillis( 3600 );
  private static final String TOKEN = Long.toBinaryString( random.nextLong() );
  private static final String EXTRA_TOKEN = "token";
  private static AtomicInteger notificationIdGenerator;

  private PushReceiverCallback callback;

  private static Boolean isFCM;
  private static String pushServiceClassName;


  private static String getPushServiceClassName( Context appContext )
  {
    if( BackendlessPushService.pushServiceClassName != null )
      return BackendlessPushService.pushServiceClassName;

    ClassLoader clsLoader = appContext.getClassLoader();

    PackageManager packageManager = appContext.getPackageManager();
    PackageInfo packageInfo;
    try
    {
      packageInfo = packageManager.getPackageInfo( appContext.getPackageName(), PackageManager.GET_SERVICES );
      ServiceInfo[] services = packageInfo.services;

      for( ServiceInfo srvInfo : services )
      {
        try
        {
          if( BackendlessPushService.class.isAssignableFrom( clsLoader.loadClass( srvInfo.name ) ) )
          {
            BackendlessPushService.pushServiceClassName = srvInfo.name;
            break;
          }
        }
        catch( ClassNotFoundException e )
        {
          Log.w( TAG, "You have declared class in AndroidManifest.xml that is not present in your app.", e );
        }
      }
    }
    catch( PackageManager.NameNotFoundException e )
    {
      Log.e( TAG, "Can not load current app package." );
      throw new IllegalStateException( "Can not load current app package.", e );
    }

    if( BackendlessPushService.pushServiceClassName == null )
    {
      throw new IllegalStateException(
        "A reference to BackendlessPushService is missing in AndroidManifest.xml. Make sure to add" +
        "<service android:name=\"com.backendless.push.BackendlessPushService\" /> to the manifest file." +
        "Alternatively, if you have a custom push service implementation, make sure your class inherits from" +
        "\"com.backendless.push.BackendlessPushService\" and is registered in the manifest file" +
        "as <service android:name=\"your push service fully qualified class name\" />" );
    }
    else
      return BackendlessPushService.pushServiceClassName;
  }

  static void enqueueWork( final Context context, final Intent work )
  {
    final String className = getPushServiceClassName( context );
    ComponentName comp = new ComponentName( context, className );
    try
    {
      JobIntentService.enqueueWork( context, comp, JOB_ID, work.setComponent( comp ) );
    }
    catch( RuntimeException e )
    {
      if( e.getMessage() != null && e.getMessage().contains( "does not require android.permission.BIND_JOB_SERVICE" ) )
      {
        Log.e( BackendlessPushService.class.getSimpleName(), "You should set the 'android.permission.BIND_JOB_SERVICE' permission in 'AndroidManifest.xml' for '" + className + "'. See Android documentation on https://developer.android.com/reference/android/app/job/JobService.html#PERMISSION_BIND" );

        Handler handler = new Handler( Looper.getMainLooper() );
        for( int i = 1; i < 5; i++ )
          handler.postDelayed( new Runnable()
          {
            @Override
            public void run()
            {
              Toast.makeText( context.getApplicationContext(), "Configuration error in AndroidManifest for '" + className + "'! See logcat.", Toast.LENGTH_LONG ).show();
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
    String action = intent.getAction();

    switch( action )
    {
      case GCMConstants.INTENT_FROM_GCM_REGISTRATION_CALLBACK:
        handleRegistration( this, intent );
        break;
      case GCMConstants.INTENT_FROM_GCM_MESSAGE:
        handleMessage( this, intent );
        break;
      case GCMConstants.INTENT_FROM_GCM_LIBRARY_RETRY:
        handleRetry( this, intent );
        break;

      case BackendlessPushService.ACTION_FCM_REGISTRATION:
        registerOnBackendless( this, intent );
        break;
      case BackendlessPushService.ACTION_FCM_UNREGISTRATION:
        unregisterOnBackendless( this, intent );
        break;
      case BackendlessPushService.ACTION_FCM_REFRESH_TOKEN:
        refreshTokenOnBackendless( this, intent );
        break;
      case BackendlessPushService.ACTION_FCM_ONMESSAGE:
        handleMessage( this, intent );
        break;
    }
  }

  @Override
  public void onRegistered( Context context, Map<String, String> channelRegistrations )
  {
    StringBuilder sb = new StringBuilder();

    for( String id : channelRegistrations.values() )
      sb.append( id ).append( ',' );

    sb.delete( sb.length() - 1, sb.length() );
    onRegistered( context, sb.toString() );
  }

  /**
   * Used for GCM
   * @param context
   * @param registrationIds
   */
  @Deprecated
  @Override
  public void onRegistered( Context context, String registrationIds )
  {
  }

  @Override
  public void onUnregistered( Context context, Boolean unregistered )
  {
  }

  @Override
  public boolean onMessage( Context context, Intent intent )
  {
    Log.i( TAG, "A silent notification has been received by Backendless Push Service. The notification has not been handled since it requires a custom push service class which extends from com.backendless.push.BackendlessPushService. The notification payload can be found within intent extras: intent.getStringExtra(PublishOptions.<CONSTANT_VALUE>)." );
    return true;
  }

  @Override
  public void onError( Context context, String message )
  {
    throw new RuntimeException( message );
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
    boolean showPushNotification = callback.onMessage( context, intent );
    if( !showPushNotification )
      return;

    int notificationId = BackendlessPushService.notificationIdGenerator.getAndIncrement();

    try
    {
      String immediatePush = intent.getStringExtra( PublishOptions.ANDROID_IMMEDIATE_PUSH );
      if( immediatePush != null )
      {
        AndroidPushTemplate androidPushTemplate = (AndroidPushTemplate) weborb.util.io.Serializer.fromBytes( immediatePush.getBytes(), weborb.util.io.Serializer.JSON, false );

        if( androidPushTemplate.getContentAvailable() != null && androidPushTemplate.getContentAvailable() == 1 )
          return;

        if( androidPushTemplate.getName() == null || androidPushTemplate.getName().isEmpty() )
          androidPushTemplate.setName( BackendlessPushService.IMMEDIATE_MESSAGE );

        if( android.os.Build.VERSION.SDK_INT > 25 )
        {
          if( PushTemplateHelper.getNotificationChannel( context, androidPushTemplate.getName() ) == null )
            androidPushTemplate.setName( BackendlessPushService.IMMEDIATE_MESSAGE );
        }

        Notification notification = PushTemplateHelper.convertFromTemplate( context, androidPushTemplate, intent.getExtras().deepCopy(), notificationId );
        PushTemplateHelper.showNotification( context, notification, androidPushTemplate.getName(), notificationId );
        return;
      }

      final String templateName = intent.getStringExtra( PublishOptions.TEMPLATE_NAME );
      if( templateName != null )
      {
        AndroidPushTemplate androidPushTemplate = PushTemplateHelper.getPushNotificationTemplates().get( templateName );
        if( androidPushTemplate != null )
        {
          if( androidPushTemplate.getContentAvailable() != null && androidPushTemplate.getContentAvailable() == 1 )
            return;

          Notification notification = PushTemplateHelper.convertFromTemplate( context, androidPushTemplate, intent.getExtras().deepCopy(), notificationId );
          intent.getExtras().deepCopy();
          PushTemplateHelper.showNotification( context, notification, androidPushTemplate.getName(), notificationId );
        }
        return;
      }


      final String message = intent.getStringExtra( PublishOptions.MESSAGE_TAG );
      final String contentTitle = intent.getStringExtra( PublishOptions.ANDROID_CONTENT_TITLE_TAG );
      final String summarySubText = intent.getStringExtra( PublishOptions.ANDROID_SUMMARY_SUBTEXT_TAG );
      String soundResource = intent.getStringExtra( PublishOptions.ANDROID_CONTENT_SOUND_TAG );
      fallBackMode( context, message, contentTitle, summarySubText, soundResource, notificationId );
    }
    catch ( Throwable throwable )
    {
      Log.e( TAG, "Error processing push notification", throwable );
    }
  }

  private void fallBackMode( Context context, String message, String contentTitle, String summarySubText, String soundResource, final int notificationId )
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

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage( AudioAttributes.USAGE_NOTIFICATION_RINGTONE )
                .setContentType( AudioAttributes.CONTENT_TYPE_SONIFICATION )
                .setFlags( AudioAttributes.FLAG_AUDIBILITY_ENFORCED )
                .setLegacyStreamType( AudioManager.STREAM_NOTIFICATION )
                .build();

        notificationChannel.setSound( PushTemplateHelper.getSoundUri( context, soundResource ), audioAttributes );
        notificationManager.createNotificationChannel( notificationChannel );
      }

      notificationBuilder = new NotificationCompat.Builder( context, notificationChannel.getId() );
    }
    else
      notificationBuilder = new NotificationCompat.Builder( context );

    notificationBuilder.setSound( PushTemplateHelper.getSoundUri( context, soundResource ), AudioManager.STREAM_NOTIFICATION );

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
        Map<String, String> channelRegistrations = processRegistrationPayload( context, registrationInfo );

        StringBuilder sb = new StringBuilder();

        for( String id : channelRegistrations.values() )
          sb.append( id ).append( ',' );

        sb.delete( sb.length() - 1, sb.length() );

        GCMRegistrar.setRegistrationIds( context, sb.toString(), registrationExpiration );
        callback.onRegistered( context, sb.toString() );
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

  private Map<String, String> processRegistrationPayload( final Context context, final String payload )
  {
    Object[] obj;
    try
    {
      obj = (Object[]) weborb.util.io.Serializer.fromBytes( payload.getBytes(), weborb.util.io.Serializer.JSON, false );
    }
    catch( IOException e )
    {
      Log.e( TAG, "Could not deserialize server response: " + e.getMessage() );
      callback.onError( context, "Could not deserialize server response: " + e.getMessage() );
      return null;
    }

    PushTemplateHelper.deleteNotificationChannel( context );
    Map<String, AndroidPushTemplate> templates = (Map<String, AndroidPushTemplate>) obj[ 1 ];

    if( android.os.Build.VERSION.SDK_INT > 25 )
    {
      for( AndroidPushTemplate templ : templates.values() )
        PushTemplateHelper.getOrCreateNotificationChannel( context.getApplicationContext(), templ );
    }

    PushTemplateHelper.setPushNotificationTemplates( templates, payload.getBytes() );

    String regs = (String) obj[ 0 ];
    Map<String, String> channelRegistrations = new HashMap<>();
    String[] regPairs = regs.split( "," );

    for( String pair : regPairs )
    {
      String[] valueKey = pair.split( "::" );
      channelRegistrations.put( valueKey[1], valueKey[0] );
    }

    return channelRegistrations;
  }

  private void registerOnBackendless( final Context context, Intent intent )
  {
    String deviceToken = intent.getStringExtra( BackendlessPushService.KEY_DEVICE_TOKEN );
    List<String> channels = intent.getStringArrayListExtra( BackendlessPushService.KEY_CHANNELS );
    Long expiration = intent.getLongExtra( BackendlessPushService.KEY_EXPIRATION, 0 );

    Backendless.Messaging.registerDeviceOnServer( deviceToken, channels, expiration, new AsyncCallback<String>()
    {
      @Override
      public void handleResponse( String response )
      {
        Log.d( TAG, "Registered on Backendless." );
        Map<String, String> channelRegistrations = processRegistrationPayload( context, response );
        callback.onRegistered( context, channelRegistrations );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        Log.d( TAG, "Could not register device on Backendless server: " + fault.toString() );
        callback.onError( context, "Could not register device on Backendless server: " + fault.toString() );
      }
    } );
  }

  private void unregisterOnBackendless( final Context context, Intent intent )
  {
    List<String> channels = intent.getStringArrayListExtra( BackendlessPushService.KEY_CHANNELS );

    Backendless.Messaging.unregisterDeviceOnServer( channels, new AsyncCallback<Integer>()
    {
      @Override
      public void handleResponse( Integer response )
      {
        Log.d( TAG, "Unregistered on Backendless." );
        if( response < 1 )
          FCMRegistration.unregisterDeviceOnFCM( context, callback );
        else
          callback.onUnregistered( context, true );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        Log.d( TAG, "Could not unregister device on Backendless server: " + fault.toString() );
        callback.onError( context, "Could not unregister device on Backendless server: " + fault.toString() );
      }
    } );
  }

  private void refreshTokenOnBackendless( final Context context, Intent intent )
  {
    String newDeviceToken = intent.getStringExtra( BackendlessPushService.KEY_DEVICE_TOKEN );

    Backendless.Messaging.refreshDeviceToken( newDeviceToken, new AsyncCallback<Boolean>()
    {
      @Override
      public void handleResponse( Boolean response )
      {
        if( response )
          Log.d( TAG, "Device token refreshed successfully." );
        else
        {
          Log.d( TAG, "Device is not registered on any channel." );
          FCMRegistration.unregisterDeviceOnFCM( context, callback );
        }
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        Log.e( TAG, "Can not refresh device token on Backendless. " + fault.getMessage() );
      }
    } );
  }

  public static boolean isFCM( Context appContext )
  {
    if( isFCM != null )
      return isFCM;

    ClassLoader clsLoader = appContext.getClassLoader();

    try
    {
      clsLoader.loadClass( "com.google.firebase.messaging.FirebaseMessagingService" );
    }
    catch( ClassNotFoundException e )
    {
      Log.i( TAG, "Class FirebaseMessagingService not found. GCM will be used." );
      return isFCM = false;
    }

    PackageManager packageManager = appContext.getPackageManager();
    PackageInfo packageInfo;
    String srvClassName = null;
    try
    {
      packageInfo = packageManager.getPackageInfo( appContext.getPackageName(), PackageManager.GET_SERVICES );
      ServiceInfo[] services = packageInfo.services;

      boolean flag = false;

      Class<?> fcmService;
      try
      {
        fcmService = clsLoader.loadClass( "com.backendless.push.BackendlessFCMService" );
      }
      catch( ClassNotFoundException e )
      {
        Log.e( TAG, "Can not load declared service class.", e );
        throw new IllegalStateException( "Can not load declared service class.", e );
      }

      for( ServiceInfo srvInfo : services )
      {
        try
        {
          if( fcmService.isAssignableFrom( clsLoader.loadClass( srvInfo.name ) ) )
          {
            flag = true;
            srvClassName = srvInfo.name;
            break;
          }
        }
        catch( ClassNotFoundException e )
        {
          Log.w( TAG, "You have declared class in AndroidManifest.xml that is not present in your app.", e );
        }
      }

      if( !flag )
      {
        Log.i( TAG, "Class FirebaseMessagingService not found. GCM will be used." );
        return isFCM = false;
      }
    }
    catch( PackageManager.NameNotFoundException e )
    {
      Log.e( TAG, "Can not load current app package." );
      throw new IllegalStateException( "Can not load current app package.", e );
    }

    final String action = "com.google.firebase.MESSAGING_EVENT";
    Intent intent = new Intent( action );
    intent.setClassName( appContext.getPackageName(), srvClassName );
    List<ResolveInfo> srvIntentFilters = packageManager.queryIntentServices( intent, PackageManager.GET_INTENT_FILTERS );

    if( srvIntentFilters.isEmpty() )
      throw new IllegalStateException( "Missed intent-filter action " + action );

    return isFCM = true;
  }
}
