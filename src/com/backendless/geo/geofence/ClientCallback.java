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

package com.backendless.geo.geofence;

import android.location.Location;

/**
 * Created by baas on 03.04.15.
 */
public class ClientCallback implements ICallback
{
  private IGeofenceCallback geofenceCallback;

  public ClientCallback()
  {
  }

  public ClientCallback( IGeofenceCallback geofenceCallback )
  {
    this.geofenceCallback = geofenceCallback;
  }

  @Override
  public void callOnEnter( GeoFence geoFence, Location location )
  {
    geofenceCallback.geoPointEntered( geoFence.getGeofenceName(), geoFence.getObjectId(), location.getLatitude(), location.getLongitude() );
  }

  @Override
  public void callOnStay( GeoFence geoFence, Location location )
  {
    geofenceCallback.geoPointStayed( geoFence.getGeofenceName(), geoFence.getObjectId(), location.getLatitude(), location.getLongitude() );
  }

  @Override
  public void callOnExit( GeoFence geoFence, Location location )
  {
    geofenceCallback.geoPointExited( geoFence.getGeofenceName(), geoFence.getObjectId(), location.getLatitude(), location.getLongitude() );
  }

  @Override
  public boolean equalCallbackParameter( Object object )
  {
    if(object.getClass() != geofenceCallback.getClass())
      return false;
    return this.geofenceCallback.equals( object );
  }

  public IGeofenceCallback getGeofenceCallback() {
    return geofenceCallback;
  }

  public void setGeofenceCallback( IGeofenceCallback geofenceCallback ) {
    this.geofenceCallback = geofenceCallback;
  }
}
