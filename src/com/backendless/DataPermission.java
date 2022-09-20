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
import com.backendless.persistence.AclPermissionDTO;
import com.backendless.persistence.PersistenceOperations;

import java.util.List;

public class DataPermission
{
  private static final String PERMISSION_SERVICE = "com.backendless.services.persistence.permissions.ClientPermissionService";

  public static final Find FIND = new Find();
  public static final Update UPDATE = new Update();
  public static final Remove REMOVE = new Remove();

  public static class Find extends AbstractDataPermission
  {
    @Override
    protected PersistenceOperations getOperation()
    {
      return PersistenceOperations.FIND;
    }
  }

  public static class Update extends AbstractDataPermission
  {
    @Override
    protected PersistenceOperations getOperation()
    {
      return PersistenceOperations.UPDATE;
    }
  }

  public static class Remove extends AbstractDataPermission
  {
    @Override
    protected PersistenceOperations getOperation()
    {
      return PersistenceOperations.REMOVE;
    }
  }

  public Boolean[] updateUsersPermissions( String tableName, String objectId,
                                           List<AclPermissionDTO> permissions )
  {
    String method = "updateUsersPermissions";
    Object[] args = new Object[] { tableName, objectId, permissions };
    return Invoker.invokeSync( PERMISSION_SERVICE, method, args );
  }

  public void updateUsersPermissions( String tableName, String objectId,
                                      List<AclPermissionDTO> permissions, AsyncCallback<Boolean[]> callback )
  {
    String method = "updateUsersPermissions";
    Object[] args = new Object[] { tableName, objectId, permissions };
    Invoker.invokeAsync( PERMISSION_SERVICE, method, args, callback );
  }
}
