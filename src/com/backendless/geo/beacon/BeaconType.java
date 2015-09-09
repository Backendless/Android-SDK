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

/**
 * Created by baas on 02.09.15.
 */
public enum BeaconType
{
  IBEACON(0x0215), EDDYSTONE(0xfeaa), UNKNOWN(-1);
  int serviceUUID;

  BeaconType( int serviceUUID )
  {
    this.serviceUUID = serviceUUID;
  }

  public static BeaconType ofServiceUUID(int serviceUUID)
  {
    for( BeaconType beaconType : BeaconType.values() )
    {
      if(beaconType.serviceUUID == serviceUUID)
      {
        return beaconType;
      }
    }

    return UNKNOWN;
  }
}
