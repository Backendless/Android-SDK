package com.backendless;

import com.backendless.async.callback.AsyncCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

final class MapRelationsStore implements RelationsStore<Map<String, Object>>
{
  private final Map<String, Object> parent;
  private final String parentTableName;

  MapRelationsStore( final Map<String, Object> parentMap )
  {
    this.parent = parentMap;
    this.parentTableName = (String) parentMap.get( Persistence.REST_CLASS_FIELD );
  }

  public void addRelation( String columnName, Collection<Map<String, Object>> childs )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Collection<String> childObjectIds = new ArrayList<>();
    for( Map<String, Object> child : childs )
    {
      String childObjectId = (String) child.get( Persistence.DEFAULT_OBJECT_ID_FIELD );
      childObjectIds.add( childObjectId );
    }

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, childObjectIds };
    Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "addRelation", args );
  }

  public void addRelation( String columnName, Collection<Map<String, Object>> childs, AsyncCallback<Void> callback )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Collection<String> childObjectIds = new ArrayList<>();
    for( Map<String, Object> child : childs )
    {
      String childObjectId = (String) child.get( Persistence.DEFAULT_OBJECT_ID_FIELD );
      childObjectIds.add( childObjectId );
    }

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, childObjectIds };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "addRelation", args, callback );
  }

  public int addRelation( String columnName, String whereClause )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "addRelation", args );
  }

  public void addRelation( String columnName, String whereClause, AsyncCallback<Integer> callback )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "addRelation", args, callback );
  }

  public void setRelation( String columnName, Collection<Map<String, Object>> childs )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Collection<String> childObjectIds = new ArrayList<>();
    for( Map<String, Object> child : childs )
    {
      String childObjectId = (String) child.get( Persistence.DEFAULT_OBJECT_ID_FIELD );
      childObjectIds.add( childObjectId );
    }

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, childObjectIds };
    Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "setRelation", args );
  }

  public void setRelation( String columnName, Collection<Map<String, Object>> childs, AsyncCallback<Void> callback )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Collection<String> childObjectIds = new ArrayList<>();
    for( Map<String, Object> child : childs )
    {
      String childObjectId = (String) child.get( Persistence.DEFAULT_OBJECT_ID_FIELD );
      childObjectIds.add( childObjectId );
    }

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, childObjectIds };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "setRelation", args, callback );
  }

  public int setRelation( String columnName, String whereClause )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "setRelation", args );
  }

  public void setRelation( String columnName, String whereClause, AsyncCallback<Integer> callback )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "setRelation", args, callback );
  }

  public void deleteRelation( String columnName, Collection<Map<String, Object>> childs )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Collection<String> childObjectIds = new ArrayList<>();
    for( Map<String, Object> child : childs )
    {
      String childObjectId = (String) child.get( Persistence.DEFAULT_OBJECT_ID_FIELD );
      childObjectIds.add( childObjectId );
    }

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, childObjectIds };
    Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "deleteRelation", args );
  }

  public void deleteRelation( String columnName, Collection<Map<String, Object>> childs, AsyncCallback<Void> callback )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Collection<String> childObjectIds = new ArrayList<>();
    for( Map<String, Object> child : childs )
    {
      String childObjectId = (String) child.get( Persistence.DEFAULT_OBJECT_ID_FIELD );
      childObjectIds.add( childObjectId );
    }

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, childObjectIds };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "deleteRelation", args, callback );
  }

  public int deleteRelation( String columnName, String whereClause )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "deleteRelation", args );
  }

  public void deleteRelation( String columnName, String whereClause, AsyncCallback<Integer> callback )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "deleteRelation", args, callback );
  }
}
