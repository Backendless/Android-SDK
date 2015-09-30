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

import android.graphics.Bitmap;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.message.AsyncMessage;
import com.backendless.core.ResponseCarrier;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.files.router.BitmapOutputStreamRouter;

class UploadBitmapAsyncTask
{
  private AsyncCallback<BackendlessFile> responder;
  private boolean overwrite;

  void executeThis( Bitmap bitmap, Bitmap.CompressFormat compressFormat, int quality, String name, String path,
      AsyncCallback<BackendlessFile> responder )
  {
    executeThis( bitmap, compressFormat, quality, name, path, false, responder );
  }

  void executeThis( Bitmap bitmap, Bitmap.CompressFormat compressFormat, int quality, String name, String path, boolean overwrite,
                           AsyncCallback<BackendlessFile> responder )
  {
    this.responder = responder;
    this.overwrite = overwrite;
    doInBackground( bitmap, compressFormat, quality, name, path );
  }

  private void doInBackground( final Bitmap bitmap, final Bitmap.CompressFormat compressFormat, final int quality,
                               final String name, final String path )
  {
    ThreadPoolService.getPoolExecutor().execute( new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          BitmapOutputStreamRouter bitmapOutputStreamRouter = new BitmapOutputStreamRouter( bitmap, compressFormat, quality );
          BackendlessFile result = Backendless.Files.uploadFromStream( bitmapOutputStreamRouter, name, path, overwrite );
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