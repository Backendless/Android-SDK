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

package com.backendless.io;

import weborb.writer.IProtocolFormatter;
import weborb.writer.ITypeWriter;

import java.io.IOException;

public class DoubleWriter implements ITypeWriter
{
  public void write( Object object, IProtocolFormatter formatter ) throws IOException
  {
    Number numValue = (Number) object;

    if( numValue instanceof Double && ((Double) numValue).isNaN() )
      numValue = 0;

    formatter.writeNumber( numValue.doubleValue() );
  }

  public boolean isReferenceableType()
  {
    return false;
  }
}
