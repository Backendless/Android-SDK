package com.backendless.examples.filedownload.filedownloaddemo;

import com.backendless.Backendless;

public class Main {

  public static void main(String[] args)
  {
    Backendless.initApp( Defaults.APPLICATION_ID, Defaults.API_KEY );

    Person person = Backendless.Data.of( Person.class ).findById( Defaults.ID_PERSON );

    System.out.println( person.image.getFileURL() );

    new DownloadWithoutCancel().downloadMethods( person );

//    new DownloadWithCancel().downloadMethods( person );

    new DownloadWithTimeOut().downloadMethods( person );
  }

}
