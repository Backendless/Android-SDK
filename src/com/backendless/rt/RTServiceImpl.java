package com.backendless.rt;

import com.backendless.async.callback.Fault;
import com.backendless.async.callback.Result;
import com.backendless.exceptions.BackendlessFault;

import java.util.concurrent.CopyOnWriteArrayList;

public class RTServiceImpl implements RTService
{
  private final RTClient rtClient = RTClientFactory.get();

  private final CopyOnWriteArrayList<Result<Void>> connectListeners = new CopyOnWriteArrayList<>();
  private final CopyOnWriteArrayList<Result<String>> disconnectListeners = new CopyOnWriteArrayList<>();
  private final CopyOnWriteArrayList<Result<ReconnectAttempt>> reconnectListeners = new CopyOnWriteArrayList<>();
  private final CopyOnWriteArrayList<Fault> connectErrorListeners = new CopyOnWriteArrayList<>();

  public RTServiceImpl()
  {
    if(rtClient.isAvailable())
    {
      rtClient.setConnectEventListener( new Result<Void>()
      {
        @Override
        public void handle( Void result )
        {
          RTServiceImpl.this.handle( result, connectListeners );
        }
      } );

      rtClient.setDisconnectEventListener( new Result<String>()
      {
        @Override
        public void handle( String result )
        {
          RTServiceImpl.this.handle( result, disconnectListeners );
        }
      } );

      rtClient.setConnectErrorEventListener( new Fault()
      {
        @Override
        public void handle( BackendlessFault fault )
        {
          RTServiceImpl.this.handle( fault, connectErrorListeners );
        }
      } );

      rtClient.setReconnectAttemptEventListener( new Result<ReconnectAttempt>()
      {
        @Override
        public void handle( ReconnectAttempt result )
        {
          RTServiceImpl.this.handle( result, reconnectListeners );
        }
      } );
    }
  }

  @Override
  public void addConnectListener( Result<Void> callback )
  {
    if( rtClient.isConnected() )
      callback.handle( null );

    connectListeners.add( callback );
  }

  @Override
  public void addReconnectAttemptListener( Result<ReconnectAttempt> callback )
  {
    reconnectListeners.add( callback );
  }

  @Override
  public void addConnectErrorListener( Fault fault )
  {
    connectErrorListeners.add( fault );
  }

  @Override
  public void removeConnectionListeners()
  {
    connectListeners.clear();
  }

  @Override
  public void addDisconnectListener( Result<String> callback )
  {
    disconnectListeners.add( callback );
  }

  @Override
  @SuppressWarnings( "unchecked" )
  public <T extends Result> void removeListener( T callback )
  {
    if( connectErrorListeners.contains( callback ) )
    {
      removeEventListeners( (Fault) callback, connectErrorListeners );
    }
    else if( disconnectListeners.contains( callback ) )
    {
      removeEventListeners( callback, disconnectListeners );
    }
    else if( connectListeners.contains( callback ) )
    {
      removeEventListeners( callback, connectListeners );
    }
    else if( reconnectListeners.contains( callback ) )
    {
      removeEventListeners( callback, reconnectListeners );
    }
  }

  @Override
  public void connect()
  {
    rtClient.connect();
  }

  @Override
  public void disconnect()
  {
    rtClient.disconnect();
  }

  private <T extends Result> void removeEventListeners( T callback, CopyOnWriteArrayList<T> listeners )
  {
    listeners.remove( callback );
  }

  @SuppressWarnings( "unchecked" )
  private <T, E extends Result> void handle( T result, CopyOnWriteArrayList<E> listeners )
  {
    for( E listener : listeners )
    {
      listener.handle( result );
    }
  }
}
