package com.mbaas.command;

import android.content.Context;
import android.widget.Toast;
import com.backendless.files.BackendlessFile;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.mbaas.util.FutureUtils.waitUntilDoneWithPeriodicAction;

public final class DownloadFileToBytesCommand implements DownloadFileCommand
{
  private final Context context;
  private final BackendlessFile file;

  public DownloadFileToBytesCommand( Context context, BackendlessFile file )
  {
    this.context = context;
    this.file = file;
  }

  @Override
  public void execute()
  {
    final Future<byte[]> download = file.download();

    waitUntilDoneWithPeriodicAction( download, 2, TimeUnit.SECONDS,
                                     new Runnable()
                                     {
                                       @Override
                                       public void run()
                                       {
                                         Toast.makeText( context, "Downloading...", Toast.LENGTH_SHORT ).show();
                                       }
                                     } );



  }
}
