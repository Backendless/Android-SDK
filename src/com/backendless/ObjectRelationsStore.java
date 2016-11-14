package com.backendless;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.BackendlessSerializer;

import java.util.ArrayList;
import java.util.Collection;

public class ObjectRelationsStore<T, N> implements RelationsStore<T, N>
{
  public void addRelation( T parent, String columnName, Collection<N> childs )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parent.getClass() );

    String parentObjectId = FootprintsManager.getInstance().getObjectId( parent );
    Collection<String> childObjectIds = new ArrayList<>();
    for( N child : childs )
    {
      String childObjectId = FootprintsManager.getInstance().getObjectId( child );
      childObjectIds.add( childObjectId );
    }

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, childObjectIds };
    Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "addRelation", args );
  }

  public void addRelation( T parent, String columnName, Collection<N> childs, AsyncCallback<Void> callback )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parent.getClass() );

    String parentObjectId = FootprintsManager.getInstance().getObjectId( parent );
    Collection<String> childObjectIds = new ArrayList<>();
    for( N child : childs )
    {
      String childObjectId = FootprintsManager.getInstance().getObjectId( child );
      childObjectIds.add( childObjectId );
    }

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, childObjectIds };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "addRelation", args, callback );
  }

  public int addRelation( T parent, String columnName, String whereClause )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parent.getClass() );

    String parentObjectId = FootprintsManager.getInstance().getObjectId( parent );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "addRelation", args );
  }

  public void addRelation( T parent, String columnName, String whereClause, AsyncCallback<Integer> callback )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parent.getClass() );

    String parentObjectId = FootprintsManager.getInstance().getObjectId( parent );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "addRelation", args, callback );
  }

  public void setRelation( T parent, String columnName, Collection<N> childs )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parent.getClass() );

    String parentObjectId = FootprintsManager.getInstance().getObjectId( parent );
    Collection<String> childObjectIds = new ArrayList<>();
    for( N child : childs )
    {
      String childObjectId = FootprintsManager.getInstance().getObjectId( child );
      childObjectIds.add( childObjectId );
    }

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, childObjectIds };
    Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "setRelation", args );
  }

  public void setRelation( T parent, String columnName, Collection<N> childs, AsyncCallback<Void> callback )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parent.getClass() );

    String parentObjectId = FootprintsManager.getInstance().getObjectId( parent );
    Collection<String> childObjectIds = new ArrayList<>();
    for( N child : childs )
    {
      String childObjectId = FootprintsManager.getInstance().getObjectId( child );
      childObjectIds.add( childObjectId );
    }

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, childObjectIds };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "setRelation", args, callback );
  }

  public int setRelation( T parent, String columnName, String whereClause )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parent.getClass() );

    String parentObjectId = FootprintsManager.getInstance().getObjectId( parent );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "setRelation", args );
  }

  public void setRelation( T parent, String columnName, String whereClause, AsyncCallback<Integer> callback )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parent.getClass() );

    String parentObjectId = FootprintsManager.getInstance().getObjectId( parent );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "addRelation", args, callback );
  }

  public void deleteRelation( T parent, String columnName, Collection<N> childs )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parent.getClass() );

    String parentObjectId = FootprintsManager.getInstance().getObjectId( parent );
    Collection<String> childObjectIds = new ArrayList<>();
    for( N child : childs )
    {
      String childObjectId = FootprintsManager.getInstance().getObjectId( child );
      childObjectIds.add( childObjectId );
    }

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, childObjectIds };
    Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "deleteRelation", args );
  }

  public void deleteRelation( T parent, String columnName, Collection<N> childs, AsyncCallback<Void> callback )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parent.getClass() );

    String parentObjectId = FootprintsManager.getInstance().getObjectId( parent );
    Collection<String> childObjectIds = new ArrayList<>();
    for( N child : childs )
    {
      String childObjectId = FootprintsManager.getInstance().getObjectId( child );
      childObjectIds.add( childObjectId );
    }

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, childObjectIds };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "deleteRelation", args, callback );
  }

  public int deleteRelation( T parent, String columnName, String whereClause )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parent.getClass() );

    String parentObjectId = FootprintsManager.getInstance().getObjectId( parent );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "deleteRelation", args );
  }

  public void deleteRelation( T parent, String columnName, String whereClause, AsyncCallback<Integer> callback )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parent.getClass() );

    String parentObjectId = FootprintsManager.getInstance().getObjectId( parent );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "deleteRelation", args, callback );
  }
}
