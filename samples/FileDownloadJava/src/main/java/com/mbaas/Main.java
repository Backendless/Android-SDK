package com.mbaas;

import com.backendless.Backendless;
import com.backendless.persistence.DataQueryBuilder;
import com.mbaas.command.DownloadFileToBytesCommand;
import com.mbaas.command.DownloadFileToFolderCommand;
import com.mbaas.command.DownloadFileToFolderSpecifyingNameCommand;
import com.mbaas.command.DownloadFileToOutputStreamCommand;
import com.mbaas.model.Person;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

public class Main
{
  public static void main( String[] args )
  {
    final Scanner scanner = new Scanner( System.in );

    System.out.print( "Application ID: " );
    final String appId = scanner.next();

    System.out.print( "API key: " );
    final String apiKey = scanner.next();

    Backendless.initApp( appId, apiKey );

    System.out.println( "Please make sure your app contains a table named 'Person' with a FILE REFERENCE column " +
                                "named 'fileRef' and that the table contains at least one object referencing a file!\n" +
                                "Press ENTER when ready..." );
    try
    {
      System.in.read();
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }

    System.out.println( "Retrieving test Person..." );
    final DataQueryBuilder query = DataQueryBuilder.create().setWhereClause( "fileRef is not null" );
    final Person person = Backendless.Data.of( Person.class ).find( query ).get( 0 );
    System.out.println( "Found Person with objectId " + person.getObjectId() );

    boolean repeat;
    do
    {
      System.out.println( "Select how you want the file to be downloaded:" );
      System.out.println( "1) Download to specified folder with same name as in File Service" );
      System.out.println( "2) Download to specified folder with given name" );
      System.out.println( "3) Download to specified OutputStream" );
      System.out.println( "4) Download as bytes" );
      System.out.print( "(enter a number): " );
      final int option = scanner.nextInt();

      switch( option )
      {
        case 1:
        {
          System.out.println( "Enter a full path to folder where you want the file to be stored:" );
          final String destinationPath = scanner.next();

          new DownloadFileToFolderCommand( person.getFileRef(), destinationPath )
                  .execute();

          break;
        }
        case 2:
        {
          System.out.println( "Enter a full path to folder where you want the file to be stored:" );
          final String destinationPath = scanner.next();

          System.out.println( "Enter a name for downloaded file:" );
          final String fileName = scanner.next();

          new DownloadFileToFolderSpecifyingNameCommand( person.getFileRef(), destinationPath, fileName )
                  .execute();

          break;
        }
        case 3:
        {
          try (final OutputStream outputStream = new ByteArrayOutputStream())
          {
            new DownloadFileToOutputStreamCommand( person.getFileRef(), outputStream )
                    .execute();
          }
          catch( IOException e )
          {
            throw new RuntimeException( "Failed to close OutputStream: " + e.getMessage(), e );
          }

          break;
        }
        case 4:
        {
          new DownloadFileToBytesCommand( person.getFileRef() )
                  .execute();
          break;
        }
      }

      System.out.println( "Another download? (Y/N)" );
      final String another = scanner.next();
      repeat = another.equalsIgnoreCase( "Y" );
    }
    while( repeat );
  }
}
