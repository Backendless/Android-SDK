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
import java.util.concurrent.TimeUnit;

class DelayedPersistence
{
  private static final Queue<Object> entitiesToSave;
  private static final ScheduledExecutorService savingExecutor = Executors.newSingleThreadScheduledExecutor();
  private static Future<?> savingTask = null;

  private static final int INITIAL_BACKOFF = 5;
  private static final long MAX_BACKOFF = TimeUnit.DAYS.toSeconds( 2 );
  private static final TimeUnit BACKOFF_TIME_UNIT = TimeUnit.SECONDS;
  private static int BACKOFF = INITIAL_BACKOFF;

  private static final String STORAGE_FILE_NAME = "backendless_delayed_persistence";

  static
  {
    entitiesToSave = loadQueueFromStorage();
    resumeSaving();
  }

  static synchronized void queueSave( final Object entity )
  {
    // put entity to the save queue
    entitiesToSave.offer( entity );
    saveQueueToStorage( entitiesToSave );

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
            while( !entitiesToSave.isEmpty() )
            {
              // peek first, poll only after the entity is saved, because save may fail
              final Object entity = entitiesToSave.peek();
              if( entity != null )
              {
                Backendless.Data.save( entity );
                entitiesToSave.poll();
                saveQueueToStorage( entitiesToSave );

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

  private static void saveQueueToStorage( Queue<Object> queue )
  {
    FileOutputStream storage = null;
    try
    {
      storage = openQueueStorageOutput();
      storage.write( Serializer.toBytes( queue, ISerializer.JSON ) );
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

  private static Queue<Object> loadQueueFromStorage()
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

      return new ArrayDeque<>( Arrays.asList( (Object[]) Serializer.fromBytes( serializedQueue, ISerializer.JSON, false ) ) );
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
    final Context context = ContextHandler.getAppContext();
    if( context != null ) // Android environment
    {
      try
      {
        return context.openFileInput( STORAGE_FILE_NAME );
      }
      catch( FileNotFoundException ignored )
      {
        return null;
      }
    }
    else // Java environment
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
    final Context context = ContextHandler.getAppContext();
    if( context != null ) // Android environment
    {
      try
      {
        return context.openFileOutput( STORAGE_FILE_NAME, Context.MODE_PRIVATE );
      }
      catch( FileNotFoundException ignored )
      {
        return null;
      }
    }
    else // Java environment
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
}
