package com.backendless.rt.command;

import com.backendless.rt.users.UserInfo;

import java.util.Map;

public class Command<T>
{

  public static <T> Command<T> of( Class<T> dataType )
  {
    return new Command<>( dataType );
  }

  public static Command<String> string(  )
  {
    return new Command<>( String.class );
  }

  public static Command<Map> map(  )
  {
    return new Command<>( Map.class );
  }

  private Command( Class<T> dataType )
  {
    this.dataType = dataType;
  }

  private final transient Class<T> dataType;
  private String type;
  private T data;
  private UserInfo userInfo;

  public String getType()
  {
    return type;
  }

  public Command setType( String type )
  {
    this.type = type;
    return this;
  }

  public T getData()
  {
    return data;
  }

  public Command setData( T data )
  {
    this.data = data;
    return this;
  }

  public UserInfo getUserInfo()
  {
    return userInfo;
  }

  public Command setUserInfo( UserInfo userInfo )
  {
    this.userInfo = userInfo;
    return this;
  }

  @Override
  public String toString()
  {
    return "RTCommand{" + "dataType=" + dataType + ", type='" + type + '\'' + ", data=" + data + ", userInfo=" + userInfo + '}';
  }
}
