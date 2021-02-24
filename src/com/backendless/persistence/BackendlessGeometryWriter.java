package com.backendless.persistence;

import com.backendless.persistence.Geometry;
import com.backendless.persistence.GeometryDTO;
import weborb.writer.IProtocolFormatter;
import weborb.writer.ITypeWriter;
import weborb.writer.MessageWriter;

import java.io.IOException;


public class BackendlessGeometryWriter implements ITypeWriter
{
  @Override
  public void write( Object o, IProtocolFormatter iProtocolFormatter ) throws IOException
  {
    Geometry geometry = (Geometry) o;
    GeometryDTO geometryDTO = new GeometryDTO(geometry.getClass().getName(), geometry.getSRS().getSRSId(), geometry.asGeoJSON());
    MessageWriter.writeObject(geometryDTO, iProtocolFormatter);
  }

  @Override
  public boolean isReferenceableType()
  {
    return false;
  }
}
