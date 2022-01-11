package com.backendless.rt.data;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.rt.RTListener;

import java.util.List;

public interface EventHandler<T> extends RTListener
{
  void addCreateListener( AsyncCallback<T> callback );

  void addCreateListener( String whereClause, AsyncCallback<T> callback );

  void removeCreateListeners();

  void removeCreateListener( String whereClause, AsyncCallback<T> callback );

  void removeCreateListener( AsyncCallback<T> callback );

  void removeCreateListeners( String whereClause );

  void addUpdateListener( AsyncCallback<T> callback );

  void addUpdateListener( String whereClause, AsyncCallback<T> callback );

  void removeUpdateListeners();

  void removeUpdateListener( String whereClause, AsyncCallback<T> callback );

  void removeUpdateListener( AsyncCallback<T> callback );

  void removeUpdateListeners( String whereClause );

  void addUpsertListener( AsyncCallback<T> callback );

  void addUpsertListener( String whereClause, AsyncCallback<T> callback );

  void removeUpsertListeners();

  void removeUpsertListener( AsyncCallback<T> callback );

  void removeUpsertListeners( String whereClause );

  void removeUpsertListener( String whereClause, AsyncCallback<T> callback );

  void addDeleteListener( AsyncCallback<T> callback );

  void addDeleteListener( String whereClause, AsyncCallback<T> callback );

  void removeDeleteListeners();

  void removeDeleteListener( String whereClause, AsyncCallback<T> callback );

  void removeDeleteListener( AsyncCallback<T> callback );

  void removeDeleteListeners( String whereClause );

  void addBulkCreateListener( AsyncCallback<List> callback );

  void removeBulkCreateListener( AsyncCallback<List<String>> callback );

  void removeBulkCreateListeners();

  void addBulkUpdateListener( AsyncCallback<BulkEvent> callback );

  void addBulkUpdateListener( String whereClause, AsyncCallback<BulkEvent> callback );

  void removeBulkUpdateListeners();

  void removeBulkUpdateListener( String whereClause, AsyncCallback<BulkEvent> callback );

  void removeBulkUpdateListener( AsyncCallback<BulkEvent> callback );

  void removeBulkUpdateListeners( String whereClause );

  void addBulkUpsertListener( AsyncCallback<List> callback );

  void removeBulkUpsertListeners();

  void removeBulkUpsertListener( AsyncCallback<List<String>> callback );

  void addBulkDeleteListener( AsyncCallback<BulkEvent> callback );

  void addBulkDeleteListener( String whereClause, AsyncCallback<BulkEvent> callback );

  void removeBulkDeleteListeners();

  void removeBulkDeleteListener( String whereClause, AsyncCallback<BulkEvent> callback );

  void removeBulkDeleteListener( AsyncCallback<BulkEvent> callback );

  void removeBulkDeleteListeners( String whereClause );

  void addSetRelationListener( String relationColumnName, AsyncCallback<RelationStatus> callback );

  void addSetRelationListener( String relationColumnName, List<String> parentObjects, AsyncCallback<RelationStatus> callback );

  void removeSetRelationListeners();

  void addAddRelationListener( String relationColumnName, AsyncCallback<RelationStatus> callback );

  void addAddRelationListener( String relationColumnName, List<String> parentObjects, AsyncCallback<RelationStatus> callback );

  void removeAddRelationListeners();

  void addDeleteRelationListener( String relationColumnName, AsyncCallback<RelationStatus> callback );

  void addDeleteRelationListener( String relationColumnName, List<String> parentObjects, AsyncCallback<RelationStatus> callback );

  void removeDeleteRelationListeners();
}
