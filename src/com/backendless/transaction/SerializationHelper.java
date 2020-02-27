package com.backendless.transaction;

import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.BackendlessSerializer;
import com.backendless.utils.MapEntityUtil;

import java.lang.reflect.Constructor;
import java.util.Map;

public class SerializationHelper
{
  public static <E> Map<String, Object> serializeEntityToMap( final E entity )
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    checkDeclaredType( entity.getClass() );
    final Map<String, Object> serializedEntity = BackendlessSerializer.serializeToMap( entity );
    MapEntityUtil.removeNullsAndRelations( serializedEntity);

    return serializedEntity;
  }

  private static <T> void checkDeclaredType( Class<T> entityClass )
  {
    if( entityClass.isArray() || entityClass.isAssignableFrom( Iterable.class ) || entityClass.isAssignableFrom( Map.class ) )
      throw new IllegalArgumentException( ExceptionMessage.WRONG_ENTITY_TYPE );

    try
    {
      Constructor[] constructors = entityClass.getConstructors();

      if( constructors.length > 0 )
        entityClass.getConstructor();
    }
    catch( NoSuchMethodException e )
    {
      throw new IllegalArgumentException( ExceptionMessage.ENTITY_MISSING_DEFAULT_CONSTRUCTOR );
    }
  }
}
