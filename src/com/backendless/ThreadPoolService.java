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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolService
{
  private static volatile ExecutorService threadPoolExecutor;
  private final static int corePoolSize = 10;

  public static ExecutorService getPoolExecutor()
  {
    if( threadPoolExecutor == null )
    {
      synchronized( ThreadPoolService.class )
      {
        if( threadPoolExecutor == null )
          threadPoolExecutor = Executors.newFixedThreadPool( corePoolSize );
      }
    }

    return threadPoolExecutor;
  }
}
