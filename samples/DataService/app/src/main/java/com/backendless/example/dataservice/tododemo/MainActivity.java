package com.backendless.example.dataservice.tododemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.backendless.Backendless;

public class MainActivity extends AppCompatActivity
{
  @Override
  public void onCreate( @Nullable Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    if( Defaults.APPLICATION_ID.equals( "" ) || Defaults.API_KEY.equals( "" ) )
    {
      showAlert( this, "Missing application ID and api key arguments. Login to Backendless Console, select your app and get the ID and key from the Manage > App Settings screen. Copy/paste the values into the Backendless.initApp call" );
      return;
    }

    Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.API_KEY );

    setContentView( R.layout.base );
  }

  public void showAlert( final Activity context, String message )
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
