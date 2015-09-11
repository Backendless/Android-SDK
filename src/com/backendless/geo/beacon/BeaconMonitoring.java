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

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.utils.ScheduledExecutor;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by baas on 02.09.15.
 */
public class BeaconMonitoring extends ScheduledExecutor implements IPresenceListener
{
  // locks for monitoredBeacons set
  private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
  private final Lock readLock = readWriteLock.readLock();
  private final Lock writeLock = readWriteLock.writeLock();

  private Set<BackendlessBeacon> monitoredBeacons = new HashSet<BackendlessBeacon>();
  private volatile boolean discovery = false;
  private Set<BackendlessBeacon> discoveryBeacons = Collections.synchronizedSet( new HashSet<BackendlessBeacon>() );

  public BeaconMonitoring( boolean runDiscovery, int timeFrequency )
  {
    this(runDiscovery, timeFrequency, null );
  }

  public BeaconMonitoring( boolean runDiscovery, int timeFrequency, Set<BackendlessBeacon> monitoredBeacons )
  {
    if(monitoredBeacons == null)
      receiveBeaconsInfo();

    this.discovery = runDiscovery;
    super.setTimeFrequency( timeFrequency );
  }

  @Override
  public void onDetectedBeacons( Map<BackendlessBeacon, Double> beaconToDistances )
  {
    for( BackendlessBeacon beacon : beaconToDistances.keySet() )
    {
      readLock.lock();
      boolean enubledBeacon = monitoredBeacons.contains( beacon );
      readLock.unlock();

      if( enubledBeacon )
      {
        sendEntered( beacon, beaconToDistances.get( beacon ) );
      }

      if( discovery )
      {
        discoveryBeacons.add( beacon );
      }
    }
  }


  @Override
  protected void calculate()
  {
    if( !discoveryBeacons.isEmpty() )
    {
      sendBeacons( new ArrayList<BackendlessBeacon>( discoveryBeacons ) );
      discoveryBeacons.clear();
    }

    receiveBeaconsInfo();
  }

  public void sendBeacons( List<BackendlessBeacon> discoveredBeacons )
  {
    Backendless.CustomService.invoke( BeaconConstants.SERVICE_NAME, BeaconConstants.SERVICE_VERSION, "beacons", new Object[] { discoveredBeacons }, (AsyncCallback) null );
  }

  public void receiveBeaconsInfo()
  {
    Backendless.CustomService.invoke( BeaconConstants.SERVICE_NAME, BeaconConstants.SERVICE_VERSION, "getenabled", new Object[] { }, BeaconsInfo.class, new AsyncCallback<BeaconsInfo>()
    {
      @Override
      public void handleResponse( BeaconsInfo response )
      {
        writeLock.lock();
        monitoredBeacons = response.getBeacons();
        writeLock.unlock();

        discovery = response.isDiscovery();
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        Backendless.Logging.getLogger( BeaconTracker.class ).error( fault.getCode() + " : " + fault.getMessage() );
      }
    } );
  }

  public void sendEntered( BackendlessBeacon beacon, double distance )
  {
    Backendless.CustomService.invoke( BeaconConstants.SERVICE_NAME, BeaconConstants.SERVICE_VERSION, "proximity", new Object[] { beacon.getObjectId(), distance }, (AsyncCallback) null );
  }
}
