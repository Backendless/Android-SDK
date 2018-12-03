package com.mbaas;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.mbaas.command.DownloadFileToFolderCommand;
import com.mbaas.command.DownloadFileToOutputStreamCommand;
import com.mbaas.model.Person;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
  private Person person;

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_main );

    Backendless.initApp( getApplicationContext(),
                         "86886045-9D4A-C442-FFE0-385B2759C400",
                         "4260026A-1BFD-DEFD-FFB8-D2E9D9AEEF00" );

    final DataQueryBuilder query = DataQueryBuilder.create().setWhereClause( "fileRef is not null" );
    Backendless.Data.of( Person.class ).find( query, new AsyncCallback<List<Person>>()
    {
      @Override
      public void handleResponse( List<Person> persons )
      {
        person = persons.get( 0 );
        Toast.makeText( getApplicationContext(), "Using Person with objectId " + person.getObjectId(),
                        Toast.LENGTH_LONG )
                .show();
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        Toast.makeText( getApplicationContext(),
                        "Failed to load Person with file reference: " + fault.getMessage(),
                        Toast.LENGTH_LONG )
                .show();
      }
    } );

    final Button downloadToInternalFilesWithSameNameButton =
            findViewById( R.id.download_to_internal_files_with_same_name_button );
    downloadToInternalFilesWithSameNameButton.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        final String destinationPath = getFilesDir().getAbsolutePath();
        new DownloadFileToFolderCommand( getApplicationContext(), person.getFileRef(), destinationPath )
                .execute();
      }
    } );

    final Button downloadToInternalCacheWithSameNameButton =
            findViewById( R.id.download_to_internal_cache_with_same_name_button );
    downloadToInternalCacheWithSameNameButton.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        final String destinationPath = getCacheDir().getAbsolutePath();
        new DownloadFileToFolderCommand( getApplicationContext(), person.getFileRef(), destinationPath )
                .execute();
      }
    } );

    final Button downloadToInternalFilesWithSameNameUsingOutputStreamButton =
            findViewById( R.id.download_to_internal_files_with_same_name_using_output_stream_button );
    downloadToInternalFilesWithSameNameUsingOutputStreamButton.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        final String fileURL = person.getFileRef().getFileURL();
        final String fileName = fileURL.substring( fileURL.lastIndexOf( '/' ) + 1 );
        try
        {
          final FileOutputStream outputStream =
                  openFileOutput( fileName, Context.MODE_PRIVATE );
          new DownloadFileToOutputStreamCommand( getApplicationContext(), person.getFileRef(), outputStream )
                  .execute();
        }
        catch( FileNotFoundException e )
        {
          Toast.makeText( getApplicationContext(), "File not found: " + e.getMessage(), Toast.LENGTH_LONG )
                  .show();
        }
      }
    } );
  }
}
