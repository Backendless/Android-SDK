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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileOutputStreamRouter extends IOutputStreamRouter
{
  private File file;
  private UploadCallback uploadCallback;

  public FileOutputStreamRouter( File file, UploadCallback uploadCallback )
  {
    this.file = file;
    this.uploadCallback = uploadCallback;
  }

  @Override
  public void writeStream( int bufferSize ) throws IOException
  {
    long fileSize = file.length();
    byte[] buffer = new byte[ bufferSize ];
    int readBytesTotal = 0;

    try (FileInputStream fileInputStream = new FileInputStream( file ))
    {
      int readBytes;
      while( ( readBytes = fileInputStream.read( buffer ) ) != -1 )
      {
        getOutputStream().write( buffer, 0, readBytes );
        updateProgress( fileSize, readBytesTotal += readBytes );
      }
    }
  }

  private void updateProgress( long fileSize, double readBytesTotal )
  {
    if( uploadCallback != null )
    {
      int progress = (int) (( readBytesTotal / fileSize) * 100);
      uploadCallback.onProgressUpdate( progress );
    }
  }
}
