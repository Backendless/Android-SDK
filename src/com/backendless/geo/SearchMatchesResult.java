package com.backendless.geo;

public class SearchMatchesResult
{
  private Double matches;
  private GeoPoint geoPoint;

  public SearchMatchesResult()
  {
  }

  public Double getMatches()
  {
    return matches;
  }

  public void setMatches( Double matches )
  {
    this.matches = matches;
  }

  public GeoPoint getGeoPoint()
  {
    return geoPoint;
  }

  public void setGeoPoint( GeoPoint geoPoint )
  {
    this.geoPoint = geoPoint;
  }
}
