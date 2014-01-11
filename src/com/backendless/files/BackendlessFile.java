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

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;

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

  public void remove() throws BackendlessException
  {
    Backendless.Files.remove( fileURL );
  }

  public void remove( AsyncCallback<Void> responder )
  {
    Backendless.Files.remove( fileURL, responder );
  }
}
