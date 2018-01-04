package com.backendless.rt.users;

import java.util.Arrays;

public class UserStatusResponse
{
  private UserStatus status;
  private UserInfo[] data;

  public UserStatus getStatus()
  {
    return status;
  }

  public UserStatusResponse setStatus( UserStatus status )
  {
    this.status = status;
    return this;
  }

  public UserInfo[] getData()
  {
    return data;
  }

  public UserStatusResponse setData( UserInfo[] data )
  {
    this.data = data;
    return this;
  }

  @Override
  public String toString()
  {
    return "UserStatusResponse{" + "status=" + status + ", data=" + Arrays.toString( data ) + '}';
  }
}
