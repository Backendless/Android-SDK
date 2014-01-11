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

package com.backendless.examples.endless.matchmaker.controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.examples.endless.matchmaker.R;
import com.backendless.examples.endless.matchmaker.controllers.shared.Lifecycle;
import com.backendless.examples.endless.matchmaker.utils.Defaults;
import com.backendless.examples.endless.matchmaker.views.UIFactory;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class LoginActivity extends Activity
{
  private final static String PREF_TAG = "ENDLESS_LOGIN";
  private final static String EMAIL_PREF = "email";
  private EditText emailField;
  private EditText passwordField;
  private String userEmail;
  private ProgressDialog progressDialog;

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.login );

    progressDialog = UIFactory.getDefaultProgressDialog( this );

    //Initializing Backendless API
    Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.APPLICATION_SECRET_KEY, Defaults.APPLICATION_VERSION );

    //Binding UI elements
    emailField = (EditText) findViewById( R.id.emailField );
    passwordField = (EditText) findViewById( R.id.passwordField );
    passwordField.setTypeface( Typeface.DEFAULT );
    passwordField.setTransformationMethod( new PasswordTransformationMethod() );

    //Checking for a login intent (it can be send from a Registration activity)
    Intent intent = getIntent();
    userEmail = intent.getStringExtra( BackendlessUser.EMAIL_KEY );
    String userPassword = intent.getStringExtra( BackendlessUser.PASSWORD_KEY );

    //Checking if user is already logged in and was saved in Backendless.UserService.CurrentUser
    if( (userEmail == null || userPassword == null) && Backendless.UserService.CurrentUser() != null )
    {
      userEmail = Backendless.UserService.CurrentUser().getEmail();
      userPassword = Backendless.UserService.CurrentUser().getPassword();
    }

    if( userEmail != null )
      emailField.setText( userEmail );

    if( userEmail != null && userPassword != null )
    {
      //Sending login request asynchronously from the intent credentials
      Backendless.UserService.login( userEmail, userPassword, loginCallback );
    }
    else
    {
      Button loginButton = (Button) findViewById( R.id.loginButton );
      loginButton.setOnClickListener( loginListener );

      findViewById( R.id.registerButton ).setOnClickListener( registerListener );
      progressDialog.cancel();
    }

    Button loginFacebookButton = (Button) findViewById( R.id.loginFacebookButton );
    loginFacebookButton.setVisibility( View.INVISIBLE );
    TextView loginFacebookText = (TextView) findViewById( R.id.loginWith );
    loginFacebookText.setVisibility( View.INVISIBLE );
  }

  //Listeners section
  private View.OnClickListener loginListener = new View.OnClickListener()
  {
    @Override
    public void onClick( View view )
    {
      progressDialog = UIFactory.getDefaultProgressDialog( LoginActivity.this );

      //Sending login request asynchronously
      Backendless.UserService.login( emailField.getText().toString(), passwordField.getText().toString(), loginCallback );
    }
  };

  private View.OnClickListener registerListener = new View.OnClickListener()
  {
    @Override
    public void onClick( View view )
    {
      Lifecycle.runRegisterActivity( LoginActivity.this );
      finish();
    }
  };

  //Callbacks section
  private AsyncCallback<BackendlessUser> loginCallback = new AsyncCallback<BackendlessUser>()
  {
    @Override
    public void handleResponse( BackendlessUser backendlessUser )
    {
      if( progressDialog != null )
        progressDialog.cancel();

      Lifecycle.runProfileActivity( LoginActivity.this );

      //Registering device for push notifications
      Backendless.Messaging.registerDevice( Defaults.GCM_SENDER_ID, Defaults.MESSAGING_CHANNEL, new AsyncCallback<Void>()
      {
        @Override
        public void handleResponse( Void aVoid )
        {
          Toast.makeText( LoginActivity.this, "Your device is successfully registered!", Toast.LENGTH_LONG ).show();
        }

        @Override
        public void handleFault( BackendlessFault backendlessFault )
        {
          Toast.makeText( LoginActivity.this, backendlessFault.getMessage(), Toast.LENGTH_LONG ).show();
        }
      } );

      finish();
    }

    @Override
    public void handleFault( BackendlessFault backendlessFault )
    {
      //Displaying fault information
      if( progressDialog != null )
        progressDialog.cancel();

      Toast.makeText( LoginActivity.this, backendlessFault.getMessage(), Toast.LENGTH_SHORT ).show();
    }
  };

  //Lifecycle section
  @Override
  protected void onStop()
  {
    super.onStop();

    String email = emailField.getText().toString();

    if( email == null )
      return;

    SharedPreferences settings = getSharedPreferences( PREF_TAG, 0 );
    SharedPreferences.Editor editor = settings.edit();
    editor.putString( EMAIL_PREF, email );
    editor.commit();
  }

  @Override
  protected void onStart()
  {
    super.onStart();

    SharedPreferences settings = getSharedPreferences( PREF_TAG, 0 );
    String email = settings.getString( EMAIL_PREF, null );

    if( email != null && userEmail == null )
    {
      userEmail = email;
      emailField.setText( email );
    }
  }
}