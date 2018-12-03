package com.mbaas.command;

import android.content.Context;
import android.widget.Toast;
import com.backendless.files.BackendlessFile;

import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.mbaas.util.FutureUtils.waitUntilDoneWithPeriodicAction;

public final class DownloadFileToOutputStreamCommand implements DownloadFileCommand
{
  private final Context context;
  private final BackendlessFile file;
  private final OutputStream outputStream;

  public DownloadFileToOutputStreamCommand( Context context, BackendlessFile file,
                                            OutputStream outputStream )
  {
    this.context = context;
    this.file = file;
    this.outputStream = outputStream;
  }

  @Override
  public void execute()
  {
    final Future<Void> download = file.download( outputStream );

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
      download.get();
      Toast.makeText( context, "Successfully downloaded file to specified OutputStream", Toast.LENGTH_LONG )
              .show();
    }
    catch( ExecutionException e )
    {
      Toast.makeText( context, "Failed to download file to OutputStream: " + e.getCause().getMessage(),
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
