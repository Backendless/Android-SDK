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

public class LoginActivity extends Activity
{
  private ProgressDialog progressDialog;

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.act_login );

    Backendless.initApp( LoginActivity.this, Default.APP_ID, Default.SECRET_KEY, Default.VERSION );

    TextView textEmail = (TextView) findViewById( R.id.textEmail );
    TextView textPass = (TextView) findViewById( R.id.textPass );
    TextView textPiar = (TextView) findViewById( R.id.textPiar );
    TextView textOr = (TextView) findViewById( R.id.textOr );
    Typeface typeface = Typeface.createFromAsset( getAssets(), "fonts/verdana.ttf" );
    textEmail.setTypeface( typeface );
    textPass.setTypeface( typeface );
    textPiar.setTypeface( typeface );
    textOr.setTypeface( typeface );

    final Button loginBtn = (Button) findViewById( R.id.loginBtn );
    loginBtn.setTypeface( typeface );
    loginBtn.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View view )
      {
        EditText editEmailText = (EditText) findViewById( R.id.emailEdit );
        EditText editPasswordText = (EditText) findViewById( R.id.passwordEdit );

        final String messageMail = editEmailText.getText().toString();
        final String messagePassword = editPasswordText.getText().toString();

        if( TextUtils.isEmpty( messageMail ) || TextUtils.isEmpty( messagePassword ) )
        {
          String alertMessage = "Please, fill in empty fields!";
          Toast.makeText( LoginActivity.this, alertMessage, Toast.LENGTH_LONG ).show();
          return;
        }
        progressDialog = ProgressDialog.show( LoginActivity.this, "", "Loading", true );
        Backendless.UserService.login( messageMail, messagePassword, new AsyncCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser backendlessUser )
          {
            progressDialog.cancel();
            Intent intent = new Intent( LoginActivity.this, EndlessTaggingActivity.class );
            intent.putExtra( Default.EXTRA_EMAIL, messageMail );
            intent.putExtra( Default.EXTRA_PASSWORD, messagePassword );
            startActivity( intent );
          }

          @Override
          public void handleFault( BackendlessFault backendlessFault )
          {
            progressDialog.cancel();
            Toast.makeText( LoginActivity.this, backendlessFault.getMessage(), Toast.LENGTH_LONG ).show();
          }
        } );
      }
    }

    );
    Button goRegistrationBtn = (Button) findViewById( R.id.goRegistrationBtn );
    goRegistrationBtn.setTypeface( typeface );
    goRegistrationBtn.setOnClickListener( new View.OnClickListener()

    {
      @Override
      public void onClick( View view )
      {
        Intent intent = new Intent( LoginActivity.this, RegistrationActivity.class );
        startActivity( intent );
        finish();
      }
    } );

    Button facebookBtn = (Button) findViewById( R.id.facebookBtn );
    facebookBtn.setTypeface( typeface );
    facebookBtn.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        Backendless.UserService.loginWithFacebook( LoginActivity.this, new AsyncCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser response )
          {
            Intent intent = new Intent( LoginActivity.this, EndlessTaggingActivity.class );
            startActivity( intent );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            Toast.makeText( LoginActivity.this, fault.getMessage(), Toast.LENGTH_LONG ).show();
          }
        } );
      }
    } );
    Button twitterBtn = (Button) findViewById( R.id.twitterBtn );
    twitterBtn.setTypeface( typeface );
    twitterBtn.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        Backendless.UserService.loginWithTwitter( LoginActivity.this, new AsyncCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser response )
          {
            Intent intent = new Intent( LoginActivity.this, EndlessTaggingActivity.class );
            startActivity( intent );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            Toast.makeText( LoginActivity.this, fault.getMessage(), Toast.LENGTH_LONG ).show();
          }
        } );
      }
    } );
  }
}
