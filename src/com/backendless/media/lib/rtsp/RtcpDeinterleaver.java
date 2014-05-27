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

package com.backendless.media.lib.rtsp;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

class RtcpDeinterleaver extends InputStream implements Runnable
{
	
	public final static String TAG = "RtcpDeinterleaver";
	
	private IOException mIOException;
	private InputStream mInputStream;
	private PipedInputStream mPipedInputStream;
	private PipedOutputStream mPipedOutputStream;
	private byte[] mBuffer;
	
	public RtcpDeinterleaver(InputStream inputStream) {
		mInputStream = inputStream;
		mPipedInputStream = new PipedInputStream(4096);
		try {
			mPipedOutputStream = new PipedOutputStream(mPipedInputStream);
		} catch (IOException e) {}
		mBuffer = new byte[1024];
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			while (true) {
				int len = mInputStream.read(mBuffer, 0, 1024);
				mPipedOutputStream.write(mBuffer, 0, len);
			}
		} catch (IOException e) {
			try {
				mPipedInputStream.close();
			} catch (IOException ignore) {}
			mIOException = e;
		}
	}

	@Override
	public int read(byte[] buffer) throws IOException
  {
		if (mIOException != null) {
			throw mIOException;
		}
		return mPipedInputStream.read(buffer);
	}		
	
	@Override
	public int read(byte[] buffer, int offset, int length) throws IOException
  {
		if (mIOException != null) {
			throw mIOException;
		}
		return mPipedInputStream.read(buffer, offset, length);
	}	
	
	@Override
	public int read() throws IOException
  {
		if (mIOException != null) {
			throw mIOException;
		}
		return mPipedInputStream.read();
	}

	@Override
	public void close() throws IOException
  {
		mInputStream.close();
	}

}
