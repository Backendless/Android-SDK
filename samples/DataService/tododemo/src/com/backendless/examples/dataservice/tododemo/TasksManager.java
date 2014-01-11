package com.backendless.examples.dataservice.tododemo;

import android.content.Context;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.IDataStore;
import com.backendless.Messaging;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import java.util.List;

public class TasksManager
{
  private static final IDataStore<Task> DATA_STORE = Backendless.Persistence.of( Task.class );
  private static final String DEFAULT_WHERE_CLAUSE = "deviceId = '" + Messaging.DEVICE_ID + "'";
  private static final BackendlessDataQuery backendlessDataQuery = new BackendlessDataQuery();

  static
  {
    backendlessDataQuery.setWhereClause( DEFAULT_WHERE_CLAUSE );
    backendlessDataQuery.setQueryOptions( new QueryOptions( 50, 0 ) );
  }

  public static void remove( final Task entity, Context context, final InnerCallback<Long> callback )
  {
    DATA_STORE.remove( entity, new DefaultCallback<Long>( context )
    {
      @Override
      public void handleResponse( Long response )
      {
        super.handleResponse( response );

        if( callback != null )
          callback.handleResponse( response );
      }
    } );
  }

  public static void saveEntity( final Task entity, Context context, final InnerCallback<Task> callback )
  {
    DATA_STORE.save( entity, new DefaultCallback<Task>( context )
    {
      @Override
      public void handleResponse( Task response )
      {
        super.handleResponse( response );

        if( callback != null )
          callback.handleResponse( response );
      }
    } );
  }

  public static void findEntities( final AsyncCallback<List<Task>> callback )
  {
    TasksManager.DATA_STORE.find( backendlessDataQuery, new AsyncCallback<BackendlessCollection<Task>>()
    {
      @Override
      public void handleResponse( BackendlessCollection<Task> response )
      {
        callback.handleResponse( response.getCurrentPage() );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        callback.handleFault( fault );
      }
    }

    );
  }
}
