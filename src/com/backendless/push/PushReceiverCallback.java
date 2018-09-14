package com.backendless.push;

import android.content.Context;
import android.content.Intent;

import java.util.Map;


public interface PushReceiverCallback
{
  /**
   *
   * @param context
   * @param channelRegistrations The <b>key</b> is a channel name, and the <b>value</b> is an objectId of 'DeviceRegistration' on this channel.
   */
  void onRegistered( Context context, Map<String, String> channelRegistrations );

  /**
   * Will be removed after April 2019.
   * @param context
   * @param registrationIds
   */
  @Deprecated
  void onRegistered( Context context, String registrationIds );

  void onUnregistered( Context context, Boolean unregistered );

  boolean onMessage( Context context, Intent intent );

  void onError( Context context, String message );
}
