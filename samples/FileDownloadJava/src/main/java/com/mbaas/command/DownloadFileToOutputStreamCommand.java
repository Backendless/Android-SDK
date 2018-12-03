package com.mbaas.command;

import com.backendless.files.BackendlessFile;

import java.io.OutputStream;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.mbaas.util.FutureUtils.processResult;
import static com.mbaas.util.FutureUtils.waitUntilDoneWithPeriodicAction;

public final class DownloadFileToOutputStreamCommand implements DownloadFileCommand
{
  private final BackendlessFile file;
  private final OutputStream outputStream;

  public DownloadFileToOutputStreamCommand( BackendlessFile file, OutputStream outputStream )
  {
    this.file = file;
    this.outputStream = outputStream;
  }

  @Override
  public void execute()
  {
    final Future<Void> download = file.download( outputStream );

    waitUntilDoneWithPeriodicAction( download, 2, TimeUnit.SECONDS,
                                     () -> System.out.println( "Downloading..." ) );

    processResult( download,
                   v -> System.out.println( "Successfully downloaded file to specified OutputStream" ),
                   throwable -> System.err.println( "Failed to download file to OutputStream: " +
                                                            throwable.getMessage() ) );
  }
}
