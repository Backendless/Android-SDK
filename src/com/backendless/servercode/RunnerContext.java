package com.backendless.servercode;

import com.backendless.commons.DeviceType;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ivanlappo
 * Date: 8/19/13
 * Time: 12:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class RunnerContext
{
  private String appId;
  private String userId;
  private String userToken;
  private List<String> userRole;
  private DeviceType deviceType;
  private Map missingProperties;

  public String getAppId()
  {
    return appId;
  }

  public void setAppId( String appId )
  {
    this.appId = appId;
  }

  public String getUserId()
  {
    return userId;
  }

  public void setUserId( String userId )
  {
    this.userId = userId;
  }

  public String getUserToken()
  {
    return userToken;
  }

  public List<String> getUserRole()
  {
    return userRole;
  }

  public void setUserRole( List<String> userRole )
  {
    this.userRole = userRole;
  }

  public void setUserToken( String userToken )
  {
    this.userToken = userToken;
  }

  public DeviceType getDeviceType()
  {
    return deviceType;
  }

  public void setDeviceType( DeviceType deviceType )
  {
    this.deviceType = deviceType;
  }

  public Map getMissingProperties()
  {
    return missingProperties;
  }

  public void setMissingProperties( Map missingProperties )
  {
    this.missingProperties = missingProperties;
  }
}
