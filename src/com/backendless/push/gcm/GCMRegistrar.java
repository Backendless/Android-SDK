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

package com.backendless.push.gcm;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import com.backendless.push.AbstractRegistrar;
import com.backendless.push.BackendlessPushBroadcastReceiver;
import com.backendless.push.Constants;

import java.util.*;

public final class GCMRegistrar extends AbstractRegistrar
{
  //TODO add persisting Backendless registration id

  public static final int DEFAULT_ON_SERVER_LIFESPAN_MS = /*2 days*/1000 * 60 * 60 * 24 * 2;
  private static final String GSF_PACKAGE = "com.google.android.gsf";

  @Override
  public void subscribe( Context context, String senderId, String channelName )
  {
    if( channelName != null )
      BackendlessPushBroadcastReceiver.setChannels( Arrays.asList( channelName ) );

    resetBackoff( context );
    internalSubscription( context, senderId );
  }

  @Override
  public void checkPossibility( Context context )
  {
    checkDevice( context );
    checkManifest( context );
  }

  @Override
  public void register( Context context, String senderId, List<String> channels, Date expiration )
  {
    if( expiration != null )
      BackendlessPushBroadcastReceiver.setRegistrationExpiration( expiration.getTime() );

    if( channels != null )
      BackendlessPushBroadcastReceiver.setChannels( channels );

    resetBackoff( context );
    internalRegister( context, senderId );
  }

  @Override
  public void unregister( Context context )
  {
    internalUnregister( context );
  }

  private void internalRegister( Context context, String senderId )
  {
    Intent intent = new Intent( Constants.INTENT_TO_GCM_REGISTRATION );
    intent.setPackage( GSF_PACKAGE );
    intent.putExtra( Constants.EXTRA_APPLICATION_PENDING_INTENT,
                    PendingIntent.getBroadcast( context, 0, new Intent(), 0 ) );
    intent.putExtra( Constants.EXTRA_SENDER, senderId );
    context.startService( intent );
  }

  private void internalSubscription( Context context, String senderId )
  {
    Intent intent = new Intent( Constants.INTENT_TO_GCM_REGISTRATION );
    intent.setPackage( GSF_PACKAGE );
    intent.putExtra( Constants.EXTRA_APPLICATION_PENDING_INTENT, PendingIntent.getBroadcast( context, 0, new Intent(), 0 ) );
    intent.putExtra( Constants.EXTRA_SENDER, senderId );
    intent.putExtra( Constants.EXTRA_SUBSCRIPTION_REGISTRATION, true );
    context.startService( intent );
  }

  private void internalUnregister( Context context )
  {
    Intent intent = new Intent( Constants.INTENT_TO_GCM_UNREGISTRATION );
    intent.setPackage( GSF_PACKAGE );
    intent.putExtra( Constants.EXTRA_APPLICATION_PENDING_INTENT, PendingIntent.getBroadcast( context, 0, new Intent(), 0 ) );
    context.startService( intent );
  }

  private static void checkDevice( Context context )
  {
    int version = Build.VERSION.SDK_INT;

    if( version < 8 )
      throw new UnsupportedOperationException( "Device must be at least " + "API Level 8 (instead of " + version + ")" );

    PackageManager packageManager = context.getPackageManager();

    try
    {
      packageManager.getPackageInfo( GSF_PACKAGE, 0 );
    }
    catch( PackageManager.NameNotFoundException e )
    {
      throw new UnsupportedOperationException( "Device does not have package " + GSF_PACKAGE );
    }
  }

  private static void checkManifest( Context context )
  {
    PackageManager packageManager = context.getPackageManager();
    String packageName = context.getPackageName();

    checkPermission( packageName + ".permission.C2D_MESSAGE", packageManager );
    for( String permission : DEFAULT_PERMISSIONS )
      checkPermission( permission, packageManager );

    // check receivers
    PackageInfo receiversInfo;
    try
    {
      receiversInfo = packageManager.getPackageInfo( packageName, PackageManager.GET_RECEIVERS );
    }
    catch( PackageManager.NameNotFoundException e )
    {
      throw new IllegalStateException( "Could not get receivers for package " + packageName );
    }

    ActivityInfo[] receivers = receiversInfo.receivers;
    if( receivers == null || receivers.length == 0 )
      throw new IllegalStateException( "No receiver for package " + packageName );

    Set<String> allowedReceivers = new HashSet<String>();
    for( ActivityInfo receiver : receivers )
      if( Constants.PERMISSION_GCM_INTENTS.equals( receiver.permission ) )
        allowedReceivers.add( receiver.name );

    if( allowedReceivers.isEmpty() )
      throw new IllegalStateException( "No receiver allowed to receive " + Constants.PERMISSION_GCM_INTENTS );

    checkReceiver( context, allowedReceivers, Constants.INTENT_FROM_GCM_REGISTRATION_CALLBACK );
    checkReceiver( context, allowedReceivers, Constants.INTENT_FROM_GCM_MESSAGE );
  }

  private static void checkPermission( String packageName, PackageManager packageManager )
  {
    try
    {
      packageManager.getPermissionInfo( packageName, PackageManager.GET_PERMISSIONS );
    }
    catch( PackageManager.NameNotFoundException e )
    {
      throw new IllegalStateException( "Application does not define permission " + packageName );
    }
  }

  private static void checkReceiver( Context context, Set<String> allowedReceivers, String action )
  {
    PackageManager pm = context.getPackageManager();
    String packageName = context.getPackageName();
    Intent intent = new Intent( action );
    intent.setPackage( packageName );
    List<ResolveInfo> receivers = pm.queryBroadcastReceivers( intent, PackageManager.GET_INTENT_FILTERS );

    if( receivers.isEmpty() )
      throw new IllegalStateException( "No receivers for action " + action );

    // make sure receivers match
    for( ResolveInfo receiver : receivers )
    {
      String name = receiver.activityInfo.name;
      if( !allowedReceivers.contains( name ) )
        throw new IllegalStateException( "Receiver " + name + " is not set with permission " + Constants.PERMISSION_GCM_INTENTS );
    }
  }

}
