package com.backendless.core.responder.policy;

import weborb.client.IResponder;
import weborb.exceptions.AdaptingException;
import weborb.types.IAdaptingType;

abstract class AbstractAdaptingPolicyDecorator<E> implements IAdaptingPolicy<E>
{
  private IAdaptingPolicy<E> adaptingPolicy;

  AbstractAdaptingPolicyDecorator( IAdaptingPolicy<E> adaptingPolicy )
  {
    this.adaptingPolicy = adaptingPolicy;
  }

  @Override
  public Object adapt( Class<E> clazz, IAdaptingType entity, IResponder nextResponder ) throws AdaptingException {
    Object createdInstance = adaptingPolicy.adapt( clazz, entity, nextResponder );
    notifyInstanceCreated( createdInstance, entity );

    return createdInstance;
  }

  public abstract void notifyInstanceCreated( Object instance, IAdaptingType entity ) throws AdaptingException;
}
