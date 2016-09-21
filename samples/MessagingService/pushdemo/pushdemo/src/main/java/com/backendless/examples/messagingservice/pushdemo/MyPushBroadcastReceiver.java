package com.backendless.examples.messagingservice.pushdemo;

import com.backendless.push.BackendlessBroadcastReceiver;
import com.backendless.push.BackendlessPushService;

public class MyPushBroadcastReceiver extends BackendlessBroadcastReceiver
{
  @Override
  public Class<? extends BackendlessPushService> getServiceClass()
  {
    return MyPushService.class;
  }
}
