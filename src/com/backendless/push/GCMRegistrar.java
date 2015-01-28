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

package com.backendless.push;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import java.util.*;

public final class GCMRegistrar
{
  //TODO add persisting Backendless registration id

  public static final int DEFAULT_ON_SERVER_LIFESPAN_MS = /*2 days*/1000 * 60 * 60 * 24 * 2;

  private static final String BACKOFF_MS = "backoff_ms";
  private static final String GSF_PACKAGE = "com.google.android.gsf";
  private static final String PREFERENCES = "com.google.android.gcm";
  private static final int DEFAULT_BACKOFF_MS = 3000;
  private static final String PROPERTY_GCM_DEVICE_TOKEN = "gcmDeviceToken";
  private static final String PROPERTY_REGISTRATION_ID = "registrationId";
  private static final String PROPERTY_EXPIRATION = "registrationDate";
  private static final String PROPERTY_APP_VERSION = "appVersion";

  private static final List<String> DEFAULT_PERMISSIONS;

  static
  {
    DEFAULT_PERMISSIONS = new ArrayList<String>();
    DEFAULT_PERMISSIONS.add( GCMConstants.PERMISSION_GCM_MESSAGE );
    DEFAULT_PERMISSIONS.add( GCMConstants.PERMISSION_ANDROID_ACCOUNTS );
    DEFAULT_PERMISSIONS.add( GCMConstants.PERMISSION_ANDROID_INTERNET );
    //DEFAULT_PERMISSIONS.add( GCMConstants.PERMISSION_GCM_INTENTS );
  }

  public static void checkDevice( Context context )
  {
    int version = Build.VERSION.SDK_INT;

    if( version < 8 )
      throw new UnsupportedOperationException( "Device must be at least " +
                                                       "API Level 8 (instead of " + version + ")" );

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

  public static void checkManifest( Context context )
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
      if( GCMConstants.PERMISSION_GCM_INTENTS.equals( receiver.permission ) )
        allowedReceivers.add( receiver.name );

    if( allowedReceivers.isEmpty() )
      throw new IllegalStateException( "No receiver allowed to receive " + GCMConstants.PERMISSION_GCM_INTENTS );

    checkReceiver( context, allowedReceivers, GCMConstants.INTENT_FROM_GCM_REGISTRATION_CALLBACK );
    checkReceiver( context, allowedReceivers, GCMConstants.INTENT_FROM_GCM_MESSAGE );
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
        throw new IllegalStateException( "Receiver " + name +
                                                 " is not set with permission " +
                                                 GCMConstants.PERMISSION_GCM_INTENTS );
    }
  }

  public static void register( Context context, String senderId, List<String> channels, Date expiration )
  {
    RegistrationInfo registrationInfo = getRegistrationInfo( context );

    if( registrationInfo == null )
      requesRegistration( context, senderId, channels, expiration );
    else
    {
      GCMRegistrar.resetBackoff( context );
      BackendlessBroadcastReceiver.setSenderId( "" );
      requesRegistration( context, senderId, channels, expiration );
    }
  }

  private static void sendInternalRegistration( Context context, RegistrationInfo registrationInfo )
  {
    Intent intent = new Intent();
    intent.setAction( GCMConstants.INTENT_FROM_GCM_REGISTRATION_CALLBACK );
    intent.putExtra( GCMConstants.EXTRA_IS_INTERNAL, true );
    intent.putExtra( GCMConstants.EXTRA_REGISTRATION_ID, registrationInfo.getRegistrationId() );
    intent.putExtra( GCMConstants.EXTRA_DEVICE_TOKEN, registrationInfo.getGcmDeviceToken() );
    context.sendOrderedBroadcast( intent, null );
  }

  private static void requesRegistration( Context context, String senderId, List<String> channels, Date expiration )
  {
    GCMRegistrar.resetBackoff( context );
    BackendlessBroadcastReceiver.setSenderId( senderId );

    if( expiration != null )
      BackendlessBroadcastReceiver.setRegistrationExpiration( expiration.getTime() );

    if( channels != null )
      BackendlessBroadcastReceiver.setChannels( channels );

    internalRegister( context, senderId );
  }

  static void internalRegister( Context context, String senderId )
  {
    Intent intent = new Intent( GCMConstants.INTENT_TO_GCM_REGISTRATION );
    intent.setPackage( GSF_PACKAGE );
    intent.putExtra( GCMConstants.EXTRA_APPLICATION_PENDING_INTENT, PendingIntent.getBroadcast( context, 0, new Intent(), 0 ) );
    intent.putExtra( GCMConstants.EXTRA_SENDER, senderId );
    context.startService( intent );
  }

  public static void unregister( Context context )
  {
    GCMRegistrar.resetBackoff( context );
    BackendlessBroadcastReceiver.setSenderId( "" );
    internalUnregister( context );
  }

  static void internalUnregister( Context context )
  {
    Intent intent = new Intent( GCMConstants.INTENT_TO_GCM_UNREGISTRATION );
    intent.setPackage( GSF_PACKAGE );
    intent.putExtra( GCMConstants.EXTRA_APPLICATION_PENDING_INTENT, PendingIntent.getBroadcast( context, 0, new Intent(), 0 ) );
    context.startService( intent );
  }

  public static RegistrationInfo getRegistrationInfo( Context context )
  {
    final SharedPreferences prefs = getGCMPreferences( context );
    RegistrationInfo registrationInfo = new RegistrationInfo();
    registrationInfo.setGcmDeviceToken( prefs.getString( PROPERTY_GCM_DEVICE_TOKEN, "" ) );
    registrationInfo.setRegistrationId( prefs.getString( PROPERTY_REGISTRATION_ID, "" ) );
    registrationInfo.setRegistrationExpiration( prefs.getLong( PROPERTY_EXPIRATION, 0 ) );
    int oldVersion = prefs.getInt( PROPERTY_APP_VERSION, Integer.MIN_VALUE );
    int newVersion = getAppVersion( context );

    if(registrationInfo.getGcmDeviceToken() == null || registrationInfo.getGcmDeviceToken().equals( "" ))
      return null;

    if(registrationInfo.getRegistrationId() == null || registrationInfo.getRegistrationId().equals( "" ))
      return null;

    if( (oldVersion != Integer.MIN_VALUE && oldVersion != newVersion) || registrationInfo.getRegistrationExpiration() == null || registrationInfo.getRegistrationExpiration() < System.currentTimeMillis() )
    {
      clearRegistration( context );
      return null;
    }

    return registrationInfo;
  }

  public static boolean isRegistered( Context context )
  {
    return getRegistrationInfo( context ) != null;
  }

  static void clearRegistration( Context context )
  {
    setGCMdeviceToken( context, "" );
    setRegistrationId( context, "", 0 );
  }

  static void setGCMdeviceToken( Context context, String deviceToken )
  {
    int appVersion = getAppVersion( context );
    SharedPreferences.Editor editor = getGCMPreferences( context ).edit();
    editor.putString( PROPERTY_GCM_DEVICE_TOKEN, deviceToken );
    editor.putInt( PROPERTY_APP_VERSION, appVersion );
    editor.commit();
  }

  static void setRegistrationId( Context context, String registrationId, long expirationDate )
  {
    SharedPreferences.Editor editor = getGCMPreferences( context ).edit();
    editor.putString( PROPERTY_REGISTRATION_ID, registrationId );
    editor.putLong( PROPERTY_EXPIRATION, expirationDate );
    editor.commit();
  }

  private static int getAppVersion( Context context )
  {
    try
    {
      PackageInfo packageInfo = context.getPackageManager().getPackageInfo( context.getPackageName(), 0 );
      return packageInfo.versionCode;
    }
    catch( PackageManager.NameNotFoundException e )
    {
      throw new RuntimeException( "Coult not get package name: " + e );
    }
  }

  static void resetBackoff( Context context )
  {
    setBackoff( context, DEFAULT_BACKOFF_MS );
  }

  static int getBackoff( Context context )
  {
    final SharedPreferences prefs = getGCMPreferences( context );
    return prefs.getInt( BACKOFF_MS, DEFAULT_BACKOFF_MS );
  }

  static void setBackoff( Context context, int backoff )
  {
    final SharedPreferences prefs = getGCMPreferences( context );
    SharedPreferences.Editor editor = prefs.edit();
    editor.putInt( BACKOFF_MS, backoff );
    editor.commit();
  }

  private static SharedPreferences getGCMPreferences( Context context )
  {
    return context.getSharedPreferences( PREFERENCES, Context.MODE_PRIVATE );
  }

  private GCMRegistrar()
  {
    throw new UnsupportedOperationException();
  }
}
