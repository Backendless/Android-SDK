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

import com.backendless.persistence.PersistenceOperations;

public class DataPermission
{
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
}
