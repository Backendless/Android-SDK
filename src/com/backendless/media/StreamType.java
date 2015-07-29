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

public enum StreamType
{
  AVAILABLE( 0 ), LIVE( 1 ), RECORDING( 2 ), LIVE_RECORDING( 3 ), NOT_AVAILABLE( 4 );
  private int value;

  private StreamType( int value )
  {
    this.value = value;
  }

  public int getValue()
  {
    return value;
  }

  public static StreamType convertWowzaStreamType( String streamType )
  {

    if( "live".equals( streamType ) )
      return LIVE;

    if( "live-record".equals( streamType ) )
      return LIVE_RECORDING;

    if( "play-live".equals( streamType ) )
      return RECORDING;

    if( "play-record".equals( streamType ) )
      return AVAILABLE;
    throw new UnsupportedOperationException();
  }
}