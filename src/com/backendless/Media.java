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

package com.backendless;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;

import com.backendless.exceptions.BackendlessException;
import com.backendless.media.DisplayOrientation;
import com.backendless.media.Session;
import com.backendless.media.SessionBuilder;
import com.backendless.media.StreamProtocolType;
import com.backendless.media.StreamQuality;
import com.backendless.media.StreamType;
import com.backendless.media.audio.AudioQuality;
import com.backendless.media.gl.SurfaceView;
import com.backendless.media.rtsp.RtspClient;
import com.backendless.media.video.VideoQuality;

public final class Media
{
  private static final String HLS_PLAYLIST_CONSTANT = "/playlist.m3u8";
  private final static String WOWZA_SERVER_IP = "10.0.1.48"; // TODO change to
                                                             // media.backendless.com
  private final static String WOWZA_SERVER_LIVE_APP_NAME = "mediaAppLive";
  private final static String WOWZA_SERVER_VOD_APP_NAME = "mediaAppVod";
  private final static String RTSP_PROTOCOL = StreamProtocolType.RTSP.getValue();
  private final static String HLS_PROTOCOL = StreamProtocolType.HLS.getValue();
  private final static Integer WOWZA_SERVER_PORT = 1935;
  private RtspClient rtspClient;
  private Session session;
  private MediaPlayer mediaPlayer;
  private SurfaceView surfaceView;

  private static final Media instance = new Media();

  static Media getInstance()
  {
    return instance;
  }

  public void toggleFlash()
  {
    checkSessionIsNull();
    session.toggleFlash();
  }

  public VideoQuality getVideoQuality()
  {
    checkSessionIsNull();
    return session.getVideoTrack().getVideoQuality();
  }

  public void setVideoQuality( VideoQuality videoQuality )
  {
    checkSessionIsNull();
    session.setVideoQuality( videoQuality );
  }

  public void switchCamera()
  {
    checkSessionIsNull();
    session.switchCamera();
  }

  public void startPreview()
  {
    checkSessionIsNull();
    session.startPreview();
  }

  public void stopPreview()
  {
    checkSessionIsNull();
    session.stopPreview();
  }

  public int getCamera()
  {
    checkSessionIsNull();
    return session.getCamera();
  }

  public void stopClintStream()
  {
    checkRtspClientIsNull();
    rtspClient.stopStream();
  }

  public void releaseSession()
  {
    checkSessionIsNull();
    session.release();
  }

  public void releaseClint()
  {
    checkRtspClientIsNull();
    rtspClient.release();
  }

  public void stopPlaying()
  {
    checkPlayerIsNull();
    mediaPlayer.reset();
  }

  public void configure( Context context, SurfaceView mSurfaceView, DisplayOrientation orientation )
  {
    surfaceView = mSurfaceView;
    session = getSession( context, surfaceView, orientation.getValue() );
    rtspClient = getRtspClient( context, session );
    mediaPlayer = new MediaPlayer();
    mediaPlayer.setDisplay( surfaceView.getHolder() );
    mediaPlayer.setAudioStreamType( AudioManager.STREAM_MUSIC );
    mediaPlayer.setOnPreparedListener( new OnPreparedListener() {

      @Override
      public void onPrepared( MediaPlayer mp )
      {
        mp.start();
      }
    } );
  }

  public void publishRecordOrStop( String tube, String streamName )
  {
    publishStreamOrStop( tube, streamName, StreamType.LIVE_RECORDING );
  }

  public void publishLiveOrStop( String tube, String streamName )
  {
    publishStreamOrStop( tube, streamName, StreamType.LIVE );
  }

  public void playLiveOrStop( Context context, MediaPlayer mediaPlayer, StreamProtocolType streamProtocolType, String tube,
      String streamName ) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException
  {
    playStreamOrStop( streamProtocolType, tube, streamName, StreamType.RECORDING );
  }

  public void playRecordOrStop( Context context, MediaPlayer mediaPlayer, StreamProtocolType streamProtocolType, String tube,
      String streamName ) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException
  {
    playStreamOrStop( streamProtocolType, tube, streamName, StreamType.AVAILABLE );
  }

  private void publishStreamOrStop( String tube, String streamName, StreamType streamType )
  {
    checkSessionIsNull();
    checkRtspClientIsNull();
    if( streamName == null || streamName.isEmpty() )
    {
      streamName = "Default";
    }

    if( tube == null || tube.isEmpty() )
    {
      tube = "Default";
    }
    String operationType = getOperationType( streamType );
    String params = getConnectParams( tube, operationType, streamName );
    startOrStopStream( rtspClient, streamName, params );
  }

  private void checkPlayerIsNull()
  {
    if( mediaPlayer == null )
    {
      throw new BackendlessException( "Player client is null. Method configure( .. ) must be invoked" );
    }
  }

  private void checkSessionIsNull()
  {
    if( session == null )
    {
      throw new BackendlessException( "Session client is null. Method configure( .. ) must be invoked" );
    }
  }

  private void checkRtspClientIsNull()
  {
    if( rtspClient == null )
    {
      throw new BackendlessException( "Streaming client is null. Method configure( .. ) must be invoked" );
    }
  }

  private String getOperationType( StreamType streamType )
  {
    return ( streamType == StreamType.LIVE ) ? "publishLive" : "publishRecorded";
  }

  private void playStreamOrStop( StreamProtocolType streamProtocolType, String tube, String fileName, StreamType streamType )
      throws IllegalArgumentException, SecurityException, IllegalStateException, IOException
  {
    checkPlayerIsNull();
    if( mediaPlayer.isPlaying() )
    {
      mediaPlayer.stop();
      mediaPlayer.reset();
    }
    else
    {
      // session.stopPreview();
      mediaPlayer.reset();

      if( streamProtocolType == null )
      {
        streamProtocolType = StreamProtocolType.RTSP;
      }
      String protocol = getProtocol( streamProtocolType );
      String operationType = ( streamType == StreamType.RECORDING ) ? "playLive" : "playRecorded";
      String wowzaAddress = WOWZA_SERVER_IP + ":" + WOWZA_SERVER_PORT + "/"
          + ( ( streamType == StreamType.RECORDING ) ? WOWZA_SERVER_LIVE_APP_NAME : WOWZA_SERVER_VOD_APP_NAME ) + "/_definst_/";
      String params = getConnectParams( tube, operationType, fileName );

      String streamName = getStreamName( fileName, streamProtocolType );
      String url = protocol + wowzaAddress + streamName + params;
      System.out.println( "url = " + url );

      mediaPlayer.setDataSource( url );
      mediaPlayer.prepare();
      mediaPlayer.start();
    }

  }

  private String getStreamName( String fileName, StreamProtocolType protocol )
  {
    String subDir = Backendless.getApplicationId().toLowerCase() + "/files/media/";
    String hlsAdditionalParameter = ( protocol == StreamProtocolType.HLS ) ? HLS_PLAYLIST_CONSTANT : "";
    return subDir + fileName + hlsAdditionalParameter;
  }

  private String getProtocol( StreamProtocolType streamProtocolType )
  {
    if( streamProtocolType.equals( StreamProtocolType.RTSP ) )
    {
      return RTSP_PROTOCOL;
    }
    if( streamProtocolType.equals( StreamProtocolType.HLS ) )
    {
      return HLS_PROTOCOL;
    }
    throw new BackendlessException( "Backendless Android SDK not support type '" + streamProtocolType + "'" );
  }

  private Session getSession( Context context, SurfaceView mSurfaceView, int orientation )
  {
    Session mSession = SessionBuilder.getInstance().setContext( context ).setAudioEncoder( SessionBuilder.AUDIO_AAC )
        .setAudioQuality( new AudioQuality( 8000, 16000 ) ).setVideoEncoder( SessionBuilder.VIDEO_H264 ).setSurfaceView( mSurfaceView )
        .setPreviewOrientation( orientation ).setCallback( (Session.Callback) context ).build();

    return mSession;
  }

  private String getConnectParams( String tube, String operationType, String streamName )
  {
    String paramsToSend;
    BackendlessUser currentUser = Backendless.UserService.CurrentUser();
    Object identity = currentUser != null ? currentUser.getProperty( "user-token" ) : null;

    HashMap<String, String> map = new HashMap<String, String>();
    map.putAll( HeadersManager.getInstance().getHeaders() );
    map.put( "identity", identity != null ? identity.toString() : map.get( "user-token" ) );
    paramsToSend = "?application-id=" + map.get( "application-id" ) + "&version=" + Backendless.getVersion() + "&identity="
        + map.get( "identity" ) + "&tube=" + tube + "&operationType=" + operationType + "&streamName=" + streamName;
    return paramsToSend;
  }

  @SuppressWarnings( "unused" )
  private void selectQuality( Session session, StreamQuality streamQuality )
  {
    Pattern pattern = Pattern.compile( "(\\d+)x(\\d+)\\D+(\\d+)\\D+(\\d+)" );
    Matcher matcher = pattern.matcher( streamQuality.getValue() );
    matcher.find();
    int width = Integer.parseInt( matcher.group( 1 ) );
    int height = Integer.parseInt( matcher.group( 2 ) );
    int framerate = Integer.parseInt( matcher.group( 3 ) );
    int bitrate = Integer.parseInt( matcher.group( 4 ) ) * 1000;
    session.setVideoQuality( new VideoQuality( width, height, framerate, bitrate ) );
  }

  // Connects/disconnects to the RTSP server and starts/stops the stream
  private void startOrStopStream( RtspClient rtspClient, String streamName, String params )
  {
    if( !rtspClient.isStreaming() )
    {
      rtspClient.setServerAddress( WOWZA_SERVER_IP, WOWZA_SERVER_PORT );
      rtspClient.setStreamPath( "/" + WOWZA_SERVER_LIVE_APP_NAME + "/" + streamName + params );
      rtspClient.startStream();
    }
    else
    {
      // Stops the stream and disconnects from the RTSP server
      rtspClient.stopStream();
    }
  }

  private RtspClient getRtspClient( Context context, Session mSession )
  {
    // Configures the RTSP client
    if( rtspClient == null )
    {
      rtspClient = new RtspClient();
      rtspClient.setSession( mSession );
      rtspClient.setCallback( (RtspClient.Callback) context );
    }
    return rtspClient;
  }
}
