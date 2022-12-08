package com.backendless.persistence;

import com.backendless.utils.PermissionTypes;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AclPermissionDTO
{
  private PersistenceOperations permission;
  private PermissionTypes permissionType;
  private String userId;

  public AclPermissionDTO( PersistenceOperations permission, PermissionTypes permissionType, String userId )
  {
    this.permission = permission;
    this.permissionType = permissionType;
    this.userId = userId;
  }
}
