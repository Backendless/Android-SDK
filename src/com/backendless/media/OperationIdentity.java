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

package com.backendless.media;

import com.backendless.commons.media.OperationMeta;

public class OperationIdentity
{

  String appId;
  String version;
  String identity;
  String operation;
  OperationMeta meta;

  public OperationIdentity()
  {

  }

  public OperationIdentity( String appId, String version, String identity, String tube, String operation,
                            String streamType )
  {
    super();
    this.appId = appId;
    this.version = version;
    this.identity = identity;
    getMeta().tube = tube;
    getMeta().streamType = streamType;

    this.operation = operation;
  }

  public String getAppId()
  {
    return appId;
  }

  public void setAppId( String appId )
  {
    this.appId = appId;
  }

  public String getVersion()
  {
    return version;
  }

  public void setVersion( String version )
  {
    this.version = version;
  }

  public String getIdentity()
  {
    return identity;
  }

  public void setIdentity( String identity )
  {
    this.identity = identity;
  }

  public String getOperation()
  {
    return operation;
  }

  public void setOperation( String operation )
  {
    this.operation = operation;
  }

  public OperationMeta getMeta()
  {
    if( meta == null )
      meta = new OperationMeta();
    return meta;
  }

  public void setMeta( OperationMeta meta )
  {
    this.meta = meta;
  }

//  public static OperationIdentity extractIdentifier( IMediaStream stream )
//  {
//    IClient client = stream.getClient();
//    OperationIdentity identifier = (OperationIdentity) client.getProperties().get( Media.IDENTIFIER );
//    return identifier;
//  }

  public String getStreamName()
  {
    return getMeta().getMediaObjectInfo().streamName;
  }

  public void setStreamName( String streamName )
  {
    getMeta().getMediaObjectInfo().streamName = streamName;
  }

  @Override
  public String toString()
  {
    return " Identity information = AppId :" + appId + " version: " + version + " identity: " + identity + " operation: " + operation;
  }
}
