package com.mbaas.command;

import com.backendless.files.BackendlessFile;

import java.io.File;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.mbaas.util.FutureUtils.processResult;
import static com.mbaas.util.FutureUtils.waitUntilDoneWithPeriodicAction;

public final class DownloadFileToFolderSpecifyingNameCommand implements DownloadFileCommand
{
  private final BackendlessFile file;
  private final String destinationFolder;
  private final String fileName;

  public DownloadFileToFolderSpecifyingNameCommand( BackendlessFile file, String destinationFolder,
                                                    String fileName )
  {
    this.file = file;
    this.destinationFolder = destinationFolder;
    this.fileName = fileName;
  }

  @Override
  public void execute()
  {
    final Future<File> download = file.download( destinationFolder, fileName );

    waitUntilDoneWithPeriodicAction( download, 2, TimeUnit.SECONDS,
                                     () -> System.out.println( "Downloading..." ) );

    processResult( download,
                   file -> System.out.println( "Successfully downloaded file to " + file.getAbsolutePath() ),
                   throwable -> System.err.println( "Failed to download file to " + destinationFolder + " " +
                                                            "with name " + fileName + ": " +
                                                            throwable.getMessage() ) );
  }
}
