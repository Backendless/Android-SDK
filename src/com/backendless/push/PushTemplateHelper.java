package com.backendless.push;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
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

  static Notification convertFromTemplate( Context context, AndroidPushTemplate template, String messageText, String messageId, String contentTitle, String summarySubText, int notificationId )
  {
    context = context.getApplicationContext();
    // Notification channel ID is ignored for Android 7.1.1 (API level 25) and lower.

    NotificationCompat.Builder notificationBuilder;
    // android.os.Build.VERSION_CODES.O == 26
    if( android.os.Build.VERSION.SDK_INT > 25 )
    {
      NotificationChannel notificationChannel = getOrCreateNotificationChannel( context, template );

      notificationBuilder = new NotificationCompat.Builder( context, notificationChannel.getId() );

      if( template.getBadge() != null &&
          ( template.getBadge() == NotificationCompat.BADGE_ICON_SMALL || template.getBadge() == NotificationCompat.BADGE_ICON_LARGE ) )
        notificationBuilder.setBadgeIconType( template.getBadge() );
      else
        notificationBuilder.setBadgeIconType( NotificationCompat.BADGE_ICON_NONE );

      if( template.getBadgeNumber() != null )
        notificationBuilder.setNumber( template.getBadgeNumber() );

      if( template.getCancelAfter() != null && template.getCancelAfter() != 0 )
        notificationBuilder.setTimeoutAfter( template.getCancelAfter()*1000 );
    }
    else
    {
      notificationBuilder = new NotificationCompat.Builder( context );

      if( template.getPriority() != null && template.getPriority() > 0 && template.getPriority() < 6 )
        notificationBuilder.setPriority( template.getPriority() - 3 );
      else
        notificationBuilder.setPriority( NotificationCompat.PRIORITY_DEFAULT );

      if( notificationBuilder.getPriority() > NotificationCompat.PRIORITY_LOW )
        notificationBuilder.setSound( PushTemplateHelper.getSoundUri( context, template.getSound() ), AudioManager.STREAM_NOTIFICATION );

      if( template.getVibrate() != null && template.getVibrate().length > 0 && notificationBuilder.getPriority() > NotificationCompat.PRIORITY_LOW )
      {
        long[] vibrate = new long[ template.getVibrate().length ];
        int index = 0;
        for( long l : template.getVibrate() )
          vibrate[ index++ ] = l;

        notificationBuilder.setVibrate( vibrate );
      }
    }

    if( template.getAttachmentUrl() != null )
    {
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
    }
    else if( messageText.length() > 35 )
    {
      NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle()
              .setBigContentTitle( contentTitle != null ? contentTitle : template.getContentTitle() )
              .setSummaryText( summarySubText != null ? summarySubText : template.getSummarySubText() )
              .bigText( messageText );
      notificationBuilder.setStyle( bigText );
    }

    if( template.getLargeIcon() != null )
    {
      if (template.getLargeIcon().startsWith( "http" ))
      {
        try
        {
          InputStream is = (InputStream) new URL( template.getLargeIcon() ).getContent();
          Bitmap bitmap = BitmapFactory.decodeStream( is );

          if( bitmap != null )
            notificationBuilder.setLargeIcon( bitmap );
          else
            Log.i( PushTemplateHelper.class.getSimpleName(), "Cannot convert Large Icon into bitmap." );
        }
        catch( IOException e )
        {
          Log.e( PushTemplateHelper.class.getSimpleName(), "Cannot receive bitmap for Large Icon." );
        }
      }
      else
      {
        int largeIconResource = context.getResources().getIdentifier( template.getLargeIcon(), "raw", context.getPackageName() );
        if (largeIconResource != 0)
        {
          Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), largeIconResource);
          notificationBuilder.setLargeIcon( bitmap );
        }
      }
    }

    int icon = 0;
    if( template.getIcon() != null )
      icon = context.getResources().getIdentifier( template.getIcon(), "drawable", context.getPackageName() );

    if( icon == 0 )
      icon = context.getResources().getIdentifier( "ic_launcher", "drawable", context.getPackageName() );

    if( icon != 0 )
        notificationBuilder.setSmallIcon( icon );

    if (template.getLightsColor() != null && template.getLightsOnMs() != null && template.getLightsOffMs() != null)
      notificationBuilder.setLights(template.getLightsColor()|0xFF000000, template.getLightsOnMs(), template.getLightsOffMs());

    if (template.getColorCode() != null)
      notificationBuilder.setColor( template.getColorCode()|0xFF000000 );

    if (template.getCancelOnTap() != null)
      notificationBuilder.setAutoCancel( template.getCancelOnTap() );
    else
      notificationBuilder.setAutoCancel( false );

    notificationBuilder
            .setShowWhen( true )
            .setWhen( System.currentTimeMillis() )
            .setContentTitle( contentTitle != null ? contentTitle : template.getContentTitle() )
            .setSubText( summarySubText != null ? summarySubText : template.getSummarySubText() )
            .setContentText( messageText );

    Intent notificationIntent = context.getPackageManager().getLaunchIntentForPackage( context.getPackageName() );
    notificationIntent.putExtra( PublishOptions.MESSAGE_ID, messageId );
    notificationIntent.putExtra( PublishOptions.TEMPLATE_NAME, template.getName() );
    notificationIntent.putExtra( PublishOptions.MESSAGE_TAG, messageText );
    notificationIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );

    PendingIntent contentIntent = PendingIntent.getActivity( context, notificationId * 3, notificationIntent, 0 );
    notificationBuilder.setContentIntent( contentIntent );

    if ( template.getActions() != null)
    {
      List<NotificationCompat.Action> actions = createActions( context, template.getActions(), template.getName(), messageId, messageText, notificationId );
      for( NotificationCompat.Action action : actions )
        notificationBuilder.addAction( action );
    }

    return notificationBuilder.build();
  }

  static Uri getSoundUri( Context context, String resource )
  {
    Uri soundUri;
    if( resource != null && !resource.isEmpty() )
    {
      int soundResource = context.getResources().getIdentifier( resource, "raw", context.getPackageName() );
      soundUri = Uri.parse( "android.resource://" + context.getPackageName() + "/" + soundResource );
    }
    else
      soundUri = RingtoneManager.getDefaultUri( RingtoneManager.TYPE_NOTIFICATION );

    return soundUri;
  }

  static private List<NotificationCompat.Action> createActions( Context context, Action[] actions, String templateName, String messageId, String messageText, int notificationId )
  {
    List<NotificationCompat.Action> notifActions = new ArrayList<>();

    int i = 1;
    for( Action a : actions )
    {
      Intent actionIntent = new Intent( a.getTitle() );
      actionIntent.setClassName( context, a.getId() );
      actionIntent.putExtra( PublishOptions.MESSAGE_ID, messageId );
      actionIntent.putExtra( PublishOptions.MESSAGE_TAG, messageText );
      actionIntent.putExtra( PublishOptions.TEMPLATE_NAME, templateName );
      actionIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );

      // user should use messageId and tag(templateName) to cancel notification.

      PendingIntent pendingIntent = PendingIntent.getActivity( context, notificationId * 3 + i++, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT );

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

  static public void deleteNotificationChannel( Context context )
  {
    if( android.os.Build.VERSION.SDK_INT < 26 )
      return;

    NotificationManager notificationManager = (NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE );
    List<NotificationChannel> notificationChannels = notificationManager.getNotificationChannels();
    for (NotificationChannel notifChann : notificationChannels)
      notificationManager.deleteNotificationChannel( notifChann.getId() );
  }

  static public NotificationChannel getNotificationChannel( final Context context, final String templateName )
  {
    final String channelId = Backendless.getApplicationId() + ":" + templateName;
    NotificationManager notificationManager = (NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE );
    return notificationManager.getNotificationChannel( channelId );
  }

  static public NotificationChannel getOrCreateNotificationChannel( Context context, final AndroidPushTemplate template )
  {
    final String channelId = Backendless.getApplicationId() + ":" + template.getName();
    NotificationManager notificationManager = (NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE );

    NotificationChannel notificationChannel = notificationManager.getNotificationChannel( channelId );

    if( notificationChannel != null )
      return notificationChannel;

    notificationChannel = new NotificationChannel( channelId, template.getName(), NotificationManager.IMPORTANCE_DEFAULT );
    PushTemplateHelper.updateNotificationChannel( context, notificationChannel, template );
    notificationManager.createNotificationChannel( notificationChannel );
    return notificationChannel;
  }

  static private NotificationChannel updateNotificationChannel( Context context, NotificationChannel notificationChannel, final AndroidPushTemplate template )
  {
    if( template.getShowBadge() != null )
      notificationChannel.setShowBadge( template.getShowBadge() );

    if( template.getPriority() != null && template.getPriority() > 0 && template.getPriority() < 6 )
      notificationChannel.setImportance( template.getPriority() ); // NotificationManager.IMPORTANCE_DEFAULT

    AudioAttributes audioAttributes = new AudioAttributes.Builder()
            .setUsage( AudioAttributes.USAGE_NOTIFICATION_RINGTONE )
            .setContentType( AudioAttributes.CONTENT_TYPE_SONIFICATION )
            .setFlags( AudioAttributes.FLAG_AUDIBILITY_ENFORCED )
            .setLegacyStreamType( AudioManager.STREAM_NOTIFICATION )
            .build();

    notificationChannel.setSound( PushTemplateHelper.getSoundUri( context, template.getSound() ), audioAttributes );

    if (template.getLightsColor() != null)
    {
      notificationChannel.enableLights( true );
      notificationChannel.setLightColor( template.getLightsColor()|0xFF000000 );
    }

    if( template.getVibrate() != null && template.getVibrate().length > 0 )
    {
      long[] vibrate = new long[ template.getVibrate().length ];
      int index = 0;
      for( long l : template.getVibrate() )
        vibrate[ index++ ] = l;

      notificationChannel.enableVibration( true );
      notificationChannel.setVibrationPattern( vibrate );
    }

    return notificationChannel;
  }

  static void showNotification( final Context context, final Notification notification, final String tag, final int notificationId )
  {
    final NotificationManagerCompat notificationManager = NotificationManagerCompat.from( context.getApplicationContext() );
    Handler handler = new Handler( Looper.getMainLooper() );
    handler.post( new Runnable()
    {
      @Override
      public void run()
      {
        notificationManager.notify( tag, notificationId, notification );
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
