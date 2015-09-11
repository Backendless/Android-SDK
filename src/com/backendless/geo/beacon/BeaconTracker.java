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
import android.util.Base64;
import com.backendless.AndroidService;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import org.altbeacon.beacon.*;
import weborb.util.io.ISerializer;
import weborb.util.io.Serializer;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by baas on 01.09.15.
 */
public class BeaconTracker implements BeaconConsumer, RangeNotifier
{
  private BeaconManager mBeaconManager;
  private BeaconMonitoring beaconMonitor;
  private IPresenceListener listener;
  private boolean discovery;
  private int frequency;
  double distanceChange;

  private Map<BackendlessBeacon, Double> stayedBeacons = new HashMap<BackendlessBeacon, Double>();

  public void startMonitoring( boolean runDiscovery, int frequency, IPresenceListener listener, double distanceChange,
                               final AsyncCallback<Void> responder )
  {
    waitAndroidService();

    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    if( !checkCondition( mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled(), responder, new BackendlessException( ExceptionMessage.BLUETOOTH_UNAVALIBLE ) ) )
      return;

    mBeaconManager = BeaconManager.getInstanceForApplication( BeaconTracker.this.getApplicationContext() );
    if( !checkCondition( isMonitored(), responder, new BackendlessException( ExceptionMessage.PRESENCE_MONITORING ) ) )
      return;

    if( !checkCondition( frequency < 0, responder, new IllegalArgumentException( ExceptionMessage.INVALID_LOG_POLICY ) ) )
      return;

    this.discovery = runDiscovery;
    this.frequency = frequency;
    this.listener = listener;
    this.distanceChange = distanceChange;

    saveSettings();

    Backendless.CustomService.invoke( BeaconConstants.SERVICE_NAME, BeaconConstants.SERVICE_VERSION, "getenabled", new Object[] { }, new AsyncCallback<BeaconsInfo>()
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

    Map<BackendlessBeacon, Double> notifiedBeacons = new HashMap<BackendlessBeacon, Double>();
    Map<BackendlessBeacon, Double> currentBeacons = new HashMap<BackendlessBeacon, Double>();
    for( org.altbeacon.beacon.Beacon beacon : collection )
    {
      BeaconType beaconType = BeaconType.ofServiceUUID( beacon.getServiceUuid() );
      BackendlessBeacon backendlessBeacon = new BackendlessBeacon( beaconType, beacon );
      double distance = beacon.getDistance();
      currentBeacons.put( backendlessBeacon, distance );

      if( !stayedBeacons.containsKey( backendlessBeacon ) )
      {
        notifiedBeacons.put( backendlessBeacon, beacon.getDistance() );
      }
      else
      {
        double prevDistance = stayedBeacons.get( backendlessBeacon );
        if( Math.abs( prevDistance - distance ) >= distanceChange )
        {
          notifiedBeacons.put( backendlessBeacon, distance );
        }
      }
    }
    stayedBeacons = currentBeacons;

    if( !notifiedBeacons.isEmpty() )
    {
      beaconMonitor.onDetectedBeacons( new HashMap<BackendlessBeacon, Double>( notifiedBeacons ) );

      if( listener != null )
        listener.onDetectedBeacons( new HashMap<BackendlessBeacon, Double>( notifiedBeacons ) );
    }
  }

  private boolean isMonitored()
  {
    return mBeaconManager.isBound( this );
  }

  private void addBeaconParsers( BeaconManager beaconManager )
  {
    if( beaconManager.getBeaconParsers().size() > 1 )
    {
      return;
    }

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
    editor.putBoolean( BeaconConstants.DISCOVERY, discovery );
    editor.putInt( BeaconConstants.FREQUENCY, frequency );
    editor.putFloat( BeaconConstants.DISTANCE_CHANGE, (float) distanceChange );
    try
    {
      if( listener != null )
        editor.putString( BeaconConstants.PRESENCE_LISTENER, Base64.encodeToString( Serializer.toBytes( listener, ISerializer.AMF3 ), Base64.DEFAULT ) );
    }
    catch( Exception e )
    {
      Backendless.Logging.getLogger( BeaconTracker.class ).error( "Cannot save IPresenceListener : " + e.getMessage() );
    }
    editor.apply();
  }

  private void initSettings()
  {
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences( getApplicationContext() );
    discovery = sharedPref.getBoolean( BeaconConstants.DISCOVERY, BeaconConstants.DEFUTL_DISCOVERY );
    frequency = sharedPref.getInt( BeaconConstants.FREQUENCY, BeaconConstants.DEFAULT_FREQUENCY );
    distanceChange = sharedPref.getFloat( BeaconConstants.DISTANCE_CHANGE, (float) BeaconConstants.DEFAUTL_DISTANCE_CHANGE );
    String listenersStr = sharedPref.getString( BeaconConstants.PRESENCE_LISTENER, null );
    try
    {
      if(listenersStr != null)
      listener = (IPresenceListener) Serializer.fromBytes( Base64.decode( listenersStr, Base64.DEFAULT ), ISerializer.AMF3, false );
    }
    catch( IOException e )
    {
      Backendless.Logging.getLogger( BeaconTracker.class ).error( "Cannot init IPresenceListener : " + e.getMessage() );
    }
  }

  private void waitAndroidService()
  {
    while( !(AndroidService.recoverService() instanceof AndroidService) )
      try
      {
        Thread.sleep( 500 );
      }
      catch( InterruptedException e )
      {
        throw new RuntimeException( e );
      }
  }

  private boolean checkCondition( boolean condition, AsyncCallback<?> asyncCallback, Throwable t )
  {
    if( condition )
    {
      if( asyncCallback != null )
        asyncCallback.handleFault( new BackendlessFault( t ) );
      return false;
    }

    return true;
  }
}
