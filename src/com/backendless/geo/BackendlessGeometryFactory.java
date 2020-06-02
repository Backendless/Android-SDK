package com.backendless.geo;

import com.backendless.persistence.Geometry;
import com.backendless.persistence.GeometryDTO;
import weborb.reader.AnonymousObject;
import weborb.reader.NamedObject;
import weborb.reader.NullType;
import weborb.reader.ReferenceCache;
import weborb.types.IAdaptingType;
import weborb.util.IArgumentObjectFactory;

import java.lang.reflect.Type;
import java.util.Map;


public class BackendlessGeometryFactory implements IArgumentObjectFactory
{
  @Override
  public Object createObject( IAdaptingType adaptingType )
  {
    if( adaptingType instanceof NamedObject )
      adaptingType = ((NamedObject) adaptingType).getTypedObject();

    if( adaptingType.getClass() == NullType.class )
      return null;

    ReferenceCache refCache = ReferenceCache.getInstance();

    if( refCache.hasObject( adaptingType, GeometryDTO.class ) )
    {
      return refCache.getObject( adaptingType, GeometryDTO.class );
    }
    else if( adaptingType instanceof AnonymousObject )
    {
      @SuppressWarnings( "unchecked" )
      Map<String, Object> properties = (Map<String, Object>) adaptingType.defaultAdapt();
      String geoJson = (String) properties.get( "geoJson" );

      if (geoJson == null)
        return null;

      String geomClass = (String) properties.get( "geomClass" );
      Integer srsId = (Integer) properties.get( "srsId" );

      Geometry geometry = new GeometryDTO( geomClass, srsId, geoJson ).toGeometry();
      refCache.addObject( adaptingType, GeometryDTO.class, geometry );
      return geometry;
    }
    else
    {
      throw new RuntimeException( "Can not create BackendlessGeometry from type " + adaptingType.getClass().getName() );
    }
  }

  @Override
  public boolean canAdapt( IAdaptingType adaptingType, Type type )
  {
    return false;
  }
}
