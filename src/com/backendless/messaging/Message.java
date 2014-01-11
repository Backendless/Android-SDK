/*
 * ********************************************************************************************************************
 *  <p/>
 *  BACKENDLESS.COM CONFIDENTIAL
 *  <p/>
 *  ********************************************************************************************************************
 *  <p/>
 *  Copyright 2012 BACKENDLESS.COM. All Rights Reserved.
 *  <p/>
 *  NOTICE: All information contained herein is, and remains the property of Backendless.com and its suppliers,
 *  if any. The intellectual and technical concepts contained herein are proprietary to Backendless.com and its
 *  suppliers and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret
 *  or copyright law. Dissemination of this information or reproduction of this material is strictly forbidden
 *  unless prior written permission is obtained from Backendless.com.
 *  <p/>
 *  ********************************************************************************************************************
 */

package com.backendless.messaging;

import java.util.HashMap;
import java.util.Map;

public class Message
{
  private String messageId;
  private Map<String, String> headers;
  private Object data;
  private String publisherId;
  private long timestamp;

  public String getMessageId()
  {
    return messageId;
  }

  public Map<String, String> getHeaders()
  {
    if( headers == null )
      return headers = new HashMap<String, String>();

    return new HashMap<String, String>( headers );
  }

  public Object getData()
  {
    return data;
  }

  public String getPublisherId()
  {
    return publisherId;
  }

  public long getTimestamp()
  {
    return timestamp;
  }

  public void setMessageId( String messageId )
  {
    this.messageId = messageId;
  }

  public void setHeaders( Map<String, String> headers )
  {
    this.headers = headers;
  }

  public void setData( Object data )
  {
    this.data = data;
  }

  public void setPublisherId( String publisherId )
  {
    this.publisherId = publisherId;
  }

  public void setTimestamp( long timestamp )
  {
    this.timestamp = timestamp;
  }

  @Override
  public boolean equals( Object o )
  {
    if( this == o )
      return true;
    if( o == null || getClass() != o.getClass() )
      return false;

    Message message = (Message) o;

    if( timestamp != message.timestamp )
      return false;
    if( data != null ? !data.equals( message.data ) : message.data != null )
      return false;
    if( headers != null ? !headers.equals( message.headers ) : message.headers != null )
      return false;
    if( messageId != null ? !messageId.equals( message.messageId ) : message.messageId != null )
      return false;
    if( publisherId != null ? !publisherId.equals( message.publisherId ) : message.publisherId != null )
      return false;

    return true;
  }

  @Override
  public int hashCode()
  {
    int result = messageId != null ? messageId.hashCode() : 0;
    result = 31 * result + (headers != null ? headers.hashCode() : 0);
    result = 31 * result + (data != null ? data.hashCode() : 0);
    result = 31 * result + (publisherId != null ? publisherId.hashCode() : 0);
    result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
    return result;
  }

  @Override
  public String toString()
  {
    final StringBuffer sb = new StringBuffer();
    sb.append( "Message" );
    sb.append( "{messageId='" ).append( messageId ).append( '\'' );
    sb.append( ", data=" ).append( data );
    sb.append( ", headers={" );

    int i = 0;
    for( Map.Entry<String, String> entry : headers.entrySet() )
    {
      sb.append( entry.getKey() ).append( ":" ).append( entry.getValue() );

      if( ++i != headers.size() )
        sb.append( ", " );
    }

    sb.append( "}, publisherId='" ).append( publisherId ).append( '\'' );
    sb.append( ", timestamp=" ).append( timestamp );
    sb.append( '}' );
    return sb.toString();
  }
}