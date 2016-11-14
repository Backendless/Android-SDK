package com.backendless;

import com.backendless.async.callback.AsyncCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public final class MapRelationsStore
{
  private String parentTableName;
  private String childTableName;

  public MapRelationsStore( String parentTableName, String childTableName )
  {
    this.parentTableName = parentTableName;
    this.childTableName = childTableName;
  }

  public void addRelation( Map<String, Object> parent, String columnName, Collection<Map<String, Object>> childs )
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

  public void addRelation( Map<String, Object> parent, String columnName, Collection<Map<String, Object>> childs,
                           AsyncCallback<Void> callback )
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

  public int addRelation( Map<String, Object> parent, String columnName, String whereClause )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "addRelation", args );
  }

  public void addRelation( Map<String, Object> parent, String columnName, String whereClause,
                           AsyncCallback<Integer> callback )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "addRelation", args, callback );
  }

  public void setRelation( Map<String, Object> parent, String columnName, Collection<Map<String, Object>> childs )
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

  public void setRelation( Map<String, Object> parent, String columnName, Collection<Map<String, Object>> childs,
                           AsyncCallback<Void> callback )
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

  public int setRelation( Map<String, Object> parent, String columnName, String whereClause )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "setRelation", args );
  }

  public void setRelation( Map<String, Object> parent, String columnName, String whereClause,
                           AsyncCallback<Integer> callback )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "setRelation", args, callback );
  }

  public void deleteRelation( Map<String, Object> parent, String columnName, Collection<Map<String, Object>> childs )
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

  public void deleteRelation( Map<String, Object> parent, String columnName, Collection<Map<String, Object>> childs,
                              AsyncCallback<Void> callback )
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

  public int deleteRelation( Map<String, Object> parent, String columnName, String whereClause )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    return Invoker.invokeSync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "deleteRelation", args );
  }

  public void deleteRelation( Map<String, Object> parent, String columnName, String whereClause,
                              AsyncCallback<Integer> callback )
  {
    String parentObjectId = (String) parent.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

    Object[] args = new Object[] { parentTableName, columnName, parentObjectId, whereClause };
    Invoker.invokeAsync( Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS, "deleteRelation", args, callback );
  }
}
