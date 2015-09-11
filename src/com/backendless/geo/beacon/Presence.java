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

import com.backendless.async.callback.AsyncCallback;

/**
 * Created by baas on 08.09.15.
 */
public class Presence
{
    BeaconTracker beaconTracker = new BeaconTracker();
  private static Presence instance = null;

  private Presence()
  {
  }

  public static Presence getInstance()
  {
    if(instance == null)
      instance = new Presence();
    return instance;
  }

  public void startMonitoring( AsyncCallback<Void> responder )
    {
      startMonitoring( BeaconConstants.DEFUTL_DISCOVERY, responder );
    }

    public void startMonitoring( boolean runDiscovery, AsyncCallback<Void> responder )
    {
      startMonitoring( runDiscovery, BeaconConstants.DEFAULT_FREQUENCY, responder );
    }

    public void startMonitoring( boolean runDiscovery, int frequency, AsyncCallback<Void> responder )
    {
      startMonitoring( runDiscovery, frequency, null, responder );
    }

  public void startMonitoring( boolean runDiscovery, int frequency, IPresenceListener listener, AsyncCallback<Void> responder )
  {
    startMonitoring( runDiscovery, frequency, listener, BeaconConstants.DEFAUTL_DISTANCE_CHANGE, responder );
  }

  public void startMonitoring( boolean runDiscovery, int frequency, IPresenceListener listener, double distanceChange, AsyncCallback<Void> responder )
  {
    beaconTracker.startMonitoring( runDiscovery, frequency, listener, distanceChange, responder );
  }

  public void stopMonitoring(  )
  {
    beaconTracker.stopMonitoring();
  }
}
