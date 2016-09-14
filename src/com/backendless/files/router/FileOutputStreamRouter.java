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

import com.backendless.async.callback.UploadCallback;

import java.io.*;

public class FileOutputStreamRouter implements OutputStreamRouter
{

  private final File file;
  private final UploadCallback uploadCallback;

  private int lastProgress;

  public FileOutputStreamRouter( File file, UploadCallback uploadCallback )
  {
    this.file = file;
    this.uploadCallback = uploadCallback == null ? new DummyUploadCallback() : uploadCallback;
  }

  @Override
  public void writeStream( OutputStream out ) throws IOException
  {
    long fileSize = file.length();
    byte[] buffer = new byte[ BUFFER_DEFAULT_LENGTH ];
    int readBytesTotal = 0;

    try (BufferedInputStream inputStream = new BufferedInputStream( new FileInputStream( file ) ) )
    {
      int readBytes;
      while( ( readBytes = inputStream.read( buffer ) ) != -1 )
      {
        out.write( buffer, 0, readBytes );
        out.flush();
        updateProgress( fileSize, readBytesTotal += readBytes );
      }
    }
  }

  private void updateProgress( long fileSize, double readBytesTotal )
  {
    int progress = (int) ( ( readBytesTotal / fileSize ) * 100 );
    if( progress != lastProgress )
    {
      lastProgress = progress;
      uploadCallback.onProgressUpdate( progress );
    }
  }

  private static class DummyUploadCallback implements UploadCallback
  {
    @Override
    public void onProgressUpdate( Integer progress )
    {
      //noop
    }
  }
}
