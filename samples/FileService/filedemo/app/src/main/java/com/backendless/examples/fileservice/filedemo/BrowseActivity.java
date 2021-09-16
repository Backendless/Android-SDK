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
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowMetrics;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BrowseActivity extends Activity
{
  private static int itemWidth;
  private static int padding;
  private ImageAdapter imageAdapter;

  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.browse );

    padding = (int) getResources().getDimension( R.dimen.micro_padding );

    WindowMetrics currentWindowMetrics = getWindowManager().getCurrentWindowMetrics();
    int screenWidth = currentWindowMetrics.getBounds().width();
    itemWidth = (screenWidth - (2 * (int) getResources().getDimension( R.dimen.default_margin ))) / 3;

    GridView gridView = findViewById( R.id.gridView );
    imageAdapter = new ImageAdapter( this );
    gridView.setAdapter( imageAdapter );

    Toast.makeText( BrowseActivity.this, "Downloading images...", Toast.LENGTH_SHORT ).show();

    DataQueryBuilder queryBuilder = DataQueryBuilder.create()
            .addSortBy( "uploaded" )
            .setPageSize( 100 )
            .setOffset( 0 );

    Backendless.Persistence.of( ImageEntity.class ).find( queryBuilder, new AsyncCallback<List<ImageEntity>>()
    {
      @Override
      public void handleResponse( final List<ImageEntity> imageEntities )
      {
        Toast.makeText( BrowseActivity.this, "Will add " + imageEntities.size() + " images", Toast.LENGTH_SHORT ).show();

        new Thread()
        {
          @Override
          public void run()
          {
            for( ImageEntity imageEntity : imageEntities )
            {
              Message message = new Message();
              try
              {
                URL url = new URL( imageEntity.getUrl() );
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput( true );
                connection.connect();
                InputStream input = connection.getInputStream();
                message.obj = BitmapFactory.decodeStream( input );
              }
              catch( Exception e )
              {
                message.obj = e;
              }

              imagesHandler.sendMessage( message );
            }
          }
        }.start();
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        Toast.makeText( BrowseActivity.this, "Make some upload first", Toast.LENGTH_SHORT ).show();
      }
    } );
  }

  private Handler imagesHandler = new Handler( Looper.getMainLooper(), new Handler.Callback()
  {
    @Override
    public boolean handleMessage( @NonNull Message message )
    {
      Object result = message.obj;

      if( result instanceof Bitmap )
        imageAdapter.add( (Bitmap) result );
      else
        Toast.makeText( BrowseActivity.this, ((Exception) result).getMessage(), Toast.LENGTH_SHORT ).show();

      return true;
    }
  } );

  private static class ImageAdapter extends BaseAdapter
  {
    private Context context;
    private List<Bitmap> images = new ArrayList<>();

    ImageAdapter( Context c )
    {
      context = c;
    }

    @Override
    public int getCount()
    {
      return images.size();
    }

    void add( Bitmap bitmap )
    {
      images.add( bitmap );
      notifyDataSetChanged();
    }

    public Bitmap getItem( int position )
    {
      return images.get( position );
    }

    public long getItemId( int position )
    {
      return position;
    }

    public View getView( final int position, View convertView, ViewGroup parent )
    {
      ImageView imageView;
      if( convertView == null )
      {
        imageView = new ImageView( context );
        imageView.setLayoutParams( new GridView.LayoutParams( itemWidth, itemWidth ) );
        imageView.setScaleType( ImageView.ScaleType.CENTER_CROP );
        imageView.setPadding( padding, padding, padding, padding );
      }
      else
        imageView = (ImageView) convertView;

      imageView.setImageBitmap( getItem( position ) ); // Load image into ImageView

      return imageView;
    }
  }
}