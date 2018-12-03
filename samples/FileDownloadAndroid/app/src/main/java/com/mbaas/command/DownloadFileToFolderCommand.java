package com.mbaas.command;

import android.content.Context;
import android.widget.Toast;
import com.backendless.files.BackendlessFile;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.mbaas.util.FutureUtils.waitUntilDoneWithPeriodicAction;

public final class DownloadFileToFolderCommand implements DownloadFileCommand
{
  private final Context context;
  private final BackendlessFile file;
  private final String destinationFolder;

  public DownloadFileToFolderCommand( Context context, BackendlessFile file, String destinationFolder )
  {
    this.context = context;
    this.file = file;
    this.destinationFolder = destinationFolder;
  }

  @Override
  public void execute()
  {
    final Future<File> download = file.download( destinationFolder );

    waitUntilDoneWithPeriodicAction( download, 2, TimeUnit.SECONDS,
                                     new Runnable()
                                     {
                                       @Override
                                       public void run()
                                       {
                                         Toast.makeText( context, "Downloading...", Toast.LENGTH_SHORT ).show();
                                       }
                                     } );

    try
    {
      final File file = download.get();
      Toast.makeText( context, "Successfully downloaded file to " + file.getAbsolutePath(), Toast.LENGTH_LONG )
              .show();
    }
    catch( ExecutionException e )
    {
      Toast.makeText( context,
                      "Failed to download file to " + destinationFolder + ": " + e.getCause().getMessage(),
                      Toast.LENGTH_LONG )
              .show();
    }
    catch( InterruptedException e )
    {
      // should not happen
      throw new RuntimeException( e );
    }
  }
}
