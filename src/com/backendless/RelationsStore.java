package com.backendless;

import com.backendless.async.callback.AsyncCallback;

import java.util.Collection;

public interface RelationsStore<T, N>
{
  void addRelation( T parent, String columnName, Collection<N> childs );

  void addRelation( T parent, String columnName, Collection<N> childs, AsyncCallback<Void> callback );

  int addRelation( T parent, String columnName, String whereClause );

  void addRelation( T parent, String columnName, String whereClause, AsyncCallback<Integer> callback );

  void setRelation( T parent, String columnName, Collection<N> childs );

  void setRelation( T parent, String columnName, Collection<N> childs, AsyncCallback<Void> callback );

  int setRelation( T parent, String columnName, String whereClause );

  void setRelation( T parent, String columnName, String whereClause, AsyncCallback<Integer> callback );

  void deleteRelation( T parent, String columnName, Collection<N> childs );

  void deleteRelation( T parent, String columnName, Collection<N> childs, AsyncCallback<Void> callback );

  int deleteRelation( T parent, String columnName, String whereClause );

  void deleteRelation( T parent, String columnName, String whereClause, AsyncCallback<Integer> callback );
}
