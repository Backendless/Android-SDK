package com.backendless.files;

import android.os.AsyncTask;
import android.widget.ProgressBar;

import java.io.File;
import java.io.OutputStream;
import java.util.concurrent.Future;

/**********************************************************************************************************************
 * BACKENDLESS.COM CONFIDENTIAL
 * <p>
 * *********************************************************************************************************************
 * <p>
 * Copyright 2012 BACKENDLESS.COM. All Rights Reserved.
 * <p>
 * NOTICE:  All information contained herein is, and remains the property of Backendless.com and its suppliers,
 * if any.  The intellectual and technical concepts contained herein are proprietary to Backendless.com and its
 * suppliers and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret
 * or copyright law. Dissemination of this information or reproduction of this material is strictly forbidden
 * unless prior written permission is obtained from Backendless.com.
 * <p>
 * CREATED ON: 6/10/16
 * AT: 12:37 PM
 **********************************************************************************************************************/
public class BackendlessFileAndroid extends BackendlessFile
{

  public BackendlessFileAndroid( String fileURL )
  {
    super( fileURL );
  }

  public AsyncTask getDownloadTask( String localDirectoryPath )
  {
    return new DownloadTask( getFileURL(), localDirectoryPath );
  }

  public Future<File> download( String localFilePathName, ProgressBar progressBar )
  {
    return new FileDownloadAndroid().download( getFileURL(), localFilePathName, progressBar );
  }

  public Future<Void> download( OutputStream stream, ProgressBar progressBar )
  {
    return new FileDownloadAndroid().download( getFileURL(), stream, progressBar );
  }

  public Future<byte[]> download( ProgressBar progressBar )
  {
    return new FileDownloadAndroid().download( getFileURL(), progressBar );
  }

}
