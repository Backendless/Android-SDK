/*
 * ********************************************************************************************************************
 *  <p/>
 *  BACKENDLESS.COM CONFIDENTIAL
 *  <p/>
 *  ********************************************************************************************************************
 *  <p/>
 *  Copyright 2012 BACKENDLESS.COM. All Rights Reserved.
 *  <p/>
 *  NOTICE: All information contained herein is, and remains the property of Backendless.com and its suppliers,
 *  if any. The intellectual and technical concepts contained herein are proprietary to Backendless.com and its
 *  suppliers and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret
 *  or copyright law. Dissemination of this information or reproduction of this material is strictly forbidden
 *  unless prior written permission is obtained from Backendless.com.
 *  <p/>
 *  ********************************************************************************************************************
 */

package com.backendless.examples.endless.matchmaker;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.backendless.examples.endless.matchmaker.controllers.PushActivity;
import com.backendless.examples.endless.matchmaker.controllers.DialogMessageActivity;
import com.backendless.examples.endless.matchmaker.utils.Defaults;
import com.backendless.push.BackendlessBroadcastReceiver;

import java.util.List;

public class PushReceiver extends BackendlessBroadcastReceiver
{
  public PushReceiver()
  {
    super( Defaults.GCM_SENDER_ID );
  }

  @Override
  public void onRegistered( Context context, String registrationId )
  {
    SharedPreferences.Editor editor = context.getSharedPreferences( context.getResources().getText( R.string.app_name ).toString(), Context.MODE_PRIVATE ).edit();
    editor.putString( Defaults.DEVICE_REGISTRATION_ID_PROPERTY, registrationId );
    editor.commit();
  }

  @Override
  public void onUnregistered( Context context, Boolean unregistered )
  {
    SharedPreferences.Editor editor = context.getSharedPreferences( context.getResources().getText( R.string.app_name ).toString(), Context.MODE_PRIVATE ).edit();
    editor.remove( Defaults.DEVICE_REGISTRATION_ID_PROPERTY );
    editor.commit();
  }

  @Override
  public boolean onMessage( Context context, Intent intent )
  {
    String title = context.getResources().getString( R.string.app_name );
    String message = null;
    String targetName = null;
    String currentName = null;
    String type = intent.getStringExtra( "type" );

    if( type.equals( "ping" ) )
    {
      String[] userNames = intent.getStringExtra( "message" ).split( "," );
      targetName = userNames[ 0 ];
      currentName = userNames[ 1 ];
      message = "You have a new ping!";
    }
    else
    {
      currentName = intent.getStringExtra( "currentUserName" );
      targetName = intent.getStringExtra( "targetUserName" );
      message = "You got a new message from ";
    }

    //Check is running
    ActivityManager activityManager = (ActivityManager) context.getSystemService( Context.ACTIVITY_SERVICE );
    List<ActivityManager.RunningAppProcessInfo> processesInfo = activityManager.getRunningAppProcesses();
    List<ActivityManager.RunningTaskInfo> services = activityManager.getRunningTasks( Integer.MAX_VALUE );
    boolean isRunApp = false;

    for( int i = 0; i < processesInfo.size(); i++ )
    {

      if( processesInfo.get( i ).processName.equals( "com.backendless.examples.endless.matchmaker" ) && services.get( 0 ).topActivity.getPackageName().toString().equalsIgnoreCase( context.getPackageName().toString() ) )
      {
        isRunApp = true;
        Intent targetIntent = new Intent( context, DialogMessageActivity.class );
        targetIntent.putExtra( Defaults.CURRENT_USER_NAME, currentName );
        targetIntent.putExtra( Defaults.TARGET_USER_NAME, targetName );
        targetIntent.putExtra( "type", type );
        targetIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP );
        context.startActivity( targetIntent );
      }
    }

    if( isRunApp == false )
    {
      Intent targetIntent = new Intent( context, PushActivity.class );
      targetIntent.putExtra( Defaults.TARGET_USER_NAME, targetName );
      targetIntent.putExtra( Defaults.CURRENT_USER_NAME, currentName );
      targetIntent.putExtra( "type", type );
      targetIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP );
      PendingIntent pendingIntent = PendingIntent.getActivity( context, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT );
      Notification notification = new Notification( R.drawable.logo_small, message, 0 );
      notification.setLatestEventInfo( context, title, message, pendingIntent );
      notification.flags |= Notification.FLAG_AUTO_CANCEL;
      NotificationManager notificationManager = (NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE );
      notificationManager.notify( 0, notification );
    }
    return true;
  }
}
