package com.backendless.examples.filedownload.filedownloaddemo;

import com.backendless.Backendless;

import java.util.concurrent.ExecutionException;

public class Main
{

  public static void main( String[] args )
  {
    Backendless.initApp( Defaults.APPLICATION_ID, Defaults.API_KEY );

    Person person = Backendless.Data.of( Person.class ).findById( Defaults.ID_PERSON );
    System.out.println( person.image.getFileURL() );

    try
    {
      new Downloader().downloadMethods( person );
    }
    catch( ExecutionException | InterruptedException e )
    {
      e.printStackTrace();
    }

    try
    {
      new Downloader().downloadMethodsWithCancel( person );
    }
    catch( ExecutionException | InterruptedException e )
    {
      e.printStackTrace();
    }

  }

}
