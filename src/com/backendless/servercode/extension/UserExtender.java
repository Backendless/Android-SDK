package com.backendless.servercode.extension;

import com.backendless.BackendlessCollection;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.property.UserProperty;
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
public abstract class UserExtender
{
  public UserExtender()
  {
  }

  public void beforeFind( RunnerContext context, BackendlessDataQuery query ) throws Exception
  {

  }

  public void afterFind( RunnerContext context, BackendlessDataQuery query,
                         ExecutionResult<BackendlessCollection> result ) throws Exception
  {
  }

  public void beforeFindById( RunnerContext context, String objectId, String[] relations ) throws Exception
  {

  }

  public void afterFindById( RunnerContext context, String objectId, String[] relations, ExecutionResult<HashMap> result ) throws Exception
  {
  }

  public void beforeLogin( RunnerContext context, String login, String password ) throws Exception
  {
  }

  public void afterLogin( RunnerContext context, String login, String password, ExecutionResult<HashMap> result ) throws Exception
  {
  }

  public void beforeRegister( RunnerContext context, HashMap userValue ) throws Exception
  {
  }

  public void afterRegister( RunnerContext context, HashMap userValue, ExecutionResult<HashMap> result ) throws Exception
  {
  }

  public void beforeUpdate( RunnerContext context, HashMap userValue ) throws Exception
  {
  }

  public void afterUpdate( RunnerContext context, HashMap userValue, ExecutionResult<HashMap> result ) throws Exception
  {
  }

  public void beforeUpdateBulk( RunnerContext context, String whereClause, HashMap userValues ) throws Exception
  {
  }

  public void afterUpdateBulk( RunnerContext context, String whereClause, HashMap userValues,
                               ExecutionResult<Integer> result ) throws Exception
  {
  }

  public void beforeRemove( RunnerContext context, String id ) throws Exception
  {
  }

  public void afterRemove( RunnerContext context, String id, ExecutionResult<Long> result ) throws Exception
  {
  }

  public void beforeRemoveBulk( RunnerContext context, String whereClause ) throws Exception
  {
  }

  public void afterRemoveBulk( RunnerContext context, String whereClause,
                               ExecutionResult<Integer> result ) throws Exception
  {
  }

  public void beforeDescribe( RunnerContext context ) throws Exception
  {
  }

  public void afterDescribe( RunnerContext context, ExecutionResult<List<UserProperty>> result ) throws Exception
  {
  }

  public void beforeRestorePassword( RunnerContext context, String email ) throws Exception
  {
  }

  public void afterRestorePassword( RunnerContext context, String email, ExecutionResult result ) throws Exception
  {
  }

  public void beforeLogout( RunnerContext context ) throws Exception
  {
  }

  public void afterLogout( RunnerContext context, ExecutionResult result ) throws Exception
  {
  }
}

