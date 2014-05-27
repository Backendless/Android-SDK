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
import com.backendless.commons.media.MediaObjectInfo;
import com.backendless.exceptions.BackendlessException;
import com.backendless.media.MediaOperations;
import com.backendless.media.StreamType;
import com.backendless.media.lib.Session;
import com.backendless.media.lib.SessionBuilder;
import com.backendless.media.lib.audio.AudioQuality;
import com.backendless.media.lib.gl.SurfaceView;
import com.backendless.media.lib.rtsp.RtspClient;

public final class Media
{
  private final static String GEO_MANAGER_SERVER_ALIAS = "com.backendless.services.media.MediaService";
  private final static String SERVER_URL = "rtsp://10.0.1.9:1935/mediaApp/";  //Change to api.backendless.com

  private static final Media instance = new Media();

  static Media getInstance()
  {
    return instance;
  }

  public void playRecord( Context context, String tube, String streamName, SurfaceView mSurfaceView  )
  {
    playStream( context, tube, streamName, false, mSurfaceView  );
  }

  public void playLive( Context context, String tube, String streamName, SurfaceView mSurfaceView  )
  {
    playStream( context, tube, streamName, true, mSurfaceView );
  }

  public void publishLive( String tube, String streamName )
  {
    recordStream( tube, streamName, StreamType.Live );
  }

  public void publishLiveAndRecord( String tube, String streamName )
  {
    recordStream( tube, streamName, StreamType.LiveRecording );
  }

  private void playStream(Context context, String tube, String fileName, Boolean typeIsLive, SurfaceView mSurfaceView )
  {
    Session mSession = getSession( context, mSurfaceView );
    RtspClient mClient = getRtspClient( context, mSession );
    String streamType = null;
    String operation = "playRecord";
    if( typeIsLive )
    {
      streamType = "live";
      operation = "playLive";
    }


  }

  private void recordStream( String tube, String streamName, StreamType streamType )
  {

  }

  private Session getSession(Context context, SurfaceView mSurfaceView)
  {
    Session mSession = SessionBuilder.getInstance()
            .setContext(context)
            .setAudioEncoder(SessionBuilder.AUDIO_AAC)
            .setAudioQuality(new AudioQuality(8000,16000))
            .setVideoEncoder(SessionBuilder.VIDEO_H264)
            .setSurfaceView(mSurfaceView)
            .setPreviewOrientation(0)
            .setCallback( (Session.Callback) context )
            .build();

    return mSession;
  }

  private RtspClient getRtspClient(Context context, Session mSession)
{
    // Configures the RTSP client
    RtspClient mClient = new RtspClient();
    mClient.setSession(mSession);
    mClient.setCallback( (RtspClient.Callback) context );

  return mClient;
  }
  private boolean acceptConnection( String tube, MediaOperations operation ) throws BackendlessException
  {
    Boolean hasAccess = false;

    hasAccess = Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "acceptConnection", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), null, tube, operation } );
    return hasAccess;
  }

  private void publishStarted( AppVersion appVersion, String tube, StreamType type, String identity,
                               MediaObjectInfo mediaObjectInfo )
  {
    Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "publishStarted", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), categoryName } );
  }

  private void publishFinished( AppVersion appVersion, String tube, StreamType type, String identity,
                                MediaObjectInfo mediaObjectInfo )
  {
    Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "publishFinished", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), categoryName } );
  }

  private void streamDestroyed( String identity, OperationMeta meta )
  {
    Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "streamDestroyed", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), identity, meta } );
  }

  private void streamCreated( String identity, OperationMeta meta )
  {
    Invoker.invokeSync( GEO_MANAGER_SERVER_ALIAS, "streamCreated", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), identity, meta } );
  }
}
