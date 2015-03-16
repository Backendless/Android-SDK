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
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BrowseActivity extends Activity
{
  private static int itemWidth;
  private static ImageAdapter imageAdapter;
  private static int padding;

  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.browse );

    padding = (int) getResources().getDimension( R.dimen.micro_padding );

    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics( displayMetrics );
    int screenWidth = displayMetrics.widthPixels;
    itemWidth = (screenWidth - (2 * (int) getResources().getDimension( R.dimen.default_margin ))) / 3;

    GridView gridView = (GridView) findViewById( R.id.gridView );
    imageAdapter = new ImageAdapter( this );
    gridView.setAdapter( imageAdapter );

    Toast.makeText( BrowseActivity.this, "Downloading images...", Toast.LENGTH_SHORT ).show();

    BackendlessDataQuery backendlessDataQuery = new BackendlessDataQuery();
    backendlessDataQuery.setQueryOptions( new QueryOptions( 100, 0, "uploaded" ) );
    Backendless.Persistence.of( ImageEntity.class ).find( backendlessDataQuery, new AsyncCallback<BackendlessCollection<ImageEntity>>()
    {
      @Override
      public void handleResponse( final BackendlessCollection<ImageEntity> response )
      {
        Toast.makeText( BrowseActivity.this, "Will add " + response.getCurrentPage().size() + " images", Toast.LENGTH_SHORT ).show();

        new Thread()
        {
          @Override
          public void run()
          {
            List<ImageEntity> imageEntities = response.getCurrentPage();

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

  private Handler imagesHandler = new Handler( new Handler.Callback()
  {
    @Override
    public boolean handleMessage( Message message )
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
    private List<Bitmap> images = new ArrayList<Bitmap>();

    public ImageAdapter( Context c )
    {
      context = c;
    }

    @Override
    public int getCount()
    {
      return images.size();
    }

    public void add( Bitmap bitmap )
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