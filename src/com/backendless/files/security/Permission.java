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

import com.backendless.utils.PermissionTypes;

public abstract class Permission
{
  protected String folder;
  protected PermissionTypes access;
  protected FileOperation operation;

  protected Permission( String folder, PermissionTypes access, FileOperation operation )
  {
    this.folder = folder;
    this.access = access;
    this.operation = operation;
  }

  public Permission()
  {
  }

  public boolean hasAccess()
  {
    return access.equals( PermissionTypes.GRANT ) ;
  }

  public String getFolder()
  {
    return folder;
  }

  public PermissionTypes getAccess()
  {
    return access;
  }

  public FileOperation getOperation()
  {
    return operation;
  }

  public void setFolder( String folder )
  {
    this.folder = folder;
  }

  public void setAccess( PermissionTypes access )
  {
    this.access = access;
  }

  public void setOperation( FileOperation operation )
  {
    this.operation = operation;
  }
}

