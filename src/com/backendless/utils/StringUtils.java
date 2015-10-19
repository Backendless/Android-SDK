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

package com.backendless.utils;

/**
 * Created by Artur Dzidzoiev on 10/9/15.
 */
public class StringUtils
{

  public static boolean isNullOrEmpty( String string )
  {
    return string == null || string.isEmpty();
  }

  public static void checkNotNullOrEmpty( String string, String errorMessage )
  {
    if( isNullOrEmpty( string ) )
      throw new IllegalArgumentException( errorMessage );
  }
}
