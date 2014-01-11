package com.backendless.core.responder.policy;

import weborb.client.IResponder;
import weborb.exceptions.AdaptingException;
import weborb.types.IAdaptingType;

public interface IAdaptingPolicy<E>
{
  Object adapt( Class<E> clazz, IAdaptingType entity, IResponder nextResponder ) throws AdaptingException;
}
