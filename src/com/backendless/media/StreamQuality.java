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

public enum StreamQuality
{
  LOW( "320x240, 30 fps, 250 Kbps" ), MEDIUM( "480x360, 30 fps, 300 Kbps" ), HIGH( "640x480, 30 fps, 600 Kbps" );

  /*
  * LOW - 176x144, 170 Kbps
  * MEDIUM - 352x288, 300 Kbps
  * HIGH - 640x480, 600 Kbps
  */
  private String value = "";

  private StreamQuality( String value )
  {
    this.value = value;
  }

  public String getValue()
  {
    return value;
  }

}
