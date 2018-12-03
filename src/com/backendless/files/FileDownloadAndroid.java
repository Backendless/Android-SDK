package com.backendless.files;

import android.widget.ProgressBar;
import com.backendless.ThreadPoolService;
import com.backendless.exceptions.BackendlessException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;


public class FileDownloadAndroid
{

  private final static String FILE_DOWNLOAD_ERROR = "Could not downloading a file";

  Future<File> download( final String fileURL, final String destinationPath, final ProgressBar progressBar )
  {
    Callable<File> downloadTask = new Callable<File>()
    {
      @Override
      public File call()
      {
        return FileDownloadAndroid.this.downloading( fileURL, destinationPath, progressBar );
      }
    };
    return ThreadPoolService.getPoolExecutor().submit( downloadTask );
  }

  Future<File> download( final String fileURL, final String destinationPath, final String fileName,
                         final ProgressBar progressBar )
  {
    Callable<File> downloadTask = new Callable<File>()
    {
      @Override
      public File call()
      {
        return FileDownloadAndroid.this.downloading( fileURL, destinationPath, fileName, progressBar );
      }
    };
    return ThreadPoolService.getPoolExecutor().submit( downloadTask );
  }

  Future<Void> download( final String fileURL, final OutputStream stream, final ProgressBar progressBar )
  {
    Callable<Void> downloadTask = new Callable<Void>()
    {
      @Override
      public Void call()
      {
        FileDownloadAndroid.this.downloading( fileURL, stream, progressBar );
        return null;
      }
    };
    return ThreadPoolService.getPoolExecutor().submit( downloadTask );
  }

  Future<byte[]> download( final String fileURL, final ProgressBar progressBar )
  {
    Callable<byte[]> downloadTask = new Callable<byte[]>()
    {
      @Override
      public byte[] call()
      {
        return FileDownloadAndroid.this.downloading( fileURL, progressBar );
      }
    };
    return ThreadPoolService.getPoolExecutor().submit( downloadTask );
  }

  private byte[] downloading( String fileURL, ProgressBar progressBar )
  {
    byte[] byteArray;
    try( ByteArrayOutputStream out = new ByteArrayOutputStream() )
    {
      downloading( fileURL, out, progressBar );
      byteArray = out.toByteArray();
    }
    catch( IOException e )
    {
      throw new BackendlessException( FILE_DOWNLOAD_ERROR, e.getMessage() );
    }

    return byteArray;
  }

  private File downloading( String fileURL, String destinationPath, ProgressBar progressBar )
  {
    String fileName = fileURL.substring( fileURL.lastIndexOf( '/' ) + 1 );
    return downloading( fileURL, destinationPath, fileName, progressBar );
  }

  private File downloading( String fileURL, String destinationPath, String fileName, ProgressBar progressBar )
  {
    String localFilePathName = destinationPath + fileName;
    final File file = new File( localFilePathName );

    try( BufferedOutputStream out = new BufferedOutputStream( new FileOutputStream( file ) ) )
    {
      downloading( fileURL, out, progressBar );
    }
    catch( IOException e )
    {
      throw new BackendlessException( FILE_DOWNLOAD_ERROR, e.getMessage() );
    }

    return file;
  }

  private void downloading( String fileURL, OutputStream out, ProgressBar progressBar )
  {
    URL url;
    try
    {
      url = new URL( fileURL );
    }
    catch( MalformedURLException e )
    {
      throw new BackendlessException( FILE_DOWNLOAD_ERROR, e.getMessage() );
    }
    try( InputStream in = url.openStream() )
    {
      int fileSize = url.openConnection().getContentLength();
      int countReadSize = 0;
      progressBar.setProgress( 0 );
      int count;
      byte[] buffer = new byte[ 4096 ];
      while( ( count = in.read( buffer ) ) > 0 )
      {
        if( Thread.currentThread().isInterrupted() )
        {
          break;
        }
        out.write( buffer, 0, count );
        countReadSize += count;
        progressBar.setProgress( countReadSize * 100 / fileSize );
      }
    }
    catch( IOException e )
    {
      throw new BackendlessException( FILE_DOWNLOAD_ERROR, e.getMessage() );
    }
  }

}