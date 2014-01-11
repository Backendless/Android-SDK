package com.backendless.examples.endless.tagging;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.ArrayList;
import java.util.List;

public class PointCommentsActivity extends Activity
{
  private ProgressDialog progressDialog;
  private List<String> commentsPoint = new ArrayList<String>();
  private String geoPointId, userName;
  private ImageView imageView;
  private Button addCommentBtn, photoBtn;
  private ListView lvMain;
  private ArrayAdapter<String> adapter;

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.act_point_comments );

    photoBtn = (Button) findViewById( R.id.photoBtn );
    photoBtn.setVisibility( View.INVISIBLE );
    final TextView textViewComment = (TextView) findViewById( R.id.textViewComment );
    TextView textPointComments = (TextView) findViewById( R.id.textPointComments );
    Typeface typeface = Typeface.createFromAsset( getAssets(), "fonts/verdana.ttf" );
    textPointComments.setTypeface( typeface );
    textViewComment.setTypeface( typeface );
    textViewComment.setVisibility( View.INVISIBLE );
    final EditText editComment = (EditText) findViewById( R.id.editComment );
    editComment.setVisibility( View.INVISIBLE );
    photoBtn.setTypeface( typeface );

    Intent intent = getIntent();
    geoPointId = intent.getStringExtra( Default.GEO_POINT_ID );
    userName = (String) Backendless.UserService.CurrentUser().getProperty( "name" );
    if( userName == null )
      userName = Backendless.UserService.CurrentUser().getEmail();
    Bitmap bitmap = intent.getParcelableExtra( Default.SEND_BITMAP );
    imageView = (ImageView) findViewById( R.id.photoView );
    imageView.setImageBitmap( bitmap );
    imageView.setScaleType( ImageView.ScaleType.FIT_XY );

    final Button commentsBtn = (Button) findViewById( R.id.commentsBtn );
    commentsBtn.setTypeface( typeface );
    commentsBtn.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        imageView.setVisibility( View.INVISIBLE );
        commentsBtn.setEnabled( false );
        BackendlessDataQuery backendlessDataQuery = new BackendlessDataQuery();
        backendlessDataQuery.addProperty( "name" );
        backendlessDataQuery.addProperty( "message" );
        backendlessDataQuery.setWhereClause( "geoPointId=" + "'" + geoPointId + "'" );
        progressDialog = ProgressDialog.show( PointCommentsActivity.this, "", "Loading", true );
        Backendless.Persistence.of( Comment.class ).find( backendlessDataQuery, new AsyncCallback<BackendlessCollection<Comment>>()
        {
          @Override
          public void handleResponse( BackendlessCollection<Comment> response )
          {
            BackendlessCollection<Comment> comments = response;
            for( int i = 0; i < comments.getCurrentPage().size(); i++ )
            {
              Comment comment = comments.getCurrentPage().get( i );
              String userName = comment.getName();
              commentsPoint.add( userName + "  :  " + comment.getMessage() );
            }
            lvMain = (ListView) findViewById( R.id.commentList );
            lvMain.setVisibility( View.VISIBLE );
            adapter = new ArrayAdapter<String>( PointCommentsActivity.this, android.R.layout.simple_list_item_1, commentsPoint );
            lvMain.setAdapter( adapter );
            progressDialog.cancel();
            textViewComment.setVisibility( View.VISIBLE );
            editComment.setVisibility( View.VISIBLE );
            addCommentBtn.setVisibility( View.VISIBLE );
            commentsBtn.setVisibility( View.INVISIBLE );
            photoBtn.setVisibility( View.VISIBLE );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            progressDialog.cancel();
            Toast.makeText( PointCommentsActivity.this, fault.getMessage(), Toast.LENGTH_LONG ).show();
            textViewComment.setVisibility( View.VISIBLE );
            editComment.setVisibility( View.VISIBLE );
            addCommentBtn.setVisibility( View.VISIBLE );
            commentsBtn.setVisibility( View.INVISIBLE );
            photoBtn.setVisibility( View.VISIBLE );
          }
        } );
      }
    } );

    addCommentBtn = (Button) findViewById( R.id.addCommentBtn );
    addCommentBtn.setTypeface( typeface );
    addCommentBtn.setVisibility( View.INVISIBLE );
    addCommentBtn.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        final String commentName = editComment.getText().toString();
        if( TextUtils.isEmpty( commentName ) )
        {
          String alertMessage = "Please, fill in empty field!";
          Toast.makeText( PointCommentsActivity.this, alertMessage, Toast.LENGTH_LONG ).show();
          return;
        }
        progressDialog = ProgressDialog.show( PointCommentsActivity.this, "", "Loading", true );
        Comment commentPoint = new Comment( userName, commentName, geoPointId );
        Backendless.Persistence.save( commentPoint, new BackendlessCallback<Comment>()
        {
          @Override
          public void handleResponse( Comment commentPoint )
          {
            Intent categoryIntent = getIntent();
            String name = categoryIntent.getStringExtra( Default.SEARCH_CATEGORY_NAME );
            Intent intent = new Intent();
            intent.putExtra( Default.SEARCH_CATEGORY_NAME, name );
            setResult( Default.ADD_NEW_COMMENT, intent );
            progressDialog.cancel();
            finish();
            Toast.makeText( PointCommentsActivity.this, "A new comment was successfully added", Toast.LENGTH_LONG ).show();
          }
        } );
      }
    } );
    photoBtn.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        adapter.clear();
        lvMain.setAdapter( adapter );
        lvMain.setVisibility( View.INVISIBLE );
        textViewComment.setVisibility( View.INVISIBLE );
        editComment.setVisibility( View.INVISIBLE );
        addCommentBtn.setVisibility( View.VISIBLE );
        imageView.setVisibility( View.VISIBLE );
        photoBtn.setVisibility( View.INVISIBLE );
        commentsBtn.setVisibility( View.VISIBLE );
        commentsBtn.setEnabled( true );
      }
    } );
  }
}
