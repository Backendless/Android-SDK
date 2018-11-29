package com.backendless.examples.filedownload.filedownloaddemo;

import java.io.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

class Downloader
{

  void downloadMethods( Person person ) throws ExecutionException, InterruptedException
  {
    downloadWithLocalPath( person );

    downloadOutputStream( person );

    downloadByteArray( person );
  }

  void downloadMethodsWithCancel( Person person ) throws ExecutionException, InterruptedException
  {
    downloadWithLocalPathAndCancel( person );

    downloadOutputStreamAndCancel( person );

    downloadByteArrayAndCancel( person );
  }

  private void downloadWithLocalPath( Person person ) throws InterruptedException, ExecutionException
  {
    Future<File> futureDownloadWithLocalPath = person.image.download( Defaults.LOCAL_FILE_PATH );
    File fileWithLocalPath = futureDownloadWithLocalPath.get();
    System.out.println( fileWithLocalPath.toString() );
  }

  private void downloadOutputStream( Person person ) throws InterruptedException, ExecutionException
  {
    OutputStream[] out = downloadOutputStream();
    Future<Void> futureDownloadWithOutputStream = person.image.download( out[ 0 ] );
    futureDownloadWithOutputStream.get();
    System.out.println( "file downloaded" );
  }

  private void downloadByteArray( Person person ) throws InterruptedException, ExecutionException
  {
    Future<byte[]> futureDownloadByteArray = person.image.download();
    File fileFromByteArray = writeFileFromByteArray( futureDownloadByteArray.get() );
    System.out.println( fileFromByteArray.toString() );
  }

  private void downloadWithLocalPathAndCancel( Person person ) throws InterruptedException, ExecutionException
  {
    Future<File> futureDownloadWithLocalPath = person.image.download( Defaults.LOCAL_FILE_PATH );
    if( !futureDownloadWithLocalPath.isDone() )
    {
      futureDownloadWithLocalPath.cancel( true );
      System.out.println( "Downloading cancel" );
    }
    else
    {
      System.out.println( futureDownloadWithLocalPath.get().toString() );
    }
  }

  private void downloadOutputStreamAndCancel( Person person ) throws InterruptedException, ExecutionException
  {
    OutputStream[] out = downloadOutputStream();
    Future<Void> futureDownloadWithOutputStream = person.image.download( out[ 0 ] );
    if( !futureDownloadWithOutputStream.isDone() )
    {
      futureDownloadWithOutputStream.cancel( true );
      System.out.println( "Downloading cancel" );
    }
    else
    {
      futureDownloadWithOutputStream.get();
      System.out.println( "file downloaded" );
    }
  }

  private void downloadByteArrayAndCancel( Person person ) throws InterruptedException, ExecutionException
  {
    Future<byte[]> futureDownloadByteArray = person.image.download();
    if( !futureDownloadByteArray.isDone() )
    {
      futureDownloadByteArray.cancel( true );
      System.out.println( "Downloading cancel" );
    }
    else
    {
      System.out.println( writeFileFromByteArray( futureDownloadByteArray.get() ).toString() );
    }
  }

  private OutputStream[] downloadOutputStream()
  {
    File file = new File( Defaults.LOCAL_FILE_PATH );
    final OutputStream[] out = { null };
    try
    {
      out[ 0 ] = new FileOutputStream( file );
    }
    catch( FileNotFoundException e )
    {
      e.printStackTrace();
    }

    return out;
  }

  private File writeFileFromByteArray( byte[] bytes )
  {
    File file = new File( Defaults.LOCAL_FILE_PATH );

    FileOutputStream stream = null;
    try
    {
      stream = new FileOutputStream( file );
    }
    catch( FileNotFoundException e )
    {
      e.printStackTrace();
    }
    try
    {
      try
      {
        if( stream != null )
        {
          stream.write( bytes );
        }
      }
      catch( IOException e )
      {
        e.printStackTrace();
      }
    }
    finally
    {
      try
      {
        if( stream != null )
        {
          stream.close();
        }
      }
      catch( IOException e )
      {
        e.printStackTrace();
      }
    }

    return file;
  }

}
