package com.backendless.examples.filedownload.filedownloaddemo;

public class DownloadWithoutCancel {

  void downloadMethods( Person person)
  {
    new Downloader().downloadWithLocalPath( person );

    new Downloader().downloadWithOutputStream( person );

    new Downloader().downloadByteArray( person );
  }

}
