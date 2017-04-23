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

package com.backendless.files;

import android.app.ProgressDialog;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import java.io.*;
import java.net.*;
import java.util.*;

public class BackendlessFile
{
  private final static int bufferSize = 4 * 1024; //4KB
  private static boolean isAutoDownloadEnabled;
  private final File autoDownloadFile = (isAutoDownloadEnabled) ? new File( "/data/local/tmp/" + UUID.randomUUID().toString() ) : null;
  private boolean isDownloaded;
  private String fileURL;
  private android.app.ProgressDialog globalProgressDial;
  private long contentLength = -1;

  public BackendlessFile( String fileURL )
  {
    this.fileURL = fileURL;
    if( isAutoDownloadEnabled )
    {
      new Thread( new Runnable()
      {
        @Override
        public void run()
        {
          try
          {
            synchronized( autoDownloadFile ) // to lock other thread until file is downloaded
            {
              download( new FileOutputStream( autoDownloadFile ), (ProgressDialog) null );
            }
          }
          catch( IOException e )
          {
            e.printStackTrace();
          }
        }
      } ).start();
    }
  }

  public static void autoDownload( boolean enable )
  {
    isAutoDownloadEnabled = enable;
  }

  private InputStream getFileInputStream() throws IOException
  {
    if( isAutoDownloadEnabled )
    {
      synchronized( autoDownloadFile )
      {
      } // wait other thread to read from src
      return new FileInputStream( autoDownloadFile );
    }

    URL url = new URL( fileURL );
    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

    if( httpConn.getResponseCode() != 200 )
    {
      throw new InvalidObjectException( "Response code is invalid" );
    }
    if( contentLength < 0 )
    {
      contentLength = Long.parseLong( httpConn.getHeaderField( "Content-Length" ) );
    }
    return httpConn.getInputStream();
  }

  private void download( OutputStream outputStream, ProgressDialog pd ) throws IOException
  {
    byte[] data = new byte[ bufferSize ];
    int resp;
    InputStream input = getFileInputStream();

    while( (resp = input.read( data )) > 0 )
    {
      if( resp == bufferSize )
      {
        outputStream.write( data );

        if( pd != null )
        {
          pd.incrementProgressBy( bufferSize / 1024 );
        }

        if ( globalProgressDial != null) {
          pd = globalProgressDial;
        }
      }

      byte[] tmp = Arrays.copyOf( data, resp );
      outputStream.write( tmp );

      if (isAutoDownloadEnabled)
      {
        isDownloaded = true;
      }
    }
  }

  public void download( OutputStream outputStream, android.app.ProgressDialog pd, AsyncCallback<Void> callback )
  {
    if (isAutoDownloadEnabled && !isDownloaded) {
      this.globalProgressDial = pd;
    }

    try
    {
      pd.setMax( (int) (contentLength / 1024) );
      download( outputStream, pd );
      callback.handleResponse( null );
    }
    catch( IOException e )
    {
      callback.handleFault( new BackendlessFault( e.getMessage() ) );
    }
  }

  public void download( OutputStream outputStream, AsyncCallback<Void> callback )
  {
    download( outputStream, null, callback );
  }

  public void download( String localFilePathName, android.app.ProgressDialog pd,
                        AsyncCallback<File> callback )
  {
    if (isAutoDownloadEnabled && !isDownloaded) {
      this.globalProgressDial = pd;
    }

    File file = new File( localFilePathName );

    try
    {
      if( !file.exists() )
      {
        file.mkdirs();
        file.createNewFile();
      }
      OutputStream fileOutputStream = new FileOutputStream( file );
      pd.setMax( (int) (contentLength / 1024) );
      download( fileOutputStream, pd );
      callback.handleResponse( file );
    }
    catch( IOException e )
    {
      callback.handleFault( new BackendlessFault( e.getMessage() ) );
    }
  }

  public void download( String localFilePathName, AsyncCallback<File> callback )
  {
    download( localFilePathName, null, callback );
  }

  public void download( AsyncCallback<byte[]> callback, android.app.ProgressDialog pd )
  {
    if (isAutoDownloadEnabled && !isDownloaded) {
      this.globalProgressDial = pd;
    }
    try
    {
      InputStream inputStream = getFileInputStream();
      pd.setMax( (int) (contentLength / 1024) );
      File file = new File( fileURL );

      if( contentLength > Integer.MAX_VALUE )
      {
        callback.handleFault( new BackendlessFault( "This file is larger than 2 GB. Please use different method" ) );
        return;
      }

      byte[] ret = new byte[ (int) contentLength ];
      int part;
      byte[] data = new byte[ bufferSize ];
      int res = bufferSize;

      for( int i = 0; res == bufferSize; i++ )
      {
        res = inputStream.read( data );
        System.arraycopy( data, 0, ret, bufferSize * i, res );
        pd.incrementProgressBy( bufferSize / 1024 );
      }
      callback.handleResponse( data );
    }
    catch( IOException e )
    {
      callback.handleFault( new BackendlessFault( e.getMessage() ) );
    }
  }

  public void download( AsyncCallback<byte[]> callback )
  {
    download( callback, null );
  }

  public void remove() throws BackendlessException
  {
    Backendless.Files.remove( fileURL );
  }

  public void remove( AsyncCallback<Void> responder )
  {
    Backendless.Files.remove( fileURL, responder );
  }

  public String getFileURL()
  {
    return fileURL;
  }

  public BackendlessFile setFileURL( String fileURL )
  {
    this.fileURL = fileURL;
    return this;
  }
}
