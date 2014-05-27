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

package com.backendless.media.lib.hw;

import android.media.MediaCodecInfo;

import java.nio.ByteBuffer;

/**
 * Converts from NV21 to YUV420 semi planar or planar.
 */		
public class NV21Convertor {

	private int mSliceHeight, mHeight;
	private int mStride, mWidth;
	private int mSize;
	private boolean mPlanar, mPanesReversed = false;
	private int mYPadding;
	private byte[] mBuffer; 
	ByteBuffer mCopy;
	
	public void setSize(int width, int height) {
		mHeight = height;
		mWidth = width;
		mSliceHeight = height;
		mStride = width;
		mSize = mWidth*mHeight;
	}
	
	public void setStride(int width) {
		mStride = width;
	}
	
	public void setSliceHeigth(int height) {
		mSliceHeight = height;
	}
	
	public void setPlanar(boolean planar) {
		mPlanar = planar;
	}
	
	public void setYPadding(int padding) {
		mYPadding = padding;
	}
	
	public int getBufferSize() {
		return 3*mSize/2;
	}
	
	public void setEncoderColorFormat(int colorFormat) {
		switch (colorFormat) {
		case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
		case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedSemiPlanar:
		case MediaCodecInfo.CodecCapabilities.COLOR_TI_FormatYUV420PackedSemiPlanar:
			setPlanar(false);
			break;	
		case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar:
		case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedPlanar:
			setPlanar(true);
			break;
		}
	}
	
	public void setColorPanesReversed(boolean b) {
		mPanesReversed = b;
	}
	
	public int getStride() {
		return mStride;
	}

	public int getSliceHeigth() {
		return mSliceHeight;
	}

	public int getYPadding() {
		return mYPadding;
	}
	
	
	public boolean getPlanar() {
		return mPlanar;
	}
	
	public boolean getUVPanesReversed() {
		return mPanesReversed;
	}
	
	public void convert(byte[] data, ByteBuffer buffer) {
		byte[] result = convert(data);
		buffer.put(result, 0, result.length);
	}
	
	public byte[] convert(byte[] data) {

		// A buffer large enough for every case
		if (mBuffer==null || mBuffer.length != 3*mSliceHeight*mStride/2+mYPadding) {
			mBuffer = new byte[3*mSliceHeight*mStride/2+mYPadding];
		}
		
		if (!mPlanar) {
			if (mSliceHeight==mHeight && mStride==mWidth) {
				// Swaps U and V
				if (!mPanesReversed) {
					for (int i = mSize; i < mSize+mSize/2; i += 2) {
						mBuffer[0] = data[i+1];
						data[i+1] = data[i];
						data[i] = mBuffer[0]; 
					}
				}
				if (mYPadding>0) {
					System.arraycopy( data, 0, mBuffer, 0, mSize );
					System.arraycopy( data, mSize, mBuffer, mSize + mYPadding, mSize / 2 );
					return mBuffer;
				}
				return data;
			}
		} else {
			if (mSliceHeight==mHeight && mStride==mWidth) {
				// De-interleave U and V
				if (!mPanesReversed) {
					for (int i = 0; i < mSize/4; i+=1) {
						mBuffer[i] = data[mSize+2*i+1];
						mBuffer[mSize/4+i] = data[mSize+2*i];
					}
				} else {
					for (int i = 0; i < mSize/4; i+=1) {
						mBuffer[i] = data[mSize+2*i];
						mBuffer[mSize/4+i] = data[mSize+2*i+1];
					}
				}
				if (mYPadding == 0) {
					System.arraycopy( mBuffer, 0, data, mSize, mSize / 2 );
				} else {
					System.arraycopy( data, 0, mBuffer, 0, mSize );
					System.arraycopy( mBuffer, 0, mBuffer, mSize + mYPadding, mSize / 2 );
					return mBuffer;
				}
				return data;
			}
		}
		
		return data;
	}	
	
}
