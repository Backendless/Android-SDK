/*
 * ********************************************************************************************************************
 *  <p/>
 *  BACKENDLESS.COM CONFIDENTIAL
 *  <p/>
 *  ********************************************************************************************************************
 *  <p/>
 *  Copyright 2012 BACKENDLESS.COM. All Rights Reserved.
 *  <p/>
 *  NOTICE: All information contained herein is, and remains the property of Backendless.com and its suppliers,
 *  if any. The intellectual and technical concepts contained herein are proprietary to Backendless.com and its
 *  suppliers and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret
 *  or copyright law. Dissemination of this information or reproduction of this material is strictly forbidden
 *  unless prior written permission is obtained from Backendless.com.
 *  <p/>
 *  ********************************************************************************************************************
 */

package com.backendless.files;

import android.widget.ProgressBar;
import com.backendless.Backendless;
import com.backendless.ThreadPoolService;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;

import java.io.File;
import java.io.OutputStream;


public class BackendlessFile
{
  private String fileURL;

  public BackendlessFile( String fileURL )
  {
    this.fileURL = fileURL;
  }

  public void setFileURL( String fileURL )
  {
    this.fileURL = fileURL;
  }

  public String getFileURL()
  {
    return fileURL;
  }

  public int remove() throws BackendlessException
  {
    return Backendless.Files.remove( fileURL );
  }

  public void remove( AsyncCallback<Integer> responder )
  {
    Backendless.Files.remove( fileURL, responder );
  }

  public void download( String localFilePathName, AsyncCallback<File> callback )
  {
    new FilesLoad().download( fileURL, localFilePathName, callback );
  }

  public void download( final OutputStream stream, final AsyncCallback<Void> callback )
  {
    new FilesLoad().download( fileURL, stream, callback );
  }

  public void download( final AsyncCallback<byte[]> callback )
  {
    new FilesLoad().download( fileURL, callback );
  }

  public void download( final String localFilePathName, final ProgressBar progressBar, final AsyncCallback<File> callback )
  {
    new FilesLoad().download( fileURL, localFilePathName, progressBar, callback );
  }

  public void download( final OutputStream stream, final ProgressBar progressBar, final AsyncCallback<Void> callback )
  {
    new FilesLoad().download( fileURL, stream, progressBar, callback );
  }

  public void download( final ProgressBar progressBar, final AsyncCallback<byte[]> callback )
  {
    new FilesLoad().download( fileURL, progressBar, callback );
  }

  /*
  public void download()
  {
    URL file = new URL( fileURL );
    ReadableByteChannel rbc = Channels.newChannel(file.openStream());
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
  }
  */

}
