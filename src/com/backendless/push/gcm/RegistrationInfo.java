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

package com.backendless.push.gcm;

public class RegistrationInfo
{
  private String deviceToken = "";
  private String registrationId = "";
  private Long registrationExpiration;

  public String getDeviceToken()
  {
    return deviceToken;
  }

  public void setDeviceToken( String gcmDeviceToken )
  {
    this.deviceToken = gcmDeviceToken;
  }

  public String getRegistrationId()
  {
    return registrationId;
  }

  public void setRegistrationId( String registrationId )
  {
    this.registrationId = registrationId;
  }

  public Long getRegistrationExpiration()
  {
    return registrationExpiration;
  }

  public void setRegistrationExpiration( Long registrationExpiration )
  {
    this.registrationExpiration = registrationExpiration;
  }
}
