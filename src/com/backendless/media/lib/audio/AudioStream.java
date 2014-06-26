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

import android.media.MediaRecorder;
import android.util.Log;
import com.backendless.media.lib.MediaStream;

import java.io.IOException;

/**
 * Don't use this class directly.
 */
public abstract class AudioStream extends MediaStream
{

  protected int mAudioSource;
  protected int mOutputFormat;
  protected int mAudioEncoder;
  protected AudioQuality mRequestedQuality = AudioQuality.DEFAULT_AUDIO_QUALITY.clone();
  protected AudioQuality mQuality = mRequestedQuality.clone();

  public AudioStream()
  {
    setAudioSource( MediaRecorder.AudioSource.CAMCORDER );
  }

  public void setAudioSource( int audioSource )
  {
    mAudioSource = audioSource;
  }

  public void setAudioQuality( AudioQuality quality )
  {
    mRequestedQuality = quality;
  }

  /**
   * Returns the quality of the stream.
   */
  public AudioQuality getAudioQuality()
  {
    return mQuality;
  }

  protected void setAudioEncoder( int audioEncoder )
  {
    mAudioEncoder = audioEncoder;
  }

  protected void setOutputFormat( int outputFormat )
  {
    mOutputFormat = outputFormat;
  }

  @Override
  protected void encodeWithMediaRecorder() throws IOException
  {

    // We need a local socket to forward data output by the camera to the packetizer
    createSockets();

    Log.v( TAG, "Requested audio with " + mQuality.bitRate / 1000 + "kbps" + " at " + mQuality.samplingRate / 1000 + "kHz" );

    mMediaRecorder = new MediaRecorder();
    mMediaRecorder.setAudioSource( mAudioSource );
    mMediaRecorder.setOutputFormat( mOutputFormat );
    mMediaRecorder.setAudioEncoder( mAudioEncoder );
    mMediaRecorder.setAudioChannels( 1 );
    mMediaRecorder.setAudioSamplingRate( mQuality.samplingRate );
    mMediaRecorder.setAudioEncodingBitRate( mQuality.bitRate );

    // We write the ouput of the camera in a local socket instead of a file !
    // This one little trick makes backendless feasible quiet simply: data from the camera
    // can then be manipulated at the other end of the socket
    mMediaRecorder.setOutputFile( mSender.getFileDescriptor() );

    mMediaRecorder.prepare();
    mMediaRecorder.start();

    try
    {
      // mReceiver.getInputStream contains the data from the camera
      // the mPacketizer encapsulates this stream in an RTP stream and send it over the network
      mPacketizer.setInputStream( mReceiver.getInputStream() );
      mPacketizer.start();
      mStreaming = true;
    }
    catch( IOException e )
    {
      stop();
      throw new IOException( "Something happened with the local sockets :/ Start failed !" );
    }
  }
}
