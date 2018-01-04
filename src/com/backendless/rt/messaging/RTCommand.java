package com.backendless.rt.messaging;

import java.util.Map;

public class RTCommand<T>
{

  public static <T> RTCommand<T> of( Class<T> dataType )
  {
    return new RTCommand<>( dataType );
  }

  public static RTCommand<String> string(  )
  {
    return new RTCommand<>( String.class );
  }

  public static RTCommand<Map> map(  )
  {
    return new RTCommand<>( Map.class );
  }

  private RTCommand( Class<T> dataType )
  {
    this.dataType = dataType;
  }

  private final transient Class<T> dataType;
  private String type;
  private T data;
  private String connectionId;
  private String userId;

  public String getType()
  {
    return type;
  }

  public RTCommand setType( String type )
  {
    this.type = type;
    return this;
  }

  public T getData()
  {
    return data;
  }

  public RTCommand setData( T data )
  {
    this.data = data;
    return this;
  }

  public String getConnectionId()
  {
    return connectionId;
  }

  public RTCommand setConnectionId( String connectionId )
  {
    this.connectionId = connectionId;
    return this;
  }

  public String getUserId()
  {
    return userId;
  }

  public RTCommand setUserId( String userId )
  {
    this.userId = userId;
    return this;
  }

  @Override
  public String toString()
  {
    return "RTCommand{" + "dataType=" + dataType + ", type='" + type + '\'' + ", data=" + data + ", connectionId='" + connectionId + '\'' + ", userId='" + userId + '\'' + '}';
  }
}
