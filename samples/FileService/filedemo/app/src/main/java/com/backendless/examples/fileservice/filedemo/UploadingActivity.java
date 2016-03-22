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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;

import java.util.UUID;

public class UploadingActivity extends Activity
{
  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.uploading );

    LinearLayout imageContainer = (LinearLayout) findViewById( R.id.imageContainer );
    Bitmap photo = (Bitmap) getIntent().getExtras().get( Defaults.DATA_TAG );
    if( photo == null )
    {
      finishActivity( RESULT_CANCELED );
      return;
    }

    Drawable drawable = new BitmapDrawable( photo );
    drawable.setAlpha( 50 );
    imageContainer.setBackgroundDrawable( drawable );

    String name = UUID.randomUUID().toString() + ".png";

    Backendless.Files.Android.upload( photo, Bitmap.CompressFormat.PNG, 100, name, Defaults.DEFAULT_PATH_ROOT, new AsyncCallback<BackendlessFile>()
    {
      @Override
      public void handleResponse( final BackendlessFile backendlessFile )
      {
        ImageEntity entity = new ImageEntity( System.currentTimeMillis(), backendlessFile.getFileURL() );
        Backendless.Persistence.save( entity, new BackendlessCallback<ImageEntity>()
        {
          @Override
          public void handleResponse( ImageEntity imageEntity )
          {
            Intent data = new Intent();
            data.putExtra( Defaults.DATA_TAG, imageEntity.getUrl() );
            setResult( RESULT_OK, data );
            finish();
          }
        } );
      }

      @Override
      public void handleFault( BackendlessFault backendlessFault )
      {
        Toast.makeText( UploadingActivity.this, backendlessFault.toString(), Toast.LENGTH_SHORT ).show();
      }
    } );
  }
}
