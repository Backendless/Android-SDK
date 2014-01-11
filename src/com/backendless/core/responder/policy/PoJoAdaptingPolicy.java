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
import weborb.types.IAdaptingType;

public class PoJoAdaptingPolicy<E> implements IAdaptingPolicy<E>
{
  @Override
  public Object adapt( Class<E> clazz, IAdaptingType entity, IResponder nextResponder )
  {
    E result = null;

    try
    {
      result = (E) (clazz != null ? entity.adapt( clazz ) : entity.defaultAdapt());

      if( nextResponder != null )
        nextResponder.responseHandler( result );
    }
    catch( AdaptingException e )
    {
      Fault fault = new Fault( "Unable to adapt response to " + clazz.getSimpleName(), e.getMessage() );

      if( nextResponder != null )
        nextResponder.errorHandler( fault );
    }
    catch( ClassCastException e )
    {
      Fault fault = new Fault( "Unable to cast result", e.getMessage() );

      if( nextResponder != null )
        nextResponder.errorHandler( fault );
    }

    return result;
  }
}
