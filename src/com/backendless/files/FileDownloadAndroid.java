package com.backendless.files;

import android.widget.ProgressBar;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class FileDownloadAndroid
{

  private final static String FILE_DOWNLOAD_ERROR = "Could not download a file";

  FutureTask<Void> download( final String fileURL, final String localFilePathName, final ProgressBar progressBar,
                             final AsyncCallback<File> callback )
  {
    FutureTask<Void> downloadTask = new FutureTask<>( new Callable<Void>()
    {
      @Override
      public Void call()
      {
        try
        {
          callback.handleResponse( download( localFilePathName, progressBar, fileURL ) );
        }
        catch( Exception e )
        {
          callback.handleFault( new BackendlessFault( e ) );
        }
        return null;
      }
    } );
    ExecutorService downloadWithLocalPathExecutor = Executors.newSingleThreadExecutor();
    downloadWithLocalPathExecutor.execute( downloadTask );
    downloadWithLocalPathExecutor.shutdown();

    return downloadTask;
  }

  FutureTask<Void> download( final String fileURL, final OutputStream stream, final ProgressBar progressBar,
                             final AsyncCallback<Void> callback )
  {
    FutureTask<Void> downloadTask = new FutureTask<>( new Callable<Void>()
    {
      @Override
      public Void call()
      {
        try
        {
          download( stream, progressBar, fileURL );
          callback.handleResponse( null );
        }
        catch( Exception e )
        {
          callback.handleFault( new BackendlessFault( e ) );
        }
        return null;
      }
    } );
    ExecutorService downloadWithLocalPathExecutor = Executors.newSingleThreadExecutor();
    downloadWithLocalPathExecutor.execute( downloadTask );
    downloadWithLocalPathExecutor.shutdown();

    return downloadTask;
  }

  FutureTask<Void> download( final String fileURL, final ProgressBar progressBar, final AsyncCallback<byte[]> callback )
  {
    FutureTask<Void> downloadTask = new FutureTask<>( new Callable<Void>()
    {
      @Override
      public Void call()
      {
        try
        {
          callback.handleResponse( download( progressBar, fileURL ) );
        }
        catch( Exception e )
        {
          callback.handleFault( new BackendlessFault( e ) );
        }
        return null;
      }
    } );
    ExecutorService downloadWithLocalPathExecutor = Executors.newSingleThreadExecutor();
    downloadWithLocalPathExecutor.execute( downloadTask );
    downloadWithLocalPathExecutor.shutdown();

    return downloadTask;
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
