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

import java.util.Map;
import java.util.UUID;

interface IBackendlessService
{
  static final String TAG = "BackendlessService";

  void setAuthKeys( String applicationId, String secretKey, String version );

  String getApplicationId();

  String getSecretKey();

  String getVersion();

  void setHeaders( Map<String, String> headers );

  Map<String,String> getHeaders();

  void cleanHeaders();

  static enum Type
  {
    APPLICATION_ID, SECRET_KEY, VERSION, HEADERS;

    String name64()
    {
      return UUID.nameUUIDFromBytes( this.name().getBytes() ).toString();
    }
  }

  static interface Init
  {
    void initService( Object arg, IServiceCreatedCallback callback );
  }
}
