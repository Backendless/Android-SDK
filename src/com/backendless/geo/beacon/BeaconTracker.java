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

package com.backendless.geo.beacon;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import com.backendless.AndroidService;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import org.altbeacon.beacon.*;

import java.util.Collection;

/**
 * Created by baas on 01.09.15.
 */
public class BeaconTracker implements BeaconConsumer, RangeNotifier
{
  private BeaconManager mBeaconManager;
  private BeaconMonitoring beaconMonitor;
  private boolean discovery;
  private int frequency;

  public void startMonitoring( boolean runDiscovery, int frequency, final AsyncCallback<Void> responder )
  {
    waiteAndroidService();
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    if( mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled() )
      responder.handleFault( new BackendlessFault( new BackendlessException( ExceptionMessage.BLUETOOTH_UNAVALIBLE ) ) );

    mBeaconManager = BeaconManager.getInstanceForApplication( BeaconTracker.this.getApplicationContext() );
    if( responder != null && isMonitored() )
      responder.handleFault( new BackendlessFault( new BackendlessException( ExceptionMessage.PRESENCE_MONITORING ) ) );

    this.discovery = runDiscovery;
    this.frequency = frequency;

    saveSettings();

    Backendless.CustomService.invoke( BeaconConstancts.SERVICE_NAME, BeaconConstancts.SERVICE_VERSION, "getenabled", new Object[] { }, new AsyncCallback<BeaconsInfo>()
    {
      @Override
      public void handleResponse( BeaconsInfo response )
      {
        addBeaconParsers( mBeaconManager );
        bind();

        if( responder != null )
          responder.handleResponse( null );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        if( responder != null )
          responder.handleFault( fault );
      }
    } );
  }

  public void stopMonitoring()
  {
    unbind();
  }

  @Override
  public void onBeaconServiceConnect()
  {
    initSettings();
    beaconMonitor = new BeaconMonitoring( discovery, frequency );
    Region region = new Region( "all-beacons-region", null, null, null );
    try
    {
      mBeaconManager.startRangingBeaconsInRegion( region );
    }
    catch( RemoteException e )
    {
      Backendless.Logging.getLogger( BeaconTracker.class ).error( "Cannot start beacon ranging", e );
    }
    mBeaconManager.setRangeNotifier( this );
  }

  @Override
  public Context getApplicationContext()
  {
    return ((AndroidService) AndroidService.recoverService()).getApplicationContext();
  }

  @Override
  public void unbindService( ServiceConnection serviceConnection )
  {
    getApplicationContext().unbindService( serviceConnection );
  }

  @Override
  public boolean bindService( Intent intent, ServiceConnection serviceConnection, int i )
  {
    return getApplicationContext().bindService( intent, serviceConnection, i );
  }

  @Override
  public void didRangeBeaconsInRegion( Collection<org.altbeacon.beacon.Beacon> collection, Region region )
  {
    beaconMonitor.onDetectedBeacons( collection );
  }

  private boolean isMonitored()
  {
    return mBeaconManager.isBound( this );
  }

  private void addBeaconParsers( BeaconManager beaconManager )
  {
    beaconManager.getBeaconParsers().clear();

    // iBeacon
    beaconManager.getBeaconParsers().add( new BeaconParser().
            setBeaconLayout( "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24" ) );

    //Eddystone - detect the main identifier (UID) frame:
    beaconManager.getBeaconParsers().add( new BeaconParser().
            setBeaconLayout( "s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19" ) );
    //Eddystone - detect the telemetry (TLM) frame:
    beaconManager.getBeaconParsers().add( new BeaconParser().
            setBeaconLayout( "x,s:0-1=feaa,m:2-2=20,d:3-3,d:4-5,d:6-7,d:8-11,d:12-15" ) );
    //Eddystone - detect the URL frame:
    beaconManager.getBeaconParsers().add( new BeaconParser().
            setBeaconLayout( "s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-20" ) );
  }

  private void bind()
  {
    if( !mBeaconManager.isBound( this ) )
      mBeaconManager.bind( this );
  }

  private void unbind()
  {
    if( mBeaconManager.isBound( this ) )
      mBeaconManager.unbind( this );
  }

  private void saveSettings()
  {
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences( getApplicationContext() );
    SharedPreferences.Editor editor = sharedPref.edit();
    editor.putBoolean( BeaconConstancts.DISCOVERY, discovery );
    editor.putInt( BeaconConstancts.FREQUENCY, frequency );
    editor.apply();
  }

  private void initSettings()
  {
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences( getApplicationContext() );
    discovery = sharedPref.getBoolean( BeaconConstancts.DISCOVERY, BeaconConstancts.DEFUTL_DISCOVERY );
    frequency = sharedPref.getInt( BeaconConstancts.FREQUENCY, BeaconConstancts.DEFAULT_FREQUENCY );
  }

  private void waiteAndroidService()
  {
    while( ! (AndroidService.recoverService() instanceof AndroidService) )
      try
      {
        Thread.sleep( 500 );
      }
      catch( InterruptedException e )
      {
        throw new RuntimeException( e );
      }
  }
}
