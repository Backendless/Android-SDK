package com.backendless.examples.filedownload.filedownloaddemo;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.io.*;

public class Main {

  public static void main(String[] args)
  {
    Backendless.initApp( Defaults.APPLICATION_ID, Defaults.API_KEY );

    Person person = Backendless.Data.of( Person.class ).findById( Defaults.ID_PERSON );

    System.out.println( person.image.getFileURL() );

    downloadWithLocalPath( person );

    downloadWithOutputStream( person );

    downloadByteArray( person );

  }

  private static void downloadWithLocalPath( Person person ) {
    person.image.download( Defaults.LOCAL_FILE_PATH, new AsyncCallback<File>() {
      @Override
      public void handleResponse( File file ) {
        System.out.println( file.toString() );
      }

      @Override
      public void handleFault(BackendlessFault fault) {
        new BackendlessFault( fault );
      }
    });
  }

  private static void downloadWithOutputStream( Person person ) {
    File file = new File( Defaults.LOCAL_FILE_PATH );
    final OutputStream[] out = {null};
    try {
      out[0] = new FileOutputStream( file );
      System.out.println( file.toString() );
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    person.image.download(out[0], new AsyncCallback<Void>() {
      @Override
      public void handleResponse( Void response ) {
        System.out.println( "File downloaded" );
      }
      @Override
      public void handleFault(BackendlessFault fault) {
        new BackendlessFault( fault );
      }
    });
  }

  private static void downloadByteArray( Person person ) {
    person.image.download(new AsyncCallback<byte[]>() {
      @Override
      public void handleResponse( byte[] response ) {
        File file = writeFileFromByteArray( response );
        System.out.println( file.toString() );
      }

      @Override
      public void handleFault( BackendlessFault fault ) {
        new BackendlessFault( fault );
      }
    });
  }

  private static File writeFileFromByteArray(byte[] bytes)
  {
    File file = new File( Defaults.LOCAL_FILE_PATH );

    FileOutputStream stream = null;
    try {
      stream = new FileOutputStream( file );
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    try {
      try {
        stream.write( bytes );
      } catch ( IOException e ) {
        e.printStackTrace();
      }
    } finally {
      try {
        stream.close();
      } catch ( IOException e ) {
        e.printStackTrace();
      }
    }

    return file;
  }

}
