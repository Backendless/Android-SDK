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

import java.util.concurrent.ThreadFactory;

class ThreadFactoryService implements ThreadFactory
{
  private final static String DEFAULT_THREAD_GROUP_NAME = "Backendless_thread_group";
  private final static ThreadGroup THREAD_GROUP;
  private static ThreadFactoryService instance;

  static
  {
    THREAD_GROUP = new ThreadGroup( DEFAULT_THREAD_GROUP_NAME );
  }

  @Override
  public Thread newThread( Runnable runnable )
  {
    return new Thread( THREAD_GROUP, runnable );
  }

  protected static ThreadFactoryService getThreadFactory()
  {
    if( instance == null )
      synchronized( ThreadFactoryService.class )
      {
        if( instance == null )
        {
          instance = new ThreadFactoryService();
        }
      }

    return instance;
  }
}
