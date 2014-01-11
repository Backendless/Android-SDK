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

package com.backendless.examples.messagingservice.pubsubdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity
{
  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.main );

    if( Defaults.APPLICATION_ID.equals( "" ) || Defaults.SECRET_KEY.equals( "" ) || Defaults.VERSION.equals( "" ) || Defaults.CHANNEL_NAME.equals( "" ) )
    {
      showAlert( this, "Missing application ID and secret key arguments. Login to Backendless Console, select your app and get the ID and key from the Manage > App Settings screen. Copy/paste the values into the Backendless.initApp call" );
      return;
    }

    final EditText nameField = (EditText) findViewById( R.id.nameField );
    findViewById( R.id.startButton ).setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View view )
      {
        String name = nameField.getText().toString();

        if( name == null || name.equals( "" ) )
        {
          Toast.makeText( MainActivity.this, "Please enter your name", Toast.LENGTH_SHORT ).show();
          return;
        }

        Intent intent = new Intent( getBaseContext(), ChatActivity.class );
        intent.putExtra( Defaults.NAME_TAG, name );
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
