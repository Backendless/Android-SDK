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

package com.backendless.examples.geoservice.geodemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.geo.BackendlessGeoQuery;
import com.backendless.geo.GeoPoint;
import com.backendless.geo.Units;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PointsActivity extends Activity
{
  private PointsAdapter adapter;
  private BackendlessGeoQuery backendlessGeoQuery;
  private ProgressDialog progressDialog;

  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.points );

    ListView listview = (ListView) findViewById( R.id.pointsView );
    View header = getLayoutInflater().inflate( R.layout.points_header, null );
    listview.addHeaderView( header );
    adapter = new PointsAdapter( this );
    listview.setAdapter( adapter );

    final TextView radiusField = (TextView) findViewById( R.id.radiusField );
    final SeekBar radiusBar = (SeekBar) findViewById( R.id.radiusBar );
    radiusBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener()
    {
      @Override
      public void onProgressChanged( SeekBar seekBar, int progress, boolean fromUser )
      {
        radiusField.setText( String.valueOf( progress ) );
      }

      @Override
      public void onStartTrackingTouch( SeekBar seekBar )
      {/*stub*/}

      @Override
      public void onStopTrackingTouch( SeekBar seekBar )
      {
        int radius = seekBar.getProgress();
        backendlessGeoQuery.setRadius( radius == 0 ? 0.0000001 : radius );
        searchPoints();
      }
    } );

    Intent intent = getIntent();
    double latitude = intent.getDoubleExtra( Defaults.LATITUDE_TAG, 0 );
    double longitude = intent.getDoubleExtra( Defaults.LONGITUDE_TAG, 0 );
    int radius = intent.getIntExtra( Defaults.RADIUS_TAG, 1 );
    radiusBar.setProgress( radius );
    backendlessGeoQuery = new BackendlessGeoQuery( latitude, longitude, radius, Units.KILOMETERS );
    backendlessGeoQuery.addCategory( Defaults.SAMPLE_CATEGORY );
    backendlessGeoQuery.setIncludeMeta( true );
    searchPoints();
  }

  private static class PointsAdapter extends BaseAdapter
  {
    private LayoutInflater inflater;
    private List<GeoPoint> pointsList = new ArrayList<GeoPoint>();

    public PointsAdapter( Context context )
    {
      inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    }

    public int getCount()
    {
      return pointsList.size();
    }

    public GeoPoint getItem( int position )
    {
      return pointsList.get( position );
    }

    public void add( GeoPoint geoPoint )
    {
      if( !pointsList.contains( geoPoint ) )
        pointsList.add( geoPoint );
    }

    public long getItemId( int position )
    {
      return position;
    }

    public View getView( int position, View convertView, ViewGroup parent )
    {
      ViewHolder holder;
      if( convertView == null )
      {
        convertView = inflater.inflate( R.layout.points_row, null, false );
        holder = new ViewHolder();
        holder.city = (TextView) convertView.findViewById( R.id.cityField );
        holder.latitude = (TextView) convertView.findViewById( R.id.latitudeField );
        holder.longitude = (TextView) convertView.findViewById( R.id.longitudeField );
        convertView.setTag( holder );
      }
      else
        holder = (ViewHolder) convertView.getTag();

      holder.city.setText( String.valueOf(pointsList.get( position ).getMetadata( Defaults.CITY_TAG )) );
      holder.latitude.setText( String.valueOf( pointsList.get( position ).getLatitude() ) );
      holder.longitude.setText( String.valueOf( pointsList.get( position ).getLongitude() ) );

      return convertView;
    }

    public void addAll( Collection<? extends GeoPoint> geoPoints )
    {
      for( GeoPoint geoPoint : geoPoints )
        add( geoPoint );
    }

    private static class ViewHolder
    {
      TextView city;
      TextView latitude;
      TextView longitude;
    }

    public void clear()
    {
      pointsList.clear();
    }
  }

  private void searchPoints()
  {
    progressDialog = ProgressDialog.show( PointsActivity.this, "", "Loading", true );
    Backendless.Geo.getPoints( backendlessGeoQuery, new AsyncCallback<BackendlessCollection<GeoPoint>>()
    {
      @Override
      public void handleResponse( BackendlessCollection<GeoPoint> geoPointBackendlessCollection )
      {
        List<GeoPoint> points = geoPointBackendlessCollection.getCurrentPage();
        adapter.clear();

        if( !points.isEmpty() )
          adapter.addAll( points );

        adapter.notifyDataSetChanged();
        progressDialog.cancel();
      }

      @Override
      public void handleFault( BackendlessFault backendlessFault )
      {
        progressDialog.cancel();
      }
    } );
  }
}