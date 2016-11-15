package com.backendless;

import com.backendless.async.callback.AsyncCallback;

import java.util.Collection;

public interface RelationsStore<N>
{
  void addRelation( String columnName, Collection<N> childs );

  void addRelation( String columnName, Collection<N> childs, AsyncCallback<Void> callback );

  int addRelation( String columnName, String whereClause );

  void addRelation( String columnName, String whereClause, AsyncCallback<Integer> callback );

  void setRelation( String columnName, Collection<N> childs );

  void setRelation( String columnName, Collection<N> childs, AsyncCallback<Void> callback );

  int setRelation( String columnName, String whereClause );

  void setRelation( String columnName, String whereClause, AsyncCallback<Integer> callback );

  void deleteRelation( String columnName, Collection<N> childs );

  void deleteRelation( String columnName, Collection<N> childs, AsyncCallback<Void> callback );

  int deleteRelation( String columnName, String whereClause );

  void deleteRelation( String columnName, String whereClause, AsyncCallback<Integer> callback );
}
