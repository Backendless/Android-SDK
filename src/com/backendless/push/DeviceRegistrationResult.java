package com.backendless.push;

import java.util.Map;


public class DeviceRegistrationResult
{
  private String deviceToken;
  private Map<String, String> channelRegistrations;

  /**
   * The device token that device receives after registration on Google FCM.
   *
   * @return
   */
  public String getDeviceToken()
  {
    return deviceToken;
  }

  /**
   * <p>The map, where <i>key</i> is a <b>channel name</b> and <i>value</i> is a <b>device registration id</b> (table DeviceRegistrations).
   * <p>It is received after successful registration on Backendless server.
   *
   * @return
   */
  public Map<String, String> getChannelRegistrations()
  {
    return channelRegistrations;
  }

  DeviceRegistrationResult setDeviceToken( String deviceToken )
  {
    this.deviceToken = deviceToken;
    return this;
  }

  DeviceRegistrationResult setChannelRegistrations( Map<String, String> channelRegistrations )
  {
    this.channelRegistrations = channelRegistrations;
    return this;
  }

  @Override
  public String toString()
  {
    return "DeviceRegistrationResult{" + "deviceToken='" + deviceToken + '\'' + ", channelRegistrations=" + channelRegistrations + '}';
  }
}
