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

import com.backendless.Backendless;
import com.backendless.async.message.IAsyncMessage;

public class ResponseCarrier
{
  private static final IHandleCarrier HANDLE_CARRIER;

  private static volatile ResponseCarrier instance;
  private static final Object carrierLock = new Object();

  private ResponseCarrier()
  {
  }

  public static ResponseCarrier getInstance()
  {
    if( instance == null )
      synchronized( carrierLock )
      {
        if( instance == null )
          instance = new ResponseCarrier();
      }

    return instance;
  }

  static
  {
    HANDLE_CARRIER = Backendless.isAndroid() ? new AndroidCarrier() : new JavaCarrier();
  }

  public <T> void deliverMessage( IAsyncMessage<T> asyncMessage )
  {
    HANDLE_CARRIER.deliverMessage( asyncMessage );
  }
}
