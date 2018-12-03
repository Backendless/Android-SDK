package com.mbaas.util;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
}
