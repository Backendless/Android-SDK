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
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegisterActivity extends Activity
{
  private final static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat( "yyyy/MM/dd" );
  private final static String NAME_KEY = "name";
  private final static String GENDER_KEY = "gender";
  private final static String DATE_OF_BIRTH_KEY = "dateofbirth";
  private final static String LOGIN_KEY = "login";

  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.register );

    final EditText nameField = (EditText) findViewById( R.id.nameField );
    final EditText passwordField = (EditText) findViewById( R.id.passwordField );
    final EditText verifyPasswordField = (EditText) findViewById( R.id.verifyPasswordField );
    final EditText emailField = (EditText) findViewById( R.id.emailField );
    final RadioGroup genderRadio = (RadioGroup) findViewById( R.id.genderRadio );
    final Calendar dateOfBirth = Calendar.getInstance();
    final EditText dateOfBirthField = (EditText) findViewById( R.id.dateOfBirthField );
    dateOfBirthField.setText( SIMPLE_DATE_FORMAT.format( dateOfBirth.getTime() ) );
    findViewById( R.id.dateOfBirthField ).setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View view )
      {
        new DatePickerDialog( RegisterActivity.this, new DatePickerDialog.OnDateSetListener()
        {
          @Override
          public void onDateSet( DatePicker datePicker, int year, int monthOfYear, int dayOfMonth )
          {
            dateOfBirth.set( Calendar.YEAR, year );
            dateOfBirth.set( Calendar.MONTH, monthOfYear );
            dateOfBirth.set( Calendar.DAY_OF_MONTH, dayOfMonth );
            dateOfBirthField.setText( SIMPLE_DATE_FORMAT.format( dateOfBirth.getTime() ) );
          }
        }, dateOfBirth.get( Calendar.YEAR ), dateOfBirth.get( Calendar.MONTH ), dateOfBirth.get( Calendar.DAY_OF_MONTH ) ).show();
      }
    } );

    findViewById( R.id.registerButton ).setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View view )
      {
        String name = nameField.getText().toString();
        if( name == null || name.equals( "" ) )
        {
          showToast( "Name cannot be empty" );
          return;
        }

        String password = passwordField.getText().toString();
        if( password == null || password.equals( "" ) )
        {
          showToast( "Password cannot be empty" );
          return;
        }

        String verifyPassword = verifyPasswordField.getText().toString();
        if( !password.equals( verifyPassword ) )
        {
          showToast( "Passwords does not match" );
          return;
        }

        String email = emailField.getText().toString();
        if( email == null || email.equals( "" ) )
        {
          showToast( "Email cannot be empty" );
          return;
        }

        Gender gender = null;
        switch( genderRadio.getCheckedRadioButtonId() )
        {
          case -1:
            showToast( "Please select your gender" );
            return;

          case R.id.genderMale:
            gender = Gender.MALE;
            break;

          case R.id.genderFemale:
            gender = Gender.FEMALE;
        }

        BackendlessUser user = new BackendlessUser();
        user.setPassword( password );
        user.setEmail( email );
        user.setProperty( NAME_KEY, name );
        user.setProperty( GENDER_KEY, gender );
        user.setProperty( DATE_OF_BIRTH_KEY, dateOfBirth.getTime() );
        user.setProperty( LOGIN_KEY, email );

        Backendless.UserService.register( user, new DefaultCallback<BackendlessUser>( RegisterActivity.this )
        {
          @Override
          public void handleResponse( BackendlessUser response )
          {
            super.handleResponse( response );
            startActivity( new Intent( getBaseContext(), RegisteredActivity.class ) );
            finish();
          }
        } );
      }
    } );
  }

  private void showToast( String msg )
  {
    Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
  }

  private static enum Gender
  {
    MALE, FEMALE
  }
}