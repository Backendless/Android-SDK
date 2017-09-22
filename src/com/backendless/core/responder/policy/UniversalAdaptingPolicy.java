package com.backendless.core.responder.policy;

import weborb.client.IResponder;
import weborb.exceptions.AdaptingException;
import weborb.reader.ArrayType;
import weborb.types.IAdaptingType;

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
 * CREATED ON: 8/15/17
 * AT: 4:39 PM
 **********************************************************************************************************************/
public class UniversalAdaptingPolicy<E> implements IAdaptingPolicy<E>
{
  private IAdaptingPolicy<E> collectionAdaptingPolicy;
  private IAdaptingPolicy<E> pojoAdaptingPolicy;

  public UniversalAdaptingPolicy()
  {
    collectionAdaptingPolicy = new CollectionAdaptingPolicy<>();
    pojoAdaptingPolicy = new PoJoAdaptingPolicy<>();
  }

  @Override
  public Object adapt( Class<E> clazz, IAdaptingType entity, IResponder nextResponder ) throws AdaptingException
  {
    if( entity != null && entity instanceof ArrayType)
      return collectionAdaptingPolicy.adapt( clazz, entity, nextResponder );
    else
      return pojoAdaptingPolicy.adapt( clazz, entity, nextResponder );
  }
}
