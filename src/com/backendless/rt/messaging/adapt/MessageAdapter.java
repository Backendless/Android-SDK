package com.backendless.rt.messaging.adapt;

import com.backendless.utils.WeborbSerializationHelper;
import weborb.exceptions.AdaptingException;
import weborb.reader.StringType;
import weborb.types.IAdaptingType;

import java.util.HashMap;
import java.util.Map;

/**********************************************************************************************************************
 *
 * BACKENDLESS.COM CONFIDENTIAL
 *
 **********************************************************************************************************************
 *
 *  Copyright 2012 BACKENDLESS.COM. All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of Backendless.com and its suppliers,
 * if any.  The intellectual and technical concepts contained herein are proprietary to Backendless.com and its 
 * suppliers and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret
 * or copyright law. Dissemination of this information or reproduction of this material is strictly forbidden 
 * unless prior written permission is obtained from Backendless.com.
 *
 * CREATED ON: 4/20/18 
 *         AT: 4:38 PM
 **********************************************************************************************************************/
public class MessageAdapter
{
  private static HashMap<Class, IMessagingAdapter> adapters;
  private static IMessagingAdapter DEFAULT_ADAPTER = new DefaultAdapter();

  static
  {
    adapters = new HashMap<>();
    adapters.put( String.class, new StringAdapter() );

    IMessagingAdapter hashMapAdapter = new HashMapAdapter();
    adapters.put( Map.class, hashMapAdapter );
    adapters.put( HashMap.class, hashMapAdapter );
  }
  public static Object adapt( IAdaptingType message, Class clazz ) throws AdaptingException
  {
    // check if there is a custom adapter for a particular class.
    IMessagingAdapter messagingAdapter = adapters.get( clazz );
    Object result = null;

    if( messagingAdapter != null )
      result = messagingAdapter.adapt( message, clazz );

    // if there is no custom adapter or the assigner adaptor
    // could not handle the message delegate to the default one
    if( result == null )
      result = DEFAULT_ADAPTER.adapt( message, clazz );
    
    return result;
  }
}
