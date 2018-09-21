package com.backendless.push;

import android.content.Intent;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class BackendlessFCMService extends FirebaseMessagingService
{
  private static final String TAG = BackendlessFCMService.class.getSimpleName();

  @Override
  public final void onNewToken( String token )
  {
    super.onNewToken( token );
    Intent msgWork = new Intent( BackendlessPushService.ACTION_FCM_REFRESH_TOKEN );
    msgWork.putExtra( BackendlessPushService.KEY_DEVICE_TOKEN, token );
    BackendlessPushService.enqueueWork( this.getApplicationContext(), msgWork );
  }

  @Override
  public final void onMessageReceived( RemoteMessage remoteMessage )
  {
    Intent msgWork = remoteMessage.toIntent();
    msgWork.setAction( BackendlessPushService.ACTION_FCM_ONMESSAGE );
    BackendlessPushService.enqueueWork( this.getApplicationContext(), msgWork );
  }

  @Override
  public void onDeletedMessages()
  {
    super.onDeletedMessages();
    Log.w( TAG, "there are too many messages (>100) pending for this app or your device hasn't connected to FCM in more than one month." );
  }
}
