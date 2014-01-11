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

package com.backendless.core.responder;

import com.backendless.core.responder.policy.IAdaptingPolicy;
import com.backendless.core.responder.policy.PoJoAdaptingPolicy;
import com.backendless.exceptions.BackendlessFault;
import weborb.client.Fault;
import weborb.client.IRawResponder;
import weborb.client.IResponder;
import weborb.exceptions.AdaptingException;
import weborb.reader.AnonymousObject;
import weborb.reader.NamedObject;
import weborb.reader.StringType;
import weborb.types.IAdaptingType;
import weborb.v3types.ErrMessage;

public class AdaptingResponder<E> implements IRawResponder
{
  private Class<E> clazz;
  private IResponder nextResponder;
  private IAdaptingPolicy<E> adaptingPolicy;

  public AdaptingResponder()
  {
    this.adaptingPolicy = new PoJoAdaptingPolicy<E>();
  }

  public AdaptingResponder( IAdaptingPolicy<E> adaptingPolicy )
  {
    this.adaptingPolicy = adaptingPolicy;
  }

  public AdaptingResponder( Class<E> clazz )
  {
    this.clazz = clazz;
  }

  public AdaptingResponder( Class<E> clazz, IAdaptingPolicy<E> adaptingPolicy )
  {
    this.clazz = clazz;
    this.adaptingPolicy = adaptingPolicy;
  }

  public AdaptingResponder( Class<E> clazz, IAdaptingPolicy<E> adaptingPolicy, IResponder nextResponder )
  {
    this.clazz = clazz;
    this.nextResponder = nextResponder;
    this.adaptingPolicy = adaptingPolicy;
  }

  public final void responseHandler( Object adaptingType )
  {
    IAdaptingType type = (IAdaptingType) adaptingType;
    IAdaptingType bodyHolder = ((NamedObject) type).getTypedObject();

    if( ((IAdaptingType) adaptingType).getDefaultType().equals( ErrMessage.class ) )
    {
      handledAsFault( (AnonymousObject) bodyHolder, nextResponder );
    }
    else
    {
      IAdaptingType entity = (IAdaptingType) ((AnonymousObject) bodyHolder).getProperties().get( "body" );
      try
      {
        adaptingPolicy.adapt( clazz, entity, nextResponder );
      }
      catch( AdaptingException e )
      {
        errorHandler( new BackendlessFault( e ) );
      }
    }
  }

  public void errorHandler( Fault fault )
  {
    nextResponder.errorHandler( fault );
  }

  public void setNextResponder( IResponder nextResponder )
  {
    this.nextResponder = nextResponder;
  }

  IResponder getNextResponder()
  {
    return nextResponder;
  }

  Class<E> getClazz()
  {
    return clazz;
  }

  private void handledAsFault( AnonymousObject bodyHolder, IResponder responder )
  {
    if( responder != null )
    {
      StringType faultMessage = (StringType) bodyHolder.getProperties().get( "faultString" );
      StringType faultDetail = (StringType) bodyHolder.getProperties().get( "faultDetail" );
      StringType faultCode = (StringType) bodyHolder.getProperties().get( "faultCode" );

      Fault fault = new Fault( (String) faultMessage.defaultAdapt(), (String) faultDetail.defaultAdapt(), (String) faultCode.defaultAdapt() );
      responder.errorHandler( fault );
    }
  }
}
