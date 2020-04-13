package com.backendless.geo;

import com.backendless.persistence.Geometry;
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
    MessageWriter.writeObject( geometry.asWKT(), iProtocolFormatter );
  }

  @Override
  public boolean isReferenceableType()
  {
    return false;
  }
}
