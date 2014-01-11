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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;

public class MainActivity extends Activity implements LocationListener
{
  private TextView latitudeField;
  private TextView longitudeField;

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.main );

    if( Defaults.APPLICATION_ID.equals( "" ) || Defaults.SECRET_KEY.equals( "" ) || Defaults.VERSION.equals( "" ) )
    {
      showAlert( this, "Missing application ID and secret key arguments. Login to Backendless Console, select your app and get the ID and key from the Manage > App Settings screen. Copy/paste the values into the Backendless.initApp call" );
      return;
    }

    Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );

    latitudeField = (TextView) findViewById( R.id.latitudeField );
    longitudeField = (TextView) findViewById( R.id.longitudeField );
    final TextView radiusField = (TextView) findViewById( R.id.radiusField );
    final SeekBar radiusBar = (SeekBar) findViewById( R.id.radiusBar );
    radiusBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged( SeekBar seekBar, int progress, boolean b )
      {
        radiusField.setText( String.valueOf( progress ) );
      }

      @Override
      public void onStartTrackingTouch( SeekBar seekBar )
      {/*stub*/}

      @Override
      public void onStopTrackingTouch( SeekBar seekBar )
      {/*stub*/}
    } );

    LocationManager locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
    Criteria criteria = new Criteria();

    String provider = locationManager.getBestProvider( criteria, true );
    if( provider == null )
    {
      AlertDialog alertDialog = new AlertDialog.Builder( this ).create();
      alertDialog.setTitle( "Geo provide" );
      alertDialog.setMessage( "Could not find any geo provider. Click 'OK' button to use 0:0 coordinates." );
      alertDialog.setButton( "OK", new DialogInterface.OnClickListener()
      {
        public void onClick( DialogInterface dialog, int which )
        {
          latitudeField.setText( "0" );
          longitudeField.setText( "0" );
        }
      } );
      alertDialog.show();
    }
    else
      locationManager.requestSingleUpdate( provider, this, null );

    findViewById( R.id.searchButton ).setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View view )
      {
        int radius = radiusBar.getProgress();
        if( radius != 0 )
        {
          double latitude = Double.valueOf( latitudeField.getText().toString() );
          double longitude = Double.valueOf( longitudeField.getText().toString() );
          Intent intent = new Intent( getBaseContext(), PointsActivity.class );
          intent.putExtra( Defaults.LATITUDE_TAG, latitude );
          intent.putExtra( Defaults.LONGITUDE_TAG, longitude );
          intent.putExtra( Defaults.RADIUS_TAG, radius );
          startActivity( intent );
        }
        else
          Toast.makeText( MainActivity.this, "Search radius cannot be 0", Toast.LENGTH_SHORT ).show();
      }
    } );
  }

  @Override
  public void onLocationChanged( Location location )
  {
    latitudeField.setText( String.valueOf( location.getLatitude() ) );
    longitudeField.setText( String.valueOf( location.getLongitude() ) );
  }

  @Override
  public void onStatusChanged( String s, int i, Bundle bundle )
  {/*stub*/}

  @Override
  public void onProviderEnabled( String s )
  {/*stub*/}

  @Override
  public void onProviderDisabled( String s )
  {/*stub*/}

  public static void showAlert( final Activity context, String message )
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
