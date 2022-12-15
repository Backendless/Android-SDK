package com.backendless.hive;

import weborb.types.IAdaptingType;
import weborb.util.IArgumentObjectFactory;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class ScanResultFactory implements IArgumentObjectFactory
{
  @Override
  public Object createObject( IAdaptingType iAdaptingType )
  {
    Map<String, Object> properties = (Map<String, Object>) iAdaptingType.defaultAdapt();
    Object[] keys = (Object[]) properties.get( "keys" );
    String cursor = (String) properties.get( "cursor" );
    return new ScanResult( (List)Arrays.asList( keys ), cursor );
  }

  @Override
  public boolean canAdapt( IAdaptingType iAdaptingType, Type type )
  {
    if( type instanceof Class )
    {
      return ((Class) (type)).getName().equals( ScanResult.class.getName() );
    }
    return false;
  }
}
