package com.backendless;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.BackendlessSerializer;

import java.util.Collection;

public class ObjectRelationsStore<T, N>
{
  private Class<T> parentClass;
  private Class<N> childClass;

  public ObjectRelationsStore( Class<T> parentClass, Class<N> childClass )
  {
    this.parentClass = parentClass;
    this.childClass = childClass;
  }

  public void addRelation( String parentObjectId, String columnName, Collection<String> childObjectIds )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parentClass );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, childObjectIds };
    Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "addRelation", args );
  }

  public void addRelation( String parentObjectId, String columnName, Collection<String> childObjectIds,
                           AsyncCallback<Void> callback )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parentClass );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, childObjectIds };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "addRelation", args, callback );
  }

  public int addRelation( String parentObjectId, String columnName, String whereClause )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parentClass );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "addRelation", args );
  }

  public void addRelation( String parentObjectId, String columnName, String whereClause,
                           AsyncCallback<Integer> callback )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parentClass );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "addRelation", args, callback );
  }

  public void setRelation( String parentObjectId, String columnName, Collection<String> childObjectIds )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parentClass );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, childObjectIds };
    Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "setRelation", args );
  }

  public void setRelation( String parentObjectId, String columnName, Collection<String> childObjectIds,
                           AsyncCallback<Void> callback )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parentClass );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, childObjectIds };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "setRelation", args, callback );
  }

  public int setRelation( String parentObjectId, String columnName, String whereClause )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parentClass );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "setRelation", args );
  }

  public void setRelation( String parentObjectId, String columnName, String whereClause,
                           AsyncCallback<Integer> callback )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parentClass );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "setRelation", args, callback );
  }

  public void deleteRelation( String parentObjectId, String columnName, Collection<String> childObjectIds )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parentClass );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, childObjectIds };
    Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "deleteRelation", args );
  }

  public void deleteRelation( String parentObjectId, String columnName, Collection<String> childObjectIds,
                              AsyncCallback<Void> callback )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parentClass );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, childObjectIds };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "deleteRelation", args, callback );
  }

  public int deleteRelation( String parentObjectId, String columnName, String whereClause )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parentClass );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "deleteRelation", args );
  }

  public void deleteRelation( String parentObjectId, String columnName, String whereClause,
                              AsyncCallback<Integer> callback )
  {
    String parentTableName = BackendlessSerializer.getSimpleName( parentClass );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "deleteRelation", args, callback );
  }
}
