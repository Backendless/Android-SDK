package com.backendless.rt;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.rt.messaging.MessagingSubscription;
import weborb.types.IAdaptingType;

import java.util.concurrent.CopyOnWriteArrayList;

public abstract class ConnectListener<T extends RTSubscription>
{
  private final CopyOnWriteArrayList<AsyncCallback<Void>> connectedCallbacks = new CopyOnWriteArrayList<>();
  private final RTClient rtClient = RTClientFactory.get();
  private volatile boolean connected;
  private final T connectSubscription;

  public ConnectListener( String subject  )
  {
    this.connectSubscription = createConnectSubscription( subject  );
  }

  public void connect( )
  {
    rtClient.subscribe( connectSubscription );
  }

  private T createConnectSubscription( String subject )
  {
    return createSubscription( new RTCallback()
    {
      @Override
      public AsyncCallback usersCallback()
      {
        return null;
      }

      @Override
      public void handleResponse( IAdaptingType response )
      {
        connected = true;
        for( AsyncCallback<Void> connectedCallback : connectedCallbacks )
        {
          connectedCallback.handleResponse( null );
        }

        connected();
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        connected = false;
        for( AsyncCallback<Void> connectedCallback : connectedCallbacks )
        {
          connectedCallback.handleFault( fault );
        }
      }
    } );
  }

  public void disconnect()
  {
    rtClient.unsubscribe( connectSubscription.getId() );
    connected = false;
  }

  public boolean isConnected()
  {
    return connected;
  }

  public void addConnectListener( AsyncCallback<Void> callback )
  {
    if( connected )
      callback.handleResponse( null );

    connectedCallbacks.add( callback );
  }

  public void removeConnectListener( AsyncCallback<Void> callback )
  {
    connectedCallbacks.remove( callback );
  }

  public void removeConnectListeners(  )
  {
    connectedCallbacks.clear();
  }

  public abstract void connected();
  public abstract T createSubscription( RTCallback callback );
}
