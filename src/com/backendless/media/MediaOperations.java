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

public enum MediaOperations
{
  ADD( "1", "Add Media" ),
  REMOVE( "2", "Remove Media" ),
  RECORD( "3", "Record Audio & Video" ),
  PLAY( "4", "Play recorded Audio & Video" );

  private String operationId;
  private String operationName;

  private MediaOperations( String operationId, String operationName )
  {
    this.operationId = operationId;
    this.operationName = operationName;
  }
}
