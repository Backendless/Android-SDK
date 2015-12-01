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
import android.view.SurfaceHolder;

import com.backendless.exceptions.BackendlessException;
import com.backendless.media.DisplayOrientation;
import com.backendless.media.Session;
import com.backendless.media.SessionBuilder;
import com.backendless.media.StreamProtocolType;
import com.backendless.media.StreamType;
import com.backendless.media.StreamVideoQuality;
import com.backendless.media.audio.AudioQuality;
import com.backendless.media.gl.SurfaceView;
import com.backendless.media.rtsp.RtspClient;
import com.backendless.media.video.VideoQuality;

public final class Media
{

  private final static String WOWZA_SERVER_IP = "media.backendless.com";
  private final static String WOWZA_SERVER_LIVE_APP_NAME = "mediaAppLive";
  private final static String WOWZA_SERVER_VOD_APP_NAME = "mediaAppVod";
  private final static Integer WOWZA_SERVER_PORT = 1935;

  private final static String RTSP_PROTOCOL = StreamProtocolType.RTSP.getValue();
  private final static String HLS_PROTOCOL = StreamProtocolType.HLS.getValue();

  private static final String MEDIA_FILES_LOCATION = "/files/media/";
  private static final String HLS_PLAYLIST_CONSTANT = "/playlist.m3u8";
  private RtspClient rtspClient;
  private Session session;
  private MediaPlayer mediaPlayer;
  private StreamProtocolType protocolType;

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

  public StreamVideoQuality getStreamQuality()
  {
    checkSessionIsNull();
    VideoQuality videoQuality = session.getVideoTrack().getVideoQuality();
    int width = videoQuality.resX;
    int height = videoQuality.resY;
    int framerate = videoQuality.framerate;
    int bitrate = videoQuality.bitrate;
    StreamVideoQuality streamQuality = StreamVideoQuality.getFromString( width + "x" + height + ", " + framerate + " fps, " + bitrate
        / 1000 + " Kbps" );
    return streamQuality;
  }

  public void setVideoQuality( StreamVideoQuality streamQuality )
  {
    checkSessionIsNull();
    if( streamQuality == null )
    {
      return;
    }
    VideoQuality videoQuality = convertVideoQuality( streamQuality );
    session.setVideoQuality( videoQuality );
  }

  private VideoQuality convertVideoQuality( StreamVideoQuality streamQuality )
  {
    Pattern pattern = Pattern.compile( "(\\d+)x(\\d+)\\D+(\\d+)\\D+(\\d+)" );
    Matcher matcher = pattern.matcher( streamQuality.getValue() );

    matcher.find();
    int width = Integer.parseInt( matcher.group( 1 ) );
    int height = Integer.parseInt( matcher.group( 2 ) );
    int framerate = Integer.parseInt( matcher.group( 3 ) );
    int bitrate = Integer.parseInt( matcher.group( 4 ) ) * 1000;

    VideoQuality videoQuality = new VideoQuality( width, height, framerate, bitrate );
    return videoQuality;
  }

  public void setAudioQuality( int sampleRate, int bitRate )
  {
    checkSessionIsNull();
    session.setAudioQuality( new AudioQuality( sampleRate, bitRate ) );
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

  private void stopClientStream()
  {
    checkRtspClientIsNull();
    rtspClient.stopStream();
  }

  public void releaseSession()
  {
    checkSessionIsNull();
    session.release();
  }

  public void releaseClient()
  {
    checkRtspClientIsNull();
    rtspClient.release();
  }

  public boolean isPublishing()
  {
    checkRtspClientIsNull();
    return rtspClient.isStreaming();
  }

  public boolean isMediaPlayerBusy()
  {
    checkPlayerIsNull();
    return mediaPlayer.isPlaying();
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

  /**
   * <p>
   * default video quality to 176x144 20fps 500Kbps<br/>
   * default audio quality to 16 000 sampleRate 272000 bitRate
   * </p>
   */
  public void configureForPublish( Context context, SurfaceView mSurfaceView, DisplayOrientation orientation )
  {
    session = getSession( context, mSurfaceView, orientation.getValue() );
    rtspClient = getRtspClient( context, session );
  }

  /**
   * StreamProtocolType sets to default value - RTSP
   * 
   * @param mSurfaceHolder
   */
  public void configureForPlay( SurfaceHolder mSurfaceHolder )
  {
    configureForPlay( mSurfaceHolder, StreamProtocolType.RTSP );
  }

  public void configureForPlay( SurfaceHolder mSurfaceHolder, StreamProtocolType protocolType )
  {
    this.protocolType = protocolType;
    mediaPlayer = new MediaPlayer();
    mediaPlayer.setDisplay( mSurfaceHolder );
    mediaPlayer.setAudioStreamType( AudioManager.STREAM_MUSIC );
    mediaPlayer.setOnPreparedListener( new OnPreparedListener() {

      @Override
      public void onPrepared( MediaPlayer mp )
      {
        mp.start();
      }
    } );
  }

  public void recordVideo( String tube, String streamName ) throws BackendlessException
  {
    startRtspStream( tube, streamName, StreamType.LIVE_RECORDING );
  }

  public void broadcastLiveVideo( String tube, String streamName ) throws BackendlessException
  {
    startRtspStream( tube, streamName, StreamType.LIVE );
  }

  public void playLiveVideo( String tube, String streamName ) throws IllegalArgumentException, SecurityException, IllegalStateException,
      IOException, BackendlessException
  {
    playStream( tube, streamName, StreamType.RECORDING );
  }

  public void playOnDemandVideo( String tube, String streamName ) throws IllegalArgumentException, SecurityException,
      IllegalStateException, IOException, BackendlessException
  {
    playStream( tube, streamName, StreamType.AVAILABLE );
  }

  private void startRtspStream( String tube, String streamName, StreamType streamType ) throws BackendlessException
  {
    checkSessionIsNull();
    checkRtspClientIsNull();
    if( mediaPlayer != null )
    {
      mediaPlayer.reset();
    }
    streamName = makeNameValid( streamName );
    tube = makeTubeValid( tube );
    String operationType = getOperationType( streamType );
    String params = getConnectParams( tube, operationType, streamName );
    startStream( rtspClient, streamName, params );
  }

  public void stopPublishing()
  {
    checkRtspClientIsNull();
    if( rtspClient.isStreaming() )
    {
      stopClientStream();
    }
  }

  private String getOperationType( StreamType streamType )
  {
    return ( streamType == StreamType.LIVE ) ? "publishLive" : "publishRecorded";
  }

  private void playStream( String tube, String streamName, StreamType streamType ) throws IllegalArgumentException, SecurityException,
      IllegalStateException, IOException, BackendlessException
  {
    checkPlayerIsNull();
    if( mediaPlayer.isPlaying() )
    {
      throw new BackendlessException( "Other stream is playing now. You must to stop it before" );
    }
    streamName = makeNameValid( streamName );
    tube = makeTubeValid( tube );

    if( session != null )
    {
      session.stopPreview();
    }
    mediaPlayer.reset();

    if( protocolType == null )
    {
      protocolType = StreamProtocolType.RTSP;
    }
    String protocol = getProtocol( protocolType );
    String operationType = ( streamType == StreamType.RECORDING ) ? "playLive" : "playRecorded";
    String wowzaAddress = WOWZA_SERVER_IP + ":" + WOWZA_SERVER_PORT + "/"
        + ( ( streamType == StreamType.RECORDING ) ? WOWZA_SERVER_LIVE_APP_NAME : WOWZA_SERVER_VOD_APP_NAME ) + "/_definst_/";
    String params = getConnectParams( tube, operationType, streamName );

    String streamPath = getStreamName( streamName, protocolType );
    String url = protocol + wowzaAddress + streamPath + params;
    mediaPlayer.setDataSource( url );
    mediaPlayer.prepareAsync();

  }

  private String makeTubeValid( String tube )
  {
    if( tube == null || tube.isEmpty() )
    {
      tube = "default";
    }
    else
    {
      tube = tube.trim();
    }
    return tube;
  }

  private String makeNameValid( String streamName )
  {
    if( streamName == null || streamName.isEmpty() )
    {
      streamName = "default";
    }
    else
    {
      streamName = streamName.trim().replace( '.', '_' );
    }
    return streamName;
  }

  public void stopVideoPlayback()
  {
    checkPlayerIsNull();
    if( mediaPlayer.isPlaying() )
    {
      mediaPlayer.stop();
      mediaPlayer.reset();
    }
  }

  private String getStreamName( String fileName, StreamProtocolType protocol )
  {
    String subDir = Backendless.getApplicationId().toLowerCase() + MEDIA_FILES_LOCATION;
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
    throw new BackendlessException( "Backendless Android SDK not supported protocol type '" + streamProtocolType + "'" );
  }

  private Session getSession( Context context, SurfaceView mSurfaceView, int orientation )
  {
    Session mSession = SessionBuilder.getInstance().setContext( context ).setAudioEncoder( SessionBuilder.AUDIO_AAC )
        .setVideoEncoder( SessionBuilder.VIDEO_H264 ).setSurfaceView( mSurfaceView ).setPreviewOrientation( orientation )
        .setCallback( (Session.Callback) context ).build();

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

  // Connects/disconnects to the RTSP server and starts/stops the stream
  private void startStream( RtspClient rtspClient, String streamName, String params ) throws BackendlessException
  {
    if( rtspClient.isStreaming() )
    {
      throw new BackendlessException( "Rtsp client is working on other stream" );
    }
    rtspClient.setServerAddress( WOWZA_SERVER_IP, WOWZA_SERVER_PORT );
    rtspClient.setStreamPath( "/" + WOWZA_SERVER_LIVE_APP_NAME + "/" + streamName + params );
    rtspClient.startStream();
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

  public StreamProtocolType getProtocolType()
  {
    return protocolType;
  }

  public void setProtocolType( StreamProtocolType protocolType )
  {
    this.protocolType = protocolType;
  }

}
