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

package com.backendless.media.lib.audio;

/**
 * A class that represents the quality of an audio stream.
 */
public class AudioQuality
{

  /**
   * Default audio stream quality.
   */
  public final static AudioQuality DEFAULT_AUDIO_QUALITY = new AudioQuality( 8000, 32000 );

  /**
   * Represents a quality for a video stream.
   */
  public AudioQuality()
  {
  }

  /**
   * Represents a quality for an audio stream.
   *
   * @param samplingRate The sampling rate
   * @param bitRate      The bitrate in bit per seconds
   */
  public AudioQuality( int samplingRate, int bitRate )
  {
    this.samplingRate = samplingRate;
    this.bitRate = bitRate;
  }

  public int samplingRate = 0;
  public int bitRate = 0;

  public boolean equals( AudioQuality quality )
  {
    if( quality == null )
      return false;
    return (quality.samplingRate == this.samplingRate & quality.bitRate == this.bitRate);
  }

  public AudioQuality clone()
  {
    return new AudioQuality( samplingRate, bitRate );
  }

  public static AudioQuality parseQuality( String str )
  {
    AudioQuality quality = DEFAULT_AUDIO_QUALITY.clone();
    if( str != null )
    {
      String[] config = str.split( "-" );
      try
      {
        quality.bitRate = Integer.parseInt( config[ 0 ] ) * 1000; // conversion to bit/s
        quality.samplingRate = Integer.parseInt( config[ 1 ] );
      }
      catch( IndexOutOfBoundsException ignore )
      {
      }
    }
    return quality;
  }
}
