package com.backendless.examples.filedownload.filedownloaddemo;

import java.util.concurrent.FutureTask;

class DownloadWithCancel
{

  void downloadMethods( Person person, int timeAborted ) throws InterruptedException
  {
    FutureTask<Void> downloadWithLocalPathTask = new Downloader().downloadWithLocalPath( person );
    Thread.sleep(timeAborted);
    downloadWithLocalPathTask.cancel(true);
    System.out.println("Download with localPath canceled");

    FutureTask<Void> downloadWithOutputStreamTask = new Downloader().downloadWithOutputStream( person );
    Thread.sleep(timeAborted);
    downloadWithOutputStreamTask.cancel(true);
    System.out.println("Download with OutputStream canceled");

    FutureTask<Void> downloadByteArrayTask = new Downloader().downloadByteArray( person );
    Thread.sleep(timeAborted);
    downloadByteArrayTask.cancel(true);
    System.out.println("Download ByteArray canceled");
  }

}
