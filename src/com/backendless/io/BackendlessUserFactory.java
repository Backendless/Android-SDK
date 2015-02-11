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

package com.backendless.io;

import com.backendless.BackendlessUser;
import weborb.reader.AnonymousObject;
import weborb.reader.NamedObject;
import weborb.reader.ReferenceCache;
import weborb.types.IAdaptingType;
import weborb.util.IArgumentObjectFactory;

import java.lang.reflect.Type;
import java.util.HashMap;

public class BackendlessUserFactory implements IArgumentObjectFactory
{
  @Override
  public Object createObject( IAdaptingType iAdaptingType )
  {
    if( iAdaptingType instanceof NamedObject )
      iAdaptingType = ((NamedObject) iAdaptingType).getTypedObject();

    if( iAdaptingType.getClass().getName().equals( "weborb.reader.NullType" ))
      return null;

      ReferenceCache refCache = ReferenceCache.getInstance();
      BackendlessUser backendlessUser;

      if( refCache.hasObject( iAdaptingType, BackendlessUser.class ) )
      {
          return refCache.getObject( iAdaptingType, BackendlessUser.class );
      }
      else
      {
          backendlessUser = new BackendlessUser();
          refCache.addObject( iAdaptingType, BackendlessUser.class, backendlessUser );
      }

    HashMap props;

    try
    {
      props = (HashMap) iAdaptingType.adapt( HashMap.class );
    }
    catch( Throwable t )
    {
      props = new HashMap();
    }

    backendlessUser.setProperties( props );
    return backendlessUser;
  }

  @Override
  public boolean canAdapt( IAdaptingType iAdaptingType, Type type )
  {
    return false;
  }
}
