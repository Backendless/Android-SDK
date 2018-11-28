package com.backendless.files;

import android.widget.ProgressBar;
import com.backendless.ThreadPoolService;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class FileDownloadAndroid {

  private static final byte[] DEFAULT_CHUNK_SIZE = new byte[1024];

  private final static String FILE_DOWNLOAD_ERROR = "Could not download a file";

  void download(final String fileURL, final String localFilePathName,
                final ProgressBar progressBar, final AsyncCallback<File> callback)
  {
    ThreadPoolService.getPoolExecutor().execute(new Runnable()
    {
      @Override
      public void run()
      {
        try {
          callback.handleResponse( download( localFilePathName, progressBar, fileURL ) );
        }
        catch (Exception e) {
          callback.handleFault( new BackendlessFault( e ));
        }
      }
    });
  }

  void download(final String fileURL, final OutputStream stream,
                final ProgressBar progressBar, final AsyncCallback<Void> callback)
  {
    ThreadPoolService.getPoolExecutor().execute(new Runnable()
    {
      @Override
      public void run()
      {
        try {
          download( stream, progressBar, fileURL );
          callback.handleResponse( null );
        }
        catch (Exception e)
        {
          callback.handleFault( new BackendlessFault( e ));
        }
      }
    });
  }

  void download(final String fileURL, final ProgressBar progressBar,
                final AsyncCallback<byte[]> callback)
  {
    ThreadPoolService.getPoolExecutor().execute(new Runnable()
    {
      @Override
      public void run()
      {
        try {
          callback.handleResponse( download( progressBar, fileURL ));
        }
        catch (Exception e) {
          callback.handleFault( new BackendlessFault( e ));
        }
      }
    });
  }

  private File download( String localFilePathName, ProgressBar progressBar, String fileURL )
  {
    final File file = new File( localFilePathName );

    BufferedOutputStream out;
    try {
      out = new BufferedOutputStream(new FileOutputStream( file ));
    } catch (FileNotFoundException e) {
      throw new BackendlessException( FILE_DOWNLOAD_ERROR, e.getMessage() );
    }

    download( out, progressBar, fileURL );
    return file;
  }

  private void download( OutputStream out, ProgressBar progressBar, String fileURL )
  {
    InputStream in = null;
    try {
      URL url = new URL( fileURL );
      in = url.openStream();

      int fileSize = url.openConnection().getContentLength();
      int countReadSize = 0;
      progressBar.setProgress( 0 );
      int count;
      while (( count = in.read( DEFAULT_CHUNK_SIZE )) > 0 ) {
        out.write( DEFAULT_CHUNK_SIZE, 0, count );
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
    finally {
      if ( in != null ) {
        try {
          in.close();
        } catch (IOException e) {
          throw new BackendlessException( FILE_DOWNLOAD_ERROR, e.getMessage() );
        }
      }
      if (out != null) {
        try {
          out.close();
        } catch (IOException e) {
          throw new BackendlessException( FILE_DOWNLOAD_ERROR, e.getMessage() );
        }
      }
    }
  }

  private byte[] download( ProgressBar progressBar, String fileURL )
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    InputStream in = null;
    byte[] bytes;
    try {
      URL url = new URL( fileURL );
      in = url.openStream ();

      int fileSize = url.openConnection().getContentLength();
      int countReadSize = 0;
      progressBar.setProgress( 0 );
      int count;
      while ( (count = in.read( DEFAULT_CHUNK_SIZE )) > 0 ) {
        out.write( DEFAULT_CHUNK_SIZE, 0, count );
        countReadSize += count;
        progressBar.setProgress( countReadSize * 100 / fileSize );
      }
      bytes = out.toByteArray();
    }
    catch( MalformedURLException e )
    {
      throw new IllegalArgumentException( FILE_DOWNLOAD_ERROR, e );
    }
    catch( IOException e )
    {
      throw new BackendlessException( FILE_DOWNLOAD_ERROR, e.getMessage() );
    }
    finally {
      if ( in != null ) {
        try {
          in.close();
        } catch (IOException e) {
          new BackendlessException( FILE_DOWNLOAD_ERROR, e.getMessage() );
        }
      }
      if ( out != null ) {
        try {
          out.close();
        } catch (IOException e) {
          new BackendlessException(FILE_DOWNLOAD_ERROR, e.getMessage());
        }
      }
    }

    return bytes;
  }

}
