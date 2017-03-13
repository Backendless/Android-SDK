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
import com.backendless.HeadersManager;
import com.backendless.ThreadPoolService;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;

import com.backendless.persistence.local.UserTokenStorageFactory;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BackendlessFile
{
  private static final int BUFFER_SIZE = 64 * 1024;
  private static final int TIMEOUT = 5000;
  private static final int MAX_PROGRESS_DIALOG = 10000;

  private static boolean autoDownloadEnabled = true;

  private String fileURL;
  private boolean isAutoDownloadComplete = false;
  private boolean isAutoDownloadInProcess = false;
  private Long downLoadProgress;
  private byte[] autoDownloadedData;

  private boolean userCancelsDownload = false;

  public BackendlessFile( String fileURL )
  {
    this.fileURL = fileURL;
  }

  public void setFileURL( String fileURL )
  {
    this.fileURL = fileURL;
    if( autoDownloadEnabled )
    {
      isAutoDownloadComplete = false;
      asyncAutoDownloadData();
    }
  }

  public String getFileURL()
  {
    return fileURL;
  }

  public void remove() throws BackendlessException
  {
    Backendless.Files.remove( fileURL );
  }

  public void remove( AsyncCallback<Void> responder )
  {
    Backendless.Files.remove( fileURL, responder );
  }

  public static void autoDownload( boolean enable )
  {
    autoDownloadEnabled = enable;
  }

  public void download(final OutputStream stream, final AsyncCallback<Void> asyncCallback )
  {
    download( stream, null, asyncCallback );
  }

  public void download( final OutputStream stream, final android.app.ProgressDialog progressDialog,
                        final AsyncCallback<Void> asyncCallback )
  {

    if( stream == null )
      asyncCallbackFaultOrThrowException( asyncCallback, ExceptionMessage.NULL_STREAM);

    setListenerForProgressDialog( progressDialog );
    asyncDownloadToStream( stream, asyncCallback, progressDialog );
  }

  public void download( final String localFilePathName, final AsyncCallback<File> asyncCallback )
  {
    download( localFilePathName, null, asyncCallback );
  }

  public void download( String localFilePathName, android.app.ProgressDialog progressDialog,
                        AsyncCallback<File> asyncCallback )
  {
    checkFilePathName( localFilePathName, asyncCallback );
    setListenerForProgressDialog( progressDialog );
    asyncDownloadToFile( localFilePathName, asyncCallback, progressDialog );
  }

  public void download( final AsyncCallback<byte[]> asyncCallback )
  {
    download( null, asyncCallback );
  }

  public void download( android.app.ProgressDialog progressDialog, AsyncCallback<byte[]> asyncCallback )
  {
    setListenerForProgressDialog( progressDialog );
    asyncDownloadToByteArray( asyncCallback, progressDialog );
  }

  private void asyncDownloadToStream( final OutputStream outputStream, final AsyncCallback<Void> asyncCallback,
                                      final android.app.ProgressDialog progressDialog )
  {

    if( autoDownloadEnabled )
    {

      if( !isAutoDownloadComplete )
        waitForAutoDownload( asyncCallback, progressDialog );

      downloadToStreamFromMemory( outputStream, asyncCallback );

    }
    else
    {

      try
      {
        ThreadPoolService.getPoolExecutor().execute( new Runnable()
        {
          @Override
          public void run()
          {
            checkInternetConnection( asyncCallback );
            downloadToStream( outputStream, asyncCallback, progressDialog );
          }
        } );
      }
      catch( Exception e )
      {
        asyncCallbackFaultOrThrowException( asyncCallback, ExceptionMessage.ASYNC_DOWNLOAD_ERROR + e.getMessage() );
      }
    }
  }

  private void asyncDownloadToFile( final String localFilePathName, final AsyncCallback<File> asyncCallback,
                                    final android.app.ProgressDialog progressDialog )
  {
    final File outputFile = new File( localFilePathName );

    if( autoDownloadEnabled )
    {

      if( !isAutoDownloadComplete )
        waitForAutoDownload( asyncCallback, progressDialog );

      downloadToFileFromMemory( outputFile, asyncCallback );
      asyncCallback.handleResponse( outputFile );
    }
    else
    {

      try
      {
        ThreadPoolService.getPoolExecutor().execute( new Runnable()
        {
          @Override
          public void run()
          {
            checkInternetConnection( asyncCallback );
            downloadToFile( outputFile, asyncCallback, progressDialog );
            asyncCallback.handleResponse( outputFile );
          }
        } );
      }
      catch( Exception e )
      {
        asyncCallbackFaultOrThrowException( asyncCallback, ExceptionMessage.ASYNC_DOWNLOAD_ERROR + e.getMessage() );
      }

    }
  }

  private void asyncDownloadToByteArray( final AsyncCallback<byte[]> asyncCallback,
                                         final android.app.ProgressDialog progressDialog )
  {

    if( autoDownloadEnabled )
    {

      if( !isAutoDownloadComplete )
        waitForAutoDownload( asyncCallback, progressDialog );

      byte[] bytes = autoDownloadedData;
      asyncCallback.handleResponse( bytes );
    }

    try
    {
      ThreadPoolService.getPoolExecutor().execute( new Runnable()
      {
        @Override
        public void run()
        {
          checkInternetConnection( asyncCallback );

          if( isAutoDownloadComplete )
            asyncCallback.handleResponse( autoDownloadedData );
          else
          {
            byte[] bytes = downloadToByteArray( asyncCallback, progressDialog );
            asyncCallback.handleResponse( bytes );
          }
        }
      } );
    }
    catch( Exception e)
    {
      asyncCallbackFaultOrThrowException( asyncCallback, ExceptionMessage.ASYNC_DOWNLOAD_ERROR + e.getMessage() );
    }

  }

  private <T> void waitForAutoDownload( AsyncCallback<T> asyncCallback, ProgressDialog progressDialog )
  {
    while( isAutoDownloadInProcess )
    {
      if( progressDialog != null)
        progressDialog.setProgress( downLoadProgress.intValue() );

      try
      {
        Thread.sleep( 200L );
      }
      catch( InterruptedException e )
      {
        asyncCallbackFaultOrThrowException( asyncCallback, ExceptionMessage.INTERRUPTED_WHILE_AUTO_DOWNLOAD +
                e.getMessage() );
      }
    }
  }

  private void setListenerForProgressDialog( final android.app.ProgressDialog progressDialog )
  {

    if( progressDialog != null )
    {
      progressDialog.setOnDismissListener( new android.content.DialogInterface.OnDismissListener()
      {
        @Override
        public void onDismiss( android.content.DialogInterface dialogInterface )
        {
          userCancelsDownload = true;
        }
      } );
    }

  }

  private void downloadToStream(final OutputStream outputStream, final AsyncCallback<Void> asyncCallback,
                                final android.app.ProgressDialog progressDialog )
  {
    HttpURLConnection urlConnection = null;

    try
    {
      urlConnection = getHttpURLConnection( asyncCallback );
      long fileSize = checkAvailableMemory( urlConnection );

      try (InputStream inputStream = new BufferedInputStream( urlConnection.getInputStream(), BUFFER_SIZE ))
      {
        readAndWrite( inputStream, outputStream, progressDialog, fileSize );
        outputStream.flush();
      }
      catch (BackendlessException e)
      {
        outputStream.close();
      }
    }
    catch( IOException e )
    {
      asyncCallbackFaultOrThrowException( asyncCallback, ExceptionMessage.FILE_DOWNLOAD_ERROR_MESSAGE + e.getMessage() );
    }
    finally
    {

      if( urlConnection != null )
        urlConnection.disconnect();

    }
  }

  private void downloadToFile( final File outputFile, final AsyncCallback<File> asyncCallback,
                               final android.app.ProgressDialog progressDialog )
  {
    HttpURLConnection urlConnection = null;

    try
    {
      urlConnection = getHttpURLConnection( asyncCallback );
      long fileSize = urlConnection.getContentLengthLong();

      try (InputStream inputStream = new BufferedInputStream( urlConnection.getInputStream(), BUFFER_SIZE );
           OutputStream outputStream = new BufferedOutputStream( new FileOutputStream( outputFile ) ))
      {
        readAndWrite( inputStream, outputStream, progressDialog, fileSize );
      }
      catch (BackendlessException e)
      {
        outputFile.delete();
      }

    }
    catch( IOException e )
    {
      asyncCallbackFaultOrThrowException( asyncCallback, ExceptionMessage.FILE_DOWNLOAD_ERROR_MESSAGE + e.getMessage() );
    }
    finally
    {

      if( urlConnection != null )
        urlConnection.disconnect();

    }
  }

  private byte[] downloadToByteArray( final AsyncCallback<byte[]> asyncCallback,
                                      final android.app.ProgressDialog progressDialog )
  {
    HttpURLConnection urlConnection = null;
    ByteArrayOutputStream outputStream = null;
    byte[] result = new byte[0];

    try
    {
      urlConnection = getHttpURLConnection( asyncCallback );
      long fileSize = checkAvailableMemory( urlConnection );

      if( fileSize > Integer.MAX_VALUE )
      {
        throw new IOException( ExceptionMessage.FILE_IS_TOO_BIG );
      }

      try (InputStream inputStream = new BufferedInputStream( urlConnection.getInputStream(), BUFFER_SIZE ))
      {
        outputStream = new ByteArrayOutputStream();
        readAndWrite( inputStream, outputStream, progressDialog, fileSize );
      }
      catch( IOException e)
      {

        if( outputStream != null )
        {
          outputStream.flush();
          outputStream.close();
        }

        asyncCallbackFaultOrThrowException( asyncCallback, ExceptionMessage.FILE_DOWNLOAD_ERROR_MESSAGE + e.getMessage() );
      }

      if( outputStream != null )
      {
        result = outputStream.toByteArray();
        outputStream.close();
      }

    }
    catch( IOException e )
    {
      asyncCallbackFaultOrThrowException( asyncCallback, ExceptionMessage.FILE_DOWNLOAD_ERROR_MESSAGE + e.getMessage() );
    }
    finally
    {

      if( urlConnection != null )
        urlConnection.disconnect();

    }
    return result;
  }

  private void readAndWrite( InputStream inputStream, OutputStream outputStream,
                             final android.app.ProgressDialog progressDialog, long fileSize )
          throws IOException, BackendlessException
  {

    if( progressDialog == null )
      readAndWrite( inputStream, outputStream, fileSize );
    else
    {
      long totalRead = 0;
      int bytesRead;
      byte[] chunk = new byte[ BUFFER_SIZE ];
      userCancelsDownload = false;

      while( ( bytesRead = inputStream.read( chunk ) ) > 0 && !userCancelsDownload )
      {
        outputStream.write( chunk, 0, bytesRead );
        totalRead += bytesRead;
        downLoadProgress = MAX_PROGRESS_DIALOG * totalRead / fileSize;
        progressDialog.setProgress( downLoadProgress.intValue() );
      }

      if( userCancelsDownload )
        throw new BackendlessException( ExceptionMessage.USER_CANCELS_DOWNLOAD );

    }

  }

  private void readAndWrite( InputStream inputStream, OutputStream outputStream, long fileSize )
          throws IOException, BackendlessException
  {
    long totalRead = 0;
    int bytesRead;
    byte[] chunk = new byte[ BUFFER_SIZE ];

    while( ( bytesRead = inputStream.read( chunk ) ) > 0 )
    {
      outputStream.write( chunk, 0, bytesRead );
      totalRead += bytesRead;
      downLoadProgress = MAX_PROGRESS_DIALOG * totalRead / fileSize;

      if( isAutoDownloadInProcess && !BackendlessFile.autoDownloadEnabled )
        throw new BackendlessException( ExceptionMessage.AUTODOWNLOAD_ABORTED );
    }
  }

  private <T> HttpURLConnection getHttpURLConnection( AsyncCallback<T> asyncCallback ) throws IOException
  {
    URL url = new URL( fileURL );
    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    urlConnection.setConnectTimeout( TIMEOUT );
    urlConnection.setReadTimeout( TIMEOUT );

    String userToken = UserTokenStorageFactory.instance().getStorage().get();

    if ( userToken != null && !userToken.isEmpty())
      urlConnection.setRequestProperty( HeadersManager.HeadersEnum.USER_TOKEN_KEY.name(), userToken );

    urlConnection.connect();

    if( urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK )
      asyncCallbackFaultOrThrowException( asyncCallback, ExceptionMessage.SERVER_RETURNED_HTTP +
              urlConnection.getResponseCode() + " " + urlConnection.getResponseMessage() );

    return urlConnection;
  }

  private <T> void checkFilePathName( String localFilePathName, AsyncCallback<T> asyncCallback )
  {

    if( localFilePathName == null || localFilePathName.isEmpty() )
      asyncCallbackFaultOrThrowException( asyncCallback, ExceptionMessage.NULL_NAME );

    checkWriteToFilePathNameIsPossible( localFilePathName, asyncCallback );
  }

  private <T> void checkWriteToFilePathNameIsPossible( String localFilePathName, AsyncCallback<T> asyncCallback )
  {
    File parentFile = new File( localFilePathName ).getParentFile();

    if( !parentFile.exists() || !parentFile.isDirectory() || !parentFile.canWrite() )
      asyncCallbackFaultOrThrowException( asyncCallback, ExceptionMessage.UNABLE_DOWNLOAD_TO_DIRECTORY );

  }

  private <T> void asyncCallbackFaultOrThrowException( AsyncCallback<T> asyncCallback, String exceptionMessage )
  {

    if( asyncCallback != null )
      asyncCallback.handleFault( new BackendlessFault( exceptionMessage ) );
    else
      throw new BackendlessException( exceptionMessage );

  }

  private <T> void checkInternetConnection( final AsyncCallback<T> asyncCallback )
  {

    try
    {
      URL url = new URL( Backendless.getUrl() );
      HttpURLConnection connection = ( HttpURLConnection ) url.openConnection();
      connection.setConnectTimeout( TIMEOUT );
      connection.connect();
      if( connection.getResponseCode() != HttpURLConnection.HTTP_OK )
        throw new IOException();
    }
    catch( IOException e)
    {
      asyncCallbackFaultOrThrowException( asyncCallback, ExceptionMessage.INTERNET_CONNECTION_IS_NOT_AVAILABLE );
    }

  }

  private long checkAvailableMemory( HttpURLConnection urlConnection )
  {
    long fileSize = urlConnection.getContentLengthLong();
    long freeMemory = Runtime.getRuntime().freeMemory();

    if( freeMemory < fileSize )
      throw new BackendlessException( ExceptionMessage.NOT_ENOUGH_MEMORY );

    return fileSize;
  }

  private void asyncAutoDownloadData()
  {

    try
    {
      ThreadPoolService.getPoolExecutor().execute( new Runnable()
      {
        @Override
        public void run()
        {
          checkInternetConnection( null );
          isAutoDownloadInProcess = true;
          autoDownloadedData = downloadToByteArray( null, null );
          isAutoDownloadInProcess = false;
          isAutoDownloadComplete = true;
        }
      } );
    }
    catch( Exception e )
    {
      asyncCallbackFaultOrThrowException( null, ExceptionMessage.ASYNC_DOWNLOAD_ERROR + e.getMessage() );
    }

  }

  private void downloadToFileFromMemory( File outputFile, AsyncCallback<File> asyncCallback )
  {

    try( OutputStream outputStream = new BufferedOutputStream( new FileOutputStream( outputFile ), BUFFER_SIZE ) )
    {
      downloadToStreamFromMemory( outputStream, asyncCallback );
      outputStream.flush();
    }
    catch( IOException e )
    {
      asyncCallbackFaultOrThrowException( asyncCallback, ExceptionMessage.FILE_DOWNLOAD_ERROR_MESSAGE + e.getMessage() );
    }

  }

  private <T> void downloadToStreamFromMemory( OutputStream outputStream, AsyncCallback<T> asyncCallback )
  {

    try( InputStream inputStream = new ByteArrayInputStream( autoDownloadedData ) )
    {
      readFromMemory( inputStream, outputStream );
    }
    catch( IOException e )
    {
      asyncCallbackFaultOrThrowException( asyncCallback, ExceptionMessage.FILE_DOWNLOAD_ERROR_MESSAGE + e.getMessage() );
    }

  }

  private void readFromMemory( InputStream inputStream, OutputStream outputStream ) throws IOException
  {
    int bytesRead;
    byte[] chunk = new byte[ BUFFER_SIZE ];

    while( ( bytesRead = inputStream.read( chunk ) ) > 0 )
    {
      outputStream.write( chunk, 0, bytesRead );
    }
  }


}