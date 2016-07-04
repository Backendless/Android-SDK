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

package com.backendless.examples.messagingservice.pushdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.backendless.Backendless;

public class MainActivity extends Activity
{
  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.main );

    new AlertDialog.Builder( this ).setMessage( "The Backendless Push example demonstrates native Android Push Notifications. Make sure to login to Backendless Console and configure Mobile Settings for your application. (Select App > Manage > App Settings > Mobile Settings). You will need to obtain Google Project Number and Google \"key for server apps\" from https://code.google.com/apis/console." ).setPositiveButton( "OK", new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick( DialogInterface dialogInterface, int i )
      {
        dialogInterface.cancel();
      }
    } ).show();

    if( Defaults.APPLICATION_ID.equals( "" ) || Defaults.SECRET_KEY.equals( "" ) || Defaults.VERSION.equals( "" ) || Defaults.CHANNEL_NAME.equals( "" ) )
    {
      showAlert( this, "Missing application ID or secret key arguments. Login to Backendless Console, select your app and get the ID and key from the Manage > App Settings screen. Copy/paste the values into the Backendless.initApp call." );
      return;
    }

    if( Defaults.SENDER_ID.equals( "" ) )
    {
      showAlert( this, "Missing SENDER_ID argument. SENDER_ID must contain the Google Project Number. Login to Google API Console at https://code.google.com/apis/console and copy/paste the Project Number value into the constant defined below." );
      return;
    }

    Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );

    findViewById( R.id.registerButton ).setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View view )
      {
        Backendless.Messaging.registerDevice( Defaults.SENDER_ID, Defaults.CHANNEL_NAME );
        Intent intent = new Intent( getBaseContext(), PushActivity.class );
        startActivity( intent );
      }
    } );
  }

  public static void showAlert( final Activity context, String message )
  {
    new AlertDialog.Builder( context ).setTitle( "An error occurred" ).setMessage( message ).setPositiveButton( "OK", new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick( DialogInterface dialogInterface, int i )
      {
        context.finish();
      }
    } ).show();
  }
}
