package com.mbaas.command;

import com.backendless.files.BackendlessFile;

import java.io.File;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.mbaas.util.FutureUtils.processResult;
import static com.mbaas.util.FutureUtils.waitUntilDoneWithPeriodicAction;

public final class DownloadFileToFolderCommand implements DownloadFileCommand
{
  private final BackendlessFile file;
  private final String destinationFolder;

  public DownloadFileToFolderCommand( BackendlessFile file, String destinationFolder )
  {
    this.file = file;
    this.destinationFolder = destinationFolder;
  }

  @Override
  public void execute()
  {
    final Future<File> download = file.download( destinationFolder );

    waitUntilDoneWithPeriodicAction( download, 2, TimeUnit.SECONDS,
                                     () -> System.out.println( "Downloading..." ) );

    processResult( download,
                   file -> System.out.println( "Successfully downloaded file to " + file.getAbsolutePath() ),
                   throwable -> System.err.println( "Failed to download file to " + destinationFolder + ": " +
                                                            throwable.getMessage() ) );
  }
}
