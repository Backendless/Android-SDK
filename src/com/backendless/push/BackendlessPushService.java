package com.backendless.push;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.util.Log;
import android.widget.RemoteViews;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.Action;
import com.backendless.messaging.AndroidPushTemplate;
import com.backendless.messaging.PublishOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class BackendlessPushService extends IntentService implements PushReceiverCallback
{
  private static final String TAG = "BackendlessPushService";
  private static final Random random = new Random();

  private static final int MAX_BACKOFF_MS = (int) TimeUnit.SECONDS.toMillis( 3600 );
  private static final String TOKEN = Long.toBinaryString( random.nextLong() );
  private static final String EXTRA_TOKEN = "token";
  private static Map<String, AndroidPushTemplate> pushNotificationTemplateDTOs;

  private PushReceiverCallback callback;

  public BackendlessPushService()
  {
    this( "BackendlessPushService" );
  }

  public BackendlessPushService( String name )
  {
    super( name );
    this.callback = this;
  }

  public BackendlessPushService( PushReceiverCallback callback )
  {
    super(null);
    this.callback = callback;
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
    try
    {
      handleIntent( this, intent );
    }
    finally
    {
      BackendlessBroadcastReceiver.completeWakefulIntent( intent );
    }
  }

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
    final int messageId = intent.getIntExtra( BackendlessBroadcastReceiver.EXTRA_MESSAGE_ID, 0 );
    final String contentText = intent.getStringExtra( PublishOptions.ANDROID_CONTENT_TEXT_TAG );

    try
    {
      final String templateName = intent.getStringExtra( PublishOptions.TEMPLATE_NAME );
      if( templateName != null )
      {
        if (pushNotificationTemplateDTOs == null)
          restorePushTemplates();
        AndroidPushTemplate androidPushTemplateDTO = pushNotificationTemplateDTOs.get( templateName );
        Notification notification = convertFromTemplate( context, androidPushTemplateDTO, contentText, messageId );
        showNotification( notification, androidPushTemplateDTO.getName(), messageId );
        return;
      }

      String immediatePush = intent.getStringExtra( PublishOptions.ANDROID_IMMEDIATE_PUSH );
      if( immediatePush != null )
      {
        AndroidPushTemplate androidPushTemplateDTO = (AndroidPushTemplate) weborb.util.io.Serializer.fromBytes( immediatePush.getBytes(), weborb.util.io.Serializer.JSON, false );
        Notification notification = convertFromTemplate( context, androidPushTemplateDTO, contentText, messageId );
        showNotification( notification, androidPushTemplateDTO.getName(), messageId );
        return;
      }

      boolean showPushNotification = callback.onMessage( context, intent );

      if( showPushNotification )
      {
        CharSequence tickerText = intent.getStringExtra( PublishOptions.ANDROID_TICKER_TEXT_TAG );
        CharSequence contentTitle = intent.getStringExtra( PublishOptions.ANDROID_CONTENT_TITLE_TAG );

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
          notificationManager.notify( intent.getIntExtra( BackendlessBroadcastReceiver.EXTRA_MESSAGE_ID, 0 ), notification );
        }
      }
    }
    catch ( Throwable throwable )
    {
      Log.e( TAG, "Error processing push notification", throwable );
    }
  }

  private Notification convertFromTemplate( Context context, AndroidPushTemplate templateDTO, String messageText, int messageId )
  {
    // Notification channel ID is ignored for Android 7.1.1 (API level 25) and lower.

    NotificationCompat.Builder notificationBuilder;
    // android.os.Build.VERSION_CODES.O == 26
    if( android.os.Build.VERSION.SDK_INT >= 26 )
    {
      final String channelId = Backendless.getApplicationId() + ":" + templateDTO.getName();
      NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );

      NotificationChannel notificationChannel = notificationManager.getNotificationChannel( channelId );

      if( notificationChannel == null )
      {
        notificationChannel = this.createNotificationChannel( channelId, templateDTO );
        notificationManager.createNotificationChannel( notificationChannel );
      }

      notificationBuilder = new NotificationCompat.Builder( getApplicationContext(), channelId );
      notificationBuilder.setDefaults( Notification.DEFAULT_ALL );

      if( templateDTO.getColorized() != null )
        notificationBuilder.setColorized( templateDTO.getColorized() );

      if( templateDTO.getBadge() != null )
        notificationBuilder.setBadgeIconType( templateDTO.getBadge() ); // Notification.BADGE_ICON_SMALL

      if( templateDTO.getCancelAfter() != null && templateDTO.getCancelAfter() != 0 )
        notificationBuilder.setTimeoutAfter( templateDTO.getCancelAfter() );
    }
    else
    {
      notificationBuilder = new NotificationCompat.Builder( getApplicationContext() );
      notificationBuilder.setDefaults( Notification.DEFAULT_ALL );
      notificationBuilder.setPriority( templateDTO.getPriority() ); // Notification.PRIORITY_DEFAULT

      if( templateDTO.getButtonTemplate().getSound() != null )
        notificationBuilder.setSound( Uri.parse( templateDTO.getButtonTemplate().getSound() ) );

      if( templateDTO.getButtonTemplate().getVibrate() != null )
      {
        long[] vibrate = new long[ templateDTO.getButtonTemplate().getVibrate().length ];
        int index = 0;
        for( long l : templateDTO.getButtonTemplate().getVibrate() )
          vibrate[ index++ ] = l;

        notificationBuilder.setVibrate( vibrate );
      }

      notificationBuilder.setVisibility( templateDTO.getButtonTemplate().getVisibility() );
    }

    try
    {
      InputStream is = (InputStream) new URL( templateDTO.getAttachmentUrl() ).getContent();
      Bitmap bitmap = BitmapFactory.decodeStream( is );
      if( bitmap != null )
        notificationBuilder.setStyle( new NotificationCompat.BigPictureStyle().bigPicture( bitmap ) );
      else
        Log.i( TAG, "Cannot convert rich media for notification into bitmap." );
    }
    catch( IOException e )
    {
      Log.e( TAG, "Cannot receive rich media for notification." );
    }

    notificationBuilder
            .setDefaults( Notification.DEFAULT_ALL )
            .setShowWhen( true )
            .setWhen( System.currentTimeMillis() )
            .setSmallIcon( Integer.parseInt( templateDTO.getIcon() ) )
            .setColor( templateDTO.getColorCode() )
            .setLights( templateDTO.getLightsColor(), templateDTO.getLightsOnMs(), templateDTO.getLightsOffMs() )
            .setAutoCancel( templateDTO.getCancelOnTap() )
            .setTicker( templateDTO.getTickerText() )
            .setContentTitle( templateDTO.getFirstRowTitle() )
            .setContentText( messageText );

    Intent notificationIntent = context.getPackageManager().getLaunchIntentForPackage( context.getPackageName() );
    notificationIntent.putExtra( BackendlessBroadcastReceiver.EXTRA_MESSAGE_ID, messageId );
    notificationIntent.putExtra( PublishOptions.TEMPLATE_NAME, templateDTO.getName() );
    notificationIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
    PendingIntent contentIntent = PendingIntent.getActivity( context, messageId * 3, notificationIntent, 0 );
    notificationBuilder.setContentIntent( contentIntent );

    if (templateDTO.getButtonTemplate().getActions() != null)
    {
      List<NotificationCompat.Action> actions = createActions(context, templateDTO.getButtonTemplate().getActions(), templateDTO.getName(), messageId);
      for( NotificationCompat.Action action : actions )
        notificationBuilder.addAction( action );
    }

    return notificationBuilder.build();
  }

  private List<NotificationCompat.Action> createActions( Context context, Action[] actions, String templateName, int messageId )
  {
    List<NotificationCompat.Action> notifActions = new ArrayList<>();

    int i = 1;
    for( Action a : actions )
    {
      Intent actionIntent = new Intent( a.getTitle() );
      actionIntent.setClassName( context, a.getId() );
      actionIntent.putExtra( BackendlessBroadcastReceiver.EXTRA_MESSAGE_ID, messageId );
      actionIntent.putExtra( PublishOptions.TEMPLATE_NAME, templateName );
      actionIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );

      // user should use messageId and tag(templateName) to cancel notification.

      PendingIntent pendingIntent = PendingIntent.getActivity( context, messageId * 3 + i++, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT );

      NotificationCompat.Action.Builder actionBuilder = new NotificationCompat.Action.Builder( 0, a.getTitle(), pendingIntent );

      if( a.getOptions() == 1 )
      {
        RemoteInput remoteInput = new RemoteInput.Builder( PublishOptions.INLINE_REPLY ).build();
        actionBuilder.setAllowGeneratedReplies( true ).addRemoteInput( remoteInput );
      }
      notifActions.add( actionBuilder.build() );
    }

    return notifActions;
  }

  private NotificationChannel createNotificationChannel( final String channelId, final AndroidPushTemplate templateDTO )
  {
    NotificationChannel notificationChannel = new NotificationChannel( channelId, templateDTO.getName(), NotificationManager.IMPORTANCE_DEFAULT );
    notificationChannel.setShowBadge( templateDTO.getButtonTemplate().getShowBadge() );
    notificationChannel.setImportance( templateDTO.getPriority() ); // NotificationManager.IMPORTANCE_DEFAULT

    if( templateDTO.getButtonTemplate().getSound() != null )
      notificationChannel.setSound( Uri.parse( templateDTO.getButtonTemplate().getSound() ), null );

    notificationChannel.enableLights( true );
    notificationChannel.setLightColor( templateDTO.getLightsColor() );

    if( templateDTO.getButtonTemplate().getVibrate() != null )
    {
      long[] vibrate = new long[ templateDTO.getButtonTemplate().getVibrate().length ];
      int index = 0;
      for( long l : templateDTO.getButtonTemplate().getVibrate() )
        vibrate[ index++ ] = l;

      notificationChannel.setVibrationPattern( vibrate );
    }

    notificationChannel.setLockscreenVisibility( templateDTO.getButtonTemplate().getVisibility() ); // Notification.VISIBILITY_PUBLIC
    notificationChannel.setBypassDnd( templateDTO.getButtonTemplate().getBypassDND() );
    return notificationChannel;
  }

  private void showNotification( final Notification notification, final String tag, final int messageId )
  {
    final NotificationManagerCompat notificationManager = NotificationManagerCompat.from( getApplicationContext() );
    Handler handler = new Handler( Looper.getMainLooper() );
    handler.post( new Runnable()
    {
      @Override
      public void run()
      {
        notificationManager.notify( tag, messageId, notification );
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
      GCMRegistrar.setRegistrationExpiration( context, -1 );
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
          BackendlessPushService.pushNotificationTemplateDTOs = Collections.unmodifiableMap( (Map<String,AndroidPushTemplate>) obj[1] );
          savePushTemplates( registrationInfo.getBytes() );
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

  private void savePushTemplates( byte[] rawBytes )
  {
    try
    {
      JSONArray jsonArray = new JSONArray( new String( rawBytes));
      JSONObject templates = jsonArray.getJSONObject( 1 );
      Backendless.savePushTemplates( templates.toString() );
    }
    catch( JSONException e )
    {
      Log.w( TAG, "Cannot deserialize AndroidPushTemplate to JSONObject.", e );
    }
  }

  private void restorePushTemplates()
  {
    String rawTemplates = Backendless.getPushTemplatesAsJson();

    if (rawTemplates == null)
    {
      pushNotificationTemplateDTOs = Collections.emptyMap();
      return;
    }

    Map<String, AndroidPushTemplate> templates;
    try
    {
      templates = (Map<String, AndroidPushTemplate>) weborb.util.io.Serializer.fromBytes( rawTemplates.getBytes(), weborb.util.io.Serializer.JSON, false );
      pushNotificationTemplateDTOs = Collections.unmodifiableMap(templates);
    }
    catch( IOException e )
    {
      pushNotificationTemplateDTOs = Collections.emptyMap();
      Log.w( TAG, "Cannot deserialize AndroidPushTemplate to JSONObject.", e );
    }
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
