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
    long length = file.length();
    int uploadBufferLength = length < bufferSize ? (int) length : bufferSize;
    byte[] buffer = new byte[ uploadBufferLength ];
    int bytesRead = 0;
    int progress = 0;
    FileInputStream fileInputStream = new FileInputStream( file );

    try
    {
      while( true )
      {
        int currentBytes = fileInputStream.read( buffer );

        if( currentBytes == 0 )
          break;

        bytesRead += currentBytes;

        getOutputStream().write( buffer );

        if( uploadCallback != null )
        {
          int curProgress = (int) (((double) bytesRead / length) * 100);

          if( progress != curProgress )
          {
            progress = curProgress;
            uploadCallback.onProgressUpdate( progress );
          }
        }

        if( length - bytesRead < uploadBufferLength )
        {
          uploadBufferLength = (int) (length - bytesRead);
          buffer = new byte[ uploadBufferLength ];
        }
      }

      if( uploadCallback != null && progress != 100 )
        uploadCallback.onProgressUpdate( 100 );
    }
    finally
    {
      fileInputStream.close();
    }
  }
}
