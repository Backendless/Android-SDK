package com.backendless;

import java.util.Map;


public class RegistrationStatus
{
  private String deviceToken;
  private Map<String, String> channelRegistrations;

  public String getDeviceToken()
  {
    return deviceToken;
  }

  public void setDeviceToken( String deviceToken )
  {
    this.deviceToken = deviceToken;
  }

  public Map<String, String> getChannelRegistrations()
  {
    return channelRegistrations;
  }

  public void setChannelRegistrations( Map<String, String> channelRegistrations )
  {
    this.channelRegistrations = channelRegistrations;
  }
}
