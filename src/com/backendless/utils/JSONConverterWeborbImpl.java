package com.backendless.utils;

import com.backendless.util.JSONConverter;
import weborb.exceptions.AdaptingException;
import weborb.types.IAdaptingType;
import weborb.util.io.ISerializer;
import weborb.util.io.Serializer;

import java.io.IOException;
import java.lang.reflect.Type;


public class JSONConverterWeborbImpl implements JSONConverter
{
  @Override
  public <T> T readObject( String jsonString, Type type )
  {
    try
    {
      IAdaptingType adaptingType = (IAdaptingType) Serializer.fromBytes( jsonString.getBytes(), Serializer.JSON, true );
      return (T) adaptingType.adapt( type );
    }
    catch( IOException e )
    {
      throw new IllegalStateException( e );
    }
    catch( AdaptingException e )
    {
      throw new RuntimeException( e );
    }
  }

  @Override
  public String writeObject( Object obj )
  {
    try
    {
      byte[] bytes = Serializer.toBytes( obj, ISerializer.JSON );
      return new String( bytes );
    }
    catch( Exception e )
    {
      throw new IllegalStateException( e );
    }
  }
}
