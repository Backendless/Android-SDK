package com.backendless.transaction;

import com.backendless.Persistence;
import com.backendless.exceptions.ExceptionMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionHelper
{
  public static OpResult makeOpResult( String operationResultId, OperationType operationType )
  {
    Map<String, Object> reference = new HashMap<>();
    reference.put( UnitOfWork.REFERENCE_MARKER, true );
    reference.put( UnitOfWork.OP_RESULT_ID, operationResultId );
    return new OpResult( reference, operationType );
  }

  static  <E> List<Map<String, Object>> convertInstancesToMaps( List<E> instances )
  {
    if( instances == null || instances.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    List<Map<String, Object>> serializedEntities = new ArrayList<>();
    for ( final Object entity : instances )
    {
      serializedEntities.add( SerializationHelper.serializeEntityToMap( entity ) );
    }
    return serializedEntities;
  }

  static List<String> convertMapToObjectIds( List<Map<String, Object>> objectsMaps )
  {
    List<String> objectIds = new ArrayList<>();
    for( Map<String, Object> map : objectsMaps )
    {
      objectIds.add( (String) map.get( Persistence.DEFAULT_OBJECT_ID_FIELD ) );
    }
    return objectIds;
  }

  static <E> List<String> getObjectIdsFromUnknownList( List<E> children )
  {
    List<String> childrenMaps;
    if( children.get( 0 ).getClass().isAssignableFrom( Map.class ) )
      childrenMaps = TransactionHelper.convertMapToObjectIds( (List<Map<String, Object>>) children );
    else if( children.get( 0 ).getClass().isAssignableFrom( String.class ) )
      childrenMaps = (List<String>) children;
    else if( !( children.get( 0 ).getClass().isArray() || children.get( 0 ).getClass().isAssignableFrom( Iterable.class ) ) )
      childrenMaps = TransactionHelper.convertMapToObjectIds( TransactionHelper.convertInstancesToMaps( children ) );
    else
      throw new IllegalArgumentException( ExceptionMessage.LIST_MAP_OR_STRING_OR_INSTANCES );
    return childrenMaps;
  }
}
