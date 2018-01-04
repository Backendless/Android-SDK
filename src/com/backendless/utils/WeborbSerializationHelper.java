package com.backendless.utils;

import weborb.exceptions.AdaptingException;
import weborb.reader.AnonymousObject;
import weborb.reader.CacheableAdaptingTypeWrapper;
import weborb.types.IAdaptingType;
import weborb.util.io.ISerializer;
import weborb.util.io.Serializer;

import java.util.logging.Logger;

public class WeborbSerializationHelper
{
  private static final Logger logger = Logger.getLogger( "WeborbSerializationHelper" );

  public static Object[] serialize( Object args )
  {
    try
    {
      return new Object[] { Serializer.toBytes( args, ISerializer.JSON ) };
    }
    catch( Exception e )
    {
      logger.severe( "weborb serialization error " + e.toString() );
      throw new RuntimeException( e );
    }
  }

  public static IAdaptingType deserialize( Object arg )
  {
    try
    {
      final IAdaptingType iAdaptingType = (IAdaptingType) Serializer.fromBytes( (byte[]) arg, ISerializer.JSON, true );
      if( iAdaptingType instanceof CacheableAdaptingTypeWrapper )
      {
        return ((CacheableAdaptingTypeWrapper) iAdaptingType).getType();
      }
      else
        return iAdaptingType;
    }
    catch( Exception e )
    {
      logger.severe( "weborb deserialization error " + e.toString() );
      throw new RuntimeException( e );
    }
  }

  public static String asString( IAdaptingType object, String key )
  {
    final AnonymousObject anonymousObject = cast( object );

    return asString( anonymousObject, key );
  }

  private static AnonymousObject cast( IAdaptingType object )
  {
    final AnonymousObject anonymousObject;
    if( object instanceof AnonymousObject )
    {
      anonymousObject = (AnonymousObject) object;
    }
    else if( object instanceof CacheableAdaptingTypeWrapper )
    {
      anonymousObject = (AnonymousObject) ((CacheableAdaptingTypeWrapper) object).getType();
    }
    else
    {
      throw new IllegalArgumentException( "object should be or contains AnonymousObject" );
    }
    return anonymousObject;
  }

  public static String asString( AnonymousObject object, String key )
  {
    try
    {
      final IAdaptingType adaptingType = (IAdaptingType) object.getProperties().get( key );
      return adaptingType == null ? null : (String) adaptingType.adapt( String.class );
    }
    catch( AdaptingException e )
    {
      logger.severe( "get weborb string error " + e.getMessage() );
      throw new RuntimeException( e );
    }
  }

  public static Object asObject( AnonymousObject object, String key )
  {
    final IAdaptingType adaptingType = (IAdaptingType) object.getProperties().get( key );
    return adaptingType == null ? null : adaptingType.defaultAdapt();
  }

  public static IAdaptingType asAdaptingType( AnonymousObject object, String key )
  {
    return (IAdaptingType) object.getProperties().get( key );
  }

  public static IAdaptingType asAdaptingType( IAdaptingType object, String key )
  {
    final AnonymousObject anonymousObject = cast( object );
    return (IAdaptingType) anonymousObject.getProperties().get( key );
  }
}
