package com.backendless.push;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FCMRegistration
{
  private static final String DEFAULT_TOPIC = "default-topic";
  private static final String TAG = FCMRegistration.class.getSimpleName();

  public static void registerDevice( final Context appContext, final List<String> channels, final long expiration,
                                     final AsyncCallback<String> callback )
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
              Intent msgWork = new Intent( BackendlessPushService.ACTION_FCM_REGISTRATION );
              msgWork.putExtra( BackendlessPushService.KEY_DEVICE_TOKEN, deviceToken );
              msgWork.putStringArrayListExtra( BackendlessPushService.KEY_CHANNELS, new ArrayList<>( channels ) );
              msgWork.putExtra( BackendlessPushService.KEY_EXPIRATION, expiration );
              BackendlessPushService.enqueueWork( appContext, msgWork );

              if( callback != null )
                callback.handleResponse( deviceToken );
            }
          }
        } );
      }
    } );
  }

  public static void unregisterDevice( final Context appContext, final List<String> channels )
  {
    Intent msgWork = new Intent( BackendlessPushService.ACTION_FCM_UNREGISTRATION );
    msgWork.putStringArrayListExtra( BackendlessPushService.KEY_CHANNELS, new ArrayList<>(channels) );
    BackendlessPushService.enqueueWork( appContext, msgWork );
  }

  public static void unregisterDeviceOnFCM(final Context context, final PushReceiverCallback callback)
  {
    FirebaseMessaging.getInstance().unsubscribeFromTopic( DEFAULT_TOPIC ).addOnCompleteListener( new OnCompleteListener<Void>()
    {
      @Override
      public void onComplete( @NonNull Task<Void> task )
      {
        if( task.isSuccessful() )
        {
          Log.d( TAG, "Unsubscribed on FCM." );
          callback.onUnregistered( context, true );
        }
        else
        {
          Log.e( TAG, "Failed to unsubscribe in FCM.", task.getException() );
          String reason = (task.getException() != null) ? Objects.toString( task.getException().getMessage() ) : "";
          callback.onError( context, "Failed to unsubscribe on FCM. " + reason );
        }
      }
    } );
  }
}
