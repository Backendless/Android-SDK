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

package com.backendless.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by baas on 03.09.15.
 */
public abstract class ScheduledExecutor
{
  private static final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
  private ScheduledFuture<?> scheduledFuture;
  private int timeFrequency;

  protected abstract void calculate();

  protected void doTask()
  {
    calculate();
    setupTimer();
  }

  protected void setTimeFrequency(int timeFrequency)
  {
    this.timeFrequency = timeFrequency;
    setupTimer();
  }

  private void setupTimer()
  {
    if( scheduledFuture != null )
    {
      scheduledFuture.cancel( true );
      scheduledFuture = null;
    }

    if( timeFrequency > 0 )
    {
      scheduledTask();
    }
  }

  private void scheduledTask()
  {
    scheduledFuture = scheduledExecutorService.schedule( new Runnable()
    {
      @Override
      public void run()
      {
        doTask();
      }
    }, timeFrequency, TimeUnit.MILLISECONDS );
  }
}
