package com.backendless.push;

import android.content.Context;
import android.content.Intent;

/**
 * Added for backward compatibility.
 */
public interface PushReceiverCallback
{

  void onRegistered( Context context, String registrationId );

  void onUnregistered( Context context, Boolean unregistered );

  boolean onMessage( Context context, Intent intent );

  void onError( Context context, String message );
}
