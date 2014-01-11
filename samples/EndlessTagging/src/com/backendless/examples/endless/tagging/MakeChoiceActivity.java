package com.backendless.examples.endless.tagging;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import java.io.*;

public class MakeChoiceActivity extends Activity

{
  private ProgressDialog progressDialog;

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.act_make_choice );

    TextView textMake = (TextView) findViewById( R.id.textMake );
    TextView textOr = (TextView) findViewById( R.id.textOr );
    Typeface typeface = Typeface.createFromAsset( getAssets(), "fonts/verdana.ttf" );
    textOr.setTypeface( typeface );
    textMake.setTypeface( typeface );

    Button photoBtn = (Button) findViewById( R.id.photoBtn );
    photoBtn.setTypeface( typeface );
    photoBtn.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        Intent intent = new Intent( MakeChoiceActivity.this, MakePhotoActivity.class );
        startActivityForResult( intent, Default.MAKE_NEW_PHOTO );
      }
    } );
    Button chooseBtn = (Button) findViewById( R.id.chooseBtn );
    chooseBtn.setTypeface( typeface );
    chooseBtn.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        Intent photoPickerIntent = new Intent( Intent.ACTION_PICK );
        photoPickerIntent.setType( "image/*" );
        startActivityForResult( photoPickerIntent, Default.SELECT_PHOTO );
      }
    } );
  }

  @Override
  protected void onActivityResult( int requestCode, int resultCode, Intent imageReturnedIntent )
  {
    super.onActivityResult( requestCode, resultCode, imageReturnedIntent );

    switch( requestCode )
    {
      case Default.SELECT_PHOTO:
        if( resultCode == RESULT_OK )
        {
          Uri selectedImage = imageReturnedIntent.getData();
          File imageFile = new File( getRealPathFromURI( selectedImage ) );
          final String filePath = imageFile.getPath();
          progressDialog = ProgressDialog.show( MakeChoiceActivity.this, "", "Loading", true );
          Backendless.Files.upload( imageFile, Default.DEFAULT_PATH_ROOT, new AsyncCallback<BackendlessFile>()
          {
            @Override
            public void handleResponse( BackendlessFile response )
            {
              String photoBrowseUrl = response.getFileURL();
              Intent intent = new Intent();
              intent.putExtra( Default.PHOTO_BROWSE_URL, photoBrowseUrl );
              intent.putExtra( Default.FILE_PATH, filePath );
              setResult( Default.ADD_NEW_PHOTO_RESULT, intent );
              progressDialog.cancel();
              finish();
            }

            @Override
            public void handleFault( BackendlessFault fault )
            {
              progressDialog.cancel();
              Toast.makeText( MakeChoiceActivity.this, fault.toString(), Toast.LENGTH_SHORT ).show();
            }
          } );
        }
        break;
      case Default.MAKE_NEW_PHOTO:
        String photoCameraUrl = imageReturnedIntent.getStringExtra( Default.PHOTO_CAMERA_URL );
        String filePath = imageReturnedIntent.getStringExtra( Default.FILE_PATH );
        Intent intent = new Intent();
        intent.putExtra( Default.PHOTO_CAMERA_URL, photoCameraUrl );
        intent.putExtra( Default.FILE_PATH, filePath );
        setResult( Default.ADD_NEW_PHOTO_RESULT, intent );
        finish();
        break;
    }
  }

  private String getRealPathFromURI( Uri contentURI )
  {
    Cursor cursor = getContentResolver().query( contentURI, null, null, null, null );
    if( cursor == null )
    {
      return contentURI.getPath();
    }
    else
    {
      cursor.moveToFirst();
      int idx = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
      return cursor.getString( idx );
    }
  }
}


