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
import com.backendless.geo.GeoPoint;

/**
 * Created by baas on 03.04.15.
 */
public class ServerState implements IState
{
  private GeoPoint geoPoint;

  public ServerState( GeoPoint geoPoint )
  {
    this.geoPoint = geoPoint;
  }

  @Override
  public void callOnEnter( GeoFence geoFence, Location location )
  {
    updatePoint( location );
  }

  @Override
  public void callOnStay( GeoFence geoFence, Location location )
  {
    updatePoint( location );
  }

  @Override
  public void callOnExit( GeoFence geoFence, Location location )
  {
    updatePoint( location );
  }

  private void updatePoint( Location location )
  {
    geoPoint.setLatitude( location.getLatitude() );
    geoPoint.setLongitude( location.getLongitude() );
  }

  public boolean equalGeoPoint( GeoPoint geoPoint )
  {
    return this.geoPoint.getMetadata().equals( geoPoint.getMetadata() ) && this.geoPoint.getCategories().equals( geoPoint.getCategories() );
  }
}
