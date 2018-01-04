package com.backendless.rt.users;

public class UserInfo
{
  private String connectionId;
  private String userId;

  public String getConnectionId()
  {
    return connectionId;
  }

  public UserInfo setConnectionId( String connectionId )
  {
    this.connectionId = connectionId;
    return this;
  }

  public String getUserId()
  {
    return userId;
  }

  public UserInfo setUserId( String userId )
  {
    this.userId = userId;
    return this;
  }

  @Override
  public String toString()
  {
    return "UserInfo{" + "connectionId='" + connectionId + '\'' + ", userId='" + userId + '\'' + '}';
  }
}
