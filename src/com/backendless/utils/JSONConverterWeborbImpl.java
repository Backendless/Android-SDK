package com.backendless.utils;

import com.backendless.util.JSONConverter;
import weborb.util.io.ISerializer;
import weborb.util.io.Serializer;

import java.io.IOException;


public class JSONConverterWeborbImpl implements JSONConverter
{
  @Override
  public <T> T readObject( String jsonString, Class<T> typeClass )
  {
    try
    {
      return (T) Serializer.fromBytes( jsonString.getBytes(), Serializer.JSON, false );
    }
    catch( IOException e )
    {
      throw new IllegalStateException( e );
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
