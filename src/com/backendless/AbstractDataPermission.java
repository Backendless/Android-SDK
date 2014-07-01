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

package com.backendless;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.core.responder.AdaptingResponder;
import com.backendless.core.responder.policy.PoJoAdaptingPolicy;
import com.backendless.persistence.PersistenceOperations;
import com.backendless.utils.PermissionTypes;

public abstract class AbstractDataPermission
{
  private static final String PERMISSION_SERVICE = "com.backendless.services.persistence.permissions.ClientPermissionService";

  protected abstract PersistenceOperations getOperation();

  public <T> void grantForUser( String userId, T dataObject )
  {
    grantForUser( userId, dataObject, null );
  }

  public <T> void grantForUser( String userId, T dataObject, AsyncCallback<T> responder )
  {
    String method = "updateUserPermission";
    Object[] args = { Backendless.getApplicationId(), Backendless.getVersion(), dataObject.getClass().getSimpleName(), userId, Persistence.getEntityId( dataObject ), getOperation(), PermissionTypes.GRANT };
    serverCall( responder, method, args, dataObject.getClass() );
  }

  public <T> void denyForUser( String userId, T dataObject )
  {
    denyForUser( userId, dataObject, null );
  }

  public <T> void denyForUser( String userId, T dataObject, AsyncCallback<T> responder )
  {
    String method = "updateUserPermission";
    Object[] args = { Backendless.getApplicationId(), Backendless.getVersion(), dataObject.getClass().getSimpleName(), userId, Persistence.getEntityId( dataObject ), getOperation(), PermissionTypes.DENY };
    serverCall( responder, method, args, null );
  }

  public <T> void grantForRole( String roleName, Object dataObject )
  {
    grantForRole( roleName, dataObject, null );
  }

  public <T> void grantForRole( String roleName, Object dataObject, AsyncCallback<T> responder )
  {
    String method = "updateRolePermission";
    Object[] args = { Backendless.getApplicationId(), Backendless.getVersion(), dataObject.getClass().getSimpleName(), roleName, Persistence.getEntityId( dataObject ), getOperation(), PermissionTypes.GRANT };
    serverCall( responder, method, args, dataObject.getClass() );
  }

  public <T> void denyForRole( String roleName, Object dataObject )
  {
    denyForRole( roleName, dataObject, null );
  }

  public <T> void denyForRole( String roleName, Object dataObject, AsyncCallback<T> responder )
  {
    String method = "updateRolePermission";
    Object[] args = { Backendless.getApplicationId(), Backendless.getVersion(), dataObject.getClass().getSimpleName(), roleName, Persistence.getEntityId( dataObject ), getOperation(), PermissionTypes.DENY };
    serverCall( responder, method, args, null );
  }

  public <T> void grantForAllUsers( Object dataObject )
  {
    grantForAllUsers( dataObject, null );
  }

  public <T> void grantForAllUsers( Object dataObject, AsyncCallback<T> responder )
  {
    String method = "updateAllUserPermission";
    Object[] args = { Backendless.getApplicationId(), Backendless.getVersion(), dataObject.getClass().getSimpleName(), Persistence.getEntityId( dataObject ), getOperation(), PermissionTypes.GRANT };
    serverCall( responder, method, args, dataObject.getClass() );
  }

  public <T> void denyForAllUsers( Object dataObject )
  {
    denyForAllUsers( dataObject, null );
  }

  public <T> void denyForAllUsers( Object dataObject, AsyncCallback<T> responder )
  {
    String method = "updateAllUserPermission";
    Object[] args = { Backendless.getApplicationId(), Backendless.getVersion(), dataObject.getClass().getSimpleName(), Persistence.getEntityId( dataObject ), getOperation(), PermissionTypes.DENY };
    serverCall( responder, method, args, null );
  }

  public <T> void grantForAllRoles( Object dataObject )
  {
    grantForAllRoles( dataObject, null );
  }

  public <T> void grantForAllRoles( Object dataObject, AsyncCallback<T> responder )
  {
    String method = "updateAllRolePermission";
    Object[] args = { Backendless.getApplicationId(), Backendless.getVersion(), dataObject.getClass().getSimpleName(), Persistence.getEntityId( dataObject ), getOperation(), PermissionTypes.GRANT };
    serverCall( responder, method, args, dataObject.getClass() );
  }

  public <T> void denyForAllRoles( Object dataObject )
  {
    denyForAllRoles( dataObject, null );
  }

  public <T> void denyForAllRoles( Object dataObject, AsyncCallback<T> responder )
  {
    String method = "updateAllRolePermission";
    Object[] args = { Backendless.getApplicationId(), Backendless.getVersion(), dataObject.getClass().getSimpleName(), Persistence.getEntityId( dataObject ), getOperation(), PermissionTypes.DENY };
    serverCall( responder, method, args, null );
  }

  private <T> void serverCall( AsyncCallback<T> responder, String method, Object[] args, Class type )
  {
    Invoker.invokeAsync( PERMISSION_SERVICE, method, args, responder, new AdaptingResponder<T>( type, new PoJoAdaptingPolicy<T>() ) );
  }
}
