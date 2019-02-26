package com.backendless;

import android.content.Context;
import weborb.util.io.ISerializer;
import weborb.util.io.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

class DelayedPersistence
{
  private static final Queue<Runnable> pendingOperations;
  private static final ScheduledExecutorService savingExecutor =
          Executors.newSingleThreadScheduledExecutor( new DefaultDaemonThreadFactory() );
  private static Future<?> savingTask = null;

  private static final int INITIAL_BACKOFF = 5;
  private static final long MAX_BACKOFF = TimeUnit.DAYS.toSeconds( 2 );
  private static final TimeUnit BACKOFF_TIME_UNIT = TimeUnit.SECONDS;
  private static int BACKOFF = INITIAL_BACKOFF;

  private static final String STORAGE_FILE_NAME = "backendless_delayed_persistence";

  static
  {
    pendingOperations = loadPendingOperationsFromStorage();
    resumeSaving();
  }

  static synchronized void queueSave( final Object objectToSave )
  {
    // put entity to the save queue
    pendingOperations.offer( new SaveOperation( objectToSave ) );
    savePendingOperationsToStorage( pendingOperations );

    resumeSaving();
  }

  static synchronized void resumeSaving()
  {
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
            while( !pendingOperations.isEmpty() )
            {
              // peek first, poll only after the entity is saved, because save may fail
              final Runnable operation = pendingOperations.peek();
              if( operation != null )
              {
                operation.run();
                pendingOperations.poll();
                savePendingOperationsToStorage( pendingOperations );

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

  private static void savePendingOperationsToStorage( Queue<Runnable> pendingOperations )
  {
    FileOutputStream storage = null;
    try
    {
      storage = openQueueStorageOutput();
      storage.write( Serializer.toBytes( pendingOperations, ISerializer.JSON ) );
    }
    catch( Exception ignored )
    {
    }
    finally
    {
      if( storage != null )
      {
        try
        {
          storage.close();
        }
        catch( IOException ignored )
        {
        }
      }
    }
  }

  private static Queue<Runnable> loadPendingOperationsFromStorage()
  {
    FileInputStream storage = null;
    try
    {
      storage = openQueueStorageInput();
      if( storage == null )
        return new ArrayDeque<>();

      ByteArrayOutputStream buffer = new ByteArrayOutputStream();

      int nRead;
      byte[] data = new byte[ 1024 ];

      while( (nRead = storage.read( data, 0, data.length )) != -1 )
      {
        buffer.write( data, 0, nRead );
      }

      final byte[] serializedQueue = buffer.toByteArray();

      return new ArrayDeque<>( Arrays.asList( (Runnable[]) Serializer.fromBytes( serializedQueue, ISerializer.JSON, false ) ) );
    }
    catch( Exception ignored )
    {
      return new ArrayDeque<>();
    }
    finally
    {
      if( storage != null )
      {
        try
        {
          storage.close();
        }
        catch( IOException ignored )
        {
        }
      }
    }
  }

  private static FileInputStream openQueueStorageInput()
  {
    if( Backendless.isAndroid() )
    {
      try
      {
        final Context context = ContextHandler.getAppContext();
        return context.openFileInput( STORAGE_FILE_NAME );
      }
      catch( FileNotFoundException ignored )
      {
        return null;
      }
    }
    else
    {
      try
      {
        return new FileInputStream( new File( STORAGE_FILE_NAME ) );
      }
      catch( FileNotFoundException ignored )
      {
        return null;
      }
    }
  }

  private static FileOutputStream openQueueStorageOutput()
  {
    if( Backendless.isAndroid() )
    {
      try
      {
        final Context context = ContextHandler.getAppContext();
        return context.openFileOutput( STORAGE_FILE_NAME, Context.MODE_PRIVATE );
      }
      catch( FileNotFoundException ignored )
      {
        return null;
      }
    }
    else
    {
      try
      {
        return new FileOutputStream( new File( STORAGE_FILE_NAME ), false );
      }
      catch( FileNotFoundException ignored )
      {
        return null;
      }
    }
  }

  /**
   * @see Executors : DefaultThreadFactory
   */
  static class DefaultDaemonThreadFactory implements ThreadFactory
  {
    private final ThreadGroup group;

    DefaultDaemonThreadFactory()
    {
      SecurityManager s = System.getSecurityManager();
      group = (s != null) ? s.getThreadGroup() :
              Thread.currentThread().getThreadGroup();
    }

    public Thread newThread( Runnable r )
    {
      Thread t = new Thread( group, r, "offline-saving-queue-thread" );
      t.setDaemon( true );

      if( t.getPriority() != Thread.NORM_PRIORITY )
        t.setPriority( Thread.NORM_PRIORITY );

      return t;
    }
  }

  public static class SaveOperation implements Runnable
  {
    private Object objectToSave;

    /**
     * @deprecated to be used by WebORB serializer only; use {@link #SaveOperation(Object)} instead
     */
    @Deprecated()
    public SaveOperation()
    {
    }

    SaveOperation( Object objectToSave )
    {
      this.objectToSave = objectToSave;
    }

    public Object getObjectToSave()
    {
      return objectToSave;
    }

    /**
     * @deprecated to be used by WebORB serializer only; use {@link #SaveOperation(Object)} instead
     */
    @Deprecated
    public void setObjectToSave( Object objectToSave )
    {
      this.objectToSave = objectToSave;
    }

    @Override
    public void run()
    {
      if( getObjectToSave() != null )
      {
        Backendless.Data.save( getObjectToSave() );
      }
    }
  }
}
