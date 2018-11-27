package com.backendless.files;

import android.os.AsyncTask;
import android.widget.ProgressBar;
import com.backendless.async.callback.AsyncCallback;

import java.io.File;
import java.io.OutputStream;

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

  public void download( String localFilePathName, ProgressBar progressBar, AsyncCallback<File> callback )
  {
    new FilesLoad().download( getFileURL(), localFilePathName, progressBar, callback );
  }

  public void download(OutputStream stream, ProgressBar progressBar, AsyncCallback<Void> callback )
  {
    new FilesLoad().download( getFileURL(), stream, progressBar, callback );
  }

  public void download(ProgressBar progressBar, AsyncCallback<byte[]> callback )
  {
    new FilesLoad().download( getFileURL(), progressBar, callback );
  }
}
