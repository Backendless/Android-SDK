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

package com.backendless.geo;

import weborb.types.IAdaptingType;
import weborb.util.io.ISerializer;
import weborb.util.io.Serializer;

public class LocationListenerInfo
{
  public String className;
  public byte[] classData;

  public LocationListenerInfo()
  {

  }

  public LocationListenerInfo( IBackendlessLocationListener listener ) throws Exception
  {
    classData = Serializer.toBytes( listener, ISerializer.AMF3 );
    className = listener.getClass().getName();
  }

  protected IBackendlessLocationListener getBackendlessListener()
  {
    try
    {
      IAdaptingType listenerObj = (IAdaptingType) Serializer.fromBytes( classData, ISerializer.AMF3, true );
      return (IBackendlessLocationListener) listenerObj.adapt( Class.forName( className ) );
    }
    catch( Throwable t )
    {
      return null;
    }
  }
}
