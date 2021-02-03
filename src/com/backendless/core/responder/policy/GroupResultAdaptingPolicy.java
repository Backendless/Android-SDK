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

package com.backendless.core.responder.policy;

import weborb.client.IResponder;
import weborb.reader.AnonymousObject;
import weborb.reader.ArrayType;
import weborb.reader.NamedObject;
import weborb.types.IAdaptingType;

import java.util.ArrayList;
import java.util.List;

public class GroupResultAdaptingPolicy<E> implements IAdaptingPolicy<E>
{

  @Override
  public Object adapt( Class<E> clazz, IAdaptingType entity, IResponder nextResponder )
  {
    Object result;

    if( entity == null )
      return null;

    NamedObject namedObj = (NamedObject) entity;

    List<NamedObject> namedObjects = new ArrayList<>();
    namedObjects.add( namedObj );

    while( ((AnonymousObject) namedObjects.get( 0 ).getTypedObject()).getProperties().containsKey( "hasNextPage" ) )
    {
      List<NamedObject> nextLevel = new ArrayList<>();
      for( NamedObject namedObject : namedObjects )
      {
        ArrayType arrayType = (ArrayType) ((AnonymousObject) namedObject.getTypedObject()).getProperties().get( "items" );

        Object[] dataArray = (Object[]) arrayType.getArray();
        for( Object item : dataArray )
          nextLevel.add( (NamedObject) item );
        namedObjects = nextLevel;
      }
    }

    if( clazz != null && weborb.types.Types.getMappedClientClass( clazz.getName() ) == null )
    {
      for( NamedObject namedObject : namedObjects )
        namedObject.setDefaultType( clazz );
    }

    result = entity.defaultAdapt();

    if( nextResponder != null )
      nextResponder.responseHandler( result );

    return result;
  }

}
