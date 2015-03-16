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

import com.backendless.BackendlessCollection;
import weborb.client.Fault;
import weborb.client.IResponder;
import weborb.exceptions.AdaptingException;
import weborb.reader.AnonymousObject;
import weborb.reader.ArrayType;
import weborb.reader.NamedObject;
import weborb.types.IAdaptingType;

public class CollectionAdaptingPolicy<E> implements IAdaptingPolicy<E>
{
  @Override
  public Object adapt( Class<E> clazz, IAdaptingType entity, IResponder nextResponder )
  {
    BackendlessCollection<?> result = null;

    try
    {
      BackendlessCollection<E> list = createListOfType( clazz );

      AnonymousObject bodyValue = (AnonymousObject) ((NamedObject) entity).getTypedObject();
      ArrayType data = (ArrayType) bodyValue.getProperties().get( "data" );

      if( data != null )
      {
        Object[] dataArray = (Object[]) data.getArray();

        if( weborb.types.Types.getMappedClientClass( clazz.getName() ) == null )
        {
          for( int i = 0; i < dataArray.length; i++ )
            ((NamedObject) dataArray[ i ]).setDefaultType( clazz );
        }
      }

      result = (BackendlessCollection<?>) entity.adapt( list.getClass() );

      if( nextResponder != null )
        nextResponder.responseHandler( result );
    }
    catch( AdaptingException e )
    {
      Fault fault = new Fault( "Unable to adapt response to BackendlessCollection", e.getMessage() );

      if( nextResponder != null )
        nextResponder.errorHandler( fault );
    }

    return result;
  }

  private static <E> BackendlessCollection<E> createListOfType( Class<E> type )
  {
    return new BackendlessCollection<E>();
  }
}
