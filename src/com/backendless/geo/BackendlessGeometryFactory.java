package com.backendless.geo;

import com.backendless.persistence.Geometry;
import com.backendless.persistence.GeometryDTO;
import com.backendless.persistence.SpatialReferenceSystemEnum;
import weborb.reader.AnonymousObject;
import weborb.reader.NamedObject;
import weborb.reader.NullType;
import weborb.reader.ReferenceCache;
import weborb.types.IAdaptingType;
import weborb.util.IArgumentObjectFactory;

import java.lang.reflect.Type;
import java.util.Arrays;
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

      // If GeometryDTO
      if (geoJson != null)
      {
        String geomClass = (String) properties.get( "geomClass" );
        Integer srsId = (Integer) properties.get( "srsId" );

        Geometry geometry = new GeometryDTO( geomClass, srsId, geoJson ).toGeometry();
        refCache.addObject( adaptingType, GeometryDTO.class, geometry );
        return geometry;
      }

      Object[] coordinates = (Object[]) properties.get( "coordinates" );
      // If GeoJSON
      if( coordinates != null )
      {
        Integer srsId = (Integer) properties.get( "srsId" );
        SpatialReferenceSystemEnum srs = SpatialReferenceSystemEnum.valueBySRSId( srsId );
        String type = (String) properties.get( "type" );

        String geoJsonBuilder = "{\"type\":\"" + type + "\",\"coordinates\":" + jsonCoordinatePairs( type, coordinates ) + "}";
        Geometry geometry = Geometry.fromGeoJSON( geoJsonBuilder, srs );

        refCache.addObject( adaptingType, GeometryDTO.class, geometry );
        return geometry;
      }

      return null;
    }
    else
    {
      throw new RuntimeException( "Can not create BackendlessGeometry from type " + adaptingType.getClass().getName() );
    }
  }

  private String jsonCoordinatePairs( String type, Object[] coordinates )
  {
    switch( type )
    {
      case "Point":
        return Arrays.toString( coordinates );
      case "LineString":
        return createLineStringCoordinatePairs( coordinates );
      case "Polygon":
        return createPolygonCoordinatePairs( coordinates );
      default:
        throw new RuntimeException( "Can not create BackendlessGeometry, wrong Geometry type");
    }
  }

  private String createPolygonCoordinatePairs( Object[] coordinates )
  {
    StringBuilder sb = new StringBuilder();
    sb.append( '[' );

    for( Object object : coordinates )
      sb.append( createLineStringCoordinatePairs( (Object[]) object ) ).append( "," );

    sb.setCharAt( sb.length() - 1, ']' );
    return sb.toString();
  }

  private String createLineStringCoordinatePairs( Object[] coordinates )
  {
    StringBuilder sb = new StringBuilder();
    sb.append( '[' );

    for( Object object : coordinates )
      sb.append( Arrays.toString( (Object[]) object ) ).append( "," );

    sb.setCharAt( sb.length() - 1, ']' );
    return sb.toString();
  }

  @Override
  public boolean canAdapt( IAdaptingType adaptingType, Type type )
  {
    return false;
  }
}
