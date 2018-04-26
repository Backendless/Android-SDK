package com.backendless.rt.messaging.adapt;

import com.backendless.utils.WeborbSerializationHelper;
import weborb.exceptions.AdaptingException;
import weborb.reader.StringType;
import weborb.types.IAdaptingType;

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
 *         AT: 4:43 PM
 **********************************************************************************************************************/
public class StringAdapter implements IMessagingAdapter
{
  @Override
  public Object adapt( IAdaptingType message, Class clazz ) throws AdaptingException
  {
    // if the message is a string, it might be a JSON object. In that case, try adapting it
    try
    {
      return WeborbSerializationHelper.deserialize( ((String) message.adapt( String.class )).getBytes() );
    }
    catch( Throwable t )
    {
      // ignore
    }

    return null;
  }
}
