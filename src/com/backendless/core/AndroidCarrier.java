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

package com.backendless.core;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.backendless.async.message.IAsyncMessage;

class AndroidCarrier implements IHandleCarrier
{
  private static final Handler handler;

  static
  {
    handler = new Handler( Looper.getMainLooper(), new Handler.Callback()
    {
      @Override
      public boolean handleMessage( Message message )
      {
        ((IAsyncMessage) message.obj).handleCallback();

        return true;
      }
    } );
  }

  public <T> void deliverMessage( IAsyncMessage<T> asyncMessage )
  {
    Message message = new Message();
    message.obj = asyncMessage;
    handler.sendMessage( message );
  }
}
