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

package com.backendless.utils;

import com.backendless.Persistence;
import com.backendless.core.responder.AdaptingResponder;
import com.backendless.core.responder.policy.CollectionAdaptingPolicy;
import com.backendless.core.responder.policy.DecoratorCachingAdaptingPolicy;
import com.backendless.core.responder.policy.IAdaptingPolicy;
import com.backendless.core.responder.policy.PoJoAdaptingPolicy;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.BackendlessSerializer;

import java.util.Date;

public class ResponderHelper
{
  public static <E> AdaptingResponder getCollectionAdaptingResponder( Class<E> entity )
  {
    entity = BackendlessSerializer.getClassForDeserialization( entity );
    return getAdaptingResponder( entity, new CollectionAdaptingPolicy<E>() );
  }

  public static  <E> AdaptingResponder getPOJOAdaptingResponder( Class<E> entity )
  {
    entity = BackendlessSerializer.getClassForDeserialization( entity );
    return getAdaptingResponder( entity, new PoJoAdaptingPolicy<E>() );
  }

  public static <E> AdaptingResponder getAdaptingResponder( Class<E> entity, IAdaptingPolicy<E> policy )
  {
    if( needsPhantomCache( entity ) )
      policy = new DecoratorCachingAdaptingPolicy<E>( policy );

    return new AdaptingResponder<E>( entity, policy );
  }

  public static <T> boolean needsPhantomCache( Class<T> entityClass ) throws BackendlessException
  {
    try
    {
      if( !entityClass.getField( Persistence.DEFAULT_OBJECT_ID_FIELD ).getType().equals( String.class ) )
        throw new IllegalArgumentException( ExceptionMessage.ENTITY_WRONG_OBJECT_ID_FIELD_TYPE );

      if( !entityClass.getField( Persistence.DEFAULT_META_FIELD ).getType().equals( String.class ) )
        throw new IllegalArgumentException( ExceptionMessage.ENTITY_WRONG_META_FIELD_TYPE );

      if( !entityClass.getField( Persistence.DEFAULT_CREATED_FIELD ).getType().equals( Date.class ) )
        throw new IllegalArgumentException( ExceptionMessage.ENTITY_WRONG_CREATED_FIELD_TYPE );

      if( !entityClass.getField( Persistence.DEFAULT_UPDATED_FIELD ).getType().equals( Date.class ) )
        throw new IllegalArgumentException( ExceptionMessage.ENTITY_WRONG_UPDATED_FIELD_TYPE );
    }
    catch( NoSuchFieldException e )
    {
      return true;
    }

    return false;
  }
}
