package com.backendless.persistence;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.UserService;

import java.util.Map;

/**
 * *******************************************************************************************************************
 * <p/>
 * BACKENDLESS.COM CONFIDENTIAL
 * <p/>
 * *********************************************************************************************************************
 * <p/>
 * Copyright 2012 BACKENDLESS.COM. All Rights Reserved.
 * <p/>
 * NOTICE:  All information contained herein is, and remains the property of Backendless.com and its suppliers,
 * if any.  The intellectual and technical concepts contained herein are proprietary to Backendless.com and its
 * suppliers and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret
 * or copyright law. Dissemination of this information or reproduction of this material is strictly forbidden
 * unless prior written permission is obtained from Backendless.com.
 * <p/>
 * CREATED ON: 3/3/16
 * AT: 1:43 PM
 * ********************************************************************************************************************
 */
public class DefaultSerializer extends BackendlessSerializer implements IObjectSerializer
{
  public boolean shouldTraverse()
  {
    return true;
  }

  public Class getSerializationFriendlyClass( Class clazz )
  {
    return clazz;
  }

  public String getClassName( Class clazz )
  {
    if( clazz == BackendlessUser.class )
    {
      return UserService.USERS_TABLE_NAME;
    }
    else
    {
      String mappedName = weborb.types.Types.getMappedClientClass( clazz.getName() );

      if( mappedName != null )
        return mappedName;
      else
        return clazz.getSimpleName();
    }
  }
}
