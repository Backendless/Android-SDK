package com.backendless.files;

import android.widget.ProgressBar;
import com.backendless.ThreadPoolService;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

class FilesLoad {

  private static final byte[] DEFAULT_CHUNK_SIZE = new byte[1024];

  void download(final String fileURL, final String localFilePathName, final AsyncCallback<File> callback)
  {
    ThreadPoolService.getPoolExecutor().execute(new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          callback.handleResponse( load( localFilePathName, fileURL ));
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
          load( stream, fileURL );
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
          final byte[] fileInByte = new FilesLoad().load( fileURL );
          callback.handleResponse( fileInByte );
        }
        catch (Exception e) {
          callback.handleFault( new BackendlessFault( e ));
        }
      }
    });
  }

  public void download( final String fileURL, final String localFilePathName,
                        final ProgressBar progressBar, final AsyncCallback<File> callback )
  {
    ThreadPoolService.getPoolExecutor().execute(new Runnable()
    {
      @Override
      public void run()
      {
        try {
          final File file = new FilesLoad().load( localFilePathName, progressBar, fileURL );
          callback.handleResponse( file );
        }
        catch (Exception e) {
          callback.handleFault( new BackendlessFault( e ));
        }
      }
    });
  }

  public void download( final String fileURL, final OutputStream stream,
                        final ProgressBar progressBar, final AsyncCallback<Void> callback )
  {
    ThreadPoolService.getPoolExecutor().execute(new Runnable()
    {
      @Override
      public void run()
      {
        try {
          new FilesLoad().load( stream, progressBar, fileURL );
          callback.handleResponse(null);
        }
        catch (Exception e)
        {
          callback.handleFault( new BackendlessFault( e ));
        }
      }
    });
  }

  public void download( final String fileURL, final ProgressBar progressBar,
                        final AsyncCallback<byte[]> callback )
  {
    ThreadPoolService.getPoolExecutor().execute(new Runnable()
    {
      @Override
      public void run()
      {
        try {
          final byte[] fileInByte = new FilesLoad().load( progressBar, fileURL );
          callback.handleResponse( fileInByte );
        }
        catch (Exception e) {
          callback.handleFault( new BackendlessFault( e ));
        }
      }
    });
  }

  private File load(String localFilePathName, String fileURL)
  {
    InputStream in = null;
    File file = new File( localFilePathName );
    FileOutputStream out = null;
    try {
      URL url = new URL( fileURL );

      in = new BufferedInputStream( url.openStream() );
      out = new FileOutputStream( file );

      int count;
      while (( count = in.read( DEFAULT_CHUNK_SIZE )) > 0 ) {
          out.write( DEFAULT_CHUNK_SIZE, 0, count );
      }
    }
    catch( MalformedURLException e )
    {
      throw new IllegalArgumentException( ExceptionMessage.FILE_UPLOAD_ERROR, e );
    }
    catch( IOException e )
    {
      throw new BackendlessException( ExceptionMessage.FILE_UPLOAD_ERROR, e.getMessage() );
    }
    finally {
      if (in != null) {
        try {
          in.close();
        } catch ( IOException e ) {
          throw new BackendlessException( ExceptionMessage.FILE_UPLOAD_ERROR, e.getMessage() );
        }
      }
      if (out != null) {
        try {
          out.close();
        } catch (IOException e) {
          throw new BackendlessException( ExceptionMessage.FILE_UPLOAD_ERROR, e.getMessage() );
        }
      }
    }

    return file;
  }

  void load( OutputStream out, String fileURL )
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
      throw new IllegalArgumentException( ExceptionMessage.FILE_UPLOAD_ERROR, e );
    }
    catch( IOException e )
    {
      throw new BackendlessException( ExceptionMessage.FILE_UPLOAD_ERROR, e.getMessage() );
    }
    finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          throw new BackendlessException( ExceptionMessage.FILE_UPLOAD_ERROR, e.getMessage() );
        }
      }
      if (out != null) {
        try {
          out.close();
        } catch (IOException e) {
          throw new BackendlessException( ExceptionMessage.FILE_UPLOAD_ERROR, e.getMessage() );
        }
      }
    }
  }

  byte[] load( String fileURL )
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
      throw new IllegalArgumentException( ExceptionMessage.FILE_UPLOAD_ERROR, e );
    }
    catch( IOException e )
    {
      throw new BackendlessException( ExceptionMessage.FILE_UPLOAD_ERROR, e.getMessage() );
    }
    finally {
      try {
        out.close();
      } catch (IOException e) {
        throw new BackendlessException( ExceptionMessage.FILE_UPLOAD_ERROR, e.getMessage() );
      }
    }

    return bytes;
  }

  File load(String localFilePathName, ProgressBar progressBar, String fileURL )
  {
    InputStream in = null;
    final File file = new File( localFilePathName );

    FileOutputStream out = null;
    try {
      URL url = new URL( fileURL );
      in = new BufferedInputStream( url.openStream() );
      out = new FileOutputStream( file );

      int fileSize = url.openConnection().getContentLength();
      int countReadSize = 0;
      int progress = 0;
      progressBar.setProgress( progress );

      int count;
      while (( count = in.read( DEFAULT_CHUNK_SIZE )) > 0) {
        out.write(DEFAULT_CHUNK_SIZE, 0, count);
        countReadSize += count;
        progress = countReadSize * 100 / fileSize;
        progressBar.setProgress(progress);
      }
    }
    catch( MalformedURLException e )
    {
      throw new IllegalArgumentException( ExceptionMessage.FILE_UPLOAD_ERROR, e );
    }
    catch( IOException e )
    {
      throw new BackendlessException( ExceptionMessage.FILE_UPLOAD_ERROR, e.getMessage() );
    }
    finally {
      if ( in != null ) {
        try {
          in.close();
        } catch ( IOException e ) {
          throw new BackendlessException( ExceptionMessage.FILE_UPLOAD_ERROR, e.getMessage() );
        }
      }
      if ( out != null ) {
        try {
          out.close();
        } catch (IOException e) {
          throw new BackendlessException( ExceptionMessage.FILE_UPLOAD_ERROR, e.getMessage() );
        }
      }
    }

    return file;
  }

  void load( OutputStream out, ProgressBar progressBar, String fileURL )
  {
    InputStream in = null;
    try {
      URL url = new URL( fileURL );

      in = new BufferedInputStream( url.openStream() );

      int fileSize = url.openConnection().getContentLength();
      int countReadSize = 0;
      int progress = 0;
      progressBar.setProgress( progress );

      int count;
      while (( count = in.read( DEFAULT_CHUNK_SIZE )) > 0 ) {
        out.write( DEFAULT_CHUNK_SIZE, 0, count );
        countReadSize += count;
        progress = countReadSize * 100 / fileSize;
        progressBar.setProgress( progress );
      }
    }
    catch( MalformedURLException e )
    {
      throw new IllegalArgumentException( ExceptionMessage.FILE_UPLOAD_ERROR, e );
    }
    catch( IOException e )
    {
      throw new BackendlessException( ExceptionMessage.FILE_UPLOAD_ERROR, e.getMessage() );
    }
    finally {
      if ( in != null ) {
        try {
          in.close();
        } catch (IOException e) {
          throw new BackendlessException( ExceptionMessage.FILE_UPLOAD_ERROR, e.getMessage() );
        }
      }
      if (out != null) {
        try {
          out.close();
        } catch (IOException e) {
          throw new BackendlessException( ExceptionMessage.FILE_UPLOAD_ERROR, e.getMessage() );
        }
      }
    }
  }

  byte[] load( ProgressBar progressBar, String fileURL )
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    InputStream in;
    URL url;
    byte[] bytes;
    try {
      url = new URL( fileURL );
      in = url.openStream ();

      int fileSize = url.openConnection().getContentLength();
      int countReadSize = 0;
      int progress = 0;
      progressBar.setProgress( progress );

      int count;
      while ( (count = in.read( DEFAULT_CHUNK_SIZE )) > 0 ) {
        out.write( DEFAULT_CHUNK_SIZE, 0, count );
        countReadSize += count;
        progress = countReadSize * 100 / fileSize;
        progressBar.setProgress( progress );
      }
      bytes = out.toByteArray();
    }
    catch( MalformedURLException e )
    {
      throw new IllegalArgumentException( ExceptionMessage.FILE_UPLOAD_ERROR, e );
    }
    catch( IOException e )
    {
      throw new BackendlessException( ExceptionMessage.FILE_UPLOAD_ERROR, e.getMessage() );
    }
    finally {
      try {
        out.close();
      } catch (IOException e) {
        throw new BackendlessException( ExceptionMessage.FILE_UPLOAD_ERROR, e.getMessage() );
      }
    }

    return bytes;
  }

}
