package com.backendless.examples.filedownload.filedownloaddemo;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

class DownloadWithCancel {

  void downloadMethods( Person person )
  {
    FutureTask<Void> downloadWithLocalPathTask = new FutureTask<>(new Callable<Void>()
    {
      @Override
      public Void call() {
        new Downloader().downloadWithLocalPath( person );
        return null;
      }
    });
    ExecutorService downloadWithLocalPathExecutor = Executors.newSingleThreadExecutor();
    downloadWithLocalPathExecutor.execute(downloadWithLocalPathTask);
    downloadWithLocalPathTask.cancel(true);
    System.out.println("Download with localPath canceled");
    downloadWithLocalPathExecutor.shutdown();

    FutureTask<Void> downloadWithOutputStreamTask = new FutureTask<>(new Callable<Void>()
    {
      @Override
      public Void call() {
        new Downloader().downloadWithOutputStream( person );
        return null;
      }
    });
    ExecutorService downloadWithOutputStreamExecutor = Executors.newSingleThreadExecutor();
    downloadWithOutputStreamExecutor.execute(downloadWithOutputStreamTask);
    downloadWithOutputStreamTask.cancel(true);
    System.out.println("Download with OutputStream canceled");
    downloadWithOutputStreamExecutor.shutdown();

    FutureTask<Void> downloadByteArrayTask = new FutureTask<>(new Callable<Void>()
    {
      @Override
      public Void call() {
        new Downloader().downloadByteArray( person );
        return null;
      }
    });
    ExecutorService downloadByteArrayExecutor = Executors.newSingleThreadExecutor();
    downloadByteArrayExecutor.execute(downloadByteArrayTask);
    downloadByteArrayTask.cancel(true);
    System.out.println("Download ByteArray canceled");
    downloadByteArrayExecutor.shutdown();
  }

}
