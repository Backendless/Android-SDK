package com.backendless.examples.endless.tagging;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

public class AddCommentActivity extends Activity
{
  private ProgressDialog progressDialog;

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.act_add_comment );

    TextView textPointName = (TextView) findViewById( R.id.textPointName );
    TextView textComment = (TextView) findViewById( R.id.textAddComment );
    Typeface typeface = Typeface.createFromAsset( getAssets(), "fonts/verdana.ttf" );
    textPointName.setTypeface( typeface );
    textComment.setTypeface( typeface );

    Intent intent = getIntent();
    String filePath = intent.getStringExtra( Default.FILE_PATH );
    Bitmap myBitmap = BitmapFactory.decodeFile( filePath, null );
    ImageView imageView = (ImageView) findViewById( R.id.imagePhotoNew );
    imageView.setImageBitmap( myBitmap );
    imageView.setScaleType( ImageView.ScaleType.FIT_XY );

    Button addCommentBtn = (Button) findViewById( R.id.addCommentBtn );
    addCommentBtn.setTypeface( typeface );
    addCommentBtn.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        EditText addNewPointName = (EditText) findViewById( R.id.editNewPointName );
        final String pointNameNew = addNewPointName.getText().toString();
        if( TextUtils.isEmpty( pointNameNew ) )
        {
          String alertMessage = "Please, fill in empty field!";
          Toast.makeText( AddCommentActivity.this, alertMessage, Toast.LENGTH_LONG ).show();
          return;
        }
        progressDialog = ProgressDialog.show( AddCommentActivity.this, "", "Loading", true );
        Intent intent = new Intent();
        intent.putExtra( Default.NEW_POINT_NAME, pointNameNew );
        setResult( Default.ADD_NEW_POINT_RESULT, intent );
        progressDialog.cancel();
        finish();
      }
    } );
  }
}
