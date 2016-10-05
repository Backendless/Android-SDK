package com.backendless.files;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import weborb.reader.NamedObject;
import weborb.reader.ReferenceCache;
import weborb.reader.StringType;
import weborb.util.IArgumentObjectFactory;
import weborb.types.IAdaptingType;

import java.lang.reflect.Type;

/**********************************************************************************************************************
 * BACKENDLESS.COM CONFIDENTIAL
 * <p>
 * *********************************************************************************************************************
 * <p>
 * Copyright 2012 BACKENDLESS.COM. All Rights Reserved.
 * <p>
 * NOTICE:  All information contained herein is, and remains the property of Backendless.com and its suppliers,
 * if any.  The intellectual and technical concepts contained herein are proprietary to Backendless.com and its
 * suppliers and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret
 * or copyright law. Dissemination of this information or reproduction of this material is strictly forbidden
 * unless prior written permission is obtained from Backendless.com.
 * <p>
 * CREATED ON: 6/7/16
 * AT: 4:23 PM
 **********************************************************************************************************************/
public class BackendlessFileFactory implements IArgumentObjectFactory
{
  public Object createObject( IAdaptingType argument )
  {
    if( argument instanceof NamedObject )
      argument = ((NamedObject) argument).getTypedObject();

    if( argument.getClass().getName().equals( "weborb.reader.NullType" ) )
      return null;

    ReferenceCache refCache = ReferenceCache.getInstance();

    if( refCache.hasObject( argument, BackendlessFile.class ) )
    {
      return refCache.getObject( argument, BackendlessFile.class );
    }
    else
    {
      if( argument instanceof StringType )
      {
        StringType stringType = (StringType) argument;
        BackendlessFile file;

        if( Backendless.isAndroid() )
          file = new BackendlessFileAndroid( stringType.getValue() );
        else
          file = new BackendlessFile( stringType.getValue() );

        refCache.addObject( argument, BackendlessFile.class, file );
        return file;
      }
      else
        throw new RuntimeException( "unknown type" );
    }
  }

  public boolean canAdapt( IAdaptingType type, Type target )
  {
    return false;
  }
}
