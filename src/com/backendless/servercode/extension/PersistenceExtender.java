package com.backendless.servercode.extension;

import com.backendless.BackendlessCollection;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.property.ObjectProperty;
import com.backendless.servercode.ExecutionResult;
import com.backendless.servercode.RunnerContext;

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
  public void beforeFindById( RunnerContext context, String objectId, String[] relations ) throws Exception
  {
  }

  public void afterFindById( RunnerContext context, String objectId, String[] relations, ExecutionResult<T> entity ) throws Exception
  {
  }

  public void beforeCreate( RunnerContext context, T t ) throws Exception
  {
  }

  public void afterCreate( RunnerContext context, T t, ExecutionResult<T> entity ) throws Exception
  {
  }

  public void beforeUpdate( RunnerContext context, T t ) throws Exception
  {
  }

  public void afterUpdate( RunnerContext context, T t, ExecutionResult<T> entity ) throws Exception
  {
  }

  public void beforeLoadRelations( RunnerContext context, String objectId, String entityName,
                                   String[] relations ) throws Exception
  {
  }

  public void afterLoadRelations( RunnerContext context, String objectId, String entityName, String[] relations,
                                  ExecutionResult<Map> entity ) throws Exception
  {
  }

  public void beforeRemove( RunnerContext context, String objectId ) throws Exception
  {
  }

  public void afterRemove( RunnerContext context, String objectId, ExecutionResult<Long> removedVal ) throws Exception
  {
  }

  public void beforeDescribe( RunnerContext context, String entityName ) throws Exception
  {
  }

  public void afterDescribe( RunnerContext context, String entityName, ExecutionResult<ObjectProperty[]> propertiesFound ) throws Exception
  {
  }

  public void beforeFind( RunnerContext context, BackendlessDataQuery query ) throws Exception
  {
  }

  public void afterFind( RunnerContext context, BackendlessDataQuery query,
                         ExecutionResult<BackendlessCollection<T>> collection ) throws Exception
  {
  }

  public void beforeFirst( RunnerContext context ) throws Exception
  {
  }

  public void afterFirst( RunnerContext context, ExecutionResult<T> entity ) throws Exception
  {
  }

  public void beforeLast( RunnerContext context ) throws Exception
  {
  }

  public void afterLast( RunnerContext context, ExecutionResult<T> entity ) throws Exception
  {
  }
}

