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
import com.backendless.Backendless;
import com.backendless.Geo;
import com.backendless.Invoker;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.geo.GeoPoint;

/**
 * Created by baas on 03.04.15.
 */
public class ServerCallback implements ICallback
{
  private GeoPoint geoPoint;

  public ServerCallback( GeoPoint geoPoint )
  {
    this.geoPoint = geoPoint;
  }

  @Override
  public void callOnEnter( GeoFence geoFence, Location location )
  {
    updatePoint( location );
    onGeofenceServerCallback( "onEnterGeofence", geoFence.getObjectId(), geoPoint );
  }

  @Override
  public void callOnStay( GeoFence geoFence, Location location )
  {
    updatePoint( location );
    onGeofenceServerCallback( "onStayGeofence", geoFence.getObjectId(), geoPoint );
  }

  @Override
  public void callOnExit( GeoFence geoFence, Location location )
  {
    updatePoint( location );
    onGeofenceServerCallback( "onExitGeofence", geoFence.getObjectId(), geoPoint );
  }

  @Override
  public boolean equalCallbackParameter( Object object )
  {
    if(object.getClass() != GeoPoint.class)
    return false;
    return this.geoPoint.getMetadata().equals( ((GeoPoint)object).getMetadata() ) && this.geoPoint.getCategories().equals( ((GeoPoint)object).getCategories() );
  }

  private void updatePoint( Location location )
  {
    geoPoint.setLatitude( location.getLatitude() );
    geoPoint.setLongitude( location.getLongitude() );
  }

  private void onGeofenceServerCallback( String method, String geofenceId, GeoPoint geoPoint )
  {
    Invoker.invokeAsync( Geo.GEO_MANAGER_SERVER_ALIAS, method, new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), geofenceId, geoPoint }, new AsyncCallback<Void>()
    {
      @Override
      public void handleResponse( Void v )
      {
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
      }
    } );
  }
}
