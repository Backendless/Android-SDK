package com.backendless;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.BackendlessSerializer;

import java.util.ArrayList;
import java.util.Collection;

public final class Relations
{
  public <T, N> ObjectRelationsStore<T, N> of( Class<T> parent, Class<N> child )
  {
    return new ObjectRelationsStore<>( parent, child );
  }

  public MapRelationsStore of( String parentTable, String childTable )
  {
    return new MapRelationsStore( parentTable, childTable );
  }

  public <T, N> void addRelation( T parent, String columnName, Collection<N> childs )
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

  public <T, N> void addRelation( T parent, String columnName, Collection<N> childs, AsyncCallback<Void> callback )
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

  public <T> int addRelation( T parent, String columnName, String whereClause )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parent.getClass() );

    String parentObjectId = FootprintsManager.getInstance().getObjectId( parent );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "addRelation", args );
  }

  public <T> void addRelation( T parent, String columnName, String whereClause, AsyncCallback<Integer> callback )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parent.getClass() );

    String parentObjectId = FootprintsManager.getInstance().getObjectId( parent );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "addRelation", args, callback );
  }

  public <T, N> void setRelation( T parent, String columnName, Collection<N> childs )
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

  public <T, N> void setRelation( T parent, String columnName, Collection<N> childs, AsyncCallback<Void> callback )
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

  public <T> int setRelation( T parent, String columnName, String whereClause )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parent.getClass() );

    String parentObjectId = FootprintsManager.getInstance().getObjectId( parent );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "setRelation", args );
  }

  public <T> void setRelation( T parent, String columnName, String whereClause, AsyncCallback<Integer> callback )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parent.getClass() );

    String parentObjectId = FootprintsManager.getInstance().getObjectId( parent );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "addRelation", args, callback );
  }

  public <T, N> void deleteRelation( T parent, String columnName, Collection<N> childs )
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

  public <T, N> void deleteRelation( T parent, String columnName, Collection<N> childs, AsyncCallback<Void> callback )
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

  public <T> int deleteRelation( T parent, String columnName, String whereClause )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parent.getClass() );

    String parentObjectId = FootprintsManager.getInstance().getObjectId( parent );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "deleteRelation", args );
  }

  public <T> void deleteRelation( T parent, String columnName, String whereClause, AsyncCallback<Integer> callback )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parent.getClass() );

    String parentObjectId = FootprintsManager.getInstance().getObjectId( parent );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "deleteRelation", args, callback );
  }
}
