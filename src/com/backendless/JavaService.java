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

class JavaService implements IBackendlessService
{
  private AuthKeys authKeys;
  private Map<String, String> headers;

  @Override
  public void setAuthKeys( String applicationId, String secretKey, String version )
  {
    authKeys = new AuthKeys( applicationId, secretKey, version );
  }

  @Override
  public String getApplicationId()
  {
    return authKeys.getApplicationId();
  }

  @Override
  public String getSecretKey()
  {
    return authKeys.getSecretKey();
  }

  @Override
  public String getVersion()
  {
    return authKeys.getVersion();
  }

  @Override
  public Map<String, String> getHeaders()
  {
    return headers;
  }

  @Override
  public void setHeaders( Map<String, String> headers )
  {
    this.headers = headers;
  }

  @Override
  public void cleanHeaders()
  {
    headers = null;
  }

  final static class Init implements IBackendlessService.Init
  {
    @Override
    public void initService( Object arg, IServiceCreatedCallback callback )
    {
      callback.onServiceConnected( new JavaService() );
    }
  }
}
