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

import com.backendless.media.*;
import com.backendless.media.audio.AudioQuality;
import com.backendless.media.gl.SurfaceView;
import com.backendless.media.rtsp.RtspClient;
import com.backendless.media.video.VideoQuality;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Media
{
  private final static String SERVER_URL_LIVE = "wowza.backendless.com:1935/mediaAppLive/";
  private final static String SERVER_URL_VOD = "wowza.backendless.com:1935/mediaAppVod/";
  private final static String RTSP_PROTOCOL = StreamProtocolType.RTSP.getValue();
  private final static String HLS_PROTOCOL = StreamProtocolType.HLS.getValue();
  private final static String USER_NAME = "root";
  private final static String USER_PASSWORD = "123";
  private String url;
  private String params;

  private static final Media instance = new Media();

  static Media getInstance()
  {
    return instance;
  }

  public void publishLiveAndRecord( Context context, SurfaceView backendlessSurfaceView, String tube, String streamName,
      StreamQuality streamQuality, DisplayOrientation orientation )
  {
    publishStream( context, backendlessSurfaceView, tube, streamName, streamQuality, StreamType.LiveRecording, orientation );
  }

  public void publishLive( Context context, SurfaceView backendlessSurfaceView, String tube, String streamName,
      StreamQuality streamQuality, DisplayOrientation orientation )
  {
    publishStream( context, backendlessSurfaceView, tube, streamName, streamQuality, StreamType.Live, orientation );
  }

  public void playLive( Context context, VideoView videoView, StreamProtocolType streamProtocolType, String tube, String streamName )
  {
    playStream( context, videoView, streamProtocolType, tube, streamName, StreamType.Recording );
  }

  public void playRecord( Context context, VideoView videoView, StreamProtocolType streamProtocolType, String tube, String streamName )
  {
    playStream( context, videoView, streamProtocolType, tube, streamName, StreamType.Available );
  }

  private void publishStream( Context context, SurfaceView backendlessSurfaceView, String tube, String streamName,
                              StreamQuality streamQuality, StreamType streamType, DisplayOrientation orientation )
  {
    String operationType;

    if( streamType.getValue()== 1 )
      operationType = "publishLive";

    else operationType = "publishLiveAndRecord";

    params = getConnectParams( tube, operationType, streamName);
    url = RTSP_PROTOCOL + SERVER_URL_LIVE + streamName + params;

    Session session = getSession( context, backendlessSurfaceView, orientation.getValue() );
    RtspClient rtspClient = getRtspClient( context, session );
    backendlessSurfaceView.getHolder().addCallback( (SurfaceHolder.Callback) context );
    selectQuality(session, streamQuality);
    startOrStopStream( context, rtspClient, url );
  }

  private void playStream( Context context, VideoView videoView, StreamProtocolType streamProtocolType, String tube, String streamName, StreamType streamType )
  {
    String operationType = null;
    String url = null;
    String subDir = Backendless.getApplicationId().toLowerCase() + "/files/media/";

    if( streamProtocolType == null )
    {
      streamProtocolType = StreamProtocolType.RTSP;
    }

    if(  streamProtocolType.equals( StreamProtocolType.RTSP ))
    {
      if( streamType.getValue() == 2  )
      {
        operationType = "playLive";
        params = getConnectParams( tube, operationType, streamName);
        url = RTSP_PROTOCOL + SERVER_URL_LIVE + "_definst_/" + subDir + streamName + params;
      }

      else
      {
        operationType = "playRecord";
        params = getConnectParams( tube, operationType, streamName);
        url =  RTSP_PROTOCOL + SERVER_URL_VOD + "_definst_/mp4:" + subDir + streamName + ".mp4" + params;
      }
    }

    if( streamProtocolType.equals( StreamProtocolType.HLS ))
    {
      if( streamType.getValue() == 2 )
      {
        operationType = "playLive";
        params = getConnectParams( tube, operationType, streamName);
        url = HLS_PROTOCOL + SERVER_URL_LIVE + "_definst_/" + subDir + streamName + "/playlist.m3u8" + params;
      }

      else
      {
        operationType = "playRecord";
        params = getConnectParams( tube, operationType, streamName);
        url =  HLS_PROTOCOL + SERVER_URL_VOD + "_definst_/mp4:" + subDir + streamName + ".mp4/playlist.m3u8"+ params;
      }
    }

    videoView.setVideoURI( Uri.parse( url ));
    videoView.setMediaController(new MediaController( context ));
    videoView.requestFocus();
    videoView.start();
  }

  private Session getSession( Context context, SurfaceView mSurfaceView, int orientation )
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

  private String getConnectParams(String tube, String operationType, String streamName)
  {
    String paramsToSend;
    BackendlessUser currentUser = Backendless.UserService.CurrentUser();
    Object identity = currentUser != null?currentUser.getProperty( "user-token" ): null;

    HashMap<String, String> map = new HashMap<String, String>(  );
    map.putAll( HeadersManager.getInstance().getHeaders() );
    map.put( "identity", identity != null? identity.toString(): map.get( "user-token" ) );
    paramsToSend = "?application-id=" + map.get( "application-id" ) + "&version=" + Backendless.getVersion() + "&identity=" + map.get( "identity" ) + "&tube=" + tube + "&operationType=" + operationType+"&streamName=" + streamName;
    return paramsToSend;
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
  private void startOrStopStream( Context context, RtspClient rtspClient, String url ) {
    if (!rtspClient.isStreaming()) {
      String ip,port,path;

      // We save the content user inputs in Shared Preferences
      SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences( context );
      SharedPreferences.Editor editor = mPrefs.edit();
      editor.putString("uri", url);
      editor.putString("password", USER_PASSWORD);
      editor.putString("username", USER_NAME);
      editor.commit();

      // We parse the URI written in the Editext
      Pattern uri = Pattern.compile("rtsp://(.+):(\\d+)/(.+)");
      Matcher m = uri.matcher(url); m.find();
      ip = m.group(1);
      port = m.group(2);
      path = m.group(3);

      rtspClient.setCredentials(USER_NAME, USER_PASSWORD);
      rtspClient.setServerAddress( ip, Integer.parseInt( port ) );
      rtspClient.setStreamPath( "/" + path );
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
}
