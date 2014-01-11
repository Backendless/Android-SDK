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

package com.backendless;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.UploadCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.files.BackendlessFile;
import com.backendless.files.router.FileOutputStreamRouter;
import com.backendless.files.router.IOutputStreamRouter;
import weborb.v3types.GUID;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Files
{
  protected static final String FILE_MANAGER_SERVER_ALIAS = "com.backendless.services.file.FileService";
  private static final int BUFFER_DEFAULT_LENGTH = 8192;
  private static final String SERVER_ERROR_REGEXP = "(\"message\":\"([^\"}]*)\")(,\"code\":([^\"}]*))?+";
  private static final Pattern SERVER_ERROR_PATTERN = Pattern.compile( SERVER_ERROR_REGEXP );
  private static final String SERVER_RESULT_REGEXP = "(\"fileURL\":\"([^\"[}]]*))";
  private static final Pattern SERVER_RESULT_PATTERN = Pattern.compile( SERVER_RESULT_REGEXP );
  private static final int MESSAGE_POSITION = 2;
  private static final int CODE_POSITION = 4;
  private static final Files instance = new Files();
  public final FilesAndroidExtra Android = FilesAndroidExtra.getInstance();

  private Files()
  {}

  static Files getInstance()
  {
    return instance;
  }

  public BackendlessFile upload( File file, String path ) throws Exception
  {
    return upload( file, path, new EmptyUploadCallback() );
  }

  public BackendlessFile upload( File file, String path, UploadCallback uploadCallback ) throws Exception
  {
    checkFileAndPath( file, path );

    return uploadFromStream( new FileOutputStreamRouter( file, uploadCallback ), file.getName(), path );
  }

  private void checkFileAndPath( File file, String path )
  {
    if( file == null )
      throw new NullPointerException( ExceptionMessage.NULL_FILE );

    if( path == null )
      throw new NullPointerException( ExceptionMessage.NULL_PATH );

    if( !file.exists() )
      throw new IllegalArgumentException( ExceptionMessage.WRONG_FILE );

    if( !file.canRead() )
      throw new IllegalArgumentException( ExceptionMessage.NOT_READABLE_FILE );
  }

  public BackendlessFile uploadFromStream( IOutputStreamRouter outputStreamRouter, String name,
                                           String path ) throws Exception
  {
    HttpURLConnection connection = null;
    int uploadBufferLength = BUFFER_DEFAULT_LENGTH;
    String CRLF = "\r\n";
    String boundary = (new GUID()).toString();

    try
    {
      java.net.URL url = new URL( Backendless.getUrl() + "/" + Backendless.getVersion() + "/files/" + encodeURL( path ) + "/" + encodeURL( name ) );
      connection = (HttpURLConnection) url.openConnection();
      connection.setDoOutput( true );
      connection.setDoInput( true );
      connection.addRequestProperty( "Content-Type", "multipart/form-data; boundary=" + boundary );

      for( String key : HeadersManager.getInstance().getHeaders().keySet() )
        connection.addRequestProperty( key, HeadersManager.getInstance().getHeaders().get( key ) );

      outputStreamRouter.setOutputStream( connection.getOutputStream() );
      PrintWriter writer = new PrintWriter( new OutputStreamWriter( outputStreamRouter.getOutputStream(), "UTF-8" ), true );
      writer.append( "--" ).append( boundary ).append( CRLF );
      writer.append( "Content-Disposition: form-data; name=\"file\"; filename=\"" ).append( name ).append( "\"" ).append( CRLF );
      writer.append( "Content-Type: application/octet-stream" ).append( CRLF );
      writer.append( "Content-Transfer-Encoding: binary" ).append( CRLF );
      writer.append( CRLF ).flush();

      outputStreamRouter.writeStream( uploadBufferLength );
      outputStreamRouter.flush();

      writer.append( CRLF ).flush();
      writer.append( "--" ).append( boundary ).append( "--" ).append( CRLF );
      writer.close();

      outputStreamRouter.close();

      if( connection.getResponseCode() != 200 )
      {
        Scanner scanner = new Scanner( connection.getErrorStream() );
        scanner.useDelimiter( "\\Z" );
        String response = scanner.next();
        scanner.close();

        Matcher matcher = SERVER_ERROR_PATTERN.matcher( response );
        String message = null;
        String code = null;

        while( matcher.find() )
        {
          message = matcher.group( MESSAGE_POSITION );
          code = matcher.group( CODE_POSITION );
        }

        throw new BackendlessException( code == null ? String.valueOf( connection.getResponseCode() ) : code, message );
      }
      else
      {
        Scanner scanner = new Scanner( connection.getInputStream() );
        scanner.useDelimiter( "\\Z" );
        String response = scanner.next();
        scanner.close();

        Matcher matcher = SERVER_RESULT_PATTERN.matcher( response );
        String fileUrl = null;

        while( matcher.find() )
          fileUrl = matcher.group( MESSAGE_POSITION );

        return new BackendlessFile( fileUrl );
      }
    }
    catch( MalformedURLException e )
    {
      throw new IllegalArgumentException( ExceptionMessage.FILE_UPLOAD_ERROR, e );
    }
    catch( UnsupportedEncodingException e )
    {
      throw new IllegalArgumentException( ExceptionMessage.FILE_UPLOAD_ERROR, e );
    }
    catch( IOException e )
    {
      throw new BackendlessException( ExceptionMessage.FILE_UPLOAD_ERROR, e.getMessage() );
    }
    finally
    {
      if( connection != null )
        connection.disconnect();
    }
  }

  private String encodeURL( String urlStr ) throws UnsupportedEncodingException
  {
    String[] splitedStr = urlStr.split( "/" );
    String result = "";

    for( int i = 0; i < splitedStr.length; i++ )
    {
      if( i != 0 )
        result += "/";

      result += URLEncoder.encode( splitedStr[ i ], "UTF-8" );
    }

    return result;
  }

  public void upload( File file, String path, AsyncCallback<BackendlessFile> responder )
  {
    upload( file, path, new EmptyUploadCallback(), responder );
  }

  public void upload( final File file, final String path, final UploadCallback uploadCallback,
                      final AsyncCallback<BackendlessFile> responder )
  {
    try
    {
      checkFileAndPath( file, path );
      new UploadFileAsyncTask().executeThis( file, path, uploadCallback, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public void remove( String fileUrl ) throws BackendlessException
  {
    if( fileUrl == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_PATH );

    Invoker.invokeSync( FILE_MANAGER_SERVER_ALIAS, "deleteFileOrDirectory", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), fileUrl } );
  }

  public void removeDirectory( String directoryPath ) throws BackendlessException
  {
    if( directoryPath == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_PATH );

    Invoker.invokeSync( FILE_MANAGER_SERVER_ALIAS, "deleteFileOrDirectory", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), directoryPath } );
  }

  public void remove( String fileUrl, AsyncCallback<Void> responder )
  {
    try
    {
      if( fileUrl == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_PATH );

      Invoker.invokeAsync( FILE_MANAGER_SERVER_ALIAS, "deleteFileOrDirectory", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), fileUrl }, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public void removeDirectory( String directoryPath, AsyncCallback<Void> responder )
  {
    try
    {
      if( directoryPath == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_PATH );

      Invoker.invokeAsync( FILE_MANAGER_SERVER_ALIAS, "deleteFileOrDirectory", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), directoryPath }, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  private class EmptyUploadCallback implements UploadCallback
  {
    public void onProgressUpdate( Integer progress )
    {
      //A stub. Needed for handy methods.
    }
  }
}
