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
import com.backendless.media.lib.SessionBuilder;
import com.backendless.media.lib.rtp.AMRNBPacketizer;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * A class for backendless AAC from the camera of an android device using RTP.
 * You should use a {@link android.service.textservice.SpellCheckerService.Session} instantiated with {@link SessionBuilder} instead of using this class directly.
 * Call {@link #setDestinationAddress(InetAddress)}, {@link #setDestinationPorts(int)} and {@link #setAudioQuality(AudioQuality)}
 * to configure the stream. You can then call {@link #start()} to start the RTP stream.
 * Call {@link #stop()} to stop the stream.
 */
public class AMRNBStream extends AudioStream {

	public AMRNBStream() {
		super();

		mPacketizer = new AMRNBPacketizer();

		setAudioSource( MediaRecorder.AudioSource.CAMCORDER);
		
		try {
			// RAW_AMR was deprecated in API level 16.
			Field deprecatedName = MediaRecorder.OutputFormat.class.getField("RAW_AMR");
			setOutputFormat(deprecatedName.getInt(null));
		} catch (Exception e) {
			setOutputFormat( MediaRecorder.OutputFormat.AMR_NB);
		}
		
		setAudioEncoder( MediaRecorder.AudioEncoder.AMR_NB);
		
	}

	/**
	 * Starts the stream.
	 */
	public synchronized void start() throws IllegalStateException, IOException
  {
		if (!mStreaming) {
			configure();
			super.start();
		}
	}
	
	public synchronized void configure() throws IllegalStateException, IOException
  {
		super.configure();
		mMode = MODE_MEDIARECORDER_API;
		mQuality = mRequestedQuality.clone();
	}
	
	/**
	 * Returns a description of the stream using SDP. It can then be included in an SDP file.
	 */	
	public String getSessionDescription() {
		return "m=audio "+ String.valueOf( getDestinationPorts()[ 0 ] )+" RTP/AVP 96\r\n" +
				"a=rtpmap:96 AMR/8000\r\n" +
				"a=fmtp:96 octet-align=1;\r\n";
	}

	@Override
	protected void encodeWithMediaCodec() throws IOException
  {
		super.encodeWithMediaRecorder();
	}

}
