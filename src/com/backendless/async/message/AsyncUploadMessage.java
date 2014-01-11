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

package com.backendless.async.message;

import com.backendless.async.callback.UploadCallback;

public class AsyncUploadMessage implements IAsyncMessage<UploadCallback>
{
  private UploadCallback uploadCallback;
  private int currentProgress;

  public AsyncUploadMessage( UploadCallback uploadCallback )
  {
    this.uploadCallback = uploadCallback;
  }

  public void setCurrentProgress( int currentProgress )
  {
    this.currentProgress = currentProgress;
  }

  @Override
  public void handleCallback()
  {
    if(uploadCallback != null)
      uploadCallback.onProgressUpdate( currentProgress );
  }
}
