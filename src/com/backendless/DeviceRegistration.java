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

package com.backendless;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeviceRegistration
{
  protected String id = "";
  protected String deviceToken = "";
  protected String deviceId = "";
  protected String os;
  protected String osVersion;
  protected Date expiration;
  private List<String> channels = new ArrayList<String>();

  public String getId()
  {
    return id;
  }

  public synchronized void setId( String id )
  {
    this.id = id;
  }

  public String getDeviceToken()
  {
    return deviceToken;
  }

  public synchronized void setDeviceToken( String deviceToken )
  {
    this.deviceToken = deviceToken;
  }

  public String getDeviceId()
  {
    return deviceId;
  }

  public synchronized void setDeviceId( String deviceId )
  {
    this.deviceId = deviceId;
  }

  public String getOs()
  {
    return os;
  }

  public void setOs( String os )
  {
    this.os = os;
  }

  public String getOsVersion()
  {
    return osVersion;
  }

  public void setOsVersion( int osVersion )
  {
    this.osVersion = String.valueOf( osVersion );
  }

  public void setOsVersion( String osVersion )
  {
    this.osVersion = osVersion;
  }

  public Date getExpiration()
  {
    return expiration;
  }

  public void setExpiration( Date expiration )
  {
    this.expiration = expiration;
  }

  public List<String> getChannels()
  {
    if( channels == null )
      return channels = new ArrayList<String>();

    return channels;
  }

  public synchronized void setChannels( List<String> channels )
  {
    this.channels = channels;
  }

  public synchronized void addChannel( String channel )
  {
    if( channels == null )
      channels = new ArrayList<String>();

    channels.add( channel );
  }
}