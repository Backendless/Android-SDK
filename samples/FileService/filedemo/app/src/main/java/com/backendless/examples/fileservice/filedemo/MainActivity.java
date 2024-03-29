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

package com.backendless.examples.fileservice.filedemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.backendless.Backendless;

public class MainActivity extends Activity
{
  private TextView welcomeTextField;
  private TextView urlField;
  private Button takePhotoButton;
  private Button browseGalleryButton;

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.main );

    if( Defaults.APPLICATION_ID.equals( "" ) || Defaults.API_KEY.equals( "" ) )
    {
      showAlert( this, "Missing application ID and API key arguments. Login to Backendless Console, select your app and get the ID and key from the Manage > App Settings screen. Copy/paste the values into the Backendless.initApp call" );
      return;
    }
    Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.API_KEY );
    Backendless.setUrl( Defaults.SERVER_URL );

    welcomeTextField = findViewById( R.id.welcomeTextField );
    urlField = findViewById( R.id.urlField );

    takePhotoButton = findViewById( R.id.takePhotoButton );
    takePhotoButton.setOnClickListener( view -> {
      Intent cameraIntent = new Intent( android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
      startActivityForResult( cameraIntent, Defaults.CAMERA_REQUEST );
    } );

    browseGalleryButton = findViewById( R.id.galleryButton );
    browseGalleryButton.setOnClickListener( v -> {
      Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
      photoPickerIntent.setType("image/*");
      startActivityForResult(photoPickerIntent, Defaults.SELECT_PHOTO);
    } );

    findViewById( R.id.browseUploadedButton ).setOnClickListener( view -> {
      Intent intent = new Intent( MainActivity.this, BrowseActivity.class );
      startActivity( intent );
    } );
  }

  @Override
  public void onActivityResult( int requestCode, int resultCode, Intent data )
  {
    if( resultCode != RESULT_OK )
      return;

    switch( requestCode )
    {
      case Defaults.SELECT_PHOTO:
      case Defaults.CAMERA_REQUEST:
        data.setClass( getBaseContext(), UploadingActivity.class );
        startActivityForResult( data, Defaults.URL_REQUEST );
        break;

      case Defaults.URL_REQUEST:
        welcomeTextField.setText( getResources().getText( R.string.welcome_text ) );
        urlField.setText( (String) data.getExtras().get( Defaults.DATA_TAG ) );
        takePhotoButton.setText( getResources().getText( R.string.takeAnotherPhoto ) );
    }
  }

  public static void showAlert( final Activity context, String message )
  {
    new AlertDialog.Builder( context )
            .setTitle( "An error occurred" )
            .setMessage( message )
            .setPositiveButton( "OK", ( dialogInterface, i ) -> context.finish() ).show();
  }
}
