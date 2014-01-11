package com.backendless.examples.endless.tagging;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;

public class MakePhotoActivity extends Activity
{
  private ImageView mImage;
  private Random random = new Random();
  private ProgressDialog progressDialog;

  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.act_make_photo );

    mImage = (ImageView) findViewById( R.id.camera_image );
    Intent intent = new Intent( android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
    startActivityForResult( intent, Default.CAMERA_PIC_REQUEST );
  }

  protected void onActivityResult( int requestCode, int resultCode, Intent data )
  {
    if( requestCode == Default.CAMERA_PIC_REQUEST && resultCode == RESULT_OK )
    {
      String pictureName = "camera" + random.nextInt() + ".jpg";
      Bitmap bitmap = (Bitmap) data.getExtras().get( Default.DATA_TAG );
      Bitmap bitmapFile = bitmap;
      mImage.setImageBitmap( bitmap );
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      bitmapFile.compress( Bitmap.CompressFormat.JPEG, 100, bytes );
      File file = new File( Environment.getExternalStorageDirectory() + File.separator + pictureName );
      final String filePath = file.getPath();
      try
      {
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream( file );
        fileOutputStream.write( bytes.toByteArray() );
        fileOutputStream.close();
      }
      catch( IOException e )
      {
        e.printStackTrace();
      }
      progressDialog = ProgressDialog.show( MakePhotoActivity.this, "", "Loading", true );
      Backendless.Files.Android.upload( bitmap, Bitmap.CompressFormat.JPEG, 100, pictureName, Default.DEFAULT_PATH_ROOT, new AsyncCallback<BackendlessFile>()
      {
        @Override
        public void handleResponse( BackendlessFile response )
        {
          String photoCameraUrl = response.getFileURL();
          Intent intent = new Intent();
          intent.putExtra( Default.PHOTO_CAMERA_URL, photoCameraUrl );
          intent.putExtra( Default.FILE_PATH, filePath );
          setResult( Default.ADD_NEW_PHOTO_RESULT, intent );
          progressDialog.cancel();
          finish();
        }

        @Override
        public void handleFault( BackendlessFault fault )
        {
          progressDialog.cancel();
          Toast.makeText( MakePhotoActivity.this, fault.toString(), Toast.LENGTH_SHORT ).show();
        }
      } );
    }
    else
    {
      mImage = (ImageView) findViewById( R.id.camera_image );
      Intent intent = new Intent( android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
      startActivityForResult( intent, Default.CAMERA_PIC_REQUEST );
    }
  }
}
