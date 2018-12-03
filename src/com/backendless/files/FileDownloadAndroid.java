package com.backendless.files;

import android.widget.ProgressBar;
import com.backendless.ThreadPoolService;
import com.backendless.exceptions.BackendlessException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.*;

public class FileDownloadAndroid
{

  private final static String FILE_DOWNLOAD_ERROR = "Could not download a file";

  Future<File> download( final String fileURL, final String localFilePathName, final ProgressBar progressBar )
  {
    Callable<File> downloadTask = () -> download( localFilePathName, progressBar, fileURL );
    return ThreadPoolService.getPoolExecutor().submit( downloadTask );
  }

  Future<Void> download( final String fileURL, final OutputStream stream, final ProgressBar progressBar )
  {
    Callable<Void> downloadTask = () ->
    {
      download( stream, progressBar, fileURL );
      return null;
    };
    return ThreadPoolService.getPoolExecutor().submit( downloadTask );
  }

  Future<byte[]> download( final String fileURL, final ProgressBar progressBar )
  {
    Callable<byte[]> downloadTask = () -> download( progressBar, fileURL );
    return ThreadPoolService.getPoolExecutor().submit( downloadTask );
  }

  private byte[] download( ProgressBar progressBar, String fileURL )
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    download( out, progressBar, fileURL );

    return out.toByteArray();
  }

  private File download( String localFilePathName, ProgressBar progressBar, String fileURL )
  {
    final File file = new File( localFilePathName );

    BufferedOutputStream out;
    try
    {
      out = new BufferedOutputStream( new FileOutputStream( file ) );
    }
    catch( FileNotFoundException e )
    {
      throw new BackendlessException( FILE_DOWNLOAD_ERROR, e.getMessage() );
    }

    download( out, progressBar, fileURL );
    return file;
  }

  private void download( OutputStream out, ProgressBar progressBar, String fileURL )
  {
    InputStream in = null;
    try
    {
      URL url = new URL( fileURL );
      in = url.openStream();

      int fileSize = url.openConnection().getContentLength();
      int countReadSize = 0;
      progressBar.setProgress( 0 );
      int count;
      byte[] buffer = new byte[ 4096 ];
      while( ( count = in.read( buffer ) ) > 0 )
      {
        out.write( buffer, 0, count );
        countReadSize += count;
        progressBar.setProgress( countReadSize * 100 / fileSize );
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
