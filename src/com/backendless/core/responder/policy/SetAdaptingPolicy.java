package com.backendless.core.responder.policy;

import weborb.client.Fault;
import weborb.client.IResponder;
import weborb.exceptions.AdaptingException;
import weborb.reader.ArrayType;
import weborb.reader.NamedObject;
import weborb.types.IAdaptingType;

import java.util.HashSet;
import java.util.Set;

public class SetAdaptingPolicy<E> implements IAdaptingPolicy<E>
{
  @Override
  public Object adapt( Class<E> clazz, IAdaptingType entity, IResponder nextResponder )
  {
    Set<E> result = null;

    try
    {
      Set<E> set = new HashSet<>();
      if( entity == null )
        return set;

      ArrayType data = (ArrayType) entity;

      Object[] dataArray = (Object[]) data.getArray();

      if( clazz != null && weborb.types.Types.getMappedClientClass( clazz.getName() ) == null )
      {
        for ( Object aDataArray : dataArray )
          if( aDataArray instanceof NamedObject )
            ((NamedObject) aDataArray).setDefaultType( clazz );
      }

      result = (Set<E>) entity.adapt( set.getClass() );

      if( nextResponder != null )
        nextResponder.responseHandler( result );
    }
    catch( AdaptingException e )
    {
      Fault fault = new Fault( "Unable to adapt response to Set<" + clazz.getName() + ">", e.getMessage() );

      if( nextResponder != null )
        nextResponder.errorHandler( fault );
    }

    return result;
  }
}
