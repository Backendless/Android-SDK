package com.backendless.servercode;

import com.backendless.commons.DeviceType;

import java.util.List;
import java.util.Map;

public class RunnerContext extends AbstractContext
{
  private Map missingProperties;
  private Object prematureResult;

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

  @Deprecated
  public List<String> getUserRole()
  {
    return this.userRoles;
  }

  @Deprecated
  public void setUserRole( List<String> userRole )
  {
    this.userRoles = userRole;
  }

  public Map getMissingProperties()
  {
    return missingProperties;
  }

  public void setMissingProperties( Map missingProperties )
  {
    this.missingProperties = missingProperties;
  }

  public Object getPrematureResult()
  {
    return prematureResult;
  }

  public void setPrematureResult( Object prematureResult )
  {
    this.prematureResult = prematureResult;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder( "RunnerContext{" );
    sb.append( "missingProperties=" ).append( missingProperties );
    sb.append( ", prematureResult=" ).append( prematureResult );
    sb.append( ", " ).append(super.toString());
    sb.append( "}" );
    return sb.toString();
  }
}
