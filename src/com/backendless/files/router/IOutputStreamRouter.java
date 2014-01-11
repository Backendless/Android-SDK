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

package com.backendless.files.router;

import java.io.IOException;
import java.io.OutputStream;

public abstract class IOutputStreamRouter
{
  private OutputStream outputStream;

  public OutputStream getOutputStream()
  {
    return outputStream;
  }

  public void setOutputStream( OutputStream outputStream )
  {
    this.outputStream = outputStream;
  }

  public abstract void writeStream( int bufferSize ) throws IOException;

  public void flush() throws IOException
  {
    outputStream.flush();
  }

  public void close() throws IOException
  {
    outputStream.close();
  }
}
