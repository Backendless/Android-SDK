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

package com.backendless.examples.endless.matchmaker.controllers.shared;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.backendless.examples.endless.matchmaker.controllers.*;
import com.backendless.examples.endless.matchmaker.models.local.PreferenceTheme;
import com.backendless.examples.endless.matchmaker.utils.Defaults;
import com.backendless.BackendlessUser;
import com.backendless.geo.GeoPoint;

public class Lifecycle
{
  public final static String PREFERENCE_TYPE_TAG = "ENDLESS_PREFERENCE_TYPE";

  public static void runProfileActivity( Context context )
  {
    //Starting profile activity
    Intent intent = new Intent( context, ProfileActivity.class );
    context.startActivity( intent );
  }

  public static void runRegisterActivity( Context context )
  {
    //Starting register activity
    Intent intent = new Intent( context, RegisterActivity.class );
    context.startActivity( intent );
  }

  public static void runLoginActivity( Context context )
  {
    runLoginActivity( context, null, null );
  }

  public static void runLoginActivity( Context context, String email, String password )
  {
    //Starting login activity
    Intent intent = new Intent( context, LoginActivity.class );

    if( email != null && password != null )
    {
      intent.putExtra( BackendlessUser.EMAIL_KEY, email );
      intent.putExtra( BackendlessUser.PASSWORD_KEY, password );
    }

    context.startActivity( intent );
  }

  public static void runEditPreferencesActivity( Context context, PreferenceTheme preferenceTheme )
  {
    if( preferenceTheme == null )
      throw new RuntimeException( "Preference type should not be null" );

    //Starting login activity
    Intent intent = new Intent( context, EditPreferencesActivity.class );
    intent.putExtra( PREFERENCE_TYPE_TAG, preferenceTheme.toString() );
    context.startActivity( intent );
  }

  public static void runPingsActivity( Context context, GeoPoint currentUserGeoPoint )
  {
    //Starting pings list activity
    Intent intent = new Intent( context, PingsActivity.class );
    intent.putExtra( Defaults.CURRENT_USER_GEO_POINT_BUNDLE_TAG, currentUserGeoPoint );
    context.startActivity( intent );
  }

  public static void runMatchViewActivity( Context context, GeoPoint currentUserGeoPoint, GeoPoint targetUserGeoPoint )
  {
    //Starting match view activity
    Intent intent = new Intent( context, MatchViewActivity.class );
    intent.putExtra( Defaults.CURRENT_USER_GEO_POINT_BUNDLE_TAG, currentUserGeoPoint );
    intent.putExtra( Defaults.TARGET_USER_GEO_POINT_BUNDLE_TAG, targetUserGeoPoint );
    context.startActivity( intent );
  }

  public static void runMatchViewActivityFromPings( Context context, GeoPoint currentUserGeoPoint,
                                                    GeoPoint targetUserGeoPoint, String triger )
  {
    //Starting match view activity
    Intent intent = new Intent( context, MatchViewActivity.class );
    intent.putExtra( Defaults.CURRENT_USER_GEO_POINT_BUNDLE_TAG, currentUserGeoPoint );
    intent.putExtra( Defaults.TARGET_USER_GEO_POINT_BUNDLE_TAG, targetUserGeoPoint );
    intent.putExtra( Defaults.TRIGER, triger );
    context.startActivity( intent );
  }

  public static void runSendMessageActivity( Context context, GeoPoint currentUserGeoPoint,
                                             GeoPoint targetUserGeoPoint )
  {
    //Starting match view activity
    Intent intent = new Intent( context, SendMessageActivity.class );
    intent.putExtra( Defaults.CURRENT_USER_GEO_POINT_BUNDLE_TAG, currentUserGeoPoint );
    intent.putExtra( Defaults.TARGET_USER_GEO_POINT_BUNDLE_TAG, targetUserGeoPoint );
    context.startActivity( intent );
  }

  public static void runFindMatchesActivity( Context context )
  {
    //Starting pings list activity
    Intent intent = new Intent( context, FindMatchesActivity.class );
    context.startActivity( intent );
    ((Activity) context).finish();
  }
}
