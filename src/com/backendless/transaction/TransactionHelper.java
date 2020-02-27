package com.backendless.transaction;

import com.backendless.Persistence;
import com.backendless.exceptions.ExceptionMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class TransactionHelper
{
  private static final String LAST_LOGIN_COLUMN_NAME = "lastLogin";
  private static final String PASSWORD_KEY = "password";
  private static final String SOCIAL_ACCOUNT_COLUMN_NAME = "socialAccount";
  private static final String USER_STATUS_COLUMN_NAME = "userStatus";

  static void removeSystemField( Map<String, Object> changes )
  {
    changes.remove( LAST_LOGIN_COLUMN_NAME );
    changes.remove( PASSWORD_KEY );
    changes.remove( SOCIAL_ACCOUNT_COLUMN_NAME );
    changes.remove( USER_STATUS_COLUMN_NAME );
    changes.remove( Persistence.DEFAULT_OBJECT_ID_FIELD );
    changes.remove( Persistence.DEFAULT_CREATED_FIELD );
    changes.remove( Persistence.DEFAULT_UPDATED_FIELD );
  }

  static OpResult makeOpResult( String tableName, String operationResultId, OperationType operationType )
  {
    return new OpResult( tableName, operationType, operationResultId );
  }

  static <E> List<Map<String, Object>> convertInstancesToMaps( List<E> instances )
  {
    if( instances == null || instances.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    List<Map<String, Object>> serializedEntities = new ArrayList<>();
    for ( final Object entity : instances )
      serializedEntities.add( SerializationHelper.serializeEntityToMap( entity ) );

    return serializedEntities;
  }

  static List<Object> convertMapsToObjectIds( List<Map<String, Object>> objectsMaps )
  {
    List<Object> objectIds = new ArrayList<>();
    for( Map<String, Object> objectMap : objectsMaps )
      objectIds.add( convertObjectMapToObjectIdOrLeaveReference( objectMap ) );

    return objectIds;
  }

  static String convertObjectMapToObjectId( Map<String, Object> objectMap )
  {
    if( objectMap == null || objectMap.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_MAP );

    Object maybeObjectId = objectMap.get( Persistence.DEFAULT_OBJECT_ID_FIELD );
    if( !( maybeObjectId instanceof String ) )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OBJECT_ID_IN_OBJECT_MAP );

    return (String) maybeObjectId;
  }

  static Object convertObjectMapToObjectIdOrLeaveReference( Map<String, Object> objectMap )
  {
    if( objectMap == null || objectMap.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_MAP );

    if( objectMap.containsKey( UnitOfWork.REFERENCE_MARKER ) )
    {
      objectMap.put( UnitOfWork.PROP_NAME, Persistence.DEFAULT_OBJECT_ID_FIELD );
      return objectMap;
    }

    Object maybeObjectId = objectMap.get( Persistence.DEFAULT_OBJECT_ID_FIELD );
    if( !( maybeObjectId instanceof String ) )
      throw new IllegalArgumentException( ExceptionMessage.NULL_OBJECT_ID_IN_OBJECT_MAP );

    return maybeObjectId;
  }

  static <E> List<Object> getObjectIdsFromUnknownList( List<E> children )
  {
    List<Object> childrenMaps;
    if( children.get( 0 ) instanceof Map )
      childrenMaps = TransactionHelper.convertMapsToObjectIds( (List<Map<String, Object>>) children );
    else if( children.get( 0 ) instanceof String )
      childrenMaps = (List<Object>) children;
    else if( !( children.get( 0 ).getClass().isArray() || children.get( 0 ).getClass().isAssignableFrom( Iterable.class ) ) )
      childrenMaps = TransactionHelper.convertMapsToObjectIds( TransactionHelper.convertInstancesToMaps( children ) );
    else
      throw new IllegalArgumentException( ExceptionMessage.LIST_MAP_OR_STRING_OR_INSTANCES );
    return childrenMaps;
  }

  static Map<String, Object> convertCreateBulkOrFindResultIndexToObjectId( OpResultValueReference parentObject )
  {
    Map<String, Object> referenceToObjectId;
    if( OperationType.supportCollectionEntityDescriptionType.contains( parentObject.getOpResult().getOperationType() ) )
      referenceToObjectId = parentObject.resolveTo( Persistence.DEFAULT_OBJECT_ID_FIELD ).makeReference();
    else if( OperationType.supportListIdsResultType.contains( parentObject.getOpResult().getOperationType() ) )
      referenceToObjectId = parentObject.makeReference();
    else
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );
    return referenceToObjectId;
  }
}
