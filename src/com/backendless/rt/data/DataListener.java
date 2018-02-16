package com.backendless.rt.data;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.rt.RTListener;

public interface DataListener<T> extends RTListener
{
  void addCreateListener( AsyncCallback<T> callback );

  void addCreateListener( String whereClause, AsyncCallback<T> callback );

  void removeCreateListeners();

  void removeCreateListeners( String whereClause, AsyncCallback<T> callback );

  void removeCreateListeners( AsyncCallback<T> callback );

  void removeCreateListeners( String whereClause );

  void addUpdateListener( AsyncCallback<T> callback );

  void addUpdateListener( String whereClause, AsyncCallback<T> callback );

  void removeUpdateListeners();

  void removeUpdateListeners( String whereClause, AsyncCallback<T> callback );

  void removeUpdateListeners( AsyncCallback<T> callback );

  void removeUpdateListeners( String whereClause );

  void addDeleteListener( AsyncCallback<T> callback );

  void addDeleteListener( String whereClause, AsyncCallback<T> callback );

  void removeDeleteListeners();

  void removeDeleteListeners( String whereClause, AsyncCallback<T> callback );

  void removeDeleteListeners( AsyncCallback<T> callback );

  void removeDeleteListeners( String whereClause );

  void addBulkUpdateListener( AsyncCallback<BulkEvent> callback );

  void addBulkUpdateListener( String whereClause, AsyncCallback<BulkEvent> callback );

  void removeBulkUpdateListeners();

  void removeBulkUpdateListeners( String whereClause, AsyncCallback<T> callback );

  void removeBulkUpdateListeners( AsyncCallback<T> callback );

  void removeBulkUpdateListeners( String whereClause );

  void addBulkDeleteListener( AsyncCallback<BulkEvent> callback );

  void addBulkDeleteListener( String whereClause, AsyncCallback<BulkEvent> callback );

  void removeBulkDeleteListeners();

  void removeBulkDeleteListeners( String whereClause, AsyncCallback<T> callback );

  void removeBulkDeleteListeners( AsyncCallback<T> callback );

  void removeBulkDeleteListeners( String whereClause );
}
