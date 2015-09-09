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

import java.util.Set;

/**
 * Created by baas on 02.09.15.
 */
public class BeaconsInfo
{
  protected boolean discovery;
  protected Set<BackendlessBeacon> beacons;

  public BeaconsInfo()
  {
  }

  public BeaconsInfo( boolean discovery, Set<BackendlessBeacon> beacons )
  {
    this.discovery = discovery;
    this.beacons = beacons;
  }

  public boolean isDiscovery()
  {
    return discovery;
  }

  public Set<BackendlessBeacon> getBeacons()
  {
    return beacons;
  }
}
