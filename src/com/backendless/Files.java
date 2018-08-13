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
import com.backendless.core.responder.AdaptingResponder;
import com.backendless.core.responder.policy.CollectionAdaptingPolicy;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.files.BackendlessFile;
import com.backendless.files.BackendlessFilesQuery;
import com.backendless.files.FileInfo;
import com.backendless.files.router.FileOutputStreamRouter;
import com.backendless.files.router.OutputStreamRouter;
import com.backendless.files.security.FileRolePermission;
import com.backendless.files.security.FileUserPermission;
import com.backendless.utils.StringUtils;
import weborb.types.Types;
import weborb.v3types.GUID;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Files
{
  private static final int DEFAULT_CHUNK_SIZE = 64 * 1024;
  private static final String OVERWRITE_PARAMETER_NAME = "overwrite";
  protected static final String FILE_MANAGER_SERVER_ALIAS = "com.backendless.services.file.FileService";
  private static final String SERVER_ERROR_REGEXP = "(\"message\":\"([^\"}]*)\")(,\"code\":([^\"}]*))?+";
  private static final Pattern SERVER_ERROR_PATTERN = Pattern.compile( SERVER_ERROR_REGEXP );
  private static final String SERVER_RESULT_REGEXP = "(\"fileURL\":\"([^\"[}]]*))";
  private static final Pattern SERVER_RESULT_PATTERN = Pattern.compile( SERVER_RESULT_REGEXP );
  private static final int MESSAGE_POSITION = 2;
  private static final int CODE_POSITION = 4;
  private static final Files instance = new Files();
  public final FilesAndroidExtra Android = FilesAndroidExtra.getInstance();

  private Files()
  {
    Types.addClientClassMapping( "com.backendless.services.file.permissions.FileRolePermission", FileRolePermission.class );
    Types.addClientClassMapping( "com.backendless.services.file.permissions.FileUserPermission", FileUserPermission.class );
    Types.addClientClassMapping( "com.backendless.management.files.FileInfo", FileInfo.class );
  }

  static Files getInstance()
  {
    return instance;
  }

  public BackendlessFile upload( File file, String path ) throws Exception
  {
    return upload( file, path, false );
  }

  public BackendlessFile upload( File file, String path, boolean overwrite ) throws Exception
  {
    return upload( file, path, overwrite, new EmptyUploadCallback() );
  }

  public BackendlessFile upload( File file, String path, UploadCallback uploadCallback ) throws Exception
  {
    return upload( file, path, false, uploadCallback );
  }

  public BackendlessFile upload( File file, String path, boolean overwrite,
                                 UploadCallback uploadCallback ) throws Exception
  {
    checkFileAndPath( file, path );

    return uploadFromStream( new FileOutputStreamRouter( file, uploadCallback ), file.getName(), path, overwrite );
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

  public BackendlessFile uploadFromStream( OutputStreamRouter outputStreamRouter, String name,
                                           String path ) throws Exception
  {
    return uploadFromStream( outputStreamRouter, name, path, false );
  }

  public BackendlessFile uploadFromStream( OutputStreamRouter outputStreamRouter, String name,
                                           String path, boolean overwrite ) throws Exception
  {
    HttpURLConnection connection = null;
    String CRLF = "\r\n";
    String boundary = (new GUID()).toString();

    try
    {
      String urlStr = Backendless.getUrl() + '/' + Backendless.getApplicationId() + '/' + Backendless.getSecretKey() + "/files/" + encodeURL( path ) + "/" + encodeURL( name );

      if( overwrite )
        urlStr = urlStr + "?" + OVERWRITE_PARAMETER_NAME + "=" + overwrite;

      java.net.URL url = new URL( urlStr );
      connection = (HttpURLConnection) url.openConnection();
      connection.setChunkedStreamingMode( DEFAULT_CHUNK_SIZE );
      connection.setDoOutput( true );
      connection.setDoInput( true );
      connection.addRequestProperty( "Content-Type", "multipart/form-data; boundary=" + boundary );

      for( String key : HeadersManager.getInstance().getHeaders().keySet() )
        connection.addRequestProperty( key, HeadersManager.getInstance().getHeaders().get( key ) );

      try ( OutputStream outputStream = connection.getOutputStream();
            PrintWriter writer = new PrintWriter( new OutputStreamWriter( outputStream, "UTF-8" ), true ) )
      {
        writer.append( "--" ).append( boundary ).append( CRLF );
        writer.append( "Content-Disposition: form-data; name=\"file\"; filename=\"" ).append( name ).append( "\"" ).append( CRLF );
        writer.append( "Content-Type: application/octet-stream" ).append( CRLF );
        writer.append( "Content-Transfer-Encoding: binary" ).append( CRLF );
        writer.append( CRLF ).flush();

        outputStreamRouter.writeStream( outputStream );
        outputStream.flush();

        writer.append( CRLF ).flush();
        writer.append( "--" ).append( boundary ).append( "--" ).append( CRLF );
      }

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
    upload( file, path, false, responder );
  }

  public void upload( File file, String path, boolean overwrite, AsyncCallback<BackendlessFile> responder )
  {
    upload( file, path, overwrite, new EmptyUploadCallback(), responder );
  }

  public void upload( final File file, final String path, final UploadCallback uploadCallback,
                      final AsyncCallback<BackendlessFile> responder )
  {
    upload( file, path, false, uploadCallback, responder );
  }

  public void upload( final File file, final String path, boolean overwrite, final UploadCallback uploadCallback,
                      final AsyncCallback<BackendlessFile> responder )
  {
    try
    {
      checkFileAndPath( file, path );
      new UploadFileAsyncTask().executeThis( file, path, overwrite, uploadCallback, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public int remove( String fileUrl ) throws BackendlessException
  {
    return this.removeDirectory( fileUrl );
  }

  public int removeDirectory( String directoryPath ) throws BackendlessException
  {
    return this.removeDirectory( directoryPath, "*", true );
  }

  public int removeDirectory( String directoryPath, String pattern, boolean recursive ) throws BackendlessException
  {
    if( directoryPath == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_PATH );

    return Invoker.invokeSync( FILE_MANAGER_SERVER_ALIAS, "deleteFileOrDirectory", new Object[] { directoryPath, pattern, recursive } );
  }

  public void remove( String fileUrl, AsyncCallback<Integer> responder )
  {
    this.removeDirectory( fileUrl, responder );
  }

  public void removeDirectory( String directoryPath, AsyncCallback<Integer> responder )
  {
    this.removeDirectory( directoryPath, "*", true, responder );
  }

  public void removeDirectory( String directoryPath, String pattern, boolean recursive, AsyncCallback<Integer> responder )
  {
    try
    {
      if( directoryPath == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_PATH );

      Invoker.invokeAsync( FILE_MANAGER_SERVER_ALIAS, "deleteFileOrDirectory", new Object[] { directoryPath, "*", true }, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public String saveFile( String path, String fileName, byte[] fileContent )
  {
    return Invoker.invokeSync( FILE_MANAGER_SERVER_ALIAS, "saveFile", new Object[] { path, fileName, fileContent } );
  }

  public String saveFile( String path, String fileName, byte[] fileContent, boolean overwrite )
  {
    return Invoker.invokeSync( FILE_MANAGER_SERVER_ALIAS, "saveFile", new Object[] { path, fileName, fileContent, overwrite } );
  }

  public String saveFile( String filePathName, byte[] fileContent, boolean overwrite )
  {
    return Invoker.invokeSync( FILE_MANAGER_SERVER_ALIAS, "saveFile", new Object[] { filePathName, fileContent, overwrite } );
  }

  //Async methods
  public void saveFile( String path, String fileName, byte[] fileContent, AsyncCallback<String> responder )
  {
    Invoker.invokeAsync( FILE_MANAGER_SERVER_ALIAS, "saveFile", new Object[] { path, fileName, fileContent }, responder );
  }

  public void saveFile( String path, String fileName, byte[] fileContent, boolean overwrite,
                        AsyncCallback<String> responder )
  {
    Invoker.invokeAsync( FILE_MANAGER_SERVER_ALIAS, "saveFile", new Object[] { path, fileName, fileContent, overwrite }, responder );
  }

  public void saveFile( String filePathName, byte[] fileContent, boolean overwrite, AsyncCallback<String> responder )
  {
    Invoker.invokeAsync( FILE_MANAGER_SERVER_ALIAS, "saveFile", new Object[] { filePathName, fileContent, overwrite }, responder );
  }

  public String renameFile( String oldPathName, String newName ) throws BackendlessException
  {
    if( oldPathName == null || oldPathName.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_PATH );
    if( newName == null || newName.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_NAME );

    return Invoker.invokeSync( FILE_MANAGER_SERVER_ALIAS, "renameFile", new Object[] { oldPathName, newName } );
  }

  public void renameFile( String oldPathName, String newName,
                          AsyncCallback<String> responder ) throws BackendlessException
  {
    if( oldPathName == null || oldPathName.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_PATH );
    if( newName == null || newName.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_NAME );

    Invoker.invokeAsync( FILE_MANAGER_SERVER_ALIAS, "renameFile", new Object[] { oldPathName, newName }, responder );
  }

  public String copyFile( String sourcePathName, String targetPath ) throws BackendlessException
  {
    if( sourcePathName == null || sourcePathName.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_PATH );
    if( targetPath == null || targetPath.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_NAME );

    return Invoker.invokeSync( FILE_MANAGER_SERVER_ALIAS, "copyFile", new Object[] { sourcePathName, targetPath } );
  }

  public void copyFile( String sourcePathName, String targetPath,
                        AsyncCallback<String> responder ) throws BackendlessException
  {
    if( sourcePathName == null || sourcePathName.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_PATH );
    if( targetPath == null || targetPath.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_NAME );

    Invoker.invokeAsync( FILE_MANAGER_SERVER_ALIAS, "copyFile", new Object[] { sourcePathName, targetPath }, responder );
  }

  public String moveFile( String sourcePathName, String targetPath ) throws BackendlessException
  {
    if( sourcePathName == null || sourcePathName.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_PATH );
    if( targetPath == null || targetPath.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_NAME );

    return Invoker.invokeSync( FILE_MANAGER_SERVER_ALIAS, "moveFile", new Object[] { sourcePathName, targetPath } );
  }

  public void moveFile( String sourcePathName, String targetPath,
                        AsyncCallback<String> responder ) throws BackendlessException
  {
    if( sourcePathName == null || sourcePathName.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_PATH );
    if( targetPath == null || targetPath.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_NAME );

    Invoker.invokeAsync( FILE_MANAGER_SERVER_ALIAS, "moveFile", new Object[] { sourcePathName, targetPath }, responder );
  }

  public List<FileInfo> listing( String path )
  {
    return listing( path, "*", false );
  }

  public List<FileInfo> listing( String path, String pattern, boolean recursive )
  {
    return listing( path, pattern, recursive, BackendlessFilesQuery.DEFAULT_PAGE_SIZE, BackendlessFilesQuery.DEFAULT_OFFSET );
  }

  public List<FileInfo> listing( String path, String pattern, boolean recursive, int pagesize,
                                                  int offset )
  {
    List<FileInfo> fileInfoList = Invoker.invokeSync( FILE_MANAGER_SERVER_ALIAS, "listing", new Object[] { path, pattern, recursive, pagesize, offset }, new AdaptingResponder<FileInfo>( FileInfo.class, new CollectionAdaptingPolicy<FileInfo>() ) );
    return fileInfoList;
  }

  public void listing( String path, AsyncCallback<List<FileInfo>> responder )
  {
    listing( path, "*", false, responder );
  }

  public void listing( String path, String pattern, boolean recursive,
                       AsyncCallback<List<FileInfo>> responder )
  {
    listing( path, pattern, recursive, BackendlessFilesQuery.DEFAULT_PAGE_SIZE, BackendlessFilesQuery.DEFAULT_OFFSET, responder );
  }

  public void listing( final String path, final String pattern, final boolean recursive, final int pagesize,
                       final int offset, final AsyncCallback<List<FileInfo>> responder )
  {
    Invoker.invokeAsync( FILE_MANAGER_SERVER_ALIAS, "listing", new Object[] { path, pattern, recursive, pagesize, offset }, new AsyncCallback<List<FileInfo>>()
    {
      @Override
      public void handleResponse( List<FileInfo> response )
      {
        if( responder != null )
          responder.handleResponse( response );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        if( responder != null )
          responder.handleFault( fault );
      }
    }, new AdaptingResponder<FileInfo>( FileInfo.class, new CollectionAdaptingPolicy<FileInfo>() ) );
  }

  public int getFileCount( String path, String pattern, boolean recursive, boolean countDirectories )
  {
    return Invoker.invokeSync( FILE_MANAGER_SERVER_ALIAS, "count", new Object[] { path, pattern, recursive, countDirectories } );
  }

  public int getFileCount( String path, String pattern, boolean recursive )
  {
    return getFileCount( path, pattern, recursive, false );
  }

  public int getFileCount( String path, String pattern )
  {
    return getFileCount( path, pattern, false );
  }

  public int getFileCount( String path )
  {
    return getFileCount( path, "*" );
  }

  public void getFileCount( String path, String pattern, boolean recursive, boolean countDirectories,
                            AsyncCallback<Integer> responder )
  {
    try
    {
      Invoker.invokeAsync( FILE_MANAGER_SERVER_ALIAS, "count", new Object[] { path, pattern, recursive, countDirectories }, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public void getFileCount( String path, String pattern, boolean recursive, AsyncCallback<Integer> responder )
  {
    getFileCount( path, pattern, recursive, false, responder );
  }

  public void getFileCount( String path, String pattern, AsyncCallback<Integer> responder )
  {
    getFileCount( path, pattern, false, responder );
  }

  public void getFileCount( String path, AsyncCallback<Integer> responder )
  {
    getFileCount( path, "*", responder );
  }

  public boolean exists( String path )
  {
    StringUtils.checkEmpty( path, ExceptionMessage.NULL_PATH );
    return Invoker.<Boolean>invokeSync( FILE_MANAGER_SERVER_ALIAS, "exists", new Object[] { path } );
  }

  public void exists( String path, AsyncCallback<Boolean> responder )
  {
    StringUtils.checkEmpty( path, ExceptionMessage.NULL_PATH );
    Invoker.invokeAsync( FILE_MANAGER_SERVER_ALIAS, "exists", new Object[] { path }, responder );
  }

  private class EmptyUploadCallback implements UploadCallback
  {
    public void onProgressUpdate( Integer progress )
    {
      //A stub. Needed for handy methods.
    }
  }
}
