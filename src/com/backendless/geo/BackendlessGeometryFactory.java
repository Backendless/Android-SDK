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
  public Object createObject( IAdaptingType iAdaptingType )
  {
    if( iAdaptingType instanceof NamedObject )
      iAdaptingType = ((NamedObject) iAdaptingType).getTypedObject();

    if( iAdaptingType.getClass() == NullType.class )
      return null;

    ReferenceCache refCache = ReferenceCache.getInstance();

    if( refCache.hasObject( iAdaptingType, GeometryDTO.class ) )
    {
      return refCache.getObject( iAdaptingType, GeometryDTO.class );
    }
    else
    {
      if( iAdaptingType instanceof AnonymousObject )
      {
        @SuppressWarnings( "unchecked" )
        Map<String, Object> properties = (Map<String, Object>) iAdaptingType.defaultAdapt();
        String geoJson = (String) properties.get( "geoJson" );

        if (geoJson == null)
          return null;

        String geomClass = (String) properties.get( "geomClass" );
        Integer srsId = (Integer) properties.get( "srsId" );

        Geometry geometry = new GeometryDTO( geomClass, srsId, geoJson ).toGeometry();
        refCache.addObject( iAdaptingType, GeometryDTO.class, geometry );
        return geometry;
      }
      else
        throw new RuntimeException( "unknown type" );
    }
  }

  @Override
  public boolean canAdapt( IAdaptingType iAdaptingType, Type type )
  {
    return false;
  }
}
