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

    public void startMonitoring( AsyncCallback<Void> responder )
    {
      startMonitoring( BeaconConstancts.DEFUTL_DISCOVERY, responder );
    }

    public void startMonitoring( boolean runDiscovery, AsyncCallback<Void> responder )
    {
      startMonitoring( runDiscovery, BeaconConstancts.DEFAULT_FREQUENCY, responder );
    }

    public void startMonitoring( boolean runDiscovery, int frequency, AsyncCallback<Void> responder )
    {
      beaconTracker.startMonitoring( runDiscovery, frequency, responder );
    }
}
