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

  protected static <T> IAtomic<T> createAtomicCounter( final String counterName, final Class<? extends T> type )
  {
    return new IAtomic<T>()
    {
      @Override
      public void reset()
      {
        Backendless.Counters.reset( counterName );
      }

      @Override
      public void reset( AsyncCallback responder )
      {
        Backendless.Counters.reset( counterName, responder );
      }

      @Override
      public T get()
      {
        return (T) Counters.convertToType( Backendless.Counters.get( counterName ), type );
      }

      @Override
      public void get( AsyncCallback<T> responder )
      {
        Backendless.Counters.get( counterName, responder );
      }

      @Override
      public T getAndIncrement()
      {
        return (T) Counters.convertToType( Backendless.Counters.getAndIncrement( counterName ), type );
      }

      public void getAndIncrement( AsyncCallback<T> responder )
      {
        Backendless.Counters.getAndIncrement( counterName, responder );
      }

      @Override
      public T incrementAndGet()
      {
        return  (T) Counters.convertToType( Backendless.Counters.incrementAndGet( counterName ), type );
      }

      public void incrementAndGet( AsyncCallback<T> responder )
      {
        Backendless.Counters.incrementAndGet( counterName, responder );
      }

      @Override
      public T getAndDecrement()
      {
        return  (T) Counters.convertToType( Backendless.Counters.getAndDecrement( counterName ), type );
      }

      public void getAndDecrement( AsyncCallback<T> responder )
      {
        Backendless.Counters.getAndDecrement( counterName, responder );
      }

      @Override
      public T decrementAndGet()
      {
        return  (T) Counters.convertToType( Backendless.Counters.decrementAndGet( counterName ), type );
      }

      public void decrementAndGet( AsyncCallback<T> responder )
      {
        Backendless.Counters.decrementAndGet( counterName, responder );
      }

      @Override
      public T addAndGet( Number value )
      {
        return  (T) Counters.convertToType( Backendless.Counters.addAndGet( counterName, value ), type );
      }

      public void addAndGet( Number value, AsyncCallback<T> responder )
      {
        Backendless.Counters.addAndGet( counterName, value, responder );
      }

      @Override
      public T getAndAdd( Number value )
      {
        return  (T) Counters.convertToType( Backendless.Counters.getAndAdd( counterName, value ), type );
      }

      public void getAndAdd( Number value, AsyncCallback<T> responder )
      {
        Backendless.Counters.getAndAdd( counterName, value, responder );
      }

      @Override
      public boolean compareAndSet( Number expected, Number updated )
      {
        return Backendless.Counters.compareAndSet( counterName, expected, updated );
      }

      public void compareAndSet( Number expected, Number updated, AsyncCallback<Boolean> responder )
      {
        Backendless.Counters.compareAndSet( counterName, expected, updated, responder );
      }
    };
  }
}
