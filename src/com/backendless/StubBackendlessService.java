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

final class StubBackendlessService implements IBackendlessService
{
  private AuthKeys authKeys;
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
  public void setHeaders( Map<String, String> headers )
  {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public Map<String, String> getHeaders()
  {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void cleanHeaders()
  {
    //To change body of implemented methods use File | Settings | File Templates.
  }
}
