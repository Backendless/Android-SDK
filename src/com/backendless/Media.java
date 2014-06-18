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

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;
import android.widget.MediaController;
import android.widget.VideoView;
import com.backendless.commons.media.OperationMeta;
import com.backendless.exceptions.BackendlessException;
import com.backendless.media.MediaOperations;
import com.backendless.media.StreamProtocolType;
import com.backendless.media.StreamQuality;
import com.backendless.media.StreamType;
import com.backendless.media.lib.Session;
import com.backendless.media.lib.SessionBuilder;
import com.backendless.media.lib.audio.AudioQuality;
import com.backendless.media.lib.gl.SurfaceView;
import com.backendless.media.lib.rtsp.RtspClient;
import com.backendless.media.lib.video.VideoQuality;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Media
{
  private final static String GEO_MANAGER_SERVER_ALIAS = "com.backendless.services.media.MediaService";
  private final static String SERVER_URL_LIVE = "10.0.1.9:1935/mediaAppLive/";  //Change to api.backendless.com
  private final static String SERVER_URL_VOD = "10.0.1.9:1935/mediaAppVod/";  //Change to api.backendless.com
  private final static String RTSP_PROTOCOL = StreamProtocolType.RTSP.getValue();
  private final static String HLS_PROTOCOL = StreamProtocolType.HLS.getValue();
  private final static String USER_NAME = "root";
  private final static String USER_PASSWORD = "123";
  private String URL = "";

  private static final Media instance = new Media();

  static Media getInstance()
  {
    return instance;
  }

  public void publishLiveAndRecord( Context context,  SurfaceView surfaceView, String tube, String streamName, StreamQuality streamQuality )
  {
    publishStream( context, surfaceView, tube, streamName, true, streamQuality );
  }

  public void publishLive( Context context, SurfaceView surfaceView, String tube, String streamName, StreamQuality streamQuality )
  {
    publishStream( context, surfaceView, tube, streamName, false, streamQuality );
  }

  public void playLive( Context context, VideoView videoView, StreamProtocolType streamProtocolType, String tube, String streamName )
  {
    playStream( context, videoView, streamProtocolType, tube, streamName, StreamType.Live );
  }

  public void playRecord( Context context, VideoView videoView, StreamProtocolType streamProtocolType, String tube, String streamName )
  {
    playStream( context, videoView, streamProtocolType, tube, streamName, StreamType.Available );
  }

  private void publishStream( Context context, SurfaceView surfaceView, String tube, String streamName,
                              Boolean iSRecord, StreamQuality streamQuality )
  {
    URL = RTSP_PROTOCOL + SERVER_URL_LIVE + streamName;
    Session session = getSession( context, surfaceView );
    RtspClient rtspClient = getRtspClient( context, session );
    surfaceView.getHolder().addCallback( (SurfaceHolder.Callback) context );
    selectQuality(session, streamQuality);
    startOrStopStream( context, rtspClient );

//    String streamType = null;
//    String operation = "playRecord";
//    if( iSRecord )
//    {
//      streamType = "live";
//      operation = "playLive";
//    }
  }

  private void playStream( Context context, VideoView videoView, StreamProtocolType streamProtocolType, String tube, String streamName, StreamType streamType )
  {
    String url = null;

    if( streamProtocolType == null )
    {
      streamProtocolType = StreamProtocolType.RTSP;
    }

    if(  streamProtocolType.equals( StreamProtocolType.RTSP ))
    {
      if( streamType.getValue() == 1  )
        url = RTSP_PROTOCOL + SERVER_URL_VOD + streamName;

      else
        url =  RTSP_PROTOCOL + SERVER_URL_VOD + "mp4:" + streamName + ".mp4"; //Add File Format
    }

    if( streamProtocolType.equals( StreamProtocolType.HLS ))
    {
      if( streamType.getValue() == 1 )
        url = HLS_PROTOCOL + SERVER_URL_VOD + streamName + "/playlist.m3u8";

      else
        url =  HLS_PROTOCOL + SERVER_URL_VOD + "mp4:" + streamName + ".mp4/playlist.m3u8"; //Add File Format
    }

    videoView.setVideoURI( Uri.parse( url ));
    videoView.setMediaController(new MediaController( context ));
    videoView.requestFocus();
    videoView.start();
  }

  private Session getSession(Context context, SurfaceView mSurfaceView)
  {
    Session mSession = SessionBuilder.getInstance()
            .setContext( context )
            .setAudioEncoder( SessionBuilder.AUDIO_AAC )
            .setAudioQuality( new AudioQuality( 8000, 16000 ) )
            .setVideoEncoder( SessionBuilder.VIDEO_H264 )
            .setSurfaceView( mSurfaceView )
            .setPreviewOrientation( 0 )
            .setCallback( (Session.Callback) context )
            .build();

    return mSession;
  }

  private void selectQuality(Session session, StreamQuality streamQuality)
  {
    Pattern pattern = Pattern.compile("(\\d+)x(\\d+)\\D+(\\d+)\\D+(\\d+)");
    Matcher matcher = pattern.matcher(streamQuality.getValue());
    matcher.find();
    int width = Integer.parseInt(matcher.group(1));
    int height = Integer.parseInt(matcher.group(2));
    int framerate = Integer.parseInt(matcher.group(3));
    int bitrate = Integer.parseInt(matcher.group(4))*1000;
    session.setVideoQuality(new VideoQuality(width, height, framerate, bitrate));
  }

  // Connects/disconnects to the RTSP server and starts/stops the stream
  private void startOrStopStream( Context context, RtspClient rtspClient ) {
    if (!rtspClient.isStreaming()) {
      String ip,port,path;

      // We save the content user inputs in Shared Preferences
      SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences( context );
      SharedPreferences.Editor editor = mPrefs.edit();
      editor.putString("uri", URL);
      editor.putString("password", USER_PASSWORD);
      editor.putString("username", USER_NAME);
      editor.commit();

      // We parse the URI written in the Editext
      Pattern uri = Pattern.compile("rtsp://(.+):(\\d+)/(.+)");
      Matcher m = uri.matcher(URL); m.find();
      ip = m.group(1);
      port = m.group(2);
      path = m.group(3);

      rtspClient.setCredentials(USER_NAME, USER_PASSWORD);
      rtspClient.setServerAddress(ip, Integer.parseInt(port));
      rtspClient.setStreamPath("/"+path);
      rtspClient.startStream();

    } else {
      // Stops the stream and disconnects from the RTSP server
      rtspClient.stopStream();
    }
  }

  private RtspClient getRtspClient(Context context, Session mSession)
{
    // Configures the RTSP client
    RtspClient mClient = new RtspClient();
    mClient.setSession( mSession );
    mClient.setCallback( (RtspClient.Callback) context );

  return mClient;
  }
  private boolean acceptConnection( String tube, MediaOperations operation ) throws BackendlessException
  {
    Boolean hasAccess = false;

    hasAccess = Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "acceptConnection", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), null, tube, operation } );
    return hasAccess;
  }

//  private void publishStarted( AppVersion appVersion, String tube, StreamType type, String identity,
//                               MediaObjectInfo mediaObjectInfo )
//  {
//    Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "publishStarted", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), categoryName } );
//  }
//
//  private void publishFinished( AppVersion appVersion, String tube, StreamType type, String identity,
//                                MediaObjectInfo mediaObjectInfo )
//  {
//    Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "publishFinished", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), categoryName } );
//  }

  private void streamDestroyed( String identity, OperationMeta meta )
  {
    Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "streamDestroyed", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), identity, meta } );
  }

  private void streamCreated( String identity, OperationMeta meta )
  {
    Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "streamCreated", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), identity, meta } );
  }
}
