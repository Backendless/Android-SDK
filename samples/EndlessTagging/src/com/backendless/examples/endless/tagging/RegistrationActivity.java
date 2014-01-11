package com.backendless.examples.endless.tagging;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class RegistrationActivity extends Activity
{
  private ProgressDialog progressDialog;

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.act_registration );

    TextView textEmail = (TextView) findViewById( R.id.textEmail );
    TextView textPass = (TextView) findViewById( R.id.textPass );
    TextView textName = (TextView) findViewById( R.id.textName );
    TextView textRegistration = (TextView) findViewById( R.id.textRegistration );
    Typeface typeface = Typeface.createFromAsset( getAssets(), "fonts/verdana.ttf" );
    textEmail.setTypeface( typeface );
    textPass.setTypeface( typeface );
    textName.setTypeface( typeface );
    textRegistration.setTypeface( typeface );

    Button registrationBtn = (Button) findViewById( R.id.registrationBtn );
    registrationBtn.setTypeface( typeface );
    registrationBtn.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View view )
      {

        EditText editEmailText = (EditText) findViewById( R.id.emailEdit );
        EditText editPasswordText = (EditText) findViewById( R.id.passwordEdit );
        EditText editNameText = (EditText) findViewById( R.id.editUserName );
        final String messageMail = editEmailText.getText().toString();
        final String messagePassword = editPasswordText.getText().toString();
        final String messageName = editNameText.getText().toString();

        if( TextUtils.isEmpty( messageMail ) || TextUtils.isEmpty( messagePassword ) || TextUtils.isEmpty( messageName ) )
        {
          String alertMessage = "Please, fill in all fields!";
          Toast.makeText( RegistrationActivity.this, alertMessage, Toast.LENGTH_LONG ).show();
          return;
        }
        progressDialog = ProgressDialog.show( RegistrationActivity.this, "", "Loading", true );
        BackendlessUser userObj = new BackendlessUser();
        userObj.setProperty( "name", messageName );
        userObj.setEmail( messageMail );
        userObj.setPassword( messagePassword );
        Backendless.UserService.register( userObj, new AsyncCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser backendlessUser )
          {
            final Intent intent = new Intent( RegistrationActivity.this, LoginActivity.class );
            intent.putExtra( Default.EXTRA_PASSWORD, messagePassword );
            intent.putExtra( Default.EXTRA_EMAIL, messageMail );
            startActivity( intent );
            progressDialog.cancel();
            finish();
            Toast.makeText( RegistrationActivity.this, "Registration was successful", Toast.LENGTH_LONG ).show();
          }

          @Override
          public void handleFault( BackendlessFault backendlessFault )
          {
            progressDialog.cancel();
            Toast.makeText( RegistrationActivity.this, backendlessFault.getMessage(), Toast.LENGTH_LONG ).show();
          }
        } );
      }
    } );
    Button cancelBtn = (Button) findViewById( R.id.cancelBtn );
    cancelBtn.setTypeface( typeface );
    cancelBtn.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View view )
      {
        Intent intent = new Intent( RegistrationActivity.this, LoginActivity.class );
        startActivity( intent );
        finish();
      }
    } );
  }
}
