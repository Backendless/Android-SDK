package com.backendless.transaction;

import com.backendless.Persistence;
import com.backendless.exceptions.ExceptionMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionHelper
{
  private static final String LAST_LOGIN_COLUMN_NAME = "lastLogin";
  private static final String PASSWORD_KEY = "password";
  private static final String SOCIAL_ACCOUNT_COLUMN_NAME = "socialAccount";
  private static final String USER_STATUS_COLUMN_NAME = "userStatus";

  public static void removeSystemField( Map<String, Object> changes )
  {
    changes.remove( LAST_LOGIN_COLUMN_NAME );
    changes.remove( PASSWORD_KEY );
    changes.remove( SOCIAL_ACCOUNT_COLUMN_NAME );
    changes.remove( USER_STATUS_COLUMN_NAME );
    changes.remove( Persistence.DEFAULT_OBJECT_ID_FIELD );
    changes.remove( Persistence.DEFAULT_CREATED_FIELD );
    changes.remove( Persistence.DEFAULT_UPDATED_FIELD );
  }

  public static OpResult makeOpResult( String tableName, String operationResultId, OperationType operationType )
  {
    Map<String, Object> reference = new HashMap<>();
    reference.put( UnitOfWork.REFERENCE_MARKER, true );
    reference.put( UnitOfWork.OP_RESULT_ID, operationResultId );
    return new OpResult( tableName, reference, operationType );
  }

  static <E> List<String> convertInstancesToObjectIds( List<E> instances )
  {
    List<Map<String, Object>> objectMaps = convertInstancesToMaps( instances );
    List<String> objectIds = new ArrayList<>();
    for( Map<String, Object> map : objectMaps )
    {
      String objectId = (String) map.get( Persistence.DEFAULT_OBJECT_ID_FIELD );
      if( objectId == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_OBJECT_ID_IN_OBJECT_MAP );

      objectIds.add( objectId );
    }
    return objectIds;
  }

  static <E> List<Map<String, Object>> convertInstancesToMaps( List<E> instances )
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
