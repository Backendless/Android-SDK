package com.backendless.examples.endless.tagging;

public class Comment

{
  private String name;
  private String message;
  private String geoPointId;

  public Comment()
  {
  }

  public Comment( String name, String message, String geoPointId )
  {
    this.name = name;
    this.message = message;
    this.geoPointId = geoPointId;
  }

  public String getName()
  {
    return name;
  }

  public void setName()
  {
    this.name = name;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

  public String getGeoPointId()
  {
    return geoPointId;
  }

  public void setGeoPointId( String geoPointId )
  {
    this.geoPointId = geoPointId;
  }
};
