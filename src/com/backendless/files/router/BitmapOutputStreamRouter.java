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

package com.backendless.files.router;

import android.graphics.Bitmap;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitmapOutputStreamRouter implements OutputStreamRouter
{
  private final Bitmap bitmap;
  private final Bitmap.CompressFormat compressFormat;
  private final int quality;

  public BitmapOutputStreamRouter( Bitmap bitmap, Bitmap.CompressFormat compressFormat, int quality )
  {
    this.bitmap = bitmap;
    this.compressFormat = compressFormat;
    this.quality = quality;
  }

  @Override
  public void writeStream( OutputStream outputStream ) throws IOException
  {
    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream( outputStream, BUFFER_DEFAULT_LENGTH );
    bitmap.compress( compressFormat, quality, bufferedOutputStream );
    bufferedOutputStream.flush();
  }
}
