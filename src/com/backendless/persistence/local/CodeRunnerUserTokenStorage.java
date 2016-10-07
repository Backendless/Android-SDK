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

package com.backendless.persistence.local;

import com.backendless.servercode.InvocationContext;

class CodeRunnerUserTokenStorage implements IStorage<String>
{
  private static final CodeRunnerUserTokenStorage instance = new CodeRunnerUserTokenStorage();

  public static CodeRunnerUserTokenStorage instance()
  {
    return instance;
  }

  private CodeRunnerUserTokenStorage()
  {

  }

  @Override
  public String get()
  {
    return InvocationContext.getUserToken();
  }

  @Override
  public void set( String value )
  {
    InvocationContext.setUserToken( value );
  }
}
