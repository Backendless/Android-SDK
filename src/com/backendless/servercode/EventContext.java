package com.backendless.servercode;

/**
 * Created by oleg on 3/31/16.
 */
public class EventContext
{
  private String tableName;
  private String channelName;
  private String directoryName;

  public String getTableName()
  {
    return tableName;
  }

  public void setTableName( String tableName )
  {
    this.tableName = tableName;
  }

  public String getChannelName()
  {
    return channelName;
  }

  public void setChannelName( String channelName )
  {
    this.channelName = channelName;
  }

  public String getDirectoryName()
  {
    return directoryName;
  }

  public void setDirectoryName( String directoryName )
  {
    this.directoryName = directoryName;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder( "EventContext{" );
    sb.append( "tableName='" ).append( tableName ).append( '\'' );
    sb.append( ", channelName='" ).append( channelName ).append( '\'' );
    sb.append( ", directoryName='" ).append( directoryName ).append( '\'' );
    sb.append( '}' );
    return sb.toString();
  }
}
