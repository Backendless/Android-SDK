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

package com.backendless.fencedemo;

import android.util.Log;
import com.backendless.geo.geofence.IGeofenceCallback;

public class CustomCallBack implements IGeofenceCallback
{

  @Override
  public void geoPointEntered(String geofenceName, String geofenceId, double latitude, double longitude) {
      Log.i("GeoFence", "Point enters in fence: " + geofenceName);
  }

  @Override
  public void geoPointStayed(String geofenceName, String geofenceId, double latitude, double longitude) {
      Log.i("GeoFence", "Point stay in fence: " + geofenceName);
  }

  @Override
  public void geoPointExited(String geofenceName, String geofenceId, double latitude, double longitude) {
      Log.i("GeoFence", "Point exit from fence: " + geofenceName);
  }
}
