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

package com.backendless.beacon;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.geo.beacon.BackendlessBeacon;
import com.backendless.geo.beacon.IPresenceListener;

import java.util.Map;

public class Main extends Activity
{
  private final static int FREQUENCY = 60;
  public final static double DISTANCE_CHANGE = 0.1;
  private PresenceListener presenceListener;

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.main );

    Backendless.setUrl( Defaults.SERVER_URL );
    Backendless.initApp( getBaseContext(), Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );
    initUI();
  }

  private void initUI()
  {
    final TextView textView = (TextView) findViewById( R.id.textView );
    textView.setText( "" );
    presenceListener = new PresenceListener( textView );

    final CheckBox discovery = (CheckBox) findViewById( R.id.cBDiscovery );

    findViewById( R.id.btnStartMonitoring ).setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        Backendless.Geo.Presence.startMonitoring( discovery.isChecked(), FREQUENCY, presenceListener, DISTANCE_CHANGE, new AsyncCallback<Void>()
        {
          @Override
          public void handleResponse( Void aVoid )
          {
            Toast.makeText( Main.this, "Beacon monitoring started", Toast.LENGTH_LONG ).show();
          }

          @Override
          public void handleFault( BackendlessFault backendlessFault )
          {
            Toast.makeText( Main.this, "Failed: " + backendlessFault.toString(), Toast.LENGTH_LONG ).show();
          }
        } );
      }
    } );

    findViewById( R.id.btnStopMonitoring ).setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        Backendless.Geo.Presence.stopMonitoring();
        Toast.makeText( Main.this, "Beacon monitoring stopped", Toast.LENGTH_LONG ).show();
      }
    } );
  }

  public class PresenceListener implements IPresenceListener
  {
    private TextView textView;

    public PresenceListener( TextView textView )
    {
      this.textView = textView;
    }

    @Override
    public void onDetectedBeacons( final Map<BackendlessBeacon, Double> beaconToDistances )
    {
      final StringBuilder text = new StringBuilder( "Detected beacon!!!\n" );
      for( BackendlessBeacon backendlessBeacon : beaconToDistances.keySet() )
      {
        text.append( "Distance: " + beaconToDistances.get( backendlessBeacon ) + " - " );
        text.append( backendlessBeacon.toString() + "\n\n" );
      }
      runOnUiThread( new Runnable()
      {
        @Override
        public void run()
        {
          textView.setText( text.toString() );
        }
      } );
    }
  }
}