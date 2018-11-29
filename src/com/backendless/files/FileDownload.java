package com.backendless.files;

import com.backendless.exceptions.BackendlessException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.*;

class FileDownload
{

  private final static String FILE_DOWNLOAD_ERROR = "Could not download a file";

  Future<File> download( final String fileURL, final String localFilePathName )
  {
    FutureTask<File> downloadTask = new FutureTask<>( new Callable<File>()
    {
      @Override
      public File call()
      {
        return downloading( fileURL, localFilePathName );
      }
    } );
    ExecutorService downloadWithLocalPathExecutor = Executors.newSingleThreadExecutor();
    downloadWithLocalPathExecutor.execute( downloadTask );
    downloadWithLocalPathExecutor.shutdown();

    return downloadTask;
  }

  Future<Void> download( final String fileURL, final OutputStream stream )
  {
    FutureTask<Void> downloadTask = new FutureTask<>( new Callable<Void>()
    {
      @Override
      public Void call()
      {
        downloading( fileURL, stream );
        return null;
      }
    } );
    ExecutorService downloadWithLocalPathExecutor = Executors.newSingleThreadExecutor();
    downloadWithLocalPathExecutor.execute( downloadTask );
    downloadWithLocalPathExecutor.shutdown();

    return downloadTask;
  }

  Future<byte[]> download( final String fileURL )
  {
    FutureTask<byte[]> downloadTask = new FutureTask<>( new Callable<byte[]>()
    {
      @Override
      public byte[] call()
      {
        return downloading( fileURL );
      }
    } );
    ExecutorService downloadWithLocalPathExecutor = Executors.newSingleThreadExecutor();
    downloadWithLocalPathExecutor.execute( downloadTask );
    downloadWithLocalPathExecutor.shutdown();

    return downloadTask;
  }

  private byte[] downloading( String fileURL )
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    downloading( fileURL, out );

    return out.toByteArray();
  }

  private File downloading( String fileURL, String localFilePathName )
  {
    File file = new File( localFilePathName );

    BufferedOutputStream out;
    try
    {
      out = new BufferedOutputStream( new FileOutputStream( file ) );
    }
    catch( FileNotFoundException e )
    {
      throw new BackendlessException( FILE_DOWNLOAD_ERROR, e.getMessage() );
    }
    downloading( fileURL, out );

    return file;
  }

  private void downloading( String fileURL, OutputStream out )
  {
    InputStream in = null;
    try
    {
      URL url = new URL( fileURL );

      in = new BufferedInputStream( url.openStream() );

      int count;
      byte[] buffer = new byte[ 4096 ];
      while( ( count = in.read( buffer ) ) > 0 )
      {
        out.write( buffer, 0, count );
      }
    }
    catch( MalformedURLException e )
    {
      throw new IllegalArgumentException( FILE_DOWNLOAD_ERROR, e );
    }
    catch( IOException e )
    {
      throw new BackendlessException( FILE_DOWNLOAD_ERROR, e.getMessage() );
    }
    finally
    {
      if( in != null )
      {
        try
        {
          in.close();
        }
        catch( IOException e )
        {
          throw new BackendlessException( FILE_DOWNLOAD_ERROR, e.getMessage() );
        }
      }
      if( out != null )
      {
        try
        {
          out.close();
        }
        catch( IOException e )
        {
          throw new BackendlessException( FILE_DOWNLOAD_ERROR, e.getMessage() );
        }
      }
    }
  }

}
