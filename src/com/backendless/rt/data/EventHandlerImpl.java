package com.backendless.rt.data;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessSerializer;
import com.backendless.rt.RTCallback;
import com.backendless.rt.RTListenerImpl;
import com.backendless.rt.RTSubscription;
import com.backendless.rt.SubscriptionNames;
import weborb.exceptions.AdaptingException;
import weborb.types.IAdaptingType;

import java.util.HashMap;

public class EventHandlerImpl<T> extends RTListenerImpl implements EventHandler<T>
{
  private final Class clazz;
  private final String tableName;

  EventHandlerImpl( Class<T> clazz )
  {
    this.clazz = clazz;
    this.tableName = BackendlessSerializer.getSimpleName( clazz );
  }

  EventHandlerImpl( String tableName )
  {
    this.clazz = HashMap.class;
    this.tableName = tableName;
  }

  //--------create-------

  @Override
  public void addCreateListener( AsyncCallback<T> callback )
  {
    DataSubscription subscription = new DataSubscription( RTDataEvents.created, tableName, createCallback( callback ) );
    addEventListener( subscription );
  }

  @Override
  public void addCreateListener( String whereClause, AsyncCallback<T> callback )
  {
    DataSubscription subscription = new DataSubscription( RTDataEvents.created, tableName, createCallback( callback ) )
            .withWhere( whereClause );

    addEventListener( subscription );
  }

  @Override
  public void removeCreateListeners()
  {
     removeListeners( RTDataEvents.created );
  }

  @Override
  public void removeCreateListener( final String whereClause, final AsyncCallback<T> callback )
  {
    removeListeners( RTDataEvents.created, whereClause, callback );
  }

  @Override
  public void removeCreateListener( final AsyncCallback<T> callback )
  {
    removeListeners( RTDataEvents.created, callback );
  }

  @Override
  public void removeCreateListeners( final String whereClause )
  {
    removeListeners( RTDataEvents.created, whereClause );
  }

  //--------update-------

  @Override
  public void addUpdateListener( AsyncCallback<T> callback )
  {
    DataSubscription subscription = new DataSubscription( RTDataEvents.updated, tableName, createCallback( callback ) );
    addEventListener( subscription );
  }

  @Override
  public void addUpdateListener( String whereClause, AsyncCallback<T> callback )
  {
    DataSubscription subscription = new DataSubscription( RTDataEvents.updated, tableName, createCallback( callback ) )
            .withWhere( whereClause );

    addEventListener( subscription );
  }

  @Override
  public void removeUpdateListeners()
  {
    removeListeners( RTDataEvents.updated );
  }

  @Override
  public void removeUpdateListener( final String whereClause, final AsyncCallback<T> callback )
  {
    removeListeners( RTDataEvents.updated, whereClause, callback );
  }

  @Override
  public void removeUpdateListener( final AsyncCallback<T> callback )
  {
    removeListeners( RTDataEvents.updated, callback );
  }

  @Override
  public void removeUpdateListeners( final String whereClause )
  {
    removeListeners( RTDataEvents.updated, whereClause );
  }
  
  //--------remove-------

  @Override
  public void addDeleteListener( AsyncCallback<T> callback )
  {
    DataSubscription subscription = new DataSubscription( RTDataEvents.deleted, tableName, createCallback( callback ) );
    addEventListener( subscription );
  }

  @Override
  public void addDeleteListener( String whereClause, AsyncCallback<T> callback )
  {
    DataSubscription subscription = new DataSubscription( RTDataEvents.deleted, tableName, createCallback( callback ) )
            .withWhere( whereClause );

    addEventListener( subscription );
  }

  @Override
  public void removeDeleteListeners()
  {
    removeListeners( RTDataEvents.deleted );
  }

  @Override
  public void removeDeleteListener( final String whereClause, final AsyncCallback<T> callback )
  {
    removeListeners( RTDataEvents.deleted, whereClause, callback );
  }

  @Override
  public void removeDeleteListener( final AsyncCallback<T> callback )
  {
    removeListeners( RTDataEvents.deleted, callback );
  }

  @Override
  public void removeDeleteListeners( final String whereClause )
  {
    removeListeners( RTDataEvents.deleted, whereClause );
  }

  //--------bulk-update-------

  @Override
  public void addBulkUpdateListener( AsyncCallback<BulkEvent> callback )
  {
    DataSubscription subscription = new DataSubscription( RTDataEvents.bulk_updated, tableName, createCallback( callback, BulkEvent.class ) );
    addEventListener( subscription );
  }

  @Override
  public void addBulkUpdateListener( String whereClause, AsyncCallback<BulkEvent> callback )
  {
    DataSubscription subscription = new DataSubscription( RTDataEvents.bulk_updated, tableName, createCallback( callback, BulkEvent.class ) )
            .withWhere( whereClause );

    addEventListener( subscription );
  }

  @Override
  public void removeBulkUpdateListeners()
  {
    removeListeners( RTDataEvents.bulk_updated );
  }

  @Override
  public void removeBulkUpdateListener( final String whereClause, final AsyncCallback<BulkEvent> callback )
  {
    removeListeners( RTDataEvents.bulk_updated, whereClause, callback );
  }

  @Override
  public void removeBulkUpdateListener( final AsyncCallback<BulkEvent> callback )
  {
    removeListeners( RTDataEvents.bulk_updated, callback );
  }

  @Override
  public void removeBulkUpdateListeners( final String whereClause )
  {
    removeListeners( RTDataEvents.bulk_updated, whereClause );
  }

  //--------bulk-remove-------

  @Override
  public void addBulkDeleteListener( AsyncCallback<BulkEvent> callback )
  {
    DataSubscription subscription = new DataSubscription( RTDataEvents.bulk_deleted, tableName, createCallback( callback, BulkEvent.class ) );
    addEventListener( subscription );
  }

  @Override
  public void addBulkDeleteListener( String whereClause, AsyncCallback<BulkEvent> callback )
  {
    DataSubscription subscription = new DataSubscription( RTDataEvents.bulk_deleted, tableName, createCallback( callback, BulkEvent.class ) )
            .withWhere( whereClause );

    addEventListener( subscription );
  }

  @Override
  public void removeBulkDeleteListeners()
  {
    removeListeners( RTDataEvents.bulk_deleted );
  }

  @Override
  public void removeBulkDeleteListener( final String whereClause, final AsyncCallback<BulkEvent> callback )
  {
    removeListeners( RTDataEvents.bulk_deleted, whereClause, callback );
  }

  @Override
  public void removeBulkDeleteListener( final AsyncCallback<BulkEvent> callback )
  {
    removeListeners( RTDataEvents.bulk_deleted, callback );
  }

  @Override
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
        return isEventSubscription( subscription, event );
      }
    } );
  }

  private void removeListeners( final RTDataEvents event, final AsyncCallback callback )
  {
    checkCallback( callback );
    removeEventListener( new Predicate()
    {
      @Override
      public boolean test( RTSubscription subscription )
      {
        return isEventSubscription( subscription, event )
                && subscription.getCallback().usersCallback().equals( callback );
      }
    } );
  }

  private void removeListeners( final RTDataEvents event, final String whereClause, final AsyncCallback callback )
  {
    checkCallback( callback );
    checkWhereClause( whereClause );
    removeEventListener( new Predicate()
    {
      @Override
      public boolean test( RTSubscription subscription )
      {
        return isEventSubscription( subscription, event )
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
        return isEventSubscription( subscription, event )
                && whereClause.equals(((DataSubscription)subscription).getWhereClause());
      }
    } );
  }

  private boolean isEventSubscription( RTSubscription subscription, RTDataEvents event )
  {
    if( !(subscription instanceof DataSubscription))
      return false;

    DataSubscription dataSubscription = (DataSubscription) subscription;

    return dataSubscription.getSubscriptionName() == SubscriptionNames.OBJECTS_CHANGES
            && dataSubscription.getEvent() == event;
  }

  private RTCallback createCallback( final AsyncCallback<T> callback )
  {
    return createCallback( callback, clazz );
  }

  private <Type> RTCallback createCallback( final AsyncCallback<Type> callback, final Class<Type> classType )
  {
    checkCallback( callback );

    return new RTCallback()
    {
      @Override
      public AsyncCallback<Type> usersCallback()
      {
        return callback;
      }

      @Override
      public void handleResponse( IAdaptingType response )
      {
        try
        {
          final Type adaptedResponse = (Type) response.adapt( classType );
          callback.handleResponse( adaptedResponse );
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

  private void checkCallback( AsyncCallback callback )
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
