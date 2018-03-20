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

package com.backendless.geo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import com.backendless.Backendless;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.geo.geofence.GeoFenceMonitoring;
import weborb.util.io.ISerializer;
import weborb.util.io.Serializer;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by julia
 */
public class LocationTracker extends Service implements LocationListener
{
  private static final String LOCATION = "location";
  private static final String LOCATION_LISTENERS = "locationListeners";
  private static final String LOCATION_LATITUDE_TAG = "locationLatitude";
  private static final String LOCATION_LONGITUDE_TAG = "locationLongitude";

  private static int minTime = 60 * 1000; // 1 minute
  private static int minDistance = 10; // meters
  private static Criteria criteria = null;
  public static float ACCEPTABLE_DISTANCE = 30; // meters

  private static LocationTracker instance;

  private LocationManager locationManager;
  private Map<String, IBackendlessLocationListener> locationListeners;
  private String provider;

  public LocationTracker()
  {
  }

  @Override
  public void onCreate()
  {
    super.onCreate();
    Backendless.initApplicationFromProperties( this );
    init();
    initLocationListeners();

    if( locationListeners != null && !locationListeners.isEmpty() )
    {
      listenBestProvider();
      changeLocation();
    }
  }

  @Override
  public int onStartCommand( Intent intent, int flags, int startId )
  {
    instance = this;
    return START_STICKY;
  }

  @Override
  public IBinder onBind( Intent intent )
  {
    return null;
  }

  /**
   * @deprecated May be removed in the next versions, since service shouldn't be directly accessible.
   */
  @Deprecated
  public static LocationTracker getInstance()
  {
    return instance;
  }

  @Override
  public void onLocationChanged( Location location )
  {
    if( location != null )
    {
      locationChanged( location );
      saveLocation( location );
    }
  }

  @Override
  public void onStatusChanged( String s, int i, Bundle bundle )
  {
    listenBestProvider();
  }

  @Override
  public void onProviderEnabled( String s )
  {
    listenBestProvider();
  }

  @Override
  public void onProviderDisabled( String s )
  {
    if( s.equals( provider ) )
      listenBestProvider();
  }

  public void addListener( String name, IBackendlessLocationListener locationListener )
  {

    if( locationListeners.isEmpty() )
     listenBestProvider();

    this.locationListeners.put( name, locationListener );
    firstListen( locationListener );
    saveLocationListeners();
  }

  public IBackendlessLocationListener getListener( String name )
  {
    return locationListeners.get( name );
  }

  public Map<String, IBackendlessLocationListener> getLocationListeners()
  {
    return locationListeners;
  }

  public void removeListener( String name )
  {
    locationListeners.remove( name );

    if( locationListeners.size() == 0 )
      locationManager.removeUpdates( this );

    saveLocationListeners();
  }

  public static void setLocationTrackerParameters( int minTime, int minDistance, int acceptedDistanceAfterReboot )
  {
    setLocationTrackerParameters( minTime, minDistance, acceptedDistanceAfterReboot, new Criteria() );
  }

  public static void setLocationTrackerParameters( int minTime, int minDistance, int acceptedDistanceAfterReboot,
                                                   Criteria criteria )
  {
    LocationTracker.minTime = minTime;
    LocationTracker.minDistance = minDistance;
    LocationTracker.ACCEPTABLE_DISTANCE = acceptedDistanceAfterReboot;
    LocationTracker.criteria = criteria;

    if( getInstance() != null && !LocationTracker.getInstance().locationListeners.isEmpty() )
      LocationTracker.getInstance().listenBestProvider();
  }

  private void init()
  {
    locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
    locationListeners = Collections.synchronizedMap( new HashMap<String, IBackendlessLocationListener>() );
    Backendless.Data.mapTableToClass( Location.class.getSimpleName(), Location.class );
    Backendless.Data.mapTableToClass( GeoFenceMonitoring.class.getSimpleName(), GeoFenceMonitoring.class );
  }

  private void listenBestProvider()
  {
    String bestProvider = locationManager.getBestProvider( criteria != null ? criteria : new Criteria(), true );

    if( bestProvider == null )
      throw new BackendlessException( ExceptionMessage.NOT_FOUND_PROVIDER );

    listenProvider( bestProvider );
  }

  private void listenProvider( String provider )
  {
    this.provider = provider;
    locationManager.removeUpdates( this );
    locationManager.requestLocationUpdates( this.provider, minTime, minDistance, this );
  }

  private void firstListen( IBackendlessLocationListener locationListener )
  {
    Location location = null;

    try
    {
      location = locationManager.getLastKnownLocation( provider );
    }
    catch( Exception ex )
    {
    }

    if( location != null )
      locationListener.onLocationChanged( location );
  }

  private void locationChanged( Location location )
  {
    for( String name : locationListeners.keySet() )
      locationListeners.get( name ).onLocationChanged( location );

    saveLocationListeners();
  }

  private void initLocationListeners()
  {
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences( getApplicationContext() );
    String locationListenersStr = sharedPref.getString( LOCATION_LISTENERS, null );

    if( locationListenersStr != null )
    {
      try
      {
        Map<String, LocationListenerInfo> serializedListeners = (Map<String, LocationListenerInfo>) Serializer.fromBytes( Base64.decode( locationListenersStr, Base64.DEFAULT ), ISerializer.AMF3, false );

        if( serializedListeners != null )
        {
          Iterator<String> iterator = serializedListeners.keySet().iterator();

          while( iterator.hasNext() )
          {
            String name = iterator.next();
            LocationListenerInfo listenerInfo = serializedListeners.get( name );
            IBackendlessLocationListener listener = listenerInfo.getBackendlessListener();

            if( listener != null )
            {
              if( locationListeners == null )
                locationListeners = new HashMap<String, IBackendlessLocationListener>();

              locationListeners.put( name, listener );
            }

          }
        }
      } catch( IOException e )
      {
        Log.e( "Cannot get location listeners", e.getMessage() );
      }
    }
  }

  private void saveLocationListeners()
  {
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences( getApplicationContext() );
    SharedPreferences.Editor editor = sharedPref.edit();

    try
    {
      HashMap<String, LocationListenerInfo> modifiedListeners = getLabeledLocationListeners();
      editor.putString( LOCATION_LISTENERS, Base64.encodeToString( Serializer.toBytes( modifiedListeners, ISerializer.AMF3 ), Base64.DEFAULT ) );
    }
    catch( Throwable e )
    {
      Log.e( "Cannot save location listeners", e.getMessage() );
    }
    editor.apply();

    super.onDestroy();
  }

  private HashMap<String, LocationListenerInfo> getLabeledLocationListeners() throws Exception
  {
    Iterator<String> iterator = locationListeners.keySet().iterator();
    HashMap<String, LocationListenerInfo> labeledListeners = new HashMap<String, LocationListenerInfo>();

    while( iterator.hasNext() )
    {
      String name = iterator.next();
      IBackendlessLocationListener listener = locationListeners.get( name );
      labeledListeners.put( name, new LocationListenerInfo( listener ) );
    }

    return labeledListeners;
  }

  private void changeLocation()
  {
    Location oldLocation = getSavedLocation();

    if( provider == null )
      listenBestProvider();

    Location location = locationManager.getLastKnownLocation( provider );

    if( oldLocation != null )
      for( String name : locationListeners.keySet() )
        locationListeners.get( name ).onLocationChanged( oldLocation, location );
  }

  private Location getSavedLocation()
  {
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences( getApplicationContext() );
//    String locationStr = sharedPref.getString( LOCATION, null );
    String locationLatitudeString = sharedPref.getString( LOCATION_LATITUDE_TAG, null );
    String locationLongitudeString = sharedPref.getString( LOCATION_LONGITUDE_TAG, null );
    Location savedLocation = new Location( provider );

//    if( locationStr == null )
//    {
//      return null;
//    }

    if( locationLatitudeString == null || locationLongitudeString == null )
      return null;

    try
    {
      savedLocation.setLatitude( (Double) Serializer.fromBytes( Base64.decode( locationLatitudeString, Base64.DEFAULT ), ISerializer.AMF3, false ) );
      savedLocation.setLongitude( (Double) Serializer.fromBytes( Base64.decode( locationLongitudeString, Base64.DEFAULT ), ISerializer.AMF3, false ) );
      return savedLocation;
//      return (Location) Serializer.fromBytes( Base64.decode( locationStr, Base64.DEFAULT ), ISerializer.AMF3, false );
    }
    catch( IOException e )
    {
      Log.e( "Cannot get location", e.getMessage() );
      return null;
    }
  }

  private void saveLocation( Location location )
  {
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences( getApplicationContext() );
    SharedPreferences.Editor editor = sharedPref.edit();
    try
    {
      editor.putString( LOCATION_LATITUDE_TAG, Base64.encodeToString( Serializer.toBytes( location.getLatitude(), ISerializer.AMF3 ), Base64.DEFAULT ) );
      editor.putString( LOCATION_LONGITUDE_TAG, Base64.encodeToString( Serializer.toBytes( location.getLongitude(), ISerializer.AMF3 ), Base64.DEFAULT ) );
//      editor.putString( LOCATION, Base64.encodeToString( Serializer.toBytes(location, ISerializer.AMF3), Base64.DEFAULT ) );
    }
    catch( Exception e )
    {
      Log.e( "Cannot save location", e.getMessage() );
    }

    editor.apply();
  }
}
