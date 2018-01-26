package com.backendless.push;

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
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.util.Log;
import com.backendless.Backendless;
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


public class PushTemplateHelper
{
  private static Map<String, AndroidPushTemplate> pushNotificationTemplates;

  public static Map<String, AndroidPushTemplate> getPushNotificationTemplates()
  {
    return pushNotificationTemplates;
  }

  public static void setPushNotificationTemplates( Map<String, AndroidPushTemplate> pushNotificationTemplates, byte[] rawTemplates )
  {
    PushTemplateHelper.pushNotificationTemplates = Collections.unmodifiableMap( pushNotificationTemplates );
    savePushTemplates( rawTemplates );
  }

  private static void savePushTemplates( byte[] rawBytes )
  {
    try
    {
      JSONArray jsonArray = new JSONArray( new String( rawBytes));
      JSONObject templates = jsonArray.getJSONObject( 1 );
      Backendless.savePushTemplates( templates.toString() );
    }
    catch( JSONException e )
    {
      Log.w( PushTemplateHelper.class.getSimpleName(), "Cannot deserialize AndroidPushTemplate to JSONObject.", e );
    }
  }

  static Notification convertFromTemplate( Context context, AndroidPushTemplate template, String messageText, int messageId )
  {
    // Notification channel ID is ignored for Android 7.1.1 (API level 25) and lower.

    NotificationCompat.Builder notificationBuilder;
    // android.os.Build.VERSION_CODES.O == 26
    if( android.os.Build.VERSION.SDK_INT >= 26 )
    {
      final String channelId = Backendless.getApplicationId() + ":" + template.getName();
      NotificationManager notificationManager = (NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE );

      NotificationChannel notificationChannel = notificationManager.getNotificationChannel( channelId );

      if( notificationChannel == null )
      {
        notificationChannel = PushTemplateHelper.createNotificationChannel( channelId, template );
        notificationManager.createNotificationChannel( notificationChannel );
      }

      notificationBuilder = new NotificationCompat.Builder( context.getApplicationContext(), channelId );
      notificationBuilder.setDefaults( Notification.DEFAULT_ALL );

      if( template.getColorized() != null )
        notificationBuilder.setColorized( template.getColorized() );

      if( template.getBadge() != null )
        notificationBuilder.setBadgeIconType( template.getBadge() );
      else
        notificationBuilder.setBadgeIconType( Notification.BADGE_ICON_NONE );

      if( template.getCancelAfter() != null && template.getCancelAfter() != 0 )
        notificationBuilder.setTimeoutAfter( template.getCancelAfter() );
    }
    else
    {
      notificationBuilder = new NotificationCompat.Builder( context.getApplicationContext() );
      notificationBuilder.setDefaults( Notification.DEFAULT_ALL );

      if (template.getPriority() != null)
        notificationBuilder.setPriority( template.getPriority() );
      else
        notificationBuilder.setPriority( Notification.PRIORITY_DEFAULT );

      if( template.getButtonTemplate().getSound() != null )
        notificationBuilder.setSound( Uri.parse( template.getButtonTemplate().getSound() ) );

      if( template.getButtonTemplate().getVibrate() != null )
      {
        long[] vibrate = new long[ template.getButtonTemplate().getVibrate().length ];
        int index = 0;
        for( long l : template.getButtonTemplate().getVibrate() )
          vibrate[ index++ ] = l;

        notificationBuilder.setVibrate( vibrate );
      }

      if (template.getButtonTemplate().getVisibility() != null)
        notificationBuilder.setVisibility( template.getButtonTemplate().getVisibility() );
      else
        notificationBuilder.setVisibility( template.getButtonTemplate().getVisibility() );
    }

    try
    {
      InputStream is = (InputStream) new URL( template.getAttachmentUrl() ).getContent();
      Bitmap bitmap = BitmapFactory.decodeStream( is );

      if( bitmap != null )
        notificationBuilder.setStyle( new NotificationCompat.BigPictureStyle().bigPicture( bitmap ) );
      else
        Log.i( PushTemplateHelper.class.getSimpleName(), "Cannot convert rich media for notification into bitmap." );
    }
    catch( IOException e )
    {
      Log.e( PushTemplateHelper.class.getSimpleName(), "Cannot receive rich media for notification." );
    }

    int icon = 0;
    if( template.getIcon() != null )
      icon = context.getResources().getIdentifier( template.getIcon(), "drawable", context.getPackageName() );

    if( icon == 0 )
    {
      icon = context.getResources().getIdentifier( "ic_launcher", "drawable", context.getPackageName() );
      if( icon != 0 )
        notificationBuilder.setSmallIcon( icon );
    }

    if (template.getLightsColor() != null && template.getLightsOnMs() != null && template.getLightsOffMs() != null)
      notificationBuilder.setLights(template.getLightsColor(), template.getLightsOnMs(), template.getLightsOffMs());

    if (template.getColorCode() != null)
      notificationBuilder.setColor( template.getColorCode() );

    if (template.getCancelOnTap() != null)
      notificationBuilder.setAutoCancel( template.getCancelOnTap() );
    else
      notificationBuilder.setAutoCancel( false );

    notificationBuilder
            .setDefaults( Notification.DEFAULT_ALL )
            .setShowWhen( true )
            .setWhen( System.currentTimeMillis() )
            .setTicker( template.getTickerText() )
            .setContentTitle( template.getFirstRowTitle() )
            .setContentText( messageText );

    Intent notificationIntent = context.getPackageManager().getLaunchIntentForPackage( context.getPackageName() );
    notificationIntent.putExtra( BackendlessBroadcastReceiver.EXTRA_MESSAGE_ID, messageId );
    notificationIntent.putExtra( PublishOptions.TEMPLATE_NAME, template.getName() );
    notificationIntent.putExtra( PublishOptions.MESSAGE_TAG, messageText );
    notificationIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
    PendingIntent contentIntent = PendingIntent.getActivity( context, messageId * 3, notificationIntent, 0 );
    notificationBuilder.setContentIntent( contentIntent );

    if (template.getButtonTemplate().getActions() != null)
    {
      List<NotificationCompat.Action> actions = createActions( context, template.getButtonTemplate().getActions(), template.getName(), messageId, messageText );
      for( NotificationCompat.Action action : actions )
        notificationBuilder.addAction( action );
    }

    return notificationBuilder.build();
  }

  static private List<NotificationCompat.Action> createActions( Context context, Action[] actions, String templateName, int messageId, String messageText )
  {
    List<NotificationCompat.Action> notifActions = new ArrayList<>();

    int i = 1;
    for( Action a : actions )
    {
      Intent actionIntent = new Intent( a.getTitle() );
      actionIntent.setClassName( context, a.getId() );
      actionIntent.putExtra( BackendlessBroadcastReceiver.EXTRA_MESSAGE_ID, messageId );
      actionIntent.putExtra( PublishOptions.MESSAGE_TAG, messageText );
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

  static private NotificationChannel createNotificationChannel( final String channelId, final AndroidPushTemplate template )
  {
    NotificationChannel notificationChannel = new NotificationChannel( channelId, template.getName(), NotificationManager.IMPORTANCE_DEFAULT );
    notificationChannel.setShowBadge( template.getButtonTemplate().getShowBadge() );
    notificationChannel.setImportance( template.getPriority() ); // NotificationManager.IMPORTANCE_DEFAULT

    if( template.getButtonTemplate().getSound() != null )
      notificationChannel.setSound( Uri.parse( template.getButtonTemplate().getSound() ), null );

    notificationChannel.enableLights( true );
    notificationChannel.setLightColor( template.getLightsColor() );

    if( template.getButtonTemplate().getVibrate() != null )
    {
      long[] vibrate = new long[ template.getButtonTemplate().getVibrate().length ];
      int index = 0;
      for( long l : template.getButtonTemplate().getVibrate() )
        vibrate[ index++ ] = l;

      notificationChannel.setVibrationPattern( vibrate );
    }

    if (template.getButtonTemplate().getVisibility() != null)
      notificationChannel.setLockscreenVisibility( template.getButtonTemplate().getVisibility() );
    else
      notificationChannel.setLockscreenVisibility( Notification.VISIBILITY_PUBLIC );

    if( template.getButtonTemplate().getBypassDND() != null )
      notificationChannel.setBypassDnd( template.getButtonTemplate().getBypassDND() );
    else
      notificationChannel.setBypassDnd( false );

    return notificationChannel;
  }

  static void showNotification( final Context context, final Notification notification, final String tag, final int messageId )
  {
    final NotificationManagerCompat notificationManager = NotificationManagerCompat.from( context.getApplicationContext() );
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

  static void restorePushTemplates()
  {
    String rawTemplates = Backendless.getPushTemplatesAsJson();

    if (rawTemplates == null)
    {
      pushNotificationTemplates = Collections.emptyMap();
      return;
    }

    Map<String, AndroidPushTemplate> templates;
    try
    {
      templates = (Map<String, AndroidPushTemplate>) weborb.util.io.Serializer.fromBytes( rawTemplates.getBytes(), weborb.util.io.Serializer.JSON, false );
      pushNotificationTemplates = Collections.unmodifiableMap( templates);
    }
    catch( IOException e )
    {
      pushNotificationTemplates = Collections.emptyMap();
      Log.w( PushTemplateHelper.class.getSimpleName(), "Cannot deserialize AndroidPushTemplate to JSONObject.", e );
    }
  }
}
