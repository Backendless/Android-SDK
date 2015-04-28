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

package com.backendless.files.security;

import com.backendless.Backendless;
import com.backendless.Invoker;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.core.responder.AdaptingResponder;
import com.backendless.core.responder.policy.PoJoAdaptingPolicy;
import com.backendless.utils.PermissionTypes;

public abstract class AbstractFilePermission
{
  private static final String PERMISSION_SERVICE = "com.backendless.services.file.FileService";

  protected abstract FileOperation getOperation();

  public void grantForUser( String userId, String fileOrDirURL )
  {
    grantForUser( userId, fileOrDirURL, null );
  }

  public void grantForUser( String userId, String fileOrDirURL, AsyncCallback<Void> responder )
  {
    String method = "updateUserPermission";
    Object[] args = buildArgs( fileOrDirURL, userId, false, PermissionTypes.GRANT );
    serverCall( responder, method, args );
  }

  public void denyForUser( String userId, String fileOrDirURL )
  {
    denyForUser( userId, fileOrDirURL, null );
  }

  public void denyForUser( String userId, String fileOrDirURL, AsyncCallback responder )
  {
    String method = "updateUserPermission";
    Object[] args = buildArgs( fileOrDirURL, userId, false, PermissionTypes.DENY );
    serverCall( responder, method, args );
  }

  public void grantForRole( String roleName, String fileOrDirURL )
  {
    grantForRole( roleName, fileOrDirURL, null );
  }

  public void grantForRole( String roleName, String fileOrDirURL, AsyncCallback responder )
  {
    String method = "updateRolePermissions";
    Object[] args = buildArgs( fileOrDirURL, roleName, true, PermissionTypes.GRANT );
    serverCall( responder, method, args );
  }

  public void denyForRole( String roleName, String fileOrDirURL )
  {
    denyForRole( roleName, fileOrDirURL, null );
  }

  public void denyForRole( String roleName, String fileOrDirURL, AsyncCallback responder )
  {
    String method = "updateRolePermissions";
    Object[] args = buildArgs( fileOrDirURL, roleName, true, PermissionTypes.DENY );
    serverCall( responder, method, args );
  }

  public void grantForAllUsers( String fileOrDirURL )
  {
    grantForAllUsers( fileOrDirURL, null );
  }

  public void grantForAllUsers( String fileOrDirURL, AsyncCallback responder )
  {
    String method = "updatePermissionForAllUsers";
    Object[] args = buildArgs( fileOrDirURL, null, false, PermissionTypes.GRANT );
    serverCall( responder, method, args );
  }

  public void denyForAllUsers( String fileOrDirURL )
  {
    denyForAllUsers( fileOrDirURL, null );
  }

  public void denyForAllUsers( String fileOrDirURL, AsyncCallback responder )
  {
    String method = "updatePermissionForAllUsers";
    Object[] args = buildArgs( fileOrDirURL, null, false, PermissionTypes.DENY);
    serverCall( responder, method, args );
  }

  public void grantForAllRoles( String fileOrDirURL )
  {
    grantForAllRoles( fileOrDirURL, null );
  }

  public void grantForAllRoles( String fileOrDirURL, AsyncCallback responder )
  {
    String method = "updateRolePermissionsForAllRoles";
    Object[] args = buildArgs( fileOrDirURL, null, true, PermissionTypes.GRANT );
    serverCall( responder, method, args );
  }

  public void denyForAllRoles( String fileOrDirURL )
  {
    denyForAllRoles( fileOrDirURL, null );
  }

  public void denyForAllRoles( String fileOrDirURL, AsyncCallback responder )
  {
    String method = "updateRolePermissionsForAllRoles";
    Object[] args = buildArgs( fileOrDirURL, null, true, PermissionTypes.DENY );
    serverCall( responder, method, args );
  }

  private Object[] buildArgs( String fileOrDirURL, String principal, boolean isRole, PermissionTypes permissionType )
  {
    String appId = Backendless.getApplicationId();
    String version = Backendless.getVersion();
    FileOperation operation = getOperation();

      Permission permission;

      if( isRole )
          permission = new FileRolePermission( fileOrDirURL, permissionType, operation );
      else
          permission = new FileUserPermission( fileOrDirURL, permissionType, operation );

    if( principal != null )
      return new Object[]{appId, version, principal, permission};
    else
      return new Object[]{appId, version, permission};
  }

  private void serverCall( AsyncCallback responder, String method, Object[] args )
  {
    if( responder == null )
      Invoker.invokeSync( PERMISSION_SERVICE, method, args, new AdaptingResponder( null, new PoJoAdaptingPolicy() ) );
    else
      Invoker.invokeAsync( PERMISSION_SERVICE, method, args, responder, new AdaptingResponder( null, new PoJoAdaptingPolicy() ) );
  }
}
