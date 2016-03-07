package com.backendless.servercode;

import com.backendless.commons.DeviceType;

import java.util.List;

public class AbstractContext
{
  protected String appId;
  protected String userId;
  protected String userToken;
  protected List<String> userRoles;
  protected DeviceType deviceType;

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

  public void setUserToken( String userToken )
  {
    this.userToken = userToken;
  }

  public List<String> getUserRoles()
  {
    return userRoles;
  }

  public void setUserRoles( List<String> userRoles )
  {
    this.userRoles = userRoles;
  }

  public DeviceType getDeviceType()
  {
    return deviceType;
  }

  public void setDeviceType( DeviceType deviceType )
  {
    this.deviceType = deviceType;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder( "AbstractContext{" );
    sb.append( "appId='" ).append( appId ).append( '\'' );
    sb.append( ", userId='" ).append( userId ).append( '\'' );
    sb.append( ", userToken='" ).append( userToken ).append( '\'' );
    sb.append( ", userRoles=" ).append( userRoles );
    sb.append( ", deviceType=" ).append( deviceType );
    sb.append( '}' );
    return sb.toString();
  }
}
