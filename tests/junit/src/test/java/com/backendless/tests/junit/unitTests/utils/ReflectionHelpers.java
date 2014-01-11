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

package com.backendless.tests.junit.unitTests.utils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created with IntelliJ IDEA.
 * User: Michael Cheremuhin
 * Date: 10/17/13
 * Time: 23:52
 */
public class ReflectionHelpers
{
  private static final File androidJar;

  static
  {
    File candidate = new File( "libs/android.jar" );
    if( !candidate.exists() )
      candidate = new File( "../../../../sdk/android/libs/android.jar" );
    androidJar = candidate;
  }

  public static void loadAndroidSdk() throws Exception
  {
    URL androiJarUrl = androidJar.toURI().toURL();
    URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
    Class urlClass = URLClassLoader.class;
    Method method = urlClass.getDeclaredMethod( "addURL", new Class[] { URL.class } );
    method.setAccessible( true );
    method.invoke( urlClassLoader, new Object[] { androiJarUrl } );
  }

  public static Object getObjectInstanceWithPrivateConstructor( String fullName, Object... args ) throws Exception
  {
    Class aClass = Class.forName( fullName );
    Constructor aClassDeclaredConstructor = aClass.getDeclaredConstructor();
    aClassDeclaredConstructor.setAccessible( true );

    return aClassDeclaredConstructor.newInstance( args );
  }

  public static Method getObjectPrivateMethod( Class aClass, String methodName,
                                               Class... args ) throws NoSuchMethodException
  {
    Method aClassDeclaredMethod = aClass.getDeclaredMethod( methodName, args );
    aClassDeclaredMethod.setAccessible( true );

    return aClassDeclaredMethod;
  }
}
