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
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.files.BackendlessFile;
import com.backendless.files.router.BitmapOutputStreamRouter;

public final class FilesAndroidExtra
{
  private static final FilesAndroidExtra instance = new FilesAndroidExtra();

  static FilesAndroidExtra getInstance()
  {
    return instance;
  }

  private FilesAndroidExtra()
  {}

  public BackendlessFile upload( android.graphics.Bitmap bitmap,
                                 android.graphics.Bitmap.CompressFormat compressFormat, int quality, String name,
                                 String path ) throws Exception
  {
    checkBitmapAndPath( bitmap, compressFormat, path );

    return Backendless.Files.uploadFromStream( new BitmapOutputStreamRouter( bitmap, compressFormat, quality ), name, path );
  }

  public void upload( final android.graphics.Bitmap bitmap,
                      final android.graphics.Bitmap.CompressFormat compressFormat, final int quality, String name,
                      String path, final AsyncCallback<BackendlessFile> responder )
  {
    try
    {
      checkBitmapAndPath( bitmap, compressFormat, path );
      new UploadBitmapAsyncTask().executeThis( bitmap, compressFormat, quality, name, path, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  private void checkBitmapAndPath( android.graphics.Bitmap bitmap,
                                   android.graphics.Bitmap.CompressFormat compressFormat, String path )
  {
    if( bitmap == null )
      throw new NullPointerException( ExceptionMessage.NULL_BITMAP );

    if( compressFormat == null )
      throw new NullPointerException( ExceptionMessage.NULL_COMPRESS_FORMAT );

    if( path == null || path.equals( "" ) )
      throw new IllegalArgumentException( ExceptionMessage.NULL_PATH );
  }
}
