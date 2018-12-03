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

  private final static String FILE_DOWNLOAD_ERROR = "Could not download a file";

  Future<File> download( final String fileURL, final String localFilePathName, final ProgressBar progressBar )
  {
    Callable<File> downloadTask = new Callable<File>()
    {
      @Override
      public File call()
      {
        return FileDownloadAndroid.this.download( localFilePathName, progressBar, fileURL );
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
        FileDownloadAndroid.this.download( stream, progressBar, fileURL );
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
        return FileDownloadAndroid.this.download( progressBar, fileURL );
      }
    };
    return ThreadPoolService.getPoolExecutor().submit( downloadTask );
  }

  private byte[] download( ProgressBar progressBar, String fileURL )
  {
    byte[] byteArray;
    try( ByteArrayOutputStream out = new ByteArrayOutputStream() )
    {
      download( out, progressBar, fileURL );
      byteArray = out.toByteArray();
    }
    catch( IOException e )
    {
      throw new BackendlessException( FILE_DOWNLOAD_ERROR, e.getMessage() );
    }


    return byteArray;
  }

  private File download( String localFilePathName, ProgressBar progressBar, String fileURL )
  {
    final File file = new File( localFilePathName );

    try( BufferedOutputStream out = new BufferedOutputStream( new FileOutputStream( file ) ) )
    {
      download( out, progressBar, fileURL );
    }
    catch( IOException e )
    {
      throw new BackendlessException( FILE_DOWNLOAD_ERROR, e.getMessage() );
    }

    return file;
  }

  private void download( OutputStream out, ProgressBar progressBar, String fileURL )
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
