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
 * Created by baas on 07.09.15.
 */
public class BeaconConstants
{
  public final static String SERVICE_NAME = "beacons";
  public final static String SERVICE_VERSION = "1.0";

  public final static String DISCOVERY = "beacon-frequency";
  public final static String FREQUENCY = "beacon-discovery";
  public final static int DEFAULT_FREQUENCY = 5 * 60; // 5 minutes
  public final static boolean DEFUTL_DISCOVERY = true;

  public final static String IBEACON_UUID_STR = "uuid";
  public final static String IBEACON_MAJOR_STR = "majorVersion";
  public final static String IBEACON_MINOR_STR = "minorVersion";
  public final static String EDDYSTONE_NAMESPACE_ID_STR = "namespaceId";
  public final static String EDDYSTONE_INSTANCE_ID_STR = "instanceId";
  public final static String EDDYSTONE_TELEMETRY_VERSION_STR = "telemetryVersion";
  public final static String EDDYSTONE_BATTERY_STR = "batteryMilliVolts";
  public final static String EDDYSTONE_TEMPERATURE_STR = "temperatureCelsius";
  public final static String EDDYSTONE_PDU_COUNT_STR = "pduCount";
  public final static String EDDYSTONE_UPTIME_STR = "uptimeSeconds";
}
