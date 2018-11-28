package com.backendless.examples.filedownload.filedownloaddemo;

import java.util.concurrent.*;

class DownloadWithTimeOut {

  void downloadMethods( Person person )
  {
    ScheduledExecutorService downloadWithLocalExecutor = Executors.newSingleThreadScheduledExecutor();
    final Future<Void> downloadWithLocalHandler = downloadWithLocalExecutor.submit(new Callable<Void>(){
      @Override
      public Void call() throws Exception {
        new Downloader().downloadWithLocalPath( person );
        return null;
      }
    });
    downloadWithLocalExecutor.schedule(new Runnable(){
      public void run(){
        downloadWithLocalHandler.cancel(true);
      }
    }, 10000, TimeUnit.MILLISECONDS);

    ScheduledExecutorService downloadWithOutputStreamExecutor = Executors.newSingleThreadScheduledExecutor();
    final Future<Void> downloadWithOutputStreamHandler = downloadWithOutputStreamExecutor.submit(new Callable<Void>(){
      @Override
      public Void call() throws Exception {
        new Downloader().downloadWithOutputStream( person );
        return null;
      }
    });
    downloadWithOutputStreamExecutor.schedule(new Runnable(){
      public void run(){
        downloadWithOutputStreamHandler.cancel(true);
      }
    }, 10000, TimeUnit.MILLISECONDS);

    ScheduledExecutorService downloadByteArrayExecutor = Executors.newSingleThreadScheduledExecutor();
    final Future<Void> downloadByteArrayHandler = downloadByteArrayExecutor.submit(new Callable<Void>(){
      @Override
      public Void call() throws Exception {
        new Downloader().downloadByteArray( person );
        return null;
      }
    });
    downloadByteArrayExecutor.schedule(new Runnable(){
      public void run(){
        downloadByteArrayHandler.cancel(true);
      }
    }, 10000, TimeUnit.MILLISECONDS);
  }

}
