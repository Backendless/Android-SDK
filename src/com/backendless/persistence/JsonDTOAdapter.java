package com.backendless.persistence;

import com.backendless.util.JSONUtil;
import weborb.exceptions.AdaptingException;
import weborb.reader.ReferenceCache;
import weborb.types.ICacheableAdaptingType;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class JsonDTOAdapter implements ICacheableAdaptingType
{
  private String rawJsonString;

  public JsonDTOAdapter()
  {

  }

  public JsonDTOAdapter( String rawJsonString )
  {
    this.rawJsonString = rawJsonString;
  }

  public String getRawJsonString()
  {
    return rawJsonString;
  }

  public void setRawJsonString( String value )
  {
    this.rawJsonString = value;
  }

  @Override
  public boolean equals( Object o )
  {
    if( this == o )
      return true;

    if( !(o instanceof JsonDTO) )
      return false;
    
    JsonDTOAdapter jsonDTO = (JsonDTOAdapter) o;
    return Objects.equals( rawJsonString, jsonDTO.rawJsonString );
  }

  @Override
  public int hashCode()
  {
    return Objects.hash( rawJsonString );
  }

  @Override
  public Class getDefaultType()
  {
    return Map.class;
  }

  @Override
  public Object defaultAdapt()
  {
    return defaultAdapt( null );
  }

  @Override
  public Object adapt( Type type ) throws AdaptingException
  {
    return adapt( type, null );
  }

  @Override
  public boolean canAdaptTo( Type type )
  {
    return true;
  }

  @Override
  public Object defaultAdapt( ReferenceCache referenceCache )
  {
    try
    {
      return adapt( HashMap.class, referenceCache );
    }
    catch( AdaptingException exception )
    {
      throw new RuntimeException( "unable to adapt JSON value to HashMap" );
    }
  }

  @Override
  public Object adapt( Type type, ReferenceCache referenceCache ) throws AdaptingException
  {
    if( getRawJsonString() == null )
      return null;

    Object result = JSONUtil.getJsonConverter().readObject( getRawJsonString(), (Class) type );

    if( referenceCache != null )
      referenceCache.addObject( this, JsonDTO.class, result );

    return result;
  }
}