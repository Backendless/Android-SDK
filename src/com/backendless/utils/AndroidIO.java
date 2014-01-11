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

import android.content.Context;

import java.io.*;
import java.util.Map;

public final class AndroidIO
{
  public static String mapToString( Map<String, String> map ) throws IOException
  {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ObjectOutputStream objOut = new ObjectOutputStream( byteArrayOutputStream );
    objOut.writeObject( map );
    objOut.close();

    return byteArrayOutputStream.toString();
  }

  public static void writeToFile( Context context, String fileName, Map<String, String> values )
  {
    try
    {
      String rawValues = mapToString( values );
      writeToFile( context, fileName, rawValues );
    }
    catch( Exception e )
    {/*It can't be handled and we do not expect the user to handle it*/}
  }

  public static void writeToFile( Context context, String fileName, String value )
  {
    try
    {
      FileOutputStream outputStream = context.openFileOutput( fileName, Context.MODE_PRIVATE );
      outputStream.write( value.getBytes() );
      outputStream.close();
    }
    catch( Exception e )
    {/*It can't be handled and we do not expect the user to handle it*/}
  }

  public static Map mapFromString( String rawMap ) throws IOException, ClassNotFoundException
  {
    ObjectInputStream objectInputStream = new ObjectInputStream( new ByteArrayInputStream( rawMap.getBytes() ) );
    Map result = (Map) objectInputStream.readObject();
    objectInputStream.close();

    return result;
  }

  public static Map readMapFromFile( Context context, String fileName )
  {
    try
    {
      String rawMap = readFromFile( context, fileName );

      return mapFromString( rawMap );
    }
    catch( Exception e )
    {/*It can't be handled and we do not expect the user to handle it*/}

    return null;
  }

  public static String readFromFile( Context context, String fileName )
  {
    try
    {
      FileInputStream inputStream = context.openFileInput( fileName );
      String result = new DataInputStream( inputStream ).readLine();
      inputStream.close();

      return result;
    }
    catch( Exception ee )
    {/*It can't be handled and we do not expect the user to handle it*/}

    return null;
  }
}
