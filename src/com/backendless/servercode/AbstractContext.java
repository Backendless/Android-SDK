package com.backendless.servercode;

import com.backendless.commons.DeviceType;

import java.util.List;

public abstract class AbstractContext
{
  protected String appId;
  protected String userId;
  protected String userToken;
  protected List<String> userRoles;
  protected DeviceType deviceType;

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
