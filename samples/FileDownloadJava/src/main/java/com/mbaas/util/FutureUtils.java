package com.mbaas.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class FutureUtils
{
  public static void waitUntilDoneWithPeriodicAction( Future<?> task, int period, TimeUnit timeUnit, Runnable action )
  {
    while( !task.isDone() )
    {
      action.run();

      try
      {
        timeUnit.sleep( period );
      }
      catch( InterruptedException e )
      {
        // should not happen
        throw new RuntimeException( e );
      }
    }
  }

  public static <T> void processResult( Future<T> task, Consumer<T> onSuccess, Consumer<Throwable> onError )
  {
    try
    {
      final T result = task.get();
      onSuccess.accept( result );
    }
    catch( InterruptedException e )
    {
      // should not happen
      throw new RuntimeException( e );
    }
    catch( ExecutionException e )
    {
      onError.accept( e.getCause() );
    }
  }
}
