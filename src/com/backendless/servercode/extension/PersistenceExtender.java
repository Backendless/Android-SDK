/*
 * ********************************************************************************************************************
 *  <p/>
 *  BACKENDLESS.COM CONFIDENTIAL
 *  <p/>
 *  ********************************************************************************************************************
 *  <p/>
 *  Copyright 2012 BACKENDLESS.COM. All Rights Reserved.
 *  <p/>
 *  NOTICE: All information contained herein is, and remains the property of Backendless.com and its suppliers,
 *  if any. The intellectual and technical concepts contained herein are proprietary to Backendless.com and its
 *  suppliers and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret
 *  or copyright law. Dissemination of this information or reproduction of this material is strictly forbidden
 *  unless prior written permission is obtained from Backendless.com.
 *  <p/>
 *  ********************************************************************************************************************
 */

package com.backendless.servercode.extension;

import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.property.ObjectProperty;
import com.backendless.servercode.ExecutionResult;
import com.backendless.servercode.RunnerContext;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ivanlappo
 * Date: 5/20/13
 * Time: 12:40 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class PersistenceExtender<T>
{
  public void beforeFindById( RunnerContext context, Object objectId, String[] relations ) throws Exception
  {
  }

  public void afterFindById( RunnerContext context, Object objectId, String[] relations,
                             ExecutionResult<T> entity ) throws Exception
  {
  }

  public void beforeCreate( RunnerContext context, T t ) throws Exception
  {
  }

  public void afterCreate( RunnerContext context, T t, ExecutionResult<T> entity ) throws Exception
  {
  }

  public void beforeCreateBulk( RunnerContext context, List<T> entities )
  {
  }

  public void afterCreateBulk( RunnerContext context, List<T> entities, ExecutionResult<List<String>> result )
  {
  }

  public void beforeUpdate( RunnerContext context, T t ) throws Exception
  {
  }

  public void afterUpdate( RunnerContext context, T t, ExecutionResult<T> entity ) throws Exception
  {
  }

  public void beforeUpdateBulk( RunnerContext context, String tableName, String whereClause,
                                HashMap hashmap ) throws Exception
  {
  }

  public void afterUpdateBulk( RunnerContext context, String tableName, String whereClause, HashMap hashmap,
                               ExecutionResult<Integer> result ) throws Exception
  {
  }

  public void beforeLoadRelations( RunnerContext context, Object objectId, String entityName,
                                   String relationName, int pageSize, int offset ) throws Exception
  {
  }

  public void afterLoadRelations( RunnerContext context, Object objectId, String entityName,
                                  String relationName, int pageSize, int offset,
                                  ExecutionResult<List> collection ) throws Exception
  {
  }

  public void beforeRemove( RunnerContext context, Object objectId ) throws Exception
  {
  }

  public void afterRemove( RunnerContext context, Object objectId, ExecutionResult<Long> removedVal ) throws Exception
  {
  }

  public void beforeRemoveBulk( RunnerContext context, String tableName, String whereClause ) throws Exception
  {
  }

  public void afterRemoveBulk( RunnerContext context, String tableName, String whereClause,
                               ExecutionResult<Integer> result ) throws Exception
  {
  }

  public void beforeDescribe( RunnerContext context, String entityName ) throws Exception
  {
  }

  public void afterDescribe( RunnerContext context, String entityName,
                             ExecutionResult<List<ObjectProperty>> propertiesFound ) throws Exception
  {
  }

  public void beforeFind( RunnerContext context, BackendlessDataQuery query ) throws Exception
  {
  }

  public void afterFind( RunnerContext context, BackendlessDataQuery query,
                         ExecutionResult<List<T>> List ) throws Exception
  {
  }

  public void beforeFirst( RunnerContext context, String[] relations, Integer relationsDepth, String[] properties ) throws Exception
  {
  }

  public void afterFirst( RunnerContext context, String[] relations, Integer relationsDepth, String[] properties, ExecutionResult<T> entity ) throws Exception
  {
  }

  public void beforeLast( RunnerContext context, String[] relations, Integer relationsDepth, String[] properties ) throws Exception
  {
  }

  public void afterLast( RunnerContext context, String[] relations, Integer relationsDepth, String[] properties, ExecutionResult<T> entity ) throws Exception
  {
  }

  public void beforeCount( RunnerContext context, BackendlessDataQuery query ) throws Exception
  {
  }

  public void afterCount( RunnerContext context, BackendlessDataQuery query,
                         ExecutionResult<Integer> result ) throws Exception
  {
  }

  public void beforeAddRelation( RunnerContext context, String columnName, String parentObjectId, Object childrenArrayORWhereClause )
  {

  }

  public void afterAddRelation( RunnerContext context, String columnName, String parentObjectId, Object childrenArrayORWhereClause, ExecutionResult<Integer> result )
  {

  }

  public void beforeSetRelation( RunnerContext context, String columnName, String parentObjectId, Object childrenArrayORWhereClause )
  {

  }

  public void afterSetRelation( RunnerContext context, String columnName, String parentObjectId, Object childrenArrayORWhereClause, ExecutionResult<Integer> result )
  {

  }

  public void beforeDeleteRelation( RunnerContext context, String columnName, String parentObjectId, Object childrenArrayORWhereClause )
  {

  }

  public void afterDeleteRelation( RunnerContext context, String columnName, String parentObjectId, Object childrenArrayORWhereClause, ExecutionResult<Integer> result )
  {

  }
}

