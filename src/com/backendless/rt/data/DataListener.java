package com.backendless.rt.data;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessSerializer;
import com.backendless.rt.RTCallback;
import com.backendless.rt.RTListener;
import com.backendless.rt.RTSubscription;
import com.backendless.rt.SubscriptionNames;
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

  //--------create-------

  public void addCreateListener( AsyncCallback<T> callback )
  {
    DataSubscription subscription = new DataSubscription( RTDataEvents.created, tableName, createCallback( callback ) );
    addEventListener( subscription );
  }

  public void addCreateListener( String whereClause, AsyncCallback<T> callback )
  {
    DataSubscription subscription = new DataSubscription( RTDataEvents.created, tableName, createCallback( callback ) )
            .withWhere( whereClause );

    addEventListener( subscription );
  }

  public void removeCreateListeners()
  {
     removeListeners( RTDataEvents.created );
  }

  public void removeCreateListeners( final String whereClause, final AsyncCallback<T> callback )
  {
    removeListeners( RTDataEvents.created, whereClause, callback );
  }

  public void removeCreateListeners( final AsyncCallback<T> callback )
  {
    removeListeners( RTDataEvents.created, callback );
  }

  public void removeCreateListeners( final String whereClause )
  {
    removeListeners( RTDataEvents.created, whereClause );
  }

  //--------update-------

  public void addUpdateListener( AsyncCallback<T> callback )
  {
    DataSubscription subscription = new DataSubscription( RTDataEvents.updated, tableName, createCallback( callback ) );
    addEventListener( subscription );
  }

  public void addUpdateListener( String whereClause, AsyncCallback<T> callback )
  {
    DataSubscription subscription = new DataSubscription( RTDataEvents.updated, tableName, createCallback( callback ) )
            .withWhere( whereClause );

    addEventListener( subscription );
  }

  public void removeUpdateListeners()
  {
    removeListeners( RTDataEvents.updated );
  }

  public void removeUpdateListeners( final String whereClause, final AsyncCallback<T> callback )
  {
    removeListeners( RTDataEvents.updated, whereClause, callback );
  }

  public void removeUpdateListeners( final AsyncCallback<T> callback )
  {
    removeListeners( RTDataEvents.updated, callback );
  }

  public void removeUpdateListeners( final String whereClause )
  {
    removeListeners( RTDataEvents.updated, whereClause );
  }
  
  //--------remove-------

  public void addDeleteListener( AsyncCallback<T> callback )
  {
    DataSubscription subscription = new DataSubscription( RTDataEvents.deleted, tableName, createCallback( callback ) );
    addEventListener( subscription );
  }

  public void addDeleteListener( String whereClause, AsyncCallback<T> callback )
  {
    DataSubscription subscription = new DataSubscription( RTDataEvents.deleted, tableName, createCallback( callback ) )
            .withWhere( whereClause );

    addEventListener( subscription );
  }

  public void removeDeleteListeners()
  {
    removeListeners( RTDataEvents.deleted );
  }

  public void removeDeleteListeners( final String whereClause, final AsyncCallback<T> callback )
  {
    removeListeners( RTDataEvents.deleted, whereClause, callback );
  }

  public void removeDeleteListeners( final AsyncCallback<T> callback )
  {
    removeListeners( RTDataEvents.deleted, callback );
  }

  public void removeDeleteListeners( final String whereClause )
  {
    removeListeners( RTDataEvents.deleted, whereClause );
  }

  //--------bulk-update-------

  public void addBulkUpdateListener( AsyncCallback<T> callback )
  {
    DataSubscription subscription = new DataSubscription( RTDataEvents.bulk_updated, tableName, createCallback( callback ) );
    addEventListener( subscription );
  }

  public void addBulkUpdateListener( String whereClause, AsyncCallback<T> callback )
  {
    DataSubscription subscription = new DataSubscription( RTDataEvents.bulk_updated, tableName, createCallback( callback ) )
            .withWhere( whereClause );

    addEventListener( subscription );
  }

  public void removeBulkUpdateListeners()
  {
    removeListeners( RTDataEvents.bulk_updated );
  }

  public void removeBulkUpdateListeners( final String whereClause, final AsyncCallback<T> callback )
  {
    removeListeners( RTDataEvents.bulk_updated, whereClause, callback );
  }

  public void removeBulkUpdateListeners( final AsyncCallback<T> callback )
  {
    removeListeners( RTDataEvents.bulk_updated, callback );
  }

  public void removeBulkUpdateListeners( final String whereClause )
  {
    removeListeners( RTDataEvents.bulk_updated, whereClause );
  }

  //--------bulk-remove-------

  public void addBulkDeleteListener( AsyncCallback<T> callback )
  {
    DataSubscription subscription = new DataSubscription( RTDataEvents.bulk_deleted, tableName, createCallback( callback ) );
    addEventListener( subscription );
  }

  public void addBulkDeleteListener( String whereClause, AsyncCallback<T> callback )
  {
    DataSubscription subscription = new DataSubscription( RTDataEvents.bulk_deleted, tableName, createCallback( callback ) )
            .withWhere( whereClause );

    addEventListener( subscription );
  }

  public void removeBulkDeleteListeners()
  {
    removeListeners( RTDataEvents.bulk_deleted );
  }

  public void removeBulkDeleteListeners( final String whereClause, final AsyncCallback<T> callback )
  {
    removeListeners( RTDataEvents.bulk_deleted, whereClause, callback );
  }

  public void removeBulkDeleteListeners( final AsyncCallback<T> callback )
  {
    removeListeners( RTDataEvents.bulk_deleted, callback );
  }

  public void removeBulkDeleteListeners( final String whereClause )
  {
    removeListeners( RTDataEvents.bulk_deleted, whereClause );
  }

  //------end-------

  private void removeListeners( final RTDataEvents event )
  {
    removeEventListener( new Predicate()
    {
      @Override
      public boolean test( RTSubscription subscription )
      {
        return isCreateSubscription( subscription, event );
      }
    } );
  }

  private void removeListeners( final RTDataEvents event, final AsyncCallback<T> callback )
  {
    checkCallback( callback );
    removeEventListener( new Predicate()
    {
      @Override
      public boolean test( RTSubscription subscription )
      {
        return isCreateSubscription( subscription, event )
                && subscription.getCallback().usersCallback().equals( callback );
      }
    } );
  }

  private void removeListeners( final RTDataEvents event, final String whereClause, final AsyncCallback<T> callback )
  {
    checkCallback( callback );
    checkWhereClause( whereClause );
    removeEventListener( new Predicate()
    {
      @Override
      public boolean test( RTSubscription subscription )
      {
        return isCreateSubscription( subscription, event )
                && subscription.getCallback().usersCallback().equals( callback )
                && whereClause.equals(((DataSubscription)subscription).getWhereClause());
      }
    } );
  }

  private void removeListeners( final RTDataEvents event, final String whereClause )
  {
    checkWhereClause( whereClause );
    removeEventListener( new Predicate()
    {
      @Override
      public boolean test( RTSubscription subscription )
      {
        return isCreateSubscription( subscription, event )
                && whereClause.equals(((DataSubscription)subscription).getWhereClause());
      }
    } );
  }

  private boolean isCreateSubscription( RTSubscription subscription, RTDataEvents event )
  {
    if( !(subscription instanceof DataSubscription))
      return false;

    DataSubscription dataSubscription = (DataSubscription) subscription;

    if(dataSubscription.getSubscriptionName() == SubscriptionNames.OBJECTS_CHANGES
            && dataSubscription.getEvent() == event )
    {
      return true;
    }

    return false;
  }

  private RTCallback<T> createCallback( final AsyncCallback<T> callback )
  {
    checkCallback( callback );

    return new RTCallback<T>()
    {
      @Override
      public AsyncCallback<T> usersCallback()
      {
        return callback;
      }

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

  private void checkCallback( AsyncCallback<T> callback )
  {
    if( callback == null )
      throw new IllegalArgumentException( "Callback can not be null" );
  }

  private void checkWhereClause( String whereClause )
  {
    if( whereClause == null )
      throw new IllegalArgumentException( "whereClause can not be null" );
  }
}
