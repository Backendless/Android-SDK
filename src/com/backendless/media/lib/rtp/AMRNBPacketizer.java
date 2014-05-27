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

package com.backendless.media.lib.rtp;

import android.util.Log;

import java.io.IOException;

/**
 * 
 *   RFC 3267.
 *   
 *   AMR Streaming over RTP.
 *   
 *   Must be fed with an InputStream containing raw amr nb
 *   Stream must begin with a 6 bytes long header: "#!AMR\n", it will be skipped
 *   
 */
public class AMRNBPacketizer extends AbstractPacketizer implements Runnable
{

	public final static String TAG = "AMRNBPacketizer";

	private final int AMR_HEADER_LENGTH = 6; // "#!AMR\n"
	private static final int AMR_FRAME_HEADER_LENGTH = 1; // Each frame has a short header
	private static final int[] sFrameBits = {95, 103, 118, 134, 148, 159, 204, 244};
	private int samplingRate = 8000;

	private Thread t;

	public AMRNBPacketizer() {
		super();
		socket.setClockFrequency(samplingRate);
	}

	public void start() {
		if (t==null) {
			t = new Thread(this);
			t.start();
		}
	}

	public void stop() {
		if (t != null) {
			try {
				is.close();
			} catch (IOException ignore) {}
			t.interrupt();
			try {
				t.join();
			} catch (InterruptedException e) {}
			t = null;
		}
	}

	public void run() {

		int frameLength, frameType;
		long now = System.nanoTime(), oldtime = now;
		byte[] header = new byte[AMR_HEADER_LENGTH];

		try {

			// Skip raw amr header
			fill(header,0,AMR_HEADER_LENGTH);
			
			if (header[5] != '\n') {
				Log.e( TAG, "Bad header ! AMR not correcty supported by the phone !" );
				return;
			}

			while (!Thread.interrupted()) {

				buffer = socket.requestBuffer();
				buffer[rtphl] = (byte) 0xF0;
				
				// First we read the frame header
				fill(buffer, rtphl+1,AMR_FRAME_HEADER_LENGTH);

				// Then we calculate the frame payload length
				frameType = (Math.abs( buffer[ rtphl + 1 ] ) >> 3) & 0x0f;
				frameLength = (sFrameBits[frameType]+7)/8;

				// And we read the payload
				fill(buffer, rtphl+2,frameLength);

				//Log.d(TAG,"Frame length: "+frameLength+" frameType: "+frameType);

				// RFC 3267 Page 14: "For AMR, the sampling frequency is 8 kHz"
				// FIXME: Is this really always the case ??
				ts += 160L*1000000000L/samplingRate; //stats.average();
				socket.updateTimestamp(ts);
				socket.markNextPacket();

				//Log.d(TAG,"expected: "+ expected + " measured: "+measured);
				
				send(rtphl+1+AMR_FRAME_HEADER_LENGTH+frameLength);
				
			}

		} catch (IOException e) {
		} catch (InterruptedException e) {}

		Log.d( TAG, "AMR packetizer stopped !" );

	}

	private int fill(byte[] buffer, int offset,int length) throws IOException
  {
		int sum = 0, len;
		while (sum<length) {
			len = is.read(buffer, offset+sum, length-sum);
			if (len<0) {
				throw new IOException("End of stream");
			}
			else sum+=len;
		}
		return sum;
	}


}
