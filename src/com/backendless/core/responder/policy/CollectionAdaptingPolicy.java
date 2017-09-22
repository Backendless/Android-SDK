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

import weborb.client.Fault;
import weborb.client.IResponder;
import weborb.exceptions.AdaptingException;
import weborb.reader.ArrayType;
import weborb.reader.NamedObject;
import weborb.types.IAdaptingType;

import java.util.ArrayList;
import java.util.List;

public class CollectionAdaptingPolicy<E> implements IAdaptingPolicy<E>
{
  @Override
  public Object adapt( Class<E> clazz, IAdaptingType entity, IResponder nextResponder )
  {
    List<E> result = null;

    try
    {
      List<E> list = new ArrayList<>();
      if( entity == null )
        return list;

      ArrayType data = (ArrayType) entity;

      Object[] dataArray = (Object[]) data.getArray();

      if( clazz != null && weborb.types.Types.getMappedClientClass( clazz.getName() ) == null )
      {
        for ( Object aDataArray : dataArray )
          if( aDataArray instanceof  NamedObject)
           ((NamedObject) aDataArray).setDefaultType( clazz );
      }

      result = (List<E>) entity.adapt( list.getClass() );

      if( nextResponder != null )
        nextResponder.responseHandler( result );
    }
    catch( AdaptingException e )
    {
      Fault fault = new Fault( "Unable to adapt response to List<" + clazz.getName() + ">", e.getMessage() );

      if( nextResponder != null )
        nextResponder.errorHandler( fault );
    }

    return result;
  }
}
