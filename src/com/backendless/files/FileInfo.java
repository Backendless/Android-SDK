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

package com.backendless.files;

/**
 * Created by baas on 12.08.15.
 */
public class FileInfo
{
  private String name;
  private long createdOn;
  private String publicUrl;
  private String url;
  private Integer size;

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public long getCreatedOn()
  {
    return createdOn;
  }

  public void setCreatedOn( long createOn )
  {
    this.createdOn = createOn;
  }

  public String getPublicUrl()
  {
    return publicUrl;
  }

  public void setPublicUrl( String publicUrl )
  {
    this.publicUrl = publicUrl;
  }

  public String getURL()
  {
    return url;
  }

  public void setURL( String url )
  {
    this.url = url;
  }

  public Integer getSize()
  {
    return size;
  }

  public void setSize( Integer size )
  {
    this.size = size;
  }
}
