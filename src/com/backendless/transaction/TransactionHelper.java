package com.backendless.transaction;

import com.backendless.Persistence;
import com.backendless.exceptions.ExceptionMessage;

import java.util.ArrayList;
import java.util.Arrays;
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

  static List<String> convertInstancesMapsToObjectIds( List<Map<String, Object>> objectsMaps )
  {
    List<String> objectIds = new ArrayList<>();
    for( Map<String, Object> objectMap : objectsMaps )
      objectIds.add( convertObjectMapToObjectId( objectMap ) );

    return objectIds;
  }

  static List<Object> convertMapsToObjectIds( List<Map<String, Object>> objectsMaps )
  {
    List<Object> objectIds = new ArrayList<>();
    for( Map<String, Object> objectMap : objectsMaps )
      objectIds.add( convertObjectMapToObjectId( objectMap ) );

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

  // accept children not String[], it check before
  static <E> List<String> getObjectIdsFromListInstances( E[] children )
  {
    if( children[ 0 ] instanceof Map )
      throw new IllegalArgumentException( ExceptionMessage.RELATION_USE_LIST_OF_MAPS );
    else if( !( children[ 0 ].getClass().isArray() || children[ 0 ].getClass().isAssignableFrom( Iterable.class ) ) )
      return TransactionHelper.convertInstancesMapsToObjectIds( TransactionHelper.convertInstancesToMaps( Arrays.asList( children ) ) );
    else
      throw new IllegalArgumentException( ExceptionMessage.LIST_NOT_INSTANCES );
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

  static void makeReferenceToValueFromOpResult( Map<String, Object> map )
  {
    for( Map.Entry<String, Object> entry : map.entrySet() )
    {
      if( entry.getValue() instanceof OpResult )
        if( OperationType.supportIntResultType.contains( ( (OpResult) entry.getValue() ).getOperationType() ) )
          entry.setValue( ( (OpResult) entry.getValue() ).makeReference() );
        else
          throw new IllegalArgumentException( ExceptionMessage.OP_RESULT_FROM_THIS_OPERATION_NOT_SUPPORT_IN_THIS_PLACE );
      if( entry.getValue() instanceof OpResultValueReference )
      {
        OpResultValueReference reference = (OpResultValueReference) entry.getValue();
        if( createUpdatePropName( reference ) ||
                createBulkResultIndex( reference ) ||
                findPropNameResultIndex( reference ) )
          entry.setValue( reference.makeReference() );
        else
          throw new IllegalArgumentException( ExceptionMessage.OP_RESULT_FROM_THIS_OPERATION_NOT_SUPPORT_IN_THIS_PLACE );
      }
    }
  }

  private static boolean createUpdatePropName( OpResultValueReference reference )
  {
    return OperationType.supportEntityDescriptionResultType.contains( reference.getOpResult().getOperationType() ) &&
            reference.getPropName() != null &&
            reference.getResultIndex() == null;
  }

  private static boolean createBulkResultIndex( OpResultValueReference reference )
  {
    return OperationType.CREATE_BULK.equals( reference.getOpResult().getOperationType() ) &&
            reference.getPropName() == null &&
            reference.getResultIndex() != null;
  }

  private static boolean findPropNameResultIndex( OpResultValueReference reference )
  {
    return OperationType.FIND.equals( reference.getOpResult().getOperationType() ) &&
            reference.getPropName() != null &&
            reference.getResultIndex() != null;
  }
}
