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

package com.backendless;

final class AuthKeys
{
  private final String applicationId;
  private final String secretKey;
  private final String version;

  AuthKeys( String applicationId, String secretKey, String version )
  {
    this.applicationId = applicationId;
    this.secretKey = secretKey;
    this.version = version;
  }

  String getApplicationId()
  {
    return applicationId;
  }

  String getSecretKey()
  {
    return secretKey;
  }

  String getVersion()
  {
    return version;
  }
}
