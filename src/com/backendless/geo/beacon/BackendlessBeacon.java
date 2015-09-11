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

import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by baas on 02.09.15.
 */

public class BackendlessBeacon
{
  protected String objectId;
  protected String bluetoothAddress;
  protected BeaconType type;
  protected Map<String, String> iBeaconProps;
  protected Map<String, String> eddystoneProps;

  public BackendlessBeacon()
  {
  }

  public BackendlessBeacon( BeaconType type, org.altbeacon.beacon.Beacon beacon )
  {
    this.type = type;
    bluetoothAddress = beacon.getBluetoothAddress();

    switch( type )
    {
      case IBEACON:
        iBeaconProps = createIbeaconProps( beacon.getId1().toString(), beacon.getId2().toString(), beacon.getId3().toString() );
        break;

      case EDDYSTONE:
        int intAmount = beacon.getIdentifiers().size();
        if( intAmount > 1 )
        {
          eddystoneProps = createEddystoneProps( beacon.getId1().toString(), beacon.getId2().toString() );
        }
        else
        {
          eddystoneProps = createEddystoneProps( UrlBeaconUrlCompressor.uncompress( beacon.getId1().toByteArray() ), null );
        }

        if( !beacon.getExtraDataFields().isEmpty() )
        {
          eddystoneProps.putAll( createEddystoneExtraProps( beacon.getExtraDataFields().get( 0 ).toString(), beacon.getExtraDataFields().get( 1 ).toString(), beacon.getExtraDataFields().get( 2 ).toString(), beacon.getExtraDataFields().get( 3 ).toString(), String.valueOf( (beacon.getExtraDataFields().get( 4 ) / 10) ) ) );
        }

        break;
    }
  }

  private Map<String, String> createIbeaconProps( String uuid, String majorVersion, String minorVersion )
  {
    Map<String, String> properties = new HashMap<String, String>( 3 );
    properties.put( BeaconConstants.IBEACON_UUID_STR, uuid );
    properties.put( BeaconConstants.IBEACON_MAJOR_STR, majorVersion );
    properties.put( BeaconConstants.IBEACON_MINOR_STR, minorVersion );

    return properties;
  }

  private Map<String, String> createEddystoneProps( String namespaceId, String instanceId )
  {
    Map<String, String> properties = new HashMap<String, String>( 2 );
    properties.put( BeaconConstants.EDDYSTONE_NAMESPACE_ID_STR, namespaceId );
    properties.put( BeaconConstants.EDDYSTONE_INSTANCE_ID_STR, instanceId );

    return properties;
  }

  private Map<String, String> createEddystoneExtraProps( String telemetry, String batteryMil, String temperatureCels,
                                                         String PduCount, String upTime )
  {
    Map<String, String> properties = new HashMap<String, String>( 5 );
    properties.put( BeaconConstants.EDDYSTONE_TELEMETRY_VERSION_STR, telemetry ); // telemetry Version
    properties.put( BeaconConstants.EDDYSTONE_BATTERY_STR, batteryMil ); // battery MilliVolts
    properties.put( BeaconConstants.EDDYSTONE_TEMPERATURE_STR, temperatureCels ); // temperature Celsius
    properties.put( BeaconConstants.EDDYSTONE_PDU_COUNT_STR, PduCount ); //PDU cont
    properties.put( BeaconConstants.EDDYSTONE_UPTIME_STR, upTime ); // up time

    return properties;
  }

  public String getBluetoothAddress()
  {
    return bluetoothAddress;
  }

  public BeaconType getType()
  {
    return type;
  }

  public Map<String, String> getiBeaconProps()
  {
    return iBeaconProps;
  }

  public Map<String, String> getEddystoneProps()
  {
    return eddystoneProps;
  }

  public String getObjectId()
  {
    return objectId;
  }

  @Override
  public boolean equals( Object o )
  {
    if( this == o )
      return true;
    if( o == null || getClass() != o.getClass() )
      return false;

    BackendlessBeacon that = (BackendlessBeacon) o;

    if( !bluetoothAddress.equals( that.bluetoothAddress ) )
      return false;

    return true;
  }

  @Override
  public int hashCode()
  {
    return bluetoothAddress.hashCode();
  }

  @Override
  public String toString()
  {
    return "BackendlessBeacon{" +
            "objectId='" + objectId + '\'' +
            ", bluetoothAddress='" + bluetoothAddress + '\'' +
            ", type=" + type +
            (type == BeaconType.IBEACON ? ", iBeaconProps=" + iBeaconProps : ", eddystoneProps=" + eddystoneProps) +
            '}';
  }
}
