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

public enum StreamVideoQuality
{
  LOW_170("176x144, 30 fps, 170 Kbps"), LOW_200("176x144, 30 fps, 200 Kbps"), LOW_250("176x144, 30 fps, 250 Kbps"), MEDIUM_250(
      "320x240, 30 fps, 250 Kbps"), MEDIUM_300("352x288, 30 fps, 300 Kbps"), MEDIUM_400("352x288, 30 fps, 400 Kbps"), HIGH_600(
      "640x480, 30 fps, 600 Kbps");

  private String value = "";

  public static StreamVideoQuality getFromString( String quality )
  {
    if( quality == null )
    {
      return null;
    }
    for( StreamVideoQuality streamQuality : values() )
    {
      if( quality.equals( streamQuality.getValue() ) )
      {
        return streamQuality;
      }
    }
    return null;
  }

  private StreamVideoQuality(String value)
  {
    this.value = value;
  }

  public String getValue()
  {
    return value;
  }

}
