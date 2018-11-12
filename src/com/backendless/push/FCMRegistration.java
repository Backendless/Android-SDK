package com.backendless.push;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.AndroidPushTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FCMRegistration
{
  private static final String DEFAULT_TOPIC = "default-topic";
  private static final String TAG = FCMRegistration.class.getSimpleName();

  public static void registerDevice( final Context appContext, final List<String> channels, final long expiration,
                                     final AsyncCallback<DeviceRegistrationResult> callback )
  {
    FirebaseMessaging.getInstance().subscribeToTopic( DEFAULT_TOPIC ).addOnCompleteListener( new OnCompleteListener<Void>()
    {
      @Override
      public void onComplete( @NonNull Task<Void> task )
      {
      if( !task.isSuccessful() )
      {
        Log.e( TAG, "Failed to subscribe in FCM.", task.getException() );
        if (callback != null)
          callback.handleFault( new BackendlessFault( "Failed to subscribe in FCM. " + task.getException().getMessage() ) );
      }
      else
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener( new OnCompleteListener<InstanceIdResult>()
        {
          @Override
          public void onComplete( @NonNull Task<InstanceIdResult> task )
          {
            if( !task.isSuccessful() )
            {
              Log.e( TAG, "Can not retrieve deviceToken from FCM.", task.getException() );
              if( callback != null )
                callback.handleFault( new BackendlessFault( "Can not retrieve deviceToken from FCM. " + task.getException().getMessage() ) );
            }
            else
            {
              String deviceToken = task.getResult().getToken();

              DeviceRegistrationResult devRegResult = null;
              if( callback != null )
              {
                devRegResult = new DeviceRegistrationResult();
                callback.handleResponse( devRegResult.setDeviceToken( deviceToken ) );
              }

              FCMRegistration.registerOnBackendless( appContext, deviceToken, channels, expiration, callback, devRegResult );
            }
          }
        } );
      }
    } );
  }

  private static void registerOnBackendless( final Context appContext, String deviceToken, List<String> channels, long expiration, final AsyncCallback<DeviceRegistrationResult> callback, final DeviceRegistrationResult devRegResult )
  {
    Backendless.Messaging.registerDeviceOnServer( deviceToken, channels, expiration, new AsyncCallback<String>()
    {
      @Override
      public void handleResponse( String registrationInfo )
      {
        Log.d( TAG, "Registered on Backendless." );
        try
        {
          if( callback != null )
          {
            Map<String, String> channelRegistrations = processRegistrationPayload( appContext, registrationInfo );
            callback.handleResponse( devRegResult.setChannelRegistrations( channelRegistrations ) );
          }
        }
        catch( Exception e )
        {
          callback.handleFault( new BackendlessFault( "Could not deserialize server response: " + e.getMessage() ) );
        }
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        Log.d( TAG, "Could not register device on Backendless server: " + fault.toString() );
        if( callback != null )
          callback.handleFault( new BackendlessFault( "Could not register device on Backendless server: " + fault.toString() ) );
      }
    } );
  }

  public static void unregisterDevice( final Context appContext, final List<String> channels, final AsyncCallback<Integer> callback )
  {
    Backendless.Messaging.unregisterDeviceOnServer( channels, new AsyncCallback<Integer>()
    {
      @Override
      public void handleResponse( Integer response )
      {
        Log.d( TAG, "Unregistered on Backendless." );
        if( response < 1 )
          FCMRegistration.unregisterDeviceOnFCM( appContext, callback );
        else
          callback.handleResponse( response );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        Log.d( TAG, "Could not unregister device on Backendless server: " + fault.toString() );
        callback.handleFault( new BackendlessFault( "Could not unregister device on Backendless server: " + fault.toString() ) );
      }
    } );
  }

  static void unregisterDeviceOnFCM(final Context context, final AsyncCallback<Integer> callback)
  {
    FirebaseMessaging.getInstance().unsubscribeFromTopic( DEFAULT_TOPIC ).addOnCompleteListener( new OnCompleteListener<Void>()
    {
      @Override
      public void onComplete( @NonNull Task<Void> task )
      {
        if( task.isSuccessful() )
        {
          Log.d( TAG, "Unsubscribed on FCM." );
          callback.handleResponse( 0 );
        }
        else
        {
          Log.e( TAG, "Failed to unsubscribe in FCM.", task.getException() );
          String reason = (task.getException() != null) ? Objects.toString( task.getException().getMessage() ) : "";
          callback.handleFault( new BackendlessFault( "Failed to unsubscribe on FCM. " + reason ) );
        }
      }
    } );
  }

  static Map<String, String> processRegistrationPayload( final Context context, final String registrationInfo )
  {
    Object[] obj;
    try
    {
      obj = (Object[]) weborb.util.io.Serializer.fromBytes( registrationInfo.getBytes(), weborb.util.io.Serializer.JSON, false );
    }
    catch( IOException e )
    {
      Log.e( TAG, "Could not deserialize server response: " + e.getMessage() );
      throw new BackendlessException( "Could not deserialize server response: " + e.getMessage() );
    }

    PushTemplateHelper.deleteNotificationChannel( context );
    Map<String, AndroidPushTemplate> templates = (Map<String, AndroidPushTemplate>) obj[ 1 ];

    if( android.os.Build.VERSION.SDK_INT > 25 )
    {
      for( AndroidPushTemplate templ : templates.values() )
        PushTemplateHelper.getOrCreateNotificationChannel( context.getApplicationContext(), templ );
    }

    PushTemplateHelper.setPushNotificationTemplates( templates, registrationInfo.getBytes() );

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
}
