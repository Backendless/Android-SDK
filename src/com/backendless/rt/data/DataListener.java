package com.backendless.rt.data;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessSerializer;
import com.backendless.rt.RTListener;
import com.backendless.rt.RTSubscription;
import weborb.exceptions.AdaptingException;
import weborb.types.IAdaptingType;

import java.util.HashMap;

public class DataListener<T> extends RTListener
{
  private final Class clazz;
  private final String tableName;

  DataListener( Class<T> clazz )
  {
    this.clazz = clazz;
    this.tableName = BackendlessSerializer.getSimpleName( clazz );
  }

  DataListener( String tableName )
  {
    this.clazz = HashMap.class;
    this.tableName = tableName;
  }

  public void addCreateListener( AsyncCallback<T> callback )
  {
    DataSubscription subscription = new DataSubscription( RTDataEvents.created, tableName );
    subscription.setCallback( createCallback( callback ) );
    addEventListener( subscription );
  }

  public void addCreateListener( String whereClause, AsyncCallback<T> callback )
  {
    DataSubscription subscription = new DataSubscription( RTDataEvents.created, tableName ).withWhere( whereClause );
    subscription.setCallback( createCallback( callback ) );
    addEventListener( subscription );
  }

  private AsyncCallback<IAdaptingType> createCallback( final AsyncCallback<T> callback )
  {
    return new AsyncCallback<IAdaptingType>()
    {
      @Override
      public void handleResponse( IAdaptingType response )
      {
        try
        {
          callback.handleResponse( (T) response.adapt( clazz ) );
        }
        catch( AdaptingException e )
        {
          callback.handleFault( new BackendlessFault( e.getMessage() ) );
        }
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        callback.handleFault( fault );
      }
    };
  }


}
