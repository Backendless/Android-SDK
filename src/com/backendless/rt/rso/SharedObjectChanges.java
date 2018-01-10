package com.backendless.rt.rso;

import com.backendless.rt.users.UserInfo;

public class SharedObjectChanges
{
  private String key;
  private Object value;
  private UserInfo userInfo;

  public String getKey()
  {
    return key;
  }

  public SharedObjectChanges setKey( String key )
  {
    this.key = key;
    return this;
  }

  public Object getValue()
  {
    return value;
  }

  public SharedObjectChanges setValue( Object value )
  {
    this.value = value;
    return this;
  }

  public UserInfo getUserInfo()
  {
    return userInfo;
  }

  public SharedObjectChanges setUserInfo( UserInfo userInfo )
  {
    this.userInfo = userInfo;
    return this;
  }

  @Override
  public String toString()
  {
    return "SharedObjectChanges{" + "key='" + key + '\'' + ", value=" + value + ", userInfo=" + userInfo + '}';
  }
}
