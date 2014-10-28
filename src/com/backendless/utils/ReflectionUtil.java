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

import com.backendless.async.callback.AsyncCallback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectionUtil
{
  public static <T> Type getCallbackGenericType( AsyncCallback<T> callback )
  {
    Type[] genericInterfaces = callback.getClass().getGenericInterfaces();
    Type asyncCallbackInterface = null;

    for( Type genericInterface : genericInterfaces )
    {
      if( !(genericInterface instanceof ParameterizedType) )
        continue;

      Type rawType = ((ParameterizedType) genericInterface).getRawType();
      if( rawType instanceof Class && AsyncCallback.class.isAssignableFrom( (Class) rawType ) )
      {
        asyncCallbackInterface = genericInterface;
        break;
      }
    }

    return ((ParameterizedType) asyncCallbackInterface).getActualTypeArguments()[ 0 ];
  }
}
