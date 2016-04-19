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

package com.backendless.examples.fileservice.filedemo;

public class ImageEntity
{
  private long uploaded;
  private String url;

  public ImageEntity()
  {
  }

  public ImageEntity( long uploaded, String url )
  {
    this.uploaded = uploaded;
    this.url = url;
  }

  public long getUploaded()
  {
    return uploaded;
  }

  public void setUploaded( long uploaded )
  {
    this.uploaded = uploaded;
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl( String url )
  {
    this.url = url;
  }
}
