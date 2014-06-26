

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

package com.backendless.media.lib.video;

import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;

import java.util.Iterator;
import java.util.List;

/**
 * A class that represents the quality of a video stream.
 * It contains the resolution, the framerate (in fps) and the bitrate (in bps) of the stream.
 */
public class VideoQuality
{

  public final static String TAG = "VideoQuality";

  /**
   * Default video stream quality.
   */
  public final static VideoQuality DEFAULT_VIDEO_QUALITY = new VideoQuality( 176, 144, 20, 500000 );  //Change this parameter

  /**
   * Represents a quality for a video stream.
   */
  public VideoQuality()
  {
  }

  /**
   * Represents a quality for a video stream.
   *
   * @param resX The horizontal resolution
   * @param resY The vertical resolution
   */
  public VideoQuality( int resX, int resY )
  {
    this.resX = resX;
    this.resY = resY;
  }

  /**
   * Represents a quality for a video stream.
   *
   * @param resX      The horizontal resolution
   * @param resY      The vertical resolution
   * @param framerate The framerate in frame per seconds
   * @param bitrate   The bitrate in bit per seconds
   */
  public VideoQuality( int resX, int resY, int framerate, int bitrate )
  {
    this.framerate = framerate;
    this.bitrate = bitrate;
    this.resX = resX;
    this.resY = resY;
  }

  public int framerate = 0;
  public int bitrate = 0;
  public int resX = 0;
  public int resY = 0;

  public boolean equals( VideoQuality quality )
  {
    if( quality == null )
      return false;
    return (quality.resX == this.resX &
            quality.resY == this.resY &
            quality.framerate == this.framerate &
            quality.bitrate == this.bitrate);
  }

  public VideoQuality clone()
  {
    return new VideoQuality( resX, resY, framerate, bitrate );
  }

  public static VideoQuality parseQuality( String str )
  {
    VideoQuality quality = DEFAULT_VIDEO_QUALITY.clone();
    if( str != null )
    {
      String[] config = str.split( "-" );
      try
      {
        quality.bitrate = Integer.parseInt( config[ 0 ] ) * 1000; // conversion to bit/s
        quality.framerate = Integer.parseInt( config[ 1 ] );
        quality.resX = Integer.parseInt( config[ 2 ] );
        quality.resY = Integer.parseInt( config[ 3 ] );
      }
      catch( IndexOutOfBoundsException ignore )
      {
      }
    }
    return quality;
  }

  /**
   * Checks if the requested resolution is supported by the camera.
   * If not, it modifies it by supported parameters.
   */
  public static VideoQuality determineClosestSupportedResolution( Camera.Parameters parameters, VideoQuality quality )
  {
    VideoQuality v = quality.clone();
    int minDist = Integer.MAX_VALUE;
    String supportedSizesStr = "Supported resolutions: ";
    List<Size> supportedSizes = parameters.getSupportedPreviewSizes();
    for( Iterator<Size> it = supportedSizes.iterator(); it.hasNext(); )
    {
      Size size = it.next();
      supportedSizesStr += size.width + "x" + size.height + (it.hasNext() ? ", " : "");
      int dist = Math.abs( quality.resX - size.width );
      if( dist < minDist )
      {
        minDist = dist;
        v.resX = size.width;
        v.resY = size.height;
      }
    }
    Log.v( TAG, supportedSizesStr );
    if( quality.resX != v.resX || quality.resY != v.resY )
    {
      Log.v( TAG, "Resolution modified: " + quality.resX + "x" + quality.resY + "->" + v.resX + "x" + v.resY );
    }

    return v;
  }

  public static int[] determineMaximumSupportedFramerate( Camera.Parameters parameters )
  {
    int[] maxFps = new int[] { 0, 0 };
    String supportedFpsRangesStr = "Supported frame rates: ";
    List<int[]> supportedFpsRanges = parameters.getSupportedPreviewFpsRange();
    for( Iterator<int[]> it = supportedFpsRanges.iterator(); it.hasNext(); )
    {
      int[] interval = it.next();
      supportedFpsRangesStr += interval[ 0 ] / 1000 + "-" + interval[ 1 ] / 1000 + "fps" + (it.hasNext() ? ", " : "");
      if( interval[ 1 ] > maxFps[ 1 ] || (interval[ 0 ] > maxFps[ 0 ] && interval[ 1 ] == maxFps[ 1 ]) )
      {
        maxFps = interval;
      }
    }
    Log.v( TAG, supportedFpsRangesStr );
    return maxFps;
  }
}
