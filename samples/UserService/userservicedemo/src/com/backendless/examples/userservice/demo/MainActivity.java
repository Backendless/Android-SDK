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

package com.backendless.examples.userservice.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;

public class MainActivity extends Activity
{
  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.main );

    if( Defaults.APPLICATION_ID.equals( "" ) || Defaults.SECRET_KEY.equals( "" ) || Defaults.VERSION.equals( "" ) )
    {
      showAlert( this, "Missing application ID and secret key arguments. Login to Backendless Console, select your app and get the ID and key from the Manage > App Settings screen. Copy/paste the values into the Backendless.initApp call" );
      return;
    }

    Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );

    final EditText emailField = (EditText) findViewById( R.id.emailField );
    final EditText passwordField = (EditText) findViewById( R.id.passwordField );

    findViewById( R.id.loginButton ).setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View view )
      {
        Backendless.UserService.login( emailField.getText().toString(), passwordField.getText().toString(), new DefaultCallback<BackendlessUser>( MainActivity.this )
        {
          public void handleResponse( BackendlessUser backendlessUser )
          {
            super.handleResponse( backendlessUser );
            startActivity( new Intent( getBaseContext(), LoggedInActivity.class ) );
          }
        } );
      }
    } );

    findViewById( R.id.registerButton ).setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View view )
      {
        startActivity( new Intent( getBaseContext(), RegisterActivity.class ) );
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
