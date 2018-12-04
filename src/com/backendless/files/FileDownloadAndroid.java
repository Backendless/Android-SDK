package com.backendless.files;

import android.os.AsyncTask;
import android.widget.ProgressBar;
import com.backendless.exceptions.BackendlessException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class FileDownloadAndroid
{

  private final static String FILE_DOWNLOAD_ERROR = "Could not downloading a file";

  AsyncTask download( final String fileURL, final String destinationPath, final ProgressBar progressBar )
  {
    return new AsyncTask<Void, Integer, File>()
    {
      @Override
      protected File doInBackground( Void... voids )
      {
        isCancelled();
        return downloading( fileURL, destinationPath, new ProgressCallback()
        {
          @Override
          public void onProgress( double percent )
          {
            publishProgress( (int) percent );
          }
        } );
      }

      @Override
      protected void onProgressUpdate( Integer... values )
      {
        super.onProgressUpdate( values );
        progressBar.setProgress( values[ 0 ] );
      }
    };
  }

  AsyncTask download( final String fileURL, final String destinationPath, final String fileName,
                      final ProgressBar progressBar )
  {
    return new AsyncTask<Void, Integer, File>()
    {
      @Override
      protected File doInBackground( Void... voids )
      {
        return downloading( fileURL, destinationPath, fileName, new ProgressCallback()
        {
          @Override
          public void onProgress( double percent )
          {
            publishProgress( (int) percent );
          }
        } );
      }

      @Override
      protected void onProgressUpdate( Integer... values )
      {
        super.onProgressUpdate( values );
        progressBar.setProgress( values[ 0 ] );
      }
    };
  }

  AsyncTask download( final String fileURL, final OutputStream stream, final ProgressBar progressBar )
  {
    return new AsyncTask<Void, Integer, Void>()
    {
      @Override
      protected Void doInBackground( Void... voids )
      {
        downloading( fileURL, stream, new ProgressCallback()
        {
          @Override
          public void onProgress( double percent )
          {
            publishProgress( (int) percent );
          }
        } );
        return null;
      }

      @Override
      protected void onProgressUpdate( Integer... values )
      {
        super.onProgressUpdate( values );
        progressBar.setProgress( values[ 0 ] );
      }
    };
  }

  AsyncTask download( final String fileURL, final ProgressBar progressBar )
  {
    return new AsyncTask<Void, Integer, byte[]>()
    {
      @Override
      protected byte[] doInBackground( Void... voids )
      {
        return downloading( fileURL, new ProgressCallback()
        {
          @Override
          public void onProgress( double percent )
          {
            publishProgress( (int) percent );
          }
        } );
      }

      @Override
      protected void onProgressUpdate( Integer... values )
      {
        super.onProgressUpdate( values );
        progressBar.setProgress( values[ 0 ] );
      }
    };
  }

  private byte[] downloading( String fileURL, ProgressCallback progressCallback )
  {
    byte[] byteArray;
    try( ByteArrayOutputStream out = new ByteArrayOutputStream() )
    {
      downloading( fileURL, out, progressCallback );
      byteArray = out.toByteArray();
    }
    catch( IOException e )
    {
      throw new BackendlessException( FILE_DOWNLOAD_ERROR, e.getMessage() );
    }

    return byteArray;
  }

  private File downloading( String fileURL, String destinationPath, ProgressCallback progressCallback )
  {
    String fileName = fileURL.substring( fileURL.lastIndexOf( '/' ) + 1 );
    return downloading( fileURL, destinationPath, fileName, progressCallback );
  }

  private File downloading( String fileURL, String destinationPath, String fileName, ProgressCallback progressCallback )
  {
    String localFilePathName = destinationPath + fileName;
    final File file = new File( localFilePathName );

    try( BufferedOutputStream out = new BufferedOutputStream( new FileOutputStream( file ) ) )
    {
      downloading( fileURL, out, progressCallback );
    }
    catch( IOException e )
    {
      throw new BackendlessException( FILE_DOWNLOAD_ERROR, e.getMessage() );
    }

    return file;
  }

  private void downloading( String fileURL, OutputStream out, ProgressCallback progressCallback )
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
      progressCallback.onProgress( 0 );
      int count;
      byte[] buffer = new byte[ 4096 ];
      while( ( count = in.read( buffer ) ) > 0 )
      {
        if( Thread.currentThread().isInterrupted() )// TODO: 12/4/18 if isCanceled()
        {
          break;
        }
        out.write( buffer, 0, count );
        countReadSize += count;
        progressCallback.onProgress( countReadSize * 100 / fileSize );
      }
    }
    catch( IOException e )
    {
      throw new BackendlessException( FILE_DOWNLOAD_ERROR, e.getMessage() );
    }
  }

}