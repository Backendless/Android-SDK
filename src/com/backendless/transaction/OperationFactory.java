package com.backendless.transaction;

import weborb.reader.AnonymousObject;
import weborb.reader.NamedObject;
import weborb.reader.ReferenceCache;
import weborb.types.IAdaptingType;
import weborb.util.IArgumentObjectFactory;

import java.lang.reflect.Type;
import java.util.Map;

public abstract class OperationFactory<T> implements IArgumentObjectFactory
{
  @Override
  public Object createObject( IAdaptingType adaptingType )
  {
    if( adaptingType instanceof NamedObject )
      adaptingType = ((NamedObject) adaptingType).getTypedObject();

    if( adaptingType.getClass().getName().equals( "weborb.reader.NullType" ) )
      return null;

    ReferenceCache refCache = ReferenceCache.getInstance();

    if( refCache.hasObject( adaptingType, getClazz() ) )
    {
      return refCache.getObject( adaptingType, getClazz() );
    }
    else if( adaptingType instanceof AnonymousObject )
    {
      @SuppressWarnings( "unchecked" )
      Map<String, Object> properties = (Map<String, Object>) adaptingType.defaultAdapt();
      Object payload = properties.get( "payload" );
      OperationType operationType = OperationType.valueOf( (String) properties.get( "operationType" ) );
      String table = (String) properties.get( "table" );
      String opResultId = (String) properties.get( "opResultId" );

      Operation operation = createOperation( operationType, table, opResultId, payload );
      refCache.addObject( adaptingType, getClazz(), operation );
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

  protected abstract Class<T> getClazz();

  protected abstract Operation createOperation( OperationType operationType, String table, String opResultId, Object payload );
}
