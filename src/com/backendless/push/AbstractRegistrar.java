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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.backendless.push.gcm.RegistrationInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class AbstractRegistrar
{
  private static final String PROPERTY_GCM_DEVICE_TOKEN = "gcmDeviceToken";
  private static final String PROPERTY_REGISTRATION_ID = "registrationId";
  private static final String PROPERTY_EXPIRATION = "registrationDate";
  private static final String PROPERTY_APP_VERSION = "appVersion";

  private static final String BACKOFF_MS = "backoff_ms";
  private static final String PREFERENCES = "com.google.android.gcm";
  private static final int DEFAULT_BACKOFF_MS = 3000;

  protected static List<String> DEFAULT_PERMISSIONS = new ArrayList<String>(  );

  public abstract void checkPossibility( Context context );

  public abstract void register( Context context, String senderId, List<String> channels, Date expiration );

  public abstract void subscribe( Context context, String senderId, String channelName );

  public abstract void unregister( Context context );

  protected RegistrationInfo getRegistrationInfo( Context context )
  {
    final SharedPreferences prefs = getGCMPreferences( context );
    RegistrationInfo registrationInfo = new RegistrationInfo();
    registrationInfo.setDeviceToken( prefs.getString( PROPERTY_GCM_DEVICE_TOKEN, "" ) );
    registrationInfo.setRegistrationId( prefs.getString( PROPERTY_REGISTRATION_ID, "" ) );
    registrationInfo.setRegistrationExpiration( prefs.getLong( PROPERTY_EXPIRATION, 0 ) );
    int oldVersion = prefs.getInt( PROPERTY_APP_VERSION, Integer.MIN_VALUE );
    int newVersion = getAppVersion( context );

    if(registrationInfo.getDeviceToken() == null || registrationInfo.getDeviceToken().equals( "" ))
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

  public boolean isRegistered( Context context )
  {
    return getRegistrationInfo( context ) != null;
  }

  protected void clearRegistration( Context context )
  {
    setDeviceToken( context, "" );
    setRegistrationId( context, "", 0 );
  }

  public void setDeviceToken( Context context, String deviceToken )
  {
    int appVersion = getAppVersion( context );
    SharedPreferences.Editor editor = getGCMPreferences( context ).edit();
    editor.putString( PROPERTY_GCM_DEVICE_TOKEN, deviceToken );
    editor.putInt( PROPERTY_APP_VERSION, appVersion );
    editor.commit();
  }

  public void setRegistrationId( Context context, String registrationId, long expirationDate )
  {
    SharedPreferences.Editor editor = getGCMPreferences( context ).edit();
    editor.putString( PROPERTY_REGISTRATION_ID, registrationId );
    editor.putLong( PROPERTY_EXPIRATION, expirationDate );
    editor.commit();
  }

  private SharedPreferences getGCMPreferences( Context context )
  {
    return context.getSharedPreferences( PREFERENCES, Context.MODE_PRIVATE );
  }

  public void resetBackoff( Context context )
  {
    setBackoff( context, DEFAULT_BACKOFF_MS );
  }

  public int getBackoff( Context context )
  {
    final SharedPreferences prefs = getGCMPreferences( context );
    return prefs.getInt( BACKOFF_MS, DEFAULT_BACKOFF_MS );
  }

  public void setBackoff( Context context, int backoff )
  {
    final SharedPreferences prefs = getGCMPreferences( context );
    SharedPreferences.Editor editor = prefs.edit();
    editor.putInt( BACKOFF_MS, backoff );
    editor.commit();
  }

  private int getAppVersion( Context context )
  {
    try
    {
      PackageInfo packageInfo = context.getPackageManager().getPackageInfo( context.getPackageName(), 0 );
      return packageInfo.versionCode;
    }
    catch( PackageManager.NameNotFoundException e )
    {
      throw new RuntimeException( "Couldn't not get package name: " + e );
    }
  }

}

