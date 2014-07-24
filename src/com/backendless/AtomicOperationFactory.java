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

public class AtomicOperationFactory
{
  private AtomicOperationFactory()
  {
  }

  protected static IAtomic createAtomicCounter( final String counterName )
  {
    return new IAtomic()
    {

      @Override
      public Long getAndIncrement()
      {
        return Backendless.Counters.getAndIncrement( counterName );
      }

      public void getAndIncrement( AsyncCallback<Long> responder )
      {
        Backendless.Counters.getAndIncrement( counterName, responder );
      }

      @Override
      public Long incrementAndGet()
      {
        return Backendless.Counters.incrementAndGet( counterName );
      }

      public void incrementAndGet( AsyncCallback<Long> responder )
      {
        Backendless.Counters.incrementAndGet( counterName, responder );
      }

      @Override
      public Long getAndDecrement()
      {
        return Backendless.Counters.getAndDecrement( counterName );
      }

      public void getAndDecrement( AsyncCallback<Long> responder )
      {
        Backendless.Counters.getAndDecrement( counterName, responder );
      }

      @Override
      public Long decrementAndGet()
      {
        return Backendless.Counters.decrementAndGet( counterName );
      }

      public void decrementAndGet( AsyncCallback<Long> responder )
      {
        Backendless.Counters.decrementAndGet( counterName, responder );
      }

      @Override
      public Long addAndGet( Long value )
      {
        return Backendless.Counters.addAndGet( counterName, value );
      }

      public void addAndGet( Long value, AsyncCallback<Long> responder )
      {
        Backendless.Counters.addAndGet( counterName, value, responder );
      }

      @Override
      public Long getAndAdd( Long value )
      {
        return Backendless.Counters.getAndAdd( counterName, value );
      }

      public void getAndAdd( Long value, AsyncCallback<Long> responder )
      {
        Backendless.Counters.getAndAdd( counterName, value, responder );
      }

      @Override
      public boolean compareAndSet( Long expected, Long updated )
      {
        return Backendless.Counters.compareAndSet( counterName, expected, updated );
      }

      public void compareAndSet( Long expected, Long updated, AsyncCallback<Boolean> responder )
      {
        Backendless.Counters.compareAndSet( counterName, expected, updated, responder );
      }
    };
  }
}
