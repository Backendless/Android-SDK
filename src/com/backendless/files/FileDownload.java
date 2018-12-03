package com.backendless.files;

import com.backendless.ThreadPoolService;
import com.backendless.exceptions.BackendlessException;

import java.io.*;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

class FileDownload
{

  private final static String FILE_DOWNLOAD_ERROR = "Could not download a file";

  Future<File> download( final String fileURL, final String localFilePathName )
  {
    Callable<File> downloadTask = new Callable<File>()
    {
      @Override
      public File call()
      {
        return FileDownload.this.downloading( fileURL, localFilePathName );
      }
    };
    return ThreadPoolService.getPoolExecutor().submit( downloadTask );
  }

  Future<Void> download( final String fileURL, final OutputStream stream )
  {
    Callable<Void> downloadTask = new Callable<Void>()
    {
      @Override
      public Void call()
      {
        FileDownload.this.downloading( fileURL, stream );
        return null;
      }
    };
    return ThreadPoolService.getPoolExecutor().submit( downloadTask );
  }

  Future<byte[]> download( final String fileURL )
  {
    Callable<byte[]> downloadTask = new Callable<byte[]>()
    {
      @Override
      public byte[] call()
      {
        return FileDownload.this.downloading( fileURL );
      }
    };
    return ThreadPoolService.getPoolExecutor().submit( downloadTask );
  }

  private byte[] downloading( String fileURL )
  {
    byte[] byteArray;
    try( ByteArrayOutputStream out = new ByteArrayOutputStream() )
    {
      downloading( fileURL, out );
      byteArray = out.toByteArray();
    }
    catch( IOException e )
    {
      throw new BackendlessException( FILE_DOWNLOAD_ERROR, e.getMessage() );
    }

    return byteArray;
  }

  private File downloading( String fileURL, String localFilePathName )
  {
    File file = new File( localFilePathName );

    try( BufferedOutputStream out = new BufferedOutputStream( new FileOutputStream( file ) ) )
    {
      downloading( fileURL, out );
    }
    catch( IOException e )
    {
      throw new BackendlessException( FILE_DOWNLOAD_ERROR, e.getMessage() );
    }

    return file;
  }

  private void downloading( String fileURL, OutputStream out )
  {
    try( InputStream in = new BufferedInputStream( new URL( fileURL ).openStream() ) )
    {
      int count;
      byte[] buffer = new byte[ 4096 ];
      while( ( count = in.read( buffer ) ) > 0 )
      {
        if( Thread.currentThread().isInterrupted() )
        {
          break;
        }
        out.write( buffer, 0, count );
      }
    }
    catch( IOException e )
    {
      throw new BackendlessException( FILE_DOWNLOAD_ERROR, e.getMessage() );
    }
  }

}
