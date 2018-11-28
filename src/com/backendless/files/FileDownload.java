package com.backendless.files;

import com.backendless.ThreadPoolService;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

class FileDownload {

  private static final byte[] DEFAULT_CHUNK_SIZE = new byte[1024];

  private final static String FILE_DOWNLOAD_ERROR = "Could not download a file";

  void download(final String fileURL, final String localFilePathName, final AsyncCallback<File> callback)
  {
    ThreadPoolService.getPoolExecutor().execute(new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          callback.handleResponse( download( localFilePathName, fileURL ));
        }
        catch ( Exception e )
        {
          callback.handleFault( new BackendlessFault( e ));
        }
      }
    });
  }

  void download(final String fileURL, final OutputStream stream, final AsyncCallback<Void> callback)
  {
    ThreadPoolService.getPoolExecutor().execute(new Runnable()
    {
      @Override
      public void run()
      {
        try {
          download( stream, fileURL );
          callback.handleResponse( null );
        }
        catch (Exception e)
        {
          callback.handleFault( new BackendlessFault( e ));
        }
      }
    });
  }

  void download( final String fileURL, final AsyncCallback<byte[]> callback )
  {
    ThreadPoolService.getPoolExecutor().execute(new Runnable()
    {
      @Override
      public void run()
      {
        try {
          callback.handleResponse( download( fileURL ));
        }
        catch (Exception e) {
          callback.handleFault( new BackendlessFault( e ));
        }
      }
    });
  }

  private File download( String localFilePathName, String fileURL )
  {
    File file = new File( localFilePathName );

    BufferedOutputStream out;
    try
    {
      out = new BufferedOutputStream( new FileOutputStream( file ));
    }
    catch ( FileNotFoundException e )
    {
      throw new BackendlessException( FILE_DOWNLOAD_ERROR, e.getMessage() );
    }
    download( out, fileURL );

    return file;
  }

  private void download( OutputStream out, String fileURL )
  {
    InputStream in = null;
    try {
      URL url = new URL( fileURL );

      in = new BufferedInputStream( url.openStream() );

      int count;
      while (( count = in.read( DEFAULT_CHUNK_SIZE )) > 0) {
        out.write( DEFAULT_CHUNK_SIZE, 0, count );
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
      if (in != null) {
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

  private byte[] download( String fileURL )
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    InputStream in;
    URL url;
    byte[] bytes ;
    try {
      url = new URL( fileURL );
      in = url.openStream ();

      int count;
      while (( count = in.read( DEFAULT_CHUNK_SIZE )) > 0 ) {
        out.write(DEFAULT_CHUNK_SIZE, 0, count);
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
      try {
        out.close();
      } catch (IOException e) {
        throw new BackendlessException( FILE_DOWNLOAD_ERROR, e.getMessage() );
      }
    }

    return bytes;
  }

}
