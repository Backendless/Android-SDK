/*
 * ********************************************************************************************************************
 *  <p/>
 *  BACKENDLESS.COM CONFIDENTIAL
 *  <p/>
 *  ********************************************************************************************************************
 *  <p/>
 *  Copyright 2012 BACKENDLESS.COM. All Rights Reserved.
 *  <p/>
 *  NOTICE: All information contained herein is, and remains the property of Backendless.com and its suppliers,
 *  if any. The intellectual and technical concepts contained herein are proprietary to Backendless.com and its
 *  suppliers and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret
 *  or copyright law. Dissemination of this information or reproduction of this material is strictly forbidden
 *  unless prior written permission is obtained from Backendless.com.
 *  <p/>
 *  ********************************************************************************************************************
 */

package com.backendless.geo;

import java.util.List;

/**
 * Created by baas on 01.04.15.
 */
public class GeoFence
{
  private String objectId;
  private String geofenceName;
  private boolean isActive;
  private long onStayDuration;
  private FenceType type;
  private List<GeoPoint> nodes;
  private GeoPoint nwPoint;
  private GeoPoint sePoint;

  public String getObjectId()
  {
    return objectId;
  }

  public void setObjectId( String objectId )
  {
    this.objectId = objectId;
  }

  public String getGeofenceName()
  {
    return geofenceName;
  }

  public void setGeofenceName( String geofenceName )
  {
    this.geofenceName = geofenceName;
  }

  public boolean isActive()
  {
    return isActive;
  }

  public void setActive( boolean isActive )
  {
    this.isActive = isActive;
  }

  public long getOnStayDuration()
  {
    return onStayDuration;
  }

  public void setOnStayDuration( long onStayDuration )
  {
    this.onStayDuration = onStayDuration;
  }

  public FenceType getType()
  {
    return type;
  }

  public void setType( FenceType type )
  {
    this.type = type;
  }

  public List<GeoPoint> getNodes()
  {
    return nodes;
  }

  public void setNodes( List<GeoPoint> nodes )
  {
    this.nodes = nodes;
  }

  public GeoPoint getNwPoint()
  {
    return nwPoint;
  }

  public void setNwPoint( GeoPoint nwPoint )
  {
    this.nwPoint = nwPoint;
  }

  public GeoPoint getSePoint()
  {
    return sePoint;
  }

  public void setSePoint( GeoPoint sePoint )
  {
    this.sePoint = sePoint;
  }
}
