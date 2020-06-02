package com.backendless.transaction;

import com.backendless.transaction.payload.DeleteBulkPayload;
import com.backendless.transaction.payload.Relation;
import com.backendless.transaction.payload.UpdateBulkPayload;
import weborb.reader.AnonymousObject;
import weborb.reader.NamedObject;
import weborb.reader.ReferenceCache;
import weborb.types.IAdaptingType;
import weborb.util.IArgumentObjectFactory;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class OperationFactory implements IArgumentObjectFactory
{
  @Override
  public Object createObject( IAdaptingType adaptingType )
  {
    if( adaptingType instanceof NamedObject )
      adaptingType = ((NamedObject) adaptingType).getTypedObject();

    if( adaptingType.getClass().getName().equals( "weborb.reader.NullType" ) )
      return null;

    ReferenceCache refCache = ReferenceCache.getInstance();

    if( refCache.hasObject( adaptingType, OperationFind.class ) )
    {
      return refCache.getObject( adaptingType, OperationFind.class );
    }
    else if( adaptingType instanceof AnonymousObject )
    {
      @SuppressWarnings( "unchecked" )
      Map<String, Object> properties = (Map<String, Object>) adaptingType.defaultAdapt();
      Object payload = properties.get( "payload" );
      OperationType operationType = OperationType.valueOf( (String) properties.get( "operationType" ) );
      String table = (String) properties.get( "table" );
      String opResultId = (String) properties.get( "opResultId" );


      Operation operation = null;
      switch( operationType )
      {
        case CREATE:
          operation = new OperationCreate( operationType, table, opResultId, (Map<String, Object>) payload );
          refCache.addObject( adaptingType, OperationCreate.class, operation );
          break;
        case CREATE_BULK:
          operation = new OperationCreateBulk( operationType, table, opResultId, (List) payload );
          refCache.addObject( adaptingType, OperationCreateBulk.class, operation );
          break;
        case UPDATE:
          operation = new OperationUpdate( operationType, table, opResultId, (Map<String, Object>) payload );
          refCache.addObject( adaptingType, OperationUpdate.class, operation );
          break;
        case UPDATE_BULK:
          operation = new OperationUpdateBulk( operationType, table, opResultId, (UpdateBulkPayload) payload );
          refCache.addObject( adaptingType, OperationUpdateBulk.class, operation );
          break;
        case DELETE:
          operation = new OperationDelete( operationType, table, opResultId, payload );
          refCache.addObject( adaptingType, OperationDelete.class, operation );
          break;
        case DELETE_BULK:
          operation = new OperationDeleteBulk( operationType, table, opResultId, (DeleteBulkPayload) payload );
          refCache.addObject( adaptingType, OperationDeleteBulk.class, operation );
          break;
        case FIND:
          operation = new OperationFind( operationType, table, opResultId, payload );
          refCache.addObject( adaptingType, OperationFind.class, operation );
          break;
        case ADD_RELATION:
          operation = new OperationAddRelation( operationType, table, opResultId, (Relation) payload );
          refCache.addObject( adaptingType, OperationAddRelation.class, operation );
          break;
        case SET_RELATION:
          operation = new OperationSetRelation( operationType, table, opResultId, (Relation) payload );
          refCache.addObject( adaptingType, OperationSetRelation.class, operation );
          break;
        case DELETE_RELATION:
          operation = new OperationDeleteRelation( operationType, table, opResultId, (Relation) payload );
          refCache.addObject( adaptingType, OperationDeleteRelation.class, operation );
          break;
      }

      return operation;
    }
    else
    {
      throw new RuntimeException( "Can not create Operation from type " + adaptingType.getClass().getName() );
    }
  }

  @Override
  public boolean canAdapt( IAdaptingType adaptingType, Type type )
  {
    return false;
  }
}
