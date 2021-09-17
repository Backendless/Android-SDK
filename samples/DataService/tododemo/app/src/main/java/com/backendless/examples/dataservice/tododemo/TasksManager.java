package com.backendless.examples.dataservice.tododemo;

import android.content.Context;

import com.backendless.Backendless;
import com.backendless.IDataStore;
import com.backendless.Messaging;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class TasksManager
{
  private static final IDataStore<Task> DATA_STORE = Backendless.Persistence.of( Task.class );
  private static final String DEFAULT_WHERE_CLAUSE = "deviceId = '" + Messaging.getDeviceId() + "'";
  private static final DataQueryBuilder dataQueryBuilder = DataQueryBuilder.create();

  static
  {
    dataQueryBuilder.setWhereClause( DEFAULT_WHERE_CLAUSE );
    dataQueryBuilder.setPageSize( 50 );
  }

  public static void remove( final Task entity, Context context,
                             final InnerCallback<Long> callback )
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

  public static void saveEntity( final Task entity, Context context,
                                 final InnerCallback<Task> callback )
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
    TasksManager.DATA_STORE.find( dataQueryBuilder, callback );
  }
}
