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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Created by mikhail on 6/17/15.
 */
public class CompressUtils
{
  private static final int DEFAULT_BUFFER_SIZE = 1024;

  public static byte[] compress(final byte[] data) throws IOException
  {
    return compress( data, DEFAULT_BUFFER_SIZE , Deflater.DEFAULT_COMPRESSION );
  }

  public static byte[] compress(final byte[] data, int bufferSize, int compressionLevel) throws IOException
  {
    if (data == null || data.length == 0) return new byte[0];

    final Deflater deflater = new Deflater();
    final ByteArrayOutputStream out = new ByteArrayOutputStream(data.length);

    deflater.setInput(data);
    deflater.setLevel( compressionLevel );

    deflater.finish();
    final byte[] buffer = new byte[bufferSize];
    while (!deflater.finished()) {
      out.write(buffer, 0, deflater.deflate(buffer));
    }

    return out.toByteArray();
  }

  public static byte[] decompress(final byte[] data) throws IOException, DataFormatException
  {
    return decompress( data, DEFAULT_BUFFER_SIZE );
  }

  public static byte[] decompress(final byte[] data, int bufferSize) throws IOException, DataFormatException
  {
    if (data == null || data.length == 0) return new byte[0];

    final Inflater inflater = new Inflater();
    inflater.setInput( data );

    final ByteArrayOutputStream out = new ByteArrayOutputStream(data.length);
    final byte[] buffer = new byte[bufferSize];

    while (!inflater.finished()) {
      out.write(buffer, 0, inflater.inflate(buffer));
    }

    return out.toByteArray();
  }

  public static byte[] stringToByteArray(String str)
  {

    String[] byteValues = str.split(",");
    byte[] bytes = new byte[byteValues.length];

    for (int i=0, len=bytes.length; i<len; i++) {
      bytes[i] = Byte.parseByte(byteValues[i].trim());
    }

    return bytes;
  }
}
