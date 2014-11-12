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

import android.content.Context;
import com.backendless.Backendless;
import com.backendless.exceptions.ExceptionMessage;

public class UserTokenStorageFactory
{
  static final String key = "user-token";
  private static AndroidUserTokenStorage androidUserTokenStorage;
  private static UserTokenStorageFactory instance = new UserTokenStorageFactory();

  public static UserTokenStorageFactory instance()
  {
    return instance;
  }

  private UserTokenStorageFactory()
  {
  }

  public void init( Context context )
  {
    androidUserTokenStorage = new AndroidUserTokenStorage( context );
  }

  public IStorage<String> getStorage()
  {
    if( Backendless.isAndroid() && androidUserTokenStorage == null )
      throw new IllegalArgumentException( ExceptionMessage.INIT_BEFORE_USE );

    if( Backendless.isAndroid() )
      return androidUserTokenStorage;

    if( Backendless.isCodeRunner() )
      return CodeRunnerUserTokenStorage.instance();

    return JavaUserTokenStorage.instance();
  }
}
