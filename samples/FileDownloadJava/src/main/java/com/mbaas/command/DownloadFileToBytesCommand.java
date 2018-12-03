package com.mbaas.command;

import com.backendless.files.BackendlessFile;

import java.util.Arrays;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.mbaas.util.FutureUtils.processResult;
import static com.mbaas.util.FutureUtils.waitUntilDoneWithPeriodicAction;

public final class DownloadFileToBytesCommand implements DownloadFileCommand
{
  private final BackendlessFile file;

  public DownloadFileToBytesCommand( BackendlessFile file )
  {
    this.file = file;
  }

  @Override
  public void execute()
  {
    final Future<byte[]> download = file.download();

    waitUntilDoneWithPeriodicAction( download, 2, TimeUnit.SECONDS,
                                     () -> System.out.println( "Downloading..." ) );

    processResult( download,
                   bytes -> {
                     System.out.println( "Successfully downloaded file as bytes: " + Arrays.toString( bytes ) );
                     System.out.println( "String representation: " + new String( bytes ) );
                   },
                   throwable -> System.err.println( "Failed to download file as bytes: " + throwable.getMessage() ) );
  }
}
