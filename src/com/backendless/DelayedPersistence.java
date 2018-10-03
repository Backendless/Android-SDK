package com.backendless;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class DelayedPersistence
{
  private static final Queue<Object> entitiesToSave = new ArrayDeque<>();
  private static final ScheduledExecutorService savingExecutor = Executors.newSingleThreadScheduledExecutor();
  private static Future<?> savingTask = null;

  private static final int INITIAL_BACKOFF = 5;
  private static final long MAX_BACKOFF = TimeUnit.DAYS.toSeconds( 2 );
  private static final TimeUnit BACKOFF_TIME_UNIT = TimeUnit.SECONDS;
  private static int BACKOFF = INITIAL_BACKOFF;

  static synchronized void queueSave( final Object entity )
  {
    // put entity to the save queue
    entitiesToSave.offer( entity );

    // if there's no saving task running currently, create one
    if( savingTask == null || savingTask.isDone() )
    {
      savingTask = savingExecutor.submit( new Runnable()
      {
        @Override
        public void run()
        {
          try
          {
            // try to save all queued entities
            while( !entitiesToSave.isEmpty() )
            {
              // peek first, poll only after the entity is saved, because save may fail
              final Object entity = entitiesToSave.peek();
              if( entity != null )
              {
                Backendless.Data.save( entity );
                entitiesToSave.poll();

                // reset backoff on any successful save
                BACKOFF = INITIAL_BACKOFF;
              }
            }
          }
          catch( Exception e )
          {
            if( BACKOFF < MAX_BACKOFF )
            {
              savingTask = savingExecutor.schedule( this, BACKOFF, BACKOFF_TIME_UNIT );
              BACKOFF *= 2;
            }
          }
        }
      } );
    }
  }
}
