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

/**
 * A data structure which contains ID of the published message and the status of the publish operation.
 */
public class MessageStatus implements Comparable<MessageStatus>
{
  private String messageId;
  private PublishStatusEnum status;
  private String errorMessage;

  public MessageStatus()
  {
  }

  public MessageStatus( String messageId )
  {
    this.messageId = messageId;
  }

  public PublishStatusEnum getStatus()
  {
    return status;
  }

  public String getMessageId()
  {
    return messageId;
  }

  public void setStatus( PublishStatusEnum status )
  {
    this.status = status;
  }

  public void setMessageId( String messageId )
  {
    this.messageId = messageId;
  }

  @Override
  public boolean equals( Object o )
  {
    if( this == o )
      return true;
    if( o == null || getClass() != o.getClass() )
      return false;

    MessageStatus that = (MessageStatus) o;

    if( messageId != null ? !messageId.equals( that.messageId ) : that.messageId != null )
      return false;
    if( status != that.status )
      return false;

    return true;
  }

  @Override
  public int hashCode()
  {
    int result = messageId != null ? messageId.hashCode() : 0;
    result = 31 * result + status.hashCode();
    return result;
  }

  @Override
  public String toString()
  {
    return "MessageStatus{" +
            "messageId='" + messageId + '\'' +
            ", status=" + status +
            '}';
  }

  @Override
  public int compareTo( MessageStatus o )
  {
    if( this == o )
      return 0;

    MessageStatus arg = (MessageStatus) o;

    int statusDiff = this.status == null ? (arg.getStatus() == null ? 0 : -1) : this.status.compareTo( arg.getStatus() );
    if( statusDiff != 0 )
      return statusDiff;

    return this.messageId == null ? (arg.getMessageId() == null ? 0 : -1) : this.messageId.compareTo( arg.getMessageId() );
  }

  public String getErrorMessage()
  {
    return errorMessage;
  }

  public void setErrorMessage( String errorMessage )
  {
    this.errorMessage = errorMessage;
  }
}
