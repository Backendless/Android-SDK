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
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.*;
import com.backendless.examples.endless.matchmaker.R;
import com.backendless.examples.endless.matchmaker.controllers.shared.Lifecycle;
import com.backendless.examples.endless.matchmaker.models.local.Gender;
import com.backendless.examples.endless.matchmaker.utils.Defaults;
import com.backendless.examples.endless.matchmaker.views.UIFactory;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends Activity
{
  private final static String PREF_TAG = "ENDLESS_REGISTER";

  private EditText nameField;
  private EditText passwordField;
  private EditText verifyPasswordField;
  private EditText emailField;
  private EditText birthdateField;
  private RadioGroup genderRadio;
  private ProgressDialog progressDialog;
  private Calendar userDate;
  private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "dd.MM.yyyy", Locale.getDefault() );

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.registration );

    userDate = Calendar.getInstance();
    userDate.set( 1970, Calendar.DECEMBER, 31 );
    nameField = (EditText) findViewById( R.id.nameField );
    passwordField = (EditText) findViewById( R.id.passwordField );
    passwordField.setTypeface( Typeface.DEFAULT );
    passwordField.setTransformationMethod( new PasswordTransformationMethod() );
    verifyPasswordField = (EditText) findViewById( R.id.verifyPasswordField );
    verifyPasswordField.setTypeface( Typeface.DEFAULT );
    verifyPasswordField.setTransformationMethod( new PasswordTransformationMethod() );
    emailField = (EditText) findViewById( R.id.emailField );
    birthdateField = (EditText) findViewById( R.id.birthdateField );
    birthdateField.setFocusable( false );
    birthdateField.setOnClickListener( birthdateFieldListener );
    genderRadio = (RadioGroup) findViewById( R.id.genderGroup );
    findViewById( R.id.cancelButton ).setOnClickListener( cancelListener );
    Button registerButton = (Button) findViewById( R.id.registerButton );
    registerButton.setOnClickListener( registerListener );
  }

  //Listeners section
  private View.OnClickListener birthdateFieldListener = new View.OnClickListener()
  {
    @Override
    public void onClick( View view )
    {
      DatePickerDialog datePickerDialog = new DatePickerDialog( RegisterActivity.this, dateSetListener, userDate.get( Calendar.YEAR ), userDate.get( Calendar.MONTH ), userDate.get( Calendar.DAY_OF_MONTH ) );
      datePickerDialog.show();
    }
  };

  private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
  {
    @Override
    public void onDateSet( DatePicker datePicker, int year, int monthOfYear, int dayOfMonth )
    {
      userDate.set( Calendar.YEAR, year );
      userDate.set( Calendar.MONTH, monthOfYear );
      userDate.set( Calendar.DAY_OF_MONTH, dayOfMonth );
      birthdateField.setText( simpleDateFormat.format( userDate.getTime() ) );
    }
  };

  private View.OnClickListener cancelListener = new View.OnClickListener()
  {
    @Override
    public void onClick( View view )
    {
      Lifecycle.runLoginActivity( RegisterActivity.this );
      finish();
    }
  };

  private View.OnClickListener registerListener = new View.OnClickListener()
  {
    @Override
    public void onClick( View view )
    {
      //Parsing user input data
      if( nameField.getText() == null )
      {
        Toast.makeText( getApplicationContext(), "Name should not be empty", Toast.LENGTH_SHORT ).show();
        return;
      }

      if( passwordField.getText() == null || passwordField.getText().toString().equals( "" ) )
      {
        Toast.makeText( getApplicationContext(), "Password should not be empty", Toast.LENGTH_SHORT ).show();
        return;
      }

      if( !passwordField.getText().toString().equals( verifyPasswordField.getText().toString() ) )
      {
        Toast.makeText( getApplicationContext(), "Passwords missmatch", Toast.LENGTH_SHORT ).show();
        return;
      }

      if( emailField.getText() == null )
      {
        Toast.makeText( getApplicationContext(), "Email should not be empty", Toast.LENGTH_SHORT ).show();
        return;
      }

      if( birthdateField.getText() == null )
      {
        Toast.makeText( getApplicationContext(), "Date of birth should not be empty", Toast.LENGTH_SHORT ).show();
        return;
      }

      if( genderRadio.getCheckedRadioButtonId() == -1 )
      {
        Toast.makeText( getApplicationContext(), "Select your gender", Toast.LENGTH_SHORT ).show();
        return;
      }

      BackendlessUser user = new BackendlessUser();
      user.setProperty( Defaults.NAME_PROPERTY, nameField.getText().toString() );
      user.setPassword( passwordField.getText().toString() );
      user.setEmail( emailField.getText().toString() );
      user.setProperty( Defaults.BIRTH_DATE_PROPERTY, userDate.getTime() );
      user.setProperty( Defaults.GENDER_PROPERTY, genderRadio.getCheckedRadioButtonId() == R.id.genderMale ? Gender.male : Gender.female );

      progressDialog = UIFactory.getDefaultProgressDialog( RegisterActivity.this );
      Backendless.UserService.register( user, registerCallback );
    }
  };

  //Callbacks section
  private AsyncCallback<BackendlessUser> registerCallback = new AsyncCallback<BackendlessUser>()
  {
    @Override
    public void handleResponse( BackendlessUser response )
    {
      progressDialog.cancel();
      Lifecycle.runLoginActivity( RegisterActivity.this, response.getEmail(), response.getPassword() );
      finish();
    }

    @Override
    public void handleFault( BackendlessFault fault )
    {
      progressDialog.cancel();
      Toast.makeText( RegisterActivity.this, fault.getMessage(), Toast.LENGTH_SHORT ).show();
    }
  };

  //Lifecycle section
  @Override
  protected void onStop()
  {
    super.onStop();
    //Bundling user input data
    String name = nameField.getText().toString();
    String email = emailField.getText().toString();
    String birthdate = birthdateField.getText().toString();
    int gender = genderRadio.getCheckedRadioButtonId();

    SharedPreferences settings = getSharedPreferences( PREF_TAG, 0 );
    SharedPreferences.Editor editor = settings.edit();

    if( name != null )
      editor.putString( Defaults.NAME_PROPERTY, name );

    if( email != null )
      editor.putString( BackendlessUser.EMAIL_KEY, email );

    if( birthdate != null )
      editor.putString( Defaults.BIRTH_DATE_PROPERTY, birthdate );

    if( gender != 0 )
      editor.putInt( Defaults.GENDER_PROPERTY, gender );

    editor.commit();
  }

  @Override
  protected void onStart()
  {
    super.onStart();

    //Restoring user input data
    SharedPreferences settings = getSharedPreferences( PREF_TAG, 0 );
    String name = settings.getString( Defaults.NAME_PROPERTY, null );
    String email = settings.getString( BackendlessUser.EMAIL_KEY, null );
    String birthdate = settings.getString( Defaults.BIRTH_DATE_PROPERTY, null );
    int gender = settings.getInt( Defaults.GENDER_PROPERTY, 0 );

    if( name != null )
      nameField.setText( name );

    if( email != null )
      emailField.setText( email );

    if( birthdate != null )
      birthdateField.setText( birthdate );

    if( gender != 0 )
      genderRadio.check( gender );
  }
}
