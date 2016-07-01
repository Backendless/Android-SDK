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

package com.backendless;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.UploadCallback;
import com.backendless.async.message.AsyncMessage;
import com.backendless.async.message.AsyncUploadMessage;
import com.backendless.core.ResponseCarrier;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.files.router.FileOutputStreamRouter;

import java.io.File;

class UploadFileAsyncTask
{
  private UploadCallback uploadCallback;
  private AsyncCallback<BackendlessFile> responder;
  private boolean overwrite;

  void executeThis( File file, String path, UploadCallback uploadCallback,
                           AsyncCallback<BackendlessFile> responder )
  {
    executeThis( file, path, false, uploadCallback, responder );
  }

  void executeThis( File file, String path, boolean overwrite, UploadCallback uploadCallback,
                           AsyncCallback<BackendlessFile> responder )
  {
    this.uploadCallback = uploadCallback;
    this.responder = responder;
    this.overwrite = overwrite;
    doInBackground( file, path );
  }

  private void doInBackground( final File file, final String path )
  {
    final AsyncUploadMessage asyncUploadMessage = new AsyncUploadMessage( uploadCallback );

    ThreadPoolService.getPoolExecutor().execute( new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          FileOutputStreamRouter fileOutputStreamRouter = new FileOutputStreamRouter( file, new UploadCallback()
          {
            public void onProgressUpdate( Integer progress )
            {
              asyncUploadMessage.setCurrentProgress( progress );
              ResponseCarrier.getInstance().deliverMessage( asyncUploadMessage );
            }
          } );

          BackendlessFile result = Backendless.Files.uploadFromStream( fileOutputStreamRouter, file.getName(), path, overwrite );
          ResponseCarrier.getInstance().deliverMessage( new AsyncMessage<BackendlessFile>( result, responder ) );
        }
        catch( Exception e )
        {
          ResponseCarrier.getInstance().deliverMessage( new AsyncMessage<BackendlessFile>( new BackendlessFault( e ), responder ) );
        }
      }
    } );
  }
}
