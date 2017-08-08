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

import com.backendless.BackendlessUser;
import com.backendless.commons.util.SocialType;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.property.UserProperty;
import com.backendless.servercode.ExecutionResult;
import com.backendless.servercode.RunnerContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class UserExtender
{
  public UserExtender()
  {
  }

  public void beforeFind( RunnerContext context, BackendlessDataQuery query ) throws Exception
  {

  }

  public void afterFind( RunnerContext context, BackendlessDataQuery query, ExecutionResult<List<BackendlessUser>> result ) throws Exception
  {
  }

  public void beforeFindById( RunnerContext context, Object objectId, String[] relations ) throws Exception
  {

  }

  public void afterFindById( RunnerContext context, Object objectId, String[] relations,
                             ExecutionResult<BackendlessUser> result ) throws Exception
  {
  }

  public void beforeLogin( RunnerContext context, String login, String password ) throws Exception
  {
  }

  public void afterLogin( RunnerContext context, String login, String password, ExecutionResult<BackendlessUser> result ) throws Exception
  {
  }

  public void beforeSocialLogin( RunnerContext context, Map<String, String> userValues, SocialType socialType ) throws Exception
  {
  }

  public void afterSocialLogin( RunnerContext context, Map<String, String> userValues, SocialType socialType,
                                ExecutionResult<BackendlessUser> result ) throws Exception
  {
  }

  public void beforeRegister( RunnerContext context, HashMap userValues ) throws Exception
  {
  }

  public void afterRegister( RunnerContext context, HashMap userValues, ExecutionResult<BackendlessUser> result ) throws Exception
  {
  }

  public void beforeSocialRegister( RunnerContext context, Map<String, String> userValues, SocialType socialType ) throws Exception
  {
  }

  public void afterSocialRegister( RunnerContext context, Map<String, String> userValues, SocialType socialType,
                                   ExecutionResult<BackendlessUser> result ) throws Exception
  {
  }

  public void beforeUpdate( RunnerContext context, HashMap userValues ) throws Exception
  {
  }

  public void afterUpdate( RunnerContext context, HashMap userValues, ExecutionResult<BackendlessUser> result ) throws Exception
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

  public void afterRemoveBulk( RunnerContext context, String whereClause, ExecutionResult<Integer> result ) throws Exception
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

  public void afterEmailConfirmed( RunnerContext context, String confirmationKey, ExecutionResult<String> result ) throws Exception
  {
  }

  public void beforeEmailConfirmed( RunnerContext context, String confirmationKey ) throws Exception
  {
  }
}
