package com.backendless.files;

import android.os.AsyncTask;
import android.os.PowerManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**********************************************************************************************************************
 * BACKENDLESS.COM CONFIDENTIAL
 * <p>
 * *********************************************************************************************************************
 * <p>
 * Copyright 2012 BACKENDLESS.COM. All Rights Reserved.
 * <p>
 * NOTICE:  All information contained herein is, and remains the property of Backendless.com and its suppliers,
 * if any.  The intellectual and technical concepts contained herein are proprietary to Backendless.com and its
 * suppliers and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret
 * or copyright law. Dissemination of this information or reproduction of this material is strictly forbidden
 * unless prior written permission is obtained from Backendless.com.
 * <p>
 * CREATED ON: 6/9/16
 * AT: 8:30 PM
 **********************************************************************************************************************/
public class DownloadTask extends AsyncTask<Void, Integer, String>
{
  private String url;
  private String saveLocation;

  protected DownloadTask( String url, String saveLocation )
  {
    this.url = url;
    this.saveLocation = saveLocation;
  }

  protected String doInBackground( Void... voids )
  {
    InputStream input = null;
    OutputStream output = null;
    HttpURLConnection connection = null;

    try
    {
      URL url = new URL( this.url );
      connection = (HttpURLConnection) url.openConnection();
      connection.connect();

      // expect HTTP 200 OK, so we don't mistakenly save error report
      // instead of the file
      if( connection.getResponseCode() != HttpURLConnection.HTTP_OK )
        return "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();

      // this will be useful to display download percentage
      // might be -1: server did not report the length
      int fileLength = connection.getContentLength();

      // download the file
      input = connection.getInputStream();
      output = new FileOutputStream( saveLocation );

      byte data[] = new byte[ 4096 ];
      long total = 0;
      int count;
      while( (count = input.read( data )) != -1 )
      {
        // allow canceling with back button
        if( isCancelled() )
        {
          input.close();
          return null;
        }

        total += count;
        // publishing the progress....
        if( fileLength > 0 ) // only if total length is known
          publishProgress( (int) (total * 100 / fileLength) );

        output.write( data, 0, count );
      }
    }
    catch( Exception e )
    {
      return e.toString();
    }
    finally
    {
      try
      {
        if( output != null )
          output.close();
        if( input != null )
          input.close();
      }
      catch( IOException ignored )
      {
      }

      if( connection != null )
        connection.disconnect();
    }

    return null;
  }

}