package com.backendless.servercode.extension;

import com.backendless.BackendlessCollection;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.property.ObjectProperty;
import com.backendless.servercode.ExecutionResult;
import com.backendless.servercode.RunnerContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ivanlappo
 * Date: 5/20/13
 * Time: 12:40 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class PersistenceExtender<T>
{
  private Class<T> userClass;

  public PersistenceExtender()
  {
    userClass = (Class<T>) HashMap.class;
  }

  public PersistenceExtender( Class<T> clazz )
  {
    this.userClass = clazz;
  }

  public void beforeFindById( RunnerContext context, String objectId, String[] relations ) throws Exception
  {
  }

  public T afterFindById( RunnerContext context, String objectId, String[] relations,
                          ExecutionResult<T> entity ) throws Exception
  {
    return entity.getResult();
  }

  public void beforeCreate( RunnerContext context, Map entityMap ) throws Exception
  {
  }

  public T afterCreate( RunnerContext context, Map entityMap, ExecutionResult<T> entity ) throws Exception
  {
    return entity.getResult();
  }

  public void beforeUpdate( RunnerContext context, Map entityMap ) throws Exception
  {
  }

  public T afterUpdate( RunnerContext context, Map entityMap, ExecutionResult<T> entity ) throws Exception
  {
    return entity.getResult();
  }

  public void beforeLoadRelations( RunnerContext context, String objectId, String entityName,
                                   String[] relations ) throws Exception
  {
  }

  public Map afterLoadRelations( RunnerContext context, String objectId, String entityName, String[] relations,
                                 ExecutionResult<Map> entity ) throws Exception
  {
    return entity.getResult();
  }

  public void beforeRemove( RunnerContext context, String objectId ) throws Exception
  {
  }

  public Long afterRemove( RunnerContext context, String objectId, ExecutionResult<Long> removedVal ) throws Exception
  {
    return removedVal.getResult();
  }

  public void beforeDescribe( RunnerContext context, String entityName ) throws Exception
  {
  }

  public ObjectProperty[] afterDescribe( RunnerContext context, String entityName,
                                         ExecutionResult<ObjectProperty[]> propertiesFound ) throws Exception
  {
    return propertiesFound.getResult();
  }

  public void beforeFind( RunnerContext context, BackendlessDataQuery query ) throws Exception
  {
  }

  public BackendlessCollection afterFind( RunnerContext context, BackendlessDataQuery query,
                                          ExecutionResult<BackendlessCollection> collection ) throws Exception
  {
    return collection.getResult();
  }

  public void beforeFirst( RunnerContext context ) throws Exception
  {
  }

  public T afterFirst( RunnerContext context, ExecutionResult<T> entity ) throws Exception
  {
    return entity.getResult();
  }

  public void beforeLast( RunnerContext context ) throws Exception
  {
  }

  public T afterLast( RunnerContext context, ExecutionResult<T> entity ) throws Exception
  {
    return entity.getResult();
  }

  public final Class<?> getUserClass()
  {
    return userClass;
  }
}

