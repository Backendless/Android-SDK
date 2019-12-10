package com.backendless.transaction;

import com.backendless.exceptions.ExceptionMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionHelper
{
  public static OpResult makeOpResult( String operationResultId )
  {
    Map<String, Object> reference = new HashMap<>();
    reference.put( UnitOfWork.REFERENCE_MARKER, true );
    reference.put( UnitOfWork.OP_RESULT_ID, operationResultId );
    return new OpResult( reference );
  }

  static  <E> List<Map<String, Object>> getConvertInstancesToMaps( List<E> instances )
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
}
